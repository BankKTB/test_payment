package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.io.InputStream;

@Data
public class ReturnFile {
  private String fileName;
  private InputStream file;
}
