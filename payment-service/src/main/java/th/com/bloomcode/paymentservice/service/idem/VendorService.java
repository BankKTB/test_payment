package th.com.bloomcode.paymentservice.service.idem;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.Vendor;

import java.util.List;

public interface VendorService {
    ResponseEntity<Result<List<Vendor>>> findAllByValue(String valueCode);

    ResponseEntity<Result<Vendor>> findOneByValueCode2(String valueCode);

    Vendor findOneByValueCode(String valueCode);
    Vendor findOneByValueCodeAndCompCode(String valueCode, String compCode);

//    Vendor findOneByVendorCode(String vendorCode);

    Vendor findOneByVendorCodeForStatus(String vendorCode);

    Vendor findOneAlternativePayee(String companyCode, String vendorCode, String alternativePayeeCode);
    Vendor findOne(String valueCode);
}
