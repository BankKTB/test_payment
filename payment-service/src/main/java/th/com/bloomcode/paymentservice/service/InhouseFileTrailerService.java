package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.InhouseFileTrailerRepository;
import th.com.bloomcode.paymentservice.payment.entity.InhouseFileTrailer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.List;

@Service
public class InhouseFileTrailerService {

    private final InhouseFileTrailerRepository inhouseFileTrailerRepository;
    private final EntityManager entityManager;
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public InhouseFileTrailerService(InhouseFileTrailerRepository inhouseFileTrailerRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager,
                                     @Qualifier("paymentEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.inhouseFileTrailerRepository = inhouseFileTrailerRepository;
        this.entityManager = entityManager;
        this.entityManagerFactory = entityManagerFactory;
    }

    public InhouseFileTrailer save(InhouseFileTrailer inhouseFileTrailer) {
        Long id = getNextSeries();
        inhouseFileTrailer.setId(id);
        return inhouseFileTrailerRepository.save(inhouseFileTrailer);
    }

    public void saveAll(List<InhouseFileTrailer> inhouseFileTrailers) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        int cont = 0;
//        Long id = getNextSeries();
        for (InhouseFileTrailer inhouseFileTrailer : inhouseFileTrailers) {
//            inhouseFileTrailer.setId(id++);
            entityManager.persist(inhouseFileTrailer);
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
        String sql = "SELECT INHOUSE_FILE_TRAILER_SEQ.NEXTVAL FROM DUAL";
        Query q = entityManager.createNativeQuery(sql);
        return ((BigDecimal) q.getSingleResult()).longValue();
    }

    private void updateNextSeries(Long lastNumber) {
        String sql = "ALTER SEQUENCE INHOUSE_FILE_TRAILER_SEQ START WITH " + lastNumber;
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}
