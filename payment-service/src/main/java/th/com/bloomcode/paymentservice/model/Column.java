package th.com.bloomcode.paymentservice.model;

import lombok.Data;

@Data
public class Column {

    private int sequence;
    private String name;
    private String type;
    private String caption; //*Tao 2021-01-28 caption of column header
}
