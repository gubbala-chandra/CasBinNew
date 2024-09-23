/*
package com.example.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "user")
    private List<UserGroup> userGroups;

    @OneToMany(mappedBy = "user")
    private List<UserRole> userRoles;

    // Getters and Setters
}
*/
