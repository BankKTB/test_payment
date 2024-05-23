package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.InhouseFileTrailer;

@Repository
public interface InhouseFileTrailerRepository extends CrudRepository<InhouseFileTrailer, Long> {
}
