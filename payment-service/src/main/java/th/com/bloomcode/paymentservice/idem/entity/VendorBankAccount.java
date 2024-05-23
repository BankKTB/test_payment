package th.com.bloomcode.paymentservice.idem.entity;

import lombok.Data;
import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;

import javax.persistence.*;

@Entity
@Table(name = "C_BP_BANKACCOUNT")
@Data
public class VendorBankAccount {

    @Id
    @Column(name = "C_BP_BANKACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "VENDORNAME")
    private String vendorName;

    @Column(name = "ACCOUNTNO")
    private String bankAccountNo;

    @Column(name = "BANK_ACCOUNT_HOLDER_NAME")
    private String bankAccountHolderName;

    @Column(name = "ROUTINGNO")
    private String routingNo;

    @Column(name = "BANKKEY")
    private String bankKey;

    @Column(name = "BANKNAME")
    private String bankName;

    @Column(name = "ISACTIVE")
    @Convert(converter = BooleanToStringConverter.class)
    private boolean active;
}
