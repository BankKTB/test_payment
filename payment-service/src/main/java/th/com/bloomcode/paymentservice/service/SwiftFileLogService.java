package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.SwiftFileLogRepository;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFile;
import th.com.bloomcode.paymentservice.payment.entity.SwiftFileLog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
public class SwiftFileLogService {

    private final SwiftFileLogRepository swiftFileLogRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public SwiftFileLogService(SwiftFileLogRepository swiftFileLogRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager,
                               @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.swiftFileLogRepository = swiftFileLogRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    public SwiftFileLog save(SwiftFileLog swiftFileLog) {
        return swiftFileLogRepository.save(swiftFileLog);
    }

    public void saveAll(List<SwiftFileLog> swiftFileLogs, String sendRef) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int cont = 0;
//        Long id = getNextSeries();
        for (SwiftFileLog swiftFileLog : swiftFileLogs) {
//            inhouseFileDetail.setId(id++);
            entityManager.persist(swiftFileLog);
            SwiftFile swiftFile = swiftFileLog.getSwiftFile();
            swiftFile.setSendRef(sendRef);
//            smartFileDetail.setId(detailId++);
            swiftFile.setSwiftFileLog(swiftFileLog);
            entityManager.persist(swiftFile);
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
