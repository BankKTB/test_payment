package th.com.bloomcode.paymentservice.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DBConfig {

  public static Map<String, DataSourceProperties> dataSourcePropertiesMap = new HashMap<>();

//  @Primary
//  @Bean(name = "payment")
//  @ConfigurationProperties("payment.datasource")
//  public DataSourceProperties paymentDataSourceProperties() {
//    DataSourceProperties dataSourceProperties = new DataSourceProperties();
//    dataSourcePropertiesMap.put("payment", dataSourceProperties);
//    return dataSourceProperties;
//  }
//
//  @Bean(name = "idempiere")
//  @ConfigurationProperties("idempiere.datasource")
//  public DataSourceProperties idemDataSourceProperties() {
//    DataSourceProperties dataSourceProperties = new DataSourceProperties();
//    dataSourcePropertiesMap.put("idempiere", dataSourceProperties);
//    return dataSourceProperties;
//  }

  @Bean(name = "payDataSource")
  @Qualifier("payDataSource")
  @ConfigurationProperties(prefix="pay.datasource")
  public DataSource primaryDataSource(){
    return DataSourceBuilder.create().build();
  }


  @Bean(name = "idempiereDataSource")
  @Qualifier("idempiereDataSource")
  @ConfigurationProperties(prefix="idempiere.datasource")
  public DataSource secondaryDataSource(){
    return DataSourceBuilder.create().build();
  }

  @Bean(name = "paymentJdbcTemplate")
  public JdbcTemplate primaryJdbcTemplate(
          @Qualifier("payDataSource") DataSource dataSource){
    return new JdbcTemplate(dataSource);
  }

  @Bean(name = "paymentNamedJdbcTemplate")
  public NamedParameterJdbcTemplate primaryNamedJdbcTemplate(
          @Qualifier("payDataSource") DataSource dataSource){
    return new NamedParameterJdbcTemplate(dataSource);
  }

  @Bean(name = "idemJdbcTemplate")
  public JdbcTemplate secondaryJdbcTemplate(
          @Qualifier("idempiereDataSource") DataSource dataSource){
    return new JdbcTemplate(dataSource);
  }
}
