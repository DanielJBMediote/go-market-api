package gomarketapi.helpers;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import gomarketapi.model.Bean;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public final class WhereBuilder<T extends Bean> {

  private List<Where> where = new ArrayList<>();

  private Class<T> type;

  /**
   * Construtor padrão sem encadeamento.
   */
  public WhereBuilder() {
  }

  public WhereBuilder(Class<T> type) {
    this.type = type;
  }

  public void addWhere(Where where) {
    this.where.add(where);
  }

  public List<Where> getWhere() {
    return this.where;
  }

  /**
   * Converte uma string Uniform Resource Identifier (URI)
   * para JSON e transforma em um ArrayList<Where>.
   * O parâmetro chegará assim no request: "[{
   * 'column': 'id',
   * 'value': 2 (poderá ser String, Date, Number, Boolean ou até null),
   * 'operator': 'EQUALS' (ou outros valores do enum Operator)
   * }]"
   *
   * @param whereEncodedURI String JSON (URI) contendo a lista de condições.
   * @return Um objeto WhereBuilder para encadeamento.
   */
  public WhereBuilder<T> parseWhereEncodedURIParams(String whereEncodedURI) {
    // System.out.println("Params Encrypted: " + params);
    if (whereEncodedURI == null || whereEncodedURI.isBlank()) {
      return this; // Retorna sem erro para evitar NullPointerException
    }

    try {
      String decodedWhere = URLDecoder.decode(whereEncodedURI, StandardCharsets.UTF_8);
      // System.out.println("Params Decrypted: " + decodedWhere);
      ObjectMapper objectMapper = new ObjectMapper();

      // Configuração para ignorar propriedades desconhecidas (evita erros se o JSON
      // tiver campos extras)
      objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      // Converte o JSON para List<Where> com validação básica
      this.where = objectMapper.readValue(decodedWhere, new TypeReference<List<Where>>() {
      });

      // Valida se os campos dos filtros são válidos (opcional)
      validateWhereFields(this.where);

      for (Where w : this.where) {
        System.out.println(w.toString());
      }

      return this;
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to convert 'where': " + e.getMessage(), e);
    }
  }

  // Método auxiliar para validar campos encadeados (ex: "owner.id")
  private void validateWhereFields(List<Where> whereList) {
    for (Where where : whereList) {
      // Validações básicas do Where
      if (where.getColumnName() == null || where.getColumnName().isBlank()) {
        throw new IllegalArgumentException("'columnName' cannot be null or blank.");
      }
      if (where.getValue() == null) {
        throw new IllegalArgumentException("'value' cannot be null.");
      }
      if (where.getOperator() == null) {
        throw new IllegalArgumentException("'operator' cannot be null.");
      }
      if (type == null) {
        throw new IllegalArgumentException("Type cannot be null.");
      }
      // Encontra o campo no Bean (ou sub-Beans) e valida o tipo do value
      Field field = findFieldInType(type, where.getColumnName());
      if (field == null) {
        throw new IllegalArgumentException(
            "Field '" + where.getColumnName() + "' does not exists in " + type.getSimpleName());
      }

      // Valida compatibilidade do tipo do value com o tipo do campo
      validateValueType(field, where.getValue());
    }
  }

  /**
   * Busca recursivamente um campo pelo nome em um Bean, incluindo superclasses e
   * sub-Beans.
   */
  private Field findFieldInType(Class<?> clazz, String fieldName) {
    // Verifica se o campo está no caminho aninhado (ex: "owner.id")
    String[] parts = fieldName.split("\\.");
    String currentFieldName = parts[0];

    try {
      // Busca o campo na classe atual e suas superclasses
      Field field = findFieldInClassHierarchy(clazz, currentFieldName);
      if (field == null) {
        return null;
      }

      // Se houver partes aninhadas (ex: "id" em "owner.id"), continua a busca
      if (parts.length > 1) {
        if (!Bean.class.isAssignableFrom(field.getType())) {
          throw new IllegalArgumentException(
              "Field '" + currentFieldName + "' is not a Bean and cannot have subfields..");
        }
        String remainingPath = fieldName.substring(fieldName.indexOf('.') + 1);
        return findFieldInType(field.getType(), remainingPath);
      }

      return field;
    } catch (SecurityException e) {
      throw new RuntimeException("Erro when access the field '" + fieldName + "'", e);
    }
  }

  /**
   * Busca um campo na classe e em todas as suas superclasses.
   */
  private Field findFieldInClassHierarchy(Class<?> clazz, String fieldName) {
    Class<?> currentClass = clazz;
    while (currentClass != null && currentClass != Object.class) {
      try {
        Field field = currentClass.getDeclaredField(fieldName);
        field.setAccessible(true); // Para garantir acesso a campos privados
        return field;
      } catch (NoSuchFieldException e) {
        currentClass = currentClass.getSuperclass(); // Procura na superclasse
      }
    }
    return null;
  }

  /**
   * Valida se o valor é compatível com o tipo do campo.
   * Aceita qualquer Number se o campo for numérico.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  private void validateValueType(Field field, Object value) {
    Class<?> fieldType = field.getType();
    Class<?> valueType = value.getClass();

    // Caso 1: Ambos são números -> compatíveis
    if (isNumericType(fieldType) && isNumericType(valueType)) {
      return; // Aceita qualquer combinação (Integer -> Long, Double -> Integer, etc.)
    }

    // Caso 2: Campo é primitivo -> converte para wrapper
    if (fieldType.isPrimitive()) {
      fieldType = convertPrimitiveToWrapper(fieldType);
    }

    // Caso 3: Campo é um Enum -> verifica se o valor é uma chave válida
    if (fieldType.isEnum()) {
      if (value instanceof String) {
        try {
          Enum.valueOf((Class<Enum>) fieldType, (String) value);
          return; // Valor é uma chave válida do Enum
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException(
              "Invalid enum value for field '" + field.getName() + "'. " +
                  "Expected one of: " + String.join(", ", getEnumValues(fieldType)) + ", " +
                  "Received: " + value);
        }
      } else {
        throw new IllegalArgumentException(
            "Invalid type for enum field '" + field.getName() + "'. " +
                "Expected: String, Received: " + valueType.getSimpleName());
      }
    }

    // Caso 4: Verifica compatibilidade padrão (incluindo herança)
    if (!fieldType.isAssignableFrom(valueType)) {
      throw new IllegalArgumentException(
          "Invalid type of field '" + field.getName() + "'. " +
              "Expected: " + fieldType.getSimpleName() + ", " +
              "Received: " + valueType.getSimpleName());
    }
  }

  // Método auxiliar para obter os valores válidos de um Enum
  private List<String> getEnumValues(Class<?> enumType) {
    List<String> values = new ArrayList<>();
    for (Object constant : enumType.getEnumConstants()) {
      values.add(constant.toString());
    }
    return values;
  }

  /**
   * Verifica se um tipo é numérico (Number ou primitivo numérico).
   */
  private boolean isNumericType(Class<?> type) {
    return Number.class.isAssignableFrom(type) ||
        type == int.class || type == long.class || type == double.class ||
        type == float.class || type == short.class || type == byte.class;
  }

  /**
   * Converte tipos primitivos para suas wrappers (ex: int -> Integer).
   */
  private Class<?> convertPrimitiveToWrapper(Class<?> primitiveType) {
    if (primitiveType == int.class)
      return Integer.class;
    if (primitiveType == long.class)
      return Long.class;
    if (primitiveType == double.class)
      return Double.class;
    if (primitiveType == boolean.class)
      return Boolean.class;
    // Adicione outros primitivos conforme necessário
    return primitiveType;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Specification<T> build() {
    return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      for (Where condition : where) {
        String[] nestedPath = condition.getColumnName().split("\\.");
        Path<Object> path = root.get(nestedPath[0]);

        // Navega por propriedades aninhadas (ex: "owner.id")
        for (int i = 1; i < nestedPath.length; i++) {
          path = path.get(nestedPath[i]);
        }

        // Aplica o operador correspondente
        switch (condition.getOperator()) {
          case EQUALS:
            predicates.add(builder.equal(path, condition.getValue()));
            break;
          case NOT_EQUALS:
            predicates.add(builder.notEqual(path, condition.getValue()));
            break;
          case GREATER_THAN:
          case GREATER_THAN_OR_EQUALS:
          case LESS_THAN:
          case LESS_THAN_OR_EQUALS:
            // Tratamento especial para campos numéricos
            if (path.getJavaType() == BigDecimal.class) {
              BigDecimal value = new BigDecimal(condition.getValue().toString());
              if (condition.getOperator() == Where.Operator.GREATER_THAN) {
                predicates.add(builder.greaterThan(path.as(BigDecimal.class), value));
              } else if (condition.getOperator() == Where.Operator.GREATER_THAN_OR_EQUALS) {
                predicates.add(builder.greaterThanOrEqualTo(path.as(BigDecimal.class), value));
              } else if (condition.getOperator() == Where.Operator.LESS_THAN) {
                predicates.add(builder.lessThan(path.as(BigDecimal.class), value));
              } else {
                predicates.add(builder.lessThanOrEqualTo(path.as(BigDecimal.class), value));
              }
            } else {
              // Fallback genérico para outros tipos comparáveis
              predicates
                  .add(builder.greaterThanOrEqualTo(path.as(Comparable.class), (Comparable) condition.getValue()));
            }
            break;
          case LIKE:
            predicates.add(builder.like(path.as(String.class), "%" + condition.getValue() + "%"));
            break;
          case IN:
            predicates.add(path.in((Collection<?>) condition.getValue()));
            break;
          case IS_NULL:
            predicates.add(builder.isNull(path));
            break;
          case IS_NOT_NULL:
            predicates.add(builder.isNotNull(path));
            break;
          default:
            throw new IllegalArgumentException("Operator not supported : " + condition.getOperator()
                + ". Please create new one in gomarketapi.Where.java.");
        }
      }

      // Combina todos os predicados com AND
      return builder.and(predicates.toArray(new Predicate[0]));
    };
  }

}
