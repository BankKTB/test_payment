package th.com.bloomcode.paymentservice.service.impl;

import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import th.com.bloomcode.paymentservice.model.JwtBody;
import th.com.bloomcode.paymentservice.model.ReturnLogDetail;
import th.com.bloomcode.paymentservice.model.idem.CompanyCode;
import th.com.bloomcode.paymentservice.model.payment.ProposalLog;
import th.com.bloomcode.paymentservice.model.payment.ProposalLogSum;
import th.com.bloomcode.paymentservice.model.payment.ProposalReturnLog;
import th.com.bloomcode.paymentservice.model.response.ReturnLogResultResponse;
import th.com.bloomcode.paymentservice.model.xml.pain007.Document;
import th.com.bloomcode.paymentservice.model.xml.pain007.TxInf;
import th.com.bloomcode.paymentservice.service.FileTransferService;
import th.com.bloomcode.paymentservice.service.idem.CompanyCodeService;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogService;
import th.com.bloomcode.paymentservice.service.payment.ProposalLogSumService;
import th.com.bloomcode.paymentservice.service.payment.ProposalReturnLogService;
import th.com.bloomcode.paymentservice.util.SqlUtil;
import th.com.bloomcode.paymentservice.util.Util;
import th.com.bloomcode.paymentservice.util.XMLUtil;
import th.com.bloomcode.paymentservice.webservice.model.FIMessage;
import th.com.bloomcode.paymentservice.webservice.model.WSWebInfo;
import th.com.bloomcode.paymentservice.webservice.model.request.APStatusPaidRequest;

