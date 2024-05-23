package th.com.bloomcode.paymentservice.webservice.model;

import javax.xml.bind.annotation.XmlTransient;

/*
 * Standard tag name from XML web service
 */
@XmlTransient
public class StandardXMLTagName {
	private StandardXMLTagName() {
	}
	
	/*
	 * General
	 */
	public static final String REQUEST = "REQUEST";
	public static final String RESPONSE = "RESPONSE";
	public static final String FLAG = "FLAG";
	public static final String FORM_ID = "FORMID";
	public static final String FISCAL_YEAR = "FISCAL_YEAR";
	public static final String WEB_INFO = "WEBINFO";
	public static final String DOCUMENT_TYPE = "DOC_TYPE";
	public static final String DOCUMENT_NO = "DOC_NO";
	public static final String COMPANY_CODE = "COMP_CODE";
	public static final String DATE_ACCT = "DATE_ACCT";
	public static final String DATE_DOC = "DATE_DOC";
	public static final String AMOUNT = "AMOUNT";
	public static final String HEADER_LIST = "HEADERS";
	public static final String HEADER = "HEADER";
	public static final String ERROR_LIST = "ERRORS";
	public static final String ERROR_ENTRY = "ERROR";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String DOCUMENT_STATUS = "DOC_STATUS";
	public static final String STATUS = "STATUS";
	public static final String RECORD_ID = "RECORD_ID";
	public static final String REASON = "REASON";
	public static final String CURRENCY = "CURRENCY";
	public static final String USER_WEBONLINE = "USERWEBONLINE";
	public static final String PERIOD_NO = "PERIOD_NO";
	public static final String IPADDRESS = "IPADDRESS";
	public static final String REVERSE_STATUS = "REVERSE_STATUS";
	public static final String REVERSE_DOCUMENT_NO = "REVERSE_DOC_NO";
	public static final String REFERENCE_NO = "REFERENCE_NO";
	public static final String DATE_SAP = "DATE_SAP";
	public static final String UUID = "UUID";
	public static final String MASSIVE = "MASSIVE";
	public static final String FILENAME = "FILENAME";
	public static final String START_PROCESSING = "START_PROCESSING";
	public static final String FINISH_PROCESSING = "FINISH_PROCESSING";
	public static final String CONVERSION = "CONVERSION";
	public static final String LOG_NO = "LOG_NO";
	public static final String OPTION = "OPTION";
	public static final String MENU = "MENU";
	
	/*
	 * Code
	 */
	// Budget
	public static final String BUDGET_YEAR = "BG_YEAR";
	public static final String BUDGET_ACCOUNT = "BG_ACCOUNT";
	public static final String BUDGET_CODE = "BG_CODE";
	public static final String SOURCE_OF_FUND = "FUND_SOURCE";
	public static final String FUND_CENTER = "FUND_CENTER";
	public static final String BUDGET_ACTIVITY = "BG_ACTIVITY";
	public static final String BUDGET_AREA = "BG_AREA";
	public static final String AREA_OF_WORK = "AREA_OF_WORK";
	public static final String BUDGET_PROJECT = "BG_PROJRCT";
	public static final String BUDGET_STRATEGY = "BG_STRATEGY";
	public static final String PAYMENT_CENTER = "PAYMENT_CENTER";
	public static final String COST_CENTER = "COST_CENTER";
	public static final String COST_ACTIVITY = "COST_ACTIVITY";
	public static final String BR_DOCUMENT_NO = "BR_DOC_NO";
	public static final String BR_LINE = "BR_LINE";
	public static final String CONSUME_TYPE = "CONSUME_TYPE";
	public static final String CONSUME_STATUS = "CONSUME_STATUS";
	public static final String BUDGET_TYPE = "BUDGET_TYPE";
	public static final String LINE_TYPE = "LINE_TYPE";
	public static final String SUB_WITHDRAW_TYPE = "SUB_WITHDRAW_TYPE";
	public static final String RESPONSIBLE = "RESPONSIBLE";
	public static final String INTERNAL_BUDGET = "INTERNAL_BUDGET";
	public static final String PRINT_REFERENCE_ATTACHMENT = "PRINT_REFERENCE_ATTACHMENT";
	
	// BR
	public static final String BR_RECORD_ID = "BR_RECORD_ID";
	public static final String BRTRX_RECORD_ID = "TH_BGRSERVATIONTRXLINE_ID";
	public static final String CANCEL_REASON = "CANCEL_REASON";
	public static final String ITEM_NO = "ITEM_NO";
	public static final String POSTING_DATE = "POSTING_DATE";
	public static final String REASON_NO = "REASON_NO";
	public static final String REASON_NAME = "REASON_NAME";
	public static final String OPEN_AMOUNT = "OPEN_AMOUNT";
	public static final String CARRY_FORWARD_STATUS = "CARRY_FORWARD_STATUS";
	
	// PO
	public static final String PO_DOCUMENT_NO = "PO_DOC_NO";
	public static final String PO_LINE = "PO_LINE";
	public static final String PO_AMOUNT = "PO_AMOUNT";
	public static final String PO_QUANTITY = "PO_QTY";
	public static final String PO_UOM = "PO_UOM";
	
