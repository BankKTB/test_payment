package th.com.bloomcode.paymentservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.model.common.BaseDateRange;
import th.com.bloomcode.paymentservice.model.common.BaseRange;

import java.util.List;

@Data
public class ReturnSearchProposalLog {
    private boolean fileStatusComplete;
    private boolean fileStatusNotSet;
    private boolean fileStatusReturn;
    private boolean fileTypeGiro;
    private boolean fileTypeInHouse;
    private boolean fileTypeSmart;
    private boolean fileTypeSwift;
    private List<BaseRange> generateFileName;
    private List<BaseDateRange> paymentDate;
    private List<BaseDateRange> transferDate;
    private List<BaseRange> accountNoFrom;
    private List<BaseRange> accountNoTo;
    private List<BaseRange> fileName;
    private List<BaseRange> refLine;
    private List<BaseRange> refRunning;
    private List<BaseRange> vendor;
    private List<BaseRange> vendorFrom;
    private List<BaseRange> vendorTo;
    private List<BaseRange> bankKey;
    private List<BaseRange> vendorBankAccount;
    private List<BaseRange> transferLevel;
    private List<BaseRange> payAccount;
    private List<BaseRange> paymentCompCode;
    private List<BaseRange> paymentDocument;
    private List<BaseRange> paymentFiscalYear;
    private List<BaseRange> originalDocNo;
    private List<BaseRange> originalCompCode;
    private List<BaseRange> originalFiscalYear;
    private List<BaseRange> paymentName;
    private List<BaseRange> payingCompCode;

}
