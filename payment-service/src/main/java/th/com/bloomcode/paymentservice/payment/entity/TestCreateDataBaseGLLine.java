package th.com.bloomcode.paymentservice.payment.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "GL_LINE")
public class TestCreateDataBaseGLLine {

    @Id
    private String id;
    private Timestamp created;
    private String createdBy;
    private Timestamp updated;
    private String updatedBy;

    private String postingKey;
    private String accountType;
    private String drCr;
    private String glAccount;
    private String fiArea;
    private String costCenter;
    private String fundSource;
    private String bgCode;
    private String bgActivity;
    private String costActivity;
    private BigDecimal amount;
    private String reference3;
    private String assignment;
    private String assignmentSpecialGL;
    private String brDocumentNo;
    private int brLine;
    private String paymentCenter;
    private String bankBook;
    private String subBook;
    private String subAccountType;
    private String subAccount;
    private String subAccountOwner;
    private String depositAccount;
    private String depositAccountOwner;
    private String gpsc;
    private String gpscGroup;
    private String lineItemText;
    private String lineDesc;
    private String paymentTerm;
    private String paymentMethod;
    private String wtxType;
    private String wtxCode;
    private BigDecimal wtxBase;
    private BigDecimal wtxAmount;
    private String wtxTypeP;
    private String wtxCodeP;
    private BigDecimal wtxBaseP;
    private BigDecimal wtxAmountP;
    private String vendor;
    private String vendorName;
    private boolean confirmVendor;
    private String vendorTaxId;
    private String payee;
    private String payeeTaxId;
    private String bankAccountNo;
    private String bankAccountHolderName;
    private String bankBranchNo;
    private String tradingPartner;
    private String tradingPartnerPark;
    private String specialGL;
    private Timestamp dateBaseLine;
    private Timestamp dueDate;
    private Timestamp dateValue;
    private String assetNo;
    private String assetSubNo;
    private BigDecimal qty;
    private String uom;
    private String reference1;
    private String reference2;
    private String companyCode;
    private String poDocumentNo;
    private int poLine;
    private String mrFiscalYear;
    private String mrDocumentNo;
    private int mrLine;
    private String invoiceFiscalYear;
    private String invoiceDocumentNo;
    private int invoiceLine;
    private String referenceInvoiceFiscalYear;
    private String referenceInvoiceDocumentNo;
    private int referenceInvoiceLine;
    private String clearingFiscalYear;
    private String clearingDocumentNo;
    private String clearingDocumentType;
    private Timestamp clearingDateDoc;
    private Timestamp clearingDateAcct;
    private String income;
    private String paymentBlock;
    private String paymentReference;
    private boolean autoGen;
    private boolean wtx;
    private int line;
    private String bgAccount;
    private String fundCenter;
    private String originalDocumentNo;
    private String originalFiscalYear;
    private String fundType;
    private Long glHeadId;
}
