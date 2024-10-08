package com.example.user.repository;

import com.example.user.entity.Role;
import com.example.user.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {
    @Transactional(readOnly = true)
    public Optional<Role> findByRoleName(String roleName);

    @Transactional(readOnly = true)
    public List<Role> findByStatus(Status status);
}
