package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.StoreBean;

@Repository
public interface StoreRepository extends JpaRepository<StoreBean, Long>, JpaSpecificationExecutor<StoreBean> {
}
