package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.payment.dao.ReturnReasonRepository;
import th.com.bloomcode.paymentservice.payment.entity.ReturnReason;

@Service
public class ReturnReasonService {

    private final ReturnReasonRepository returnReasonRepository;

    @Autowired
    public ReturnReasonService(ReturnReasonRepository returnReasonRepository) {
        this.returnReasonRepository = returnReasonRepository;
    }

    public ReturnReason findOneByReasonNo(String reasonNo) {
        return this.returnReasonRepository.findOneByReasonNo(reasonNo);
    }
}
