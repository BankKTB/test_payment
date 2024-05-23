package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.List;

@Data
public class JuLineDocumentResponse {


    private Long refRunning;
    private String payAccount;
    private int refLine;
    private String accountNoFrom;
    private String accountNoTo;
    private String fileType;
    private String fileName;
    private BigDecimal amountDr;
    private String glAccountDr;
    private String glAccountDrName;
    private String assignment;
    private String bgCode;
    private String bgName;
    private String costCenter;
    private String costCenterName;
    private String fiArea;
    private String postingKey;

    private String fundSource;
    private String fundSourceName;

    private String brDocNo;


    private BigDecimal WtxAmount;

    private BigDecimal WtxBase;
    private BigDecimal WtxAmountP;
    private BigDecimal WtxBaseP;
    private String mainActivity;
    private String mainActivityName;

    private String costActivity;
    private String costActivityName;


    private String subAccount;
    private String subAccountOwner;
    private String depositAccount;
    private String depositAccountOwner;
}