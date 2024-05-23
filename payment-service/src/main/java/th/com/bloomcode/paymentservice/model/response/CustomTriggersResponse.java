package th.com.bloomcode.paymentservice.model.response;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class CustomTriggersResponse {
  private Long id;
  private String state;
  private String status;
  private Long triggerAtInMillis;
  private Long paymentAliasId;
  private Long paymentType;
  private LocalDateTime paymentDate;
  private String paymentName;
  @CreatedDate
  private LocalDateTime createdDate;
  @LastModifiedDate
  private LocalDateTime updatedDate;
}
