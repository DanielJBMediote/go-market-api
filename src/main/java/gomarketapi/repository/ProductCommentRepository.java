package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.ProductCommentBean;

@Repository
public interface ProductCommentRepository
        extends JpaRepository<ProductCommentBean, Long>, JpaSpecificationExecutor<ProductCommentBean> {

}
