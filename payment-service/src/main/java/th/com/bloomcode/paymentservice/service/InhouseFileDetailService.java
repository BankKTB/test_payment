package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.payment.dao.InhouseFileDetailRepository;
import th.com.bloomcode.paymentservice.payment.entity.InhouseFileDetail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class InhouseFileDetailService {

    private final InhouseFileDetailRepository inhouseFileDetailRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public InhouseFileDetailService(InhouseFileDetailRepository inhouseFileDetailRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager,
                                    @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.inhouseFileDetailRepository = inhouseFileDetailRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void saveAll(List<InhouseFileDetail> inhouseFileDetails) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
        int cont = 0;
//        Long id = getNextSeries();
        for (InhouseFileDetail inhouseFileDetail : inhouseFileDetails) {
//            inhouseFileDetail.setId(id++);
            entityManager.persist(inhouseFileDetail);
            cont++;
            if (cont % 1000 == 0) {
                entityManager.flush();
                entityManager.clear();
//                entityManager.getTransaction().commit();
//                entityManager.getTransaction().begin();
            }
        }
//        updateNextSeries(--id);
//        entityManager.getTransaction().commit();
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