	// MR
	public static final String MR_FISCAL_YEAR = "MR_FISCAL_YEAR";
	public static final String MR_DOCUMENT_NO = "MR_DOC_NO";
	public static final String MR_LINE = "MR_LINE";
	
	// PO <> MR
	public static final String PO_DOC_NO = "PURCHAS_DOC_NO";
	public static final String PAYMENT_CENTER_CODE = "ZZPMT";
	public static final String PO_BUDGET_AREA = "BGBudgetArea";
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
	public static final String PO_ITEM_NO = "ITEM_NO_PURCHAS_DOC";
	public static final String DELIVERY_DATE = "DELIVERY_DATE";
	public static final String GPSC_CODE = "ZZLOAN";
	public static final String SHORT_TEXT = "SHORT_TEXT";
	public static final String QUANTITY = "QUANTITY";
	public static final String UOM = "ORDERPR_UN";
	public static final String ACTIVE_STATUS = "ISACTIVE";
	public static final String LINE_NET_AMOUNT = "LineNetAmt";
	public static final String MATERIAL_YEAR = "MATERIAL_DOC_YEAR";
	public static final String DELIVERY_DOC_NO = "DELIVERY_DOC_NO";
	public static final String GL_ACCOUNT = "GL_ACCOUNT";
	public static final String GL_ACCOUNT2 = "GL_ACCOUNT2";
	public static final String GL_ACCOUNT_PAYMENT = "GL_ACCOUNT_PAYMENT";
	public static final String PO_LINE_NO = "PURCHAS_LINE_NO";
	public static final String HISTORY_NAME = "HISTORY_NAME";
	public static final String PO_REFERENCE = "PO_REFERENCE";
	public static final String CANCEL_BY = "CANCEL_BY";
	public static final String ACCEPTANCE_ID = "ACCEPTANCE_ID";
	public static final String ACCEPTANCE_DATE = "ACCEPTANCE_DATE";
	
	public static final String DATE_TYPE = "DATE_TYPE";
	public static final String DATE = "DATE";
	public static final String RECEIPT_NO = "RECEIPT_NO";
	public static final String VENDOR_TAX_ID = "TAX_ID";
	
	public static final String REVERSE_NO = "REVERSE_NO";
	public static final String CONTRACT_NO = "CONTRACT_NO";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	
	// Vendor Header
	public static final String VENDOR_LIST = "VENDORS";
	public static final String VENDOR_ENTRY = "VENDOR";
	public static final String VENDOR_CODE = "VENDOR_CODE";
	public static final String VENDOR_GROUP = "VENDOR_GROUP";
	public static final String TAXID = "TAXID";
	public static final String ADDRESS_BUILDING = "BUILDING";
	public static final String ADDRESS_HOUSE_NO = "HOUSE_NUM";
	public static final String ADDRESS_SOI = "SOI";
	public static final String ADDRESS_STREET = "STREET";
	public static final String ADDRESS_SUB_DISTRICT = "SUB_DISTRICT";
	public static final String ADDRESS_DISTRICT = "DISTRICT";
	public static final String ADDRESS_POSTAL_CODE = "POST_CODE";
	public static final String ADDRESS_PROVINCE = "PROVINCE";
	public static final String ADDRESS_COUNTRY = "COUNTRY";
	public static final String TELEPHONE_NO = "TEL_NUMBER";
	public static final String TELEPHONE_EXT = "TEL_EXTENS";
	public static final String FAX_NO = "FAX_NUMBER";
	public static final String FAX_EXT = "FAX_EXTENS";
	public static final String APPROVE_STATUS = "APPROVE_STATUS";
	public static final String CONFIRM_STATUS = "CONFIRM_STATUS";
	public static final String BLOCK_STATUS = "BLOCK_STATUS";
	public static final String BLOCK_REASON = "BLOCK_REASON";
	public static final String CLOSE_STATUS = "CLOSE_STATUS";
	public static final String EDIT_STATUS = "EDIT_STATUS";
	
	// access control list
	public static final String ACCESS_CONTROL_LIST = "ACLS";
	public static final String ACCESS_CONTROL_ENTRY = "ACL";
	
	// Vendor bank account
	public static final String BANK_CODE = "BANK_CODE";
	public static final String BANK_ACCOUNT_NO = "ACCOUNT_NO";
	public static final String BANK_ACCOUNT_NAME = "ACCOUNT_NAME";
	public static final String BANK_BRANCH_NO = "BRANCH_NO";
	public static final String BANK_ACCOUNT_LIST = "BANK_ACCOUNTS";
	public static final String BANK_ACCOUNT_ENTRY = "ACCOUNT";
	
	// Withholding tax
	public static final String WITHHOLDING_TAX_LIST = "WTXS";
	public static final String WITHHOLDING_TAX_ENTRY = "TAX";
	public static final String WITHHOLDING_TAX_CODE = "WTX_CODE";
	public static final String WITHHOLDING_TAX_NAME = "WTX_NAME";
	public static final String WITHHOLDING_TAX_TYPE = "WTX_TYPE";
	public static final String WITHHOLDING_TAX_BASE = "WTX_BASE";
	public static final String WITHHOLDING_TAX_AMOUNT = "WTX_AMOUNT";
	public static final String WITHHOLDING_TAX_TYPE_P = "WTX_TYPE_P";
	public static final String WITHHOLDING_TAX_CODE_P = "WTX_CODE_P";
	public static final String WITHHOLDING_TAX_BASE_P = "WTX_BASE_P";
	public static final String WITHHOLDING_TAX_AMOUNT_P = "WTX_AMOUNT_P";
	
