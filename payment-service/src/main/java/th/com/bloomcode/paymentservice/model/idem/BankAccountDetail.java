package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class BankAccountDetail extends BaseModel implements Serializable {

  public static final String TABLE_NAME = "C_BP_BANKACCOUNT";

  public static final String COLUMN_NAME_BANK_KEY = "BANKKEY";
  public static final String COLUMN_NAME_BANK_NAME = "BANKNAME";
  public static final String COLUMN_NAME_ROUTING_NO = "ROUTINGNO";
  public static final String COLUMN_NAME_BANK_BRANCH_NAME = "BANK_BRANCH_NAME";
  public static final String COLUMN_NAME_ACCOUNT_NO = "ACCOUNTNO";
  public static final String COLUMN_NAME_ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
  public static final String COLUMN_NAME_IS_ACTIVE = "ISACTIVE";
  public static final String COLUMN_NAME_REFERENCE_DETAIL = "REFERENCE_DETAIL";

  private String bankKey;
  private String bankName;
  private String routingNo;
  private String bankBranchName;
  private String accountNo;
  private String accountHolderName;
  private boolean active;
  private String referenceDetail;

  public BankAccountDetail(
      String bankKey,
      String bankName,
      String routingNo,
      String bankBranchName,
      String accountNo,
      String accountHolderName,
      String active,
      String referenceDetail) {
    this.bankKey = bankKey;
    this.bankName = bankName;
    this.routingNo = routingNo;
    this.bankBranchName = bankBranchName;
    this.accountNo = accountNo;
    this.accountHolderName = accountHolderName;
    this.active = "Y".equalsIgnoreCase(active);
    this.referenceDetail = referenceDetail;
  }
}
