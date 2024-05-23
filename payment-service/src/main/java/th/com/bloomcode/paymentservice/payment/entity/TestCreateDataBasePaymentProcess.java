package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.PaymentAlias;
import th.com.bloomcode.paymentservice.model.payment.PaymentProcess;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "PAYMENT_PROCESS")
public class TestCreateDataBasePaymentProcess {

    @Id
    private Long id;
    private String accountType;
    private String assetNo;
    private String assetSubNo;
    private String assignment;
    private String bankAccountNo;
    private String brDocumentNo;
    private int brLine;
    private String budgetAccount;
    private String budgetActivity;
    private String budgetActivityName;
    private String costCenter;
    private String costCenterName;
    private String currency;
    private Timestamp dateAcct;
    private Timestamp dateBaseLine;
    private Timestamp dateDoc;
    private String drCr;
    private String errorCode;
    private String fiArea;
    private String fundCenter;
    private String fundCenterName;
    private String fundSource;
    private String fundSourceName;
    private String glAccount;
    private String glAccountName;
    private String headerReference;
    private String houseBank;
    private String idemStatus;
    private BigDecimal invoiceAmount;
    private String invoiceCompanyCode;
    private String invoiceCompanyName;
    private String invoiceDocumentNo;
    private String invoiceDocumentType;
    private String invoiceFiscalYear;
    private String invoicePaymentCenter;
    private BigDecimal invoiceWtxAmount;
    private BigDecimal invoiceWtxAmountP;
    private BigDecimal invoiceWtxBase;
    private BigDecimal invoiceWtxBaseP;
    private boolean child;
    private int line;
    private String lineItemText;
    private BigDecimal originalAmount;
    private String originalCompanyCode;
    private String originalCompanyName;
    private String originalDocumentNo;
    private String originalDocumentType;
    private String originalFiscalYear;
    private String originalPaymentCenter;
    private BigDecimal originalWtxAmount;
    private BigDecimal originalWtxAmountP;
    private BigDecimal originalWtxBase;
    private BigDecimal originalWtxBaseP;
    private String parentCompanyCode;
    private String parentDocumentNo;
    private String parentFiscalYear;
    private String payeeCode;
    private String payingCompanyCode;
    private String paymentBlock;
    private String paymentCenter;
    private String paymentCompanyCode;
    private Timestamp paymentDate;
    private Timestamp paymentDateAcct;
    private String paymentDocumentNo;
    private String paymentFiscalYear;
    private String paymentMethod;
    private String paymentMethodName;
    private String paymentName;
    private String paymentReference;
    private String paymentTerm;
    private String pmGroupDoc;
    private String pmGroupNo;
    private String poDocumentNo;
    private int poLine;
    private String postingKey;
    private boolean proposal;
    private boolean proposalBlock;
    private String reference1;
    private String reference2;
    private String reference3;
    private String specialGL;
    private String specialGLName;
    private String status;
    private String tradingPartner;
    private String tradingPartnerName;
    private Long paymentAliasId;

}
