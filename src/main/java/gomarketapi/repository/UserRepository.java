package gomarketapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gomarketapi.model.UserBean;

@Repository
public interface UserRepository extends JpaRepository<UserBean, Long>, JpaSpecificationExecutor<UserBean> {
  Optional<UserBean> findByUsername(String username);
  boolean existsByEmail(String email);
}