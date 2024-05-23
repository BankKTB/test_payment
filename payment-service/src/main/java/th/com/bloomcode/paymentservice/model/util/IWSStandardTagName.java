package th.com.bloomcode.paymentservice.model.util;

import javax.xml.bind.annotation.XmlTransient;

/*
 * Standard tag name from XML web service
 */
@XmlTransient
public interface IWSStandardTagName {

    /*
     * General
     */
    public static final String Request = "REQUEST";
    public static final String Response = "RESPONSE";
    public static final String Flag = "FLAG";
    public static final String FormID = "FORMID";
    public static final String FiscalYear = "FISCAL_YEAR";
    public static final String WebInfo = "WEBINFO";
    public static final String DocType = "DOC_TYPE";
    public static final String DocumentNo = "DOC_NO";
    public static final String CompanyCode = "COMP_CODE";
    public static final String DatePost = "DATE_POST";
    String ReversalDatePost = "REV_DATE_POST";
    public static final String DateAcct = "DATE_ACCT";
    public static final String DateDoc = "DATE_DOC";
    public static final String Amount = "AMOUNT";
    public static final String Header = "HEADER";
    public static final String Items = "ITEMS";
    public static final String Item = "ITEM";
    public static final String ErrorList = "ERRORS";
    public static final String ErrorEntry = "ERROR";
    public static final String Description = "DESCRIPTION";
    public static final String DocStatus = "DOC_STATUS";
    public static final String Status = "STATUS";
    public static final String RecordID = "RECORD_ID";
    public static final String Reason = "REASON";
    String ReversalReasonPost = "REV_REASON_POST";
    String ReversalInvoiceDocumentNo = "REV_INV_DOC_NO";
    String PaymentMethod = "PAYMENT_METHOD";
    String CostCenter1 = "COST_CENTER1";
    String CostCenter2 = "COST_CENTER2";
    String BudgetDocumentNo = "BR_DOC_NO";
    String Period = "PERIOD";
    String Vendor = "VENDOR";
    String VendorTaxId = "VENDOR_TAXID";
    String Payee = "PAYEE";
    String FiscYear = "FISC_YEAR";
    String Approve = "APPROVE";
    String Success = "SUCCESS";
    String CompanyCodeReversal = "COMP_CODE_REV";
    String AccountDocNoReversal = "ACC_DOC_NO_REV";
    String FiscYearReversal = "FISC_YEAR_REV";
    String DocTypeReversal = "DOC_TYPE_REV";
    String ParkPost = "PARK_POST";
    String FirstHeader = "FIRSTHEADER";
    String ExitsCompletes = "EXITS_COMPLETES";
    String ExitsComplete = "EXITS_COMPLETES";

    /*
     * Code
     */
    // Budget
    public static final String BudgetYear = "BG_YEAR";
    public static final String BudgetAccount = "BG_ACCOUNT";
    public static final String BudgetCode = "BG_CODE";
    public static final String SourceOfFund = "FUND_SOURCE";
    public static final String FundCenter = "FUND_CENTER";
    public static final String BudgetActivity = "BG_ACTIVITY";
    public static final String BudgetArea = "BG_AREA";
    public static final String AreaOfWork = "AREA_OF_WORK";
    public static final String BudgetProject = "BG_PROJRCT";
    public static final String BudgetStrategy = "BG_STRATEGY";
    public static final String PaymentCenter = "PAYMENT_CENTER";
    public static final String CostCenter = "COST_CENTER";
    public static final String CostActivity = "COST_ACTIVITY";
    public static final String BRDocumentNo = "BR_DOC_NO";
    public static final String ConsumeType = "CONSUME_TYPE";
    public static final String ConsumeStatus = "CONSUME_STATUS";

    // BR
    public static final String BRRecordID = "BR_RECORD_ID";
    public static final String BRTrxRecordID = "TH_BGRSERVATIONTRXLINE_ID";
    public static final String CancelReason = "CANCEL_REASON";
    public static final String ItemNo = "ITEM_NO";
    public static final String PostingDate = "POSTING_DATE";
    public static final String ReasonNo = "REASON_NO";
    public static final String OpenAmount = "OPEN_AMOUNT";
    public static final String CarryForwardStatus = "CARRY_FORWARD_STATUS";

    // PO
    public static final String PODocumentNo = "PO_DOC_NO";

