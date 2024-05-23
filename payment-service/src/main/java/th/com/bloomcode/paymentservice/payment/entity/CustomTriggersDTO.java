package th.com.bloomcode.paymentservice.payment.entity;

import java.sql.Timestamp;

public interface CustomTriggersDTO {
    Long getId();

    String getState();

    String getStatus();

    Long getTriggerAt();

    Long getPaymentAliasId();

    Long getPaymentType();

    Timestamp getPaymentDate();

    String getPaymentName();

    Timestamp getCreated();

    Timestamp getUpdated();

    String getCreatedBy();

    String getUpdatedBy();
}
