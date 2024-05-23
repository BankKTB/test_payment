//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.payment.dao.CompanyPayingPayMethodConfigRepository;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayingPayMethodConfig;
//import th.com.bloomcode.paymentservice.service.master.CompanyCodeService;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CompanyPayingPayMethodConfigService {
//
//    private final CompanyPayingPayMethodConfigRepository companyPayingPayMethodConfigRepository;
//
//    private final CompanyCodeService companyCodeService;
//
//    @Autowired
//    public CompanyPayingPayMethodConfigService(CompanyPayingPayMethodConfigRepository companyPayingPayMethodConfigRepository, CompanyCodeService companyCodeService) {
//        this.companyPayingPayMethodConfigRepository = companyPayingPayMethodConfigRepository;
//        this.companyCodeService = companyCodeService;
//    }
//
//
//    public ResponseEntity<Result<List<CompanyPayingPayMethodConfig>>> findAllByCompanyPayingId(Long companyPayingId) {
//        Result<List<CompanyPayingPayMethodConfig>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            List<CompanyPayingPayMethodConfig> companyPayingPayMethodConfig = companyPayingPayMethodConfigRepository
//                    .findAllByCompanyPayingIdOrderByPayMethodAsc(companyPayingId);
//
//            if (companyPayingPayMethodConfig.size() > 0) {
//
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayingPayMethodConfig);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NOT_FOUND.value());
//                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
//                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> save(CompanyPayingPayMethodConfig request) {
//        Result<CompanyPayingPayMethodConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            CompanyPayingPayMethodConfig checkDuplicate = this.companyPayingPayMethodConfigRepository
//                    .findOneByCompanyPayingIdAndPayMethod(request.getCompanyPayingId(), request.getPayMethod());
//
//            if (null == checkDuplicate) {
//
//                CompanyPayingPayMethodConfig companyPayingPayMethodConfig = this.companyPayingPayMethodConfigRepository
//                        .save(request);
//                result.setStatus(HttpStatus.CREATED.value());
//                result.setData(companyPayingPayMethodConfig);
//                return new ResponseEntity<>(result, HttpStatus.CREATED);
//            } else {
//                result.setMessage("already exist");
//                result.setStatus(HttpStatus.CONFLICT.value());
//                result.setData(null);
//                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> update(CompanyPayingPayMethodConfig request) {
//        Result<CompanyPayingPayMethodConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getId()) {
//                CompanyPayingPayMethodConfig companyPayingPayMethodConfig = companyPayingPayMethodConfigRepository
//                        .findOneById(request.getId());
//
//                if (null != companyPayingPayMethodConfig) {
//                    companyPayingPayMethodConfig.setMinimumAmount(request.getMinimumAmount());
//                    companyPayingPayMethodConfig.setMaximumAmount(request.getMaximumAmount());
//                    companyPayingPayMethodConfig.setAllowedSinglePayment(request.isAllowedSinglePayment());
//                    companyPayingPayMethodConfig
//                            .setAllowedPartnerAnotherCountry(request.isAllowedPartnerAnotherCountry());
//                    companyPayingPayMethodConfig
//                            .setAllowedCurrencyAnotherCountry(request.isAllowedCurrencyAnotherCountry());
//                    companyPayingPayMethodConfig.setAllowedBankAnotherCountry(request.isAllowedBankAnotherCountry());
//
//                    companyPayingPayMethodConfigRepository.save(companyPayingPayMethodConfig);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayingPayMethodConfig);
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
//    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> delete(Long id) {
//        Result<CompanyPayingPayMethodConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayingPayMethodConfig companyPayingPayMethodConfig = companyPayingPayMethodConfigRepository
//                        .findOneById(id);
//                companyPayingPayMethodConfigRepository.deleteById(id);
//                if (null != companyPayingPayMethodConfig) {
//
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayingPayMethodConfig);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                } else {
//                    result.setStatus(HttpStatus.NO_CONTENT.value());
//                    result.setData(null);
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
//    public ResponseEntity<Result<CompanyPayingPayMethodConfig>> findById(Long id) {
//        Result<CompanyPayingPayMethodConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayingPayMethodConfig companyPayingPayMethodConfig = companyPayingPayMethodConfigRepository
//                        .findOneById(id);
//
//                if (null != companyPayingPayMethodConfig) {
//
//                    result.setStatus(HttpStatus.OK.value());
//                    result.setData(companyPayingPayMethodConfig);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                } else {
//                    result.setStatus(HttpStatus.NO_CONTENT.value());
//                    result.setData(null);
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
//    public CompanyPayingPayMethodConfig findOneById(Long id) {
//        return this.companyPayingPayMethodConfigRepository.findOneById(id);
//    }
//
//}
