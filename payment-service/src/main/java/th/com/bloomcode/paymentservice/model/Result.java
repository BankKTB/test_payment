package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Result<E> {

    public static final String T = "T";
    public static final String F = "F";
    public static final String E = "E";

    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    public static final String ERROR = "Error";
    public static final String NOT_FOUND = "Not found";

    private Date timestamp;
    private int status;
    private String error;
    private List<String> errors = new ArrayList<>();
    private String message;
    private String xml;
    private String jsonObject;
    private E data;
    private byte[] file;

    public Result() {

    }

    public Result(int status) {
        this.status = status;
    }

    public Result(int status, String message) {
        this.status = status;
        if (null == message) {
            this.message = "";
        } else {
            this.message = message.trim();
        }
    }

    public Result(int status, String message, E data) {
        this.status = status;
        if (null == message) {
            this.message = "";
        } else {
            this.message = message.trim();
        }
        this.data = data;
    }

    public Result(E data) {
        this.data = data;
    }
}