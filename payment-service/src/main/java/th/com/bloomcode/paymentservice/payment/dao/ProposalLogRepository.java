package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.ProposalLog;

import java.util.Date;

public interface ProposalLogRepository extends CrudRepository<ProposalLog, Long> {

    @Query("SELECT p FROM ProposalLog p")
    ProposalLog findFirstLog();

    boolean existsByPaymentDateAndPaymentName(Date paymentDate, String paymentName);

    boolean existsByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompCode, String paymentFiscalYear);

    ProposalLog findOneById(Long id);

    ProposalLog findOneByPaymentDocumentAndPaymentCompCodeAndPaymentFiscalYear(String paymentDocument, String paymentCompCode, String paymentFiscalYear);
    ProposalLog findOneByOriginalDocNoAndOriginalCompCodeAndOriginalFiscalYear(String originalDocNo, String originalCompCode, String originalFiscalYear);
    ProposalLog findAllByTransferDate(Date transferDate);

    ProposalLog findOneByFileName(String fileName);
    ProposalLog findOneByFileNameAndPaymentDocument(String fileName, String paymentDocument);

    ProposalLog findOneByRefRunningAndFileTypeAndTransferLevel(Long refRunning, String fileType, String transferLevel);

}
