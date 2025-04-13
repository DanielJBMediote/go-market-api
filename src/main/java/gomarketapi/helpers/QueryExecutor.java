package gomarketapi.helpers;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

public class QueryExecutor {
  private final JdbcTemplate jdbcTemplate;
  private final EntityManager entityManager;

  public QueryExecutor(JdbcTemplate jdbcTemplate, EntityManager entityManager) {
    this.jdbcTemplate = jdbcTemplate;
    this.entityManager = entityManager;
  }

  /**
   * Executa query com mapeamento automático (usa EntityManager)
   */
  @SuppressWarnings("unchecked")
  public <T> List<T> execute(String sql, Class<T> resultClass) {
    try {
      return entityManager.createNativeQuery(sql, resultClass).getResultList();
    } catch (PersistenceException e) {
      throw new RuntimeException("Erro ao executar query via JPA", e);
    }
  }

  /**
   * Executa query com mapeamento customizado (usa JdbcTemplate)
   */
  public <T> List<T> execute(String sql, RowMapper<T> rowMapper) {
    return jdbcTemplate.query(sql, rowMapper);
  }

  /**
   * Executa query e retorna lista de mapas (para consultas ad-hoc)
   */
  public List<Map<String, Object>> executeForMap(String sql) {
    return jdbcTemplate.queryForList(sql);
  }
}