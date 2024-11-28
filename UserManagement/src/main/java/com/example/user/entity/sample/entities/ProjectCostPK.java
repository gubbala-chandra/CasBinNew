package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectCostPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "\"WBSElement\"")
    private String wBSElement;

    @Column(name = "\"FiscalYear\"")
    private String fiscalYear;

    @Column(name = "\"ValueType\"")
    private String valueType;

    @Column(name = "\"Version\"")
    private String version;

    @Column(name = "\"CostElement\"")
    private String costElement;

    @Column(name = "\"COSubkey\"")
    private String cOSubkey;

    @Column(name = "\"BusinessTransaction\"")
    private String businessTransaction;

    @Column(name = "\"DebitCreditInd\"")
    private String debitCreditInd;

    @Column(name = "\"TransactionCurrency\"")
    private String transactionCurrency;

    @Column(name = "\"FunctionalArea\"")
    private String functionalArea;

    @Column(name = "\"Fund\"")
    private String fund;

    @Column(name = "\"GrantNumber\"")
    private String grantNumber;

    @Column(name = "\"ObjectNumber\"")
    private String objectNumber;
}