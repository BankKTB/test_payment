package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.SmartFileDetail;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.SmartFileDetailRepository;
import th.com.bloomcode.paymentservice.service.payment.SmartFileBatchService;

import java.sql.Types;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class SmartFileDetailRepositoryImpl extends MetadataJdbcRepository<SmartFileDetail, Long>
    implements SmartFileDetailRepository {

  static BeanPropertyRowMapper<SmartFileDetail> beanPropertyRowMapper =
      new BeanPropertyRowMapper<>(SmartFileDetail.class);
  static Updater<SmartFileDetail> smartFileDetailUpdater =
      (t, mapping) -> {
        mapping.put(SmartFileDetail.COLUMN_NAME_SMART_FILE_DETAIL_ID, t.getId());
        mapping.put(SmartFileDetail.COLUMN_NAME_FILE_TYPE, t.getFileType());
        mapping.put(SmartFileDetail.COLUMN_NAME_REC_TYPE, t.getRecType());
        mapping.put(SmartFileDetail.COLUMN_NAME_BATCH_NUM, t.getBatchNum());
        mapping.put(SmartFileDetail.COLUMN_NAME_REC_BANK_CODE, t.getRecBankCode());
        mapping.put(SmartFileDetail.COLUMN_NAME_REC_BRANCH_CODE, t.getRecBranchCode());
        mapping.put(SmartFileDetail.COLUMN_NAME_REC_ACCT, t.getRecAcct());
        mapping.put(SmartFileDetail.COLUMN_NAME_SEND_BANK_CODE, t.getSendBankCode());
        mapping.put(SmartFileDetail.COLUMN_NAME_SEND_BRANCH_CODE, t.getSendBranchCode());
        mapping.put(SmartFileDetail.COLUMN_NAME_SEND_ACCT, t.getSendAcct());
        mapping.put(SmartFileDetail.COLUMN_NAME_EFF_DATE, t.getEffDate());
        mapping.put(SmartFileDetail.COLUMN_NAME_PAYMENT_TYPE, t.getPaymentType());
        mapping.put(SmartFileDetail.COLUMN_NAME_SERVICE_TYPE, t.getServiceType());
        mapping.put(SmartFileDetail.COLUMN_NAME_CLEAR_HOUSE_CODE, t.getClearHouseCode());

        mapping.put(SmartFileDetail.COLUMN_NAME_TRANSFER_AMT, t.getTransferAmt());
        mapping.put(SmartFileDetail.COLUMN_NAME_REC_INFORM, t.getRecInform());
        mapping.put(SmartFileDetail.COLUMN_NAME_SEND_INFORM, t.getSendInform());
        mapping.put(SmartFileDetail.COLUMN_NAME_OTH_INFORM, t.getOthInform());
        mapping.put(SmartFileDetail.COLUMN_NAME_REF_SEQ_NUM, t.getRefSeqNum());
        mapping.put(SmartFileDetail.COLUMN_NAME_FILLER, t.getFiller());
        mapping.put(SmartFileDetail.COLUMN_NAME_TRANSFER_AMOUNT, t.getTransferAmount());
        mapping.put(SmartFileDetail.COLUMN_NAME_SMART_FILE_BATCH_ID, t.getSmartFileBatchId());
      };
  static Map<String, Integer> updaterType =
      Map.ofEntries(
          entry(SmartFileDetail.COLUMN_NAME_SMART_FILE_DETAIL_ID, Types.BIGINT),
          entry(SmartFileDetail.COLUMN_NAME_FILE_TYPE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_REC_TYPE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_BATCH_NUM, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_REC_BANK_CODE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_REC_BRANCH_CODE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_REC_ACCT, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_SEND_BANK_CODE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_SEND_BRANCH_CODE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_SEND_ACCT, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_EFF_DATE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_PAYMENT_TYPE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_SERVICE_TYPE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_CLEAR_HOUSE_CODE, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_TRANSFER_AMT, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_REC_INFORM, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_SEND_INFORM, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_OTH_INFORM, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_REF_SEQ_NUM, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_FILLER, Types.NVARCHAR),
          entry(SmartFileDetail.COLUMN_NAME_TRANSFER_AMOUNT, Types.NUMERIC),
          entry(SmartFileDetail.COLUMN_NAME_SMART_FILE_BATCH_ID, Types.BIGINT));
  private static SmartFileBatchService smartFileBatchService;
  static RowMapper<SmartFileDetail> userRowMapper =
      (rs, rowNum) ->
          new SmartFileDetail(
              rs.getLong(SmartFileDetail.COLUMN_NAME_SMART_FILE_DETAIL_ID),
              rs.getString(SmartFileDetail.COLUMN_NAME_FILE_TYPE),
              rs.getString(SmartFileDetail.COLUMN_NAME_REC_TYPE),
              rs.getString(SmartFileDetail.COLUMN_NAME_BATCH_NUM),
              rs.getString(SmartFileDetail.COLUMN_NAME_REC_BANK_CODE),
              rs.getString(SmartFileDetail.COLUMN_NAME_REC_BRANCH_CODE),
              rs.getString(SmartFileDetail.COLUMN_NAME_REC_ACCT),
              rs.getString(SmartFileDetail.COLUMN_NAME_SEND_BANK_CODE),
              rs.getString(SmartFileDetail.COLUMN_NAME_SEND_BRANCH_CODE),
              rs.getString(SmartFileDetail.COLUMN_NAME_SEND_ACCT),
              rs.getString(SmartFileDetail.COLUMN_NAME_EFF_DATE),
              rs.getString(SmartFileDetail.COLUMN_NAME_PAYMENT_TYPE),
              rs.getString(SmartFileDetail.COLUMN_NAME_SERVICE_TYPE),
              rs.getString(SmartFileDetail.COLUMN_NAME_CLEAR_HOUSE_CODE),
              rs.getString(SmartFileDetail.COLUMN_NAME_TRANSFER_AMT),
              rs.getString(SmartFileDetail.COLUMN_NAME_REC_INFORM),
              rs.getString(SmartFileDetail.COLUMN_NAME_SEND_INFORM),
              rs.getString(SmartFileDetail.COLUMN_NAME_OTH_INFORM),
              rs.getString(SmartFileDetail.COLUMN_NAME_REF_SEQ_NUM),
              rs.getString(SmartFileDetail.COLUMN_NAME_FILLER),
              rs.getBigDecimal(SmartFileDetail.COLUMN_NAME_TRANSFER_AMOUNT),
                  smartFileBatchService.findOneById(
                  rs.getLong(SmartFileDetail.COLUMN_NAME_SMART_FILE_BATCH_ID)));

  public SmartFileDetailRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, SmartFileBatchService smartFileBatchService) {
    super(
        userRowMapper,
        smartFileDetailUpdater,
        updaterType,
        SmartFileDetail.TABLE_NAME,
        SmartFileDetail.COLUMN_NAME_SMART_FILE_DETAIL_ID,
        jdbcTemplate);
    SmartFileDetailRepositoryImpl.smartFileBatchService = smartFileBatchService;
  }
}
