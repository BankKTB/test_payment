package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.OmDisplayColumnTable;
import th.com.bloomcode.paymentservice.model.payment.OmSearchCriteria;
import th.com.bloomcode.paymentservice.model.request.OmSearchCriteriaRequest;
import th.com.bloomcode.paymentservice.repository.payment.OmSearchCriteriaRepository;
import th.com.bloomcode.paymentservice.service.payment.OmSearchCriteriaService;
import th.com.bloomcode.paymentservice.util.SqlUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class OmSearchCriteriaServiceImpl implements OmSearchCriteriaService {
    private final OmSearchCriteriaRepository omSearchCriteriaRepository;
    private final JdbcTemplate jdbcTemplate;

    public OmSearchCriteriaServiceImpl(OmSearchCriteriaRepository omSearchCriteriaRepository,  @Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.omSearchCriteriaRepository = omSearchCriteriaRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ResponseEntity<Result<OmSearchCriteria>> save(OmSearchCriteriaRequest request) {
        Result<OmSearchCriteria> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmSearchCriteria omSearchCriteria = this.omSearchCriteriaRepository
                    .findOneByNameAndRole(request.getName(), request.getRole());
            if (null == omSearchCriteria) {
                OmSearchCriteria dataRequest = new OmSearchCriteria();
                dataRequest.setId(SqlUtil.getNextSeries(jdbcTemplate, OmSearchCriteria.TABLE_NAME + OmSearchCriteria.SEQ, null));
                dataRequest.setIsUserOnly(request.getUserOnly());
                dataRequest.setJsonText(request.getJsonText());
                dataRequest.setName(request.getName());
                dataRequest.setRole(request.getRole());
                dataRequest.setCreatedBy(request.getCreateBy());
                dataRequest.setCreated(new Timestamp(new Date().getTime()));
                OmSearchCriteria newOmSearchCriteria = omSearchCriteriaRepository.save(dataRequest);
                result.setStatus(HttpStatus.CREATED.value());
                result.setData(newOmSearchCriteria);
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
    public ResponseEntity<Result<OmSearchCriteria>> update(OmSearchCriteriaRequest request) {
        Result<OmSearchCriteria> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmSearchCriteria omSearchCriteria = this.omSearchCriteriaRepository.findById(request.getId()).orElse(null);
            OmSearchCriteria dataRequest = new OmSearchCriteria();
            if (null != omSearchCriteria) {
                dataRequest.setId(request.getId());
                dataRequest.setUpdated(new Timestamp(new Date().getTime()));
            } else {
                dataRequest.setId(SqlUtil.getNextSeries(jdbcTemplate, OmDisplayColumnTable.TABLE_NAME + OmDisplayColumnTable.SEQ, null));
                dataRequest.setCreated(new Timestamp(new Date().getTime()));
            }
            dataRequest.setIsUserOnly(request.getUserOnly());
            dataRequest.setJsonText(request.getJsonText());
            dataRequest.setName(request.getName());
            dataRequest.setRole(request.getRole());
            dataRequest.setCreatedBy(request.getCreateBy());
            OmSearchCriteria newOmSearchCriteria = omSearchCriteriaRepository.save(dataRequest);
            result.setStatus(HttpStatus.CREATED.value());
            result.setData(newOmSearchCriteria);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<Result<OmSearchCriteria>> getOneById(Long id) {
        Result<OmSearchCriteria> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmSearchCriteria omSearchCriteria = this.omSearchCriteriaRepository.findById(id).orElse(null);
            if (null != omSearchCriteria) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omSearchCriteria);
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
    public ResponseEntity<Result<List<OmSearchCriteria>>> getAllByRole(String role, String user, String value) {
        Result<List<OmSearchCriteria>> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            List<OmSearchCriteria> omSearchCriteria = omSearchCriteriaRepository.findAllByRoleAndUserAndValue(role, user, value);
            if (omSearchCriteria.size() > 0) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omSearchCriteria);
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
    public ResponseEntity<Result<OmSearchCriteria>> deleteById(Long id) {
        Result<OmSearchCriteria> result = new Result<>();
        result.setTimestamp(new Date());
        try {
            OmSearchCriteria omSearchCriteria = this.omSearchCriteriaRepository.findById(id).orElse(null);
            if (omSearchCriteria != null) {
                result.setStatus(HttpStatus.OK.value());
                result.setData(omSearchCriteria);
                omSearchCriteriaRepository.deleteById(id);
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
        this.omSearchCriteriaRepository.deleteById(id);
    }

}
