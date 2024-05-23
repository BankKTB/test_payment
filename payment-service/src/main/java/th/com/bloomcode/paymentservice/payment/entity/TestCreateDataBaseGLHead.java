package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.JsonDateSerializer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "GL_HEAD")
public class TestCreateDataBaseGLHead {

    @Id
    private Long id;
    private Timestamp created;
    private String createdBy;
    private Timestamp updated;
    private String updatedBy;


    private String documentType;
    private String companyCode;
    private Timestamp dateDoc;
    private Timestamp dateAcct;
    private int period;
    private String currency;
    private BigDecimal amount;
    private String paymentCenter;
    private String brDocumentNo;
    private String poDocumentNo;
    private String invoiceDocumentNo;
    private String reverseInvoiceDocumentNo;
    private String originalDocumentNo;
    private String originalFiscalYear;
    private String reverseOriginalDocumentNo;
    private String reverseOriginalFiscalYear;
    private String paymentMethod;
    private String costCenter1;
    private String costCenter2;
    private String headerReference;
    private String documentHeaderText;
    private String headerReference2;
    private Timestamp reverseDateAcct;
    private String reverseReason;
    private Timestamp documentCreated;
    private String userPark;
    private String userPost;
    private String originalDocument;
    private String referenceInterDocumentNo;
    private String referenceInterCompanyCode;
    private String referenceAutoGen;
    private String documentStatus;
    private String rpApproved;
    private String documentBaseType;
    private String paymentDocumentNo;
    private Long paymentId;
}
