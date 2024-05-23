package th.com.bloomcode.paymentservice.service.idem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.repository.idem.CAClientRepository;
import th.com.bloomcode.paymentservice.service.idem.CAClientService;


import java.util.List;

@Service
public class CAClientServiceImpl implements CAClientService {

    private final CAClientRepository caClientRepository;

    @Autowired
    public CAClientServiceImpl(CAClientRepository caClientRepository) {
        this.caClientRepository = caClientRepository;
    }

    @Override
    public List<CAClient> findAll() {
        return this.caClientRepository.findAll();
    }
}
