package th.com.bloomcode.paymentservice.model.idem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class VendorBankAccount extends BaseModel implements Serializable {

    public static final String TABLE_NAME = "C_BP_BANKACCOUNT";

    public static final String COLUMN_NAME_VENDOR = "VENDOR";
    public static final String COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME = "BANK_ACCOUNT_HOLDER_NAME";
    public static final String COLUMN_NAME_VENDOR_NAME = "VENDORNAME";
    public static final String COLUMN_NAME_ACCOUNT_NO = "ACCOUNTNO";
    public static final String COLUMN_NAME_ROUTING_NO = "ROUTINGNO";
    public static final String COLUMN_NAME_BANK_KEY = "BANKKEY";
    public static final String COLUMN_NAME_IS_ACTIVE = "ISACTIVE";

    private String vendor;
    private String bankAccountHolderName;
    private String vendorName;
    private String bankAccountNo;
    private String routingNo;
    private String bank;
    private boolean active;

    public VendorBankAccount(String vendor, String vendorName, String bankAccountNo, String routingNo, String bank, String active) {
        this.vendor = vendor;
        this.vendorName = vendorName;
        this.bankAccountNo = bankAccountNo;
        this.routingNo = routingNo;
        this.bank = bank;
        this.active = "Y".equalsIgnoreCase(active);
    }

    public VendorBankAccount(String vendor, String bankAccountHolderName, String vendorName, String bankAccountNo, String routingNo, String bank, String active) {
        this.vendor = vendor;
        this.bankAccountHolderName = bankAccountHolderName;
        this.vendorName = vendorName;
        this.bankAccountNo = bankAccountNo;
        this.routingNo = routingNo;
        this.bank = bank;
        this.active = "Y".equalsIgnoreCase(active);
    }
}
