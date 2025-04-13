package gomarketapi.helpers;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representa uma cláusula WHERE para consultas, contendo coluna, valor e
 * operador.
 */
public final class Where {
  private final String columnName;
  private final Object value;
  private final Operator operator;

  /**
   * Constrói uma cláusula WHERE imutável.
   *
   * @param columnName Nome da coluna (não pode ser nulo ou vazio).
   * @param value      Valor a ser comparado (pode ser nulo apenas para operadores
   *                   IS_NULL/IS_NOT_NULL).
   * @param operator   Operador da condição (não pode ser nulo).
   * @throws IllegalArgumentException Se algum argumento for inválido.
   */
  public Where(
      @JsonProperty("columnName") String columnName,
      @JsonProperty("value") Object value,
      @JsonProperty("operator") Operator operator) {
    this.columnName = validateColumnName(columnName);
    this.value = validateValue(value, operator);
    this.operator = Objects.requireNonNull(operator, "Operator cannot be null");
  }

  public String getColumnName() {
    return columnName;
  }

  public Object getValue() {
    return value;
  }

  public Operator getOperator() {
    return operator;
  }

  // --- Validações ---
  private String validateColumnName(String columnName) {
    if (columnName == null || columnName.trim().isEmpty()) {
      throw new IllegalArgumentException("Column name cannot be null or empty");
    }
    return columnName;
  }

  private Object validateValue(Object value, Operator operator) {
    if (value == null && !operator.isNullAllowed()) {
      throw new IllegalArgumentException("Value cannot be null for operator: " + operator);
    }
    return value;
  }

  // --- Métodos padrão ---
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Where where = (Where) o;
    return columnName.equals(where.columnName) &&
        Objects.equals(value, where.value) &&
        operator == where.operator;
  }

  @Override
  public int hashCode() {
    return Objects.hash(columnName, value, operator);
  }

  @Override
  public String toString() {
    return String.format("Where{columnName='%s', value=%s, operator=%s}", columnName, value, operator);
  }

  /**
   * Operadores disponíveis para cláusulas WHERE.
   */
  public enum Operator {
    LIKE("%"),
    EQUALS("="),
    NOT_EQUALS("!="),
    LESS_THAN("<"),
    GREATER_THAN(">"),
    LESS_THAN_OR_EQUALS("<="),
    GREATER_THAN_OR_EQUALS(">="),
    IN("IN"),
    NOT_IN("NOT IN"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL");

    private final String symbol;

    Operator(String symbol) {
      this.symbol = symbol;
    }

    public String getSymbol() {
      return symbol;
    }

    /**
     * Verifica se o operador permite valor nulo.
     */
    public boolean isNullAllowed() {
      return this == IS_NULL || this == IS_NOT_NULL;
    }

    /**
     * Verifica se o operador é válido para o tipo do valor.
     */
    public boolean isValidForType(Class<?> type) {
      if (type == null)
        return false;

      // Operadores que só fazem sentido para números
      if (this == LESS_THAN || this == GREATER_THAN ||
          this == LESS_THAN_OR_EQUALS || this == GREATER_THAN_OR_EQUALS) {
        return Number.class.isAssignableFrom(type) ||
            type.isPrimitive() && type != boolean.class;
      }

      return true;
    }
  }
}