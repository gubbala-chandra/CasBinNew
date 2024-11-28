package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "AssetDepreciationArea")
@Table(name = "\"AssetDepreciationArea\"", schema = "s4Data")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AssetDepreciationAreaPK.class)
public class AssetDepreciationArea {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"ASSET-SUBNUMBER\"")
    private String mainAndSubNumber;

    @Id
    @Column(name = "\"AREA\"")
    private String realDepreciationArea;

    @Column(name = "\"DESCRIPT\"")
    private String shortNameForDepreciationArea;

    @Column(name = "\"DEACTIVATE\"")
    private String depreciationAreaIsDeactivated;

    @Column(name = "\"DEP_KEY\"")
    private String depreciationKey;

    @Column(name = "\"ULIFE_YRS\"")
    private String plannedUsefulLifeInYears;

    @Column(name = "\"ULIFE_PRDS\"")
    private String plannedUsefulLifeInPeriods;

    @Column(name = "\"EXP_ULIFE_YRS\"")
    private String expiredUsefulLifeInYears;

    @Column(name = "\"EXP_ULIFE_PRDS\"")
    private String expiredUsefulLifeInPeriods;

    @Column(name = "\"EXP_ULIFE_SDEP_YRS\"")
    private String expiredUsefulLifeInYearsFromSDep;

    @Column(name = "\"EXP_ULIFE_SDEP_PRDS\"")
    private String expiredUsefulLifeInPeriodsFromSDep;

    @Column(name = "\"ORIG_ULIFE_YRS\"")
    private String originalUsefulLifeInYears;

    @Column(name = "\"ORIG_ULIFE_PRDS\"")
    private String originalUsefulLifeInPeriods;

    @Column(name = "\"CHANGE_YR\"")
    private String depreciationKeyForChangeoverYear;

    @Column(name = "\"DEP_UNITS\"")
    private String numberOfUnitsDepreciated;

    @Column(name = "\"ODEP_START_DATE\"")
    private LocalDate depreciationStartDate;

    @Column(name = "\"SDEP_START_DATE\"")
    private LocalDate specialDepreciationStartDate;

    @Column(name = "\"INTEREST_START_DATE\"")
    private LocalDate interestCalculationStartDate;

    @Column(name = "\"READINESS\"")
    private LocalDate operatingReadinessDate;

    @Column(name = "\"INDEX\"")
    private String indexSeriesForReplacementValues;

    @Column(name = "\"AGE_INDEX\"")
    private String indexSeriesForReplacementValuesByAge;

    @Column(name = "\"VAR_DEP_PORTION\"", precision = 23, scale = 2)
    private BigDecimal variableDepreciationPortion;

    @Column(name = "\"SCRAPVALUE\"", precision = 23, scale = 2)
    private BigDecimal assetScrapValue;

    @Column(name = "\"CURRENCY\"")
    private String currencyKey;

    @Column(name = "\"CURRENCY_ISO\"")
    private String isoCurrencyCode;

    @Column(name = "\"NEG_VALUES\"")
    private String negativeValuesAllowed;

    @Column(name = "\"GRP_ASSET\"")
    private String groupAsset;

    @Column(name = "\"GRP_ASSET_SUBNO\"")
    private String groupAssetSubnumber;

    @Column(name = "\"ACQ_YR\"")
    private String acquisitionYear;

    @Column(name = "\"ACQ_PRD\"")
    private String acquisitionMonth;

    @Column(name = "\"SCRAPVALUE_PRCTG\"", precision = 23, scale = 2)
    private BigDecimal scrapValuePercentage;

    @Column(name = "\"FROM_DATE\"")
    private LocalDate validityStartDate;

    @Column(name = "\"TO_DATE\"")
    private LocalDate validityEndDate;

}
