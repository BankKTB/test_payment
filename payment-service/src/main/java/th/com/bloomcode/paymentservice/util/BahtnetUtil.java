package th.com.bloomcode.paymentservice.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.model.payment.SwiftFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class BahtnetUtil {

  public static String generateBahtnetLevelMaster(SwiftFile swift, Long refRunning, int refLine) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    StringBuilder sb = new StringBuilder();
    sb.append("<RequestPayload>");
    sb.append("<AppHdr xmlns=\"urn:iso:std:iso:20022:tech:xsd:head.001.001.02\">");
    sb.append("	<Fr>");
    sb.append("		<OrgId>");
    sb.append("			<Id>");
    sb.append("				<OrgId>");
    sb.append("					<AnyBIC>CGDXTHB1</AnyBIC>");
    sb.append("				</OrgId>");
    sb.append("			</Id>");
    sb.append("		</OrgId>");
    sb.append("	</Fr>");
    sb.append("	<To>");
    sb.append("		<OrgId>");
    sb.append("			<Id>");
    sb.append("				<OrgId>");
    sb.append("					<AnyBIC>BOTHTHBP</AnyBIC>");
    sb.append("				</OrgId>");
    sb.append("			</Id>");
    sb.append("		</OrgId>");
    sb.append("	</To>");
    sb.append("	<BizMsgIdr>").append(refRunning).append("-").append(StringUtils.leftPad(String.valueOf(refLine), 6, "0")).append("</BizMsgIdr>");
    sb.append("	<MsgDefIdr>pacs.008.001.08</MsgDefIdr>");
    sb.append("	<BizSvc>bot.bahtnet.01</BizSvc>");
    sb.append("	<CreDt>").append(df.format(new Date())).append("</CreDt>");
    sb.append("</AppHdr>");
    sb.append("<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:pacs.008.001.08\">");
    sb.append("	<FIToFICstmrCdtTrf>");
    sb.append("		<GrpHdr>");
    sb.append("			<MsgId>").append(refRunning).append("-").append(StringUtils.leftPad(String.valueOf(refLine), 6, "0")).append("</MsgId>");
    sb.append("			<CreDtTm>").append(df.format(new Date())).append("</CreDtTm>");
    sb.append("			<NbOfTxs>").append("1").append("</NbOfTxs>");
    sb.append("			<SttlmInf>");
    sb.append("				<SttlmMtd>CLRG</SttlmMtd>");
    sb.append("				<ClrSys>");
    sb.append("					<Cd>THB</Cd>");
    sb.append("				</ClrSys>");
    sb.append("			</SttlmInf>");
    sb.append("		</GrpHdr>");
    sb.append("		<CdtTrfTxInf>");
    sb.append("			<PmtId>");
    sb.append("				<InstrId>").append(refRunning).append("-").append(StringUtils.leftPad(String.valueOf(refLine), 6, "0")).append("</InstrId>");
    sb.append("				<EndToEndId>NOTPROVIDED</EndToEndId>");
    sb.append("				<UETR>").append(UUID.randomUUID()).append("</UETR>");
    sb.append("			</PmtId>");
    sb.append("			<PmtTpInf>");
    sb.append("				<CtgyPurp>");
    sb.append("					<Prtry>").append(swift.getTransferType()).append("</Prtry>");
    sb.append("				</CtgyPurp>");
    sb.append("			</PmtTpInf>");
    if (!swift.isSumFile()) {
      sb.append("			<IntrBkSttlmAmt Ccy=\"THB\">")
          .append(String.format("%.2f", swift.getSetAmount()))
          .append("</IntrBkSttlmAmt>");
    } else {
      sb.append("			<IntrBkSttlmAmt Ccy=\"THB\">")
          .append(String.format("%.2f", swift.getTotalTransferAmount()))
          .append("</IntrBkSttlmAmt>");
    }
    sb.append("			<IntrBkSttlmDt>").append(swift.getValueDate()).append("</IntrBkSttlmDt>");
    sb.append("			<SttlmPrty>").append("HIGH").append("</SttlmPrty>");
    sb.append("			<ChrgBr>").append("SHAR").append("</ChrgBr>");
    sb.append("			<InstgAgt>");
    sb.append("				<FinInstnId>");
    sb.append("					<BICFI>CGDXTHB1</BICFI>");
    sb.append("					<Othr>");
    sb.append("						<Id>").append(swift.getSendAcct()).append("</Id>");
    sb.append("					</Othr>");
    sb.append("				</FinInstnId>");
    sb.append("			</InstgAgt>");
    sb.append("			<Dbtr>");
    sb.append("				<Nm>").append(swift.getOrdName1()).append("</Nm>");
    sb.append("			</Dbtr>");
    if (!Util.isEmpty(swift.getOrdAcct())) {
      sb.append("			<DbtrAcct>");
      sb.append("				<Id>");
      sb.append("					<Othr>");
      sb.append("						<Id>").append(swift.getOrdAcct()).append("</Id>");
      sb.append("					</Othr>");
      sb.append("				</Id>");
      sb.append("			</DbtrAcct>");
    }
    sb.append("			<DbtrAgt>");
    sb.append("				<FinInstnId>");
    sb.append("					<BICFI>CGDXTHB1</BICFI>");
    sb.append("				</FinInstnId>");
    sb.append("			</DbtrAgt>");
    sb.append("			<CdtrAgt>");
    sb.append("				<FinInstnId>");
    sb.append("					<BICFI>").append(swift.getRecInsti()).append("</BICFI>");
    sb.append("				</FinInstnId>");
    sb.append("			</CdtrAgt>");
    sb.append("			<CdtrAgtAcct>");
    sb.append("				<Id>");
    sb.append("					<Othr>");
    sb.append("						<Id>").append(swift.getRecAcct()).append("</Id>");
    sb.append("					</Othr>");
    sb.append("				</Id>");
    sb.append("			</CdtrAgtAcct>");
    sb.append("			<Cdtr>");
    sb.append("				<Nm>").append(Util.replaceNull(swift.getBenName1())).append(Util.replaceNull(swift.getBenName2())).append(Util.replaceNull(swift.getBenName3())).append(Util.replaceNull(swift.getBenName4())).append("</Nm>");
    sb.append("			</Cdtr>");
    sb.append("			<CdtrAcct>");
    sb.append("				<Id>");
    sb.append("					<Othr>");
    sb.append("						<Id>").append(swift.getBenAcct()).append("</Id>");
    sb.append("					</Othr>");
    sb.append("				</Id>");
    sb.append("			</CdtrAcct>");
    if (!Util.isEmpty(swift.getSendToRec1())) {
      sb.append("			<InstrForCdtrAgt>");
      sb.append("				<InstrInf>").append(Util.replaceNull(swift.getSendToRec1())).append(Util.replaceNull(swift.getSendToRec2())).append(Util.replaceNull(swift.getSendToRec3())).append("</InstrInf>");
//      if (!Util.isEmpty(swift.getSendToRec4())) {
//        sb.append("				<InstrInf>").append(Util.replaceNull(swift.getSendToRec4())).append("</InstrInf>");
//      }
      sb.append("			</InstrForCdtrAgt>");
    }
    sb.append("			<RgltryRptg>");
    sb.append("				<DbtCdtRptgInd>").append("DEBT").append("</DbtCdtRptgInd>");
    sb.append("				<Authrty>");
    sb.append("					<Nm>AMLO</Nm>");
    sb.append("					<Ctry>TH</Ctry>");
    sb.append("				</Authrty>");
    sb.append("				<Dtls>");
//    if (swift.getRegalRep1().length() > 0) {
//      sb.append("					<Tp>").append(swift.getRegalRep1(), 0, 4).append("</Tp>");
//    } else {
      sb.append("					<Tp>TXID</Tp>");
//    }
    sb.append("					<Ctry>").append("TH").append("</Ctry>");
    sb.append("					<Inf>").append("0994000159510").append("</Inf>");
    sb.append("				</Dtls>");
    sb.append("			</RgltryRptg>");
    sb.append("		</CdtTrfTxInf>");
    sb.append("	</FIToFICstmrCdtTrf>");
    sb.append("</Document>");
    sb.append("</RequestPayload>");
    return sb.toString();
  }
}
