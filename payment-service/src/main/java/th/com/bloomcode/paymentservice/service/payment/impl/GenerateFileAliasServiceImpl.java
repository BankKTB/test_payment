package th.com.bloomcode.paymentservice.service.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.GenerateFileAlias;
import th.com.bloomcode.paymentservice.model.payment.dto.GenerateFileAliasResponse;
import th.com.bloomcode.paymentservice.model.request.GenerateFileAliasRequest;
import th.com.bloomcode.paymentservice.repository.payment.GenerateFileAliasRepository;
import th.com.bloomcode.paymentservice.service.payment.GenerateFileAliasService;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class GenerateFileAliasServiceImpl implements GenerateFileAliasService {
  private final GenerateFileAliasRepository generateFileAliasRepository;
  private final JdbcTemplate jdbcTemplate;
  private final ProposalLogService proposalLogService;

  @Autowired
  public GenerateFileAliasServiceImpl(GenerateFileAliasRepository generateFileAliasRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, ProposalLogService proposalLogService) {
    this.generateFileAliasRepository = generateFileAliasRepository;
    this.jdbcTemplate = jdbcTemplate;
    this.proposalLogService = proposalLogService;
  }

  public ResponseEntity<Result<GenerateFileAlias>> save(GenerateFileAliasRequest request) {
    Result<GenerateFileAlias> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      GenerateFileAlias generateFileAlias = new GenerateFileAlias();
      if (null == request.getId() || request.getId() == 0) {
        GenerateFileAliasResponse generateFileAliasResponse = this.generateFileAliasRepository.findOneByGenerateFileDateAndGenerateFileName(request.getGenerateFileDate(), request.getGenerateFileName());
        if (null != generateFileAliasResponse && generateFileAliasResponse.getGenerateFileAliasId() > 0) {
          generateFileAlias.setId(generateFileAliasResponse.getGenerateFileAliasId());
//          if (!"W".equalsIgnoreCase(generateFileAliasResponse.getGenerateFileRunStatus())) {
//            generateFileAlias.setRunStatus(generateFileAliasResponse.getGenerateFileRunStatus());
//          }
          generateFileAlias.setRunStatus(generateFileAliasResponse.getGenerateFileRunStatus());
        } else {
          Long seq = SqlUtil.getNextSeries(jdbcTemplate, GenerateFileAlias.TABLE_NAME + GenerateFileAlias.SEQ, null);
          generateFileAlias.setId(seq);
        }
      } else {
        GenerateFileAlias old = generateFileAliasRepository.findOneById(request.getId());
        generateFileAlias.setId(request.getId());
        if (null != old) {
          generateFileAlias.setRunStatus(old.getRunStatus());
        }
      }

//      if (generateFileAlias.getId() == 0) {
//        Long seq = SqlUtil.getNextSeries(jdbcTemplate, GenerateFileAlias.TABLE_NAME + GenerateFileAlias.SEQ);
//        generateFileAlias.setId(seq);
//      }
      generateFileAlias.setGenerateFileDate(request.getGenerateFileDate());
      generateFileAlias.setGenerateFileName(request.getGenerateFileName());
      generateFileAlias.setFileName(request.getFileName());
      generateFileAlias.setSwiftAmountDay(request.getSwiftAmountDay());
      generateFileAlias.setSwiftDate(request.getSwiftDate());
      generateFileAlias.setSmartAmountDay(request.getSmartAmountDay());
      generateFileAlias.setSmartDate(request.getSmartDate());
      generateFileAlias.setGiroAmountDay(request.getGiroAmountDay());
      generateFileAlias.setGiroDate(request.getGiroDate());
      generateFileAlias.setInhouseAmountDay(request.getInhouseAmountDay());
      generateFileAlias.setInhouseDate(request.getInhouseDate());
      generateFileAlias.setCreateAgain(request.isCreateAgain());
      generateFileAlias.setTestRun(request.isTestRun());
//      generateFileAlias.setRunStatus(request.getRunStatus());
      generateFileAlias.setPaymentAliasId(request.getPaymentAliasId());

      String errText = checkExist(generateFileAlias.getGenerateFileDate(), generateFileAlias.getGenerateFileName(), generateFileAlias.isCreateAgain(), generateFileAlias.getFileName());
      if (!Util.isEmpty(errText)) {
        result.setData(null);
        result.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        result.setMessage(errText);
        return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
      } else {
        GenerateFileAlias response = generateFileAliasRepository.save(generateFileAlias);
        result.setStatus(HttpStatus.CREATED.value());
        result.setData(response);
        return new ResponseEntity<>(result, HttpStatus.OK);
      }
    } catch (Exception e) {
      e.printStackTrace();
      result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
      result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public GenerateFileAlias save(GenerateFileAlias generateFileAlias) {
    return generateFileAliasRepository.save(generateFileAlias);
  }

  @Override
  public ResponseEntity<Result<List<GenerateFileAliasResponse>>> findByCondition(String value) {
    Result<List<GenerateFileAliasResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<GenerateFileAliasResponse> generateFileAliases = generateFileAliasRepository.findByCondition(value);
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
  public ResponseEntity<Result<List<GenerateFileAliasResponse>>> findByReturn(String value) {
    Result<List<GenerateFileAliasResponse>> result = new Result<>();
    result.setTimestamp(new Date());
    try {
      List<GenerateFileAliasResponse> generateFileAliases = generateFileAliasRepository.findByReturn(value);
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
  public GenerateFileAlias findOneById(Long id) {
    return generateFileAliasRepository.findOneById(id);
  }

  @Override
  public ResponseEntity<Result<GenerateFileAliasResponse>> searchByGenerateFileDateAndGenerateFileName(Date generateFileDate, String generateFileName) {
    Result<GenerateFileAliasResponse> result = new Result<>();
    result.setTimestamp(new Date());
    Timestamp timestamp = new Timestamp(generateFileDate.getTime());
    try {
      GenerateFileAliasResponse generateFileAliase = generateFileAliasRepository.findOneByGenerateFileDateNotLessThanAndGenerateFileName(timestamp, generateFileName);
      if (null != generateFileAliase) {
        result.setStatus(HttpStatus.OK.value());
        result.setData(generateFileAliase);
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
  public boolean existByGenerateFileDateAndGenerateFileName(Timestamp generateFileDate, String generateFileName) {
    return this.generateFileAliasRepository.existByGenerateFileDateAndGenerateFileName(generateFileDate, generateFileName);
  }

  private String checkExist(Timestamp paymentDate, String paymentName, boolean isRerun, String fileName) {
    boolean isExist = proposalLogService.isExist(paymentDate, paymentName);
    String errText = "";
    if (isExist) {
      if (!isRerun) {
        errText = "กรุณาเลือกสร้างไฟล์อีกครั้งหากต้องการประมวลผลต่อไป";
      } else {
        if (fileName.isEmpty()) {
          errText = "กรุณาระบุชื่อไฟล์ที่ต้องการสร้างใหม่";
        } else {
          boolean isFileExist = proposalLogService.isFileExist(paymentDate, paymentName, fileName);
          if (!isFileExist) {
            errText = "ระบุชื่อไฟล์ไม่ถูกต้องกรุณาระบุใหม่อีกครั้ง";
          }
        }
      }
    }
    return errText;
  }
}
