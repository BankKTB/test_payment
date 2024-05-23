package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.GenerateFileAliasResponse;
import th.com.bloomcode.paymentservice.model.request.GenerateFileAliasRequest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface GenerateFileAliasService {
    ResponseEntity<Result<GenerateFileAlias>> save(GenerateFileAliasRequest request);
    GenerateFileAlias save(GenerateFileAlias generateFileAlias);
    ResponseEntity<Result<List<GenerateFileAliasResponse>>> findByCondition(String value);
    ResponseEntity<Result<List<GenerateFileAliasResponse>>> findByReturn(String value);
    GenerateFileAlias findOneById(Long id);
    ResponseEntity<Result<GenerateFileAliasResponse>> searchByGenerateFileDateAndGenerateFileName(Date generateFileDate, String generateFileName);
    boolean existByGenerateFileDateAndGenerateFileName(Timestamp generateFileDate, String generateFileName);
}
