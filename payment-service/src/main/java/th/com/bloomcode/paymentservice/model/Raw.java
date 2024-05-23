package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class Raw {

    private List<GroupColumn> datas;
    private Set<Column> columns;
    private Paging page;
    private List<Map<String, Object>> summaries;

    private Map<String, Object> optionals;
}
