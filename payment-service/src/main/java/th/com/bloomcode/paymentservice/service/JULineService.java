package th.com.bloomcode.paymentservice.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocument;
import th.com.bloomcode.paymentservice.repository.payment.JULineRepository;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.util.List;

@Slf4j
@Service
public class JULineService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final JdbcTemplate jdbcTemplate;
    private final JULineRepository juLineRepository;

    public JULineService( @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, JULineRepository juLineRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.juLineRepository = juLineRepository;
    }


    public List<JUDocument> selectJuDetail(GenerateJuRequest request, boolean export) {
        return juLineRepository.selectJuDetail(request);
    }

    public JULine save(JULine juLine) {
        logger.info("==== Save juLine : {}", juLine);
        if (null == juLine.getId() || 0 == juLine.getId()) {
            logger.info("==== Save getJuHeadId : {}", juLine.getJuHeadId());
            juLine.setId(SqlUtil.getNextSeries(jdbcTemplate, JULine.TABLE_NAME + JULine.SEQ, null));
            juLine.setJuHeadId(juLine.getJuHeadId());
            juLineRepository.save(juLine);
        }
        return juLine;
    }

    public void save(List<JULine> juLine) {
        logger.info("==== Save All juLine : {}", juLine);
        for (JULine item : juLine) {
            if (null == item.getId() || 0 == item.getId()) {
                item.setId(SqlUtil.getNextSeries(jdbcTemplate, JULine.TABLE_NAME + JULine.SEQ, null));
                juLineRepository.save(item);
            }
        }

    }

    public void deleteAllByJuHeadId(Long juHeadId) {
        logger.info("==== deleteAllByJuHeadId : {}", juHeadId);
        juLineRepository.deleteAllByJUHeadId(juHeadId);
    }
}
