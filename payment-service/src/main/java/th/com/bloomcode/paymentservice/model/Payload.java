package th.com.bloomcode.paymentservice.model;

public class Payload {

    private String content;

    public Payload() {
    }

    public Payload(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}