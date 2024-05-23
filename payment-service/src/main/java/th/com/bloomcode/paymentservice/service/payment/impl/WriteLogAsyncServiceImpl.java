package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.idem.entity.Wtx;
import th.com.bloomcode.paymentservice.model.Message;
import th.com.bloomcode.paymentservice.model.config.ParameterConfig;
import th.com.bloomcode.paymentservice.model.config.VendorConfig;
import th.com.bloomcode.paymentservice.model.payment.*;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;
import th.com.bloomcode.paymentservice.service.payment.GLLineService;
import th.com.bloomcode.paymentservice.service.payment.PaymentProposalLogService;
import th.com.bloomcode.paymentservice.service.payment.PaymentRealRunLogService;
import th.com.bloomcode.paymentservice.service.payment.WriteLogAsyncService;
import th.com.bloomcode.paymentservice.util.MessageConstant;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class WriteLogAsyncServiceImpl implements WriteLogAsyncService {

    private final GLLineService glLineService;
    private final PaymentProposalLogService paymentProposalLogService;
    private final PaymentRealRunLogService paymentRealRunLogService;

    public WriteLogAsyncServiceImpl(GLLineService glLineService, PaymentProposalLogService paymentProposalLogService, PaymentRealRunLogService paymentRealRunLogService) {
        this.glLineService = glLineService;
        this.paymentProposalLogService = paymentProposalLogService;
        this.paymentRealRunLogService = paymentRealRunLogService;
    }

    @Override
    public void writeLogPrepareProposalDocumentNew(PaymentAlias paymentAlias, ParameterConfig parameterConfig, List<PrepareSelectProposalDocument> prepareSelectProposalDocument, WSWebInfo webInfo) {
        Map<String, List<PrepareSelectProposalDocument>> paymentGroupCompCodeAndVendor = prepareSelectProposalDocument.stream()
                .filter(p -> !"E".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.groupingBy(this::groupingByCompCodeAndVendor, Collectors.mapping((PrepareSelectProposalDocument p) -> p, toList())));
        Map<String, List<PrepareSelectProposalDocument>> paymentGroupPaymentMethod = prepareSelectProposalDocument.stream()
                .filter(p -> !"E".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.groupingBy(PrepareSelectProposalDocument::getPaymentMethod, Collectors.mapping((PrepareSelectProposalDocument p) -> p, toList())));
        List<PrepareSelectProposalDocument> paymentError = prepareSelectProposalDocument.stream()
                .filter(p -> "E".equalsIgnoreCase(p.getStatus())).collect(toList());
        log.info("paymentGroupCompCode : {}", paymentGroupCompCodeAndVendor.size());
        log.info("paymentGroupPaymentMethod : {}", paymentGroupPaymentMethod.size());
        log.info("paymentError : {}", paymentError.size());
//        this.paymentProposalLogService.clearLog();
//
//    AtomicLong paymentProposalLogId = new AtomicLong(0L);
        AtomicInteger seq = new AtomicInteger(1);

        List<PaymentProposalLog> paymentProposalLogs = new ArrayList<>();

        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_516.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                Message.MESSAGE_516.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_550.getMessageText(), 1, "PAY SYSTEMS",
                        paymentAlias.getPaymentName(), webInfo.getUserWebOnline()),
                MessageConstant.MESSAGE_CLASS_00, Message.MESSAGE_550.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_402.getMessageText(), paymentAlias.getPaymentDate(), true,
                        paymentAlias.getPaymentName()),
                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_402.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_FZ,
                Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        long startTimeMillis = System.currentTimeMillis();
        if (parameterConfig.getAdditionLog().isCheckBoxDueDate()) {
            paymentGroupCompCodeAndVendor.forEach((k, v) -> {
                String[] key = k.split("-");
                String compCode = key[0];
                String vendor = key[1];

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_691.getMessageText(), vendor, compCode),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_691.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                for (PrepareSelectProposalDocument prepareProposalDocument : v) {
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_799.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_799.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_700.getMessageText(),
                                    prepareProposalDocument.getOriginalDocumentNo(), prepareProposalDocument.getLine(), "THB",
                                    prepareProposalDocument.getAmount()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_700.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                    if (null != prepareProposalDocument.getPayee() && !prepareProposalDocument.getPayee().equalsIgnoreCase("")) {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_735.getMessageText(),
                                        prepareProposalDocument.getPayee()),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_735.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    }

                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_701.getMessageText(),
                                    prepareProposalDocument.getDateBaseLine(), 0, 0, 0, 0, 0),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_701.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_726.getMessageText(), 0),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_726.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