	// alternate payee
	public static final String ALTERNATE_PAYEE_LIST = "ALTERNATE_PAYEES";
	public static final String ALTERNATE_PAYEE_ENTRY = "PAYEE";
	public static final String ALTERNATE_PAYEE_CODE = "ALTERNATE_PAYEE_CODE";
	public static final String ALTERNATE_PAYEE_NAME = "ALTERNATE_PAYEE_NAME";
	
	// vendor accounting
	public static final String ACCOUNTING_LIST = "ACCOUNTINGS";
	public static final String ACCOUNTING_ENTRY = "ACCOUNTING";
	public static final String PAYMENT_ACCOUNT = "PAYMENT";
	public static final String LIABILITY_ACCOUNT = "LIABILITY";
	public static final String SERVICE_LIABILITY_ACCOUNT = "SERVICE_LIABILITY";
	
	// invoice
	public static final String INVOICE_COMPANY_CODE = "INV_COMP_CODE";
	public static final String INVOICE_DOC_NO = "INV_DOC_NO";
	public static final String INVOICE_LINE = "INV_LINE";
	public static final String INVOICE_FISCAL_YEAR = "INV_FISCAL_YEAR";
	public static final String REVERSAL_INVOICE_DOC_NO = "REV_INV_DOC_NO";
	public static final String REF_INVOICE_DOC_NO = "REF_INV_DOC_NO";
	public static final String REF_INVOICE_LINE = "REF_INV_LINE";
	public static final String REF_INVOICE_FISCAL_YEAR = "REF_INV_FISCAL_YEAR";
	
	// Credit memo
	public static final String CREDIT_MEMO_DOC_NO = "CN_DOC_NO";
	public static final String CREDIT_MEMO_FISCAL_YEAR = "CN_FISCAL_YEAR";
	
