//package th.com.bloomcode.paymentservice.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.payment.dao.CompanyPayeeRepository;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayee;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CompanyPayeeService {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final CompanyPayeeRepository companyPayeeRepository;
//
//    @Autowired
//    public CompanyPayeeService(CompanyPayeeRepository companyPayeeRepository) {
//        this.companyPayeeRepository = companyPayeeRepository;
//    }
//
//    public ResponseEntity<Result<CompanyPayee>> save(CompanyPayee request) {
//        Result<CompanyPayee> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getCompanyCode() && !request.getCompanyCode().isEmpty()) {
//
//                CompanyPayee checkDuplicate = companyPayeeRepository.findOneByCompanyCode(request.getCompanyCode());
//
//                if (null == checkDuplicate) {
//                    CompanyPayee companyPayee = companyPayeeRepository.save(request);
//
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayee);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//
//                } else {
//                    result.setMessage("already exist");
//                    result.setStatus(HttpStatus.CONFLICT.value());
//                    result.setData(null);
//                    return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
//
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    public ResponseEntity<Result<CompanyPayee>> update(CompanyPayee request) {
//        Result<CompanyPayee> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getId()) {
//                CompanyPayee companyPayee = companyPayeeRepository.findOneById(request.getId());
//
//                if (null != companyPayee) {
//                    companyPayee.setCompanyPayeeCode(request.getCompanyPayeeCode());
//                    companyPayee.setCompanyPayingCode(request.getCompanyPayingCode());
//                    companyPayee.setAmountDueDate(request.getAmountDueDate());
//                    companyPayee.setPaymentMethod(request.getPaymentMethod());
//                    companyPayeeRepository.save(companyPayee);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayee);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    public ResponseEntity<Result<CompanyPayee>> delete(Long id) {
//        Result<CompanyPayee> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayee companyPayee = companyPayeeRepository.findOneById(id);
//
//                if (null != companyPayee) {
//                    companyPayeeRepository.deleteById(id);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayee);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                } else {
//                    result.setStatus(HttpStatus.NO_CONTENT.value());
//                    result.setData(companyPayee);
//                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    public ResponseEntity<Result<List<CompanyPayee>>> findAll() {
//        Result<List<CompanyPayee>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            List<CompanyPayee> companyPayees = (List<CompanyPayee>) companyPayeeRepository.findAll();
//            if (companyPayees.size() > 0) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayees);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NO_CONTENT.value());
//                result.setData(companyPayees);
//                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    public ResponseEntity<Result<CompanyPayee>> findById(Long id) {
//        Result<CompanyPayee> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            CompanyPayee companyPayee = companyPayeeRepository.findOneById(id);
//            if (companyPayee != null) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayee);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NO_CONTENT.value());
//                result.setData(companyPayee);
//                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
//
//    public CompanyPayee findOneById(Long id) {
//
//        return this.companyPayeeRepository.findOneById(id);
//    }
//
//
//}
