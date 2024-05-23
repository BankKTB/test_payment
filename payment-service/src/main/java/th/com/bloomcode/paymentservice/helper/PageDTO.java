package th.com.bloomcode.paymentservice.helper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class PageDTO<T> implements Page<T> {

  Pageable pageable;
  int totalPages;
  int totalNumbers;
  List<T> content;

  public PageDTO(Pageable pageable, int totalPages,
                  int totalNumbers, List<T> content) {
    super();
    this.pageable = pageable;
    this.totalPages = totalPages;
    this.totalNumbers = totalNumbers;
    this.content = content;
  }

  @Override
  public int getNumber() {
    return pageable.getPageNumber();
  }

  @Override
  public int getSize() {
    return pageable.getPageSize();
  }

  @Override
  public int getTotalPages() {
    return this.totalPages;
  }

  @Override
  public int getNumberOfElements() {
    return this.getContent().size();
  }

  @Override
  public long getTotalElements() {
    return this.totalNumbers;
  }

  @Override
  public <U> Page<U> map(Function<? super T, ? extends U> function) {
    return null;
  }

  @Override
  public boolean hasPrevious() {
    return pageable.getPageNumber() != 0;
  }

  @Override
  public boolean isFirst() {
    return pageable.getPageNumber() == 0;
  }

  @Override
  public boolean hasNext() {
    return pageable.getPageNumber() != totalPages;
  }

  @Override
  public boolean isLast() {
    return pageable.getPageNumber() == totalPages;
  }

  @Override
  public Iterator<T> iterator() {
    return content.iterator();
  }

  @Override
  public List<T> getContent() {
    return content;
  }

  @Override
  public boolean hasContent() {
    return !content.isEmpty();
  }

  @Override
  public Sort getSort() {
    return this.pageable.getSort();
  }

  @Override
  public Pageable nextPageable() {
    return this.pageable.next();
  }

  @Override
  public Pageable previousPageable() {
    return this.pageable.previousOrFirst();
  }

}