	// FI
	public static final String ACCOUNT_DOCUMENT_NO = "ACC_DOC_NO";
	public static final String ACCOUNT_DOCUMENT_NO_2 = "ACC_DOC_NO_2";
	public static final String REVERSAL_ACCOUNT_DOCUMENT_NO = "REV_ACC_DOC_NO";
	public static final String REVERSAL_FISCAL_YEAR = "REV_FISCAL_YEAR";
	public static final String REVERSAL_DOCUMENT_TYPE = "REV_DOC_TYPE";
	public static final String REVERSAL_DATE_ACCT = "REV_DATE_ACCT";
	public static final String REVERSAL_REASON = "REV_REASON";
	public static final String REVERSAL_PERIOD = "REV_PERIOD";
	public static final String HEADER_DESCRIPTION = "HEADER_DESC";
	public static final String HEADER_REFERENCE = "HEADER_REFERENCE";
	public static final String DOC_HEADER_TEXT = "DOC_HEADER_TEXT";
	public static final String HEADER_REFERENCE2 = "HEADER_REFERENCE2";
	public static final String ORIGINAL_DOC = "ORIGINAL_DOC";
	public static final String REF_INTER_DOC_NO = "REF_INTER_DOC_NO";
	public static final String REF_INTER_SAP_DOC_NO = "REF_INTER_SAP_DOC_NO";
	public static final String REF_INTER_COMP_CODE = "REF_INTER_COMP_CODE";
	public static final String REF_AUTOGEN = "REF_AUTOGEN";
	public static final String REFERENCE_SAP = "REF_SAP";
	public static final String SAP_DOCUMENT_NO = "SAP_DOC_NO";
	public static final String REFERENCE_KEY_SAP = "REFERENCE_KEY_SAP";
	public static final String INTER_BA_DOCUMENT_NO = "INTER_BA_DOC_NO";
	public static final String LINE = "LINE";
	public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
	public static final String PAYEE_TAXID = "PAYEE_TAXID";
	public static final String VENDOR_TAXID = "VENDOR_TAXID";
	public static final String VENDOR = "VENDOR";
	public static final String PAYEE = "PAYEE";
	public static final String BANK_ACC_NO = "BANK_ACC_NO";
	public static final String BANK_ACC_NO_ACTIVE = "BANK_ACC_NO_ACTIVE";
	public static final String DOWN_PAYMENT = "DOWN_PAYMENT";
	public static final String COST_CENTER1 = "COST_CENTER1";
	public static final String COST_CENTER2 = "COST_CENTER2";
	public static final String PERIOD = "PERIOD";
	public static final String FI_AREA = "FI_AREA";
	public static final String SAVE_DATE_DOC = "SAVE_DATE_DOC";
	public static final String POSTING_KEY = "POSTING_KEY";
	public static final String ACC_TYPE = "ACC_TYPE";
	public static final String ACC_TYPE_ASSET = "ACC_TYPE_ASSET";
	public static final String ACC_TYPE_GL_ACCOUNT = "ACC_TYPE_GL_ACCOUNT";
	public static final String ACC_TYPE_MATERIAL = "ACC_TYPE_MATERIAL";
	public static final String ACC_TYPE_VENDOR = "ACC_TYPE_VENDOR";
	public static final String DR_CR = "DR_CR";
	public static final String UOMFI = "UOM";
	public static final String REFERENCE1 = "REFERENCE1";
	public static final String REFERENCE2 = "REFERENCE2";
	public static final String REFERENCE3 = "REFERENCE3";
	public static final String ASSIGNMENT = "ASSIGNMENT";
	public static final String ASSIGNMENT_SPECIAL_GL = "ASSIGNMENT_SPECIAL_GL";
	public static final String BANK_BOOK = "BANK_BOOK";
	public static final String SUB_BOOK = "SUB_BOOK";
	public static final String GPSC = "GPSC";
	public static final String SUB_ACCOUNT_TYPE = "SUB_ACCOUNT_TYPE";
	public static final String SUB_ACC = "SUB_ACC";
	public static final String SUB_ACC_OWNER = "SUB_ACC_OWNER";
	public static final String DEPOSIT_ACC = "DEPOSIT_ACC";
	public static final String DEPOSIT_ACC_OWNER = "DEPOSIT_ACC_OWNER";
	public static final String DEPOSIT_ACC_CATEGORY = "DEPOSIT_ACC_CATEGORY";
	public static final String SENDER_DEPOSIT_ACC = "SENDER_DEPOSIT_ACC";
	public static final String SENDER_DEPOSIT_ACC_OWNER = "SENDER_DEPOSIT_ACC_OWNER";
	public static final String RECEIVER_DEPOSIT_ACC = "RECEIVER_DEPOSIT_ACC";
	public static final String RECEIVER_DEPOSIT_ACC_OWNER = "RECEIVER_DEPOSIT_ACC_OWNER";
	public static final String GPSC_GROUP = "GPSC_GROUP";
	public static final String LINE_ITEM_TEXT = "LINE_ITEM_TEXT";
	public static final String LINE_DESC = "LINE_DESC";
	public static final String PAYMENT_TERM = "PAYMENT_TERM";
	public static final String TRADING_PARTNER = "TRADING_PARTNER";
	public static final String TRADING_PARTNER_PARK = "TRADING_PARTNER_PARK";
	public static final String SPECIAL_GL = "SPECIAL_GL";
	public static final String DATE_BASELINE = "DATE_BASELINE";
	public static final String DUE_DATE = "DUE_DATE";
	public static final String DATE_VALUE = "DATE_VALUE";
	public static final String DATE_AS_OF = "DATE_AS_OF";
	public static final String QTY = "QTY";
	public static final String INCOME = "INCOME";
	public static final String PAYMENT_REF = "PAYMENT_REF";
	public static final String AUTOGEN = "AUTOGEN";
	public static final String IS_WTX = "IS_WTX";
	public static final String PAYMENT_BLOCK = "PAYMENT_BLOCK";
	public static final String VALUE_OLD = "VALUE_OLD";
	public static final String VALUE_NEW = "VALUE_NEW";
	public static final String CREATED = "CREATED";
	public static final String UPDATED = "UPDATED";
	public static final String REMARK = "REMARK";
	public static final String APPROVAL = "APPROVAL";
	public static final String REJECT = "REJECT";
	public static final String STATUS_AP = "STATUS_AP";
	public static final String WTWEB_FLAG = "WTWEB_FLAG";
	public static final String SUCCESS = "SUCCESS";
	public static final String SUCCESS_COUNT = "SUCCESS_COUNT";
	public static final String ERROR_COUNT = "ERROR_COUNT";
	public static final String TOTAL_COUNT = "TOTAL_COUNT";
	public static final String SOURCE_COMPANY_CODE = "SOURCE_COMP_CODE";
	public static final String USERPARK = "USERPARK";
	public static final String USERPOST = "USERPOST";
	public static final String USERVOID = "USERVOID";
	public static final String TRANSFER_DATE = "TRANSFER_DATE";
	public static final String PAYMENT_DATE = "PM_DATE";
	public static final String PAYMENT_IDEN = "PM_IDEN";
	public static final String PAYMENT_COMPANY_CODE = "PM_COMP_CODE";
	public static final String PAYMENT_GROUP_NO = "PM_GROUP_NO";
	public static final String PAYMENT_GROUP_DOC = "PM_GROUP_DOC";
	public static final String RECEIPT_TAXID = "RECEIPT_TAXID";
	public static final String COUNT_HEADERS = "COUNT_HEADERS";
	public static final String COUNT_LINE = "COUNT_LINE";
	public static final String CURRENT_PARK = "CURRENT_PARK";
	public static final String RP_APPROVE = "RP_APPROVE";
	public static final String INTERFACE_TYPE = "INTERFACE_TYPE";
	public static final String SUB_AGENCY = "SUB_AGENCY";
	public static final String DOC_BASE_TYPE = "DOC_BASE_TYPE";
	public static final String NEED_INTER_COMP = "NEED_INTER_COMP";
	public static final String K3_DOCUMENT_NO = "K3_DOC_NO";
	public static final String K3_FISCAL_YEAR = "K3_FISCAL_YEAR";
	public static final String OPEN_ITEM = "OPEN_ITEM";
	public static final String IS_PAID = "IS_PAID";
	public static final String STATUS_PAID = "STATUS_PAID";
	public static final String IS_PASS = "IS_PASS";
	public static final String RESULT_CODE = "RESULT_CODE";
	public static final String DOC_OIA_TYPE = "DOC_OIA_TYPE";
	public static final String RESET_ONLY = "RESET_ONLY";
	public static final String GL_ACCOUNT_PARK = "GL_ACCOUNT_PARK";
	public static final String SENDER_COMPANY_CODE = "SENDER_COMP_CODE";
	public static final String GROUP_COMPANY_CODE = "GROUP_COMP_CODE";
	public static final String RECEIVER_COMPANY_CODE = "RECEIVER_COMP_CODE";
	public static final String SENDER_COST_CENTER = "SENDER_COST_CENTER";
	public static final String RECEIVER_COST_CENTER = "RECEIVER_COST_CENTER";
	public static final String SENDER_GL_ACCOUNT = "SENDER_GL_ACCOUNT";
	public static final String RATIO = "RATIO";
	public static final String VERIFY_NO = "VERIFY_NO";
	public static final String ERROR_TEXT = "ERROR_TEXT";
	public static final String PRODUCT = "PRODUCT";
	
