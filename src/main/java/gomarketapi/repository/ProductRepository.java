package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.ProductBean;

@Repository
public interface ProductRepository extends JpaRepository<ProductBean, Long>, JpaSpecificationExecutor<ProductBean> {
}