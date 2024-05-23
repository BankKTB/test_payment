package th.com.bloomcode.paymentservice.repository.idem.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.model.idem.IdemConfig;
import th.com.bloomcode.paymentservice.repository.MetadataJdbcRepository;
import th.com.bloomcode.paymentservice.repository.idem.IdemConfigRepository;
import th.com.bloomcode.paymentservice.util.Util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

@Repository
@Slf4j
public class IdemConfigRepositoryImpl  extends MetadataJdbcRepository<IdemConfig, Long>  implements IdemConfigRepository {
    static BeanPropertyRowMapper<IdemConfig> beanPropertyRowMapper = new BeanPropertyRowMapper<>(IdemConfig.class);
    private final JdbcTemplate jdbcTemplate;

    static Updater<IdemConfig> documentTypeUpdater = (t, mapping) -> {
        mapping.put(IdemConfig.COLUMN_NAME_TH_CACLIENT_ID, t.getId());
        mapping.put(IdemConfig.COLUMN_NAME_AD_CLIENT_ID, t.getClientId());
        mapping.put(IdemConfig.COLUMN_NAME_AD_ORG_ID, t.getAdOrgId());
        mapping.put(IdemConfig.COLUMN_NAME_CLIENTVALUE, t.getClientValue());
        mapping.put(IdemConfig.COLUMN_NAME_TARGETURL, t.getTargetUrl());
        mapping.put(IdemConfig.COLUMN_NAME_ISACTIVE, t.getIsActive());
        mapping.put(IdemConfig.COLUMN_NAME_TH_CACLIENT_UU, t.getClientUU());
        mapping.put(IdemConfig.COLUMN_NAME_CLIENT_ID, t.getClientId());
        mapping.put(IdemConfig.COLUMN_NAME_ROLE_ID, t.getRoleId());
        mapping.put(IdemConfig.COLUMN_NAME_USERNAME, t.getUsername());
        mapping.put(IdemConfig.COLUMN_NAME_PASSWORD, t.getPassword());
    };

    static Map<String, Integer> updaterType = Map.ofEntries(
            entry(IdemConfig.COLUMN_NAME_TH_CACLIENT_ID, Types.BIGINT),
            entry(IdemConfig.COLUMN_NAME_AD_CLIENT_ID, Types.BIGINT),
            entry(IdemConfig.COLUMN_NAME_AD_ORG_ID, Types.BIGINT),
            entry(IdemConfig.COLUMN_NAME_CLIENTVALUE, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_TARGETURL, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_CREATED, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_CREATEDBY, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_ISACTIVE, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_TH_CACLIENT_UU, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_UPDATED, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_UPDATEDBY, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_CLIENT_ID, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_ROLE_ID, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_USERNAME, Types.NVARCHAR),
            entry(IdemConfig.COLUMN_NAME_PASSWORD, Types.NVARCHAR)
    );

    static RowMapper<IdemConfig> userRowMapper = (rs, rowNum) -> new IdemConfig(
            rs.getLong(IdemConfig.COLUMN_NAME_TH_CACLIENT_ID),
            rs.getString(IdemConfig.COLUMN_NAME_AD_CLIENT_ID),
            rs.getInt(IdemConfig.COLUMN_NAME_AD_ORG_ID),
            rs.getString(IdemConfig.COLUMN_NAME_CLIENTVALUE),
            rs.getString(IdemConfig.COLUMN_NAME_TARGETURL),
            rs.getString(IdemConfig.COLUMN_NAME_CREATED),
            rs.getTimestamp(IdemConfig.COLUMN_NAME_CREATEDBY),
            rs.getString(IdemConfig.COLUMN_NAME_ISACTIVE),
            rs.getString(IdemConfig.COLUMN_NAME_TH_CACLIENT_UU),
            rs.getString(IdemConfig.COLUMN_NAME_UPDATEDBY),
            rs.getTimestamp(IdemConfig.COLUMN_NAME_UPDATED),
            rs.getInt(IdemConfig.COLUMN_NAME_CLIENT_ID),
            rs.getInt(IdemConfig.COLUMN_NAME_ROLE_ID),
            rs.getString(IdemConfig.COLUMN_NAME_USERNAME),
            rs.getString(IdemConfig.COLUMN_NAME_PASSWORD)
    );

    @Autowired
    public IdemConfigRepositoryImpl(@Qualifier("idemJdbcTemplate") JdbcTemplate jdbcTemplate) {
        super(
                userRowMapper,
                documentTypeUpdater,
                updaterType,
                IdemConfig.TABLE_NAME,
                IdemConfig.COLUMN_NAME_TH_CACLIENT_ID,
                jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public IdemConfig findByValueClientStartingWithAndIsActiveTrue(String valueCode) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(IdemConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        if (!Util.isEmpty(valueCode)) {
            sql.append(" AND CLIENTVALUE like ?%");
        }
        sql.append(" AND ISACTIVE = 'Y' ");

        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        List<IdemConfig> CcnfigWebOnlines = this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
        if (!CcnfigWebOnlines.isEmpty()) {
            return CcnfigWebOnlines.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<IdemConfig> findAllByIsActiveTrue() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT * FROM ");
        sql.append(IdemConfig.TABLE_NAME);
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND IS_ACTIVE = 'Y' ");
        Object[] objParams = new Object[params.size()];
        params.toArray(objParams);
        log.info("sql {}", sql.toString());
        return this.jdbcTemplate.query(sql.toString(), objParams, beanPropertyRowMapper);
    }
}
