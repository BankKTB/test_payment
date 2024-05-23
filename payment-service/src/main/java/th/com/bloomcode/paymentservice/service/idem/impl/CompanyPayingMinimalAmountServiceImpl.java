package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.PaymentMethod;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayingMinimalAmount;
import th.com.bloomcode.paymentservice.repository.idem.CompanyPayingMinimalAmountRepository;
import th.com.bloomcode.paymentservice.repository.idem.PaymentMethodRepository;
import th.com.bloomcode.paymentservice.service.idem.CompanyPayingMinimalAmountService;
import th.com.bloomcode.paymentservice.service.idem.PaymentMethodService;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CompanyPayingMinimalAmountServiceImpl implements CompanyPayingMinimalAmountService {
    private final CompanyPayingMinimalAmountRepository companyPayingMinimalAmountRepository;

    public CompanyPayingMinimalAmountServiceImpl(CompanyPayingMinimalAmountRepository companyPayingMinimalAmountRepository) {
        this.companyPayingMinimalAmountRepository = companyPayingMinimalAmountRepository;
    }


    @Override
    public List<CompanyPayingMinimalAmount> findAllByValue(String valueCode) {
        return this.companyPayingMinimalAmountRepository.findAllByValueCode(valueCode);
    }

    @Override
    public CompanyPayingMinimalAmount findOneByValueCode(String valueCode) {
        return this.companyPayingMinimalAmountRepository.findOneByValueCode(valueCode);
    }


}
