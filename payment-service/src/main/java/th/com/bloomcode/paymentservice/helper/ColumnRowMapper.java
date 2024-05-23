package th.com.bloomcode.paymentservice.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.Column;
import java.lang.reflect.Field;

@Slf4j
public class ColumnRowMapper<T> extends BeanPropertyRowMapper<T> {

  private ColumnRowMapper(final Class<T> mappedClass) {
    super(mappedClass);
  }

  public static <T> BeanPropertyRowMapper<T> newInstance(final Class<T> mappedClass) {
    return new ColumnRowMapper<>(mappedClass);
  }

  @Override
  protected String underscoreName(final String name) {
    final Column annotation;
    final String columnName;
    Field declaredField = null;

    try {
      declaredField = getMappedClass().getDeclaredField(name);
    } catch (NoSuchFieldException | SecurityException e) {
      log.warn("Ups, field «{}» not found in «{}».", name, getMappedClass());
    }

    if (declaredField == null
        || (annotation = declaredField.getAnnotation(Column.class)) == null
        || StringUtils.isEmpty(columnName = annotation.name())) {
      return super.underscoreName(name);
    }

    return StringUtils.lowerCase(columnName);
  }
}
