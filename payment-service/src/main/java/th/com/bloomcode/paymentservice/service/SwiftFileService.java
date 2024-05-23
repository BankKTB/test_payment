package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.SwiftFileRepository;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFile;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SwiftFileService {

    private final SwiftFileRepository swiftFileRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public SwiftFileService(SwiftFileRepository swiftFileRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager,
                            @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.swiftFileRepository = swiftFileRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    public SwiftFile save(SwiftFile swiftFile) {
        return swiftFileRepository.save(swiftFile);
    }

    public void saveAll(List<SwiftFile> swiftFiles) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int cont = 0;
//        Long id = getNextSeries();
        for (SwiftFile swiftFile : swiftFiles) {
//            inhouseFileDetail.setId(id++);
            entityManager.persist(swiftFiles);
            cont++;
            if (cont % 1000 == 0) {
                entityManager.flush();
                entityManager.clear();
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
        }
//        updateNextSeries(--id);
        entityManager.getTransaction().commit();
    }

    public List<SwiftFile> findByGenerateFileAliasId(Long generateFileAliasId, boolean isTestRun) {
        return swiftFileRepository.findByGenerateFileAliasIdAndProposal(generateFileAliasId, isTestRun);
    }

    private Long getNextSeries() {
        String sql = "SELECT INHOUSE_FILE_DETAIL_SEQ.NEXTVAL FROM DUAL";
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    private void updateNextSeries(Long lastNumber) {
        String sql = "ALTER SEQUENCE INHOUSE_FILE_DETAIL_SEQ START WITH " + lastNumber;
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
