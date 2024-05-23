package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.GLAccount;
import th.com.bloomcode.paymentservice.repository.payment.GLAccountRepository;
import th.com.bloomcode.paymentservice.service.payment.GLAccountService;

import java.util.List;

@Service
@Slf4j
public class GLAccountServiceImpl implements GLAccountService {
    private final GLAccountRepository gLAccountRepository;

    public GLAccountServiceImpl(@Qualifier("GLAccountRepositoryImpl") GLAccountRepository gLAccountRepository) {
        this.gLAccountRepository = gLAccountRepository;
    }


    @Override
    public GLAccount findOneByDocTypeAndFundSource(String docType, String fundSource) {
        return gLAccountRepository.findOneByDocTypeAndFundSource(docType, fundSource);
    }

    @Override
    public List<GLAccount> findAll() {
        return (List<GLAccount>) this.gLAccountRepository.findAll();
    }
}
