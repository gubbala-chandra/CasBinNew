package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "CodingBlockEntry")
@Table(name = "\"CodingBlockEntry\"", schema = "s4Data")
@IdClass(CodingBlockEntryPK.class)
// @EntityListeners(CodingBlockEntryListener.class)
// @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class CodingBlockEntry {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"Id\"")
    private String id;

    @Column(name = "\"CompanyCode\"")
    private String companyCode;

    @Column(name = "\"Description\"")
    private String description;

    @Column(name = "\"EnteredBy\"")
    private String enteredBy;

    @Column(name = "\"EnteredOn\"")
    private LocalDate enteredOn;// ZonedDateTime

    @Column(name = "\"GroupSet\"")
    private String groupSet;

    @Column(name = "\"GroupId\"")
    private String groupId;

    @Column(name = "\"Level\"")
    private String level;

    @Column(name = "\"LongDescription\"")
    private String longDescription;

    @Column(name = "\"Name\"")
    private String name;

    @Column(name = "\"OrgNumber\"")
    private String orgNumber;

    @Column(name = "\"Status\"")
    private String status;

    @Column(name = "\"TaxJurisdiction\"")
    private String taxJurisdiction;

    @Column(name = "\"ValidFromDate\"")
    private LocalDate validFromDate;

    @Column(name = "\"ValidToDate\"")
    private LocalDate validToDate;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"Type\"", insertable = false, updatable = false)
    private CodingBlock codingBlock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "\"OrgNumber\"", insertable = false, updatable = false)
    private OrgUnit orgUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"GroupSet\"", referencedColumnName = "\"GroupSet\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"GroupId\"", referencedColumnName = "\"GroupId\"", insertable = false, updatable = false) })
    private Group hierarchy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"Id\"", referencedColumnName = "\"KOSTL\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),

    })
    private CostCenter costCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"Id\"", referencedColumnName = "\"SAKNR\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),

    })
    private GLAccount gLAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"Id\"", referencedColumnName = "\"ORDERID\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),

    })
    private InternalOrder internalOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"Id\"", referencedColumnName = "\"PROJECT_DEFINITION\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),

    })
    private ProjectDefinition projectDefinition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"Id\"", referencedColumnName = "\"WBS_ELEMENT\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),

    })
    private WorkBreakdownStructure workBreakdownStructure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "\"Id\"", referencedColumnName = "\"ASSET-SUBNUMBER\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false),

    })
    private Asset asset;
}