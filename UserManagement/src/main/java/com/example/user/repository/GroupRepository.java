package com.example.user.repository;

import com.example.user.entity.Groups;
import com.example.user.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Groups, Long>, JpaSpecificationExecutor<Groups> {

    @Transactional(readOnly = true)
    Optional<Groups> findByGroupName(String groupName);

    @Transactional(readOnly = true)
    public List<Groups> findByStatus(Status status);
}
