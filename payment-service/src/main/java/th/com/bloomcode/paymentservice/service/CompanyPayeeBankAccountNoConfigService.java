//package th.com.bloomcode.paymentservice.service;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.model.response.CompanyPayeeBankAccountNoConfigRespone;
//import th.com.bloomcode.paymentservice.payment.dao.CompanyPayeeHouseBankKeyConfigRepository;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayee;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayeeBankAccountNoConfig;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayeeHouseBankKeyConfig;
//import th.com.bloomcode.paymentservice.util.Util;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CompanyPayeeBankAccountNoConfigService {
//
//    Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final th.com.bloomcode.paymentservice.payment.dao.CompanyPayeeBankAccountNoConfigRepository companyPayeeBankAccountNoConfigRepository;
//
//    private final CompanyPayeeHouseBankKeyConfigRepository companyPayeeHouseBankKeyConfigRepository;
//
//    private final CompanyPayeeService companyPayeeService;
//
//    private final EntityManager entityManager;
//
//    @Autowired
//    public CompanyPayeeBankAccountNoConfigService(th.com.bloomcode.paymentservice.payment.dao.CompanyPayeeBankAccountNoConfigRepository companyPayeeBankAccountNoConfigRepository, CompanyPayeeHouseBankKeyConfigRepository companyPayeeHouseBankKeyConfigRepository, CompanyPayeeService companyPayeeService, @Qualifier("paymentEntityManagerFactory") EntityManager entityManager) {
//        this.companyPayeeBankAccountNoConfigRepository = companyPayeeBankAccountNoConfigRepository;
//        this.companyPayeeHouseBankKeyConfigRepository = companyPayeeHouseBankKeyConfigRepository;
//        this.companyPayeeService = companyPayeeService;
//        this.entityManager = entityManager;
//    }
//
//
//    public ResponseEntity<Result<List<CompanyPayeeBankAccountNoConfigRespone>>> findAllByHouseBankKeyId(Long houseBankKeyId) {
//        Result<List<CompanyPayeeBankAccountNoConfigRespone>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            List<CompanyPayeeBankAccountNoConfig> companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
//                    .findAllByHouseBankKeyIdOrderByAccountCodeAsc(houseBankKeyId);
//            List<CompanyPayeeBankAccountNoConfigRespone> responseList = new ArrayList<>();
//            if (companyPayeeBankAccountNoConfig.size() > 0) {
//
//
//                for (CompanyPayeeBankAccountNoConfig payeeBankAccountNoConfig : companyPayeeBankAccountNoConfig) {
//                    CompanyPayeeBankAccountNoConfigRespone response = new CompanyPayeeBankAccountNoConfigRespone();
//                    BeanUtils.copyProperties(payeeBankAccountNoConfig, response);
//
//                    CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository.findOneById(payeeBankAccountNoConfig.getHouseBankKeyId());
//
//                    CompanyPayee companyPayee = companyPayeeService.findOneById(companyPayeeHouseBankKeyConfig.getCompanyPayeeId());
//                    logger.info("companyPayee {}", companyPayee);
//                    if (companyPayee != null) {
//                        response.setCompanyPayee(companyPayee);
//                    }
//
//                    responseList.add(response);
//                }
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(responseList);
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
//    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> save(CompanyPayeeBankAccountNoConfig request) {
//        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            CompanyPayeeBankAccountNoConfig checkDuplicate = this.companyPayeeBankAccountNoConfigRepository
//                    .findOneByAccountCode(request.getAccountCode());
//
//            if (null == checkDuplicate) {
//
//                logger.info("request {}", request);
////
//
//                if (Util.isEmpty(request.getHouseBankKey()) || Util.isEmpty(request.getAccountCode()) || Util.isEmpty(request.getBankAccountNo())) {
//                    result.setStatus(HttpStatus.BAD_REQUEST.value());
//                    result.setData(null);
//                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
//                } else {
//                    CompanyPayeeHouseBankKeyConfig houseBankKey = companyPayeeHouseBankKeyConfigRepository.findOneById(request.getHouseBankKeyId());
//                    request.setBankName(houseBankKey.getBankName());
//                    request.setBankBranch(houseBankKey.getBankBranch());
//                    request.setAddress1(houseBankKey.getAddress1());
//                    request.setAddress2(houseBankKey.getAddress2());
//                    request.setAddress3(houseBankKey.getAddress3());
//                    request.setAddress4(houseBankKey.getAddress4());
//                    request.setAddress5(houseBankKey.getAddress5());
//                    request.setCountry(houseBankKey.getCountry());
//                    request.setCountryNameEn(houseBankKey.getCountryNameEn());
//                    CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = this.companyPayeeBankAccountNoConfigRepository
//                            .save(request);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayeeBankAccountNoConfig);
//                    return new ResponseEntity<>(result, HttpStatus.CREATED);
//                }
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
//    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> update(CompanyPayeeBankAccountNoConfig request) {
//        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getId()) {
//                CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
//                        .findOneById(request.getId());
//
//                if (null != companyPayeeBankAccountNoConfig) {
////					companyPayeeBankAccountNoConfig.setMinimumAmount(request.getMinimumAmount());
////					companyPayeeBankAccountNoConfig.setMaximumAmount(request.getMaximumAmount());
////					companyPayeeBankAccountNoConfig.setAllowedSinglePayment(request.isAllowedSinglePayment());
////					companyPayeeBankAccountNoConfig
////							.setAllowedPartnerAnotherCountry(request.isAllowedPartnerAnotherCountry());
////					companyPayeeBankAccountNoConfig
////							.setAllowedCurrencyAnotherCountry(request.isAllowedCurrencyAnotherCountry());
////					companyPayeeBankAccountNoConfig.setAllowedBankAnotherCountry(request.isAllowedBankAnotherCountry());
//
//                    companyPayeeBankAccountNoConfigRepository.save(companyPayeeBankAccountNoConfig);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayeeBankAccountNoConfig);
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
//    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> delete(Long id) {
//        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
//                        .findOneById(id);
//                companyPayeeBankAccountNoConfigRepository.deleteById(id);
//                if (null != companyPayeeBankAccountNoConfig) {
//
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayeeBankAccountNoConfig);
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
//    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfigRespone>> findById(Long id) {
//        Result<CompanyPayeeBankAccountNoConfigRespone> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
//                        .findOneById(id);
//                if (null != companyPayeeBankAccountNoConfig) {
//
//                    CompanyPayeeBankAccountNoConfigRespone response = new CompanyPayeeBankAccountNoConfigRespone();
//                    BeanUtils.copyProperties(companyPayeeBankAccountNoConfig, response);
//
//                    CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository.findOneById(companyPayeeBankAccountNoConfig.getHouseBankKeyId());
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
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }
//
//    public CompanyPayeeBankAccountNoConfig findOneById(Long id) {
//        return this.companyPayeeBankAccountNoConfigRepository.findOneById(id);
//    }
//
////	public CompanyPayeeBankAccountNoConfigRepository findOneByCompanyCodeAndPayMethod(String companyCode, String payMethod) {
////		return this.companyPayeeBankAccountNoConfigRepository.findOneByCompanyCodeAndPayMethod(companyCode, payMethod);
////	}
//
//    @Transactional
//    public void deleteAllByHouseBankKeyId(Long houseBankKeyId) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("DELETE ");
//        sb.append(" FROM ");
//        sb.append(" COMPANY_PAYEE_BANK_ACCOUNT_NO_CONFIG P");
//        sb.append(" WHERE ");
//        sb.append(" P.HOUSE_BANK_KEY_ID  = " + houseBankKeyId);
//        logger.info(sb.toString());
//        Query q = entityManager.createNativeQuery(sb.toString());
//        q.executeUpdate();
//
//    }
//
//}
