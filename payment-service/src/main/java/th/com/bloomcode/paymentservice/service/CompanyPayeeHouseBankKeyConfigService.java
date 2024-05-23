//package th.com.bloomcode.paymentservice.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.model.response.CompanyPayeeHouseBankKeyConfigRespone;
//import th.com.bloomcode.paymentservice.payment.dao.CompanyPayeeHouseBankKeyConfigRepository;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayee;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayeeHouseBankKeyConfig;
//import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeBankAccountNoConfigService;
//import th.com.bloomcode.paymentservice.util.Util;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CompanyPayeeHouseBankKeyConfigService {
//
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final CompanyPayeeHouseBankKeyConfigRepository companyPayeeHouseBankKeyConfigRepository;
//
//    private final CompanyPayeeService companyPayeeService;
//
//    @Autowired
//    public CompanyPayeeHouseBankKeyConfigService(CompanyPayeeHouseBankKeyConfigRepository companyPayeeHouseBankKeyConfigRepository, CompanyPayeeService companyPayeeService, CompanyPayeeBankAccountNoConfigService companyPayeeBankAccountNoConfigService) {
//        this.companyPayeeHouseBankKeyConfigRepository = companyPayeeHouseBankKeyConfigRepository;
//        this.companyPayeeService = companyPayeeService;
//    }
//
//    public ResponseEntity<Result<List<CompanyPayeeHouseBankKeyConfig>>> findAllByCompanyPayeeId(Long companyPayeeId) {
//        Result<List<CompanyPayeeHouseBankKeyConfig>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            List<CompanyPayeeHouseBankKeyConfig> companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository
//                    .findAllByCompanyPayeeIdOrderByHouseBankKeyAscCountryAscBankBranchAsc(companyPayeeId);
//
//            if (companyPayeeHouseBankKeyConfig.size() > 0) {
//
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayeeHouseBankKeyConfig);
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
//    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> save(CompanyPayeeHouseBankKeyConfig request) {
//        Result<CompanyPayeeHouseBankKeyConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            CompanyPayeeHouseBankKeyConfig checkDuplicate = this.companyPayeeHouseBankKeyConfigRepository
//                    .findOneByHouseBankKeyAndCountryAndBankBranchAndCompanyPayeeId(request.getHouseBankKey(), request.getCountry(),
//                            request.getBankBranch(), request.getCompanyPayeeId());
//
//            if (null == checkDuplicate) {
//                if (Util.isEmpty(request.getHouseBankKey()) || Util.isEmpty(request.getBankBranch()) || Util.isEmpty(request.getCountry())) {
//                    result.setStatus(HttpStatus.BAD_REQUEST.value());
//                    result.setData(null);
//                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//                } else {
//                    CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = this.companyPayeeHouseBankKeyConfigRepository
//                            .save(request);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayeeHouseBankKeyConfig);
//                    return new ResponseEntity<>(result, HttpStatus.CREATED);
//                }
//
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
//    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> update(CompanyPayeeHouseBankKeyConfig request) {
//        Result<CompanyPayeeHouseBankKeyConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getId()) {
//                CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository
//                        .findOneById(request.getId());
//
//                if (null != companyPayeeHouseBankKeyConfig) {
////					companyPayeeHouseBankKeyConfig.setMinimumAmount(request.getMinimumAmount());
////					companyPayeeHouseBankKeyConfig.setMaximumAmount(request.getMaximumAmount());
////					companyPayeeHouseBankKeyConfig.setAllowedSinglePayment(request.isAllowedSinglePayment());
////					companyPayeeHouseBankKeyConfig
////							.setAllowedPartnerAnotherCountry(request.isAllowedPartnerAnotherCountry());
////					companyPayeeHouseBankKeyConfig
////							.setAllowedCurrencyAnotherCountry(request.isAllowedCurrencyAnotherCountry());
////					companyPayeeHouseBankKeyConfig.setAllowedBankAnotherCountry(request.isAllowedBankAnotherCountry());
//
//                    companyPayeeHouseBankKeyConfigRepository.save(companyPayeeHouseBankKeyConfig);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayeeHouseBankKeyConfig);
//                    return new ResponseEntity<>(result, HttpStatus.CREATED);
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
//    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfig>> delete(Long id) {
//        Result<CompanyPayeeHouseBankKeyConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository
//                        .findOneById(id);
//
//                if (null != companyPayeeHouseBankKeyConfig) {
//                    companyPayeeHouseBankKeyConfigRepository.deleteById(id);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayeeHouseBankKeyConfig);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                } else {
//                    result.setStatus(HttpStatus.NOT_FOUND.value());
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
//
//    public ResponseEntity<Result<CompanyPayeeHouseBankKeyConfigRespone>> findById(Long id) {
//        Result<CompanyPayeeHouseBankKeyConfigRespone> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository.findOneById(id);
//
//                if (null != companyPayeeHouseBankKeyConfig) {
//                    CompanyPayeeHouseBankKeyConfigRespone response = new CompanyPayeeHouseBankKeyConfigRespone();
//                    BeanUtils.copyProperties(companyPayeeHouseBankKeyConfig, response);
//
//                    CompanyPayee companyPayee = companyPayeeService.findOneById(companyPayeeHouseBankKeyConfig.getCompanyPayeeId());
//                    logger.info("companyPayee {}", companyPayee);
//                    if (companyPayee != null) {
//                        response.setCompanyPayee(companyPayee);
//                    }
//
//                    result.setStatus(HttpStatus.OK.value());
//                    result.setData(response);
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
//    public CompanyPayeeHouseBankKeyConfig findOneById(Long id) {
//        return this.companyPayeeHouseBankKeyConfigRepository.findOneById(id);
//    }
//
////	public CompanyPayeeHouseBankKeyConfig findOneByCompanyCodeAndPayMethod(String companyCode, String payMethod) {
////		return this.companyPayeeHouseBankKeyConfigRepository.findOneByCompanyCodeAndPayMethod(companyCode, payMethod);
////	}
//
//
//}
