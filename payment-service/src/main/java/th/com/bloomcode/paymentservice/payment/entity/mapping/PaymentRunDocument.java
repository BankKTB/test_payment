package th.com.bloomcode.paymentservice.payment.entity.mapping;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class PaymentRunDocument extends PaymentDocument {

    private Timestamp paymentClearingDate;
    private Timestamp paymentClearingEntryDate;
    private String paymentClearingDocNo;
    private String paymentClearingYear;
    private Long paymentId;
    private Date paymentDate;
    private String paymentName;
    private Timestamp paymentDateAcct;
    private String houseBank;
    private String payingCompCode;
    private String payingCompCodeName;
    private String payingBankCode;
    private String payingHouseBank;
    private String payingBankAccountNo;
    private String payingBankCountry;
    private String payingBankNo;
    private String payingGLAccount;
    private String payingBankKey;
    private String payingBankName;
    private BigDecimal amountPaid;
    private String status;
    private String errorCode;
}
