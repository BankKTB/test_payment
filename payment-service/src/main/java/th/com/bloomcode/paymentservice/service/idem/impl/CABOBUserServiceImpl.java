package th.com.bloomcode.paymentservice.service.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.repository.idem.CABOBUserRepository;
import th.com.bloomcode.paymentservice.service.idem.CABOBUserService;

import java.util.Date;

@Slf4j
@Service
public class CABOBUserServiceImpl implements CABOBUserService {
    private final CABOBUserRepository cabobUserRepository;

    @Autowired
    public CABOBUserServiceImpl(CABOBUserRepository cabobUserRepository) {
        this.cabobUserRepository = cabobUserRepository;
    }

    @Override
    public ResponseEntity<Result<Boolean>> existByUsernameAndPassword(String username, String password) {
        Result<Boolean> result = new Result<>();
        result.setTimestamp(new Date());
        boolean isExist = this.cabobUserRepository.existByUsernameAndPassword(username, password);
        if (isExist) {
            result.setMessage("");
            result.setStatus(HttpStatus.OK.value());
            result.setData(true);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            result.setMessage("ไม่พบสิทธิ์");
            result.setStatus(HttpStatus.FORBIDDEN.value());
            result.setData(false);
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
    }
}
