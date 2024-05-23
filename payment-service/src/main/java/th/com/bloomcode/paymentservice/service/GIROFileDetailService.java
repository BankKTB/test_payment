package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import th.com.bloomcode.paymentservice.payment.dao.GIROFileDetailRepository;
import th.com.bloomcode.paymentservice.payment.entity.GIROFileDetail;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class GIROFileDetailService {

    private final GIROFileDetailRepository giroFileDetailRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public GIROFileDetailService(GIROFileDetailRepository giroFileDetailRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager, @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.giroFileDetailRepository = giroFileDetailRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    public void saveAll(List<GIROFileDetail> giroFileDetails) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
        int cont = 0;
//        Long id = getNextSeries();
        for (GIROFileDetail giroFileDetail : giroFileDetails) {
//            giroFileDetail.setId(id++);
            entityManager.persist(giroFileDetail);
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
        String sql = "SELECT GIRO_FILE_DETAIL_SEQ.NEXTVAL FROM DUAL";
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    private void updateNextSeries(Long lastNumber) {
        String sql = "ALTER SEQUENCE GIRO_FILE_DETAIL_SEQ START WITH " + lastNumber;
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
