package th.com.bloomcode.paymentservice.repository.idem;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.idem.Vendor;

import java.util.List;

public interface VendorRepository extends CrudRepository<Vendor, Long> {
    Long countAllByValueCode(String valueCode);
    List<Vendor> findAllByValueCode(String valueCode);
    Vendor findOneByValueCode(String valueCode);
    Vendor findOneByValueCodeForStatus(String valueCode);
    Vendor findOneByValueCodeAndCompCode(String valueCode, String compCode);
    Vendor findOneAlternativePayee(String companyCode, String vendorCode, String alternativePayeeCode);
    Vendor findOne(String valueCode);
}