import javax.jms.Message;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class FileTransferServiceImpl implements FileTransferService {

  @Value("${sftp.host}")
  private String host;

  @Value("${sftp.port}")
  private Integer port;

  @Value("${sftp.username}")
  private String username;

  @Value("${sftp.private.key}")
  private Resource privateKey;

  @Value("${sftp.session.timeout}")
  private Integer sessionTimeout;

  @Value("${sftp.channel.timeout}")
  private Integer channelTimeout;

  @Value("${sftp.path.outgoing}")
  private String outGoing;

  @Value("${sftp.path.incoming}")
  private String inComing;

  private final CompanyCodeService companyCodeService;
  private final ProposalLogService proposalLogService;
  private final ProposalLogSumService proposalLogSumService;
  private final ProposalReturnLogService proposalReturnLogService;
  private final JdbcTemplate paymentJdbcTemplate;
  private final JmsTemplate jmsTemplate;
  private static final String CAN_UPDATE = "CAN_UPDATE";
  private static final String CANNOT_UPDATE = "CANNOT_UPDATE";

  public FileTransferServiceImpl(CompanyCodeService companyCodeService, ProposalLogService proposalLogService, ProposalLogSumService proposalLogSumService, ProposalReturnLogService proposalReturnLogService, @Qualifier("paymentJdbcTemplate") JdbcTemplate paymentJdbcTemplate, JmsTemplate jmsTemplate) {
    this.companyCodeService = companyCodeService;
    this.proposalLogService = proposalLogService;
    this.proposalLogSumService = proposalLogSumService;
    this.proposalReturnLogService = proposalReturnLogService;
    this.paymentJdbcTemplate = paymentJdbcTemplate;
    this.jmsTemplate = jmsTemplate;
  }

  @Override
  public boolean uploadFile(String localFilePath, String remoteFilePath) {
    ChannelSftp channelSftp = createChannelSftp();
    try {
      channelSftp.put(localFilePath, remoteFilePath);
      return true;
    } catch (SftpException ex) {
      log.error("Error upload file", ex);
    } finally {
      disconnectChannelSftp(channelSftp);
    }

    return false;
  }

  @Override
  public synchronized boolean uploadFile(InputStream inputStream, String paymentDate, String fileName) throws SftpException {
    ChannelSftp channelSftp = createChannelSftp();
//        channelSftp.setFilenameEncoding("TIS620");
    try {
      String directory = outGoing;

      log.info("directory : {}", directory);
      log.info("fileName : {}", fileName);
      try {
        SftpATTRS att = channelSftp.stat(directory);
        if (att != null && att.isDir()) {
          channelSftp.put(inputStream, directory + "/" + fileName);
          return true;
        }
      } catch (SftpException ex) {
        if (directory.indexOf('/') != -1) {
          mkdirs(directory.substring(0, directory.lastIndexOf('/')), channelSftp);
        }
        channelSftp.mkdir(directory);
        channelSftp.put(inputStream, directory + "/" + fileName);
      }


      return true;
    } catch (SftpException ex) {
      log.error("Error upload file", ex);
    } finally {
      disconnectChannelSftp(channelSftp);
    }

    return false;
  }

  @Override
  public synchronized boolean uploadFileBackup(InputStream inputStream, String fileName, Timestamp created) throws SftpException {
    ChannelSftp channelSftp = createChannelSftp();
//        channelSftp.setFilenameEncoding("TIS620");
    try {
      String directory = inComing + "backup/" + Util.timestampToStringReturn(created) + "_rtn";

      try {
        SftpATTRS att = channelSftp.stat(directory);
        if (att != null && att.isDir()) {
          channelSftp.put(inputStream, directory + "/" + fileName);
          return true;
        }
      } catch (SftpException ex) {
        if (directory.indexOf('/') != -1) {
          mkdirs(directory.substring(0, directory.lastIndexOf('/')), channelSftp);
        }
        channelSftp.mkdir(directory);
        channelSftp.put(inputStream, directory + "/" + fileName);
      }


      return true;
    } catch (SftpException ex) {
      log.error("Error upload file", ex);
    } finally {
      disconnectChannelSftp(channelSftp);
    }

    return false;
  }

  @Override
  public synchronized boolean uploadFileError(InputStream inputStream, String fileName) throws SftpException {
    ChannelSftp channelSftp = createChannelSftp();
//        channelSftp.setFilenameEncoding("TIS620");
    try {
      String directory = inComing + "error";

      try {
        SftpATTRS att = channelSftp.stat(directory);
        if (att != null && att.isDir()) {
          channelSftp.put(inputStream, directory + "/" + fileName);
          return true;
        }
      } catch (SftpException ex) {
        if (directory.indexOf('/') != -1) {
          mkdirs(directory.substring(0, directory.lastIndexOf('/')), channelSftp);
        }
        channelSftp.mkdir(directory);
        channelSftp.put(inputStream, directory + "/" + fileName);
      }


      return true;
    } catch (SftpException ex) {
      log.error("Error upload file", ex);
    } finally {
      disconnectChannelSftp(channelSftp);
    }

    return false;
  }

  @Override
  public boolean removeFile(String fileName) throws SftpException {
    ChannelSftp channelSftp = createChannelSftp();
//        channelSftp.setFilenameEncoding("TIS620");
    try {
      String directory = inComing;

      try {
        SftpATTRS att = channelSftp.stat(directory);
        if (att != null && att.isDir()) {
          channelSftp.rm(directory + "/" + fileName);
          return true;
        }
      } catch (SftpException ex) {
      }


      return true;
    } finally {
      disconnectChannelSftp(channelSftp);
    }

  }

  private void mkdirs(String directory, ChannelSftp c) throws SftpException {
    try {
      SftpATTRS att = c.stat(directory);
      if (att != null && att.isDir()) {
        return;
      }
    } catch (SftpException ex) {
      if (directory.indexOf('/') != -1) {
        mkdirs(directory.substring(0, directory.lastIndexOf('/')), c);
      }
      c.mkdir(directory);
    }
  }

  @Override
  public ReturnLogResultResponse downloadFile() {
    JwtBody jwt = (JwtBody) SecurityContextHolder.getContext().getAuthentication().getDetails();
    Timestamp created = new Timestamp(System.currentTimeMillis());
    ChannelSftp channelSftp = createChannelSftp();
    OutputStream outputStream;
    ReturnLogResultResponse response = new ReturnLogResultResponse();
    WSWebInfo wsWebInfo = new WSWebInfo();
    wsWebInfo.setIpAddress("127.0.0.1");
    wsWebInfo.setUserWebOnline(jwt.getSN());
    wsWebInfo.setFiArea("1000");
    wsWebInfo.setCompCode("99999");
    wsWebInfo.setPaymentCenter("9999999999");
    try {

//            String directory = "./INCOMING/QAS/";
      List<File> list = new ArrayList<>();
      Vector filelist = channelSftp.ls(inComing);
      for (int i = 0; i < filelist.size(); i++) {
        ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) filelist.get(i);
        if (!entry.getFilename().equalsIgnoreCase(".")
            && !entry.getFilename().equalsIgnoreCase("..")
            && !entry.getFilename().equalsIgnoreCase("ARCHIVE")
            && !entry.getFilename().equalsIgnoreCase("PGPEncrypt")
            && !entry.getFilename().equalsIgnoreCase("PGPEncrypt_ALCB")
            && !entry.getFilename().equalsIgnoreCase("PGPEncrypt_GSBA")
            && !entry.getFilename().equalsIgnoreCase("TmbS")
            && !entry.getFilename().equalsIgnoreCase("backup")
            && !entry.getFilename().equalsIgnoreCase("error")
        ) {
          InputStream stream = channelSftp.get(inComing + "/" + entry.getFilename());
          InputStream stream2 = channelSftp.get(inComing + "/" + entry.getFilename());
          String fileName = entry.getFilename();
          if (!"backup".equalsIgnoreCase(entry.getFilename())) {
            if (fileName.contains(".")) {
              String[] split = fileName.split("\\.");
              fileName = split[0];
            }
            if (fileName.startsWith("I") && (fileName.endsWith("R") || fileName.endsWith("S"))) {
              log.info("FOUND FILE TYPE IN HOUSE ========== > {} ", fileName);
              BufferedReader br = new BufferedReader(new InputStreamReader(stream));
              if (fileName.endsWith("R")) {
                this.updateReturnFileInHouse(br, response.getDetails(), entry.getFilename(), created, jwt.getSub(), wsWebInfo);
              }
              boolean success = this.uploadFileBackup(stream2, fileName, created);
              br.close();
              boolean delete = this.removeFile(entry.getFilename());
              log.info("status : {}", delete);
            } else if (fileName.startsWith("GA") || fileName.startsWith("BMC")) {
              log.info("FOUND FILE TYPE GIRO ========== > {} ", fileName);
              BufferedReader br = new BufferedReader(new InputStreamReader(stream));
              log.info("bufferReader : {}", br);
              this.updateReturnFileGiro(br, response.getDetails(), entry.getFilename(), created, jwt.getSub(), wsWebInfo);
              boolean success = this.uploadFileBackup(stream2, fileName, created);
              br.close();
              boolean delete = this.removeFile(entry.getFilename());
              log.info("status : {}", delete);
            }
//                        else if (fileEntry.getName().indexOf(".r") > -1) {
//                            File smartFile = new File(url + "\\" + fileEntry.getName());
//                            if (smartFile.exists()) {
//                                log.info("FOUND FILE TYPE SMART ========== > {} ", fileName);
//                                BufferedReader br = new BufferedReader(new FileReader(smartFile));
//                                this.updateReturnFileSmart(br, listProposal, fileEntry.getName());
//                                br.close();
//                            }
//                        }
            else if (entry.getFilename().contains("-AR-") || entry.getFilename().contains("-IR-") || entry.getFilename().contains("-ar-") || entry.getFilename().contains("-ir-")) {
              log.info("FOUND FILE TYPE SMART ========== > {} ", fileName);
              InputStream checkData = channelSftp.get(inComing + "/" + entry.getFilename());
              BufferedReader br = new BufferedReader(new InputStreamReader(checkData));
              String text = br.readLine();
              if (null != text && !text.startsWith("<SecureMessage><SenderId>")) {
                JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                Document document = (Document) jaxbUnmarshaller.unmarshal(stream);
//                            if (checkReturnFileSmart(document)) {
                this.updateReturnFileSmart(document, response.getDetails(), entry.getFilename(), created, jwt.getSub(), wsWebInfo);
                boolean success = this.uploadFileBackup(stream2, fileName, created);
//                            } else {
//                                ReturnLogError error = new ReturnLogError();
//                                error.setFileName(fileName);
//                                error.setError("ไม่สามารถอ่านไฟล์ได้เนื่องจากมีอักขระที่อ่านไม่ได้หรือไม่มีค่า");
//                                response.addErrors(error);
//                                boolean success = this.uploadFileError(stream2, fileName);
//                            }
                boolean delete = this.removeFile(entry.getFilename());
                log.info("status : {}", delete);
              }
              br.close();
            } else if (fileName.endsWith("_r") || fileName.endsWith("_c")) {
              boolean success = this.uploadFileBackup(stream2, fileName, created);
              boolean delete = this.removeFile(entry.getFilename());
            }
          }
          stream.close();
          stream2.close();
        }


      }
//            for (File fileEntry : list) {
//                if (!fileEntry.getName().equalsIgnoreCase(".") && !fileEntry.getName().equalsIgnoreCase("..")) {
//
//                    File inHouseFile = new File(fileEntry.getName());
//                    BufferedReader br = new BufferedReader(new FileReader(inHouseFile));
//                    String strLine = "";
//                    while ((strLine = br.readLine()) != null) {
//                        System.out.println(strLine);
//                    }
//                }
//
//
//            }
//            File file = new File(localFilePath);
//            outputStream = new FileOutputStream(file);
//            channelSftp.get(remoteFilePath, outputStream);
//            file.createNewFile();
      return response;
    } catch (SftpException ex) {
      log.error("Error download file", ex);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      disconnectChannelSftp(channelSftp);
    }

    return response;
  }

  @Override
  public ReturnLogResultResponse downloadFileJob(String username) {
    WSWebInfo wsWebInfo = new WSWebInfo();
    wsWebInfo.setIpAddress("127.0.0.1");
    wsWebInfo.setUserWebOnline(username);
    wsWebInfo.setFiArea("1000");
    wsWebInfo.setCompCode("99999");
    wsWebInfo.setPaymentCenter("9999999999");
    Timestamp created = new Timestamp(System.currentTimeMillis());
    ChannelSftp channelSftp = createChannelSftp();
    OutputStream outputStream;
    ReturnLogResultResponse response = new ReturnLogResultResponse();
    try {

//            String directory = "./INCOMING/QAS/";
      List<File> list = new ArrayList<>();
      Vector filelist = channelSftp.ls(inComing);
      for (int i = 0; i < filelist.size(); i++) {
        ChannelSftp.LsEntry entry = (ChannelSftp.LsEntry) filelist.get(i);
        if (!entry.getFilename().equalsIgnoreCase(".")
            && !entry.getFilename().equalsIgnoreCase("..")
            && !entry.getFilename().equalsIgnoreCase("ARCHIVE")
            && !entry.getFilename().equalsIgnoreCase("PGPEncrypt")
            && !entry.getFilename().equalsIgnoreCase("PGPEncrypt_ALCB")
            && !entry.getFilename().equalsIgnoreCase("PGPEncrypt_GSBA")
            && !entry.getFilename().equalsIgnoreCase("TmbS")
            && !entry.getFilename().equalsIgnoreCase("backup")
            && !entry.getFilename().equalsIgnoreCase("error")
        ) {
          InputStream stream = channelSftp.get(inComing + "/" + entry.getFilename());
          InputStream stream2 = channelSftp.get(inComing + "/" + entry.getFilename());
          String fileName = entry.getFilename();
          if (!"backup".equalsIgnoreCase(entry.getFilename())) {
            if (fileName.contains(".")) {
              String[] split = fileName.split("\\.");
              fileName = split[0];
            }
            if (fileName.startsWith("I") && (fileName.endsWith("R") || fileName.endsWith("S"))) {
              log.info("FOUND FILE TYPE IN HOUSE ========== > {} ", fileName);
              BufferedReader br = new BufferedReader(new InputStreamReader(stream));
              if (fileName.endsWith("R")) {
                this.updateReturnFileInHouse(br, response.getDetails(), entry.getFilename(), created, username, wsWebInfo);
              }
              boolean success = this.uploadFileBackup(stream2, fileName, created);
              br.close();
              boolean delete = this.removeFile(entry.getFilename());
              log.info("status : {}", delete);
            } else if (fileName.startsWith("GA") || fileName.startsWith("BMC")) {
              log.info("FOUND FILE TYPE GIRO ========== > {} ", fileName);
              BufferedReader br = new BufferedReader(new InputStreamReader(stream));
              log.info("bufferReader : {}", br);
              this.updateReturnFileGiro(br, response.getDetails(), entry.getFilename(), created, username, wsWebInfo);
              boolean success = this.uploadFileBackup(stream2, fileName, created);
              br.close();
              boolean delete = this.removeFile(entry.getFilename());
              log.info("status : {}", delete);
            }
//                        else if (fileEntry.getName().indexOf(".r") > -1) {
//                            File smartFile = new File(url + "\\" + fileEntry.getName());
//                            if (smartFile.exists()) {
//                                log.info("FOUND FILE TYPE SMART ========== > {} ", fileName);
//                                BufferedReader br = new BufferedReader(new FileReader(smartFile));
//                                this.updateReturnFileSmart(br, listProposal, fileEntry.getName());
//                                br.close();
//                            }
//                        }
            else if (entry.getFilename().contains("-AR-") || entry.getFilename().contains("-IR-") || entry.getFilename().contains("-ar-") || entry.getFilename().contains("-ir-")) {
              log.info("FOUND FILE TYPE SMART ========== > {} ", fileName);
              JAXBContext jaxbContext = JAXBContext.newInstance(Document.class);

              Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
              Document document = (Document) jaxbUnmarshaller.unmarshal(stream);
//                            if (checkReturnFileSmart(document)) {
              this.updateReturnFileSmart(document, response.getDetails(), entry.getFilename(), created, username, wsWebInfo);
              boolean success = this.uploadFileBackup(stream2, fileName, created);
//                            } else {
//                                ReturnLogError error = new ReturnLogError();
//                                error.setFileName(fileName);
//                                error.setError("ไม่สามารถอ่านไฟล์ได้เนื่องจากมีอักขระที่อ่านไม่ได้หรือไม่มีค่า");
//                                response.addErrors(error);
//                                boolean success = this.uploadFileError(stream2, fileName);
//                            }

              boolean delete = this.removeFile(entry.getFilename());
              log.info("status : {}", delete);
            }
//                        boolean delete = this.removeFile(entry.getFilename());

          }
        }


      }
//            for (File fileEntry : list) {
//                if (!fileEntry.getName().equalsIgnoreCase(".") && !fileEntry.getName().equalsIgnoreCase("..")) {
//
//                    File inHouseFile = new File(fileEntry.getName());
//                    BufferedReader br = new BufferedReader(new FileReader(inHouseFile));
//                    String strLine = "";
//                    while ((strLine = br.readLine()) != null) {
//                        System.out.println(strLine);
//                    }
//                }
//
//
//            }
//            File file = new File(localFilePath);
//            outputStream = new FileOutputStream(file);
//            channelSftp.get(remoteFilePath, outputStream);
//            file.createNewFile();
      return response;
    } catch (SftpException ex) {
      log.error("Error download file", ex);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      disconnectChannelSftp(channelSftp);
    }

    return response;
  }

  private ChannelSftp createChannelSftp() {
    try {
      JSch jSch = new JSch();
      jSch.addIdentity(privateKey.getURL().getPath());
      Session session = jSch.getSession(username, host, port);
      session.setConfig("StrictHostKeyChecking", "no");
      session.connect(sessionTimeout);
      Channel channel = session.openChannel("sftp");
      channel.connect(channelTimeout);
      return (ChannelSftp) channel;
    } catch (JSchException | IOException ex) {
      log.error("Create ChannelSftp error", ex);
    }

    return null;
  }

  private void disconnectChannelSftp(ChannelSftp channelSftp) {
    try {
      if (channelSftp == null)
        return;

      if (channelSftp.isConnected())
        channelSftp.disconnect();

      if (channelSftp.getSession() != null)
        channelSftp.getSession().disconnect();

    } catch (Exception ex) {
      log.error("SFTP disconnect error", ex);
    }
  }

  private List<ReturnLogDetail> updateReturnFileInHouse(BufferedReader br, List<ReturnLogDetail> resultList, String fileName, Timestamp created, String createdBy, WSWebInfo wsWebInfo) throws Exception {
    String transferDate = "";
    String document = "";
    String paymentDate = "";
    String paymentName = "";
    boolean isSumFile = false;
    List<ProposalReturnLog> proposalReturnLogsInsert = new ArrayList<>();
    List<ProposalReturnLog> proposalReturnLogsUpdate = new ArrayList<>();
    List<ProposalLog> proposalLogsUpdate = new ArrayList<>();
    List<ProposalLogSum> proposalLogSumsUpdate = new ArrayList<>();
    List<ProposalLogSum> listProposalLogSumInh = null;
    List<ProposalLog> listProposalLogInHouse = null;
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = br.readLine()) != null) {
      if (!line.startsWith("T")) {
        lines.add(line);
      }
    }
    if (!lines.isEmpty() && lines.size() >= 2) {
      if (lines.get(0).startsWith("H")) {
        StringBuilder lineHead = new StringBuilder(lines.get(0).trim());
        StringBuilder transferDateBuilder = new StringBuilder();
        lineHead.reverse();
        transferDateBuilder.append(lineHead.substring(0, 94));
        transferDateBuilder.reverse();
        transferDate = transferDateBuilder.substring(0, 6);
      }
      if (lines.get(1).startsWith("D")) {
        document = lines.get(1).substring(62, 72).trim();
        paymentDate = lines.get(1).substring(76, 82).trim();
        paymentName = lines.get(1).substring(82, 87).trim();
      }
      String paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + "20" + paymentDate.substring(4, 6);
      Map<String, Object> params = new HashMap<>();
      params.put("paymentDate", paymentDateConvert);
      params.put("paymentName", paymentName);
      params.put("fileType", "INHOU");
      if (Util.isEmpty(document)) {
        isSumFile = true;
        listProposalLogSumInh = proposalLogSumService.findAllProposalLogByReturnFile(params);
      } else {
        listProposalLogInHouse = proposalLogService.findAllProposalLogByReturnFile(params);
      }
      for (String strLine : lines) {
        if (strLine.startsWith("H")) {
          StringBuilder lineHead = new StringBuilder(strLine.trim());
          StringBuilder transferDateBuilder = new StringBuilder();
          lineHead.reverse();
          transferDateBuilder.append(lineHead.substring(0, 94));
          transferDateBuilder.reverse();
          transferDate = transferDateBuilder.substring(0, 6);
        }
        if (strLine.startsWith("D")) {
          String logType = CANNOT_UPDATE;
          String bankCode = strLine.substring(7, 10).trim();
          String bankAccount = strLine.substring(10, 25).trim();
          String status = strLine.substring(41, 42).trim();
          String vendorCode = strLine.substring(48, 58).trim();
          String paymentCompCode = strLine.substring(58, 62).trim();
          if ("9999".equalsIgnoreCase(paymentCompCode)) {
            paymentCompCode = paymentCompCode + "9";
          } else {
            CompanyCode compCodeObj = companyCodeService.findOneByOldValueCode("1302");
            if (null != compCodeObj) {
              paymentCompCode = compCodeObj.getValueCode();
            }
          }
          String paymentDocument = strLine.substring(62, 72).trim();
          String paymentFiscalYear = strLine.substring(72, 76).trim();
          paymentDate = strLine.substring(76, 82).trim();
          paymentName = strLine.substring(82, 87).trim();
          String fiArea = strLine.substring(87, 91).trim();
          paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + "20" + paymentDate.substring(4, 6);
          String transferDateConvert = transferDate.substring(0, 2) + "-" + transferDate.substring(2, 4) + "-" + "20" + transferDate.substring(4, 6);
          params = new HashMap<>();
          params.put("bankKey", bankCode);
          params.put("bankAccount", bankAccount);
          params.put("vendorCode", vendorCode);
          params.put("paymentCompCode", paymentCompCode);
          params.put("paymentDocument", paymentDocument);
          params.put("paymentFiscalYear", paymentFiscalYear);
          params.put("paymentDate", paymentDateConvert);
          params.put("transferDate", transferDateConvert);
          params.put("paymentName", paymentName);
          params.put("fiArea", fiArea);
          params.put("fileType", "INHOU");
          if (isSumFile) {
            Optional<ProposalLogSum> optionalProposalLog = listProposalLogSumInh.stream().filter(o -> o.getBankKey().substring(0, 3).equalsIgnoreCase(bankCode)
                && o.getVendorBankAccount().equalsIgnoreCase(bankAccount)
                && o.getVendor().equalsIgnoreCase(vendorCode)).findFirst();
            log.info("FOUND !!!! proposalLog : {}, {}, {}, {} ", optionalProposalLog.isPresent(), bankCode, bankAccount, vendorCode);
            if (optionalProposalLog.isPresent()) {
              ProposalLogSum proposalLogSum = optionalProposalLog.get();
//              log.info("FOUND !!!! proposalLog : {} ", proposalLogSum.getId());
              List<ProposalLog> listProposalLog = proposalLogService.findAllProposalLogByReturnFileSum(proposalLogSum.getRefRunning(), proposalLogSum.getRefLine());
              for (ProposalLog proposalLog : listProposalLog) {
                if ("0".equalsIgnoreCase(status)) {
                  if (this.checkUpdateStatusComplete(proposalLogSum)) {
                    proposalLogSum.setFileStatus("C");
                    proposalLogSum.setReturnBy(createdBy);
                    proposalLogSum.setReturnDate(created);
                    proposalLog.setFileStatus("C");
                    proposalLog.setReturnBy(createdBy);
                    proposalLog.setReturnDate(created);
                    proposalLogsUpdate.add(proposalLog);
                    logType = CAN_UPDATE;
                  }
                } else {
                  if (this.checkUpdateStatusReturn(proposalLogSum)) {
                    proposalLogSum.setFileStatus("R");
                    proposalLogSum.setReturnBy(createdBy);
                    proposalLogSum.setReturnDate(created);
                    proposalLog.setFileStatus("R");
                    proposalLog.setReturnBy(createdBy);
                    proposalLog.setReturnDate(created);
                    proposalLogsUpdate.add(proposalLog);
                    ProposalReturnLog checkDuplicate = proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);

                    if (checkDuplicate == null) {
                      ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                      proposalReturnLog.setId(proposalReturnId++);
                      proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                      proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                      proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                      proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                      proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                      proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                      proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                      proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                      proposalReturnLog.setVendor(proposalLog.getVendor());
                      proposalReturnLog.setBankKey(proposalLog.getBankKey());
                      proposalReturnLog.setFileName(proposalLog.getFileName());
                      proposalReturnLog.setTransferDate(proposalLog.getTransferDate());
                      proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                      proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                      proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                      proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                      proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                      proposalReturnLogsInsert.add(proposalReturnLog);
                    } else {
                      checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                      checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                      checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                      checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                      checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                      checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                      checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                      checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                      checkDuplicate.setVendor(proposalLog.getVendor());
                      checkDuplicate.setBankKey(proposalLog.getBankKey());
                      checkDuplicate.setFileName(proposalLog.getFileName());
                      checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                      checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                      checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                      proposalReturnLogsUpdate.add(checkDuplicate);
                    }
                    logType = CAN_UPDATE;
                  }
                }

                sendMQUpdate(proposalLog, wsWebInfo);
                ReturnLogDetail propLogResult = this.setProposalLogToResponse(proposalLog, logType);
                resultList.add(propLogResult);
              }
              proposalLogSumsUpdate.add(proposalLogSum);

              log.info("SAVE proposalLog SUCCESS IN HOUSE : {} ", proposalLogSum.getId());

            }
          } else {
            Optional<ProposalLog> optionalProposalLog = listProposalLogInHouse.stream().filter(o -> o.getPaymentDocument().equalsIgnoreCase(paymentDocument)).findFirst();
            if (optionalProposalLog.isPresent()) {
              ProposalLog proposalLog = optionalProposalLog.get();
              log.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
              if ("0".equalsIgnoreCase(status)) {
                if (this.checkUpdateStatusComplete(proposalLog)) {
                  proposalLog.setFileStatus("C");
                  proposalLog.setReturnBy(createdBy);
                  proposalLog.setReturnDate(created);
                  proposalLogsUpdate.add(proposalLog);
                  logType = CAN_UPDATE;
                }
              } else {
                if (this.checkUpdateStatusReturn(proposalLog)) {
                  proposalLog.setFileStatus("R");
                  proposalLog.setReturnBy(createdBy);
                  proposalLog.setReturnDate(created);
                  proposalLogsUpdate.add(proposalLog);
                  ProposalReturnLog checkDuplicate = proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);

                  if (checkDuplicate == null) {
                    ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                    proposalReturnLog.setId(proposalReturnId++);
                    proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                    proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                    proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                    proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                    proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                    proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                    proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                    proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                    proposalReturnLog.setVendor(proposalLog.getVendor());
                    proposalReturnLog.setBankKey(proposalLog.getBankKey());
                    proposalReturnLog.setFileName(proposalLog.getFileName());
                    proposalReturnLog.setTransferDate(proposalLog.getTransferDate());
                    proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                    proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                    proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                    proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                    proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                    proposalReturnLogsInsert.add(proposalReturnLog);
//                                        proposalReturnLogService.save(proposalReturnLog);
                  } else {
                    checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                    checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                    checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                    checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                    checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                    checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                    checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                    checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                    checkDuplicate.setVendor(proposalLog.getVendor());
                    checkDuplicate.setBankKey(proposalLog.getBankKey());
                    checkDuplicate.setFileName(proposalLog.getFileName());
                    checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                    checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                    checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                    proposalReturnLogsUpdate.add(checkDuplicate);
                  }
                  logType = CAN_UPDATE;
                }
              }
              log.info("SAVE proposalLog SUCCESS IN HOUSE : {} ", proposalLog.getId());
              sendMQUpdate(proposalLog, wsWebInfo);
              ReturnLogDetail propLogResult = this.setProposalLogToResponse(proposalLog, logType);
              resultList.add(propLogResult);
            }
          }
        }
      }

      Long proposalReturnId = SqlUtil.getNextSeries(paymentJdbcTemplate, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ, (long) proposalReturnLogsInsert.size());
