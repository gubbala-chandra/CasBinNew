package com.example.user.entity.sample.entities;

import javax.persistence.Column;
import java.io.Serializable;

public class ProjectDefinitionPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "\"Type\"")
    private String type;

    @Column(name = "\"PROJECT_DEFINITION\"")
    private String projectDefinitionNumber;
}
