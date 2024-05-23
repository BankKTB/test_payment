package th.com.bloomcode.paymentservice.helper;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.config.DBConfig;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DBConnection {

  static Map<String, DataSource> dataSourceMap = new HashMap<>();
  static Map<String, JdbcTemplate> jdbcTemplateMap = new HashMap<>();
  static Map<String, NamedParameterJdbcTemplate> namedParameterJdbcTemplateMap = new HashMap<>();

  public static JdbcTemplate getJdbcTemplate(String dataSourceName) {
    if (null == jdbcTemplateMap.get(dataSourceName)) {
      JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource(dataSourceName));
      jdbcTemplate.setFetchSize(5000);
      jdbcTemplateMap.put(dataSourceName, jdbcTemplate);
      return jdbcTemplate;
    } else {
      return jdbcTemplateMap.get(dataSourceName);
    }
  }

  public static NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(String dataSourceName) {
    if (null == namedParameterJdbcTemplateMap.get(dataSourceName)) {
      NamedParameterJdbcTemplate namedParameterJdbcTemplate =
          new NamedParameterJdbcTemplate(getDataSource(dataSourceName));
      namedParameterJdbcTemplate.getJdbcTemplate().setFetchSize(5000);
      namedParameterJdbcTemplateMap.put(dataSourceName, namedParameterJdbcTemplate);
      return namedParameterJdbcTemplate;
    } else {
      return namedParameterJdbcTemplateMap.get(dataSourceName);
    }
  }

  static DataSource getDataSource(String dataSourceName) {
    if (null == dataSourceMap.get("dataSourceName")) {
      if (null == DBConfig.dataSourcePropertiesMap.get(dataSourceName)) return null;
      DataSource ds =
              DBConfig.dataSourcePropertiesMap
                      .get(dataSourceName)
                      .initializeDataSourceBuilder()
                      .type(HikariDataSource.class)
                      .build();
      dataSourceMap.put(dataSourceName, ds);
      return ds;
    } else {
      return dataSourceMap.get("dataSourceName");
    }
  }
}
