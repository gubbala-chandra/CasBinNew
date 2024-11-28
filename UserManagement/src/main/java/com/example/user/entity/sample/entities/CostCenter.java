package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CostCenter")
@Table(name = "\"CostCenter\"", schema = "s4Data")
@IdClass(CostCenterPK.class)
//@EntityListeners(CostCenterListener.class)
public class CostCenter {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"KOSTL\"")
    private String costCenter;

    @Column(name = "\"KOKRS\"")
    private String controllingArea;

    @Column(name = "\"KOSAR\"")
    private String costCenterCategory;

    @Column(name = "\"KOMPL\"")
    private String completionFlag;

    @Column(name = "\"VERAK\"")
    private String personResponsible;

    @Column(name = "\"WAERS\"")
    private String currencyKey;

    @Column(name = "\"ABTEI\"")
    private String department;

    @Column(name = "\"BKZKP\"")
    private String lockForActualPrimaryPostings;

    @Column(name = "\"BKZKS\"")
    private String lockForActualSecondaryCosts;

    @Column(name = "\"BKZER\"")
    private String lockForActualRevenuePostings;

    @Column(name = "\"BKZOB\"")
    private String lockForCommitmentUpdate;

    @Column(name = "\"PKZKP\"")
    private String lockForPlanPrimaryPostings;

    @Column(name = "\"PKZKS\"")
    private String lockForPlanSecondaryCosts;

    @Column(name = "\"PKZER\"")
    private String lockForPlanningRevenue;
}