	public static final String COMPANY_CODE_2 = "COMP_CODE_2";
	public static final String REF_DOCUMENT_NO = "REF_DOC_NO";
	public static final String REF_FISCAL_YEAR = "REF_FISCAL_YEAR";
	public static final String REF_COMPANY_CODE = "REF_COMP_CODE";
	public static final String REF_LINE = "REF_LINE";
	
	public static final String CLEARING_DATE_DOC = "CLEARING_DATE_DOC";
	public static final String CLEARING_DATE_ACCT = "CLEARING_DATE_ACCT";
	public static final String CLEARING_DOC_NO = "CLEARING_DOC_NO";
	public static final String CLEARING_FISCAL_YEAR = "CLEARING_FISCAL_YEAR";
	public static final String CLEARING_DOC_TYPE = "CLEARING_DOC_TYPE";
	public static final String CLEARING_DOCUMENT_STATUS = "CLEARING_DOC_STATUS";
	
	public static final String SEARCH = "SEARCH";
	public static final String ITEM_LIST = "ITEMS";
	public static final String ITEM_ENTRY = "ITEM";
	public static final String AUTO_DOCUMENT_LIST = "AUTODOCS";
	public static final String AUTO_DOCUMENT_ENTRY = "AUTODOC";
	public static final String INVOICE_DOCUMENT_LIST = "INVDOCS";
	public static final String INVOICE_DOCUMENT_ENTRY = "INVDOC";
	public static final String FOLLOWING_LIST = "FOLLOWING_DOCS";
	public static final String FOLLOWING_ENTRY = "FOLLOWING_DOC";
	public static final String CHANGE_LOG_LIST = "CHANGED_LOGS";
	public static final String CHANGE_LOG_ENTRY = "CHANGED_LOG";
	public static final String FROMDOC_ENTRY = "FROMDOC";
	public static final String EXITS_COMPLETES_LIST = "EXITS_COMPLETES";
	public static final String EXITS_COMPLETE_ENTRY = "EXITS_COMPLETE";
	public static final String LINE_LIST = "LINES";
	public static final String DOCUMENT_LIST = "DOCUMENTS";
	public static final String DOCUMENT_ENTRY = "DOCUMENT";
	public static final String FIDOC = "FIDOC";
	public static final String MODULE = "MODULE";
	public static final String DETAIL_DOC = "DETAIL_DOC";
	public static final String NEED_DETAIL_DOC = "NEED_DETAIL_DOC";
	
	// GL Advances
	public static final String ADV_CLASS = "ADV_CLASS";
	public static final String ADV_TYPE = "ADV_TYPE";
	public static final String ADV_HEAD_GROUP = "ADV_HEAD_GROUP";
	public static final String ADV_CATEGORY = "ADV_CATEGORY";
	public static final String ADV_PAY_CATEGORY = "ADV_PAY_CATEGORY";
	public static final String DISASTER_CATEGORY = "DISASTER_CATEGORY";
	public static final String ADV_HEAD_NO = "ADV_HEAD_NO";
	public static final String ADV_HEAD_CODE = "ADV_HEAD_CODE";
	public static final String ADV_CHANGE_NO = "ADV_CHANGE_NO";
	public static final String VALID_FROM = "VALID_FROM";
	public static final String VALID_TO = "VALID_TO";
	public static final String ADV_MAIN_CODE = "ADV_MAIN_CODE";
	public static final String ADV_MAIN_LINE = "ADV_MAIN_LINE";
	public static final String ADV_ALLOCATE = "ADV_ALLOCATE";
	public static final String USER_CREATED = "USER_CREATED";
	public static final String USER_UPDATED = "USER_UPDATED";
	
