package gomarketapi.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

public class QueryBuilder {
  private final Class<?> rootEntity;
  private final String rootTable;
  private final List<String> selectedColumns = new ArrayList<>();
  private final List<String> joins = new ArrayList<>();
  private final List<String> whereClauses = new ArrayList<>();
  private String orderBy;
  private Integer limit;

  private final QueryExecutor queryExecutor;

  public QueryBuilder(Class<?> rootEntity, QueryExecutor queryExecutor) {
    this.rootEntity = rootEntity;
    this.rootTable = resolveTableName(rootEntity);
    this.queryExecutor = queryExecutor;
  }

  // public QueryBuilder(Class<?> rootEntity) {
  // this.rootEntity = rootEntity;
  // this.rootTable = resolveTableName(rootEntity);
  // }

  /**
   * Executa a query construída com mapeamento automático
   */
  public <T> List<T> getResultList(Class<T> resultClass) {
    return queryExecutor.execute(this.build(), resultClass);
  }

  /**
   * Executa a query com mapeamento customizado
   */
  public <T> List<T> getResultList(RowMapper<T> rowMapper) {
    return queryExecutor.execute(this.build(), rowMapper);
  }

  private String resolveTableName(Class<?> entity) {
    Table table = entity.getAnnotation(Table.class);
    return table != null ? table.name() : entity.getSimpleName().toLowerCase();
  }

  // private String resolveColumnName(Field field) {
  // Column column = field.getAnnotation(Column.class);
  // return column != null && !column.name().isEmpty() ? column.name() :
  // field.getName();
  // }

  private String resolveJoinColumn(Field field) {
    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
    return joinColumn != null && !joinColumn.name().isEmpty() ? joinColumn.name() : field.getName() + "_id";
  }

  public QueryBuilder select(String... columns) {
    for (String column : columns) {
      selectedColumns.add(column);
    }
    return this;
  }

  public QueryBuilder join(Class<?> entity) {
    try {
      for (Field field : rootEntity.getDeclaredFields()) {
        if (field.getType().equals(entity)) {
          ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
          OneToOne oneToOne = field.getAnnotation(OneToOne.class);

          if (manyToOne != null || oneToOne != null) {
            String joinTable = resolveTableName(entity);
            String joinColumn = resolveJoinColumn(field);
            joins.add("JOIN " + joinTable + " ON " + rootTable + "." + joinColumn + " = " + joinTable + ".id");
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to resolve join for entity: " + entity.getSimpleName(), e);
    }
    return this;
  }

  public QueryBuilder leftJoin(Class<?> entity) {
    try {
      for (Field field : rootEntity.getDeclaredFields()) {
        if (field.getType().equals(entity)) {
          String joinTable = resolveTableName(entity);
          String joinColumn = resolveJoinColumn(field);
          joins.add("LEFT JOIN " + joinTable + " ON " + rootTable + "." + joinColumn + " = " + joinTable + ".id");
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to resolve left join for entity: " + entity.getSimpleName(), e);
    }
    return this;
  }

  public QueryBuilder addWhere(String condition) {
    whereClauses.add(condition);
    return this;
  }

  public QueryBuilder addWhere(Where condition) {
    whereClauses.add(buildWhereCondition(condition));
    return this;
  }

  private String buildWhereCondition(Where where) {
    String column = where.getColumnName();
    Object value = where.getValue();
    Where.Operator operator = where.getOperator();

    switch (operator) {
      case IS_NULL:
        return column + " IS NULL";
      case IS_NOT_NULL:
        return column + " IS NOT NULL";
      case IN:
      case NOT_IN:
        if (value instanceof Iterable) {
          // Formata cada elemento da lista individualmente
          List<String> formattedValues = new ArrayList<>();
          for (Object item : (Iterable<?>) value) {
            formattedValues.add(formatValue(item));
          }
          return column + " " + operator.getSymbol() + " (" + String.join(", ", formattedValues) + ")";
        } else if (value instanceof Object[]) {
          // Trata arrays
          Object[] array = (Object[]) value;
          List<String> formattedValues = new ArrayList<>();
          for (Object item : array) {
            formattedValues.add(formatValue(item));
          }
          return column + " " + operator.getSymbol() + " (" + String.join(", ", formattedValues) + ")";
        }
        // Se não for lista nem array, trata como valor único
        return column + " " + operator.getSymbol() + " (" + formatValue(value) + ")";
      default:
        return column + " " + operator.getSymbol() + " " + formatValue(value);
    }
  }

  private String formatValue(Object value) {
    if (value instanceof String) {
      return "'" + value + "'";
    }
    return value.toString();
  }

  public QueryBuilder orderBy(String column) {
    this.orderBy = column;
    return this;
  }

  public QueryBuilder limit(int limit) {
    this.limit = limit;
    return this;
  }

  public String build() {
    StringBuilder query = new StringBuilder("SELECT ");

    // SELECT clause
    if (selectedColumns.isEmpty()) {
      query.append(rootTable).append(".*");
    } else {
      query.append(String.join(", ", selectedColumns));
    }

    // FROM clause
    query.append(" FROM ").append(rootTable);

    // JOIN clauses
    if (!joins.isEmpty()) {
      query.append(" ").append(String.join(" ", joins));
    }

    // WHERE clause
    if (!whereClauses.isEmpty()) {
      query.append(" WHERE ").append(String.join(" AND ", whereClauses));
    }

    // ORDER BY clause
    if (orderBy != null) {
      query.append(" ORDER BY ").append(orderBy);
    }

    // LIMIT clause
    if (limit != null) {
      query.append(" LIMIT ").append(limit);
    }

    return query.toString();
  }
}