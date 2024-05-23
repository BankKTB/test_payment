package th.com.bloomcode.paymentservice.idem.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.idem.entity.Wtx;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WtxRepository {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("idemEntityManagerFactory")
    private EntityManager entityManager;

    public List<Wtx> findAll() {
        Map<String, Object> params = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" TH_CAWITHHOLDINGTAX_ID,WTXCODE,CE.VALUE AS GLACCOUNT ");
        sb.append(" FROM ");
        sb.append(" TH_CAWITHHOLDINGTAX ");
        sb.append(" LEFT JOIN C_ELEMENTVALUE CE ON TH_CAWITHHOLDINGTAX.ACCOUNT_ID = CE.C_ELEMENTVALUE_ID ");


        Query q = entityManager.createNativeQuery(sb.toString(), Wtx.class);
//        for (Map.Entry<String, Object> entry : params.entrySet()) {
//            q.setParameter(entry.getKey(), entry.getValue());
//        }
        List<Wtx> wtx = q.getResultList();
        logger.info("wtxs : {}", wtx);

        return wtx;
    }


}
