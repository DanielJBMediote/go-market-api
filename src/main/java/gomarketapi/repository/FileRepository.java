package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.FileBean;

@Repository
public interface FileRepository extends JpaRepository<FileBean, Long>, JpaSpecificationExecutor<FileBean> {

}
