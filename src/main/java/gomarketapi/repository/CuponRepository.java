package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.CuponBean;

@Repository
public interface CuponRepository extends JpaRepository<CuponBean, Long>, JpaSpecificationExecutor<CuponBean> {

}