//      SqlUtil.updateNextSeries(paymentJdbcTemplate, proposalReturnId, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ);
      for (ProposalReturnLog proposalReturnLog : proposalReturnLogsInsert) {
        proposalReturnLog.setId(proposalReturnId++);
      }
      proposalReturnLogService.saveBatch(proposalReturnLogsInsert);
      proposalReturnLogService.updateBatch(proposalReturnLogsUpdate);
      proposalLogService.updateBatch(proposalLogsUpdate);
      proposalLogSumService.updateBatch(proposalLogSumsUpdate);
    }
    return resultList;
  }

  private List<ReturnLogDetail> updateReturnFileGiro(BufferedReader br, List<ReturnLogDetail> resultList, String fileName, Timestamp created, String createdBy, WSWebInfo wsWebInfo) throws Exception {
    String transferDate = "";
    String document = "";
    String paymentDate = "";
    String paymentName = "";
    boolean isSumFile = false;
    List<ProposalReturnLog> proposalReturnLogsInsert = new ArrayList<>();
    List<ProposalReturnLog> proposalReturnLogsUpdate = new ArrayList<>();
    List<ProposalLog> proposalLogsUpdate = new ArrayList<>();
    List<ProposalLogSum> proposalLogSumsUpdate = new ArrayList<>();
    List<ProposalLogSum> listProposalLogSumGiro = null;
    List<ProposalLog> listProposalLogGiro = null;
    List<String> lines = new ArrayList<>();
    String line;
    while ((line = br.readLine()) != null) {
      if (!line.startsWith("T")) {
        lines.add(line);
      }
    }
    if (!lines.isEmpty() && lines.size() >= 2) {
      if (lines.get(0).startsWith("H")) {
        StringBuilder lineHead = new StringBuilder(lines.get(0).trim());
        StringBuilder transferDateBuilder = new StringBuilder();
        lineHead.reverse();
        transferDateBuilder.append(lineHead.substring(0, 94));
        transferDateBuilder.reverse();
        transferDate = transferDateBuilder.substring(0, 6);
      }
      if (lines.get(1).startsWith("D")) {
        document = lines.get(1).substring(57, 67).trim();
        paymentDate = lines.get(1).substring(71, 77).trim();
        paymentName = lines.get(1).substring(77, 82).trim();
      }
      String paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + "20" + paymentDate.substring(4, 6);
      Map<String, Object> params = new HashMap<>();
      params.put("paymentDate", paymentDateConvert);
      params.put("paymentName", paymentName);
      params.put("fileType", "GIRO");
      if (Util.isEmpty(document)) {
        isSumFile = true;
        listProposalLogSumGiro = proposalLogSumService.findAllProposalLogByReturnFile(params);
      } else {
        listProposalLogGiro = proposalLogService.findAllProposalLogByReturnFile(params);
      }

      for (String strLine : lines) {
        if (strLine.startsWith("H")) {
          StringBuilder lineHead = new StringBuilder(strLine.trim());
          StringBuilder transferDateBuilder = new StringBuilder();
          lineHead.reverse();
          transferDateBuilder.append(lineHead.substring(0, 94));
          transferDateBuilder.reverse();
          transferDate = transferDateBuilder.substring(0, 6);
        }
        if (strLine.startsWith("D")) {
          String logType = CANNOT_UPDATE;
          String bankCode = strLine.substring(7, 10).trim();
          String bankAccount = strLine.substring(10, 20).trim();
          String status = strLine.substring(36, 37).trim();
          String vendorCode = strLine.substring(43, 53).trim();
          String paymentCompCode = strLine.substring(53, 57).trim();
          if ("9999".equalsIgnoreCase(paymentCompCode)) {
            paymentCompCode = paymentCompCode + "9";
          } else {
            CompanyCode compCodeObj = companyCodeService.findOneByOldValueCode("1302");
            if (null != compCodeObj) {
              paymentCompCode = compCodeObj.getValueCode();
            }
          }
          String paymentDocument = strLine.substring(57, 67).trim();
          String paymentFiscalYear = strLine.substring(67, 71).trim();
          String fiArea = strLine.substring(82, 86).trim();
          String transferDateConvert = transferDate.substring(0, 2) + "-" + transferDate.substring(2, 4) + "-" + "20" + transferDate.substring(4, 6);
          params.put("bankKey", bankCode);
          params.put("bankAccount", bankAccount);
          params.put("vendorCode", vendorCode);
          params.put("paymentCompCode", paymentCompCode);
          params.put("paymentDocument", paymentDocument);
          params.put("paymentFiscalYear", paymentFiscalYear);
          params.put("paymentDate", paymentDateConvert);
          params.put("transferDate", transferDateConvert);
          params.put("paymentName", paymentName);
          params.put("fiArea", fiArea);
          params.put("fileType", "GIRO");
          if (isSumFile) {
            Optional<ProposalLogSum> optionalProposalLog = listProposalLogSumGiro.stream().filter(o -> o.getBankKey().substring(0, 3).equalsIgnoreCase(bankCode)
                && o.getVendorBankAccount().equalsIgnoreCase(bankAccount)
                && o.getVendor().equalsIgnoreCase(vendorCode)).findAny();
            if (optionalProposalLog.isPresent()) {
              ProposalLogSum proposalLogSum = optionalProposalLog.get();
              log.info("FOUND !!!! proposalLog : {} ", proposalLogSum.getId());
              List<ProposalLog> listProposalLog = proposalLogService.findAllProposalLogByReturnFileSum(proposalLogSum.getRefRunning(), proposalLogSum.getRefLine());
              for (ProposalLog proposalLog : listProposalLog) {
                if ("0".equalsIgnoreCase(status)) {
                  if (this.checkUpdateStatusComplete(proposalLogSum)) {
                    proposalLogSum.setFileStatus("C");
                    proposalLogSum.setReturnBy(createdBy);
                    proposalLogSum.setReturnDate(created);
                    proposalLog.setFileStatus("C");
                    proposalLog.setReturnBy(createdBy);
                    proposalLog.setReturnDate(created);
                    proposalLogsUpdate.add(proposalLog);
                    logType = CAN_UPDATE;
                  }
                } else {
                  if (this.checkUpdateStatusReturn(proposalLogSum)) {
                    proposalLogSum.setFileStatus("R");
                    proposalLogSum.setReturnBy(createdBy);
                    proposalLogSum.setReturnDate(created);
                    proposalLog.setFileStatus("R");
                    proposalLog.setReturnBy(createdBy);
                    proposalLog.setReturnDate(created);
                    proposalLogsUpdate.add(proposalLog);
                    ProposalReturnLog checkDuplicate = proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);

                    if (checkDuplicate == null) {
                      ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                      proposalReturnLog.setId(proposalReturnId++);
                      proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                      proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                      proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                      proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                      proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                      proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                      proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                      proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                      proposalReturnLog.setVendor(proposalLog.getVendor());
                      proposalReturnLog.setBankKey(proposalLog.getBankKey());
                      proposalReturnLog.setFileName(proposalLog.getFileName());
                      proposalReturnLog.setTransferDate(proposalLog.getTransferDate());
                      proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                      proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                      proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                      proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                      proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                      proposalReturnLogsInsert.add(proposalReturnLog);
//                                        proposalReturnLogService.save(proposalReturnLog);
                    } else {
                      checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                      checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                      checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                      checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                      checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                      checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                      checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                      checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                      checkDuplicate.setVendor(proposalLog.getVendor());
                      checkDuplicate.setBankKey(proposalLog.getBankKey());
                      checkDuplicate.setFileName(proposalLog.getFileName());
                      checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                      checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                      checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                      proposalReturnLogsUpdate.add(checkDuplicate);
//                                        proposalReturnLogService.save(checkDuplicate);
                    }
                    logType = CAN_UPDATE;
                  }
                }

                sendMQUpdate(proposalLog, wsWebInfo);
                ReturnLogDetail propLogResult = this.setProposalLogToResponse(proposalLog, logType);
                resultList.add(propLogResult);
              }
              proposalLogSumsUpdate.add(proposalLogSum);

              log.info("SAVE proposalLog SUCCESS GIRO : {} ", proposalLogSum.getId());
            }
          } else {
            Optional<ProposalLog> optionalProposalLog = listProposalLogGiro.stream().filter(o -> o.getPaymentDocument().equalsIgnoreCase(paymentDocument)).findAny();
            if (optionalProposalLog.isPresent()) {
              ProposalLog proposalLog = optionalProposalLog.get();
              log.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
              if ("0".equalsIgnoreCase(status)) {
                if (this.checkUpdateStatusComplete(proposalLog)) {
                  proposalLog.setFileStatus("C");
                  proposalLog.setReturnBy(createdBy);
                  proposalLog.setReturnDate(created);
                  proposalLogsUpdate.add(proposalLog);
                  logType = CAN_UPDATE;
                }
              } else {
                if (this.checkUpdateStatusReturn(proposalLog)) {
                  proposalLog.setFileStatus("R");
                  proposalLog.setReturnBy(createdBy);
                  proposalLog.setReturnDate(created);
                  proposalLogsUpdate.add(proposalLog);
                  ProposalReturnLog checkDuplicate = proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);

                  if (checkDuplicate == null) {
                    ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                    proposalReturnLog.setId(proposalReturnId++);
                    proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                    proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                    proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                    proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                    proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                    proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                    proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                    proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                    proposalReturnLog.setVendor(proposalLog.getVendor());
                    proposalReturnLog.setBankKey(proposalLog.getBankKey());
                    proposalReturnLog.setFileName(proposalLog.getFileName());
                    proposalReturnLog.setTransferDate(proposalLog.getTransferDate());
                    proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                    proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                    proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                    proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                    proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                    proposalReturnLogsInsert.add(proposalReturnLog);
                  } else {
                    checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                    checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                    checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                    checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                    checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                    checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                    checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                    checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                    checkDuplicate.setVendor(proposalLog.getVendor());
                    checkDuplicate.setBankKey(proposalLog.getBankKey());
                    checkDuplicate.setFileName(proposalLog.getFileName());
                    checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                    checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                    checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());

                    proposalReturnLogsUpdate.add(checkDuplicate);
                  }
                  logType = CAN_UPDATE;
                }
              }
              log.info("SAVE proposalLog SUCCESS GIRO : {} ", proposalLog.getId());
              sendMQUpdate(proposalLog, wsWebInfo);
              ReturnLogDetail propLogResult = this.setProposalLogToResponse(proposalLog, logType);
              resultList.add(propLogResult);
            }
          }
        }
      }

      Long proposalReturnId = SqlUtil.getNextSeries(paymentJdbcTemplate, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ, (long) proposalReturnLogsInsert.size());
