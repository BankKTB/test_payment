package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;
import th.com.bloomcode.paymentservice.model.request.OmDisplayColumnTableRequest;
import th.com.bloomcode.paymentservice.repository.payment.OmDisplayColumnTableRepository;
import th.com.bloomcode.paymentservice.service.payment.OmDisplayColumnTableService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OmDisplayColumnTableServiceImpl implements OmDisplayColumnTableService {
    private final OmDisplayColumnTableRepository omDisplayColumnTableRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OmDisplayColumnTableServiceImpl(OmDisplayColumnTableRepository omDisplayColumnTableRepository, @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.omDisplayColumnTableRepository = omDisplayColumnTableRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<Result<OmDisplayColumnTable>> save(OmDisplayColumnTableRequest request) {
        Result<OmDisplayColumnTable> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmDisplayColumnTable omDisplayColumnTable = this.omDisplayColumnTableRepository
                    .findOneByRoleAndName(request.getRole(), request.getName());
            if (null == omDisplayColumnTable) {
                OmDisplayColumnTable dataRequest = new OmDisplayColumnTable();
                log.info("request : {} " , request);
                log.info("request.getUserOnly() : {} " , request.getUserOnly());
                dataRequest.setId(SqlUtil.getNextSeries(jdbcTemplate, OmDisplayColumnTable.TABLE_NAME + OmDisplayColumnTable.SEQ, null));
                dataRequest.setIsUserOnly(request.getUserOnly());
                dataRequest.setJsonText(request.getJsonText());
                dataRequest.setName(request.getName());
                dataRequest.setRole(request.getRole());
                dataRequest.setCreatedBy(request.getCreateBy());
                dataRequest.setCreated(new Timestamp(new Date().getTime()));
                OmDisplayColumnTable newOmDisplayColumnTable = omDisplayColumnTableRepository.save(dataRequest);
                result.setStatus(HttpStatus.CREATED.value());
                result.setData(newOmDisplayColumnTable);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.FORBIDDEN.value());
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
    public ResponseEntity<Result<OmDisplayColumnTable>> update(OmDisplayColumnTableRequest request) {
        Result<OmDisplayColumnTable> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmDisplayColumnTable omDisplayColumnTable = this.omDisplayColumnTableRepository.findById(request.getId()).orElse(null);
            OmDisplayColumnTable dataRequest = new OmDisplayColumnTable();
            if (null != omDisplayColumnTable) {
                dataRequest.setId(request.getId());
                dataRequest.setUpdated(new Timestamp(new Date().getTime()));
            } else {
                dataRequest.setId(SqlUtil.getNextSeries(jdbcTemplate, OmDisplayColumnTable.TABLE_NAME + OmDisplayColumnTable.SEQ, null));
                dataRequest.setCreated(new Timestamp(new Date().getTime()));
            }
            log.info("request : {} " , request);
            log.info("request.getUserOnly() : {} " , request.getUserOnly());
//            dataRequest.setIsUserOnly((request.getUserOnly() == null) ? false : true);
            dataRequest.setIsUserOnly(request.getUserOnly());
            dataRequest.setJsonText(request.getJsonText());
            dataRequest.setName(request.getName());
            dataRequest.setRole(request.getRole());
            dataRequest.setCreatedBy(request.getCreateBy());
            OmDisplayColumnTable newOmDisplayColumnTable = omDisplayColumnTableRepository.save(dataRequest);
            result.setStatus(HttpStatus.CREATED.value());
            result.setData(newOmDisplayColumnTable);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Result<OmDisplayColumnTable>> getOneById(Long id) {
        Result<OmDisplayColumnTable> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmDisplayColumnTable omDisplayColumnTable = this.omDisplayColumnTableRepository.findById(id).orElse(null);
            if (null != omDisplayColumnTable) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omDisplayColumnTable);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
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
    public ResponseEntity<Result<List<OmDisplayColumnTable>>> getAllByRole(String role, String user, String value) {
        Result<List<OmDisplayColumnTable>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<OmDisplayColumnTable> omDisplayColumnTable = omDisplayColumnTableRepository.findAllByRoleAndUserAndValue(role, user, value);
            if (omDisplayColumnTable.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omDisplayColumnTable);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
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
    public ResponseEntity<Result<OmDisplayColumnTable>> getOneByRoleAndName(String role, String name) {
        Result<OmDisplayColumnTable> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmDisplayColumnTable omDisplayColumnTable = this.omDisplayColumnTableRepository.findOneByRoleAndName(role, name);
            if (null != omDisplayColumnTable) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omDisplayColumnTable);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
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
    public ResponseEntity<Result<OmDisplayColumnTable>> deleteById(Long id) {
        Result<OmDisplayColumnTable> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmDisplayColumnTable omDisplayColumnTable = this.omDisplayColumnTableRepository.findById(id).orElse(null);
            if (null != omDisplayColumnTable) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omDisplayColumnTable);
                omDisplayColumnTableRepository.deleteById(id);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                result.setStatus(HttpStatus.NOT_FOUND.value());
                result.setData(null);
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
    public void delete(Long id) {
        this.omDisplayColumnTableRepository.deleteById(id);
    }
}
