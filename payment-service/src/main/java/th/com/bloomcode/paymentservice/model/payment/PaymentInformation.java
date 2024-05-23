package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareProposalDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRealRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareRunDocument;
import th.com.bloomcode.paymentservice.model.payment.dto.PrepareSelectProposalDocument;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class PaymentInformation extends BaseModel implements Serializable {

  public static final String TABLE_NAME = "PAYMENT_INFORMATION";

  public static final String COLUMN_NAME_PAYMENT_INFORMATION_ID = "ID";
  public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
  public static final String COLUMN_NAME_ADDRESS = "ADDRESS";
  public static final String COLUMN_NAME_CITY = "CITY";
  public static final String COLUMN_NAME_COUNTRY = "COUNTRY";
  public static final String COLUMN_NAME_COUNTRY_NAME = "COUNTRY_NAME";
  public static final String COLUMN_NAME_DATE_DUE = "DATE_DUE";
  public static final String COLUMN_NAME_DATE_VALUE = "DATE_VALUE";
  public static final String COLUMN_NAME_NAME1 = "NAME1";
  public static final String COLUMN_NAME_NAME2 = "NAME2";
  public static final String COLUMN_NAME_NAME3 = "NAME3";
  public static final String COLUMN_NAME_NAME4 = "NAME4";
  public static final String COLUMN_NAME_PAYEE_ADDRESS = "PAYEE_ADDRESS";
  public static final String COLUMN_NAME_PAYEE_BANK = "PAYEE_BANK";
  public static final String COLUMN_NAME_PAYEE_BANK_NAME = "PAYEE_BANK_NAME";
  public static final String COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO = "PAYEE_BANK_ACCOUNT_NO";
  public static final String COLUMN_NAME_PAYEE_BANK_KEY = "PAYEE_BANK_KEY";
  public static final String COLUMN_NAME_PAYEE_BANK_NO = "PAYEE_BANK_NO";
  public static final String COLUMN_NAME_PAYEE_BANK_REFERENCE = "PAYEE_BANK_REFERENCE";
  public static final String COLUMN_NAME_PAYEE_CITY = "PAYEE_CITY";
  public static final String COLUMN_NAME_PAYEE_CODE = "PAYEE_CODE";
  public static final String COLUMN_NAME_PAYEE_COUNTRY = "PAYEE_COUNTRY";
  public static final String COLUMN_NAME_PAYEE_NAME1 = "PAYEE_NAME1";
  public static final String COLUMN_NAME_PAYEE_NAME2 = "PAYEE_NAME2";
  public static final String COLUMN_NAME_PAYEE_NAME3 = "PAYEE_NAME3";
  public static final String COLUMN_NAME_PAYEE_NAME4 = "PAYEE_NAME4";
  public static final String COLUMN_NAME_PAYEE_POSTAL_CODE = "PAYEE_POSTAL_CODE";
  public static final String COLUMN_NAME_PAYEE_TAX_ID = "PAYEE_TAX_ID";
  public static final String COLUMN_NAME_PAYEE_TITLE = "PAYEE_TITLE";
  public static final String COLUMN_NAME_PAYING_BANK_ACCOUNT_NO = "PAYING_BANK_ACCOUNT_NO";
  public static final String COLUMN_NAME_PAYING_BANK_CODE = "PAYING_BANK_CODE";
  public static final String COLUMN_NAME_PAYING_BANK_COUNTRY = "PAYING_BANK_COUNTRY";
  public static final String COLUMN_NAME_PAYING_BANK_KEY = "PAYING_BANK_KEY";
  public static final String COLUMN_NAME_PAYING_BANK_NAME = "PAYING_BANK_NAME";
  public static final String COLUMN_NAME_PAYING_BANK_NO = "PAYING_BANK_NO";
  public static final String COLUMN_NAME_PAYING_COMPANY_CODE = "PAYING_COMPANY_CODE";
  public static final String COLUMN_NAME_PAYING_GL_ACCOUNT = "PAYING_GL_ACCOUNT";
  public static final String COLUMN_NAME_PAYING_HOUSE_BANK = "PAYING_HOUSE_BANK";
  public static final String COLUMN_NAME_PAYMENT_SPECIAL_GL = "PAYMENT_SPECIAL_GL";
  public static final String COLUMN_NAME_POSTAL_CODE = "POSTAL_CODE";
  public static final String COLUMN_NAME_SWIFT_CODE = "SWIFT_CODE";
  public static final String COLUMN_NAME_VENDOR_CODE = "VENDOR_CODE";
  public static final String COLUMN_NAME_VENDOR_FLAG = "VENDOR_FLAG";
  public static final String COLUMN_NAME_VENDOR_NAME = "VENDOR_NAME";
  public static final String COLUMN_NAME_VENDOR_TAX_ID = "VENDOR_TAX_ID";
  public static final String COLUMN_NAME_VENDOR_TITLE = "VENDOR_TITLE";
  public static final String COLUMN_NAME_PAYMENT_PROCESS_ID = "PAYMENT_PROCESS_ID";

  private String accountHolderName;
  private String address;
  private String city;
  private String country;
  private String countryName;
  private Timestamp dateDue;
  private Timestamp dateValue;
  private String name1;
  private String name2;
  private String name3;
  private String name4;
  private String payeeAddress;
  private String payeeBank;
  private String payeeBankName;
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
  private String paymentSpecialGL;
  private String postalCode;
  private String swiftCode;
  private String vendorCode;
  private String vendorFlag;
  private String vendorName;
  private String vendorTaxId;
  private String vendorTitle;
  private Long paymentProcessId;
  private PaymentProcess paymentProcess;

  public PaymentInformation(
      PrepareProposalDocument paymentDocument, boolean isProposal, boolean isChild) {
    this.accountHolderName = paymentDocument.getAccountHolderName();
    this.address = paymentDocument.getAddress();
    this.city = paymentDocument.getCity();
    this.country = paymentDocument.getCountryCode();
    this.countryName = paymentDocument.getCountry();
    //        this.dateDue = paymentDocument;
    this.dateValue = paymentDocument.getDateValue();
    this.name1 = paymentDocument.getName1();
    this.name2 = paymentDocument.getName2();
    //        this.name3 = paymentDocument;
    //        this.name4 = paymentDocument;
    this.payeeAddress = paymentDocument.getAddress();
    this.payeeBank = paymentDocument.getPayeeBank();
    this.payeeBankName = paymentDocument.getPayeeBankName();
    this.payeeBankAccountNo = paymentDocument.getPayeeBankAccountNo();
    this.payeeBankKey = paymentDocument.getPayeeBankKey();
    this.payeeBankNo = paymentDocument.getPayeeBankNo();
    this.payeeBankReference = paymentDocument.getPayeeBankReference();
    this.payeeCity = paymentDocument.getCity();
    this.payeeCode = paymentDocument.getPayee();
    this.payeeCountry = paymentDocument.getCountry();
    this.payeeName1 = paymentDocument.getName1();
    this.payeeName2 = paymentDocument.getName2();
    //        this.payeeName3 = paymentDocument;
    //        this.payeeName4 = paymentDocument;
    this.payeePostalCode = paymentDocument.getPostal();
    this.payeeTaxId = paymentDocument.getPayeeTaxId();
    //        this.payeeTitle = paymentDocument;
    this.payingBankAccountNo = paymentDocument.getPayingBankAccountNo();
    this.payingBankCode = paymentDocument.getPayingBankCode();
    this.payingBankCountry = paymentDocument.getPayingBankCountry();
    this.payingBankKey = paymentDocument.getPayingBankKey();
    this.payingBankName = paymentDocument.getPayingBankName();
    this.payingBankNo = paymentDocument.getPayingBankNo();
    this.payingCompanyCode = paymentDocument.getPayingCompCode();
    this.payingGLAccount = paymentDocument.getPayingGLAccount();
    this.payingHouseBank = paymentDocument.getPayingHouseBank();
    this.paymentSpecialGL = paymentDocument.getSpecialGL();
    //        this.pmGroupDoc = paymentDocument;
    //        this.pmGroupNo = paymentDocument;
    this.postalCode = paymentDocument.getPostal();
    this.swiftCode = paymentDocument.getSwiftCode();
    this.vendorCode = paymentDocument.getVendor();
    //        this.vendorFlag = paymentDocument;
    this.vendorName = paymentDocument.getName1();
    this.vendorTaxId = paymentDocument.getVendorTaxId();
    this.vendorTitle = "";
  }

  public PaymentInformation(
          PrepareSelectProposalDocument paymentDocument, boolean isProposal, boolean isChild) {
    this.accountHolderName =  paymentDocument.getPayee() != null? paymentDocument.getAlternativeAccountHolderName():paymentDocument.getMainAccountHolderName();
    this.address = paymentDocument.getMainAddress();
    this.city = paymentDocument.getMainCity();
    this.country = paymentDocument.getMainCountryCode();
    this.countryName = paymentDocument.getMainCountry();
    //        this.dateDue = paymentDocument;
    this.dateValue = paymentDocument.getDateValue();
    this.name1 = paymentDocument.getMainName1();
    this.name2 = paymentDocument.getMainName2();
    //        this.name3 = paymentDocument;
    //        this.name4 = paymentDocument;
    this.payeeAddress = paymentDocument.getAlternativeAddress();
    this.payeeBank = paymentDocument.getAlternativePayeeBank();
    this.payeeBankName = paymentDocument.getAlternativePayeeBankName();
    this.payeeBankAccountNo =paymentDocument.getPayee() != null? paymentDocument.getAlternativePayeeBankAccountNo():paymentDocument.getMainPayeeBankAccountNo();
    this.payeeBankKey = paymentDocument.getPayee() != null?paymentDocument.getAlternativePayeeBankKey():paymentDocument.getMainPayeeBankKey();
    this.payeeBankNo = paymentDocument.getAlternativePayeeBankNo();
    this.payeeBankReference = paymentDocument.getPayee() != null?paymentDocument.getAlternativePayeeBankReference():paymentDocument.getMainPayeeBankReference();
    this.payeeCity = paymentDocument.getAlternativeCity();
    this.payeeCode = paymentDocument.getPayee();
    this.payeeCountry = paymentDocument.getAlternativeCountry();
    this.payeeName1 = paymentDocument.getAlternativeName1();
    this.payeeName2 = paymentDocument.getAlternativeName2();
    //        this.payeeName3 = paymentDocument;
    //        this.payeeName4 = paymentDocument;
    this.payeePostalCode = paymentDocument.getAlternativePostal();
    this.payeeTaxId = paymentDocument.getPayeeTaxId();
    //        this.payeeTitle = paymentDocument;
    this.payingBankAccountNo = paymentDocument.getPayingBankAccountNo();
    this.payingBankCode = paymentDocument.getPayingBankCode();
    this.payingBankCountry = paymentDocument.getPayingBankCountry();
    this.payingBankKey = paymentDocument.getPayingBankKey();
    this.payingBankName = paymentDocument.getPayingBankName();
    this.payingBankNo = paymentDocument.getPayingBankNo();
    this.payingCompanyCode = paymentDocument.getPayingCompCode();
    this.payingGLAccount = paymentDocument.getPayingGLAccount();
    this.payingHouseBank = paymentDocument.getPayingHouseBank();
    this.paymentSpecialGL = paymentDocument.getSpecialGL();
    //        this.pmGroupDoc = paymentDocument;
    //        this.pmGroupNo = paymentDocument;
    this.postalCode = paymentDocument.getMainPostal();
    this.swiftCode = paymentDocument.getMainSwiftCode();
    this.vendorCode = paymentDocument.getVendor();
    //        this.vendorFlag = paymentDocument;
    this.vendorName = paymentDocument.getMainName1();
    this.vendorTaxId = paymentDocument.getVendorTaxId();
    this.vendorTitle = "";
  }




  public PaymentInformation(
          PrepareRunDocument paymentDocument) {
    this.accountHolderName = paymentDocument.getAccountHolderName();
    this.address = paymentDocument.getAddress();
    this.city = paymentDocument.getCity();
    this.country = paymentDocument.getCountry();
    this.countryName = paymentDocument.getCountry();
    //        this.dateDue = paymentDocument;
    this.dateValue = paymentDocument.getDateValue();
    this.name1 = paymentDocument.getName1();
    this.name2 = paymentDocument.getName2();
    //        this.name3 = paymentDocument;
    //        this.name4 = paymentDocument;
    this.payeeAddress = paymentDocument.getAddress();
    this.payeeBank = paymentDocument.getPayeeBank();
    this.payeeBankName = paymentDocument.getPayeeBankName();
    this.payeeBankAccountNo = paymentDocument.getPayeeBankAccountNo();
    this.payeeBankKey = paymentDocument.getPayeeBankKey();
    this.payeeBankNo = paymentDocument.getPayeeBankNo();
    this.payeeBankReference = paymentDocument.getPayeeBankReference();
    this.payeeCity = paymentDocument.getCity();
    this.payeeCode = paymentDocument.getPayeeCode();
    this.payeeCountry = paymentDocument.getCountry();
    this.payeeName1 = paymentDocument.getName1();
    this.payeeName2 = paymentDocument.getName2();
    //        this.payeeName3 = paymentDocument;
    //        this.payeeName4 = paymentDocument;
    this.payeePostalCode = paymentDocument.getPayeePostalCode();
    this.payeeTaxId = paymentDocument.getPayeeTaxId();
    //        this.payeeTitle = paymentDocument;
    this.payingBankAccountNo = paymentDocument.getPayingBankAccountNo();
    this.payingBankCode = paymentDocument.getPayingBankCode();
    this.payingBankCountry = paymentDocument.getPayingBankCountry();
    this.payingBankKey = paymentDocument.getPayingBankKey();
    this.payingBankName = paymentDocument.getPayingBankName();
    this.payingBankNo = paymentDocument.getPayingBankNo();
    this.payingCompanyCode = paymentDocument.getPayingCompanyCode();
    this.payingGLAccount = paymentDocument.getPayingGlAccount();
    this.payingHouseBank = paymentDocument.getPayingHouseBank();
    this.paymentSpecialGL = paymentDocument.getPaymentSpecialGl();
    //        this.pmGroupDoc = paymentDocument;
    //        this.pmGroupNo = paymentDocument;
    this.postalCode = paymentDocument.getPostalCode();
    this.swiftCode = paymentDocument.getSwiftCode();
    this.vendorCode = paymentDocument.getVendorCode();
    //        this.vendorFlag = paymentDocument;
    this.vendorName = paymentDocument.getName1();
    this.vendorTaxId = paymentDocument.getVendorTaxId();
    this.vendorTitle = "";
  }

  public PaymentInformation(
          PrepareRealRunDocument paymentDocument) {
    this.accountHolderName = paymentDocument.getAccountHolderName();
    this.address = paymentDocument.getAddress();
    this.city = paymentDocument.getCity();
    this.country = paymentDocument.getCountry();
    this.countryName = paymentDocument.getCountry();
    this.dateValue = paymentDocument.getDateValue();
    this.name1 = paymentDocument.getName1();
    this.name2 = paymentDocument.getName2();
    this.payeeAddress = paymentDocument.getAddress();
    this.payeeBank = paymentDocument.getPayeeBank();
    this.payeeBankName = paymentDocument.getPayeeBankName();
    this.payeeBankAccountNo = paymentDocument.getPayeeBankAccountNo();
    this.payeeBankKey = paymentDocument.getPayeeBankKey();
    this.payeeBankNo = paymentDocument.getPayeeBankNo();
    this.payeeBankReference = paymentDocument.getPayeeBankReference();
    this.payeeCity = paymentDocument.getCity();
    this.payeeCode = paymentDocument.getPayeeCode();
    this.payeeCountry = paymentDocument.getCountry();
    this.payeeName1 = paymentDocument.getName1();
    this.payeeName2 = paymentDocument.getName2();

    this.payeePostalCode = paymentDocument.getPayeePostalCode();
    this.payeeTaxId = paymentDocument.getPayeeTaxId();
    this.payingBankAccountNo = paymentDocument.getPayingBankAccountNo();
    this.payingBankCode = paymentDocument.getPayingBankCode();
    this.payingBankCountry = paymentDocument.getPayingBankCountry();
    this.payingBankKey = paymentDocument.getPayingBankKey();
    this.payingBankName = paymentDocument.getPayingBankName();
    this.payingBankNo = paymentDocument.getPayingBankNo();
    this.payingCompanyCode = paymentDocument.getPayingCompanyCode();
    this.payingGLAccount = paymentDocument.getPayingGlAccount();
    this.payingHouseBank = paymentDocument.getPayingHouseBank();
    this.paymentSpecialGL = paymentDocument.getPaymentSpecialGl();

    this.postalCode = paymentDocument.getPostalCode();
    this.swiftCode = paymentDocument.getSwiftCode();
    this.vendorCode = paymentDocument.getVendorCode();
    this.vendorName = paymentDocument.getName1();
    this.vendorTaxId = paymentDocument.getVendorTaxId();
  }

  public PaymentInformation(
      Long id,
      Timestamp created,
      String createdBy,
      Timestamp updated,
      String updatedBy,
      String accountHolderName,
      String address,
      String city,
      String country,
      String countryName,
      Timestamp dateDue,
      Timestamp dateValue,
      String name1,
      String name2,
      String name3,
      String name4,
      String payeeAddress,
      String payeeBank,
      String payeeBankName,
      String payeeBankAccountNo,
      String payeeBankKey,
      String payeeBankNo,
      String payeeBankReference,
      String payeeCity,
      String payeeCode,
      String payeeCountry,
      String payeeName1,
      String payeeName2,
      String payeeName3,
      String payeeName4,
      String payeePostalCode,
      String payeeTaxId,
      String payeeTitle,
      String payingBankAccountNo,
      String payingBankCode,
      String payingBankCountry,
      String payingBankKey,
      String payingBankName,
      String payingBankNo,
      String payingCompanyCode,
      String payingGLAccount,
      String payingHouseBank,
      String paymentSpecialGL,
      String postalCode,
      String swiftCode,
      String vendorCode,
      String vendorFlag,
      String vendorName,
      String vendorTaxId,
      String vendorTitle,
      Long paymentProcessId,
      PaymentProcess paymentProcess) {
    super(id, created, createdBy, updated, updatedBy);
    this.accountHolderName = accountHolderName;
    this.address = address;
    this.city = city;
    this.country = country;
    this.countryName = countryName;
    this.dateDue = dateDue;
    this.dateValue = dateValue;
    this.name1 = name1;
    this.name2 = name2;
    this.name3 = name3;
    this.name4 = name4;
    this.payeeAddress = payeeAddress;
    this.payeeBank = payeeBank;
    this.payeeBankName = payeeBankName;
    this.payeeBankAccountNo = payeeBankAccountNo;
    this.payeeBankKey = payeeBankKey;
    this.payeeBankNo = payeeBankNo;
    this.payeeBankReference = payeeBankReference;
    this.payeeCity = payeeCity;
    this.payeeCode = payeeCode;
    this.payeeCountry = payeeCountry;
    this.payeeName1 = payeeName1;
    this.payeeName2 = payeeName2;
    this.payeeName3 = payeeName3;
    this.payeeName4 = payeeName4;
    this.payeePostalCode = payeePostalCode;
    this.payeeTaxId = payeeTaxId;
    this.payeeTitle = payeeTitle;
    this.payingBankAccountNo = payingBankAccountNo;
    this.payingBankCode = payingBankCode;
    this.payingBankCountry = payingBankCountry;
    this.payingBankKey = payingBankKey;
    this.payingBankName = payingBankName;
    this.payingBankNo = payingBankNo;
    this.payingCompanyCode = payingCompanyCode;
    this.payingGLAccount = payingGLAccount;
    this.payingHouseBank = payingHouseBank;
    this.paymentSpecialGL = paymentSpecialGL;
    this.postalCode = postalCode;
    this.swiftCode = swiftCode;
    this.vendorCode = vendorCode;
    this.vendorFlag = vendorFlag;
    this.vendorName = vendorName;
    this.vendorTaxId = vendorTaxId;
    this.vendorTitle = vendorTitle;
    this.paymentProcessId = paymentProcessId;
    this.paymentProcess = paymentProcess;
  }

  @Override
  public String toString() {
    return "PaymentInformation{" +
            "accountHolderName='" + accountHolderName + '\'' +
            ", address='" + address + '\'' +
            ", city='" + city + '\'' +
            ", country='" + country + '\'' +
            ", countryName='" + countryName + '\'' +
            ", dateDue=" + dateDue +
            ", dateValue=" + dateValue +
            ", name1='" + name1 + '\'' +
            ", name2='" + name2 + '\'' +
            ", name3='" + name3 + '\'' +
            ", name4='" + name4 + '\'' +
            ", payeeAddress='" + payeeAddress + '\'' +
            ", payeeBankAccountNo='" + payeeBankAccountNo + '\'' +
            ", payeeBankKey='" + payeeBankKey + '\'' +
            ", payeeBankNo='" + payeeBankNo + '\'' +
            ", payeeBankReference='" + payeeBankReference + '\'' +
            ", payeeCity='" + payeeCity + '\'' +
            ", payeeCode='" + payeeCode + '\'' +
            ", payeeCountry='" + payeeCountry + '\'' +
            ", payeeName1='" + payeeName1 + '\'' +
            ", payeeName2='" + payeeName2 + '\'' +
            ", payeeName3='" + payeeName3 + '\'' +
            ", payeeName4='" + payeeName4 + '\'' +
            ", payeePostalCode='" + payeePostalCode + '\'' +
            ", payeeTaxId='" + payeeTaxId + '\'' +
            ", payeeTitle='" + payeeTitle + '\'' +
            ", payingBankAccountNo='" + payingBankAccountNo + '\'' +
            ", payingBankCode='" + payingBankCode + '\'' +
            ", payingBankCountry='" + payingBankCountry + '\'' +
            ", payingBankKey='" + payingBankKey + '\'' +
            ", payingBankName='" + payingBankName + '\'' +
            ", payingBankNo='" + payingBankNo + '\'' +
            ", payingCompanyCode='" + payingCompanyCode + '\'' +
            ", payingGLAccount='" + payingGLAccount + '\'' +
            ", payingHouseBank='" + payingHouseBank + '\'' +
            ", paymentSpecialGL='" + paymentSpecialGL + '\'' +
            ", postalCode='" + postalCode + '\'' +
            ", swiftCode='" + swiftCode + '\'' +
            ", vendorCode='" + vendorCode + '\'' +
            ", vendorFlag='" + vendorFlag + '\'' +
            ", vendorName='" + vendorName + '\'' +
            ", vendorTaxId='" + vendorTaxId + '\'' +
            ", vendorTitle='" + vendorTitle + '\'' +
            ", paymentProcessId=" + paymentProcessId +
            ", paymentProcess=" + paymentProcess +
            '}';
  }
}
