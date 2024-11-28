package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Asset")
@Table(name = "\"Asset\"", schema = "s4Data")
@IdClass(AssetPK.class)
public class Asset {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"ASSET-SUBNUMBER\"")
    private String mainAndSubNumber;

    @Column(name = "\"ASSET\"")
    private String asset;

    @Column(name = "\"SUBNUMBER\"")
    private String subNumber;

    @Column(name = "\"ASSETCLASS\"")
    private String assetClass;

    @Column(name = "\"CAP_DATE\"")
    private LocalDate assetCapitalizationDate;

    @Column(name = "\"DEACT_DATE\"")
    private LocalDate deactivationDate;

    @Column(name = "\"INITIAL_ACQ\"")
    private LocalDate firstAcquisitionDate;

    @Column(name = "\"INITIAL_ACQ_YR\"")
    private String firstAcquisitionFiscalYear;

    @Column(name = "\"COSTCENTER\"")
    private String costCenter;

    @Column(name = "\"RESP_CCTR\"")
    private String costCenterResponsible;

    @Column(name = "\"LOCATION\"")
    private String assetLocation;

    @Column(name = "\"FUND\"")
    private String fund;

    @Column(name = "\"FUNC_AREA\"")
    private String functionalArea;

    @Column(name = "\"GRANT_NBR\"")
    private String grant;

    @Column(name = "\"FUNC_AREA_LONG\"")
    private String functionalAreaLong;

    @Column(name = "\"FUNDS_CTR\"")
    private String fundsCenter;

    @Column(name = "\"WBS_ELEMENT_COST\"")
    private String wbsElement;

    @Column(name = "\"EVALGROUP1\"")
    private String evaluationGroup1;

    @Column(name = "\"EVALGROUP2\"")
    private String evaluationGroup2;

    @Column(name = "\"EVALGROUP3\"")
    private String evaluationGroup3;

    @Column(name = "\"EVALGROUP4\"")
    private String evaluationGroup4;

    @Column(name = "\"INV_REASON\"")
    private String reasonForInvestment;

    @Column(name = "\"ENVIR_INVEST\"")
    private String reasonForEnvironmentalInvestment;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumns({ @JoinColumn(name = "\"ASSET-SUBNUMBER\"", referencedColumnName = "\"ASSET-SUBNUMBER\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false) })
    private Collection<AssetDepreciationArea> depreciationAreas = new ArrayList<>();
}
