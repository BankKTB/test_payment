package th.com.bloomcode.paymentservice.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface MetadataRepository<T, ID> extends PagingAndSortingRepository<T, ID> {
    Long getNextSeries();
    void updateNextSeries(Long lastSeq);
}
