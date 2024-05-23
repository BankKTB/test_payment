package th.com.bloomcode.paymentservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.payment.dao.PayMethodCurrencyConfigRepository;
import th.com.bloomcode.paymentservice.payment.entity.PayMethodCurrencyConfig;
import th.com.bloomcode.paymentservice.service.payment.PayMethodConfigService;

import java.util.Date;
import java.util.List;

@Service
public class PayMethodCurrencyConfigService {

    private final PayMethodCurrencyConfigRepository payMethodCurrencyConfigRepository;
    private final PayMethodConfigService payMethodConfigService;

    @Autowired
    public PayMethodCurrencyConfigService(PayMethodCurrencyConfigRepository payMethodCurrencyConfigRepository, PayMethodConfigService payMethodConfigService) {
        this.payMethodCurrencyConfigRepository = payMethodCurrencyConfigRepository;
        this.payMethodConfigService = payMethodConfigService;
    }

//    public ResponseEntity<Result<PayMethodCurrencyConfig>> save(PayMethodCurrencyConfig request) {
//        Result<PayMethodCurrencyConfig> result = new Result<>();
//        result.setTimestamp(new Date());
//        try {
//            if (null == request.getId()) {
//                PayMethodConfig payMethodConfig = payMethodConfigService.findOneById(request.getPayMethodConfigId());
//                if (null != payMethodConfig) {
//                    PayMethodCurrencyConfig checkDuplicate = payMethodCurrencyConfigRepository
//                            .findOneByPayMethodConfigIdAndCurrency(request.getPayMethodConfigId(),
//                                    request.getCurrency());
//
//                    if (null == checkDuplicate) {
//
//                        payMethodCurrencyConfigRepository.save(request);
//                        result.setStatus(HttpStatus.CREATED.value());
//                        result.setData(request);
//                        return new ResponseEntity<>(result, HttpStatus.OK);
//                    } else {
//                        result.setStatus(HttpStatus.FORBIDDEN.value());
//                        result.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
//                        return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
//                    }
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

