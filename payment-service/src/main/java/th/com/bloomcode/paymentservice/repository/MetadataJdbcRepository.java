package th.com.bloomcode.paymentservice.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import th.com.bloomcode.paymentservice.helper.PageDTO;
import th.com.bloomcode.paymentservice.model.BaseModel;

import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Types;
import java.util.*;

@Slf4j
public abstract class MetadataJdbcRepository<T extends BaseModel, ID extends Serializable> implements MetadataRepository<T, ID> {

  private JdbcTemplate jdbcTemplate;
  private String tableName;
  private String idColumn;

  private String deleteQuery;
  private String selectAll;
  private String selectById;
  private String countQuery;
  private String selectAllByColumn;

  RowMapper<T> rowMapper;
  Updater<T> updater;
  Map<String, Integer> updaterType;

  private transient DataSource dataSource;

  public interface Updater<T> {
    void mapColumns(T t, Map<String,Object> mapping);
  }

  @Autowired
  public void setDataSource(@Qualifier("payDataSource") DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public MetadataJdbcRepository() {

  }

  public MetadataJdbcRepository(
          RowMapper<T> rowMapper,
          Updater<T> updater,
          Map<String, Integer> updaterType,
          String tableName,
          String idColumn,
          JdbcTemplate jdbcTemplate) {

    this.updater = updater;
    this.rowMapper = rowMapper;
    this.updaterType = updaterType;

    this.jdbcTemplate = jdbcTemplate;
    this.tableName = tableName;
    this.idColumn = idColumn;

    this.deleteQuery = String.format("delete from %s where %s = ?", tableName, idColumn);
    this.selectAll = String.format("select * from %s", tableName);
    this.selectById = String.format("select * from %s where %s = ?", tableName, idColumn);
    this.countQuery = String.format("select count(%s) from %s", idColumn, tableName);
    this.selectAllByColumn = String.format("select * from %s where VALUECODE like ?", tableName);
  }

  @Override
  public <S extends T> S save(S s) {
    Map<String,Object> columns = new HashMap<>();
    updater.mapColumns(s, columns);
    log.debug("id : {}", s.getId());
//    log.debug("existsById : {}", existsByIdForValidate(s.getId()));
    if (!existsByIdForValidate(s.getId())) {
      StringBuilder insertQuery = new StringBuilder(String.format("insert into %s ", this.tableName));
      StringBuilder valueQuery = new StringBuilder();

      Object[] obj = new Object[columns.size()];
      int[] type = new int[columns.size()];
      int i = 0;
    log.debug("columns size : {}", columns.size());

      for(Map.Entry<String,Object> e : columns.entrySet()) {
        if (i == 0) {
          insertQuery.append("(");
          valueQuery.append("(");
        } else {
          insertQuery.append(", ");
          valueQuery.append(", ");
        }
        int index = i++;
//        log.debug("key : {},  value : {}", e.getKey(), e.getValue());
        obj[index] = e.getValue();
        type[index] = updaterType.get(e.getKey());
        insertQuery.append(e.getKey());
        valueQuery.append("?");
      }
      insertQuery.append(")");
      valueQuery.append(")");

      insertQuery.append(" values ").append(valueQuery.toString());

      jdbcTemplate.update(insertQuery.toString(), obj, type);
    } else {
      StringBuilder updateQuery = new StringBuilder(String.format("update %s set ", this.tableName));
      columns.remove(BaseModel.COLUMN_NAME_CREATED_BY);
      columns.remove(BaseModel.COLUMN_NAME_CREATED);
      Object[] obj = new Object[columns.size() + 1];
      int[] type = new int[columns.size() + 1];
      int i = 0;

      for(Map.Entry<String,Object> e : columns.entrySet()) {
        if (i != 0) {
          updateQuery.append(", ");
        }
        int index = i++;
        obj[index] = e.getValue();
        type[index] = updaterType.get(e.getKey());
        updateQuery.append(" ").append(e.getKey()).append(" = ?");
      }

      obj[i] = s.getId();
      type[i] = Types.BIGINT;

      updateQuery.append(String.format(" where %s = ? ", this.idColumn));

      jdbcTemplate.update(updateQuery.toString(), obj, type);
    }
//    log.debug(" insertQuery : {} " , insertQuery);
    return s;
  }

  @Override
  public <S extends T> Iterable<S> saveAll(Iterable<S> iterable) {
    List<Object[]> objs = new ArrayList<>();
    String insertSql = "";
    for (S ss : iterable) {
      Map<String,Object> columns = new HashMap<>();
      updater.mapColumns(ss, columns);

      log.debug("id : {}", ss.getId());
//    log.debug("existsById : {}", existsByIdForValidate(s.getId()));

//    if (!existsByIdForValidate(s.getId())) {
      StringBuilder insertQuery = new StringBuilder(String.format("insert into %s ", this.tableName));
      StringBuilder valueQuery = new StringBuilder();

      Object[] obj = new Object[columns.size()];
      Object[] type = new Object[columns.size()];
      int i = 0;

      for(Map.Entry<String,Object> e : columns.entrySet()) {
        if (i == 0) {
          insertQuery.append("(");
          valueQuery.append("(");
        } else {
          insertQuery.append(", ");
          valueQuery.append(", ");
        }
        obj[i++] = e.getValue();
//        log.debug("value : {}", e.getValue());
        insertQuery.append(e.getKey());
        valueQuery.append("?");
      }
      insertQuery.append(")");
      valueQuery.append(")");

      insertQuery.append(" values ").append(valueQuery.toString());
      insertSql = insertQuery.toString();
      objs.add(obj);
    }


//    jdbcTemplate.update(insertQuery.toString(), obj);
    jdbcTemplate.batchUpdate(insertSql, objs);
    return iterable;
  }

  @Override
  public Optional<T> findById(ID id) {
    try {
      List<T> list = jdbcTemplate.query(this.selectById, new Object[] { id }, this.rowMapper);
      if (null == list) {
        return Optional.empty();
      } else if (!list.isEmpty()) {
        return Optional.ofNullable(list.get(0));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  private Optional<T> findByIdForValidate(Long id) {
    try {
      List<T> list = jdbcTemplate.query(this.selectById, new Object[] { id }, this.rowMapper);
      if (null == list) {
        return Optional.empty();
      } else if (!list.isEmpty()) {
        return Optional.ofNullable(list.get(0));
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public boolean existsById(ID id) {
    return count() > 0;
  }

  private boolean existsByIdForValidate(Long id) {
    return findByIdForValidate(id).isPresent();
  }

  @Override
  public Iterable<T> findAll() {
    return jdbcTemplate.query(this.selectAll, this.rowMapper);
  }

  @Override
  public Iterable<T> findAllById(Iterable<ID> iterable) {
    List<T> results = new ArrayList<>();
    for (ID id : iterable) {
      this.findById(id).ifPresent(results::add);
    }
    return results;
  }

  public Iterable<T> findAllByValue(Iterable<ID> iterable) {
    List<T> results = new ArrayList<>();
    for (ID id : iterable) {
      this.findById(id).ifPresent(results::add);
    }
    return results;
  }

  @Override
  public Iterable<T> findAll(Sort arg0) {
    StringBuilder qu = new StringBuilder(this.selectAll);

    for(Sort.Order o : arg0) {
      qu.append(" ORDER BY ").append(o.getProperty()).append(" ").append(o.getDirection().toString()).append(" ");
    }

    return jdbcTemplate.query(qu.toString(), this.rowMapper);
  }

  @Override
  public Page<T> findAll(Pageable pageable) {
    StringBuilder qu = new StringBuilder(this.selectAll);

    for(Sort.Order o : pageable.getSort()) {
      qu.append(" ORDER BY ").append(o.getProperty()).append(" ").append(o.getDirection().toString()).append(" ");
    }

    int pageSize = 10;

    qu.append(" LIMIT ").append(pageable.getPageNumber() * pageSize).append(" ").append(pageSize).append(" ");

    long count = count();

    return new PageDTO<>(pageable,
            (int) count/pageSize,
            (int) count,
            jdbcTemplate.query(qu.toString(), this.rowMapper));
  }

  @Override
  public long count() {
    Long count = jdbcTemplate.queryForObject(this.countQuery, Long.class);
    return null == count ? 0 : count;
  }

  @Override
  public void deleteById(ID id) {
    this.jdbcTemplate.update(this.deleteQuery, id);
  }

  @Override
  public void delete(T t) {
    this.jdbcTemplate.update(this.deleteQuery, t.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends T> var1) {
    for (T t : var1) {
      delete(t);
    }
  }

  @Override
  public void deleteAll() {
    Iterable<T> iterable = findAll();
    for (T t : iterable) {
      delete(t);
    }
  }

  @Override
  public Long getNextSeries() {
    OracleSequenceMaxValueIncrementer incrementer = new OracleSequenceMaxValueIncrementer(dataSource, tableName + "_SEQ");
    return incrementer.nextLongValue();
  }

  @Override
  public void updateNextSeries(Long lastSeq) {
    this.jdbcTemplate.update("ALTER SEQUENCE " + tableName + "_SEQ RESTART START WITH " + lastSeq);
  }

  private String generatePageable(Pageable pageable, Map<String, Object> params) {
    StringBuilder sql = new StringBuilder();
    if (!"UNSORTED".equalsIgnoreCase(pageable.getSort().toString())) {
      sql.append(" ORDER BY ");
      int i = 0;
      for (Sort.Order o : pageable.getSort()) {
        if (i != 0) {
          sql.append(", ");
        }
        sql.append(o.getProperty()).append(" ").append(o.getDirection().name()).append(" ");
        i++;
      }
    }

    sql.append(" OFFSET ").append(" :pageOffset ").append(" ROWS FETCH NEXT ").append(" :pageSize ").append(" ROWS ONLY");
    params.put("pageOffset", pageable.getPageNumber() * pageable.getPageSize());
    params.put("pageSize", pageable.getPageSize());
    return sql.toString();
  }

  protected String generatePageable(Pageable pageable, List<Object> params) {
    StringBuilder sql = new StringBuilder();
    if (!"UNSORTED".equalsIgnoreCase(pageable.getSort().toString())) {
      sql.append(" ORDER BY ");
      int i = 0;
      for (Sort.Order o : pageable.getSort()) {
        if (i != 0) {
          sql.append(", ");
        }
        sql.append(o.getProperty()).append(" ").append(o.getDirection().name()).append(" ");
        i++;
      }
    }

    sql.append(" OFFSET ").append("?").append(" ROWS FETCH NEXT ").append("?").append(" ROWS ONLY");
    params.add(pageable.getPageNumber() * pageable.getPageSize());
    params.add(pageable.getPageSize());
    return sql.toString();
  }
}
