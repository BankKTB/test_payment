package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.GIROFileTrailer;

@Repository
public interface GIROFileTrailerRepository extends CrudRepository<GIROFileTrailer, Long> {
}