//			this.paymentProposalLogService.addSuccessLog(0L,seq++,
//							MessageFormat.format(Message.MESSAGE_719.getMessageText(), "3200006", 2),
//							MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_719.getMessageNo(), paymentAlias.getId(),true);
//			this.paymentProposalLogService.addSuccessLog(0L,seq++,
//							MessageFormat.format(Message.MESSAGE_720.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_FZ,
//							Message.MESSAGE_720.getMessageNo(), paymentAlias.getId(),true);

                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_728.getMessageText(), parameterConfig.getParameter().getPostDate(), parameterConfig.getParameter().getPaymentDate()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_728.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_721.getMessageText(), 0),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_721.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
//				this.paymentProposalLogService.addSuccessLog(0L,seq++,
//								MessageFormat.format(Message.MESSAGE_708.getMessageText(), new Date(), new Date()),
//								MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_708.getMessageNo(), paymentAlias.getId(),true);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                }
            });
        }
        long endTimeMillis = System.currentTimeMillis();
        long executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท log isCheckBoxDueDate: " + executionTimeSeconds + " วินาที");
        startTimeMillis = System.currentTimeMillis();
        if (parameterConfig.getAdditionLog().isCheckBoxPaymentMethodAll()) {
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_699.getMessageText(), ""),
                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_699.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            startTimeMillis = System.currentTimeMillis();
            paymentError.forEach((v) -> {
                BigDecimal sumAmountPerPM = v.getAmount();
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                if (null != v.getErrorCode()) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_500.getMessageText(), v.getOriginalDocumentNo(), v.getCompanyCode(), v.getOriginalFiscalYear()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_500.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("003")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_503.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_503.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("012")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_512.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_512.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("031")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_531.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_531.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("033")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_532.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_532.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("034")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_534.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_534.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("036")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_536.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_536.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("016")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_518.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_518.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("024")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_524.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_524.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("071")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_571.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_571.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("072")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_572.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_572.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("073")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_573.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_573.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_398.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_517.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            });
            endTimeMillis = System.currentTimeMillis();
            executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
            log.info("เวลาการทำงานอัพเดท log paymentError: " + executionTimeSeconds + " วินาที");
            startTimeMillis = System.currentTimeMillis();
            paymentGroupPaymentMethod.forEach((k, v) -> {
                BigDecimal sumAmountPerPM = v.stream().map(PrepareSelectProposalDocument::getAmount).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_603.getMessageText(), k),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_603.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_640.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_640.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_644.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_644.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                long startTimeMillis2 = System.currentTimeMillis();
                for (PrepareSelectProposalDocument prepareProposalDocument : v) {
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_645.getMessageText(),
                                    prepareProposalDocument.getPayee() != null? prepareProposalDocument.getAlternativeCountryCode(): prepareProposalDocument.getMainCountryCode(),prepareProposalDocument.getPayee() != null? prepareProposalDocument.getAlternativePayeeBankKey(): prepareProposalDocument.getMainPayeeBankKey(),
                                    prepareProposalDocument.getBankAccountNo()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_645.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                }
                long endTimeMillis2 = System.currentTimeMillis();
                long executionTimeSeconds2 = (endTimeMillis2 - startTimeMillis2) / 1000; // แปลงเวลาเป็นวินาที
                log.info("เวลาการทำงานอัพเดท log for1: " + executionTimeSeconds2 + " วินาที");

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_665.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_665.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                startTimeMillis2 = System.currentTimeMillis();
                for (PrepareSelectProposalDocument prepareProposalDocument : v) {
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_663.getMessageText(),
                                    prepareProposalDocument.getPayee() != null? prepareProposalDocument.getAlternativePayeeBankKey(): prepareProposalDocument.getMainPayeeBankKey(), prepareProposalDocument.getBankAccountNo(),
                                    "", ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_663.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                }
                endTimeMillis2 = System.currentTimeMillis();
                executionTimeSeconds2 = (endTimeMillis2 - startTimeMillis2) / 1000; // แปลงเวลาเป็นวินาที
                log.info("เวลาการทำงานอัพเดท log for2: " + executionTimeSeconds2 + " วินาที");

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_668.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_668.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_648.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_648.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_658.getMessageText(), "THB", "0",
                                "999999999999999.00"),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_658.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_641.getMessageText(), "", ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_641.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_642.getMessageText(), "TH", "", ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_642.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_609.getMessageText(), k),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_609.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            });
            endTimeMillis = System.currentTimeMillis();
            executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
            log.info("เวลาการทำงานอัพเดท log paymentError: " + executionTimeSeconds + " วินาที");
        }
        endTimeMillis = System.currentTimeMillis();
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท log isCheckBoxPaymentMethodAll: " + executionTimeSeconds + " วินาที");
        startTimeMillis = System.currentTimeMillis();
        if (parameterConfig.getAdditionLog().isCheckBoxPaymentMethodUnSuccess()) {
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_699.getMessageText(), ""),
                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_699.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

            paymentError.forEach((v) -> {
                BigDecimal sumAmountPerPM = v.getAmount();
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                if (null != v.getErrorCode()) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_500.getMessageText(), v.getOriginalDocumentNo(), v.getCompanyCode(), v.getOriginalFiscalYear()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_500.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (v.getErrorCode().equalsIgnoreCase("003")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_503.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_503.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (v.getErrorCode().equalsIgnoreCase("012")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_512.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_512.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (v.getErrorCode().equalsIgnoreCase("031")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_531.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_531.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("033")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_532.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_532.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("034")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_534.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_534.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("036")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_536.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_536.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (v.getErrorCode().equalsIgnoreCase("016")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_518.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_518.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (v.getErrorCode().equalsIgnoreCase("024")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_524.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_524.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("071")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_571.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_571.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("072")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_572.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_572.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("073")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_573.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_573.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_398.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_517.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            });
        }
        endTimeMillis = System.currentTimeMillis();
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท log isCheckBoxPaymentMethodUnSuccess: " + executionTimeSeconds + " วินาที");
        startTimeMillis = System.currentTimeMillis();
        if (parameterConfig.getAdditionLog().isCheckBoxDisplayDetail()) {
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_798.getMessageText(), ""),
                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_798.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            startTimeMillis = System.currentTimeMillis();
            prepareSelectProposalDocument.forEach(prepareProposalDocument -> {
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_741.getMessageText(), prepareProposalDocument.getPaymentClearingDocNo(), prepareProposalDocument.getCompanyCode(), prepareProposalDocument.getCurrency(), prepareProposalDocument.getPaymentMethod()),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_741.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_743.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_743.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

//                GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(prepareProposalDocument.getCompanyCode(), prepareProposalDocument.getOriginalDocumentNo(), prepareProposalDocument.getOriginalFiscalYear(), "K");
                PayMethodConfig payMethodConfig = Context.sessionPayMethodConfig.get("TH-" + prepareProposalDocument.getPaymentMethod());

                String fundSource = "";
              if (prepareProposalDocument.getFundSource().substring(2, 4).equalsIgnoreCase("10")) {
                fundSource = prepareProposalDocument.getFundSource().substring(2, 4);
              } else {
                fundSource = prepareProposalDocument.getFundSource().substring(2, 5);
              }
              GLAccount glAccount;
                if (payMethodConfig.getDocumentTypeForPayment().equalsIgnoreCase("PB") || payMethodConfig.getDocumentTypeForPayment().equalsIgnoreCase("PD")) {
                    glAccount = Context.sessionGlAccountForPayment.get(payMethodConfig.getDocumentTypeForPayment() + "-**");
                    if (null == glAccount) {
                        glAccount = new GLAccount();
                        glAccount.setAgencyGlAccount("-");
                        glAccount.setCentralGlAccount("-");
                    }

                } else {
                    String fundSource1 = fundSource.length() == 2 ? StringUtils.leftPad(fundSource, 4, "_") : StringUtils.leftPad(fundSource, 5, "_");
                    String fundSource2 = StringUtils.rightPad(fundSource1, 7, "_");
                    glAccount = Context.sessionGlAccountForPayment.get(payMethodConfig.getDocumentTypeForPayment() + "-" + fundSource2);
                    if (null == glAccount) {
                        glAccount = new GLAccount();
                        glAccount.setAgencyGlAccount("-");
                        glAccount.setCentralGlAccount("-");
                    }
                }

                if (!prepareProposalDocument.getCompanyCode().equalsIgnoreCase("99999")) {

                    if (null != prepareProposalDocument.getWtxCode()) {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        Wtx wtx = Context.sessionWtx.get(prepareProposalDocument.getWtxCode());

                        if (null != wtx) {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", wtx.getGlAccount(), prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        } else {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", "-", prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        }


                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 003, "50", glAccount.getAgencyGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                    } else {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", glAccount.getAgencyGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    }
                } else {

                    if (null != prepareProposalDocument.getWtxCode()) {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        Wtx wtx = Context.sessionWtx.get(prepareProposalDocument.getWtxCode());

                        if (null != wtx) {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", wtx.getGlAccount(), prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                        } else {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", "-", prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                        }

                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 003, "50", glAccount.getCentralGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                    } else {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", glAccount.getCentralGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    }
                }


            });
            endTimeMillis = System.currentTimeMillis();
            executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
            log.info("เวลาการทำงานอัพเดท log forEach: " + executionTimeSeconds + " วินาที");
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                    Message.MESSAGE_398.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                    Message.MESSAGE_517.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        }
        endTimeMillis = System.currentTimeMillis();
        executionTimeSeconds = (endTimeMillis - startTimeMillis) / 1000; // แปลงเวลาเป็นวินาที
        log.info("เวลาการทำงานอัพเดท log isCheckBoxDisplayDetail: " + executionTimeSeconds + " วินาที");
        this.paymentProposalLogService.saveBatch(paymentProposalLogs);
    }


    @Async
    public void writeLogPaymentRealRun(PaymentAlias paymentAlias, List<PrepareRunDocument> prepareRunDocumentList, WSWebInfo webInfo) {

        log.info("writeLogPaymentRealRun");

//        AtomicLong paymentRealRunLogId = new AtomicLong(this.paymentRealRunLogService.getNextSeries());
        AtomicLong seq = new AtomicLong(1);

        List<PaymentRealRunLog> paymentRealRunLog = new ArrayList<>();

        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_516.getMessageText(), ""), paymentAlias.getId(), paymentRealRunLog);

        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_550.getMessageText(), 1, "PAY SYSTEMS",
                        paymentAlias.getPaymentName(), webInfo.getUserWebOnline()),
                paymentAlias.getId(), paymentRealRunLog);
        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_402.getMessageText(), paymentAlias.getPaymentDate(),
                        paymentAlias.getPaymentName()), paymentAlias.getId(), paymentRealRunLog);
        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""), paymentAlias.getId(), paymentRealRunLog);

        this.paymentRealRunLogService.saveBatch(paymentRealRunLog);
