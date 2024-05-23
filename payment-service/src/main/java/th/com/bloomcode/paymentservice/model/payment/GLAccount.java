package th.com.bloomcode.paymentservice.model.payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import th.com.bloomcode.paymentservice.model.BaseModel;

@Getter
@Setter
@NoArgsConstructor
public class GLAccount extends BaseModel {
    public static final String TABLE_NAME = "GL_ACCOUNT";

    public static final String COLUMN_NAME_GL_ACCOUNT_ID = "GL_ACCOUNT_ID";
    public static final String COLUMN_NAME_CENTRALGLACCOUNT = "CENTRALGLACCOUNT";
    public static final String COLUMN_NAME_CENTRALGLACCOUNT_DESCRIPTION = "CENTRALGLACCOUNT_DESCRIPTION";
    public static final String COLUMN_NAME_CENTRAL_POSTING = "CENTRAL_POSTING";
    public static final String COLUMN_NAME_AGENCYGLACCOUNT = "AGENCYGLACCOUNT";
    public static final String COLUMN_NAME_AGENCYGLACCOUNT_DESCRIPTION = "AGENCYGLACCOUNT_DESCRIPTION";
    public static final String COLUMN_NAME_AGENCY_POSTING = "AGENCY_POSTING";
    public static final String COLUMN_NAME_DOC_TYPE = "DOC_TYPE";
    public static final String COLUMN_NAME_DR_CR = "DR_CR";
    public static final String COLUMN_NAME_FUND_SOURCE = "FUND_SOURCE";
    public static final String COLUMN_NAME_METHOD = "METHOD";

    private String centralGlAccount;
    private String centralGlAccountDescription;
    private String centralPosting;
    private String agencyGlAccount;
    private String agencyGlAccountDescription;
    private String agencyPosting;
    private String documentType;
    private String drCr;
    private String fundSource;
    private String method;

    public GLAccount(Long id,
            String centralGlAccount,
                    String centralGlAccountDescription,
                    String centralPosting,
                    String agencyGlAccount,
                    String agencyGlAccountDescription,
                    String agencyPosting,
                    String documentType,
                    String drCr,
                    String fundSource,
                    String method) {
        super(id);
        this.centralGlAccount = centralGlAccount;
        this.centralGlAccountDescription = centralGlAccountDescription;
        this.centralPosting = centralPosting;
        this.agencyGlAccount = agencyGlAccount;
        this.agencyGlAccountDescription = agencyGlAccountDescription;
        this.agencyPosting = agencyPosting;
        this.documentType = documentType;
        this.drCr = drCr;
        this.fundSource = fundSource;
        this.method = method;
    }
}
