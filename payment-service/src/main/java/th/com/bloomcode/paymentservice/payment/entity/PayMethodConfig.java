//package th.com.bloomcode.paymentservice.payment.entity;
//
//import com.fasterxml.jackson.annotation.JsonIdentityInfo;
//import com.fasterxml.jackson.annotation.ObjectIdGenerators;
//import lombok.Data;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "PAY_METHOD_CONFIG")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
//
//@Data
//public class PayMethodConfig {
//
//    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAY_METHOD_CONFIG_SEQ")
//    @SequenceGenerator(sequenceName = "PAY_METHOD_CONFIG_SEQ", allocationSize = 1, name = "PAY_METHOD_CONFIG_SEQ")
//    private Long id;
//
//    private String country;
//    private String countryNameEn;
//    private String payMethod;
//    private String payMethodName;
//
//    private String payBy;
//
//    private boolean allowedPersonnelPayment;
//    private boolean bankDetail;
//    private String documentTypeForPayment;
//
//
////	@OneToMany(mappedBy = "paymentMethodConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
////	private List<PaymentMethodCurrencyConfig> paymentMethodCurrencyConfig;
//
//}