    // PO <> MR
    public static final String PO_DOC_NO = "PURCHAS_DOC_NO";
    public static final String PAYMENT_CENTER_CODE = "ZZPMT";
    public static final String BUDGET_AREA = "BGBudgetArea";
    public static final String COMPANY_CODE = "COMP_CODE";
    public static final String DOCUMENT_TYPE = "DOC_TYPE";
    public static final String PARTNER = "PARTNER";
    public static final String DOCUMENT_DATE = "DOC_DATE_DOC";
    public static final String ACCOUNT_DATE = "POST_DATE_DOC";
    public static final String REF_DOC_NO = "REF_DOC_NO";
    public static final String MATERIAL_NO = "NO_MATERIAL";
    public static final String USER_ID = "USERID";
    public static final String OUR_REF = "OUR_REF";
    public static final String ACCOUNT_ASSIGN = "ACCTASSCAT";
    public static final String CONTRACT_START = "VPER_START";
    public static final String PARTNER_NAME = "Bpartner";
    public static final String PROCURE_METHOD = "REF_1";
    public static final String CONTRACT_END = "VPER_END";
    public static final String BILL_OF_LADING = "BILL_OF_LADING";
    public static final String ITEM_NO = "ITEM_NO_PURCHAS_DOC";
    public static final String DELIVERY_DATE = "DELIVERY_DATE";
    public static final String GPSC_CODE = "ZZLOAN";
    public static final String DESCRIPTION = "SHORT_TEXT";
    public static final String QUANTITY = "QUANTITY";
    public static final String UOM = "ORDERPR_UN";
    public static final String STATUS = "ISACTIVE";
    public static final String LINE_NET_AMOUNT = "LineNetAmt";
    public static final String MATERIAL_YEAR = "MATERIAL_DOC_YEAR";
    public static final String DELIVERY_DOC_NO = "DELIVERY_DOC_NO";
    public static final String GL_ACCOUNT = "GL_ACCOUNT";

    public static final String DATE_TYPE = "DATE_TYPE";
    public static final String DATE = "DATE";
    public static final String RECEIPT_NO = "RECEIPT_NO";
    public static final String VENDOR_TAX_ID = "TAX_ID";

    public static final String REVERSE_NO = "REVERSE_NO";
    public static final String CONTRACT_NO = "CONTRACT_NO";
    public static final String CREATE_DATE = "CREATE_DATE";

    // Vendor Header
    public static final String VendorList = "VENDORS";
    public static final String VendorEntry = "VENDOR";
    public static final String VendorCode = "VENDOR_CODE";
    public static final String VendorGroup = "VENDOR_GROUP";
    public static final String TaxID = "TAXID";
    public static final String AddressBuilding = "BUILDING";
    public static final String AddressHouseNo = "HOUSE_NUM";
    public static final String AddressSoi = "SOI";
    public static final String AddressStreet = "STREET";
    public static final String AddressSubDistrict = "SUB_DISTRICT";
    public static final String AddressDistrict = "DISTRICT";
    public static final String AddressPostalCode = "POST_CODE";
    public static final String AddressProvince = "PROVINCE";
    public static final String AddressCountry = "COUNTRY";
    public static final String TelephoneNo = "TEL_NUMBER";
    public static final String TelephoneExt = "TEL_EXTENS";
    public static final String FaxNo = "FAX_NUMBER";
    public static final String FaxExt = "FAX_EXTENS";
    public static final String ApproveStatus = "APPROVE_STATUS";
    public static final String ConfirmStatus = "COMFIRM_STATUS";
    public static final String BlockStatus = "BLOCK_STATUS";

    // Vendor bank account
    public static final String BankCode = "BANK_CODE";
    public static final String BankAccountNo = "ACCOUNT_NO";
    public static final String BankAccountName = "ACCOUNT_NAME";
    public static final String BranchNo = "BRANCH_NO";
    public static final String BankAccountList = "BANK_ACCOUNTS";
    public static final String BankAccountEntry = "ACCOUNT";

    // Withholding tax
    public static final String WithHoldingTaxList = "WTXS";
    public static final String WithHoldingTaxEntry = "TAX";
    public static final String WithHoldingTaxCode = "WTX_CODE";
    public static final String WithHoldingTaxName = "WTX_NAME";
    public static final String WithHoldingTaxType = "WTX_TYPE";

    // alternate payee
    public static final String AlternatePayeeList = "ALTERNATE_PAYEES";
    public static final String AlternatePayeeEntry = "PAYEE";
    public static final String AlternatePayeeCode = "ALTERNAME_PAYEE_CODE";
    public static final String AlternatePayeeName = "ALTERNAME_PAYEE_NAME";

    // vendor accounting
    public static final String AccountingEntry = "ACCOUNTING";
    public static final String PaymentAccount = "PAYMENT";
    public static final String LiabilityAccount = "LIABILITY";
    public static final String ServiceLiabilityAccount = "SERVICE_LIABILITY";

