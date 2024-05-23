package th.com.bloomcode.paymentservice.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.model.JoinColumn;
import th.com.bloomcode.paymentservice.model.SearchCriteria;
import th.com.bloomcode.paymentservice.model.SearchQuery;

import javax.persistence.criteria.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SpecificationUtil {
	public static <T> Specification<T> bySearchQuery(SearchQuery searchQuery, Class<T> clazz) {

		return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criterailBuilder) -> {

			List<Predicate> predicates = new ArrayList<>();

			// Add Predicates for tables to be joined
			List<JoinColumn> joinColumns = searchQuery.getJoinColumn();

			if (null != joinColumns && !joinColumns.isEmpty()) {
				for (JoinColumn joinColumn : joinColumns) {
					addJoinColumnProps(predicates, joinColumn, criterailBuilder, root);
				}
			}

			List<SearchCriteria> searchCriterias = searchQuery.getSearchCriteria();

			if (null != searchCriterias && !searchCriterias.isEmpty()) {

				for (final SearchCriteria searchCriteria : searchCriterias) {
					addPredicates(predicates, searchCriteria, criterailBuilder, root);
				}
			}

			if (predicates.isEmpty()) {
				return criterailBuilder.conjunction();
			}

			return criterailBuilder.and(predicates.toArray(new Predicate[0]));

		};
	}

	private static <T> void addJoinColumnProps(List<Predicate> predicates, JoinColumn joinColumn,
			CriteriaBuilder criteriaBuilder, Root<T> root) {

		SearchCriteria searchCriteria = joinColumn.getSearchCriteria();
		Join<Object, Object> joinParent = root.join(joinColumn.getJoinColumnName());

		String property = searchCriteria.getKey();
		Path expression = joinParent.get(property);

		addPredicate(predicates, searchCriteria, criteriaBuilder, expression);

	}

	private static <T> void addPredicates(List<Predicate> predicates, SearchCriteria searchCriteria,
			CriteriaBuilder criteriaBuilder, Root<T> root) {
		String property = searchCriteria.getKey();
		Path expression = root.get(property);

		addPredicate(predicates, searchCriteria, criteriaBuilder, expression);

	}

	private static void addPredicate(List<Predicate> predicates, SearchCriteria searchCriteria,
			CriteriaBuilder criteriaBuilder, Path expression) {
		switch (searchCriteria.getOperation()) {
			case EQUAL:
				if (searchCriteria.getValue() instanceof Timestamp) {
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(searchCriteria.getValue());
					predicates.add(criteriaBuilder.equal(criteriaBuilder.function("TO_CHAR", String.class, expression, criteriaBuilder.literal("YYYY-MM-DD")), formattedDate));
				} else {
					predicates.add(criteriaBuilder.equal(expression, searchCriteria.getValue()));
				}
			break;
			case MATCH:
			predicates.add(criteriaBuilder.like(expression, "%" + searchCriteria.getValue() + "%"));
			break;
			case MATCH_START:
				predicates.add(criteriaBuilder.like(expression, searchCriteria.getValue() + "%"));
				break;
			case MATCH_END:
				predicates.add(criteriaBuilder.like(expression, "%" + searchCriteria.getValue()));
				break;
			case IN:
			predicates.add(criteriaBuilder.in(expression).value(searchCriteria.getValue()));
			break;
			case GREATER_THAN:
			predicates.add(criteriaBuilder.greaterThan(expression, (Comparable) searchCriteria.getValue()));
			break;
			case LESS_THAN:
			predicates.add(criteriaBuilder.lessThan(expression, (Comparable) searchCriteria.getValue()));
			break;
			case GREATER_THAN_EQUAL:
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) searchCriteria.getValue()));
			break;
			case LESS_THAN_EQUAL:
			predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) searchCriteria.getValue()));
			break;
			case NOT_EQUAL:
			predicates.add(criteriaBuilder.notEqual(expression, searchCriteria.getValue()));
			break;
			case NULL:
			predicates.add(criteriaBuilder.isNull(expression));
			break;
			case NOT_NULL:
			predicates.add(criteriaBuilder.isNotNull(expression));
			break;
		default:
			log.error("Predicate is not matched");
			throw new IllegalArgumentException(searchCriteria.getOperation() + " is not a valid predicate");
		}

	}
}