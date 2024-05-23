package th.com.bloomcode.paymentservice.payment.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAYMENT_CHANGE_BLOCK_LOG")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class PaymentChangeBlockLog {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PAYMENT_CHANGE_BLOCK_LOG_SEQ")
    @SequenceGenerator(sequenceName = "PAYMENT_CHANGE_BLOCK_LOG_SEQ", allocationSize = 1, name = "PAYMENT_CHANGE_BLOCK_LOG_SEQ")
    private Long id;

    private String docType;
    private String compCode;
    private String accDocNo;
    private String fiscalYear;
    private boolean approve;
    private String reason;
    private String userWeb;


    private Date postDate;
    private Date approveDate;
    private String valueOld;
    private String valueNew;
    private String userPost;
    private String username;

    private String vendor;


}