	// search
	public static final String ACCOUNT_DOCUMENT_NO_FROM = "ACC_DOC_NO_FROM";
	public static final String ACCOUNT_DOCUMENT_NO_TO = "ACC_DOC_NO_TO";
	public static final String FISCAL_YEAR_FROM = "FISCAL_YEAR_FROM";
	public static final String FISCAL_YEAR_TO = "FISCAL_YEAR_TO";
	public static final String FI_AREA_FROM = "FI_AREA_FROM";
	public static final String FI_AREA_TO = "FI_AREA_TO";
	public static final String COMPANY_CODE_FROM = "COMP_CODE_FROM";
	public static final String COMPANY_CODE_TO = "COMP_CODE_TO";
	public static final String PAYMENT_CENTER_FROM = "PAYMENT_CENTER_FROM";
	public static final String PAYMENT_CENTER_TO = "PAYMENT_CENTER_TO";
	public static final String ADV_MAIN_CODE_FROM = "ADV_MAIN_CODE_FROM";
	public static final String ADV_MAIN_CODE_TO = "ADV_MAIN_CODE_TO";
	public static final String LINE_FROM = "LINE_FROM";
	public static final String LINE_TO = "LINE_TO";
	public static final String ADV_CATEGORY_FROM = "ADV_CATEGORY_FROM";
	public static final String ADV_CATEGORY_TO = "ADV_CATEGORY_TO";
	public static final String ADV_HEAD_CODE_FROM = "ADV_HEAD_CODE_FROM";
	public static final String ADV_HEAD_CODE_TO = "ADV_HEAD_CODE_TO";
	public static final String DATE_ACCT_FROM = "DATE_ACCT_FROM";
	public static final String DATE_ACCT_TO = "DATE_ACCT_TO";
	public static final String DATE_DOC_FROM = "DATE_DOC_FROM";
	public static final String DATE_DOC_TO = "DATE_DOC_TO";
	public static final String CREATED_FROM = "CREATED_FROM";
	public static final String CREATED_TO = "CREATED_TO";
	public static final String DATE_APPROVE_FROM = "DATE_APPROVE_FROM";
	public static final String DATE_APPROVE_TO = "DATE_APPROVE_TO";
	public static final String VENDOR_TAXID_FROM = "VENDOR_TAXID_FROM";
	public static final String VENDOR_TAXID_TO = "VENDOR_TAXID_TO";
	public static final String DOCUMENT_TYPE_FROM = "DOC_TYPE_FROM";
	public static final String DOCUMENT_TYPE_TO = "DOC_TYPE_TO";
	public static final String PAYMENT_METHOD_FROM = "PAYMENT_METHOD_FROM";
	public static final String PAYMENT_METHOD_TO = "PAYMENT_METHOD_TO";
	public static final String COST_CENTER1_FROM = "COST_CENTER1_FROM";
	public static final String COST_CENTER1_TO = "COST_CENTER1_TO";
	public static final String COST_CENTER2_FROM = "COST_CENTER2_FROM";
	public static final String COST_CENTER2_TO = "COST_CENTER2_TO";
	public static final String HEADER_REFERENCE_FROM = "HEADER_REFERENCE_FROM";
	public static final String HEADER_REFERENCE_TO = "HEADER_REFERENCE_TO";
	public static final String I_AUTH = "I_AUTH";
	public static final String DECISION = "DECISION";
	
	// eGP PO
	public static final String CONTRACT_PRICE = "CONTRACT_PRICE";
	public static final String RELATE_BUDGET = "RELATE_BUDGET";
	public static final String ADVANCE_PAYMENT = "ADVANCE_PAYMENT";
	public static final String BALANCE_PRICE = "BALANCE_PRICE";
	public static final String COLLECTION_BUDGET = "COLLECTION_BUDGET";
	public static final String ADVANCE_PRICE = "ADVANCE_PRICE";
	
	// eGP MR
	public static final String COLLECTION_PHASE = "COLLECTION_PHASE";
	public static final String DELIVERY_PHASE = "DELIVERY_PHASE";
	public static final String FINE_PRICE = "FINE_PRICE";
	public static final String COLLECTION_PRICE = "COLLECTION_PRICE";
	public static final String WORK_GUARANTEE_PRICE = "WORK_GUARANTEE_PRICE";
	
	// fixed asset
	public static final String ASSET_NO = "ASSET_NO";
	public static final String ASSET_TRAN_TYPE = "ASSET_TRAN_TYPE";
	
