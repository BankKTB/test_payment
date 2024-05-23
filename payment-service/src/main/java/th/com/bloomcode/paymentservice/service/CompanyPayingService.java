//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.payment.dao.CompanyPayingRepository;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPaying;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CompanyPayingService {
//
//    private final CompanyPayingRepository companyPayingRepository;
//
//    @Autowired
//    public CompanyPayingService(CompanyPayingRepository companyPayingRepository) {
//        this.companyPayingRepository = companyPayingRepository;
//    }
//
//    public ResponseEntity<Result<CompanyPaying>> save(CompanyPaying request) {
//        Result<CompanyPaying> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getCompanyCode() && !request.getCompanyCode().isEmpty()) {
//
//                CompanyPaying checkDuplicate = companyPayingRepository.findOneByCompanyCode(request.getCompanyCode());
//
//                if (null == checkDuplicate) {
//                    CompanyPaying companyPaying = companyPayingRepository.save(request);
//
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPaying);
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
//    public ResponseEntity<Result<CompanyPaying>> update(CompanyPaying request) {
//        Result<CompanyPaying> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getId()) {
//                CompanyPaying companyPaying = companyPayingRepository.findOneById(request.getId());
//
//                if (null != companyPaying) {
//                    companyPaying.setMinimumAmountForPay(request.getMinimumAmountForPay());
//                    companyPayingRepository.save(companyPaying);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPaying);
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
//    public ResponseEntity<Result<CompanyPaying>> delete(Long id) {
//        Result<CompanyPaying> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPaying companyPaying = companyPayingRepository.findOneById(id);
//
//                if (null != companyPaying) {
//                    companyPayingRepository.deleteById(id);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPaying);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                } else {
//                    result.setStatus(HttpStatus.NO_CONTENT.value());
//                    result.setData(companyPaying);
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
//    public ResponseEntity<Result<List<CompanyPaying>>> findAll() {
//        Result<List<CompanyPaying>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            List<CompanyPaying> companyPayings = (List<CompanyPaying>) companyPayingRepository.findAll();
//            if (companyPayings.size() > 0) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayings);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NO_CONTENT.value());
//                result.setData(companyPayings);
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
//    public ResponseEntity<Result<CompanyPaying>> findById(Long id) {
//        Result<CompanyPaying> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            CompanyPaying companyPayings = companyPayingRepository.findOneById(id);
//            if (companyPayings != null) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayings);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NO_CONTENT.value());
//                result.setData(companyPayings);
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
//    public CompanyPaying findOneById(Long id) {
//
//        return this.companyPayingRepository.findOneById(id);
//    }
//
//
//    public CompanyPaying findFirst() {
//        return this.companyPayingRepository.findFirstByOrderByCompanyCodeAsc();
//    }
//
//}