//        this.paymentRealRunLogService.updateNextSeries(Long.parseLong(paymentRealRunLogId.toString()));

    }

    @Override
    public void writeLogPrepareProposalDocumentJob(PaymentAlias paymentAlias, ParameterConfig parameterConfig, List<PrepareProposalDocument> prepareProposalDocuments, WSWebInfo webInfo) {
        Map<String, List<PrepareProposalDocument>> paymentGroupCompCodeAndVendor = prepareProposalDocuments.stream()
                .filter(p -> !"E".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.groupingBy(this::groupingByCompCodeAndVendor, Collectors.mapping((PrepareProposalDocument p) -> p, toList())));
        Map<String, List<PrepareProposalDocument>> paymentGroupPaymentMethod = prepareProposalDocuments.stream()
                .filter(p -> !"E".equalsIgnoreCase(p.getStatus()))
                .collect(Collectors.groupingBy(PrepareProposalDocument::getPaymentMethod, Collectors.mapping((PrepareProposalDocument p) -> p, toList())));
        List<PrepareProposalDocument> paymentError = prepareProposalDocuments.stream()
                .filter(p -> "E".equalsIgnoreCase(p.getStatus())).collect(toList());
////        logger.info("paymentGroupCompCode : {}", paymentGroupCompCode);
////        logger.info("paymentGroupPaymentMethod : {}", paymentGroupPaymentMethod);
////        logger.info("paymentError : {}", paymentError);
//        this.paymentProposalLogService.clearLog();
//
//    AtomicLong paymentProposalLogId = new AtomicLong(0L);
        AtomicInteger seq = new AtomicInteger(1);

        List<PaymentProposalLog> paymentProposalLogs = new ArrayList<>();

        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_516.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                Message.MESSAGE_516.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_550.getMessageText(), 1, "PAY SYSTEMS",
                        paymentAlias.getPaymentName(), webInfo.getUserWebOnline()),
                MessageConstant.MESSAGE_CLASS_00, Message.MESSAGE_550.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_402.getMessageText(), paymentAlias.getPaymentDate(), true,
                        paymentAlias.getPaymentName()),
                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_402.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_FZ,
                Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

        if (parameterConfig.getAdditionLog().isCheckBoxDueDate()) {
            paymentGroupCompCodeAndVendor.forEach((k, v) -> {
                String[] key = k.split("-");
                String compCode = key[0];
                String vendor = key[1];

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_691.getMessageText(), vendor, compCode),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_691.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                for (PrepareProposalDocument prepareProposalDocument : v) {
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_799.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_799.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_700.getMessageText(),
                                    prepareProposalDocument.getOriginalDocumentNo(), prepareProposalDocument.getLine(), "THB",
                                    prepareProposalDocument.getAmount()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_700.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                    if (null != prepareProposalDocument.getPayee() && !prepareProposalDocument.getPayee().equalsIgnoreCase("")) {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_735.getMessageText(),
                                        prepareProposalDocument.getPayee()),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_735.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    }

                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_701.getMessageText(),
                                    prepareProposalDocument.getDateBaseLine(), 0, 0, 0, 0, 0),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_701.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_726.getMessageText(), 0),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_726.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
