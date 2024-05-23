package th.com.bloomcode.paymentservice.service.payment;

import org.springframework.http.ResponseEntity;
import th.com.bloomcode.paymentservice.model.Raw;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.payment.JULine;
import th.com.bloomcode.paymentservice.model.request.GenerateJuRequest;
import th.com.bloomcode.paymentservice.payment.entity.mapping.JUDocument;

import java.util.List;

public interface JULineService {
    List<JUDocument> selectJuDetail(GenerateJuRequest request);
    ResponseEntity<Result<Raw>> selectJuExportPdf(GenerateJuRequest request);
    ResponseEntity<Result<Raw>> selectJuExportExcel(GenerateJuRequest request);
    ResponseEntity<Result<Raw>> selectJuDetailExportPdf(GenerateJuRequest request);
    ResponseEntity<Result<Raw>> selectJuDetailExportExcel(GenerateJuRequest request);
    JULine save(JULine juLine);
    void save(List<JULine> juLine);
    void deleteAllByJuHeadId(Long juHeadId);
}
