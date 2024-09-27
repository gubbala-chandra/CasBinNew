package com.example.user.repository;

import com.example.user.dto.GroupNameDto;
import com.example.user.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query(value = "SELECT g.group_id , g.group_name FROM eis_auth.user_group ug JOIN eis_auth.groups g on g.group_id = ug.group_id WHERE ug.user_id = :userId", nativeQuery = true)
    public List<Object[]> findGroupNameByUserId(@Param("userId") Long userId);
}
