package th.com.bloomcode.paymentservice.service.payment.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.UnBlockDocumentLog;
import th.com.bloomcode.paymentservice.model.request.SearchUnBlockDocumentLogRequest;
import th.com.bloomcode.paymentservice.model.response.OmDetailReportResponse;
import th.com.bloomcode.paymentservice.model.response.OmItemReportResponse;
import th.com.bloomcode.paymentservice.model.response.OmReportResponse;
import th.com.bloomcode.paymentservice.repository.payment.UnBlockDocumentLogRepository;
import th.com.bloomcode.paymentservice.service.payment.UnBlockDocumentLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class UnBlockDocumentLogServiceImpl implements UnBlockDocumentLogService {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final UnBlockDocumentLogRepository unBlockDocumentLogRepository;
    private final JdbcTemplate jdbcTemplate;

    public UnBlockDocumentLogServiceImpl(UnBlockDocumentLogRepository unBlockDocumentLogRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.unBlockDocumentLogRepository = unBlockDocumentLogRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public UnBlockDocumentLog save(UnBlockDocumentLog unBlockDocumentLog) {
        logger.info("==== Save unBlockDocumentLog : {}", unBlockDocumentLog);
        if (null == unBlockDocumentLog.getId() || 0 == unBlockDocumentLog.getId()) {
            unBlockDocumentLog.setId(SqlUtil.getNextSeries(jdbcTemplate, UnBlockDocumentLog.TABLE_NAME + UnBlockDocumentLog.SEQ, null));
            unBlockDocumentLog.setCreated(new Timestamp(System.currentTimeMillis()));
            unBlockDocumentLog.setCreatedBy("SYSTEM");
        } else {
            unBlockDocumentLog.setUpdated(new Timestamp(System.currentTimeMillis()));
            unBlockDocumentLog.setUpdatedBy("IDEM");
        }
        return unBlockDocumentLogRepository.save(unBlockDocumentLog);
    }

    @Override
    public UnBlockDocumentLog findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(String companyCode, String originalDocumentNo, String originalFiscalYear) {
        List<UnBlockDocumentLog> unBlockDocumentLogList = unBlockDocumentLogRepository.findUnblockDocumentByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYear(
                companyCode, originalDocumentNo,
                originalFiscalYear);
        if (unBlockDocumentLogList.size() > 0) {
            return unBlockDocumentLogList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<Result<List<UnBlockDocumentLog>>> findLogByCondition(SearchUnBlockDocumentLogRequest searchUnBlockDocumentLogRequest) {
        Result<List<UnBlockDocumentLog>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnBlockDocumentLog> unBlockDocumentLogList = unBlockDocumentLogRepository.findLogByCondition(searchUnBlockDocumentLogRequest);
            result.setStatus(HttpStatus.OK.value());
            result.setData(unBlockDocumentLogList);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<OmReportResponse>> findReportUnblockByCondition(SearchUnBlockDocumentLogRequest searchUnBlockDocumentLogRequest) {
        Result<OmReportResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<UnBlockDocumentLog> unBlockDocumentLogList = unBlockDocumentLogRepository.findLogByCondition(searchUnBlockDocumentLogRequest);


            logger.info("unBlockDocumentLogList : {}", unBlockDocumentLogList.size());

            Map<String, List<UnBlockDocumentLog>> groupsByCompCode = unBlockDocumentLogList.stream()
                    .collect(Collectors.groupingBy(UnBlockDocumentLog::getCompanyCode,
                            Collectors.mapping((UnBlockDocumentLog p) -> p, toList())));

            logger.info("groupsByCompCode : {}", groupsByCompCode);


            final BigDecimal[] totalAmount = {new BigDecimal(0)};
            final BigDecimal[] totalTaxFee = {new BigDecimal(0)};
            final BigDecimal[] totalNetPrice = {new BigDecimal(0)};

            OmReportResponse reportResponse = new OmReportResponse();
            List<OmItemReportResponse> items = new ArrayList<>();
            groupsByCompCode.forEach((k, v) -> {
                OmItemReportResponse itemResponse = new OmItemReportResponse();


                List<OmDetailReportResponse> detail = new ArrayList<>();

                BigDecimal sumAmount = BigDecimal.ZERO;
                BigDecimal sumTaxFee = BigDecimal.ZERO;
                BigDecimal sumNetPrice = BigDecimal.ZERO;

                for (UnBlockDocumentLog unBlockDocumentLog : v) {
                    OmDetailReportResponse detailResponse = new OmDetailReportResponse();
                    detailResponse.setStatus(unBlockDocumentLog.getValueOld());
                    detailResponse.setUnblockDate(unBlockDocumentLog.getUnblockDate());
                    detailResponse.setCompanyCode(unBlockDocumentLog.getCompanyCode());
                    detailResponse.setArea(unBlockDocumentLog.getFiArea());
                    detailResponse.setPaymentCenter(unBlockDocumentLog.getPaymentCenter());
                    detailResponse.setDocType(unBlockDocumentLog.getDocumentType());
                    detailResponse.setFiscalYear(unBlockDocumentLog.getOriginalFiscalYear());
                    detailResponse.setAccDocNo(unBlockDocumentLog.getOriginalDocumentNo());
                    detailResponse.setPostDate(unBlockDocumentLog.getDateAcct());
                    detailResponse.setPaymentMethod(unBlockDocumentLog.getPaymentMethod());


                    BigDecimal amount = unBlockDocumentLog.getAmount() == null ? new BigDecimal(0) : new BigDecimal(unBlockDocumentLog.getAmount().toString());
                    detailResponse.setAmount(amount);

                    BigDecimal wtxAmount = unBlockDocumentLog.getWtxAmount() == null ? new BigDecimal(0) : new BigDecimal(unBlockDocumentLog.getWtxAmount().toString());
                    BigDecimal wtxAmountP = unBlockDocumentLog.getWtxAmountP() == null ? new BigDecimal(0) : new BigDecimal(unBlockDocumentLog.getWtxAmountP().toString());

                    detailResponse.setTaxFee(wtxAmount.add(wtxAmountP));
                    detailResponse.setNetPrice(amount.subtract(wtxAmount).subtract(wtxAmountP));
                    detailResponse.setVendor(unBlockDocumentLog.getVendor());
                    detailResponse.setVendorName(unBlockDocumentLog.getVendorName());
                    detail.add(detailResponse);

                    sumAmount = sumAmount.add(amount);
                    sumTaxFee = sumTaxFee.add(wtxAmount.add(wtxAmountP));
                    sumNetPrice = sumNetPrice.add(amount.subtract(wtxAmount).subtract(wtxAmountP));

                }

//                CompCode compCode = companyCodeService.findOneByValueCode(k);
//
//                itemResponse.setCompanyCode(compCode.getValueCode());
//                itemResponse.setCompanyName(compCode.getName());
                itemResponse.setSumAmount(sumAmount);
                itemResponse.setSumTaxFee(sumTaxFee);
                itemResponse.setSumNetPrice(sumNetPrice);

                detail.stream()
                        .sorted((o1, o2) -> o1.getAccDocNo().compareTo(o2.getAccDocNo()))
                        .collect(toList());
                itemResponse.setDetail(detail);
                itemResponse.setSumDocument(v.size());

                items.add(itemResponse);
                totalAmount[0] = totalAmount[0].add(sumAmount);
                totalTaxFee[0] = totalTaxFee[0].add(sumTaxFee);
                totalNetPrice[0] = totalNetPrice[0].add(sumNetPrice);


            });
            reportResponse.setItems(items);
            reportResponse.setTotalAmount(totalAmount[0]);
            reportResponse.setTotalTaxFee(totalTaxFee[0]);
            reportResponse.setTotalNetPrice(totalNetPrice[0]);

            if (reportResponse != null) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(reportResponse);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void saveBatch(List<UnBlockDocumentLog> unBlockDocumentLogs) {
        unBlockDocumentLogRepository.saveBatch(unBlockDocumentLogs);
    }

    @Override
    public void updateBatch(List<UnBlockDocumentLog> unBlockDocumentLogs) {
        unBlockDocumentLogRepository.updateBatch(unBlockDocumentLogs);
    }

    @Override
    public void updateStatus(String companyCode, String originalDocumentNo, String originalFiscalYear, String idemStatus, String newValue, String username, Timestamp updateDate)  {
        unBlockDocumentLogRepository.updateStatus(companyCode, originalDocumentNo, originalFiscalYear, idemStatus,newValue, username, updateDate);
    }
}
