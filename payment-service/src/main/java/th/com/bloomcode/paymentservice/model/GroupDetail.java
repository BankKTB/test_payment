package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.util.List;

@Data
public class GroupDetail {

    private String groupName;
    private String columnName;
    private boolean last;
    private List<GroupSummary> groupSummary;

}
