package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.PaymentAlias;

import java.util.Date;
import java.util.List;

public interface PaymentAliasRepository extends CrudRepository<PaymentAlias, Long> {

    @Query("SELECT p FROM PaymentAlias p ORDER BY p.paymentDate DESC")
    List<PaymentAlias> findAllByOrderByPaymentDateDesc();

    PaymentAlias findOneByPaymentDateAndPaymentName(Date paymentDate, String paymentName);

    PaymentAlias findOneById(Long id);

    @Query("SELECT COUNT(p) FROM PaymentAlias p WHERE  p.paymentName LIKE :key% ")
    Long countByLikeAllStartWith(String key);

    @Query("SELECT p FROM PaymentAlias p WHERE  p.paymentName LIKE :key%  ORDER BY p.paymentDate DESC")
    List<PaymentAlias> findByLikeAllStartWith(String key);

    @Query("SELECT COUNT(p) FROM PaymentAlias p WHERE  p.paymentName LIKE %:key ")
    Long countByLikeAllEndWith(String key);

    @Query("SELECT p FROM PaymentAlias p WHERE  p.paymentName LIKE %:key  ORDER BY p.paymentDate DESC")
    List<PaymentAlias> findByLikeAllEndWith(String key);

    @Query("SELECT COUNT(p) FROM PaymentAlias p WHERE  p.paymentName LIKE %:key% ")
    Long countByLikeAllContaining(String key);

    @Query("SELECT p FROM PaymentAlias p WHERE  p.paymentName LIKE %:key%  ORDER BY p.paymentDate DESC")
    List<PaymentAlias> findByLikeAllContaining(String key);

    @Query("SELECT COUNT(p) FROM PaymentAlias p WHERE  p.paymentName LIKE :startKey% AND p.paymentName LIKE %:endKey")
    Long countByLikeAll(String startKey, String endKey);

    @Query("SELECT p FROM PaymentAlias p WHERE  p.paymentName LIKE :startKey% AND p.paymentName LIKE %:endKey ORDER BY p.paymentDate DESC")
    List<PaymentAlias> findByLikeAll(String startKey, String endKey);

}
