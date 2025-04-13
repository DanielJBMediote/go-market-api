package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.ProductFileBean;

@Repository
public interface ProductFileRepository
    extends JpaRepository<ProductFileBean, Long>, JpaSpecificationExecutor<ProductFileBean> {

}
