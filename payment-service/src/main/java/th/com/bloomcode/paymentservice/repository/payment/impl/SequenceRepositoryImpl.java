package th.com.bloomcode.paymentservice.repository.payment.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.stereotype.Repository;
import th.com.bloomcode.paymentservice.repository.payment.SequenceRepository;

import javax.sql.DataSource;

@Repository
public class SequenceRepositoryImpl implements SequenceRepository {

  private transient DataSource dataSource;

  @Autowired
  public void setDataSource(@Qualifier("payDataSource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Long getNext(String seqName) {
    OracleSequenceMaxValueIncrementer incrementer = new OracleSequenceMaxValueIncrementer(this.dataSource, seqName);
    return incrementer.nextLongValue();
  }
}
