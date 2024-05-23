package th.com.bloomcode.paymentservice.repository.payment;

import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.GenerateFileAliasResponse;

import java.sql.Timestamp;
import java.util.List;

public interface GenerateFileAliasRepository extends CrudRepository<GenerateFileAlias, Long> {


    List<GenerateFileAliasResponse> findByCondition(String value);
    List<GenerateFileAliasResponse> findByReturn(String value);
    GenerateFileAliasResponse findOneByGenerateFileDateAndGenerateFileName(Timestamp generateFileDate, String generateFileName);
    GenerateFileAlias findOneById(Long id);
    boolean existByGenerateFileDateAndGenerateFileName(Timestamp generateFileDate, String generateFileName);
    GenerateFileAliasResponse findOneByGenerateFileDateNotLessThanAndGenerateFileName(Timestamp generateFileDate, String generateFileName);
}
