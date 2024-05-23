package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.GIROFileTrailerRepository;
import th.com.bloomcode.paymentservice.payment.entity.GIROFileTrailer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
public class GIROFileTrailerService {

    private final GIROFileTrailerRepository giroFileTrailerRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public GIROFileTrailerService(GIROFileTrailerRepository giroFileTrailerRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager, @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.giroFileTrailerRepository = giroFileTrailerRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    public GIROFileTrailer save(GIROFileTrailer giroFileTrailer) {
        Long id = getNextSeries();
        giroFileTrailer.setId(id);
        return giroFileTrailerRepository.save(giroFileTrailer);
    }

    public void saveAll(List<GIROFileTrailer> giroFileTrailers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int cont = 0;
        Long id = getNextSeries();
        for (GIROFileTrailer giroFileTrailer : giroFileTrailers) {
            giroFileTrailer.setId(id++);
            entityManager.persist(giroFileTrailer);
            cont++;
            if (cont % 1000 == 0) {
                entityManager.flush();
                entityManager.clear();
                entityManager.getTransaction().commit();
                entityManager.getTransaction().begin();
            }
        }
        updateNextSeries(--id);
        entityManager.getTransaction().commit();
    }

    private Long getNextSeries() {
        String sql = "SELECT GIRO_FILE_TRAILER_SEQ.NEXTVAL FROM DUAL";
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    private void updateNextSeries(Long lastNumber) {
        String sql = "ALTER SEQUENCE GIRO_FILE_TRAILER_SEQ START WITH " + lastNumber;
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
