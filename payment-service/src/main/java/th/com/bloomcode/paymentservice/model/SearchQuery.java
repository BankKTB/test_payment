package th.com.bloomcode.paymentservice.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchQuery {
	private List<SearchCriteria> searchCriteria;
	private int pageNumber;
	private int pageSize;
	private SortOrder sortOrder;
	private List<JoinColumn> joinColumn;

	public void addSearchCriteria(SearchCriteria searchCriteria) {
		if (null == this.searchCriteria) this.searchCriteria = new ArrayList<>();
		this.searchCriteria.add(searchCriteria);
	}

	public void addJoinColumn(JoinColumn joinColumn) {
		if (null == this.joinColumn) this.joinColumn = new ArrayList<>();
		this.joinColumn.add(joinColumn);
	}
}