//			this.paymentProposalLogService.addSuccessLog(0L,seq++,
//							MessageFormat.format(Message.MESSAGE_719.getMessageText(), "3200006", 2),
//							MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_719.getMessageNo(), paymentAlias.getId(),true);
//			this.paymentProposalLogService.addSuccessLog(0L,seq++,
//							MessageFormat.format(Message.MESSAGE_720.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_FZ,
//							Message.MESSAGE_720.getMessageNo(), paymentAlias.getId(),true);

                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_728.getMessageText(), parameterConfig.getParameter().getPostDate(), parameterConfig.getParameter().getPaymentDate()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_728.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_721.getMessageText(), 0),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_721.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
//				this.paymentProposalLogService.addSuccessLog(0L,seq++,
//								MessageFormat.format(Message.MESSAGE_708.getMessageText(), new Date(), new Date()),
//								MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_708.getMessageNo(), paymentAlias.getId(),true);
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                }
            });
        }
        if (parameterConfig.getAdditionLog().isCheckBoxPaymentMethodAll()) {
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_699.getMessageText(), ""),
                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_699.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

            paymentError.forEach((v) -> {
                BigDecimal sumAmountPerPM = v.getAmount();
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                if (null != v.getErrorCode()) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_500.getMessageText(), v.getOriginalDocumentNo(), v.getCompanyCode(), v.getOriginalFiscalYear()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_500.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("003")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_503.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_503.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("012")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_512.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_512.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("031")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_531.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_531.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("016")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_518.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_518.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (null != v.getErrorCode() && v.getErrorCode().equalsIgnoreCase("024")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_524.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_524.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_398.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_517.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            });

            paymentGroupPaymentMethod.forEach((k, v) -> {
                BigDecimal sumAmountPerPM = v.stream().map(PrepareProposalDocument::getAmount).reduce(BigDecimal.ZERO,
                        BigDecimal::add);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_603.getMessageText(), k),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_603.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_640.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_640.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_644.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_644.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                for (PrepareProposalDocument prepareProposalDocument : v) {
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_645.getMessageText(),
                                    prepareProposalDocument.getCountryCode(), prepareProposalDocument.getPayeeBankKey(),
                                    prepareProposalDocument.getBankAccountNo()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_645.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                }

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_665.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_665.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                for (PrepareProposalDocument prepareProposalDocument : v) {
                    this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_663.getMessageText(),
                                    prepareProposalDocument.getPayeeBankKey(), prepareProposalDocument.getBankAccountNo(),
                                    "", ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_663.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                }

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_668.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_668.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_648.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_648.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_658.getMessageText(), "THB", "0",
                                "999999999999999.00"),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_658.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_641.getMessageText(), "", ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_641.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_642.getMessageText(), "TH", "", ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_642.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_609.getMessageText(), k),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_609.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_693.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            });
        }

        if (parameterConfig.getAdditionLog().isCheckBoxPaymentMethodUnSuccess()) {
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_699.getMessageText(), ""),
                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_699.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

            paymentError.forEach((v) -> {
                BigDecimal sumAmountPerPM = v.getAmount();
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_601.getMessageText(), "THB", sumAmountPerPM),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_601.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                if (null != v.getErrorCode()) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_500.getMessageText(), v.getOriginalDocumentNo(), v.getCompanyCode(), v.getOriginalFiscalYear()),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_500.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (v.getErrorCode().equalsIgnoreCase("003")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_503.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_503.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (v.getErrorCode().equalsIgnoreCase("012")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_512.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_512.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }
                if (v.getErrorCode().equalsIgnoreCase("031")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_531.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_531.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (v.getErrorCode().equalsIgnoreCase("016")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_518.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_518.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                if (v.getErrorCode().equalsIgnoreCase("024")) {
                    this.paymentProposalLogService.addErrorLog(0L, seq.getAndIncrement(),
                            MessageFormat.format(Message.MESSAGE_524.getMessageText(), ""),
                            MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_524.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                }

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_398.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                        Message.MESSAGE_517.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            });
        }
        if (parameterConfig.getAdditionLog().isCheckBoxDisplayDetail()) {
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_798.getMessageText(), ""),
                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_798.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

            prepareProposalDocuments.forEach(prepareProposalDocument -> {
                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_741.getMessageText(), prepareProposalDocument.getPaymentClearingDocNo(), prepareProposalDocument.getCompanyCode(), prepareProposalDocument.getCurrency(), prepareProposalDocument.getPaymentMethod()),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_741.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                        MessageFormat.format(Message.MESSAGE_743.getMessageText(), ""),
                        MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_743.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

//                GLLine glLine = glLineService.findOneByCompanyCodeAndOriginalDocumentNoAndOriginalFiscalYearAndAccountType(prepareProposalDocument.getCompanyCode(), prepareProposalDocument.getOriginalDocumentNo(), prepareProposalDocument.getOriginalFiscalYear(), "K");
                PayMethodConfig payMethodConfig = Context.sessionPayMethodConfig.get("TH-" + prepareProposalDocument.getPaymentMethod());

                String fundSource = "";
              if (prepareProposalDocument.getFundSource().substring(2, 4).equalsIgnoreCase("10")) {
                fundSource = prepareProposalDocument.getFundSource().substring(2, 4);
              } else {
                fundSource = prepareProposalDocument.getFundSource().substring(2, 5);
              }
              GLAccount glAccount;
                if (payMethodConfig.getDocumentTypeForPayment().equalsIgnoreCase("PB") || payMethodConfig.getDocumentTypeForPayment().equalsIgnoreCase("PD")) {
                    glAccount = Context.sessionGlAccountForPayment.get(payMethodConfig.getDocumentTypeForPayment() + "-**");
                    if (null == glAccount) {
                        glAccount = new GLAccount();
                        glAccount.setAgencyGlAccount("-");
                        glAccount.setCentralGlAccount("-");
                    }

                } else {
                    String fundSource1 = fundSource.length() == 2 ? StringUtils.leftPad(fundSource, 4, "_") : StringUtils.leftPad(fundSource, 5, "_");
                    String fundSource2 = StringUtils.rightPad(fundSource1, 7, "_");
                    glAccount = Context.sessionGlAccountForPayment.get(payMethodConfig.getDocumentTypeForPayment() + "-" + fundSource2);
                    if (null == glAccount) {
                        glAccount = new GLAccount();
                        glAccount.setAgencyGlAccount("-");
                        glAccount.setCentralGlAccount("-");
                    }
                }

                if (!prepareProposalDocument.getCompanyCode().equalsIgnoreCase("99999")) {

                    if (null != prepareProposalDocument.getWtxCode()) {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        Wtx wtx = Context.sessionWtx.get(prepareProposalDocument.getWtxCode());

                        if (null != wtx) {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", wtx.getGlAccount(), prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        } else {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", "-", prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        }


                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 003, "50", glAccount.getAgencyGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                    } else {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", glAccount.getAgencyGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    }
                } else {

                    if (null != prepareProposalDocument.getWtxCode()) {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        Wtx wtx = Context.sessionWtx.get(prepareProposalDocument.getWtxCode());

                        if (null != wtx) {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", wtx.getGlAccount(), prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                        } else {
                            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                    MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", "-", prepareProposalDocument.getWtxAmount(), BigDecimal.ZERO),
                                    MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                        }

                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 003, "50", glAccount.getCentralGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                    } else {
                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 001, "25", prepareProposalDocument.getGlAccount(), prepareProposalDocument.getAmount(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);

                        this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                                MessageFormat.format(Message.MESSAGE_744.getMessageText(), 002, "50", glAccount.getCentralGlAccount(), prepareProposalDocument.getAmountPaid(), BigDecimal.ZERO),
                                MessageConstant.MESSAGE_CLASS_FZ, Message.MESSAGE_744.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
                    }
                }


            });

            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_398.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                    Message.MESSAGE_398.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
            this.paymentProposalLogService.addSuccessLog(0L, seq.getAndIncrement(),
                    MessageFormat.format(Message.MESSAGE_517.getMessageText(), ""), MessageConstant.MESSAGE_CLASS_00,
                    Message.MESSAGE_517.getMessageNo(), paymentAlias.getId(), true, paymentProposalLogs);
        }

        this.paymentProposalLogService.saveBatch(paymentProposalLogs);
    }

    @Override
    public void writeLogPaymentRealRunJob(PaymentAlias paymentAlias, List<PrepareRunDocument> prepareRunDocumentList, WSWebInfo webInfo) {
        log.info("writeLogPaymentRealRun");

//        AtomicLong paymentRealRunLogId = new AtomicLong(this.paymentRealRunLogService.getNextSeries());
        AtomicLong seq = new AtomicLong(1);

        List<PaymentRealRunLog> paymentRealRunLog = new ArrayList<>();

        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_516.getMessageText(), ""), paymentAlias.getId(), paymentRealRunLog);

        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_550.getMessageText(), 1, "PAY SYSTEMS",
                        paymentAlias.getPaymentName(), webInfo.getUserWebOnline()),
                paymentAlias.getId(), paymentRealRunLog);
        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_402.getMessageText(), paymentAlias.getPaymentDate(),
                        paymentAlias.getPaymentName()), paymentAlias.getId(), paymentRealRunLog);
        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""), paymentAlias.getId(), paymentRealRunLog);

        this.paymentRealRunLogService.saveBatch(paymentRealRunLog);
