//package th.com.bloomcode.paymentservice.idem.entity;
//
//import lombok.Data;
//import th.com.bloomcode.paymentservice.util.BooleanToStringConverter;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "C_BPARTNER")
//@Data
//public class Vendor {
//
//    @Id
//    @Column(name = "C_BPARTNER_ID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "VALUE")
//    private String valueCode;
//
//    @Column(name = "NAME")
//    private String name;
//
//    @Column(name = "TAXID")
//    private String taxId;
//
//    @Column(name = "VENDORGROUP")
//    private String vendorGroup;
//
//    @Column(name = "CONFIRMSTATUS")
//    private boolean confirmStatus;
//
//    @Column(name = "VENDORSTATUS")
//    private String vendorStatus;
//
//    @Column(name = "COMPCODE")
//    private String compCode;
//
//    @Column(name = "ISACTIVE")
//    @Convert(converter = BooleanToStringConverter.class)
//    private boolean active;
//}
