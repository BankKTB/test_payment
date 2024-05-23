package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;

import java.util.List;

public interface OmDisplayColumnTableRepository extends CrudRepository<OmDisplayColumnTable, Long> {
    OmDisplayColumnTable findOneByRoleAndName(String role, String name);
    List<OmDisplayColumnTable> findAllByRoleAndUserAndValue(String role, String user, String value);
}
