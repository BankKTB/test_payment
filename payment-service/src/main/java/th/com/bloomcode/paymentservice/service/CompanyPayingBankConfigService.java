//package th.com.bloomcode.paymentservice.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import th.com.bloomcode.paymentservice.model.Result;
//import th.com.bloomcode.paymentservice.payment.dao.CompanyPayingBankConfigRepository;
//import th.com.bloomcode.paymentservice.payment.entity.CompanyPayingBankConfig;
//
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class CompanyPayingBankConfigService {
//
//    private final CompanyPayingBankConfigRepository companyPayingBankConfigRepository;
//
//    @Autowired
//    public CompanyPayingBankConfigService(CompanyPayingBankConfigRepository companyPayingBankConfigRepository) {
//        this.companyPayingBankConfigRepository = companyPayingBankConfigRepository;
//    }
//
//    public ResponseEntity<Result<CompanyPayingBankConfig>> save(CompanyPayingBankConfig request) {
//        Result<CompanyPayingBankConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null == request.getId()) {
//                CompanyPayingBankConfig checkDuplicate = companyPayingBankConfigRepository
//                        .findOneByPayMethodAndCompanyPayingId(request.getPayMethod(), request.getCompanyPayingId());
//
//                if (null == checkDuplicate) {
//
//                    CompanyPayingBankConfig companyPayingBankConfig = companyPayingBankConfigRepository.save(request);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayingBankConfig);
//                    return new ResponseEntity<>(result, HttpStatus.CREATED);
//                } else {
//                    result.setStatus(HttpStatus.FORBIDDEN.value());
//                    result.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
//                    return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
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
//    public ResponseEntity<Result<CompanyPayingBankConfig>> update(CompanyPayingBankConfig request) {
//        Result<CompanyPayingBankConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != request.getId()) {
//                CompanyPayingBankConfig current = companyPayingBankConfigRepository.findOneById(request.getId());
//                if (null != current) {
//
////					current.setPayMethod(request.getPayMethod());
////					current.setCurrency(request.getCurrency());
////					current.setSequence(request.getSequence());
////					current.setBankAgent(request.getBankAgent());
////					current.setBankAccountNo(request.getBankAccountNo());
////					current.setAmountDay(request.getAmountDay());
////					current.setAmountForPay(request.getAmountForPay());
////					current.setAmountForAccept(request.getAmountForAccept());
////
////					companyPayingBankConfigRepository.save(request);
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(request);
//                    return new ResponseEntity<>(result, HttpStatus.CREATED);
//                } else {
//                    result.setStatus(HttpStatus.NO_CONTENT.value());
//                    result.setData(current);
//                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//                }
//
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
//    public ResponseEntity<Result<List<CompanyPayingBankConfig>>> findAllByCompanyPayingId(Long companyPayingId) {
//        Result<List<CompanyPayingBankConfig>> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//
//            List<CompanyPayingBankConfig> companyPayingBankConfig = companyPayingBankConfigRepository
//                    .findAllByCompanyPayingIdOrderByPayMethodAsc(companyPayingId);
//            if (companyPayingBankConfig.size() > 0) {
//                result.setStatus(HttpStatus.OK.value());
//                result.setData(companyPayingBankConfig);
//                return new ResponseEntity<>(result, HttpStatus.OK);
//            } else {
//                result.setStatus(HttpStatus.NO_CONTENT.value());
//                result.setData(companyPayingBankConfig);
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
//    //	public void save(Long companyPayingId) {
////		CompanyPayingBankConfig companyPayingConfig  = new CompanyPayingBankConfig();
////		companyPayingConfig.setCompanyPayingId(companyPayingId);
////		this.companyPayingBankConfigRepository.save(companyPayingConfig);
////
////	}
////
//    public ResponseEntity<Result<CompanyPayingBankConfig>> deleteById(Long id) {
//        Result<CompanyPayingBankConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null != id) {
//                CompanyPayingBankConfig companyPayingBankConfig = companyPayingBankConfigRepository.findOneById(id);
//                companyPayingBankConfigRepository.deleteById(id);
//                if (null != companyPayingBankConfig) {
//
//                    result.setStatus(HttpStatus.CREATED.value());
//                    result.setData(companyPayingBankConfig);
//                    return new ResponseEntity<>(result, HttpStatus.OK);
//                } else {
//                    result.setStatus(HttpStatus.NO_CONTENT.value());
//                    result.setData(companyPayingBankConfig);
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
////
////	public ResponseEntity<Result<CompanyPayingBankConfig>> findByCompanyPayingId(Long companyPayingId) {
////		Result<CompanyPayingBankConfig> result = new Result<>();
////		result.setTimestamp(new Date());
////		try {
////			if (companyPayingId != 0) {
////				CompanyPayingBankConfig companyPayingConfig = companyPayingBankConfigRepository
////						.findOneByCompanyPayingId(companyPayingId);
////
////				if (null != companyPayingConfig) {
////
////					result.setStatus(HttpStatus.OK.value());
////					result.setData(companyPayingConfig);
////					return new ResponseEntity<>(result, HttpStatus.OK);
////				} else {
////					result.setStatus(HttpStatus.NOT_FOUND.value());
////					result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
////					return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
////				}
////			}
////		} catch (Exception e) {
////			e.printStackTrace();
////			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
////			result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
////			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
////		}
////
////		return new ResponseEntity<>(result, HttpStatus.OK);
////	}
//}
