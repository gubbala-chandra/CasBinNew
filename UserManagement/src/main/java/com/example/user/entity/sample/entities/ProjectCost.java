package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "ProjectCost")
@Table(name = "\"ProjectCost\"", schema = "s4Data")
@IdClass(ProjectCostPK.class)
public class ProjectCost {
    @Id
    @Column(name = "\"WBSElement\"")
    private String wBSElement;
    @Id
    @Column(name = "\"FiscalYear\"")
    private String fiscalYear;
    @Id
    @Column(name = "\"ValueType\"")
    private String valueType;
    @Id
    @Column(name = "\"Version\"")
    private String version;
    @Id
    @Column(name = "\"CostElement\"")
    private String costElement;
    @Id
    @Column(name = "\"COSubkey\"")
    private String cOSubkey;
    @Id
    @Column(name = "\"BusinessTransaction\"")
    private String businessTransaction;
    @Id
    @Column(name = "\"DebitCreditInd\"")
    private String debitCreditInd;
    @Id
    @Column(name = "\"TransactionCurrency\"")
    private String transactionCurrency;
    @Id
    @Column(name = "\"FunctionalArea\"")
    private String functionalArea;
    @Id
    @Column(name = "\"Fund\"")
    private String fund;
    @Id
    @Column(name = "\"GrantNumber\"")
    private String grantNumber;
    @Id
    @Column(name = "\"ObjectNumber\"")
    private String objectNumber;

    @Column(name = "\"Period1\"", precision = 23, scale = 2)
    private BigDecimal period1;
    @Column(name = "\"Period2\"", precision = 23, scale = 2)
    private BigDecimal period2;
    @Column(name = "\"Period3\"", precision = 23, scale = 2)
    private BigDecimal period3;
    @Column(name = "\"Period4\"", precision = 23, scale = 2)
    private BigDecimal period4;
    @Column(name = "\"Period5\"", precision = 23, scale = 2)
    private BigDecimal period5;
    @Column(name = "\"Period6\"", precision = 23, scale = 2)
    private BigDecimal period6;
    @Column(name = "\"Period7\"", precision = 23, scale = 2)
    private BigDecimal period7;
    @Column(name = "\"Period8\"", precision = 23, scale = 2)
    private BigDecimal period8;
    @Column(name = "\"Period9\"", precision = 23, scale = 2)
    private BigDecimal period9;
    @Column(name = "\"Period10\"", precision = 23, scale = 2)
    private BigDecimal period10;
    @Column(name = "\"Period11\"", precision = 23, scale = 2)
    private BigDecimal period11;
    @Column(name = "\"Period12\"", precision = 23, scale = 2)
    private BigDecimal period12;
    @Column(name = "\"Period13\"", precision = 23, scale = 2)
    private BigDecimal period13;
    @Column(name = "\"Period14\"", precision = 23, scale = 2)
    private BigDecimal period14;
    @Column(name = "\"Period15\"", precision = 23, scale = 2)
    private BigDecimal period15;
    @Column(name = "\"Period16\"", precision = 23, scale = 2)
    private BigDecimal period16;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;
}

