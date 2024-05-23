package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.CustomTriggers;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomTriggersRepository extends CrudRepository<CustomTriggers, Long>, JpaSpecificationExecutor<CustomTriggers> {
//  @Query(nativeQuery=true, value="SELECT TQCT.TH_QUARTZ_CUSTOM_TRIGGERS_ID AS ID, TQCT.CREATED AS CREATED, TQCT.UPDATED AS UPDATED, TQCT.CREATED_BY AS CREATEDBY, TQCT.UPDATED_BY AS UPDATEDBY, TQCT.STATE AS STATE, TQCT.STATUS AS STATUS, TQCT.PAYMENT_ALIAS_ID AS PAYMENTALIASID, TQCT.PAYMENT_TYPE AS PAYMENTTYPE, TQCT.TRIGGER_AT_IN_MILLIS AS TRIGGERAT, PA.PAYMENT_DATE AS PAYMENTDATE, PA.PAYMENT_NAME AS PAYMENTNAME FROM TH_QUARTZ_CUSTOM_TRIGGERS TQCT LEFT JOIN PAYMENT_ALIAS PA ON TQCT.PAYMENT_ALIAS_ID = PA.ID")
  List<CustomTriggers> findAll();
  CustomTriggers findOneByParentIdAndState(Long id, String state);
  List<CustomTriggers> findByStateAndPaymentType(String state, Long paymentType);
  Optional<CustomTriggers> findOneByPaymentAliasIdAndPaymentType(Long paymentAliasId, Long paymentType);
  Optional<CustomTriggers> findOneByJobTypeAndState(String jobType, String state);
//  List<CustomTriggersDTO> findAllWhatever();
}