	// Asset
	public static final String MASTER = "MASTER";
	public static final String ASSET_LIST = "ASSETS";
	public static final String ASSET_ENTRY = "ASSET";
	public static final String CLEARING_DOC_LIST = "CLEARING_DOC_LIST";
	public static final String CLEARING_DOC_ENTRY = "CLEARING_DOC_ENTRY";
	public static final String ASSET_MASTER_NO = "ASSET_MASTER_NO";
	public static final String ASSET_SUB_NO = "ASSET_SUB_NO";
	public static final String ASSET_DESCRIPTION = "ASSET_DESCRIPTION";
	public static final String ASSET_MAIN_DESCRIPTION = "ASSET_MAIN_DESCRIPTION";
	public static final String USABLE_LIFE_YEAR = "USABLE_LIFE_YEAR";
	public static final String USABLE_LIFE_MONTH = "USABLE_LIFE_MONTH";
	public static final String QTY_CURRENT = "QTY_CURRENT";
	public static final String ASSET_CAPITAL_DATE = "ASSET_CAPITAL_DATE";
	public static final String ASSET_DISPOSAL_DATE = "ASSET_DISPOSAL_DATE";
	public static final String ASSET_FIRST_ACQUISITION_DATE = "ASSET_FIRST_ACQUISITION_DATE";
	public static final String ASSET_START_DEPRECIATION_DATE = "ASSET_START_DEPRECIATION_DATE";
	public static final String TAKE_OVER_DATE = "TAKE_OVER_DATE";
	public static final String CLOSE_YEAR = "CLOSE_YEAR";
	public static final String CARRY_FORWARD_YEAR = "CARRY_FORWARD_YEAR";
	public static final String ASSET_ACQUIISITION_DATE = "ASSET_ACQUISITION_DATE";
	public static final String ASSET_GROUP = "ASSET_GROUP";
	public static final String ASSET_GROUP_NAME = "ASSET_GROUP_NAME";
	public static final String ASSET_NAME = "ASSET_NAME";
	public static final String ASSET_VALUE = "ASSET_VALUE";
	public static final String SERIAL_NO = "SERIAL_NO";
	public static final String INVENTORY_NO = "INVENTORY_NO";
	public static final String DEPRECIATION_AREA_CODE = "DEPRECIATION_AREA_CODE";
	public static final String DEPRECIATION_KEY = "DEPRECIATION_KEY";
	public static final String DEPRECIATION = "DEPRECIATION";
	public static final String INSTANCE_COUNT = "INSTANCE_COUNT";
	public static final String ASSET_ADDITIONAL_COST = "ASSET_ADDITIONAL_COST";
	public static final String ASSET_REVALUE = "ASSET_REVALUE";
	public static final String ACCUMULATED_ADDITIONAL_COST = "ACCUMULATED_ADDITIONAL_COST";
	public static final String ACCUMULATED_REVALUE = "ACCUMULATED_REVALUE";
	public static final String ACCUMULATED_DEPRECIATION = "ACCUMULATED_DEPRECIATION";
	public static final String PLAN_DEPRECIATION = "PLAN_DEPRECIATION";
	public static final String POSTED_DEPRECIATION = "POSTED_DEPRECIATION";
	public static final String VALUE_ADJUST = "VALUE_ADJUST";
	public static final String USE_YEARS = "USE_YEARS";
	public static final String USE_MONTHS = "USE_MONTHS";
	public static final String REMAIN_YEARS = "REMAIN_YEARS";
	public static final String REMAIN_MONTHS = "REMAIN_MONTHS";
	public static final String PERCENT = "PERCENT";
	public static final String RECEIVER_ASSET_COMPANY_CODE = "RECEIVER_ASSET_COMP_CODE";
	public static final String RECEIVER_ASSET_MASTER_NO = "RECEIVER_ASSET_MASTER_NO";
	public static final String RECEIVER_ASSET_SUB_NO = "RECEIVER_ASSET_SUB_NO";
	public static final String ASSET_STATUS_CODE = "ASSET_STATUS_CODE";
	public static final String POST_REASON = "POST_REASON";
	public static final String PROCESS_PLAN = "PROCESS_PLAN";
	public static final String PROCESS_DATETIME = "PROCESS_DATETIME";
	public static final String DEPRE_TRX_LIST = "DEPRE_TRXS";
	public static final String DEPRE_TRX_ENTRY = "DEPRE_TRX";
	public static final String TRANSACTION_LIST = "TRANSACTIONS";
	public static final String TRANSACTION_ENTRY = "TRANSACTION";
	public static final String SETTLEMENT_RULE_LIST = "SETTLE_RULES";
	public static final String SETTLEMENT_RULE_ENTRY = "SETTLE_RULE";
	public static final String DELETE_RULE_LIST = "DELETE_RULES";
	public static final String DELETE_RULE_ENTRY = "DELETE_RULE";
	public static final String RULE_GROUP = "RULE_GROUP";
	public static final String SEQ = "SEQ";
	public static final String TRANS_AMOUNT = "TRANS_AMOUNT";
	public static final String CHANGE = "CHANGE";
	public static final String TRANS_ID = "TRANS_ID";
	
	// Authorization
	public static final String AUTH_PAYMENT_CENTER = "AUTH_PAYMENT_CENTER";
	public static final String AUTH_COST_CENTER = "AUTH_COST_CENTER";
	public static final String AUTH_FI_AREA = "AUTH_FI_AREA";
	public static final String AUTH_COMP_CODE = "AUTH_COMP_CODE";
	
	// Authorization
	public static final String AUTHORIZATION_OBJECT = "AUTH_OBJECT";
	public static final String AUTHORIZATION_ACTIVITY = "AUTH_ACTIVITY";
	public static final String AUTHORIZATION_ATTRIBUTE = "AUTH_ATTRIBUTE";
	public static final String AUTHORIZATION_USER = "AUTH_USER";
	public static final String AUTHORIZATION_GROUP = "AUTH_GROUP";
	public static final String AUTHORIZATION_ASSIGNMENT = "AUTH_ASSIGN_INFO";
	public static final String AUTHORIZATION_VALUE = "AUTH_ATTR_INFO";
	public static final String GROUP_NAME = "GROUP_NAME";
	
