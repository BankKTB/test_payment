package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.payment.entity.GIROFileHeader;

import java.util.List;

@Repository
public interface GIROFileHeaderRepository extends CrudRepository<GIROFileHeader, Long> {

    List<GIROFileHeader> findByGenerateFileAliasIdAndProposal(Long generateFileAliasId, boolean isTestRun);
}