package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeBankAccountNoConfig;
import th.com.bloomcode.paymentservice.model.payment.CompanyPayeeHouseBankKeyConfig;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayeeBankAccountNoConfigRepository;
import th.com.bloomcode.paymentservice.repository.payment.CompanyPayeeHouseBankKeyConfigRepository;
import th.com.bloomcode.paymentservice.service.payment.CompanyPayeeBankAccountNoConfigService;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CompanyPayeeBankAccountNoConfigServiceImpl implements CompanyPayeeBankAccountNoConfigService {
    private final CompanyPayeeBankAccountNoConfigRepository companyPayeeBankAccountNoConfigRepository;
    private final CompanyPayeeHouseBankKeyConfigRepository companyPayeeHouseBankKeyConfigRepository;

    public CompanyPayeeBankAccountNoConfigServiceImpl(CompanyPayeeBankAccountNoConfigRepository companyPayeeBankAccountNoConfigRepository, CompanyPayeeHouseBankKeyConfigRepository companyPayeeHouseBankKeyConfigRepository) {
        this.companyPayeeBankAccountNoConfigRepository = companyPayeeBankAccountNoConfigRepository;
        this.companyPayeeHouseBankKeyConfigRepository = companyPayeeHouseBankKeyConfigRepository;
    }

    @Override
    public ResponseEntity<Result<List<CompanyPayeeBankAccountNoConfig>>> findAllByHouseBankKeyId(String value) {
        Result<List<CompanyPayeeBankAccountNoConfig>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<CompanyPayeeBankAccountNoConfig> generateFileAliases = companyPayeeBankAccountNoConfigRepository.findAllByHouseBankKeyIdOrderByAccountCodeAsc(value);
            if (generateFileAliases.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(generateFileAliases);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
                return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> save(CompanyPayeeBankAccountNoConfig request) {
        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            CompanyPayeeBankAccountNoConfig checkDuplicate = this.companyPayeeBankAccountNoConfigRepository
                    .findOneByAccountCode(request.getAccountCode());
            if (null == checkDuplicate) {
                log.info("request {}", request);
                if (Util.isEmpty(request.getHouseBankKey()) || Util.isEmpty(request.getAccountCode()) || Util.isEmpty(request.getBankAccountNo())) {
                    result.setStatus(HttpStatus.BAD_REQUEST.value());
                    result.setData(null);
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
                } else {
                    CompanyPayeeBankAccountNoConfig houseBankKey = companyPayeeBankAccountNoConfigRepository.findById(request.getHouseBankKeyId()).orElse(null);
                    request.setBankName(houseBankKey.getBankName());
                    request.setBankBranch(houseBankKey.getBankBranch());
                    request.setAddress1(houseBankKey.getAddress1());
                    request.setAddress2(houseBankKey.getAddress2());
                    request.setAddress3(houseBankKey.getAddress3());
                    request.setAddress4(houseBankKey.getAddress4());
                    request.setAddress5(houseBankKey.getAddress5());
                    request.setCountry(houseBankKey.getCountry());
                    request.setCountryNameEn(houseBankKey.getCountryNameEn());
                    CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = this.companyPayeeBankAccountNoConfigRepository
                            .save(request);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(companyPayeeBankAccountNoConfig);
                    return new ResponseEntity<>(result, HttpStatus.CREATED);
                }
            } else {
                result.setMessage("already exist");
                result.setStatus(HttpStatus.CONFLICT.value());
                result.setData(null);
                return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> update(CompanyPayeeBankAccountNoConfig request) {
        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != request.getId()) {
                CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
                        .findById(request.getId()).orElse(null);
                if (null != companyPayeeBankAccountNoConfig) {
//					companyPayeeBankAccountNoConfig.setMinimumAmount(request.getMinimumAmount());
//					companyPayeeBankAccountNoConfig.setMaximumAmount(request.getMaximumAmount());
//					companyPayeeBankAccountNoConfig.setAllowedSinglePayment(request.isAllowedSinglePayment());
//					companyPayeeBankAccountNoConfig
//							.setAllowedPartnerAnotherCountry(request.isAllowedPartnerAnotherCountry());
//					companyPayeeBankAccountNoConfig
//							.setAllowedCurrencyAnotherCountry(request.isAllowedCurrencyAnotherCountry());
//					companyPayeeBankAccountNoConfig.setAllowedBankAnotherCountry(request.isAllowedBankAnotherCountry());
                    companyPayeeBankAccountNoConfigRepository.save(companyPayeeBankAccountNoConfig);
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(companyPayeeBankAccountNoConfig);
                    return new ResponseEntity<>(result, HttpStatus.CREATED);
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

    @Override
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> delete(Long id) {
        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
                        .findById(id).orElse(null);
                companyPayeeBankAccountNoConfigRepository.deleteById(id);
                if (null != companyPayeeBankAccountNoConfig) {
                    result.setStatus(HttpStatus.CREATED.value());
                    result.setData(companyPayeeBankAccountNoConfig);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(null);
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

    @Override
    public ResponseEntity<Result<CompanyPayeeBankAccountNoConfig>> findById(Long id) {
        Result<CompanyPayeeBankAccountNoConfig> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            if (null != id) {
                CompanyPayeeBankAccountNoConfig companyPayeeBankAccountNoConfig = companyPayeeBankAccountNoConfigRepository
                        .findById(id).orElse(null);
                if (null != companyPayeeBankAccountNoConfig) {
                    CompanyPayeeBankAccountNoConfig response = new CompanyPayeeBankAccountNoConfig();
                    BeanUtils.copyProperties(companyPayeeBankAccountNoConfig, response);
                    CompanyPayeeHouseBankKeyConfig companyPayeeHouseBankKeyConfig = companyPayeeHouseBankKeyConfigRepository.findById(companyPayeeBankAccountNoConfig.getHouseBankKeyId()).orElse(null);
//                  FIX TNAG
//                    CompanyPayee companyPayee = companyPayeeService.findOneById(companyPayeeHouseBankKeyConfig.getCompanyPayeeId());
//                    log.info("companyPayee {}", companyPayee);
//                    if (companyPayee != null) {
//                        response.setCompanyPayee(companyPayee);
//                    }

                    result.setStatus(HttpStatus.OK.value());
                    result.setData(response);
                    return new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    result.setStatus(HttpStatus.NO_CONTENT.value());
                    result.setData(null);
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

    @Override
    public CompanyPayeeBankAccountNoConfig findOneById(Long id) {
        return this.companyPayeeBankAccountNoConfigRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteAllByHouseBankKeyId(Long houseBankKeyId) {
        companyPayeeBankAccountNoConfigRepository.deleteAllByHouseBankKeyId(houseBankKeyId);
    }

}
