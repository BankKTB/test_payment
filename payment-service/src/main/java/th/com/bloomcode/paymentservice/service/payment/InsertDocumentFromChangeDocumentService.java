package th.com.bloomcode.paymentservice.service.payment;

import th.com.bloomcode.paymentservice.webservice.model.ZFIDoc;

import java.sql.Timestamp;

public interface InsertDocumentFromChangeDocumentService {

    void insertDocument(ZFIDoc zfiDoc, String username, Timestamp updateDate);
}
