package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.HouseBankAccount;
import th.com.bloomcode.paymentservice.idem.entity.NonBusinessDay;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NonBusinessDayRepository {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<NonBusinessDay> findByDateAndRangeDay(String dateBegin, String rangeDay) {
        Map<String, Object> params = new HashMap<>();
        params.put("dateBegin", dateBegin);
        params.put("rangeDay", rangeDay);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" C_NONBUSINESSDAY_ID, ");
        sb.append(" ISACTIVE, ");
        sb.append(" NAME, ");
        sb.append(" DATE1 ");
        sb.append(" FROM ");
        sb.append(" C_NONBUSINESSDAY ");
        sb.append(" WHERE 1 = 1 ");
        sb.append(" AND DATE1 BETWEEN TO_DATE(:dateBegin,'YYYY-MM-DD') AND TO_DATE(:dateBegin,'YYYY-MM-DD') + :rangeDay ");
        logger.info("sql : {}", sb);
        Query q = entityManager.createNativeQuery(sb.toString(), NonBusinessDay.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<NonBusinessDay> houseBankAccount = q.getResultList();
        logger.info("houseBankAccount : {}", houseBankAccount);

        return houseBankAccount;
    }

    public List<NonBusinessDay> getAll() {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" C_NONBUSINESSDAY_ID, ");
        sb.append(" ISACTIVE, ");
        sb.append(" NAME, ");
        sb.append(" DATE1 ");
        sb.append(" FROM ");
        sb.append(" C_NONBUSINESSDAY ");
        logger.info("sql : {}", sb);
        Query q = entityManager.createNativeQuery(sb.toString(), NonBusinessDay.class);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
        List<NonBusinessDay> houseBankAccount = q.getResultList();
        logger.info("houseBankAccount : {}", houseBankAccount);

        return houseBankAccount;
    }

}
