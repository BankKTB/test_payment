package th.com.bloomcode.paymentservice.repository.payment.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.payment.VendorReport;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.payment.VendorReportRepository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class VendorReportRepositoryImpl extends MetadataJdbcRepository<VendorReport, Long> implements VendorReportRepository {

    static BeanPropertyRowMapper<VendorReport> beanPropertyRowMapper = new BeanPropertyRowMapper<>(VendorReport.class);

    private final JdbcTemplate jdbcTemplate;

    public VendorReportRepositoryImpl(@Qualifier("paymentJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(beanPropertyRowMapper, null, null, null, null, jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<VendorReport> findAllByPaymentAliasIdAndIsProposalAndIsChild(Long paymentAliasId, boolean isProposal, boolean isChild) {
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT PC.BANK_ACCOUNT_NO, ");
        sql.append("PI.VENDOR_CODE AS VENDOR_CODE, ");
        sql.append("PI.VENDOR_NAME AS VENDOR_NAME, ");
        sql.append("PC.PAYMENT_DATE AS PAYMENT_DATE, ");
        sql.append("PC.PAYMENT_NAME AS PAYMENT_NAME, ");
        sql.append("PC.PAYING_COMPANY_CODE AS PAYING_COMPANY_CODE, ");
        sql.append("'กรมบัญชีกลาง' AS PAYING_COMPANY_NAME, ");
        sql.append("COUNT(1) AS TOTAL ");
        sql.append("FROM PAYMENT_PROCESS PC ");
        sql.append("LEFT JOIN PAYMENT_INFORMATION PI ON PC.ID = PI.PAYMENT_PROCESS_ID ");
        sql.append("WHERE 1 = 1 ");

        if (isProposal) {
            sql.append("          AND PC.IS_PROPOSAL = '1'           ");
            sql.append("          AND PC.IS_PROPOSAL_BLOCK = '0'           ");
        } else {
            sql.append("          AND PC.IS_PROPOSAL = '0'           ");
        }
        if (isChild) {
            sql.append("          AND PC.IS_CHILD = '1'           ");
        } else {
            sql.append("          AND PC.IS_CHILD = '0'           ");
        }
        params.add(paymentAliasId);
        sql.append("          AND PC.PAYMENT_ALIAS_ID = ?           ");
        sql.append("          GROUP BY PC.BANK_ACCOUNT_NO, PI.VENDOR_CODE, PI.VENDOR_NAME, PC.PAYMENT_DATE, PC.PAYMENT_NAME, PC.PAYING_COMPANY_CODE           ");
        sql.append("          ORDER BY PI.VENDOR_CODE, PI.VENDOR_NAME           ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql);
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
