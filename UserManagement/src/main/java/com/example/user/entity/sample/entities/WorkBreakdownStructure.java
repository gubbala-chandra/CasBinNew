package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "WorkBreakdownStructure")
@Table(name = "\"WorkBreakdownStructure\"", schema = "s4Data")
@IdClass(WorkBreakdownStructurePK.class)
public class WorkBreakdownStructure {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"WBS_ELEMENT\"")
    private String wbsElement;

    @Column(name = "\"PROJECT_DEFINITION\"")
    private String projectDefinitionNumber;

    @Column(name = "\"LEVEL\"")
    private String level;

    @Column(name = "\"PLANNING_INDICATOR\"")
    private String allowCostForcasting;

    @Column(name = "\"ACCOUNTING_INDICATOR\"")
    private String allowActualCommitmentPosting;

    @Column(name = "\"BILLING_INDICATOR\"")
    private String allowBilling;

    @Column(name = "\"PRIORITY\"")
    private String priority;

    @Column(name = "\"OBJECTCLASS\"")
    private String objectClass;

    @Column(name = "\"DELETION_FLAG\"")
    private String deletionFlag;

    @Column(name = "\"REQUEST_CCTR\"")
    private String requestCostCenter;

    @Column(name = "\"RESPONSIBLE_CCTR\"")
    private String responsibleCostCenter;

//	@Column(name = "\"STATUS\"")
//	private String status;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumns({ @JoinColumn(name = "\"WBS_ELEMENT\"", referencedColumnName = "\"Id\"", insertable = false, updatable = false),
//			@JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),
//
//	})
//	private CodingBlockEntry codingBlockEntry;
}