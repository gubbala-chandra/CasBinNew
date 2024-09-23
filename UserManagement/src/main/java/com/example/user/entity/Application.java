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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "application", schema = "eis_auth")
public class Application {

    @Id
    private String applicationId;

    @Column(nullable = false)
    private String applicationName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private AccessControlTemplate accessControlTemplate;

    @OneToMany(mappedBy = "application")
    private List<Resource> resources;

    @OneToMany(mappedBy = "application")
    private List<CasbinRule> casbinRules;

    // Getters and Setters
}*/
