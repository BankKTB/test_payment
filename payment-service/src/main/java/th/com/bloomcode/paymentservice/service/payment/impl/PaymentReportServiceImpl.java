package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.VendorReport;
import th.com.bloomcode.paymentservice.model.payment.dto.DuplicatePaymentReportResponse;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReport;
import th.com.bloomcode.paymentservice.model.payment.dto.PaymentReportPaging;
import th.com.bloomcode.paymentservice.model.request.DuplicatePaymentReport;
import th.com.bloomcode.paymentservice.model.response.*;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentReportRepository;
import th.com.bloomcode.paymentservice.repository.payment.VendorReportRepository;
import th.com.bloomcode.paymentservice.service.payment.PaymentReportService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class PaymentReportServiceImpl implements PaymentReportService {
    private final PaymentReportRepository paymentReportRepository;
    private final VendorReportRepository vendorReportRepository;
    private final PaymentAliasRepository paymentAliasRepository;

    public PaymentReportServiceImpl(PaymentReportRepository paymentReportRepository, VendorReportRepository vendorReportRepository, PaymentAliasRepository paymentAliasRepository) {
        this.paymentReportRepository = paymentReportRepository;
        this.vendorReportRepository = vendorReportRepository;
        this.paymentAliasRepository = paymentAliasRepository;
    }


    public ResponseEntity<Result<List<VendorReport>>> findVendorReport(Long id, Long type) throws Exception {
        Result<List<VendorReport>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<VendorReport> vendorReports = new ArrayList<>();
            if (type == 0) {
                vendorReports = vendorReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                vendorReports = vendorReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }
//            PaymentVendorReportResponse paymentVendorReportResponse = new PaymentVendorReportResponse();
//            List<PaymentReport> paymentReports = new ArrayList<>();
//            log.info("findVendorReport : {}", "");
//            if (type == 0) {
//                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
//            } else {
//                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
//            }
//            log.info("paymentReports  size: {}", paymentReports.size());
//            log.info("paymentReports  : {}", paymentReports);
//            Map<String, List<PaymentReport>> groupByVendor = paymentReports.stream().collect(Collectors.groupingBy(
//                    this::groupByMultipleKey, Collectors.mapping((PaymentReport p) -> p, toList())));
//
//            log.info("PaymentReport : {}", paymentReports);
//            log.info("groupByVendor : {}", groupByVendor);
//
//            List<VendorReportResponse> vendorList = new ArrayList<VendorReportResponse>();
//
//            groupByVendor.forEach((k, v) -> {
//                CompanyReportResponse companyReportResponse = new CompanyReportResponse();
//                companyReportResponse.setCompanyCode("99999");
//                companyReportResponse.setCompanyName("กระทรวงการคลัง");
//                companyReportResponse.setPaymentName(v.get(0).getPaymentName());
//                companyReportResponse.setPaymentDate(v.get(0).getPaymentDate());
//                companyReportResponse.setUserId("");
//                paymentVendorReportResponse.setCompany(companyReportResponse);
//
//                log.info(k);
//                log.info("size {}", v.size());
//                VendorReportResponse vendorReportResponse = new VendorReportResponse();
//                vendorReportResponse.setVendorCode(v.get(0).getVendorCode());
//                vendorReportResponse.setVendorNameTH(null == v.get(0).getName1() ? "" : v.get(0).getName1());
//                vendorReportResponse.setVendorNameEn(null == v.get(0).getName2() ? "" : v.get(0).getName2());
//                vendorReportResponse.setAddress(null == v.get(0).getAddress() ? "" : v.get(0).getAddress());
//                vendorReportResponse.setBankName(v.get(0).getPayeeBankReference());
//                vendorReportResponse.setBankBranch(v.get(0).getPayeeBankKey());
//                vendorReportResponse.setBankNo(v.get(0).getPayeeBankNo());
//                vendorReportResponse.setBankAccountNo(v.get(0).getPayeeBankAccountNo());
//                List<GLLineSuccessReportResponse> glLine = new ArrayList<GLLineSuccessReportResponse>();
//
//
//                for (PaymentReport paymentReport : v) {
//
//                    GLLineSuccessReportResponse gLLineSuccessReportResponse = new GLLineSuccessReportResponse();
//                    GLLineErrorReportResponse gLLineErrorReportResponse = new GLLineErrorReportResponse();
//                    List<GLLineErrorReportResponse> glLineError = new ArrayList<GLLineErrorReportResponse>();
//

//                    if (paymentReport.getStatus().equalsIgnoreCase("S")) {
//                        BigDecimal specialAmountSumFc = BigDecimal.ZERO;
//                        BigDecimal specialAmountMinusFc = BigDecimal.ZERO;
//                        BigDecimal specialAmountNet = BigDecimal.ZERO;
//                        try {
//                            List<SpecialGLLineReportResponse> specialCase = new ArrayList<>();
//                            if (paymentReport.getOriginalDocumentType().equalsIgnoreCase("KA") || paymentReport.getOriginalDocumentType().equalsIgnoreCase("KB") ||
//                                    paymentReport.getOriginalDocumentType().equalsIgnoreCase("KG") || paymentReport.getOriginalDocumentType().equalsIgnoreCase("KC")) {
//                                List<PaymentReport> listChild;
//                                if (type == 0) {
//                                    listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true);
//                                } else {
//                                    listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true);
//                                }
//                                log.info(" type {} ", type);
//                                log.info(" listChild {} ", listChild.size());
//                                if (listChild.size() > 0) {
//                                    for (PaymentReport child : listChild) {
//                                        SpecialGLLineReportResponse special = new SpecialGLLineReportResponse();
//                                        special.setCode1(child.getFiArea());
//                                        special.setCode2(child.getOriginalCompanyCode());
//                                        special.setDocumentNo(child.getOriginalDocumentNo());
//                                        special.setDocumentType(child.getOriginalDocumentType());
//                                        special.setDocumentDate(child.getDateDoc());
//                                        special.setInvoiceDocumentType(child.getInvoiceDocumentType());
//                                        special.setInvoiceDocumentNo(child.getInvoiceDocumentNo());
//                                        special.setInvoiceFiscalYear(child.getInvoiceFiscalYear());
//
//                                        special.setBaseDate(child.getDateBaseLine());
//                                        special.setPaymentTerm(child.getPaymentTerm());
//                                        special.setPk(child.getPostingKey());
//                                        special.setAmountSumFc(child.getInvoiceAmount());
//                                        special.setAmountMinusFc(
//                                                child.getInvoiceWtxAmount() != null ? child.getInvoiceWtxAmount()
//                                                        : BigDecimal.ZERO);
//                                        special.setAmountNet(child.getInvoiceAmountPaid());
//                                        special.setCurrencyNetPay(child.getCurrency());
//
//                                        specialAmountSumFc = specialAmountSumFc.subtract(child.getInvoiceAmount());
//                                        specialAmountMinusFc = specialAmountMinusFc.subtract(child.getInvoiceWtxAmount() != null ? child.getInvoiceWtxAmount()
//                                                : BigDecimal.ZERO);
//                                        specialAmountNet = specialAmountNet.subtract(child.getInvoiceAmountPaid());
//
//                                        specialCase.add(special);
//                                    }
//                                }
//                            }
//
//                            gLLineSuccessReportResponse.setSpecialGLLineReportResponseList(specialCase);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        log.info(" specialAmountSumFc {} ", specialAmountSumFc);
//                        log.info(" specialAmountMinusFc {} ", specialAmountMinusFc);
//                        log.info(" specialAmountNet {} ", specialAmountNet);
//
//
//                        gLLineSuccessReportResponse.setPaymentProcessId(paymentReport.getId());
//                        gLLineSuccessReportResponse.setCode1(paymentReport.getFiArea());
//                        gLLineSuccessReportResponse.setCode2(paymentReport.getInvoiceCompanyCode());
//                        gLLineSuccessReportResponse.setOriginalDocumentNo(paymentReport.getOriginalDocumentNo());
//                        gLLineSuccessReportResponse.setOriginalDocumentType(paymentReport.getOriginalDocumentType());
//                        gLLineSuccessReportResponse.setOriginalFiscalYear(paymentReport.getOriginalFiscalYear());
//                        gLLineSuccessReportResponse.setDocumentDate(paymentReport.getDateAcct());
//                        gLLineSuccessReportResponse.setInvoiceDocumentType(paymentReport.getInvoiceDocumentType());
//                        gLLineSuccessReportResponse.setInvoiceDocumentNo(paymentReport.getInvoiceDocumentNo());
//                        gLLineSuccessReportResponse.setInvoiceFiscalYear(paymentReport.getInvoiceFiscalYear());
//
//                        gLLineSuccessReportResponse.setBaseDate(paymentReport.getDateBaseLine());
//                        gLLineSuccessReportResponse.setPaymentTerm(paymentReport.getPaymentTerm());
//                        gLLineSuccessReportResponse.setPk(paymentReport.getPostingKey());
//                        gLLineSuccessReportResponse.setAmountSumFc(paymentReport.getInvoiceAmount());
//                        gLLineSuccessReportResponse.setAmountMinusFc(
//                                paymentReport.getInvoiceWtxAmount() != null ? paymentReport.getInvoiceWtxAmount()
//                                        : BigDecimal.ZERO);
////                        gLLineSuccessReportResponse.setAmountNet(paymentReport.getInvoiceAmount().subtract(Util.getBigDecimal(paymentReport.getInvoiceWtxAmount())));
//                        gLLineSuccessReportResponse.setAmountNet(paymentReport.getInvoiceAmountPaid());
//
//                        gLLineSuccessReportResponse.setSummaryAmountSumFc(paymentReport.getInvoiceAmount().add(specialAmountSumFc));
//                        gLLineSuccessReportResponse.setSummaryAmountMinusFc(
//                                paymentReport.getInvoiceWtxAmount() != null ? paymentReport.getInvoiceWtxAmount().add(specialAmountMinusFc)
//                                        : BigDecimal.ZERO);
////                        gLLineSuccessReportResponse.setSummaryAmountNet(paymentReport.getInvoiceAmountPaid());
//                        gLLineSuccessReportResponse.setSummaryAmountNet(paymentReport.getInvoiceAmountPaid().add(specialAmountNet));
//
//                        gLLineSuccessReportResponse.setCurrencyNetPay(paymentReport.getCurrency());
//
//                        gLLineSuccessReportResponse.setPaymentDocNo(paymentReport.getPaymentDocumentNo());
//                        gLLineSuccessReportResponse.setPaymentCompCode(paymentReport.getPaymentCompanyCode());
//                        gLLineSuccessReportResponse.setPaymentFiscalYear(paymentReport.getPaymentFiscalYear());
//
//                        gLLineSuccessReportResponse.setBankAgent(paymentReport.getPayingHouseBank());
//                        gLLineSuccessReportResponse.setBankAgentAccount(paymentReport.getPayingBankCode());
//                        gLLineSuccessReportResponse.setPaymentMethod(paymentReport.getPaymentMethod() + " - "
//                                + paymentReport.getPaymentMethodName());
//                        gLLineSuccessReportResponse.setVendorName(paymentReport.getName2());
//
//                        if (specialAmountNet.compareTo(BigDecimal.ZERO) == 0) {
//                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid().subtract(specialAmountNet));
//
////                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid());
//
//                        } else {
//                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid().subtract(specialAmountNet.negate()));
//
////                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid());
//                        }
//
//                        gLLineSuccessReportResponse.setCurrencyPay(paymentReport.getCurrency());
//                        gLLineSuccessReportResponse.setGlStatus("S");
//
//
//                    } else {
//                        gLLineSuccessReportResponse.setGlStatus("E");
//                        gLLineErrorReportResponse.setCode1(paymentReport.getFiArea());
//                        gLLineErrorReportResponse.setCode2(paymentReport.getInvoiceCompanyCode());
//                        gLLineErrorReportResponse.setOriginalDocumentNo(paymentReport.getOriginalDocumentNo());
//                        gLLineErrorReportResponse.setOriginalDocumentType(paymentReport.getOriginalDocumentType());
//                        gLLineErrorReportResponse.setOriginalFiscalYear(paymentReport.getOriginalFiscalYear());
//                        gLLineErrorReportResponse.setDocumentDate(paymentReport.getDateAcct());
//                        gLLineErrorReportResponse.setInvoiceDocumentType(paymentReport.getInvoiceDocumentType());
//                        gLLineErrorReportResponse.setInvoiceDocumentNo(paymentReport.getInvoiceDocumentNo());
//                        gLLineErrorReportResponse.setInvoiceFiscalYear(paymentReport.getInvoiceFiscalYear());
//
//                        gLLineErrorReportResponse.setBaseDate(paymentReport.getDateBaseLine());
//                        gLLineErrorReportResponse.setPaymentTerm(paymentReport.getPaymentTerm());
//                        gLLineErrorReportResponse.setPk(paymentReport.getPostingKey());
//                        gLLineErrorReportResponse.setAmountSumFc(paymentReport.getInvoiceAmount());
//                        gLLineErrorReportResponse.setAmountMinusFc(
//                                paymentReport.getInvoiceAmount() != null ? paymentReport.getInvoiceAmount()
//                                        : BigDecimal.ZERO);
//                        gLLineErrorReportResponse.setAmountNet(paymentReport.getInvoiceAmountPaid());
//                        gLLineErrorReportResponse.setCurrencyNetPay(paymentReport.getCurrency());
//                        gLLineErrorReportResponse.setErrorCode(paymentReport.getErrorCode());
//
//                        glLineError.add(gLLineErrorReportResponse);
//                        gLLineSuccessReportResponse.setError(glLineError);
//
//                    }
//
//                    glLine.add(gLLineSuccessReportResponse);
//                    vendorReportResponse.setGlLine(glLine);
//                }
//
//                vendorList.add(vendorReportResponse);
//
//                paymentVendorReportResponse.setVendor(vendorList);
//
//            });

            result.setData(vendorReports);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<Page<PaymentReportPaging>>> findVendorReportDetail(Long paymentAliasId, boolean proposal, String vendor, String bankAccount, int page, int size) throws Exception {
        Result<Page<PaymentReportPaging>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            Page<PaymentReportPaging> paymentReports = null;
            log.info("findVendorReport : {}", "");
            int success = 0;
            int error = 0;
            if (proposal) {
                success = paymentReportRepository.count(paymentAliasId, proposal, vendor, bankAccount, "S");
                error = paymentReportRepository.count(paymentAliasId, proposal, vendor, bankAccount, "E");
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(paymentAliasId, proposal, vendor, bankAccount, page, size);
            } else {
                success = paymentReportRepository.count(paymentAliasId, proposal, vendor, bankAccount, "S");
                error = paymentReportRepository.count(paymentAliasId, proposal, vendor, bankAccount, "E");
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(paymentAliasId, proposal, vendor, bankAccount, page, size);
            }
            for (PaymentReportPaging listPaymentReport : paymentReports.getContent()) {
                listPaymentReport.setSuccess(success);
                listPaymentReport.setError(error);
                if (listPaymentReport.isHaveChild()) {
                    listPaymentReport.setPaymentReportListChild(
                            paymentReportRepository
                                    .findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(
                                            listPaymentReport.getOriginalCompanyCode(),
                                            listPaymentReport.getOriginalDocumentNo(),
                                            listPaymentReport.getOriginalFiscalYear(),
                                            proposal,
                                            true,
                                        listPaymentReport.getPaymentAliasId()));
                }
            }


//            log.info("paymentReports  size: {}", paymentReports.size());
//            log.info("paymentReports  : {}", paymentReports);
//            Map<String, List<PaymentReport>> groupByVendor = paymentReports.stream().collect(Collectors.groupingBy(
//                    this::groupByMultipleKey, Collectors.mapping((PaymentReport p) -> p, toList())));
//
//            log.info("PaymentReport : {}", paymentReports);
//            log.info("groupByVendor : {}", groupByVendor);
//
//            List<VendorReportResponse> vendorList = new ArrayList<VendorReportResponse>();
//
//            groupByVendor.forEach((k, v) -> {
//                CompanyReportResponse companyReportResponse = new CompanyReportResponse();
//                companyReportResponse.setCompanyCode("99999");
//                companyReportResponse.setCompanyName("กระทรวงการคลัง");
//                companyReportResponse.setPaymentName(v.get(0).getPaymentName());
//                companyReportResponse.setPaymentDate(v.get(0).getPaymentDate());
//                companyReportResponse.setUserId("");
//                paymentVendorReportResponse.setCompany(companyReportResponse);
//
//                log.info(k);
//                log.info("size {}", v.size());
//                VendorReportResponse vendorReportResponse = new VendorReportResponse();
//                vendorReportResponse.setVendorCode(v.get(0).getVendorCode());
//                vendorReportResponse.setVendorNameTH(null == v.get(0).getName1() ? "" : v.get(0).getName1());
//                vendorReportResponse.setVendorNameEn(null == v.get(0).getName2() ? "" : v.get(0).getName2());
//                vendorReportResponse.setAddress(null == v.get(0).getAddress() ? "" : v.get(0).getAddress());
//                vendorReportResponse.setBankName(v.get(0).getPayeeBankReference());
//                vendorReportResponse.setBankBranch(v.get(0).getPayeeBankKey());
//                vendorReportResponse.setBankNo(v.get(0).getPayeeBankNo());
//                vendorReportResponse.setBankAccountNo(v.get(0).getPayeeBankAccountNo());
//                List<GLLineSuccessReportResponse> glLine = new ArrayList<GLLineSuccessReportResponse>();
//
//
//                for (PaymentReport paymentReport : v) {
//
//                    GLLineSuccessReportResponse gLLineSuccessReportResponse = new GLLineSuccessReportResponse();
//                    GLLineErrorReportResponse gLLineErrorReportResponse = new GLLineErrorReportResponse();
//                    List<GLLineErrorReportResponse> glLineError = new ArrayList<GLLineErrorReportResponse>();
//
//                    if (paymentReport.getStatus().equalsIgnoreCase("S")) {
//                        BigDecimal specialAmountSumFc = BigDecimal.ZERO;
//                        BigDecimal specialAmountMinusFc = BigDecimal.ZERO;
//                        BigDecimal specialAmountNet = BigDecimal.ZERO;
//                        try {
//                            List<SpecialGLLineReportResponse> specialCase = new ArrayList<>();
//                            if (paymentReport.getOriginalDocumentType().equalsIgnoreCase("KA") || paymentReport.getOriginalDocumentType().equalsIgnoreCase("KB") ||
//                                    paymentReport.getOriginalDocumentType().equalsIgnoreCase("KG") || paymentReport.getOriginalDocumentType().equalsIgnoreCase("KC")) {
//                                List<PaymentReport> listChild;
//                                if (type == 0) {
//                                    listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true);
//                                } else {
//                                    listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true);
//                                }
//                                log.info(" type {} ", type);
//                                log.info(" listChild {} ", listChild.size());
//                                if (listChild.size() > 0) {
//                                    for (PaymentReport child : listChild) {
//                                        SpecialGLLineReportResponse special = new SpecialGLLineReportResponse();
//                                        special.setCode1(child.getFiArea());
//                                        special.setCode2(child.getOriginalCompanyCode());
//                                        special.setDocumentNo(child.getOriginalDocumentNo());
//                                        special.setDocumentType(child.getOriginalDocumentType());
//                                        special.setDocumentDate(child.getDateDoc());
//                                        special.setInvoiceDocumentType(child.getInvoiceDocumentType());
//                                        special.setInvoiceDocumentNo(child.getInvoiceDocumentNo());
//                                        special.setInvoiceFiscalYear(child.getInvoiceFiscalYear());
//
//                                        special.setBaseDate(child.getDateBaseLine());
//                                        special.setPaymentTerm(child.getPaymentTerm());
//                                        special.setPk(child.getPostingKey());
//                                        special.setAmountSumFc(child.getInvoiceAmount());
//                                        special.setAmountMinusFc(
//                                                child.getInvoiceWtxAmount() != null ? child.getInvoiceWtxAmount()
//                                                        : BigDecimal.ZERO);
//                                        special.setAmountNet(child.getInvoiceAmountPaid());
//                                        special.setCurrencyNetPay(child.getCurrency());
//
//                                        specialAmountSumFc = specialAmountSumFc.subtract(child.getInvoiceAmount());
//                                        specialAmountMinusFc = specialAmountMinusFc.subtract(child.getInvoiceWtxAmount() != null ? child.getInvoiceWtxAmount()
//                                                : BigDecimal.ZERO);
//                                        specialAmountNet = specialAmountNet.subtract(child.getInvoiceAmountPaid());
//
//                                        specialCase.add(special);
//                                    }
//                                }
//                            }
//
//                            gLLineSuccessReportResponse.setSpecialGLLineReportResponseList(specialCase);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        log.info(" specialAmountSumFc {} ", specialAmountSumFc);
//                        log.info(" specialAmountMinusFc {} ", specialAmountMinusFc);
//                        log.info(" specialAmountNet {} ", specialAmountNet);
//
//
//                        gLLineSuccessReportResponse.setPaymentProcessId(paymentReport.getId());
//                        gLLineSuccessReportResponse.setCode1(paymentReport.getFiArea());
//                        gLLineSuccessReportResponse.setCode2(paymentReport.getInvoiceCompanyCode());
//                        gLLineSuccessReportResponse.setOriginalDocumentNo(paymentReport.getOriginalDocumentNo());
//                        gLLineSuccessReportResponse.setOriginalDocumentType(paymentReport.getOriginalDocumentType());
//                        gLLineSuccessReportResponse.setOriginalFiscalYear(paymentReport.getOriginalFiscalYear());
//                        gLLineSuccessReportResponse.setDocumentDate(paymentReport.getDateAcct());
//                        gLLineSuccessReportResponse.setInvoiceDocumentType(paymentReport.getInvoiceDocumentType());
//                        gLLineSuccessReportResponse.setInvoiceDocumentNo(paymentReport.getInvoiceDocumentNo());
//                        gLLineSuccessReportResponse.setInvoiceFiscalYear(paymentReport.getInvoiceFiscalYear());
//
//                        gLLineSuccessReportResponse.setBaseDate(paymentReport.getDateBaseLine());
//                        gLLineSuccessReportResponse.setPaymentTerm(paymentReport.getPaymentTerm());
//                        gLLineSuccessReportResponse.setPk(paymentReport.getPostingKey());
//                        gLLineSuccessReportResponse.setAmountSumFc(paymentReport.getInvoiceAmount());
//                        gLLineSuccessReportResponse.setAmountMinusFc(
//                                paymentReport.getInvoiceWtxAmount() != null ? paymentReport.getInvoiceWtxAmount()
//                                        : BigDecimal.ZERO);
////                        gLLineSuccessReportResponse.setAmountNet(paymentReport.getInvoiceAmount().subtract(Util.getBigDecimal(paymentReport.getInvoiceWtxAmount())));
//                        gLLineSuccessReportResponse.setAmountNet(paymentReport.getInvoiceAmountPaid());
//
//                        gLLineSuccessReportResponse.setSummaryAmountSumFc(paymentReport.getInvoiceAmount().add(specialAmountSumFc));
//                        gLLineSuccessReportResponse.setSummaryAmountMinusFc(
//                                paymentReport.getInvoiceWtxAmount() != null ? paymentReport.getInvoiceWtxAmount().add(specialAmountMinusFc)
//                                        : BigDecimal.ZERO);
////                        gLLineSuccessReportResponse.setSummaryAmountNet(paymentReport.getInvoiceAmountPaid());
//                        gLLineSuccessReportResponse.setSummaryAmountNet(paymentReport.getInvoiceAmountPaid().add(specialAmountNet));
//
//                        gLLineSuccessReportResponse.setCurrencyNetPay(paymentReport.getCurrency());
//
//                        gLLineSuccessReportResponse.setPaymentDocNo(paymentReport.getPaymentDocumentNo());
//                        gLLineSuccessReportResponse.setPaymentCompCode(paymentReport.getPaymentCompanyCode());
//                        gLLineSuccessReportResponse.setPaymentFiscalYear(paymentReport.getPaymentFiscalYear());
//
//                        gLLineSuccessReportResponse.setBankAgent(paymentReport.getPayingHouseBank());
//                        gLLineSuccessReportResponse.setBankAgentAccount(paymentReport.getPayingBankCode());
//                        gLLineSuccessReportResponse.setPaymentMethod(paymentReport.getPaymentMethod() + " - "
//                                + paymentReport.getPaymentMethodName());
//                        gLLineSuccessReportResponse.setVendorName(paymentReport.getName2());
//
//                        if (specialAmountNet.compareTo(BigDecimal.ZERO) == 0) {
//                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid().subtract(specialAmountNet));
//
////                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid());
//
//                        } else {
//                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid().subtract(specialAmountNet.negate()));
//
////                            gLLineSuccessReportResponse.setAmountPay(paymentReport.getInvoiceAmountPaid());
//                        }
//
//                        gLLineSuccessReportResponse.setCurrencyPay(paymentReport.getCurrency());
//                        gLLineSuccessReportResponse.setGlStatus("S");
//
//
//                    } else {
//                        gLLineSuccessReportResponse.setGlStatus("E");
//                        gLLineErrorReportResponse.setCode1(paymentReport.getFiArea());
//                        gLLineErrorReportResponse.setCode2(paymentReport.getInvoiceCompanyCode());
//                        gLLineErrorReportResponse.setOriginalDocumentNo(paymentReport.getOriginalDocumentNo());
//                        gLLineErrorReportResponse.setOriginalDocumentType(paymentReport.getOriginalDocumentType());
//                        gLLineErrorReportResponse.setOriginalFiscalYear(paymentReport.getOriginalFiscalYear());
//                        gLLineErrorReportResponse.setDocumentDate(paymentReport.getDateAcct());
//                        gLLineErrorReportResponse.setInvoiceDocumentType(paymentReport.getInvoiceDocumentType());
//                        gLLineErrorReportResponse.setInvoiceDocumentNo(paymentReport.getInvoiceDocumentNo());
//                        gLLineErrorReportResponse.setInvoiceFiscalYear(paymentReport.getInvoiceFiscalYear());
//
//                        gLLineErrorReportResponse.setBaseDate(paymentReport.getDateBaseLine());
//                        gLLineErrorReportResponse.setPaymentTerm(paymentReport.getPaymentTerm());
//                        gLLineErrorReportResponse.setPk(paymentReport.getPostingKey());
//                        gLLineErrorReportResponse.setAmountSumFc(paymentReport.getInvoiceAmount());
//                        gLLineErrorReportResponse.setAmountMinusFc(
//                                paymentReport.getInvoiceAmount() != null ? paymentReport.getInvoiceAmount()
//                                        : BigDecimal.ZERO);
//                        gLLineErrorReportResponse.setAmountNet(paymentReport.getInvoiceAmountPaid());
//                        gLLineErrorReportResponse.setCurrencyNetPay(paymentReport.getCurrency());
//                        gLLineErrorReportResponse.setErrorCode(paymentReport.getErrorCode());
//
//                        glLineError.add(gLLineErrorReportResponse);
//                        gLLineSuccessReportResponse.setError(glLineError);
//
//                    }
//
//                    glLine.add(gLLineSuccessReportResponse);
//                    vendorReportResponse.setGlLine(glLine);
//                }
//
//                vendorList.add(vendorReportResponse);
//
//                paymentVendorReportResponse.setVendor(vendorList);
//
//            });

            result.setData(paymentReports);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<CompanyReportResponse>> findCompanyReport(Long paymentAliasId) throws Exception {
        Result<CompanyReportResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            PaymentAlias paymentAlias = paymentAliasRepository.findOneById(paymentAliasId);
            CompanyReportResponse companyReportResponse = new CompanyReportResponse();
            companyReportResponse.setCompanyCode("99999");
            companyReportResponse.setCompanyName("กระทรวงการคลัง");
            companyReportResponse.setPaymentName(paymentAlias.getPaymentName());
            companyReportResponse.setPaymentDate(paymentAlias.getPaymentDate());
            companyReportResponse.setUserId("");

            result.setData(companyReportResponse);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result<List<PaymentErrorReportResponse>>> findErrorReport(Long id, Long type) throws Exception {
        Result<List<PaymentErrorReportResponse>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<PaymentErrorReportResponse> listPaymentErrorReportResponse = new ArrayList<>();

            List<PaymentReport> paymentReports = new ArrayList<>();
            log.info("findErrorReport : {}", "");
            if (type == 0) {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }


            boolean isValue = paymentReports.stream().anyMatch(paymentReport -> paymentReport.getErrorCode() != null && !"".equalsIgnoreCase(paymentReport.getErrorCode()));


            if (isValue) {
                List<PaymentReport> filterNotNull = paymentReports.stream().filter(paymentReport -> paymentReport.getErrorCode() != null && !"".equalsIgnoreCase(paymentReport.getErrorCode())).collect(Collectors.toList());

                Map<String, List<PaymentReport>> groupsByError = filterNotNull.stream()
                        .collect(Collectors.groupingBy(PaymentReport::getErrorCode,
                                Collectors.mapping((PaymentReport p) -> p, toList())));

                log.debug("groupsByError : {}", groupsByError);


                groupsByError.forEach((k, v) -> {
                    PaymentErrorReportResponse paymentErrorReportResponse = new PaymentErrorReportResponse();
                    if (k.equalsIgnoreCase("003")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("รายการถูกระงับในการชำระเงิน");
                    } else if (k.equalsIgnoreCase("012")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("จำนวนเงินที่จะจ่ายยังไม่ถึงจำนวนเงินขั้นต่ำสำหรับการชำระเงิน");
                    } else if (k.equalsIgnoreCase("016")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("วิธีการชำระเงินในการประมวลผลครั้งนี้ไม่ได้ระบุในรายการ");
                    } else if (k.equalsIgnoreCase("024")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ข้อมูลหลักของผู้รับเงินหายไป");
                    } else if (k.equalsIgnoreCase("031")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ข้อมูลผู้ขายได้รับการทำเครื่องหมายแฟลกสำหรับการลบ");
                    } else if (k.equalsIgnoreCase("033")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ข้อมูลผู้ขายมีสถานะ รอการยืนยัน");
                    } else if (k.equalsIgnoreCase("034")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ข้อมูลผู้รับเงินมีสถานะ รอการยืนยัน");
                    } else if (k.equalsIgnoreCase("035")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ข้อมูลผู้ขายมีสถานะ ไม่ยืนยัน");
                    } else if (k.equalsIgnoreCase("036")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ข้อมูลผู้รับเงินมีสถานะ ไม่ยืนยัน");
                    } else if (k.equalsIgnoreCase("060")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("รายการถูกระงับเนื่องจากยอดคงเหลือทางด้านเดบิท");
                    } else if (k.equalsIgnoreCase("071")) {
                        paymentErrorReportResponse.setErrorCode(k);
                        paymentErrorReportResponse.setDescription("ไม่พบธนาคารคู่ค้าที่ถูกต้อง");
                    }
                    listPaymentErrorReportResponse.add(paymentErrorReportResponse);
                });
                result.setData(listPaymentErrorReportResponse);
                result.setStatus(HttpStatus.OK.value());
                return new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setMessage("ไม่พบข้อมูล");
                result.setData(new ArrayList<>());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);

            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<PaymentAreaReportResponse>> findAreaReport(Long id, Long type) throws Exception {
        Result<PaymentAreaReportResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            PaymentAreaReportResponse paymentAreaReportResponse = new PaymentAreaReportResponse();

            List<PaymentReport> paymentReports = new ArrayList<>();
            log.info("findAreaReport : {}", "");
            if (type == 0) {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }


            Map<String, List<PaymentReport>> groupsByArea = paymentReports.stream().filter(obj -> obj.getStatus().equalsIgnoreCase("S"))
                    .collect(Collectors.groupingBy(PaymentReport::getFiArea,
                            Collectors.mapping((PaymentReport p) -> p, toList())));


            log.info("groupsByArea : {}", groupsByArea);

            List<AreaReportResponse> areaList = new ArrayList<AreaReportResponse>();

            groupsByArea.forEach((k, v) -> {


                final BigDecimal[] sumAmountPay = {new BigDecimal(0)};
                final BigDecimal[] sumAmountMinusFc = {new BigDecimal(0)};
                final BigDecimal[] sumAmountLc = {new BigDecimal(0)};


                AreaReportResponse areaReportResponse = new AreaReportResponse();
                areaReportResponse.setAreaCode(v.get(0).getFiArea());
                areaReportResponse.setAreaName(v.get(0).getFiAreaName());
                areaReportResponse.setSumCurrency(v.get(0).getCurrency());
                areaReportResponse.setSumLcurrency(v.get(0).getCurrency());


                Map<String, List<PaymentReport>> groupsByPayment = v.stream()
                        .collect(Collectors.groupingBy(PaymentReport::getPaymentMethod,
                                Collectors.mapping((PaymentReport p) -> p, toList())));

                log.info("groupsByPayment : {}", groupsByPayment);
                List<AreaDetailReportResponse> listDetail = new ArrayList<AreaDetailReportResponse>();
                for (Map.Entry<String, List<PaymentReport>> entry : groupsByPayment.entrySet()) {
                    String key = entry.getKey();
                    List<PaymentReport> value = entry.getValue();
                    BigDecimal amountPay = new BigDecimal(0);
                    BigDecimal amountMinusFc = new BigDecimal(0);
                    BigDecimal amountLc = new BigDecimal(0);

                    BigDecimal parentAmountPay = new BigDecimal(0);
                    BigDecimal parentAmountLc = new BigDecimal(0);
                    BigDecimal parentAmountMinusFc = new BigDecimal(0);

                    AreaDetailReportResponse areaDetailReportResponse = new AreaDetailReportResponse();
                    for (PaymentReport paymentReport : value) {
                        amountPay = amountPay.add(paymentReport.getInvoiceAmountPaid());
                        amountLc = amountLc.add(paymentReport.getInvoiceAmount());
                        amountMinusFc = amountMinusFc.add(paymentReport.getInvoiceWtxAmount());

                        if (paymentReport.isHaveChild()) {
                            List<PaymentReport> listChild = new ArrayList<PaymentReport>();
                            if (type == 0) {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true, paymentReport.getPaymentAliasId());
                            } else {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true, paymentReport.getPaymentAliasId());
                            }
                            parentAmountPay = parentAmountPay.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmountPaid().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountLc = parentAmountLc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountMinusFc = parentAmountMinusFc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceWtxAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));

                        }

                    }
                    amountPay = amountPay.subtract(parentAmountPay);
                    amountMinusFc = amountMinusFc.subtract(parentAmountMinusFc);
                    amountLc = amountLc.subtract(parentAmountLc).subtract(amountMinusFc);


                    areaDetailReportResponse.setCode(value.get(0).getFiArea());
                    areaDetailReportResponse.setPaymentMethod(value.get(0).getPaymentMethod());
                    areaDetailReportResponse.setCurrency(value.get(0).getCurrency());
                    areaDetailReportResponse.setAmountPay(amountPay.toString());
                    areaDetailReportResponse.setAmountMinusFc(amountMinusFc.toString());
                    areaDetailReportResponse.setLcurrency(value.get(0).getCurrency());
                    areaDetailReportResponse.setAmountLc(amountLc.toString());
                    listDetail.add(areaDetailReportResponse);


                    sumAmountPay[0] = sumAmountPay[0].add(amountPay);
                    sumAmountMinusFc[0] = sumAmountMinusFc[0].add(amountMinusFc);
                    sumAmountLc[0] = sumAmountLc[0].add(amountLc);
                }
                areaReportResponse.setDetail(listDetail);
                areaReportResponse.setSumAmountPay(sumAmountPay[0].toString());
                areaReportResponse.setSumAmountMinusFc(sumAmountMinusFc[0].toString());
                areaReportResponse.setSumAmountLc(sumAmountLc[0].toString());
                areaList.add(areaReportResponse);

            });

            paymentAreaReportResponse.setArea(areaList);
            result.setData(paymentAreaReportResponse);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<PaymentCountryReportResponse>> findCountryReport(Long id, Long type) throws Exception {
        Result<PaymentCountryReportResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            PaymentCountryReportResponse paymentCountryReportResponse = new PaymentCountryReportResponse();

            List<PaymentReport> paymentReports = new ArrayList<>();
            log.info("findCountryReport : {}", "");
            if (type == 0) {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }

            Map<String, List<PaymentReport>> groupsByCountry = paymentReports.stream().filter(obj -> obj.getStatus().equalsIgnoreCase("S"))
                    .collect(Collectors.groupingBy(PaymentReport::getCountry,
                            Collectors.mapping((PaymentReport p) -> p, toList())));

            log.info("groupsByCountry : {}", groupsByCountry);

            List<CountryReportResponse> countryList = new ArrayList<CountryReportResponse>();

            groupsByCountry.forEach((k, v) -> {

                final BigDecimal[] sumAmountPay = {new BigDecimal(0)};
                final BigDecimal[] sumAmountMinusFc = {new BigDecimal(0)};
                final BigDecimal[] sumAmountLc = {new BigDecimal(0)};


                CountryReportResponse countryReportResponse = new CountryReportResponse();
                countryReportResponse.setCountryCode(v.get(0).getCountry());
                countryReportResponse.setCountryName(v.get(0).getCountryName());
                countryReportResponse.setSumCurrency(v.get(0).getCurrency());
                countryReportResponse.setSumLcurrency(v.get(0).getCurrency());

                Map<String, List<PaymentReport>> groupsByPayment = v.stream()
                        .collect(Collectors.groupingBy(PaymentReport::getPaymentMethod,
                                Collectors.mapping((PaymentReport p) -> p, toList())));


                List<CountryDetailReportResponse> listDetail = new ArrayList<CountryDetailReportResponse>();
                for (Map.Entry<String, List<PaymentReport>> entry : groupsByPayment.entrySet()) {
                    String key = entry.getKey();
                    List<PaymentReport> value = entry.getValue();
                    BigDecimal amountPay = new BigDecimal(0);
                    BigDecimal amountMinusFc = new BigDecimal(0);
                    BigDecimal amountLc = new BigDecimal(0);

                    BigDecimal parentAmountPay = new BigDecimal(0);
                    BigDecimal parentAmountLc = new BigDecimal(0);
                    BigDecimal parentAmountMinusFc = new BigDecimal(0);

                    CountryDetailReportResponse countryDetailReportResponse = new CountryDetailReportResponse();
                    for (PaymentReport paymentReport : value) {

                        amountPay = amountPay.add(paymentReport.getInvoiceAmountPaid());
                        amountLc = amountLc.add(paymentReport.getInvoiceAmount());
                        amountMinusFc = amountMinusFc.add(paymentReport.getInvoiceWtxAmount());


                        if (paymentReport.isHaveChild()) {
                            List<PaymentReport> listChild = new ArrayList<PaymentReport>();
                            if (type == 0) {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true, paymentReport.getPaymentAliasId());
                            } else {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true, paymentReport.getPaymentAliasId());
                            }
                            parentAmountPay = parentAmountPay.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmountPaid().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountLc = parentAmountLc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountMinusFc = parentAmountMinusFc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceWtxAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));

                        }

                    }
                    amountPay = amountPay.subtract(parentAmountPay);

                    amountMinusFc = amountMinusFc.subtract(parentAmountMinusFc);
                    amountLc = amountLc.subtract(parentAmountLc).subtract(amountMinusFc);

                    countryDetailReportResponse.setCode(value.get(0).getCountry());
                    countryDetailReportResponse.setPaymentMethod(value.get(0).getPaymentMethod());
                    countryDetailReportResponse.setCurrency(value.get(0).getCurrency());
                    countryDetailReportResponse.setAmountPay(amountPay.toString());
                    countryDetailReportResponse.setAmountMinusFc(amountMinusFc.toString());
                    countryDetailReportResponse.setLcurrency(value.get(0).getCurrency());
                    countryDetailReportResponse.setAmountLc(amountLc.toString());
                    listDetail.add(countryDetailReportResponse);

                    sumAmountPay[0] = sumAmountPay[0].add(amountPay);
                    sumAmountMinusFc[0] = sumAmountMinusFc[0].add(amountMinusFc);
                    sumAmountLc[0] = sumAmountLc[0].add(amountLc);
                }
                countryReportResponse.setDetail(listDetail);
                countryReportResponse.setSumAmountPay(sumAmountPay[0].toString());
                countryReportResponse.setSumAmountMinusFc(sumAmountMinusFc[0].toString());
                countryReportResponse.setSumAmountLc(sumAmountLc[0].toString());
                countryList.add(countryReportResponse);

            });

            paymentCountryReportResponse.setCountry(countryList);

            result.setData(paymentCountryReportResponse);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<PaymentCurrencyReportResponse>> findCurrencyReport(Long id, Long type) throws Exception {
        Result<PaymentCurrencyReportResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            PaymentCurrencyReportResponse paymentCurrencyReportResponse = new PaymentCurrencyReportResponse();

            List<PaymentReport> paymentReports = new ArrayList<>();
            log.info("findCurrencyReport : {}", "");
            if (type == 0) {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }

            Map<String, List<PaymentReport>> groupsByCurrency = paymentReports.stream().filter(obj -> obj.getStatus().equalsIgnoreCase("S"))
                    .collect(Collectors.groupingBy(PaymentReport::getCurrency,
                            Collectors.mapping((PaymentReport p) -> p, toList())));

            List<CurrencyReportResponse> currencyList = new ArrayList<CurrencyReportResponse>();

            groupsByCurrency.forEach((k, v) -> {


                final BigDecimal[] sumAmountPay = {new BigDecimal(0)};
                final BigDecimal[] sumAmountMinusFc = {new BigDecimal(0)};
                final BigDecimal[] sumAmountLc = {new BigDecimal(0)};

                CurrencyReportResponse currencyReportResponse = new CurrencyReportResponse();
                currencyReportResponse.setCurrencyCode(v.get(0).getCurrency());
                currencyReportResponse.setCurrencyName(" รอเอาชื่อมาเพิ่ม");
                currencyReportResponse.setSumCurrency(v.get(0).getCurrency());
                currencyReportResponse.setSumLcurrency(v.get(0).getCurrency());

                Map<String, List<PaymentReport>> groupsByPayment = v.stream()
                        .collect(Collectors.groupingBy(PaymentReport::getPaymentMethod,
                                Collectors.mapping((PaymentReport p) -> p, toList())));

                log.info("groupsByPayment : {}", groupsByPayment);
                List<CurrencyDetailReportResponse> listDetail = new ArrayList<CurrencyDetailReportResponse>();
                for (Map.Entry<String, List<PaymentReport>> entry : groupsByPayment.entrySet()) {
                    String key = entry.getKey();
                    List<PaymentReport> value = entry.getValue();
                    BigDecimal amountPay = BigDecimal.ZERO;
                    BigDecimal amountMinusFc = BigDecimal.ZERO;
                    BigDecimal amountLc = BigDecimal.ZERO;

                    BigDecimal parentAmountPay = BigDecimal.ZERO;
                    BigDecimal parentAmountLc = BigDecimal.ZERO;
                    BigDecimal parentAmountMinusFc = BigDecimal.ZERO;

                    CurrencyDetailReportResponse currencyDetailReportResponse = new CurrencyDetailReportResponse();
                    for (PaymentReport paymentReport : value) {
//                        log.info("ASDASDASD {}", paymentReport.getInvoiceAmountPaid());
                        amountPay = amountPay.add(paymentReport.getInvoiceAmountPaid());
                        amountLc = amountLc.add(paymentReport.getInvoiceAmount());
                        amountMinusFc = amountMinusFc.add(paymentReport.getInvoiceWtxAmount());


                        if (paymentReport.isHaveChild()) {
                            List<PaymentReport> listChild = new ArrayList<PaymentReport>();
                            if (type == 0) {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true, paymentReport.getPaymentAliasId());
                            } else {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true, paymentReport.getPaymentAliasId());
                            }
                            parentAmountPay = parentAmountPay.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmountPaid().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountLc = parentAmountLc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountMinusFc = parentAmountMinusFc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceWtxAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));

                        }

                    }
                    amountPay = amountPay.subtract(parentAmountPay);
                    amountMinusFc = amountMinusFc.subtract(parentAmountMinusFc);
                    amountLc = amountLc.subtract(parentAmountLc).subtract(amountMinusFc);

                    currencyDetailReportResponse.setCode(value.get(0).getCurrency());
                    currencyDetailReportResponse.setPaymentMethod(value.get(0).getPaymentMethod());
                    currencyDetailReportResponse.setCountry(value.get(0).getCountry());
                    currencyDetailReportResponse.setAmountPay(amountPay.toString());
                    currencyDetailReportResponse.setAmountMinusFc(amountMinusFc.toString());
                    currencyDetailReportResponse.setLcurrency(value.get(0).getCurrency());
                    currencyDetailReportResponse.setAmountLc(amountLc.toString());
                    listDetail.add(currencyDetailReportResponse);

                    sumAmountPay[0] = sumAmountPay[0].add(amountPay);
                    sumAmountMinusFc[0] = sumAmountMinusFc[0].add(amountMinusFc);
                    sumAmountLc[0] = sumAmountLc[0].add(amountLc);
                }
                currencyReportResponse.setDetail(listDetail);
                currencyReportResponse.setSumAmountPay(sumAmountPay[0].toString());
                currencyReportResponse.setSumAmountMinusFc(sumAmountMinusFc[0].toString());
                currencyReportResponse.setSumAmountLc(sumAmountLc[0].toString());
                currencyList.add(currencyReportResponse);

            });

            paymentCurrencyReportResponse.setCurrency(currencyList);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentCurrencyReportResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<PaymentPaymentMethodReportResponse>> findPaymentMethodReport(Long id, Long type)
            throws Exception {
        Result<PaymentPaymentMethodReportResponse> result = new Result<>();

        result.setTimestamp(new Date());
        try {
            PaymentPaymentMethodReportResponse paymentPaymentMethodReportResponse = new PaymentPaymentMethodReportResponse();

            List<PaymentReport> paymentReports = new ArrayList<>();
            log.info("findPaymentMethodReport : {}", "");
            if (type == 0) {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }

            Map<String, List<PaymentReport>> groupsByPaymentMethod = paymentReports.stream().filter(obj -> obj.getStatus().equalsIgnoreCase("S"))
                    .collect(Collectors.groupingBy(PaymentReport::getPaymentMethod,
                            Collectors.mapping((PaymentReport p) -> p, toList())));


            List<PaymentMethodReportResponse> paymentMethodList = new ArrayList<PaymentMethodReportResponse>();
            final int[] sumTotalOrder = {0};
            final BigDecimal[] sumAmountLc = {new BigDecimal(0)};


            for (Map.Entry<String, List<PaymentReport>> entry : groupsByPaymentMethod.entrySet()) {
                String k = entry.getKey();
                List<PaymentReport> v = entry.getValue();

                BigDecimal amountPay = BigDecimal.ZERO;
                BigDecimal amountMinusFc = BigDecimal.ZERO;
                BigDecimal amountLc = BigDecimal.ZERO;

                BigDecimal parentAmountPay = BigDecimal.ZERO;
                BigDecimal parentAmountLc = BigDecimal.ZERO;
                BigDecimal parentAmountMinusFc = BigDecimal.ZERO;

                PaymentMethodReportResponse paymentMethodReportResponse = new PaymentMethodReportResponse();
                for (PaymentReport paymentReport : v) {
                    amountPay = amountPay.add(paymentReport.getInvoiceAmountPaid());
                    amountLc = amountLc.add(paymentReport.getInvoiceAmount());
                    amountMinusFc = amountMinusFc.add(paymentReport.getInvoiceWtxAmount());


                    if (paymentReport.isHaveChild()) {
                        List<PaymentReport> listChild = new ArrayList<PaymentReport>();
                        if (type == 0) {
                            listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true, paymentReport.getPaymentAliasId());
                        } else {
                            listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true, paymentReport.getPaymentAliasId());
                        }
                        parentAmountPay = parentAmountPay.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmountPaid().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                        parentAmountLc = parentAmountLc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                        parentAmountMinusFc = parentAmountMinusFc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceWtxAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));

                    }

                }
                amountPay = amountPay.subtract(parentAmountPay);
                amountMinusFc = amountMinusFc.subtract(parentAmountMinusFc);
                amountLc = amountLc.subtract(parentAmountLc).subtract(amountMinusFc);

                paymentMethodReportResponse.setPaymentMethodCode(v.get(0).getPaymentMethod());
                paymentMethodReportResponse.setTotalOrder(v.size());
                paymentMethodReportResponse.setCurrency(v.get(0).getCurrency());
                paymentMethodReportResponse.setAmountPay(amountPay.toString());
                paymentMethodReportResponse.setAmountLc(amountLc.toString());
                paymentMethodReportResponse.setAmountMinusFc(amountMinusFc.toString());
                paymentMethodReportResponse.setPaymentMethodCode(v.get(0).getPaymentMethod());
                paymentMethodReportResponse.setPaymentMethodName(v.get(0).getPaymentMethodName());
                paymentMethodReportResponse.setLcurrency(v.get(0).getCurrency());

                paymentMethodList.add(paymentMethodReportResponse);
                sumTotalOrder[0] = sumTotalOrder[0] + v.size();
                sumAmountLc[0] = sumAmountLc[0].add(amountLc);
            }

            paymentPaymentMethodReportResponse.setPaymentMethod(paymentMethodList);
            paymentPaymentMethodReportResponse.setSumTotalOrder(sumTotalOrder[0]);
            paymentPaymentMethodReportResponse.setSumAmountLc(sumAmountLc[0].toString());

            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentPaymentMethodReportResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Result<PaymentBankReportResponse>> findBankReport(Long id, Long type) throws Exception {
        Result<PaymentBankReportResponse> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            PaymentBankReportResponse paymentBankReportResponse = new PaymentBankReportResponse();

            List<PaymentReport> paymentReports = new ArrayList<>();
            log.info("findBankReport : {}", "");
            if (type == 0) {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, true, false);
            } else {
                paymentReports = paymentReportRepository.findAllByPaymentAliasIdAndIsProposalAndIsChild(id, false, false);
            }

            Map<String, List<PaymentReport>> groupsByBank = paymentReports.stream().filter(obj -> obj.getStatus().equalsIgnoreCase("S"))
                    .collect(Collectors.groupingBy(PaymentReport::getPayingBankAccountNo,
                            Collectors.mapping((PaymentReport p) -> p, toList())));


            List<BankReportResponse> bankList = new ArrayList<BankReportResponse>();

            final int[] sumTotalOrderAll = {0};
            final BigDecimal[] sumAmountPayAll = {new BigDecimal(0)};
            final BigDecimal[] sumAmountMinusFcAll = {new BigDecimal(0)};
            final BigDecimal[] sumAmountLcAll = {new BigDecimal(0)};

            groupsByBank.forEach((k, v) -> {


                final int[] sumTotalOrder = {0};
                final BigDecimal[] sumAmountPay = {new BigDecimal(0)};
                final BigDecimal[] sumAmountMinusFc = {new BigDecimal(0)};
                final BigDecimal[] sumAmountLc = {new BigDecimal(0)};

                BankReportResponse bankReportResponse = new BankReportResponse();
                bankReportResponse.setBankAccountNo(v.get(0).getPayingBankAccountNo());
                bankReportResponse.setBankName(v.get(0).getPayingBankName());
//                bankReportResponse.setBankBranchName("รอข้อมูล");
                bankReportResponse.setSumCurrency(v.get(0).getCurrency());
                bankReportResponse.setSumLcurrency(v.get(0).getCurrency());

                Map<String, List<PaymentReport>> groupsByPayment = v.stream()
                        .collect(Collectors.groupingBy(PaymentReport::getPaymentMethod,
                                Collectors.mapping((PaymentReport p) -> p, toList())));


                List<BankDetailReportResponse> listDetail = new ArrayList<BankDetailReportResponse>();
                for (Map.Entry<String, List<PaymentReport>> entry : groupsByPayment.entrySet()) {
                    String key = entry.getKey();
                    List<PaymentReport> value = entry.getValue();
                    BigDecimal amountPay = BigDecimal.ZERO;
                    BigDecimal amountMinusFc = BigDecimal.ZERO;
                    BigDecimal amountLc = BigDecimal.ZERO;

                    BigDecimal parentAmountPay = BigDecimal.ZERO;
                    BigDecimal parentAmountLc = BigDecimal.ZERO;
                    BigDecimal parentAmountMinusFc = BigDecimal.ZERO;

                    BankDetailReportResponse bankDetailReportResponse = new BankDetailReportResponse();
                    for (PaymentReport paymentReport : value) {
                        amountPay = amountPay.add(paymentReport.getInvoiceAmountPaid());
                        amountLc = amountLc.add(paymentReport.getInvoiceAmount());
                        amountMinusFc = amountMinusFc.add(paymentReport.getInvoiceWtxAmount());


                        if (paymentReport.isHaveChild()) {
                            List<PaymentReport> listChild = new ArrayList<PaymentReport>();
                            if (type == 0) {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), true, true, paymentReport.getPaymentAliasId());
                            } else {
                                listChild = paymentReportRepository.findAllByParentCompanyCodeAndParentDocumentNoAndParentFiscalYearAndIsProposalAndIsChild(paymentReport.getOriginalCompanyCode(), paymentReport.getOriginalDocumentNo(), paymentReport.getOriginalFiscalYear(), false, true, paymentReport.getPaymentAliasId());
                            }
                            parentAmountPay = parentAmountPay.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmountPaid().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountLc = parentAmountLc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));
                            parentAmountMinusFc = parentAmountMinusFc.add(new BigDecimal(listChild.stream().mapToDouble(item -> Double.parseDouble(item.getInvoiceWtxAmount().toString())).sum()).setScale(2, RoundingMode.HALF_UP));

                        }

                    }
                    amountPay = amountPay.subtract(parentAmountPay);
                    amountMinusFc = amountMinusFc.subtract(parentAmountMinusFc);
                    amountLc = amountLc.subtract(parentAmountLc).subtract(amountMinusFc);

                    bankDetailReportResponse.setCountry(value.get(0).getCountry());
                    bankDetailReportResponse.setBankKey(value.get(0).getPayingBankKey());
