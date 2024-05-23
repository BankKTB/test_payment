package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.idem.PeriodControl;
import th.com.bloomcode.paymentservice.repository.idem.PeriodControlRepository;
import th.com.bloomcode.paymentservice.service.idem.PeriodControlService;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Service
public class PeriodControlServiceImpl implements PeriodControlService {
    private final PeriodControlRepository periodControlRepository;

    @Autowired
    public PeriodControlServiceImpl(PeriodControlRepository periodControlRepository) {
        this.periodControlRepository = periodControlRepository;
    }


    @Override
    public List<PeriodControl> findAllByCondition(int period,String fiscalYear, String orgValue) {
        return this.periodControlRepository.findAllByCondition(period, fiscalYear, orgValue);
    }

    @Override
    public List<PeriodControl> findAllByCondition(Timestamp date, String orgValue) {
        return this.periodControlRepository.findAllByCondition(date, orgValue);
    }
}
