//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.payment.dao.SmartFileDetailRepository;
//import th.com.bloomcode.paymentservice.payment.entity.SmartFileDetail;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//public class SmartFileDetailService {
//
//    private final SmartFileDetailRepository smartFileDetailRepository;
//    private final EntityManager entityManager;
//
//    @Autowired
//    public SmartFileDetailService(SmartFileDetailRepository smartFileDetailRepository, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager) {
//        this.smartFileDetailRepository = smartFileDetailRepository;
//        this.entityManager = entityManager;
//    }
//
//    public void saveAll(List<SmartFileDetail> smartFileDetails) {
//        entityManager.getTransaction().begin();
//        int cont = 0;
//        Long id = getNextSeries(true);
//        for (SmartFileDetail smartFileDetail : smartFileDetails) {
//            smartFileDetail.setId(id++);
//            entityManager.persist(smartFileDetail);
//            cont++;
//            if (cont % 1000 == 0) {
//                entityManager.flush();
//                entityManager.clear();
//                entityManager.getTransaction().commit();
//                entityManager.getTransaction().begin();
//            }
//        }
//        updateNextSeries(--id);
//        entityManager.getTransaction().commit();
//    }
//
//    private Long getNextSeries(boolean isDetail) {
//        String sql;
//        if (isDetail) {
//            sql = "SELECT SMART_FILE_DETAIL_SEQ.NEXTVAL FROM DUAL";
//        } else {
//            sql = "SELECT SMART_FILE_LOG_SEQ.NEXTVAL FROM DUAL";
//        }
//        Query q = entityManager.createNativeQuery(sql);
//        return ((BigDecimal) q.getSingleResult()).longValue();
//    }
//
//    private void updateNextSeries(Long lastNumber) {
//        String sql = "ALTER SEQUENCE SMART_FILE_DETAIL_SEQ START WITH " + lastNumber;
//        entityManager.createNativeQuery(sql).executeUpdate();
//    }
//}
