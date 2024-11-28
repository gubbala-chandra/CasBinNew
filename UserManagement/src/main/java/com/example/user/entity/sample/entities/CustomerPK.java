package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "\"AuxSysId\"")
    private String auxSysId;

    @Column(name = "\"AuxSysCustomerNumber\"")
    private String auxSysCustomerNumber;

    @Column(name = "\"DivisionId\"")
    private String divisionId;
}

