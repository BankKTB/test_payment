package th.com.bloomcode.paymentservice.model;

import lombok.Data;

@Data
public class Paging {

    private int totalPages;
    private int size;
    private int number;
    private int numberOfElements;
    private long totalElements;

}