//        this.paymentRealRunLogService.updateNextSeries(Long.parseLong(paymentRealRunLogId.toString()));

    }

    private String groupingByCompCodeAndVendor(PrepareProposalDocument prepareProposalDocument) {
        return prepareProposalDocument.getCompanyCode() + "-" + prepareProposalDocument.getVendor();
    }
    private String groupingByCompCodeAndVendor(PrepareSelectProposalDocument prepareSelectProposalDocument) {
        return prepareSelectProposalDocument.getCompanyCode() + "-" + prepareSelectProposalDocument.getVendor();
    }

    public boolean inRange(List<VendorConfig> vendors, String value) {
        for (VendorConfig vendor : vendors) {
            if (null != vendor.getVendorTaxIdFrom() && null != vendor.getVendorTaxIdTo() && value.compareToIgnoreCase(vendor.getVendorTaxIdFrom()) >= 0 && value.compareToIgnoreCase(vendor.getVendorTaxIdTo()) <= 0) {
                return true;
            } else if (null != vendor.getVendorTaxIdFrom() && value.compareToIgnoreCase(vendor.getVendorTaxIdFrom()) == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void writeLogPaymentRealRunNew(PaymentAlias paymentAlias, List<PrepareRealRunDocument> prepareRealRunDocumentList, WSWebInfo webInfo) {
        log.info("writeLogPaymentRealRun");

//        AtomicLong paymentRealRunLogId = new AtomicLong(this.paymentRealRunLogService.getNextSeries());
        AtomicLong seq = new AtomicLong(1);

        List<PaymentRealRunLog> paymentRealRunLog = new ArrayList<>();

        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_516.getMessageText(), ""), paymentAlias.getId(), paymentRealRunLog);

        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_550.getMessageText(), 1, "PAY SYSTEMS",
                        paymentAlias.getPaymentName(), webInfo.getUserWebOnline()),
                paymentAlias.getId(), paymentRealRunLog);
        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_402.getMessageText(), paymentAlias.getPaymentDate(),
                        paymentAlias.getPaymentName()), paymentAlias.getId(), paymentRealRunLog);
        this.paymentRealRunLogService.addSuccessLog(this.paymentRealRunLogService.getNextSeries(), seq.getAndIncrement(),
                MessageFormat.format(Message.MESSAGE_693.getMessageText(), ""), paymentAlias.getId(), paymentRealRunLog);

        this.paymentRealRunLogService.saveBatch(paymentRealRunLog);
//        this.paymentRealRunLogService.updateNextSeries(Long.parseLong(paymentRealRunLogId.toString()));

    }
}