    public ResponseEntity<Result<List<PayMethodCurrencyConfig>>> findAllByPayMethodConfigId(Long payMethodConfigId) {
        Result<List<PayMethodCurrencyConfig>> result = new Result<>();
        result.setTimestamp(new Date());
        try {

            List<PayMethodCurrencyConfig> payMethodCurrencyConfig = this.payMethodCurrencyConfigRepository
                    .findAllByPayMethodConfigId(payMethodConfigId);
            if (payMethodCurrencyConfig.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(payMethodCurrencyConfig);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NO_CONTENT.value());
                result.setData(payMethodCurrencyConfig);
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<Result<PayMethodCurrencyConfig>> deleteById(Long id) {
        Result<PayMethodCurrencyConfig> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                PayMethodCurrencyConfig payMethodCurrencyConfig = payMethodCurrencyConfigRepository.findOneById(id);
                payMethodCurrencyConfigRepository.deleteById(id);
                if (null != payMethodCurrencyConfig) {

                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(payMethodCurrencyConfig);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(payMethodCurrencyConfig);
                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public PayMethodCurrencyConfig findOneById(Long id) {
        return this.payMethodCurrencyConfigRepository.findOneById(id);
    }

//	public PayMethodCurrencyConfig findOneByIdAndPayMethodConfigId(Long id, Long payMethodConfigId) {
//		return this.payMethodCurrencyConfigRepository.findOneByIdAndPayMethodConfigId(id, payMethodConfigId);
//	}

    public List<PayMethodCurrencyConfig> getAllByPayMethodConfigId(Long payMethodConfigId) {
        return this.payMethodCurrencyConfigRepository.findAllByPayMethodConfigId(payMethodConfigId);
    }

//	public void deleteById(Long id) {
//		this.payMethodCurrencyConfigRepository.deleteById(id);
//	}

//	public ResponseEntity<Result<PayMethodCurrencyConfigResponse>> save(PayMethodCurrencyConfigRequest request) {
//		Result<PayMethodCurrencyConfigResponse> result = new Result<>();
//		result.setTimestamp(new Date());
//		try {
//
//			PayMethodCurrencyConfigResponse payMethodCurrencyConfigResponse = new PayMethodCurrencyConfigResponse();
//			List<PayMethodCurrencyConfig> listPayMethodCurrencyConfig = new ArrayList<PayMethodCurrencyConfig>();
//
//			if (null != request) {
//				List<PayMethodCurrencyConfig> listCurrencyConfigCurrent = findAllByPayMethodConfigId(
//						request.getPayMethodConfigId());
//				List<PayMethodCurrencyConfig> listCurrencyConfigNew = request.getPayMethodCurrencyConfig();
//
//				if (listCurrencyConfigNew.size() >= listCurrencyConfigCurrent.size()) {
//
//					for (int i = 0; i < listCurrencyConfigNew.size(); i++) {
//
//						if (null != listCurrencyConfigNew.get(i).getId() && listCurrencyConfigNew.get(i).getId() > 0) {
//
//							PayMethodCurrencyConfig payMethodCurrencyConfig = findOneByIdAndPayMethodConfigId(
//									listCurrencyConfigNew.get(i).getId(),
//									listCurrencyConfigNew.get(i).getPayMethodConfigId());
//							if (null != payMethodCurrencyConfig) {
//								payMethodCurrencyConfig.setCurrency(listCurrencyConfigNew.get(i).getCurrency());
//								this.payMethodCurrencyConfigRepository.save(payMethodCurrencyConfig);
//								listPayMethodCurrencyConfig.add(payMethodCurrencyConfig);
//							} else {
//								PayMethodCurrencyConfig newPayMethodCurrencyConfig = new PayMethodCurrencyConfig();
//								newPayMethodCurrencyConfig.setPayMethodConfigId(request.getPayMethodConfigId());
//								newPayMethodCurrencyConfig.setCurrency(listCurrencyConfigNew.get(i).getCurrency());
//								this.payMethodCurrencyConfigRepository.save(newPayMethodCurrencyConfig);
//								listPayMethodCurrencyConfig.add(newPayMethodCurrencyConfig);
//							}
//
//						} else {
//							PayMethodCurrencyConfig newPayMethodCurrencyConfig = new PayMethodCurrencyConfig();
//							newPayMethodCurrencyConfig.setPayMethodConfigId(request.getPayMethodConfigId());
//							newPayMethodCurrencyConfig.setCurrency(listCurrencyConfigNew.get(i).getCurrency());
//							this.payMethodCurrencyConfigRepository.save(newPayMethodCurrencyConfig);
//							listPayMethodCurrencyConfig.add(newPayMethodCurrencyConfig);
//						}
//
//					}
//					payMethodCurrencyConfigResponse.setPayMethodConfigId(request.getPayMethodConfigId());
//					payMethodCurrencyConfigResponse.setPayMethodCurrencyConfig(listPayMethodCurrencyConfig);
//				} else {
//					for (int j = 0; j < listCurrencyConfigCurrent.size(); j++) {
//						this.payMethodCurrencyConfigRepository.deleteById(listCurrencyConfigCurrent.get(j).getId());
//					}
//
//					if (listCurrencyConfigNew.size() != 0) {
//
//						for (int i = 0; i < listCurrencyConfigNew.size(); i++) {
//
//							PayMethodCurrencyConfig newPayMethodCurrencyConfig = new PayMethodCurrencyConfig();
//							newPayMethodCurrencyConfig.setPayMethodConfigId(request.getPayMethodConfigId());
//							newPayMethodCurrencyConfig.setCurrency(listCurrencyConfigNew.get(i).getCurrency());
//							this.payMethodCurrencyConfigRepository.save(newPayMethodCurrencyConfig);
//							listPayMethodCurrencyConfig.add(newPayMethodCurrencyConfig);
//						}
//					}
//					payMethodCurrencyConfigResponse.setPayMethodConfigId(request.getPayMethodConfigId());
//					payMethodCurrencyConfigResponse.setPayMethodCurrencyConfig(listPayMethodCurrencyConfig);
//				}
//
//				result.setStatus(HttpStatus.OK.value());
//				result.setData(payMethodCurrencyConfigResponse);
//				return new ResponseEntity<>(result, HttpStatus.OK);
//			} else {
//				result.setStatus(HttpStatus.NOT_FOUND.value());
//				result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
//				return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//			result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
}