//                    bankDetailReportResponse.setCs("01  รอข้อมูล");
                    bankDetailReportResponse.setCs("01");
                    bankDetailReportResponse.setBankAccountNo(value.get(0).getPayingBankAccountNo());
                    bankDetailReportResponse.setPaymentMethod(value.get(0).getPaymentMethod());
                    bankDetailReportResponse.setTotalOrder(value.size());
                    bankDetailReportResponse.setCurrency(value.get(0).getCurrency());
                    bankDetailReportResponse.setAmountPay(amountPay.toString());
                    bankDetailReportResponse.setAmountMinusFc(amountMinusFc.toString());
                    bankDetailReportResponse.setLcurrency(value.get(0).getCurrency());
                    bankDetailReportResponse.setAmountLc(amountLc.toString());
                    listDetail.add(bankDetailReportResponse);
                    sumTotalOrder[0] = sumTotalOrder[0] + value.size();
                    sumAmountPay[0] = sumAmountPay[0].add(amountPay);
                    sumAmountMinusFc[0] = sumAmountMinusFc[0].add(amountMinusFc);
                    sumAmountLc[0] = sumAmountLc[0].add(amountLc);
                }
                bankReportResponse.setDetail(listDetail);
                bankReportResponse.setSumTotalOrder(sumTotalOrder[0]);
                bankReportResponse.setSumAmountPay(sumAmountPay[0].toString());
                bankReportResponse.setSumAmountMinusFc(sumAmountMinusFc[0].toString());
                bankReportResponse.setSumAmountLc(sumAmountLc[0].toString());
                bankList.add(bankReportResponse);


                sumTotalOrderAll[0] = sumTotalOrderAll[0] + sumTotalOrder[0];
                sumAmountPayAll[0] = sumAmountPayAll[0].add(sumAmountPay[0]);
                sumAmountMinusFcAll[0] = sumAmountMinusFcAll[0].add(sumAmountMinusFc[0]);
                sumAmountLcAll[0] = sumAmountLcAll[0].add(sumAmountLc[0]);

                paymentBankReportResponse.setSumCurrency(v.get(0).getCurrency());
                paymentBankReportResponse.setSumLcurrency(v.get(0).getCurrency());

            });

            paymentBankReportResponse.setSumTotalOrder(sumTotalOrderAll[0]);
            paymentBankReportResponse.setSumAmountPay(sumAmountPayAll[0].toString());
            paymentBankReportResponse.setSumAmountMinusFc(sumAmountMinusFcAll[0].toString());
            paymentBankReportResponse.setSumAmountLc(sumAmountLcAll[0].toString());


            paymentBankReportResponse.setBank(bankList);
            result.setStatus(HttpStatus.OK.value());
            result.setData(paymentBankReportResponse);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public List<PaymentReport> findDocumentPayment(Long paymentAliasId) {
        return paymentReportRepository.findPaymentDocumentAllByPaymentAliasIdAndIsProposalAndIsChild(paymentAliasId, false, false);
    }

    public List<PaymentReport> findProposalDocument(Long paymentAliasId) {
        return paymentReportRepository.findProposalAllByPaymentAliasIdAndIsProposalAndIsChild(paymentAliasId, true, false);
    }

    private String groupByMultipleKey(PaymentReport paymentReport) {

        return paymentReport.getVendorCode() + ":" + paymentReport.getBankAccountNo();
    }

    @Override
    public ResponseEntity<Result<List<DuplicatePaymentReportResponse>>> findAllDuplicatePaymentReport(DuplicatePaymentReport request) {
        Result<List<DuplicatePaymentReportResponse>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<DuplicatePaymentReportResponse> listSummaryFromTR1 = paymentReportRepository.findAllDuplicatePaymentReport(request);
            result.setData(listSummaryFromTR1);
            result.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
