package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogTR1;
import th.com.bloomcode.paymentservice.model.payment.dto.SummaryFromTR1;
import th.com.bloomcode.paymentservice.model.request.SummaryTR1Request;
import th.com.bloomcode.paymentservice.repository.payment.ProposalLogTR1Repository;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogTR1Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProposalLogTR1ServiceImpl implements ProposalLogTR1Service {

    private final ProposalLogTR1Repository proposalLogTR1Repository;

    @Override
    public void saveBatch(List<ProposalLogTR1> proposalLogTR1s) {
        proposalLogTR1Repository.saveBatch(proposalLogTR1s);
    }

    @Override
    public ResponseEntity<Result<List<SummaryFromTR1>>> getSummaryReportFromTR1(SummaryTR1Request request) {
        Result<List<SummaryFromTR1>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<SummaryFromTR1> listSummaryFromTR1 = proposalLogTR1Repository.summaryReportFromTR1(request);

            List<SummaryFromTR1> propLogToTR1s = listSummaryFromTR1.stream().filter(object -> "LEVEL3".equalsIgnoreCase(object.getRowLevel())).collect(Collectors.toList());

            System.out.println(propLogToTR1s.size());

            if (request.getUpdateTablePropLogTr1()) {
                // DELETE FOUNDED ITEMS
//                proposalLogTR1Repository.deleteAllByCriteria(request);
                Map<String, List<SummaryFromTR1>> listItem = propLogToTR1s.stream()
                        .collect(Collectors.groupingBy(this::groupingByCondition, Collectors.mapping((SummaryFromTR1 p) -> p, toList())));
                System.out.println(listItem.size());

                List<SummaryFromTR1> list = new ArrayList<>();
                if (listItem.size() > 0) {
                    listItem.forEach((key, value) -> {
                        log.info("SAVE TR1 {}", key);
                        System.out.println(value.size());
                        SummaryFromTR1 summaryFromTR1 = new SummaryFromTR1();

                        BeanUtils.copyProperties(value.get(0), summaryFromTR1);
                        BigDecimal sumAmount = new BigDecimal(value.stream()
                                .mapToDouble(amount -> Double.parseDouble(amount.getAmount().toString())).sum()).setScale(2,
                                RoundingMode.HALF_UP);
                        summaryFromTR1.setAmount(sumAmount);
                        list.add(summaryFromTR1);
                    });
                    log.info("SAVE TR1");
                    proposalLogTR1Repository.saveBatch(list, request);
                }
            }
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

    private String groupingByCondition(SummaryFromTR1 summaryFromTR1) {
        return summaryFromTR1.getTransferDate()
                + "-" + summaryFromTR1.getInvCompCode()
                + "-" + summaryFromTR1.getFundSource()
                + "-" + summaryFromTR1.getBudgetCode()
                + "-" + summaryFromTR1.getFileStatus();

    }
}
