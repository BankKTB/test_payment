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
@Table(name = "PAYMENT_INFORMATION")
public class TestCreateDataBasePaymentInformation {

    @Id
    private Long id;
    private String accountHolderName;
    private String address;
    private String city;
    private String country;
    private String countryName;
    private String currency;
    private Timestamp dateDue;
    private Timestamp dateValue;
    private String fiArea;
    private String fiAreaName;
    private String idemStatus;
    private BigDecimal invoiceAmount;
    private BigDecimal invoiceAmountPaid;
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
    private String name1;
    private String name2;
    private String name3;
    private String name4;
    private String payeeAddress;
    private String payeeBankAccountNo;
    private String payeeBankKey;
    private String payeeBankNo;
    private String payeeBankReference;
    private String payeeCity;
    private String payeeCode;
    private String payeeCountry;
    private String payeeName1;
    private String payeeName2;
    private String payeeName3;
    private String payeeName4;
    private String payeePostalCode;
    private String payeeTaxId;
    private String payeeTitle;
    private String payingBankAccountNo;
    private String payingBankCode;
    private String payingBankCountry;
    private String payingBankKey;
    private String payingBankName;
    private String payingBankNo;
    private String payingCompanyCode;
    private String payingGLAccount;
    private String payingHouseBank;
    private String paymentBlock;
    private String paymentSpecialGL;
    private String pmGroupDoc;
    private String pmGroupNo;
    private String postalCode;
    private boolean proposal;
    private String swiftCode;
    private String vendorCode;
    private String vendorFlag;
    private String vendorName;
    private String vendorTaxId;
    private String vendorTitle;
    private Long paymentAliasId;
    private Long paymentProcessId;

}
