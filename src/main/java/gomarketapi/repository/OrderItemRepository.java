package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.OrderItemBean;

@Repository
public interface OrderItemRepository
                extends JpaRepository<OrderItemBean, Long>, JpaSpecificationExecutor<OrderItemBean> {

}
