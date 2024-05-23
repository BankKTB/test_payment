package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Data
public class JUDocument implements Serializable {
    @Id
    private Long id;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;
    @Column(name = "PAYMENT_NAME")
    private String paymentName;

    @Column(name = "DOC_TYPE")
    private String docType;

    @Column(name = "DOCUMENT_NO")
    private String documentNo;

    @Column(name = "REFERENCE")
    private String reference;


    @Column(name = "FISCAL_YEAR")
    private String fiscalYear;

    @Column(name = "COMPANY_CODE")
    private String companyCode;

    @Column(name = "PAYMENT_CENTER")
    private String paymentCenter;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "DOCUMENT_STATUS")
    private String documentStatus;

    @Column(name = "DOCUMENT_STATUS_NAME")
    private String documentStatusName;

    @Column(name = "DATE_DOC")
    private Timestamp dateDoc;

    @Column(name = "DATE_ACCT")
    private Timestamp dateAcct;

    @Column(name = "TRANSFER_DATE")
    private Timestamp transferDate;

    @Column(name = "AMOUNT_CR")
    private BigDecimal amountCr;

    @Column(name = "GL_ACCOUNT_CR")
    private String glAccountCr;

    @Column(name = "TEST_RUN")
    private boolean testRun;


    @Column(name = "REF_RUNNING")
    private Long refRunning;

    @Column(name = "PAY_ACCOUNT")
    private String payAccount;

    @Column(name = "REF_LINE")
    private int refLine;
    @Column(name = "ACCOUNT_NO_FROM")
    private String accountNoFrom;
    @Column(name = "ACCOUNT_NO_TO")
    private String accountNoTo;

    @Column(name = "FILE_TYPE")
    private String fileType;
    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "AMOUNT_DR")
    private BigDecimal amountDr;

    @Column(name = "GL_ACCOUNT_DR")
    private String glAccountDr;

    @Column(name = "GL_ACCOUNT_CR_NAME")
    private String glAccountCrName;

    @Column(name = "GL_ACCOUNT_DR_NAME")
    private String glAccountDrName;

    @Column(name = "ASSIGNMENT")
    private String assignment;

    @Column(name = "BG_CODE")
    private String bgCode;


    @Column(name = "COST_CENTER")
    private String costCenter;


    @Column(name = "FI_AREA")
    private String fiArea;

    @Column(name = "POSTING_KEY")
    private String postingKey;


    @Column(name = "FUND_SOURCE")
    private String fundSource;


    @Column(name = "BR_DOC_NO")
    private String brDocNo;

    @Column(name = "WTX_AMOUNT")
    private BigDecimal WtxAmount;
    @Column(name = "WTX_BASE")
    private BigDecimal WtxBase;

    @Column(name = "WTX_AMOUNT_P")
    private BigDecimal WtxAmountP;
    @Column(name = "WTX_BASE_P")
    private BigDecimal WtxBaseP;

    @Column(name = "MAIN_ACTIVITY")
    private String mainActivity;

    @Column(name = "COST_ACTIVITY")
    private String costActivity;


    @Column(name = "SUB_ACCOUNT")
    private String subAccount;
    @Column(name = "SUB_ACCOUNT_OWNER")
    private String subAccountOwner;
    @Column(name = "DEPOSIT_ACCOUNT")
    private String depositAccount;
    @Column(name = "DEPOSIT_ACCOUNT_OWNER")
    private String depositAccountOwner;


    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "FUND_SOURCE_NAME")
    private String fundSourceName;
    @Column(name = "MAIN_ACTIVITY_NAME")
    private String mainActivityName;
    @Column(name = "BG_CODE_NAME")
    private String bgCodeName;
    @Column(name = "COST_CENTER_NAME")
    private String costCenterName;
    @Column(name = "PAYMENT_CENTER_NAME")
    private String paymentCenterName;

    @Column(name = "JU_HEAD_ID")
    private Long juHeadId;

    @Override
    public String toString() {
        return "JUDocument{" +
                "id=" + id +
                ", paymentDate=" + paymentDate +
                ", paymentName='" + paymentName + '\'' +
                ", docType='" + docType + '\'' +
                ", documentNo='" + documentNo + '\'' +
                ", reference='" + reference + '\'' +
                ", companyCode='" + companyCode + '\'' +
                ", paymentCenter='" + paymentCenter + '\'' +
                ", username='" + username + '\'' +
                ", documentStatus='" + documentStatus + '\'' +
                ", documentStatusName='" + documentStatusName + '\'' +
                ", dateDoc=" + dateDoc +
                ", dateAcct=" + dateAcct +
                ", transferDate=" + transferDate +
                ", amountCr=" + amountCr +
                ", glAccountCr='" + glAccountCr + '\'' +
                ", testRun=" + testRun +
                ", refRunning=" + refRunning +
                ", payAccount='" + payAccount + '\'' +
                ", refLine=" + refLine +
                ", accountNoFrom='" + accountNoFrom + '\'' +
                ", accountNoTo='" + accountNoTo + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileName='" + fileName + '\'' +
                ", amountDr=" + amountDr +
                ", glAccountDr='" + glAccountDr + '\'' +
                ", assignment='" + assignment + '\'' +
                ", bgCode='" + bgCode + '\'' +
                ", costCenter='" + costCenter + '\'' +
                ", fiArea='" + fiArea + '\'' +
                ", postingKey='" + postingKey + '\'' +
                ", fundSource='" + fundSource + '\'' +
                ", brDocNo='" + brDocNo + '\'' +
                ", WtxAmount=" + WtxAmount +
                ", WtxBase=" + WtxBase +
                ", WtxAmountP=" + WtxAmountP +
                ", WtxBaseP=" + WtxBaseP +
                ", mainActivity='" + mainActivity + '\'' +
                ", costActivity='" + costActivity + '\'' +
                ", subAccount='" + subAccount + '\'' +
                ", subAccountOwner='" + subAccountOwner + '\'' +
                ", depositAccount='" + depositAccount + '\'' +
                ", depositAccountOwner='" + depositAccountOwner + '\'' +
                ", companyName='" + companyName + '\'' +
                ", fundSourceName='" + fundSourceName + '\'' +
                ", mainActivityName='" + mainActivityName + '\'' +
                ", bgCodeName='" + bgCodeName + '\'' +
                ", costCenterName='" + costCenterName + '\'' +
                ", paymentCenterName='" + paymentCenterName + '\'' +
                ", juHeadId=" + juHeadId +
                '}';
    }
}
