package th.com.bloomcode.paymentservice.payment.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import th.com.bloomcode.paymentservice.payment.entity.GenerateFileAlias;
import th.com.bloomcode.paymentservice.payment.entity.GenerateFileAlias;

import java.util.Date;
import java.util.List;

public interface GenerateFileAliasRepository extends CrudRepository<GenerateFileAlias, Long> {

    GenerateFileAlias findOneById(Long id);


    GenerateFileAlias findOneByGenerateFileDateAndGenerateFileName(Date generateFileDate, String generateFileName);


    @Query("SELECT g FROM GenerateFileAlias g ORDER BY g.generateFileDate DESC")
    List<GenerateFileAlias> findAllByOrderByGenerateFileDateDesc();

    @Query("SELECT COUNT(p) FROM GenerateFileAlias p WHERE  p.generateFileName LIKE :key% ")
    Long countByLikeAllStartWith(String key);

    @Query("SELECT p FROM GenerateFileAlias p WHERE  p.generateFileName LIKE :key%  ORDER BY p.generateFileDate DESC")
    List<GenerateFileAlias> findByLikeAllStartWith(String key);

    @Query("SELECT COUNT(p) FROM GenerateFileAlias p WHERE  p.generateFileName LIKE %:key ")
    Long countByLikeAllEndWith(String key);

    @Query("SELECT p FROM GenerateFileAlias p WHERE  p.generateFileName LIKE %:key  ORDER BY p.generateFileDate DESC")
    List<GenerateFileAlias> findByLikeAllEndWith(String key);

    @Query("SELECT COUNT(p) FROM GenerateFileAlias p WHERE  p.generateFileName LIKE %:key% ")
    Long countByLikeAllContaining(String key);

    @Query("SELECT p FROM GenerateFileAlias p WHERE  p.generateFileName LIKE %:key%  ORDER BY p.generateFileDate DESC")
    List<GenerateFileAlias> findByLikeAllContaining(String key);

    @Query("SELECT COUNT(p) FROM GenerateFileAlias p WHERE  p.generateFileName LIKE :startKey% AND p.generateFileName LIKE %:endKey")
    Long countByLikeAll(String startKey, String endKey);

    @Query("SELECT p FROM GenerateFileAlias p WHERE  p.generateFileName LIKE :startKey% AND p.generateFileName LIKE %:endKey ORDER BY p.generateFileDate DESC")
    List<GenerateFileAlias> findByLikeAll(String startKey, String endKey);

}
