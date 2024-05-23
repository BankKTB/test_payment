package th.com.bloomcode.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupColumn {

    private List<Map<String, Object>> items;
    private List<GroupDetail> groups;

}
