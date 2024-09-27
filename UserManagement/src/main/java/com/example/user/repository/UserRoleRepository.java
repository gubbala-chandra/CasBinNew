package com.example.user.repository;

import com.example.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {


    @Query(value = "SELECT r.role_id , r.role_name FROM eis_auth.user_role ur JOIN eis_auth.role r on r.role_id = ur.role_id  WHERE ur.user_id = :userId", nativeQuery = true)
    public List<Object[]> findRoleNameByUserId(@Param("userId") Long userId);
}
