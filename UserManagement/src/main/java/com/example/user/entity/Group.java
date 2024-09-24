package com.example.user.entity;

import com.example.user.enums.Status;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "groups", schema = "eis_auth")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(name = "group_name", nullable = false, length = 255)
    private String groupName;

    @Column(name = "description", nullable = true, length = 1000)
    private String description;

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

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupRole> groupRoles = new ArrayList<>();

    public void addGroupRole(GroupRole groupRole) {
        this.groupRoles.add(groupRole);
        groupRole.setGroup(this);
    }
}
