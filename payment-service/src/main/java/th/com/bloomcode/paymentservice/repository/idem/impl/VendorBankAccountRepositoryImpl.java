package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.VendorBankAccount;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.VendorBankAccountRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class VendorBankAccountRepositoryImpl extends MetadataJdbcRepository<VendorBankAccount, Long> implements VendorBankAccountRepository {
    private final JdbcTemplate jdbcTemplate;

    static RowMapper<VendorBankAccount> vendorBankAccountRowMapper = (rs, rowNum) -> new VendorBankAccount(
            rs.getString(VendorBankAccount.COLUMN_NAME_VENDOR),
            rs.getString(VendorBankAccount.COLUMN_NAME_BANK_ACCOUNT_HOLDER_NAME),
            rs.getString(VendorBankAccount.COLUMN_NAME_VENDOR_NAME),
            rs.getString(VendorBankAccount.COLUMN_NAME_ACCOUNT_NO),
            rs.getString(VendorBankAccount.COLUMN_NAME_ROUTING_NO),
            rs.getString(VendorBankAccount.COLUMN_NAME_BANK_KEY),
            rs.getString(VendorBankAccount.COLUMN_NAME_IS_ACTIVE)
    );

    @Autowired
    public VendorBankAccountRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public VendorBankAccount findOneByBankAccountNoAndVendor(String bankAccountNo, String vendor) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT ");
        sql.append(" BA.ACCOUNTNO, ");
        sql.append(" BA.ISACTIVE, ");
        sql.append(" BK.ROUTINGNO, ");
        sql.append(" BN.VALUECODE AS BANKKEY, ");
        sql.append(" PN.VALUE AS VENDOR, ");
        sql.append(" BA.A_NAME AS BANK_ACCOUNT_HOLDER_NAME, ");
        sql.append(" PN.NAME AS VENDORNAME ");
        sql.append(" FROM ");
        sql.append(" C_BP_BANKACCOUNT BA LEFT JOIN C_BANK BK ON BA.C_BANK_ID = BK.C_BANK_ID ");
        sql.append(" LEFT JOIN C_BPARTNER PN ON BA.C_BPARTNER_ID = PN.C_BPARTNER_ID ");
        sql.append(" LEFT JOIN TH_CABANKINFO BI ON BK.C_BANK_ID = BI.C_BANK_ID ");
        sql.append(" LEFT JOIN TH_CABANK BN ON BI.TH_CABANK_ID = BN.TH_CABANK_ID ");
        sql.append(" WHERE ");
        sql.append(" PN.ISACTIVE = 'Y' ");
        sql.append(" AND PN.ISVENDOR = 'Y' ");
        sql.append(" AND BA.ISACTIVE = 'Y' ");
        if (!Util.isEmpty(bankAccountNo)) {
            sql.append(" AND BA.ACCOUNTNO like ? ");
            params.add(bankAccountNo);
        }
        if (!Util.isEmpty(vendor)) {
            sql.append(" AND PN.VALUE like ? ");
            params.add(vendor);
        }
        sql.append(" ORDER BY ");
        sql.append(" BA.ACCOUNTNO ASC");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);

        List<VendorBankAccount> vendorBankAccounts = this.jdbcTemplate.query(sql.toString(), objParams, vendorBankAccountRowMapper);
        if (!vendorBankAccounts.isEmpty()) {
            return vendorBankAccounts.get(0);
        } else {
            return null;
        }
    }
}