//      SqlUtil.updateNextSeries(paymentJdbcTemplate, proposalReturnId, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ);
      for (ProposalReturnLog proposalReturnLog : proposalReturnLogsInsert) {
        proposalReturnLog.setId(proposalReturnId++);
      }
      proposalReturnLogService.saveBatch(proposalReturnLogsInsert);
      proposalReturnLogService.updateBatch(proposalReturnLogsUpdate);
      proposalLogService.updateBatch(proposalLogsUpdate);
      proposalLogSumService.updateBatch(proposalLogSumsUpdate);
    }
    return resultList;
  }

  private List<ReturnLogDetail> updateReturnFileSmart(Document document, List<ReturnLogDetail> resultList, String fileName, Timestamp created, String createdBy, WSWebInfo wsWebInfo) throws Exception {
    List<ProposalLog> proposalLogsUpdate = new ArrayList<>();
    List<ProposalReturnLog> proposalReturnLogsInsert = new ArrayList<>();
    List<ProposalReturnLog> proposalReturnLogsUpdate = new ArrayList<>();
    String fileOriginalName = document.getCstmrPmtRvsl().getOrgnlGrpInf().getOrgnlMsgId();
    String effectiveDateHeader = document.getCstmrPmtRvsl().getOrgnlGrpInf().getOrgnlMsgId().substring(7, 15);
    String effectiveDateConvertHeader = effectiveDateHeader.substring(6, 8) + "-" + effectiveDateHeader.substring(4, 6) + "-" + effectiveDateHeader.substring(0, 4);
    if (null != document.getCstmrPmtRvsl().getOrgnlPmtInfAndRvsl()) {
      List<TxInf> txInfs = document.getCstmrPmtRvsl().getOrgnlPmtInfAndRvsl().getTxInf();
      for (TxInf txInf : txInfs) {
        Map<String, Object> params = new HashMap<>();
        String inform = null != txInf.getOrgnlTxRef().getCdtr().getPty() ? txInf.getOrgnlTxRef().getCdtr().getPty().getNm() : null;
        String reasonCode = txInf.getRvslRsnInf().getRsn().getCd();
        String reasonText = txInf.getRvslRsnInf().getAddtlInf();
        if (null == inform || (inform.length() < 50 && inform.length() != 36)) {
          continue;
        }
        String vendorCode = "";
        String paymentCompCode = "";
        String paymentDocument = "";
        String paymentFiscalYear = "";
        String paymentDate = "";
        String paymentName = "";
        if (inform.length() == 50) {
          vendorCode = inform.substring(0, 10);
          paymentCompCode = inform.substring(10, 14);
          paymentDocument = inform.substring(14, 24).trim();
          paymentFiscalYear = inform.substring(24, 28);
          paymentDate = inform.substring(28, 36);
          paymentName = inform.substring(36, 41);
        } else if (inform.length() == 36) {
          vendorCode = inform.substring(0, 10);
          paymentCompCode = inform.substring(10, 14);
          paymentFiscalYear = inform.substring(14, 18);
          paymentDate = inform.substring(18, 26);
          paymentName = inform.substring(26, 31);
        }

        if (Util.isEmpty(vendorCode) || Util.isEmpty(paymentCompCode) || Util.isEmpty(paymentFiscalYear) || Util.isEmpty(paymentDate) || Util.isEmpty(paymentName)) {
          continue;
        }

        String paymentDateConvert = paymentDate.substring(0, 2) + "-" + paymentDate.substring(2, 4) + "-" + paymentDate.substring(4, 8);
        Date effectiveDate = txInf.getOrgnlTxRef().getIntrBkSttlmDt();
        String effectiveDateConvert = Util.dateToStringPattern_ddMMyyyyWithScore(effectiveDate);
        BigDecimal amount = txInf.getOrgnlTxRef().getAmt().getInstdAmt().getText();
        params.put("bankKey", txInf.getOrgnlTxRef().getCdtrAgt().getFinInstnId().getClrSysMmbId().getMmbId());
        params.put("vendorCode", vendorCode.trim());
        params.put("paymentCompCode", paymentCompCode + "9");
        params.put("paymentDocument", paymentDocument);
        params.put("paymentFiscalYear", paymentFiscalYear.trim());
        params.put("paymentDate", paymentDateConvert);
        params.put("effectiveDateConvert", effectiveDateConvert);
        params.put("paymentName", paymentName.trim());
        params.put("fileType", "SMART");
        params.put("amount", amount);

        List<ProposalLogSum> listProposalLogSumSmart = proposalLogSumService.findAllProposalLogByReturnFile(params);
        if (Util.isEmpty(paymentDocument)) {
          ProposalLogSum proposalLogSum = listProposalLogSumSmart.get(0);
          if (null != proposalLogSum) {
            List<ProposalLog> listProposalLog = proposalLogService.findAllProposalLogByReturnFileSum(proposalLogSum.getRefRunning(), proposalLogSum.getRefLine());
            for (ProposalLog proposalLog : listProposalLog) {
              if (null != proposalLog) {
                log.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
                if (this.checkUpdateStatusReturn(proposalLog)) {
                  proposalLog.setFileStatus("R");
                  proposalLog.setReturnDate(created);
                  proposalLog.setReturnBy(createdBy);
                  proposalLog.setRevOriginalReason(reasonText);
                  ProposalReturnLog checkDuplicate = proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);

                  if (checkDuplicate == null) {
                    ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                    proposalReturnLog.setId(proposalReturnId++);
                    proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                    proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                    proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                    proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                    proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                    proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                    proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                    proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                    proposalReturnLog.setVendor(proposalLog.getVendor());
                    proposalReturnLog.setBankKey(proposalLog.getBankKey());
                    proposalReturnLog.setFileName(proposalLog.getFileName());
                    proposalReturnLog.setTransferDate(proposalLog.getTransferDate());
                    proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                    proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                    proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                    proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                    proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());
                    proposalReturnLog.setReasonCode(reasonCode);
                    proposalReturnLog.setReasonText(reasonText);

                    proposalReturnLogsInsert.add(proposalReturnLog);
                  } else {
                    checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                    checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                    checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                    checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                    checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                    checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                    checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                    checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                    checkDuplicate.setVendor(proposalLog.getVendor());
                    checkDuplicate.setBankKey(proposalLog.getBankKey());
                    checkDuplicate.setFileName(proposalLog.getFileName());
                    checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                    checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                    checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());
                    checkDuplicate.setReasonCode(reasonCode);
                    checkDuplicate.setReasonText(reasonText);
                    proposalReturnLogsUpdate.add(checkDuplicate);
                  }
                }
                proposalLogsUpdate.add(proposalLog);
                log.info("SAVE proposalLog SUCCESS SMART : {} ", proposalLog.getId());
              }
            }
          }
        } else {
          List<ProposalLog> listProposalLogSmart = proposalLogService.findAllProposalLogByReturnFile(params);
          if (listProposalLogSmart.size() > 0) {
            ProposalLog proposalLog = listProposalLogSmart.get(0);
            if (null != proposalLog) {
              log.info("FOUND !!!! proposalLog : {} ", proposalLog.getId());
              if (this.checkUpdateStatusReturn(proposalLog)) {
                proposalLog.setFileStatus("R");
                proposalLog.setReturnDate(created);
                proposalLog.setReturnBy(createdBy);
                proposalLog.setRevOriginalReason(reasonText);
                ProposalReturnLog checkDuplicate = proposalReturnLogService.findOneByInvoiceDocumentAndPaymentDocument(proposalLog);

                if (checkDuplicate == null) {
                  ProposalReturnLog proposalReturnLog = new ProposalReturnLog();
//                  proposalReturnLog.setId(proposalReturnId++);
                  proposalReturnLog.setInvoiceCompCode(proposalLog.getInvCompCode());
                  proposalReturnLog.setInvoiceDocNo(proposalLog.getInvDocNo());
                  proposalReturnLog.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                  proposalReturnLog.setPaymentCompCode(proposalLog.getPaymentCompCode());
                  proposalReturnLog.setPaymentDocNo(proposalLog.getPaymentDocument());
                  proposalReturnLog.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                  proposalReturnLog.setPaymentDate(proposalLog.getPaymentDate());
                  proposalReturnLog.setPaymentName(proposalLog.getPaymentName());
                  proposalReturnLog.setVendor(proposalLog.getVendor());
                  proposalReturnLog.setBankKey(proposalLog.getBankKey());
                  proposalReturnLog.setFileName(proposalLog.getFileName());
                  proposalReturnLog.setTransferDate(proposalLog.getTransferDate());
                  proposalReturnLog.setOriginalCompCode(proposalLog.getOriginalCompCode());
                  proposalReturnLog.setOriginalDocumentNo(proposalLog.getOriginalDocNo());
                  proposalReturnLog.setOriginalFiscalYear(proposalLog.getOriginalFiscalYear());
                  proposalReturnLog.setCreated(new Timestamp(System.currentTimeMillis()));
                  proposalReturnLog.setCreatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());
                  proposalReturnLog.setReasonCode(reasonCode);
                  proposalReturnLog.setReasonText(reasonText);

                  proposalReturnLogsInsert.add(proposalReturnLog);
                } else {
                  checkDuplicate.setInvoiceCompCode(proposalLog.getInvCompCode());
                  checkDuplicate.setInvoiceDocNo(proposalLog.getInvDocNo());
                  checkDuplicate.setInvoiceFiscalYear(proposalLog.getInvFiscalYear());
                  checkDuplicate.setPaymentCompCode(proposalLog.getPaymentCompCode());
                  checkDuplicate.setPaymentDocNo(proposalLog.getPaymentDocument());
                  checkDuplicate.setPaymentFiscalYear(proposalLog.getPaymentFiscalYear());
                  checkDuplicate.setPaymentDate(proposalLog.getPaymentDate());
                  checkDuplicate.setPaymentName(proposalLog.getPaymentName());
                  checkDuplicate.setVendor(proposalLog.getVendor());
                  checkDuplicate.setBankKey(proposalLog.getBankKey());
                  checkDuplicate.setFileName(proposalLog.getFileName());
                  checkDuplicate.setTransferDate(proposalLog.getTransferDate());
                  checkDuplicate.setUpdated(new Timestamp(System.currentTimeMillis()));
                  checkDuplicate.setUpdatedBy(proposalLog.getUpdatedBy() != null ? proposalLog.getUpdatedBy() : proposalLog.getCreatedBy());
                  checkDuplicate.setReasonCode(reasonCode);
                  checkDuplicate.setReasonText(reasonText);
                  proposalReturnLogsUpdate.add(checkDuplicate);
                }
              }
              proposalLogsUpdate.add(proposalLog);
              log.info("SAVE proposalLog SUCCESS SMART : {} ", proposalLog.getId());
            }
          }
        }
      }
