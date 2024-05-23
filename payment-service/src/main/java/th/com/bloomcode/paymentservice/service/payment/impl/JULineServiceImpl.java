package th.com.bloomcode.paymentservice.service.payment.impl;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.model.response.JuHeadDocumentResponse;
import th.com.bloomcode.paymentservice.model.response.JuLineDocumentResponse;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocument;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocumentExport;
import th.com.bloomcode.paymentservice.repository.payment.JULineRepository;
import th.com.bloomcode.paymentservice.service.payment.JULineService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.service.common.ExportJasperService;
import org.apache.commons.collections4.map.HashedMap;
import th.com.bloomcode.paymentservice.model.Raw;
import th.com.bloomcode.paymentservice.model.Result;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.security.core.context.SecurityContextHolder;
import th.com.bloomcode.paymentservice.util.Util;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JULineServiceImpl implements JULineService {
    private final JdbcTemplate jdbcTemplate;
    private final JULineRepository juLineRepository;
    private final ExportJasperService exportJasperService;

    @Autowired
    public JULineServiceImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, JULineRepository juLineRepository, ExportJasperService exportJasperService) {
        this.jdbcTemplate = jdbcTemplate;
        this.juLineRepository = juLineRepository;
        this.exportJasperService = exportJasperService;
    }

    @Override
    public List<JUDocument> selectJuDetail(GenerateJuRequest request) {
        return juLineRepository.selectJuDetail(request);
    }

    @Override
    public ResponseEntity<Result<Raw>> selectJuExportPdf(GenerateJuRequest request) {
        Result<Raw> result = new Result<>();
        try {
            InputStream jasperFile = getClass().getResourceAsStream("/jasper/JU.jrxml");
//            List<JUDocumentExport> dataList = juLineRepository.selectJuDetailExport(request);
            List<JUDocument> dataList = juLineRepository.selectJuDetail(request);
            log.info("selectJuExportPdf : {}", dataList);
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(dataList);
            Map<String, Object> params = new HashedMap();
            result.setFile(exportJasperService.exportPDFfNrp(params, jasperFile, itemsJRBean));
            result.setStatus(HttpStatus.OK.value());
            result.setError(HttpStatus.OK.getReasonPhrase());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            result.setMessage(e.toString());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<Raw>> selectJuExportExcel(GenerateJuRequest request) {
        Result<Raw> result = new Result<>();
        try {
            InputStream jasperFile = getClass().getResourceAsStream("/jasper/JU_EX.jrxml");
//            List<JUDocumentExport> dataList = juLineRepository.selectJuDetailExport(request);
            List<JUDocument> dataList = juLineRepository.selectJuDetail(request);
            log.info("selectJuExportExcel : {}", dataList);
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(dataList);
            Map<String, Object> params = new HashedMap();
            result.setFile(exportJasperService.exportExcelNrp(params, jasperFile, itemsJRBean));
            result.setStatus(HttpStatus.OK.value());
            result.setError(HttpStatus.OK.getReasonPhrase());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            result.setMessage(e.toString());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<Raw>> selectJuDetailExportPdf(GenerateJuRequest request) {
        Result<Raw> result = new Result<>();
        try {
            InputStream jasperFile = getClass().getResourceAsStream("/jasper/JU_DETAIL_V2.jrxml");
            List<JUDocumentExport> dataList = juLineRepository.selectJuDetailExport(request);
            log.info("selectJuDetailExportPdf : {}", dataList);
            Map<String, Object> params = new HashedMap();
            // header
            params.put("loaderUser", "GENJUDOC01");
            params.put("userName", request.getWebInfo().getUserWebOnline());
            params.put("docStatus", "เอกสารผ่านรายการ");
            params.put("source", "Web Loader");
            // summary
            List<JuHeadDocumentResponse> juHeadDocumentResponses = new ArrayList<>();
            Map<Object, List<JUDocumentExport>> groupByJuHeadId = dataList.stream()
                    .collect(Collectors.groupingBy(data -> data.getJuHeadId()));
            groupByJuHeadId.forEach((key, value) -> {
                List<JuLineDocumentResponse> juLineDocumentResponses = new ArrayList<>();
                JuHeadDocumentResponse juHeadDocumentResponse = new JuHeadDocumentResponse();
                BeanUtils.copyProperties(value.get(0), juHeadDocumentResponse);
                List<JUDocumentExport> item = value;
                for (JUDocumentExport juDocumentExport : item) {
                    JuLineDocumentResponse juLineDocumentResponse = new JuLineDocumentResponse();
                    BeanUtils.copyProperties(juDocumentExport, juLineDocumentResponse);
                    juLineDocumentResponses.add(juLineDocumentResponse);
                }
                juHeadDocumentResponse.setJuHeadId(value.get(0).getId());
                juHeadDocumentResponse.setList(juLineDocumentResponses);
                juHeadDocumentResponses.add(juHeadDocumentResponse);
            });
            int sumFile = (int) juHeadDocumentResponses.stream().mapToLong(aa -> aa.getList().size()).sum();
            BigDecimal sumAmountDr = dataList.stream().map(e -> Util.getBigDecimal(e.getAmountDr())).reduce(BigDecimal.valueOf(0), (x, y) -> x.add(y));
            params.put("summaryAmount", sumAmountDr);
            params.put("summaryDocument", groupByJuHeadId.size());
            params.put("summaryFile", sumFile);

            // create credit
//            if (dataList.size() >= 1) {
//                JUDocumentExport creditDate = new JUDocumentExport();
//                BeanUtils.copyProperties(dataList.get(0),creditDate);
//                dataList.add(creditDate);
//                dataList.get(dataList.size() - 1).setCredit(true);
//                dataList.get(dataList.size() - 1).setRefLine(dataList.size());
//            }
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(dataList);
            result.setFile(exportJasperService.exportPDFfNrp(params, jasperFile, itemsJRBean));
            result.setStatus(HttpStatus.OK.value());
            result.setError(HttpStatus.OK.getReasonPhrase());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            result.setMessage(e.toString());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Result<Raw>> selectJuDetailExportExcel(GenerateJuRequest request) {
        Result<Raw> result = new Result<>();
        try {
            InputStream jasperFile = getClass().getResourceAsStream("/jasper/JU_DETAIL_EX_V2.jrxml");
            List<JUDocumentExport> dataList = juLineRepository.selectJuDetailExport(request);
            log.info("selectJuDetailExportExcel : {}", dataList);
            Map<String, Object> params = new HashedMap();
            // header
            params.put("loaderUser", "GENJUDOC01");
            params.put("userName", request.getWebInfo().getUserWebOnline());
            params.put("docStatus", "เอกสารผ่านรายการ");
            params.put("source", "Web Loader");
            // summary
            List<JuHeadDocumentResponse> juHeadDocumentResponses = new ArrayList<>();
            Map<Object, List<JUDocumentExport>> groupByJuHeadId = dataList.stream()
                    .collect(Collectors.groupingBy(data -> data.getJuHeadId()));
            groupByJuHeadId.forEach((key, value) -> {
                List<JuLineDocumentResponse> juLineDocumentResponses = new ArrayList<>();
                JuHeadDocumentResponse juHeadDocumentResponse = new JuHeadDocumentResponse();
                BeanUtils.copyProperties(value.get(0), juHeadDocumentResponse);
                List<JUDocumentExport> item = value;
                for (JUDocumentExport juDocumentExport : item) {
                    JuLineDocumentResponse juLineDocumentResponse = new JuLineDocumentResponse();
                    BeanUtils.copyProperties(juDocumentExport, juLineDocumentResponse);
                    juLineDocumentResponses.add(juLineDocumentResponse);
                }
                juHeadDocumentResponse.setJuHeadId(value.get(0).getId());
                juHeadDocumentResponse.setList(juLineDocumentResponses);
                juHeadDocumentResponses.add(juHeadDocumentResponse);
            });
            int sumFile = (int) juHeadDocumentResponses.stream().mapToLong(aa -> aa.getList().size()).sum();
            BigDecimal sumAmountDr = dataList.stream().map(e -> Util.getBigDecimal(e.getAmountDr())).reduce(BigDecimal.valueOf(0), (x, y) -> x.add(y));
            params.put("summaryAmount", sumAmountDr);
            params.put("summaryDocument", groupByJuHeadId.size());
            params.put("summaryFile", sumFile);

            // create credit
//            if (dataList.size() >= 1) {
//                JUDocumentExport creditDate = new JUDocumentExport();
//                BeanUtils.copyProperties(dataList.get(0),creditDate);
//                dataList.add(creditDate);
//                dataList.get(dataList.size() - 1).setCredit(true);
//                dataList.get(dataList.size() - 1).setRefLine(dataList.size());
//            }
            JRBeanCollectionDataSource itemsJRBean = new JRBeanCollectionDataSource(dataList);
            result.setFile(exportJasperService.exportExcelNrp(params, jasperFile, itemsJRBean));
            result.setStatus(HttpStatus.OK.value());
            result.setError(HttpStatus.OK.getReasonPhrase());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            result.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            result.setMessage(e.toString());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public JULine save(JULine juLine) {
        log.info("==== Save juLine : {}", juLine);
        if (null == juLine.getId() || 0 == juLine.getId()) {
            log.info("==== Save getJuHeadId : {}", juLine.getJuHeadId());
            juLine.setId(SqlUtil.getNextSeries(jdbcTemplate, JULine.TABLE_NAME + JULine.SEQ, null));
            juLine.setJuHeadId(juLine.getJuHeadId());
            juLineRepository.save(juLine);
        }
        return juLine;
    }

    @Override
    public void save(List<JULine> juLine) {
        log.info("==== Save All juLine : {}", juLine);
        for (JULine item : juLine) {
            if (null == item.getId() || 0 == item.getId()) {
                item.setId(SqlUtil.getNextSeries(jdbcTemplate, JULine.TABLE_NAME + JULine.SEQ, null));
                juLineRepository.save(item);
            }
        }
    }

    @Override
    public void deleteAllByJuHeadId(Long juHeadId) {
        log.info("==== deleteAllByJuHeadId : {}", juHeadId);
        juLineRepository.deleteAllByJUHeadId(juHeadId);
    }
}
