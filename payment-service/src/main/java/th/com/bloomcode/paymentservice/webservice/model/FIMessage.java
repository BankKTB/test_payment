package th.com.bloomcode.paymentservice.webservice.model;

import th.com.bloomcode.paymentservice.webservice.adapter.Base64Adapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@XmlRootElement(name = "FI")
@XmlAccessorType(XmlAccessType.FIELD)
public class FIMessage extends BaseMQServiceXML {
	
	@XmlElement(name = "DATATYPE")
	private String dataType = null;
	
	@XmlJavaTypeAdapter(Base64Adapter.class)
	@XmlElement(name = "DATA")
	private String data = null;
	
	public void setId(String clientValue, TableLog tableLog, int logID) {
		setId(clientValue + "." + tableLog.getCode() + "." + logID);
	}
	
	public void setId(String clientValue, TableLog tableLog, int logID, TableLog tableMQLog, int mqLogID) {
		setId(clientValue + "." + tableLog.getCode() + "." + logID + "." + tableMQLog.getCode() + "." + mqLogID);
	}
	
	private static boolean isValidID(String refID) {
		return Pattern.matches("\\w+.([HIDR]L|RA).\\d+(.M[HL].\\d+)?", refID);
	}
	
	public boolean isValidID() {
		return isValidID(getId());
	}
	
	private static FIID splitID(String refID) {
		if (!isValidID(refID))
			return null;
		List<String> elephantList = Arrays.asList(refID.split("\\."));
		if (elephantList.size() == 3)
			return new FIID(elephantList.get(0), elephantList.get(1), Integer.parseInt(elephantList.get(2)));
		else if (elephantList.size() == 5)
			return new FIID(elephantList.get(0), elephantList.get(1), Integer.parseInt(elephantList.get(2)), elephantList.get(3),
					Integer.parseInt(elephantList.get(4)));
		return null;
	}
	
	public FIID getIDValue() {
		return splitID(getId());
	}
	
