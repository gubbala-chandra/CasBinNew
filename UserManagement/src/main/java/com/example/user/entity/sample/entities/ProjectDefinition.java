package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(name = "ProjectDefinition")
@Table(name = "\"ProjectDefinition\"", schema = "s4Data")
@IdClass(ProjectDefinitionPK.class)
public class ProjectDefinition {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"PROJECT_DEFINITION\"")
    private String projectDefinitionNumber;

    @Column(name = "\"START\"")
    private LocalDate startDate;

    @Column(name = "\"FINISH\"")
    private LocalDate finishDate;

    @Column(name = "\"CONTROLLING_AREA\"")
    private String controllingArea;

    @Column(name = "\"PROJECT_CURRENCY\"")
    private String currency;

    @Column(name = "\"DELETION_FLAG\"")
    private String deletionFlag;
}