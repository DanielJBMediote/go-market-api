package gomarketapi.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import gomarketapi.helpers.Where;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Deprecated
public class GlobalSpecification<T> {

  public static <T> Specification<T> withConditions(List<Where> where) {
    return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      for (Where condition : where) {
        String column = condition.getColumnName();
        Object value = condition.getValue();
        Where.Operator operator = condition.getOperator();

        switch (operator) {
          case Where.Operator.EQUALS:
            predicates.add(builder.equal(root.get(column), value));
            break;
          case Where.Operator.NOT_EQUALS:
            predicates.add(builder.notEqual(root.get(column), value));
            break;
          case Where.Operator.GREATER_THAN:
            predicates.add(builder.greaterThan(root.get(column), (Comparable) value));
            break;
          case Where.Operator.GREATER_THAN_OR_EQUALS:
            predicates.add(builder.greaterThanOrEqualTo(root.get(column), (Comparable) value));
            break;
          case Where.Operator.LESS_THAN:
            predicates.add(builder.lessThan(root.get(column), (Comparable) value));
            break;
          case Where.Operator.LESS_THAN_OR_EQUALS:
            predicates.add(builder.lessThanOrEqualTo(root.get(column), (Comparable) value));
            break;
          case Where.Operator.LIKE:
            predicates.add(builder.like(root.get(column), "%" + value + "%"));
            break;
          default:
            throw new IllegalArgumentException("Operador inválido: " + operator);
        }
      }

      // Combina todos os predicados com AND
      return builder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