    // FI
    public static final String AccountDocNo = "ACC_DOC_NO";
    public static final String InviceDocNo = "INV_DOC_NO";
    public static final String InviceFiscalYear = "INV_FISCAL_YEAR";
    public static final String ReversalAccountDocNo = "REV_ACC_DOC_NO";
    public static final String ReversalFiscalYear = "REV_FISC_YEAR";
    public static final String Line = "Line";
    public static final String ValueOld = "VALUE_OLD";
    public static final String ValueNew = "VALUE_NEW";
    public static final String UserWebOnline = "USERWEBONLINE";
    public static final String Created = "CREATED";
    public static final String Updated = "UPDATED";
    public static final String FollowingList = "FOLLOWING_DOCS";
    public static final String FollowingEntry = "FOLLOWING_DOC";
    public static final String ChangeList = "CHANGED_LOGS";
    public static final String ChangeEntry = "CHANGED_LOG";
    public static final String Remark = "REMARK";
    public static final String Reference = "REFERENCE";
    String Currency = "CURRENCY";
    String DocumentHeaderText = "DOC_HEADER_TEXT";
    String HeaderDesc = "HEADER_DESC";
    String HeaderRefLog = "HD_REF_LOG";
    String HeaderRef2 = "HD_REF2";
    String PreviousDocumentNo = "PREVIOUS_DOC_NO";
    String AutoDocument = "AUTODOC";
    String AutoGenerate = "AUTOGEN";
    String PostingKey = "POSTING_KEY";
    String AccountType = "ACC_TYPE";
    String DrCr = "DrCr";
    String GLAccount = "GL_ACC";
    String FIArea = "FI_AREA";
    String FundSource = "FUND_SOURCE";
    String Reference3 = "REFERENCE3";
    String Assignment = "ASSIGNMENT";
    String BudgetLine = "BR_LINE";
    String BankBook = "BANK_BOOK";
    String GPSC = "GPSC";
    String SubAccount = "SUB_ACC";
    String SubAccountOwner = "SUB_ACC_OWNER";
    String DepositAccountOwner = "DEPOSIT_ACC_OWNER";
    String DepositAccount = "DEPOSIT_ACC";
    String LineItemText = "LINE_ITEM_TEXT";
    String LineDesc = "LINE_DESC";
    String PaymentTerm = "PAYMENT_TERM";
    String WTXType = "WTX_TYPE";
    String WTXCode = "WTX_CODE";
    String WTXBase = "WTX_BASE";
    String WTXAmount = "WTX_AMOUNT";
    String WTXTypeP = "WTX_TYPE_P";
    String WTXCodeP = "WTX_CODE_P";
    String WTXBaseP = "WTX_BASE_P";
    String WTXAmountP = "WTX_AMOUNT_P";
    String BankAccNo = "BANK_ACC_NO";
    String BankBranchNo = "BANK_BRANCH_NO";
    String TradingPartner = "TRADING_PARTNER";
    String TradingPartnerBusArea = "TRADING_PARTNER_BUS_AREA";
    String GPSCGroup = "GPSC_GROUP";
    String SpecialGL = "SPECIAL_GL";
    String CreditMemoDoocumentNo = "CREDIT_MEMO_DOC_NO";
    String CreditMemoFiscalYear = "CREDIT_MEMO_FISCAL_YEAR";
    String DateBaseline = "DATE_BASELINE";
    String DateValue = "DATE_VALUE";
    String AssetNo = "ASSET_NO";
    String AssetSubNo = "ASSET_SUB_NO";
    String QTY = "QTY";
    String UOMISO = "UOM_ISO";
    String Reference1 = "REFERENCE1";
    String Reference2 = "REFERENCE2";
    String POLine = "PO_LINE";
    String Income = "INCOME";
    String PaymentBlock = "PAYMENT_BLOCK";
    String PaymentReference = "PAYMENT_REF";
    String IsWTX = "IS_WTX";
    String LineNo = "LINE";

    /*
     * Name
     */
    // Budget
    public static final String BudgetAccountName = "BG_ACCOUNT_NAME";
    public static final String BudgetCodeName = "BG_CODE_NAME";
    public static final String SourceOfFundName = "FUND_SOURCE_NAME";
    public static final String FundCenterName = "FUND_CENTER_NAME";
    public static final String BudgetActivityName = "BG_ACTIVITY_NAME";
    public static final String BudgetAreaName = "BG_AREA_NAME";
    public static final String AreaOfWorkName = "AREA_OF_WORK_NAME";
    public static final String BudgetProjectName = "BG_PROJRCT_NAME";
    public static final String BudgetStrategyName = "BG_STRATEGY_NAME";
    public static final String PaymentCenterName = "PAYMENT_CENTER_NAME";
    public static final String CostCenterName = "COST_CENTER_NAME";
    public static final String CostActivityName = "COST_ACTIVITY_NAME";

    // General
    public static final String CompanyCodeName = "COMP_CODE_NAME";
    public static final String DocStatusName = "DOC_STATUS_NAME";

    // Vendor
    public static final String VendorName = "VENDOR_NAME";
    public static final String VendorGroupName = "VENDOR_GROUP_NAME";
    public static final String BankName = "BANK_NAME";
    public static final String BranchName = "BRANCH_NAME";
}
