package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFile;

import java.util.List;

@Repository
public interface SwiftFileRepository extends CrudRepository<SwiftFile, Long> {

  List<SwiftFile> findByGenerateFileAliasIdAndProposal(Long generateFileAliasId, boolean isTestRun);
}
