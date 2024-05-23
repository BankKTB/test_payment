package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.OmSearchCriteria;

import java.util.List;

public interface OmSearchCriteriaRepository extends CrudRepository<OmSearchCriteria, Long> {
    OmSearchCriteria findOneByNameAndRole(String name, String role);
    List<OmSearchCriteria> findAllByRoleAndUserAndValue(String role, String user, String value);
}
