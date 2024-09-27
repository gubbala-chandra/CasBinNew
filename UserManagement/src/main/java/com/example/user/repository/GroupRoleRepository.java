package com.example.user.repository;

import com.example.user.dto.RoleIdNameDto;
import com.example.user.entity.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {


    @Query(value = "SELECT r.role_id , r.role_name FROM eis_auth.group_roles gr JOIN eis_auth.roles r on r.role_id = gr.role_id  WHERE gr.group_id = :groupId", nativeQuery = true)
    List<Object[]> findRoleNamesByGroupId(@Param("groupId") Long groupId);
}
