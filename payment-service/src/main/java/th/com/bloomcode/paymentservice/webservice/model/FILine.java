package th.com.bloomcode.paymentservice.webservice.model;

import lombok.Data;
import th.com.bloomcode.paymentservice.adapter.TimestampAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FILine {
	@XmlElement(name = StandardXMLTagName.POSTING_KEY)
	private String postingKey = null;
	@XmlElement(name = StandardXMLTagName.ACC_TYPE)
	private String accountType = null;
	@XmlElement(name = StandardXMLTagName.GL_ACCOUNT)
	private String glAccount = null;
	@XmlElement(name = StandardXMLTagName.GL_ACCOUNT2)
	private String glAccount2 = null; // GL Account

	@XmlElement(name = StandardXMLTagName.FI_AREA)
	private String fiArea = null;
	@XmlElement(name = StandardXMLTagName.COST_CENTER)
	private String costCenter = null;
	@XmlElement(name = StandardXMLTagName.SOURCE_OF_FUND)
	private String fundSource = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_CODE)
	private String bgCode = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_ACTIVITY)
	private String bgActivity = null;
	@XmlElement(name = StandardXMLTagName.COST_ACTIVITY)
	private String costActivity = null;
	@XmlElement(name = StandardXMLTagName.AMOUNT)
	private BigDecimal amount = null;
	@XmlElement(name = StandardXMLTagName.REFERENCE3)
	private String reference3 = null;
	@XmlElement(name = StandardXMLTagName.ASSIGNMENT)
	private String assignment = null;
	@XmlElement(name = StandardXMLTagName.ASSIGNMENT_SPECIAL_GL)
	private String assignmentSpecialGL = null;
	@XmlElement(name = StandardXMLTagName.BR_DOCUMENT_NO)
	private String brDocNo = null;
	@XmlElement(name = StandardXMLTagName.BR_LINE)
	private int brLine = 0;
	@XmlElement(name = StandardXMLTagName.BANK_BOOK)
	private String bankBook = null;
	@XmlElement(name = StandardXMLTagName.SUB_BOOK)
	private String subBook = null;
	@XmlElement(name = StandardXMLTagName.GPSC)
	private String gpsc = null;
	@XmlElement(name = StandardXMLTagName.SUB_ACCOUNT_TYPE)
	private String subAccountType = null;
	@XmlElement(name = StandardXMLTagName.SUB_ACC)
	private String subAccount = null;
	@XmlElement(name = StandardXMLTagName.SUB_ACC_OWNER)
	private String subAccountOwner = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_CENTER)
	private String paymentCenter = null;
	@XmlElement(name = StandardXMLTagName.DEPOSIT_ACC_OWNER)
	private String depositAccountOwner = "";
	@XmlElement(name = StandardXMLTagName.DEPOSIT_ACC)
	private String depositAccount = "";
	@XmlElement(name = StandardXMLTagName.LINE_ITEM_TEXT)
	private String lineItemText = null;
	@XmlElement(name = StandardXMLTagName.LINE_DESC)
	private String lineDesc = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_TERM)
	private String paymentTerm = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_METHOD)
	private String paymentMethod = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_TYPE)
	private String wtxType = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_CODE)
	private String wtxCode = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_BASE)
	private BigDecimal wtxBase = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_AMOUNT)
	private BigDecimal wtxAmount = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_TYPE_P)
	private String wtxTypeP = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_CODE_P)
	private String wtxCodeP = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_BASE_P)
	private BigDecimal wtxBaseP = null;
	@XmlElement(name = StandardXMLTagName.WITHHOLDING_TAX_AMOUNT_P)
	private BigDecimal wtxAmountP = null;
	@XmlElement(name = StandardXMLTagName.VENDOR_TAXID)
	private String vendorTaxID = null;
	@XmlElement(name = StandardXMLTagName.BANK_ACC_NO)
	private String bankAccNo = null;
	@XmlElement(name = StandardXMLTagName.BANK_ACC_NO_NAME)
	private String bankAccNoName = null;
	@XmlElement(name = StandardXMLTagName.BANK_ACC_NO_ACTIVE)
	private Boolean bankAccNoActive = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_REF)
	private String paymentRef = null;
	@XmlElement(name = StandardXMLTagName.BANK_BRANCH_NO)
	private String branchNo = null;
	@XmlElement(name = StandardXMLTagName.TRADING_PARTNER)
	private String tradingPartner = null;
	@XmlElement(name = StandardXMLTagName.GPSC_GROUP)
	private String gpscGroup = null;
	@XmlElement(name = StandardXMLTagName.SPECIAL_GL)
	private String specialGL = null;
	@XmlElement(name = StandardXMLTagName.INVOICE_FISCAL_YEAR)
	private String invFiscalYear = null;
	@XmlElement(name = StandardXMLTagName.INVOICE_DOC_NO)
	private String invDocNo = null;
	@XmlElement(name = StandardXMLTagName.INVOICE_LINE)
	private int invLine = 0;
	
	@XmlElement(name = StandardXMLTagName.DATE_BASELINE)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateBaseLine = null;
	@XmlElement(name = StandardXMLTagName.DUE_DATE)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dueDate = null;
	@XmlElement(name = StandardXMLTagName.ASSET_NO)
	private String assetNo = null;
	@XmlElement(name = StandardXMLTagName.ASSET_SUB_NO)
	private String assetSubNo = null;
	@XmlElement(name = StandardXMLTagName.QTY)
	private BigDecimal qty = null;
	@XmlElement(name = StandardXMLTagName.UOMFI)
	private String uom = null;
	@XmlElement(name = StandardXMLTagName.ASSET_TRAN_TYPE)
	private String assetTranType = null;
	@XmlElement(name = StandardXMLTagName.REFERENCE1)
	private String reference1 = null;
	@XmlElement(name = StandardXMLTagName.COMPANY_CODE)
	private String compCode = null;
	@XmlElement(name = StandardXMLTagName.PO_DOCUMENT_NO)
	private String poDocNo = null;
	@XmlElement(name = StandardXMLTagName.PO_LINE)
	private int poLine = 0;
	@XmlElement(name = StandardXMLTagName.INCOME)
	private String income = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_ACCOUNT)
	private String bgAccount = null;

	// return only
	@XmlElement(name = StandardXMLTagName.POSTING_KEY_NAME)
	private String postingKeyName = null;
	@XmlElement(name = StandardXMLTagName.GL_ACCOUNT_NAME)
	private String glAccountName = null;
	@XmlElement(name = StandardXMLTagName.GL_ACCOUNT2_NAME)
	private String glAccount2Name = null;

	@XmlElement(name = StandardXMLTagName.COST_CENTER_NAME)
	private String costCenterName = null;
	@XmlElement(name = StandardXMLTagName.SOURCE_OF_FUND_NAME)
	private String fundSourceName = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_CODE_NAME)
	private String bgCodeName = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_ACTIVITY_NAME)
	private String bgActivityName = null;
	@XmlElement(name = StandardXMLTagName.VENDOR_NAME)
	private String vendorName = null;
	@XmlElement(name = StandardXMLTagName.COST_ACTIVITY_NAME)
	private String costActivityName = null;
	@XmlElement(name = StandardXMLTagName.FI_AREA_NAME)
	private String fiAreaName = null;
	@XmlElement(name = StandardXMLTagName.INCOME_NAME)
	private String incomeName = null;
	@XmlElement(name = StandardXMLTagName.VENDOR)
	private String vendor = null;
	@XmlElement(name = StandardXMLTagName.TRADING_PARTNER_NAME)
	private String trandingPartnerName = null;
	@XmlElement(name = StandardXMLTagName.GPSC_NAME)
	private String gpscName = null;
	@XmlElement(name = StandardXMLTagName.GPSC_GROUP_NAME)
	private String gpscGroupName = null;
	@XmlElement(name = StandardXMLTagName.BANK_BOOK_NAME)
	private String bankBookName = null;
	@XmlElement(name = StandardXMLTagName.SUB_ACCOUNT_TYPE_NAME)
	private String subAccountTypeName = null;
	@XmlElement(name = StandardXMLTagName.SUB_BOOK_NAME)
	private String subBookName = null;
	@XmlElement(name = StandardXMLTagName.SUB_ACC_NAME)
	private String subAccountName = null;
	@XmlElement(name = StandardXMLTagName.SUB_ACC_OWNER_NAME)
	private String subAccountOwnerName = null;
	@XmlElement(name = StandardXMLTagName.DEPOSIT_ACC_OWNER_NAME)
	private String depositAccountOwnerName = null;
	@XmlElement(name = StandardXMLTagName.DEPOSIT_ACC_NAME)
	private String depositAccountName = null;
	@XmlElement(name = StandardXMLTagName.BUDGET_ACCOUNT_NAME)
	private String bgAccountName = null;
	@XmlElement(name = StandardXMLTagName.REFERENCE2)
	private String reference2 = null;
	@XmlElement(name = StandardXMLTagName.DATE_VALUE)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp dateValue = null;
	@XmlElement(name = StandardXMLTagName.PAYMENT_BLOCK)
	private String paymentBlock = null;
	@XmlElement(name = StandardXMLTagName.CLEARING_DATE_ACCT)
	@XmlJavaTypeAdapter(TimestampAdapter.class)
	private Timestamp clearingDateAcct = null;
	@XmlElement(name = StandardXMLTagName.CLEARING_DOC_NO)
	private String clearingDocNo = null;
	@XmlElement(name = StandardXMLTagName.CLEARING_FISCAL_YEAR)
	private String clearingFiscalYear = null;
	@XmlElement(name = StandardXMLTagName.DR_CR)
	private String drCr = null;
	@XmlElement(name = StandardXMLTagName.UOM_NAME)
	private String uomName = null;
	@XmlElement(name = StandardXMLTagName.LINE)
	private int line = 0;
	@XmlElement(name = StandardXMLTagName.AUTOGEN)
	private boolean autoGen = false;
}
