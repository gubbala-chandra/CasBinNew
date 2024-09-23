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
@Table(name = "access_control_template", schema = "eis_auth")
public class AccessControlTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @Column(nullable = false)
    private String templateName;

    @Column(columnDefinition = "TEXT")
    private String template;

    @OneToMany(mappedBy = "accessControlTemplate")
    private List<Application> applications;

    // Getters and Setters
}*/
