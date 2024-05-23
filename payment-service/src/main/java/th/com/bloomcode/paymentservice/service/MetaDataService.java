package th.com.bloomcode.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.model.payment.InhouseFileDetail;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.SmartFileDetail;
import th.com.bloomcode.paymentservice.model.payment.SmartFileLog;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogService;
import th.com.bloomcode.paymentservice.service.payment.SmartFileDetailService;
import th.com.bloomcode.paymentservice.service.payment.SmartFileLogService;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@Slf4j
public class MetaDataService {

    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;
    private final SmartFileLogService smartFileLogService;
    private final SmartFileDetailService smartFileDetailService;
    private final ProposalLogService proposalLogService;

    public MetaDataService(@Qualifier("paymentEntityManagerFactory") EntityManager entityManager, @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory, SmartFileLogService smartFileLogService, SmartFileDetailService smartFileDetailService, ProposalLogService proposalLogService) {
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
        this.smartFileLogService = smartFileLogService;
        this.smartFileDetailService = smartFileDetailService;
        this.proposalLogService = proposalLogService;
    }

    public void saveAllSmartLog(List<SmartFileLog> smartFileLogs) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
        int cont = 0;
//        Long logId = getNextSeries("LOG");
//        Long detailId = getNextSeries("DETAIL");
//        String sql = "INSERT INTO STG_PAYMENT.SMART_FILE_LOG (ID, BANK_ACCOUNT_NO, BANK_KEY, COMP_CODE, CREDIT_MEMO, FEE, " +
//                " FI_AREA, FISCAL_YEAR, INV_DOC_NO, PAYING_COMP_CODE, PAYMENT_DATE, PAYMENT_DOC_NO, PAYMENT_METHOD, PAYMENT_NAME, " +
//                " PAYMENT_YEAR, TRANSFER_LEVEL, VENDOR, PAYMENT_ALIAS_ID, ORIGINAL_ACC_DOC_NO, ORIGINAL_COMP_CODE, ORIGINAL_DOC_TYPE, " +
//                " ORIGINAL_FISCAL_YEAR, ORIGINAL_WTX_AMOUNT, ORIGINAL_WTX_AMOUNT_P, ORIGINAL_WTX_BASE, ORIGINAL_WTX_BASE_P, " +
//                " PAYMENT_COMP_CODE, PAYMENT_FISCAL_YEAR) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//        smartFileLogService.saveAll(smartFileLogs);
        for (SmartFileLog smartFileLog : smartFileLogs) {

//            DBConnection.getJdbcTemplate("payment").update(sql, logId++, smartFileLog.getBankAccountNo(), smartFileLog.getBankKey(),
//                    smartFileLog.getCompCode(), smartFileLog.getCreditMemo(), smartFileLog.getFee(), smartFileLog.getFiArea(),
//                    smartFileLog.getFiscalYear(), smartFileLog.getInvDocNo(), smartFileLog.getPayingCompCode(), smartFileLog.getPaymentDate(),
//                    smartFileLog.getPaymentDocNo(), smartFileLog.getPaymentMethod(), smartFileLog.getPaymentName(), smartFileLog.getPaymentYear(),
//                    smartFileLog.getTransferLevel(), smartFileLog.getVendor(), smartFileLog.getPaymentAlias().getId(), smartFileLog.getOriginalAccDocNo(),
//                    smartFileLog.getOriginalCompCode(), smartFileLog.getOriginalDocType(), smartFileLog.getOriginalFiscalYear(), smartFileLog.getOriginalWtxAmount(),
//                    smartFileLog.getOriginalWtxAmountP(), smartFileLog.getOriginalWtxBase(), smartFileLog.getOriginalWtxBaseP(), smartFileLog.getPaymentCompCode(),
//                    smartFileLog.getPaymentFiscalYear());
//            smartFileLog.setId(logId++);
//            log.info("smartFileLog : {}", smartFileLog);
//            entityManager.persist(smartFileLog);
            log.info("count : {}", cont++);
            smartFileLog = smartFileLogService.save(smartFileLog);
            SmartFileDetail smartFileDetail = smartFileLog.getSmartFileDetail();
//            log.info("smart id : {}", smartFileDetail.getId());
//            smartFileDetail.setId(detailId++);
//            smartFileDetail.setSmartFileLogId(smartFileLog.getId());
            smartFileDetailService.save(smartFileDetail);
//            entityManager.persist(smartFileDetail);
//
//            cont++;
//            if (cont % 1000 == 0) {
//                entityManager.flush();
//                entityManager.clear();
////                entityManager.getTransaction().commit();
////                entityManager.getTransaction().begin();
//            }
        }
//        updateNextSeries(--logId, "LOG");
//        updateNextSeries(--detailId, "DETAIL");
//        entityManager.getTransaction().commit();
    }

    public void saveAllProposalLog(List<ProposalLog> proposalLogs) {
        try {
//            EntityManager entityManager = entityManagerFactory.createEntityManager();
//            entityManager.getTransaction().begin();
            int cont = 0;
//            Long proposalId = getNextSeries("PROPOSAL");
            for (ProposalLog proposalLog : proposalLogs) {
//                proposalLog.setId(proposalId++);
//                entityManager.persist(proposalLog);
                proposalLogService.save(proposalLog);

//                cont++;
//                if (cont % 1000 == 0) {
//                    entityManager.flush();
//                    entityManager.clear();
////                    entityManager.getTransaction().commit();
////                    entityManager.getTransaction().begin();
//                }
            }
//            updateNextSeries(--proposalId, "PROPOSAL");
//            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void saveAllInhouseDetail(List<InhouseFileDetail> inhouseFileDetails) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int cont = 0;
//        Long logId = getNextSeries(false);
//        Long detailId = getNextSeries(true);
        for (InhouseFileDetail inhouseFileDetail : inhouseFileDetails) {
//            smartFileLog.setId(logId++);
            entityManager.persist(inhouseFileDetail);

            cont++;
            if (cont % 1000 == 0) {
                entityManager.flush();
                entityManager.clear();
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
        }
//        updateNextSeries(--logId, false);
//        updateNextSeries(--detailId, true);
        entityManager.getTransaction().commit();
    }

    private Long getNextSeries(String seqName) {
        String sql = "";
        if ("DETAIL".equalsIgnoreCase(seqName)) {
            sql = "SELECT SMART_FILE_DETAIL_SEQ.NEXTVAL FROM DUAL";
        } else if ("LOG".equalsIgnoreCase(seqName)) {
            sql = "SELECT SMART_FILE_LOG_SEQ.NEXTVAL FROM DUAL";
        } else if ("PROPOSAL".equalsIgnoreCase(seqName)) {
            sql = "SELECT PROPOSAL_LOG_SEQ.NEXTVAL FROM DUAL";
        }
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    private void updateNextSeries(Long lastNumber, String seqName) {
        String sql = "";
        if ("DETAIL".equalsIgnoreCase(seqName)) {
            sql = "ALTER SEQUENCE SMART_FILE_DETAIL_SEQ RESTART START WITH " + lastNumber;
        } else if ("LOG".equalsIgnoreCase(seqName)) {
            sql = "ALTER SEQUENCE SMART_FILE_LOG_SEQ RESTART START WITH " + lastNumber;
        } else if ("PROPOSAL".equalsIgnoreCase(seqName)) {
            sql = "ALTER SEQUENCE PROPOSAL_LOG_SEQ RESTART START WITH " + lastNumber;
        }
        entityManager.createNativeQuery(sql).executeUpdate();
    }

    public Long getFileRunning(String fileType) {
        String sql = "";
        if ("SMART".equalsIgnoreCase(fileType)) {
            sql = "SELECT SMART_SEQ.NEXTVAL FROM DUAL";
        } else if ("SWIFT".equalsIgnoreCase(fileType)) {
            sql = "SELECT SWIFT_SEQ.NEXTVAL FROM DUAL";
        } else if ("GIRO".equalsIgnoreCase(fileType)) {
            sql = "SELECT GIRO_SEQ.NEXTVAL FROM DUAL";
        } else if ("INHOU".equalsIgnoreCase(fileType)) {
            sql = "SELECT INHOUSE_SEQ.NEXTVAL FROM DUAL";
        } else if ("PAIN".equalsIgnoreCase(fileType)) {
            sql = "SELECT PAIN_SEQ.NEXTVAL FROM DUAL";
        }
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    public Long getProposalRunning() {
        String sql = "SELECT PROPOSAL_RUNNING_SEQ.NEXTVAL FROM DUAL";
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }
}