//      SqlUtil.updateNextSeries(paymentJdbcTemplate, proposalReturnId, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ);
    }

    Long proposalReturnId = SqlUtil.getNextSeries(paymentJdbcTemplate, ProposalReturnLog.TABLE_NAME + ProposalReturnLog.SEQ, (long) proposalReturnLogsInsert.size());
    for (ProposalReturnLog proposalReturnLog : proposalReturnLogsInsert) {
      proposalReturnLog.setId(proposalReturnId++);
    }
    proposalLogService.updateBatch(proposalLogsUpdate);
    proposalReturnLogService.saveBatch(proposalReturnLogsInsert);
    proposalReturnLogService.updateBatch(proposalReturnLogsUpdate);

    if (!Util.isEmpty(effectiveDateConvertHeader)) {
      proposalLogService.updateComplete(effectiveDateConvertHeader, fileOriginalName, created, createdBy);
      List<ProposalLog> listProposalLogSmart = proposalLogService.findAllProposalLogByReturnFile(effectiveDateConvertHeader, fileOriginalName);
      for (ProposalLog proposalLog : listProposalLogSmart) {
        sendMQUpdate(proposalLog, wsWebInfo);
        ReturnLogDetail propLogResult = this.setProposalLogToResponse(proposalLog, CAN_UPDATE);
        resultList.add(propLogResult);
      }
    }

    if (resultList.size() < 1) {
      ReturnLogDetail propLogResult = new ReturnLogDetail();
      propLogResult.setFileName(fileName);
      log.info("effectiveDate : {} ", effectiveDateConvertHeader);
      SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
      propLogResult.setTransferDate(df.parse(effectiveDateConvertHeader));
      propLogResult.setFileStatus("SMART");
      resultList.add(propLogResult);
    }
    return resultList;
  }

  private boolean checkReturnFileSmart(Document document) throws Exception {
    log.info("document : {}", document);

    if (null != document.getCstmrPmtRvsl().getOrgnlPmtInfAndRvsl()) {
      List<TxInf> txInfs = document.getCstmrPmtRvsl().getOrgnlPmtInfAndRvsl().getTxInf();
      for (TxInf txInf : txInfs) {
        String inform = txInf.getOrgnlTxRef().getCdtr().getPty().getNm();
        log.info("inform : {}", inform);
        if (null == inform || inform.length() < 50) {
          return false;
        }
      }
    }
    return true;
  }

  private Boolean checkUpdateStatusComplete(ProposalLog proposalLog) {
    if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
      if (null == proposalLog.getRevPaymentDocument()) {
        proposalLog.setFileStatus("C");
        return true;
      } else {
        // delete prop log
        log.info("delete prop log");
        return false;
      }
    } else {
      return true;
    }
  }

  private Boolean checkUpdateStatusComplete(ProposalLogSum proposalLog) {
    if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
      if (null == proposalLog.getRevPaymentDocument()) {
        proposalLog.setFileStatus("C");
        return true;
      } else {
        // delete prop log
        log.info("delete prop log");
        return false;
      }
    } else {
      return true;
    }
  }

  private Boolean checkUpdateStatusReturn(ProposalLog proposalLog) {
    if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
      return null == proposalLog.getRevPaymentDocument();
    } else {
      return true;
    }
  }

  private Boolean checkUpdateStatusReturn(ProposalLogSum proposalLog) {
    if ("9".equalsIgnoreCase(proposalLog.getTransferLevel())) {
      return null == proposalLog.getRevPaymentDocument();
    } else {
      return true;
    }
  }

  private ReturnLogDetail setProposalLogToResponse(ProposalLog proposalLog, String logType) {
    ReturnLogDetail propLogResult = new ReturnLogDetail();
    propLogResult.setId(proposalLog.getId());
    propLogResult.setLogType(logType);
    propLogResult.setFileStatus(proposalLog.getFileStatus());
    propLogResult.setRefRunning(proposalLog.getRefRunning());
    propLogResult.setRefLine(proposalLog.getRefLine());
    propLogResult.setPaymentDate(proposalLog.getPaymentDate());
    propLogResult.setPaymentName(proposalLog.getPaymentName());
    propLogResult.setAccountNoFrom(proposalLog.getAccountNoFrom());
    propLogResult.setAccountNoTo(proposalLog.getAccountNoTo());
    propLogResult.setFileType(proposalLog.getFileType());
    propLogResult.setFileName(proposalLog.getFileName());
    propLogResult.setTransferDate(proposalLog.getTransferDate());
    propLogResult.setVendor(proposalLog.getVendor());
    propLogResult.setAmount(proposalLog.getAmount());
    propLogResult.setDocumentNo(proposalLog.getPaymentDocument());
    return propLogResult;
  }

  private String convertAmount(String amount) {
    String amountCuttedDecimal = amount.substring(0, amount.length() - 2);
    String amountDecimal = amount.substring(amount.length() - 2);
    BigDecimal newAmount = new BigDecimal(amountCuttedDecimal + "." + amountDecimal);
    log.info(" convertAmount : {} ", newAmount.toString());
    return newAmount.toString();
  }

  private void sendMQUpdate(ProposalLog proposalLog, WSWebInfo wsWebInfo) {
    try {
      APStatusPaidRequest apStatusPaidRequest = new APStatusPaidRequest();
      apStatusPaidRequest.setCompCode(proposalLog.getPayingCompCode());
      apStatusPaidRequest.setAccountDocNo(proposalLog.getPaymentDocument());
      apStatusPaidRequest.setFiscalYear(proposalLog.getPaymentFiscalYear());
      apStatusPaidRequest.setStatusPaid(proposalLog.getFileStatus());
      apStatusPaidRequest.setWebInfo(wsWebInfo);
      FIMessage fiMessage = new FIMessage();
      fiMessage.setId(proposalLog.getPayingCompCode() + "-" + proposalLog.getPaymentFiscalYear() + "-" + proposalLog.getPaymentDocument());
      fiMessage.setType(FIMessage.Type.REQUEST.getCode());
      fiMessage.setDataType(FIMessage.DataType.STATUS_PAID.getCode());
      fiMessage.setFrom(FIMessage.System.PAYMENT.getCode());
      fiMessage.setTo("99999");
      //          log.info("apPaymentRequest {}", apPaymentRequest);
      fiMessage.setData(XMLUtil.xmlMarshall(APStatusPaidRequest.class, apStatusPaidRequest));
      this.send(fiMessage);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void send(FIMessage message) throws Exception {
//    log.info("sending message : {}", XMLUtil.xmlMarshall(FIMessage.class, message));
    final AtomicReference<Message> msg = new AtomicReference<>();
    jmsTemplate.convertAndSend("99999.AP.Payment", XMLUtil.xmlMarshall(FIMessage.class, message), message1 -> {
      msg.set(message1);
      return message1;
    });
//    log.info("msg id : {}", msg.get().getJMSMessageID());
  }
}