package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.InhouseFileHeader;

import java.util.List;

@Repository
public interface InhouseFileHeaderRepository extends CrudRepository<InhouseFileHeader, Long> {

    List<InhouseFileHeader> findByGenerateFileAliasId(Long generateFileAliasId);
}
