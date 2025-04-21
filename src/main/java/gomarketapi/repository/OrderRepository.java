package gomarketapi.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import gomarketapi.model.OrderBean;

@Repository
public interface OrderRepository extends JpaRepository<OrderBean, Long>, JpaSpecificationExecutor<OrderBean> {
    // Consulta para soma do valor total por loja (performance)
    @Query(value = """
            SELECT COALESCE(SUM(oi.quantity * oi.unitPrice), 0)
            FROM OrderBean o
            JOIN o.items oi
            JOIN oi.product p
            WHERE o.status = 'COMPLETED'
            AND p.store.id = :storeId
            AND o.createdAt BETWEEN :startDate AND :endDate
            """)
    BigDecimal sumRevenueByPeriod(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Consulta para soma do valor total de todas as lojas (performance)
    @Query(value = """
            SELECT COALESCE(SUM(oi.quantity * oi.unitPrice), 0)
            FROM OrderBean o
            JOIN o.items oi
            JOIN oi.product p
            JOIN p.store s
            WHERE o.status = 'COMPLETED'
            AND s.owner.id = :ownerId
            AND o.createdAt BETWEEN :startDate AND :endDate""")
    BigDecimal sumAllStoresRevenueByPeriod(
            @Param("ownerId") Long ownerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Consulta para total de unidades vendidas por loja
    @Query(value = """
            SELECT COALESCE(SUM(oi.quantity), 0)
            FROM OrderBean o
            JOIN o.items oi
            JOIN oi.product p
            WHERE o.status = 'COMPLETED'
            AND p.store.id = :storeId
            AND o.createdAt BETWEEN :startDate AND :endDate
            """)
    Integer sumSalesByPeriod(
            @Param("storeId") Long storeId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Consulta para total de unidades vendidas de todas loja
    @Query(value = """
            SELECT COALESCE(SUM(oi.quantity), 0)
              FROM OrderBean o
              JOIN o.items oi
              JOIN oi.product p
              JOIN p.store s
              WHERE o.status = 'COMPLETED'
              AND s.owner.id = :ownerId
              AND o.createdAt BETWEEN :startDate AND :endDate
                  """)
    Integer sumAllStoresSalesByPeriod(
            @Param("ownerId") Long ownerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Seleciona o unides e vendas do dia por loja
    @Query(value = """
            SELECT
            EXTRACT(DAY FROM o.created_at) AS day,
                    SUM(oi.quantity) AS units,
                    SUM(oi.quantity * oi.unit_price) AS revenue
                    FROM orders o
                    JOIN order_items oi ON o.id = oi.order_id
                    JOIN products p
                    WHERE p.store_id = :storeId
                    AND o.status = 'COMPLETED'
                    AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH FROM CURRENT_DATE)
                    AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
                    GROUP BY EXTRACT(DAY FROM o.created_at)
                    """, nativeQuery = true)
    List<Map<String, Object>> getDailySalesChartData(@Param("storeId") Long storeId);

    // Seleciona o unides e vendas do dia de todas as lojas
    @Query(value = """
            SELECT
                EXTRACT(DAY FROM o.created_at) AS day,
                SUM(oi.quantity) AS units,
                SUM(oi.quantity * oi.unit_price) AS revenue
            FROM orders o
            JOIN order_items oi ON o.id = oi.order_id
            JOIN products p ON oi.product_id = p.id  -- JOIN explícito adicionado
            JOIN stores s ON p.store_id = s.id
            WHERE s.owner_id = :ownerId
            AND o.status = 'COMPLETED'
            AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH FROM CURRENT_DATE)
            AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
            GROUP BY EXTRACT(DAY FROM o.created_at)
            """, nativeQuery = true)
    List<Map<String, Object>> getAllStoresDailySalesChartData(@Param("ownerId") Long ownerId);

}