	public static FIID getFIID(String RefID) {
		return splitID(RefID);
	}
	
	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public enum Type {
		// max length 5
		
		REQUEST("REQ"), RESPONSE("RES");
		
		private final String code;
		
		Type(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public enum DataType {
		// max length 5
		// Create Doc
		CREATE("CRE")
		
		// Create Doc for Payment System
		, CREATE_JU("CREJU")
		
		// Create Doc for Asset Transfer (Receiver)
		, CREATE_AA("CREAA")
		
		// Reverse Doc
		, REVERSE("REV")
		
		// Reverse Doc Invoice for Payment System
		, REVERSE_INVOICE("REINV")
		
		// Update Payment Block
		, UPDATE_PBK("UPB")
		
		// Change Payment Block All
		, PBK_ALL("PBKAL")
		
		// Update RP Approve
		, UPDATE_RP_APPROVE("URPA")
		
		// Update Bank Account No
		, UPDATE_BANK_ACC_NO("UBAN")
		
		// View Doc
		, VIEW("VIEW")
		
		// Post Doc (Park)
		, POST("POST")
		
		// Void Doc (Park)
		, VOID("VOID")
		
		// Search Doc
		, SEARCH("SEARC")
		
		// Resends Doc to Payment System
		, RESENDS_TO_PAYMENT("RSPAY")
		
		// This Invoice is Paid
		, INV_IS_PAID("ISPAY")
		
		// Update log
		, UPDATE_LOG("UPLOG")
		
		// Status Paid
		, STATUS_PAID("STPAY")
		
		// Check Reverse Doc
		, CHECK_REV("CHREV");
		
		private final String code;
		
		DataType(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public enum System {
		// Max length 30
		// ระบบ Payment
		PAYMENT("PaySys")
		
		// ระบบ จัดเก็บรายได้ 3 กรมภาษี
		, RPINF_3TAX("RPInf.3TaxComp")
		
		// ระบบ จัดเก็บรายได้ K-Corp
		, RPINF_KCORP("RPInf.KCorp")
		
		// ระบบ สร้างเอกสาร KTB Statement
		, RPINF_KTBSTATEMENT("RPInf.KTBStatement")
		
		// ระบบ สร้างเอกสาร KTB Statement รับรวม
		, RPINF_KTBSTATEMENTWOB("RPInf.KTBStatementWOB")
		
		// ระบบ สร้างเอกสาร BOT Statement
		, RPINF_BOTSTATEMENT("RPInf.BOTStatement")
		
		// ระบบ Reconverts Statement
		, RPINF_RCHECK("RPInf.RCheck")
		
		// ระบบ สร้างเอกสารตั้งหนี้ เงินเดือน, รักษาพยาบาล และ บำเหน็จบำนาญ
		, RPINF_INVOICE("RPInf.Invoice")
		
		// ระบบ Central Agency
		, RPINF_CAA("RPInf.Caa")
		
		// ระบบ ซื้อซอง
		, RPINF_EBIDING("RPInf.EBiding")
		
		// ระบบ รับกลาง
		, RPINF_EPORTAL("RPInf.EPortal")
		
		// จับคู่ห้กล้าง
		, RPINF_CLEARING("RPInf.Clearing")
		
		// ระบบ PDM
		, PDM_PUBLIC_DEBT("PDM.PublicDebt");
		
		private final String code;
		
		System(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public enum QueueName {
		// Max length 40 (แต่ให้ตั้งได้แค่ 30 อีก 10 ไว้สำหรับเลข Client)
		// Local for iDempiere - รับ สร้างเอกสาร (ยกเว้นชำระเงิน 99999)
		FI_AUTODOC("FI.AutoDoc")
		
		// Local for iDempiere - รับ สร้างเอกสารชำระเงิน 99999
		, FI_AUTODOC_PAYMENT("FI.AutoDocPayment")
		
		// Local for iDempiere - รับ อม01, 2 และปลดบล๊อก เพื่อ 99999 เท่านั้น
		, FI_AUTODOC_UP_PBK("FI.AutoDocUpPbk")
		
		// Local for iDempiere - รับ ไม่ใช่สร้างเอกสาร เพื่อ 99999 เท่านั้น
		, FI_AUTODOC_NOT_CREATE("FI.AutoDocNotCreate")
		
		// ส่ง/รับ สร้างเอกสารจัดเก็บ จากระบบ จัดเก็บรายได้ 3 กรมภาษี
		, RPINF_3TAX("RPInf.3TaxComp")
		
		// ส่ง/รับ สร้างเอกสารจัดเก็บ จากระบบ จัดเก็บรายได้ K-Corp
		, RPINF_KCORP("RPInf.KCorp")
		
		// ส่ง/รับ สร้างเอกสารจัดเก็บ จากระบบ จัดเก็บรายได้ K-Corp 2
		, RPINF_KCORP_2("RPInf.KCorp2")
		
		// ส่ง/รับ สร้างเอกสาร KTB Statement
		, RPINF_KTBSTATEMENT("RPInf.KTBStatement")
		
		// ส่ง/รับ สร้างเอกสาร KTB Statement รับรวม
		, RPINF_KTBSTATEMENTWOB("RPInf.KTBStatementWOB")
		
		// ส่ง/รับ สร้างเอกสาร BOT Statement
		, RPINF_BOTSTATEMENT("RPInf.BOTStatement")
		
		// ระบบ Reconverts Statement
		, RPINF_RCHECK("RPInf.RCheck")
		
		// ส่ง/รับ สร้างเอกสารตั้งหนี้ เงินเดือน, รักษาพยาบาล และ บำเหน็จบำนาญ
		, RPINF_INVOICE("RPInf.Invoice")
		
		// ส่งเอกสาร park ให้ระบบ RPInf
		, RPINF_RECEIVE_DOC("RPInf.ReceiveDoc")
		
		// ส่ง/รับ เปลี่ยนสถานะเอกสารจาก park --> post จากระบบ RPInf
		, RPINF_CAA("RPInf.Caa")
		
		// ส่ง/รับ ค้นหาเอกสารสถานะผ่านรายการ
		, RPINF_CAA_SEARCH("RPInf.CaaSearch")
		
		// ส่ง/รับ ซื้อซอง
		, RPINF_EBIDING("RPInf.EBiding")
		
		// ส่ง/รับ รับกลาง
		, RPINF_EPORTAL("RPInf.EPortal")
		
		// จับคู่ห้กล้าง - หักล้าง, reset, reset and reverse
		, RPINF_CLEARING("RPInf.Clearing")
		
		// จับคู่ห้กล้าง - ค้นหา
		, RPINF_CLEARING_SEARCH("RPInf.ClearingSearch")
		
		// ส่งเอกสารตั้งหนี้ ให้ระบบ Payment
		, AP_RECEIVE_DOC("AP.ReceiveDoc")
		
		// ส่ง/รับ เปลี่ยนสถานะ payment block ให้ระบบ Payment
		, AP_UP_PBK("AP.UpPbk")
		
		// ส่ง/รับ เปลี่ยนสถานะ payment block ให้ระบบ Payment ตั้งใจให้เฉพาะ ค่ารักษาพยาบาล
		, AP_UP_PBK_1("AP.UpPbk1")
		
		// ส่ง/รับ สร้างเอกสารชำระเงิน จากระบบ Payment
		, AP_PAYMENT("AP.Payment")
		
		// ส่ง/รับ สร้างเอกสารเงินกู้ จากระบบ PDM
		, PDM_PUBLIC_DEBT("PDM.PublicDebt")
		
		// ส่งเอกสารตั้งหนี้ ให้ระบบ PDM
		, PDM_RECEIVE_DOC("PDM.ReceiveDoc");
		
		private final String code;
		
		QueueName(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
	public enum TableLog {
		// Max length 2
		// TH_CAHeaderLog
		HEADER_LOG("HL")
		
		// TH_CAItemLog
		, ITEM_LOG("IL")
		
		// TH_CAAutoDocLog
		, AUTO_DOC_LOG("DL")
		
		// TH_CAReceiveAutoDocLog
		, RECEIVE_AUTO_DOC_LOG("RA")
		
		// TH_CAReceiveAutoDocLineLog
		, RECEIVE_AUTO_DOC_LINE_LOG("RL")
		
		// TH_FIReceiveLog
		, RECEIVE_MQ_LOG("MH")
		
		// TH_FIReceiveLineLog
		, RECEIVE_MQ_LINE_LOG("ML");
		
		private final String code;
		
		TableLog(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}
	}
	
}
