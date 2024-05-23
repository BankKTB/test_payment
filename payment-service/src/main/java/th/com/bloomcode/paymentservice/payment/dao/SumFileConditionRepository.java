package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.SumFileCondition;

import java.util.List;

public interface SumFileConditionRepository extends CrudRepository<SumFileCondition, Long> {

    @Query(value = "SELECT * FROM SUM_FILE_CONDITION WHERE VALID_FROM <= SYSDATE AND SYSDATE <= VALID_TO AND ACTIVE = 1", nativeQuery = true)
    List<SumFileCondition> findAll();

    SumFileCondition findOneById(Long id);


    List<SumFileCondition> findAllByOrderByVendorFromAscValidFromAsc();

    SumFileCondition findOneByPaymentMethodAndVendorFrom(String paymentMethod, String vendorFrom);


    @Query("SELECT s FROM SumFileCondition s WHERE s.active = true and s.paymentMethod =:paymentMethod and (" + "  s.vendorFrom <= :vendorFrom or s.vendorTo <= :vendorTo) and (" + "  s.vendorFrom >= :vendorFrom or s.vendorTo >= :vendorTo) or   s.vendorFrom = :vendorTo or s.vendorTo = :vendorFrom  ")
    List<SumFileCondition> checkVendorCreate(String paymentMethod, String vendorFrom, String vendorTo);

    @Query("SELECT s FROM SumFileCondition s WHERE s.paymentMethod =:paymentMethod and s.id <>:id and ((" + "  s.vendorFrom <= :vendorTo AND s.vendorFrom >= :vendorFrom) or (" + "  s.vendorTo <= :vendorTo and s.vendorTo >= :vendorFrom) or   s.vendorFrom = :vendorTo or s.vendorTo = :vendorFrom   )")
    List<SumFileCondition> checkVendorEdit(Long id, String paymentMethod, String vendorFrom, String vendorTo);

//    @Query("SELECT s FROM SumFileCondition s WHERE s.paymentMethod =:paymentMethod and  s.vendorTo <= :vendor")
//    SumFileCondition checkVendorTo(String paymentMethod, String vendor);


}
