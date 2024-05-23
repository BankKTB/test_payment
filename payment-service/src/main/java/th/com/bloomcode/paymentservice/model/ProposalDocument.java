package th.com.bloomcode.paymentservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;

import java.math.BigDecimal;

@Data
public class ProposalDocument {

    private Long paymentInformationId;

    private BigDecimal amount;

    private BigDecimal amountPaid;

    private String originalCompanyCode;

    private String currency;

    private String originalFiscalYear;

    private String payingBankCode;

    private String payingHouseBank;

    private String paymentMethod;

    private Long paymentId;

    private Long paymentAliasId;

    private String vendor;

    private String originalDocumentNo;

    private String pmGroupNo;

    private String pmGroupDoc;

    private boolean proposalBlock;

    private Long id;

    private WSWebInfo webInfo;

}