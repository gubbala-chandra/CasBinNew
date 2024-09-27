package com.example.user.entity;

import com.example.user.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user", schema = "eis_auth")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;

    @Column(name = "status", nullable = false, length=10)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Column(name = "created_by", nullable = false, length = 30)
    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_time", nullable = false)
    private Timestamp createdTime;

    @Column(name = "updated_by", nullable = true, length = 30)
    private String updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_time", nullable = false)
    private Timestamp updatedTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserGroup> userGroups = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserRole> userRoles = new ArrayList<>();

    public void addUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
        userGroup.setUser(this);
    }

    public void addUserRole(UserRole userRole) {
        this.userRoles.add(userRole);
        userRole.setUser(this);
    }
}
