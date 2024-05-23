package th.com.bloomcode.paymentservice.service.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.idem.dao.CompCodeRepository;
import th.com.bloomcode.paymentservice.idem.dao.WtxRepository;
import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.idem.entity.Wtx;
import th.com.bloomcode.paymentservice.model.Result;

import java.util.Date;
import java.util.List;

@Service
public class WtxService {

    @Autowired
    private WtxRepository wtxRepository;

    public List<Wtx> findAll() {
        return this.wtxRepository.findAll();
    }

}
