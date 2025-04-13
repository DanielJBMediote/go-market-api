package gomarketapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.CommentBean;

@Repository
public interface CommentRepository extends JpaRepository<CommentBean, Long>, JpaSpecificationExecutor<CommentBean> {

}
