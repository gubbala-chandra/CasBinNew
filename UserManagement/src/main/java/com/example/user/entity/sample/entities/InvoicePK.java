package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoicePK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    // @Column(name = "\"QDate\"")
    // private LocalDate qDate;

    @Column(name = "\"AuxSysId\"")
    private String auxSysId;

    @Column(name = "\"DivisionId\"")
    private String divisionId;

    @Column(name = "\"SalesOrgCode\"")
    private String salesOrgCode;

    @Column(name = "\"SalesOrderNumber\"")
    private String salesOrderNumber;

    @Column(name = "\"BillingDocumentNumber\"")
    private String billingDocumentNumber;
}