	/*
	 * Name
	 */
	// Budget
	public static final String BUDGET_ACCOUNT_NAME = "BG_ACCOUNT_NAME";
	public static final String BUDGET_CODE_NAME = "BG_CODE_NAME";
	public static final String SOURCE_OF_FUND_NAME = "FUND_SOURCE_NAME";
	public static final String FUND_CENTER_NAME = "FUND_CENTER_NAME";
	public static final String BUDGET_ACTIVITY_NAME = "BG_ACTIVITY_NAME";
	public static final String BUDGET_AREA_NAME = "BG_AREA_NAME";
	public static final String AREA_OF_WORK_NAME = "AREA_OF_WORK_NAME";
	public static final String BUDGET_PROJECT_NAME = "BG_PROJECT_NAME";
	public static final String BUDGET_STATEGY_NAME = "BG_STRATEGY_NAME";
	public static final String PAYMENT_CENTER_NAME = "PAYMENT_CENTER_NAME";
	public static final String COST_CENTER_NAME = "COST_CENTER_NAME";
	public static final String COST_ACTIVITY_NAME = "COST_ACTIVITY_NAME";
	public static final String BUDGET_TYPE_NAME = "BUDGET_TYPE_NAME";
	
	// General
	public static final String COMPANY_CODE_NAME = "COMP_CODE_NAME";
	public static final String DOCUMENT_STATUS_NAME = "DOC_STATUS_NAME";
	public static final String DOCUMENT_TYPE_NAME = "DOC_TYPE_NAME";
	public static final String UOM_NAME = "UOM_NAME";
	public static final String STATUS_NAME = "STATUS_NAME";
	
	// FI
	public static final String POSTING_KEY_NAME = "POSTING_KEY_NAME";
	public static final String PAYEE_NAME = "PAYEE_NAME";
	public static final String COST_CENTER1_NAME = "COST_CENTER1_NAME";
	public static final String COST_CENTER2_NAME = "COST_CENTER2_NAME";
	public static final String REVERSAL_MARK = "REVERSAL_MARK";
	public static final String FI_AREA_NAME = "FI_AREA_NAME";
	public static final String BANK_BOOK_NAME = "BANK_BOOK_NAME";
	public static final String SUB_BOOK_NAME = "SUB_BOOK_NAME";
	public static final String SUB_ACCOUNT_TYPE_NAME = "SUB_ACCOUNT_TYPE_NAME";
	public static final String SUB_ACC_NAME = "SUB_ACC_NAME";
	public static final String SUB_ACC_OWNER_NAME = "SUB_ACC_OWNER_NAME";
	public static final String DEPOSIT_ACC_NAME = "DEPOSIT_ACC_NAME";
	public static final String DEPOSIT_ACC_OWNER_NAME = "DEPOSIT_ACC_OWNER_NAME";
	public static final String DEPOSIT_ACC_CATEGORY_NAME = "DEPOSIT_ACC_CATEGORY_NAME";
	public static final String GPSC_NAME = "GPSC_NAME";
	public static final String GPSC_GROUP_NAME = "GPSC_GROUP_NAME";
	public static final String TRADING_PARTNER_NAME = "TRADING_PARTNER_NAME";
	public static final String INCOME_NAME = "INCOME_NAME";
	public static final String GL_ACCOUNT_NAME = "GL_ACCOUNT_NAME";
	public static final String GL_ACCOUNT2_NAME = "GL_ACCOUNT2_NAME";
	
	// Vendor
	public static final String VENDOR_NAME = "VENDOR_NAME";
	public static final String VENDOR_GROUP_NAME = "VENDOR_GROUP_NAME";
	public static final String BANK_NAME = "BANK_NAME";
	public static final String BANK_BRANCH_NAME = "BRANCH_NAME";
	public static final String BANK_ACC_NO_NAME = "BANK_ACC_NO_NAME";
	
	// GL Advances
	public static final String ADV_CLASS_NAME = "ADV_CLASS_NAME";
	public static final String ADV_TYPE_NAME = "ADV_TYPE_NAME";
	public static final String ADV_CATEGORY_NAME = "ADV_CATEGORY_NAME";
	public static final String DISASTER_CATEGORY_NAME = "DISASTER_CATEGORY_NAME";
	public static final String ADV_PAY_CATEGORY_NAME = "ADV_PAY_CATEGORY_NAME";
	public static final String ADV_HEAD_NAME = "ADV_HEAD_NAME";
	
	// Asset
	public static final String ASSET_TRAN_TYPE_NAME = "ASSET_TRAN_TYPE_NAME";
	
	// Job
	public static final String JOB_ID = "JOB_ID";
	public static final String PROG_NAME = "PROG_NAME";
	public static final String JOB_OWNER = "OWNER";
	public static final String VARIANT = "VARIANT";
	public static final String VARIANT_NAME = "VARIANT_NAME";
	public static final String VARIANT_VALUE = "VARIANT_VALUE";
	public static final String TEST_RUN = "TEST_RUN";
}
