package com.example.user.entity;

import com.example.user.enums.Status;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@Table(name = "role", schema = "eis_auth")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(name = "role_name", nullable = false, length = 255)
    private String roleName;

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
}
