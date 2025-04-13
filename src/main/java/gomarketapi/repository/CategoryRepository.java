package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.CategoryBean;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryBean, Long>, JpaSpecificationExecutor<CategoryBean> {
}
