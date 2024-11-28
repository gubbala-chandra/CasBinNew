package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class GLAccountCompanyCodePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "\"Type\"")
    private String type;

    @Column(name = "\"SAKNR\"")
    private String accountNumber;

    @Column(name = "\"BUKRS\"")
    private String companyCode;
}