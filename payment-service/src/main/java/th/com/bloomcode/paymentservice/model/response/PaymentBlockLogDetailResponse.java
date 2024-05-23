package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.payment.GLHead;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class PaymentBlockLogDetailResponse {

    @Id
    private Long id;

    @Column(name = "DOC_TYPE")
    private String docType;

    @Column(name = "COMP_CODE")
    private String compCode;

    @Column(name = "FISCAL_YEAR")
    private String fiscalYear;

    @Column(name = "ACC_DOC_NO")
    private String accDocNo;

    @Column(name = "REV_ACC_DOC_NO")
    private String revAccDocNo;

    @Column(name = "REV_FISCAL_YEAR")
    private String revFiscalYear;


//    @Column(name = "VENDOR")
//    private String vendor;

    public PaymentBlockLogDetailResponse() {

    }

    public PaymentBlockLogDetailResponse(GLHead glHead) {
        this.id = glHead.getId();
        this.docType = glHead.getDocumentType();
        this.fiscalYear = glHead.getOriginalFiscalYear();
        this.accDocNo = glHead.getOriginalDocumentNo();
        this.compCode = glHead.getCompanyCode();

        this.revAccDocNo = glHead.getReverseOriginalDocumentNo();
        this.revFiscalYear = glHead.getReverseOriginalFiscalYear();
    }

}
