package th.com.bloomcode.paymentservice.repository.payment.impl;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.PaymentInformation;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentAliasRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentInformationRepository;
import th.com.bloomcode.paymentservice.repository.payment.PaymentProcessRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class PaymentInformationRepositoryImpl extends MetadataJdbcRepository<PaymentInformation, Long> implements PaymentInformationRepository {

    private static PaymentAliasRepository paymentAliasRepository;
    private static PaymentProcessRepository paymentProcessRepository;
    private final JdbcTemplate jdbcTemplate;

    static BeanPropertyRowMapper<PaymentInformation> beanPropertyRowMapper = new BeanPropertyRowMapper<>(PaymentInformation.class);

    static MetadataJdbcRepository.Updater<PaymentInformation> paymentInformationUpdater = (t, mapping) -> {
        mapping.put(PaymentInformation.COLUMN_NAME_PAYMENT_INFORMATION_ID, t.getId());
        mapping.put(PaymentInformation.COLUMN_NAME_CREATED, t.getCreated());
        mapping.put(PaymentInformation.COLUMN_NAME_CREATED_BY, t.getCreatedBy());
        mapping.put(PaymentInformation.COLUMN_NAME_UPDATED, t.getUpdated());
        mapping.put(PaymentInformation.COLUMN_NAME_UPDATED_BY, t.getUpdatedBy());
        mapping.put(PaymentInformation.COLUMN_NAME_ACCOUNT_HOLDER_NAME, t.getAccountHolderName());
        mapping.put(PaymentInformation.COLUMN_NAME_ADDRESS, t.getAddress());
        mapping.put(PaymentInformation.COLUMN_NAME_CITY, t.getCity());
        mapping.put(PaymentInformation.COLUMN_NAME_COUNTRY, t.getCountry());
        mapping.put(PaymentInformation.COLUMN_NAME_COUNTRY_NAME, t.getCountryName());
        mapping.put(PaymentInformation.COLUMN_NAME_DATE_DUE, t.getDateDue());
        mapping.put(PaymentInformation.COLUMN_NAME_DATE_VALUE, t.getDateValue());
        mapping.put(PaymentInformation.COLUMN_NAME_NAME1, t.getName1());
        mapping.put(PaymentInformation.COLUMN_NAME_NAME2, t.getName2());
        mapping.put(PaymentInformation.COLUMN_NAME_NAME3, t.getName3());
        mapping.put(PaymentInformation.COLUMN_NAME_NAME4, t.getName4());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_ADDRESS, t.getPayeeAddress());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_BANK, t.getPayeeBank());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NAME, t.getPayeeBankName());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO, t.getPayeeBankAccountNo());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_BANK_KEY, t.getPayeeBankKey());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NO, t.getPayeeBankNo());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_BANK_REFERENCE, t.getPayeeBankReference());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_CITY, t.getPayeeCity());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_CODE, t.getPayeeCode());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_COUNTRY, t.getPayeeCountry());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_NAME1, t.getPayeeName1());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_NAME2, t.getPayeeName2());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_NAME3, t.getPayeeName3());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_NAME4, t.getPayeeName4());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_POSTAL_CODE, t.getPostalCode());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_TAX_ID, t.getPayeeTaxId());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYEE_TITLE, t.getPayeeTitle());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO, t.getPayingBankAccountNo());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_BANK_CODE, t.getPayingBankCode());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_BANK_COUNTRY, t.getPayingBankCountry());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_BANK_KEY, t.getPayingBankKey());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_BANK_NAME, t.getPayingBankName());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_BANK_NO, t.getPayingBankNo());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_COMPANY_CODE, t.getPayingCompanyCode());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_GL_ACCOUNT, t.getPayingGLAccount());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYING_HOUSE_BANK, t.getPayingHouseBank());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYMENT_SPECIAL_GL, t.getPaymentSpecialGL());
        mapping.put(PaymentInformation.COLUMN_NAME_POSTAL_CODE, t.getPostalCode());
        mapping.put(PaymentInformation.COLUMN_NAME_SWIFT_CODE, t.getSwiftCode());
        mapping.put(PaymentInformation.COLUMN_NAME_VENDOR_CODE, t.getVendorCode());
        mapping.put(PaymentInformation.COLUMN_NAME_VENDOR_FLAG, t.getVendorFlag());
        mapping.put(PaymentInformation.COLUMN_NAME_VENDOR_NAME, t.getVendorName());
        mapping.put(PaymentInformation.COLUMN_NAME_VENDOR_TAX_ID, t.getVendorTaxId());
        mapping.put(PaymentInformation.COLUMN_NAME_VENDOR_TITLE, t.getVendorTitle());
        mapping.put(PaymentInformation.COLUMN_NAME_PAYMENT_PROCESS_ID, t.getPaymentProcessId());
    };

    static Map<String, Integer> updaterType = Map.<String, Integer>ofEntries(
            entry(PaymentInformation.COLUMN_NAME_PAYMENT_INFORMATION_ID, Types.BIGINT),
            entry(PaymentInformation.COLUMN_NAME_CREATED, Types.TIMESTAMP),
            entry(PaymentInformation.COLUMN_NAME_CREATED_BY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_UPDATED, Types.TIMESTAMP),
            entry(PaymentInformation.COLUMN_NAME_UPDATED_BY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_ACCOUNT_HOLDER_NAME, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_ADDRESS, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_CITY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_COUNTRY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_COUNTRY_NAME, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_DATE_DUE, Types.TIMESTAMP),
            entry(PaymentInformation.COLUMN_NAME_DATE_VALUE, Types.TIMESTAMP),
            entry(PaymentInformation.COLUMN_NAME_NAME1, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_NAME2, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_NAME3, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_NAME4, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_ADDRESS, Types.NVARCHAR),
        entry(PaymentInformation.COLUMN_NAME_PAYEE_BANK, Types.NVARCHAR),
        entry(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NAME, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_BANK_KEY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NO, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_BANK_REFERENCE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_CITY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_COUNTRY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_NAME1, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_NAME2, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_NAME3, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_NAME4, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_POSTAL_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_TAX_ID, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYEE_TITLE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_BANK_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_BANK_COUNTRY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_BANK_KEY, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_BANK_NAME, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_BANK_NO, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_COMPANY_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_GL_ACCOUNT, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYING_HOUSE_BANK, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYMENT_SPECIAL_GL, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_POSTAL_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_SWIFT_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_VENDOR_CODE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_VENDOR_FLAG, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_VENDOR_NAME, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_VENDOR_TAX_ID, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_VENDOR_TITLE, Types.NVARCHAR),
            entry(PaymentInformation.COLUMN_NAME_PAYMENT_PROCESS_ID, Types.BIGINT)
    );

    static RowMapper<PaymentInformation> paymentInformationRowMapper = (rs, rowNum) -> new PaymentInformation(
            rs.getLong(PaymentInformation.COLUMN_NAME_PAYMENT_INFORMATION_ID),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_CREATED),
            rs.getString(PaymentInformation.COLUMN_NAME_CREATED_BY),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_UPDATED),
            rs.getString(PaymentInformation.COLUMN_NAME_UPDATED_BY),
            rs.getString(PaymentInformation.COLUMN_NAME_ACCOUNT_HOLDER_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_ADDRESS),
            rs.getString(PaymentInformation.COLUMN_NAME_CITY),
            rs.getString(PaymentInformation.COLUMN_NAME_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_COUNTRY_NAME),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_DATE_DUE),
            rs.getTimestamp(PaymentInformation.COLUMN_NAME_DATE_VALUE),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME1),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME2),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME3),
            rs.getString(PaymentInformation.COLUMN_NAME_NAME4),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_ADDRESS),
        rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK),
        rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_ACCOUNT_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_KEY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_BANK_REFERENCE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_CITY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME1),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME2),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME3),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_NAME4),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_POSTAL_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_TAX_ID),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYEE_TITLE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_ACCOUNT_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_COUNTRY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_KEY),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_BANK_NO),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_COMPANY_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_GL_ACCOUNT),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYING_HOUSE_BANK),
            rs.getString(PaymentInformation.COLUMN_NAME_PAYMENT_SPECIAL_GL),
            rs.getString(PaymentInformation.COLUMN_NAME_POSTAL_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_SWIFT_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_CODE),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_FLAG),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_NAME),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_TAX_ID),
            rs.getString(PaymentInformation.COLUMN_NAME_VENDOR_TITLE),
            rs.getLong(PaymentInformation.COLUMN_NAME_PAYMENT_PROCESS_ID),
            PaymentInformationRepositoryImpl.paymentProcessRepository.findById(rs.getLong(PaymentInformation.COLUMN_NAME_PAYMENT_PROCESS_ID)).orElse(null));

    public PaymentInformationRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate, PaymentAliasRepository paymentAliasRepository, PaymentProcessRepository paymentProcessRepository) {
        super(paymentInformationRowMapper, paymentInformationUpdater, updaterType, PaymentInformation.TABLE_NAME, PaymentInformation.COLUMN_NAME_PAYMENT_INFORMATION_ID, jdbcTemplate);
        PaymentInformationRepositoryImpl.paymentAliasRepository = paymentAliasRepository;
        PaymentInformationRepositoryImpl.paymentProcessRepository = paymentProcessRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public PaymentInformation findOneByPmGroupDocAndPmGroupNoAndProposalFalse(String pmGroupDoc, String pmGroupNo) {
        List<Object> params = new ArrayList<>();
        params.add(pmGroupDoc);
        params.add(pmGroupNo);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM PAYMENT_INFORMATION IM LEFT JOIN PAYMENT_PROCESS PC ON IM.PAYMENT_PROCESS_ID = PC.ID");
        sql.append(" WHERE PC.PM_GROUP_DOC = ? AND PC.PM_GROUP_NO = ?");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql find One {}", sql.toString());
        List<PaymentInformation> paymentInformations = this.jdbcTemplate.query(sql.toString(), objParams, paymentInformationRowMapper);
        if (!paymentInformations.isEmpty()) {
            return paymentInformations.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<PaymentInformation> findAllByPaymentAliasIdAndProposalFalse(Long paymentAliasId, String proposal) {
        List<Object> params = new ArrayList<>();
        params.add(paymentAliasId);
        params.add(proposal);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM PAYMENT_INFORMATION IM LEFT JOIN PAYMENT_PROCESS PC ON IM.PAYMENT_PROCESS_ID = PC.ID");
        sql.append(" WHERE PC.PAYMENT_ALIAS_ID = ? AND PC.IS_PROPOSAL = ?");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        return this.jdbcTemplate.query(sql.toString(), objParams, paymentInformationRowMapper);
    }

    @Override
    public void saveBatch(List<PaymentInformation> paymentInformations) {
        try{
            final int batchSize = 30000;
            List<List<PaymentInformation>> paymentInformationBatches = Lists.partition(paymentInformations, batchSize);
            final String sqlSave = "INSERT /*+ ENABLE_PARALLEL_DML */ INTO PAYMENT_INFORMATION (ID, ACCOUNT_HOLDER_NAME, ADDRESS, CITY, COUNTRY, COUNTRY_NAME, " +
                "DATE_DUE, DATE_VALUE, NAME1, NAME2, NAME3, NAME4, PAYEE_ADDRESS, PAYEE_BANK, PAYEE_BANK_NAME, PAYEE_BANK_ACCOUNT_NO, PAYEE_BANK_KEY, PAYEE_BANK_NO, " +
                "PAYEE_BANK_REFERENCE, PAYEE_CITY, PAYEE_CODE, PAYEE_COUNTRY, PAYEE_NAME1, PAYEE_NAME2, PAYEE_NAME3, PAYEE_NAME4, " +
                "PAYEE_POSTAL_CODE, PAYEE_TAX_ID, PAYEE_TITLE, PAYING_BANK_ACCOUNT_NO, PAYING_BANK_CODE, PAYING_BANK_COUNTRY, PAYING_BANK_KEY, " +
                "PAYING_BANK_NAME, PAYING_BANK_NO, PAYING_COMPANY_CODE, PAYING_GL_ACCOUNT, PAYING_HOUSE_BANK, PAYMENT_PROCESS_ID, " +
                "PAYMENT_SPECIAL_GL, POSTAL_CODE, SWIFT_CODE, VENDOR_CODE, VENDOR_FLAG, VENDOR_NAME, VENDOR_TAX_ID, VENDOR_TITLE, " +
                "CREATED, CREATED_BY, UPDATED, UPDATED_BY) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            for(List<PaymentInformation> batch : paymentInformationBatches) {
                this.jdbcTemplate.batchUpdate(sqlSave, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i)
                        throws SQLException {
                        int index = 0;
                        PaymentInformation paymentInformation = batch.get(i);
                        ps.setLong(++index, paymentInformation.getId());
                        ps.setString(++index, paymentInformation.getAccountHolderName());
                        ps.setString(++index, paymentInformation.getAddress());
                        ps.setString(++index, paymentInformation.getCity());
                        ps.setString(++index, paymentInformation.getCountry());
                        ps.setString(++index, paymentInformation.getCountryName());
                        ps.setTimestamp(++index, paymentInformation.getDateDue());
                        ps.setTimestamp(++index, paymentInformation.getDateValue());
                        ps.setString(++index, paymentInformation.getName1());
                        ps.setString(++index, paymentInformation.getName2());
                        ps.setString(++index, paymentInformation.getName3());
                        ps.setString(++index, paymentInformation.getName4());
                        ps.setString(++index, paymentInformation.getPayeeAddress());
                        ps.setString(++index, paymentInformation.getPayeeBank());
                        ps.setString(++index, paymentInformation.getPayeeBankName());
                        ps.setString(++index, paymentInformation.getPayeeBankAccountNo());
                        ps.setString(++index, paymentInformation.getPayeeBankKey());
                        ps.setString(++index, paymentInformation.getPayeeBankNo());
                        ps.setString(++index, paymentInformation.getPayeeBankReference());
                        ps.setString(++index, paymentInformation.getPayeeCity());
                        ps.setString(++index, paymentInformation.getPayeeCode());
                        ps.setString(++index, paymentInformation.getPayeeCountry());
                        ps.setString(++index, paymentInformation.getPayeeName1());
                        ps.setString(++index, paymentInformation.getPayeeName2());
                        ps.setString(++index, paymentInformation.getPayeeName3());
                        ps.setString(++index, paymentInformation.getPayeeName4());
                        ps.setString(++index, paymentInformation.getPayeePostalCode());
                        ps.setString(++index, paymentInformation.getPayeeTaxId());
                        ps.setString(++index, paymentInformation.getPayeeTitle());
                        ps.setString(++index, paymentInformation.getPayingBankAccountNo());
                        ps.setString(++index, paymentInformation.getPayingBankCode());
                        ps.setString(++index, paymentInformation.getPayingBankCountry());
                        ps.setString(++index, paymentInformation.getPayingBankKey());
                        ps.setString(++index, paymentInformation.getPayingBankName());
                        ps.setString(++index, paymentInformation.getPayingBankNo());
                        ps.setString(++index, paymentInformation.getPayingCompanyCode());
                        ps.setString(++index, paymentInformation.getPayingGLAccount());
                        ps.setString(++index, paymentInformation.getPayingHouseBank());
                        ps.setLong(++index, paymentInformation.getPaymentProcessId());
                        ps.setString(++index, paymentInformation.getPaymentSpecialGL());
                        ps.setString(++index, paymentInformation.getPostalCode());
                        ps.setString(++index, paymentInformation.getSwiftCode());
                        ps.setString(++index, paymentInformation.getVendorCode());
                        ps.setString(++index, paymentInformation.getVendorFlag());
                        ps.setString(++index, paymentInformation.getVendorName());
                        ps.setString(++index, paymentInformation.getVendorTaxId());
                        ps.setString(++index, paymentInformation.getVendorTitle());
                        ps.setTimestamp(++index, paymentInformation.getCreated());
                        ps.setString(++index, paymentInformation.getCreatedBy());
                        ps.setTimestamp(++index, paymentInformation.getUpdated());
                        ps.setString(++index, paymentInformation.getUpdatedBy());
                    }
                    @Override
                    public int getBatchSize() {
                        return batch.size();
                    }
                });
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
