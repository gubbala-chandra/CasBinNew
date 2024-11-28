package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "GLAccountCompanyCode")
@Table(name = "\"GLAccountCompanyCode\"", schema = "s4Data")
@IdClass(GLAccountCompanyCodePK.class)
public class GLAccountCompanyCode {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"SAKNR\"")
    private String accountNumber;

    @Id
    @Column(name = "\"BUKRS\"")
    private String companyCode;

    @Column(name = "\"BEGRU\"")
    private String authorizationGroup;

    @Column(name = "\"BUSAB\"")
    private String accountingClerkAbbreviation;

    @Column(name = "\"FSTAG\"")
    private String fieldStatusGroup;

    @Column(name = "\"HBKID\"")
    private String shortKeyForHouseBank;

    @Column(name = "\"HKTID\"")
    private String iDForAccountDetails;

    @Column(name = "\"MITKZ\"")
    private String reconciliationAccount;

    @Column(name = "\"MWSKZ\"")
    private String taxCodeOnSalesPurchases;

    @Column(name = "\"WAERS\"")
    private String currencyKey;

    @Column(name = "\"XINTB\"")
    private String onlyPostedToAutomatically;

    @Column(name = "\"XKRES\"")
    private String lineItemsDisplay;

    @Column(name = "\"XNKON\"")
    private String supplementForAutomaticPostings;

    @Column(name = "\"XOPVW\"")
    private String openItemManagement;

    @Column(name = "\"XSPEB\"")
    private String blockedForPosting;

    @Column(name = "\"ZUAWA\"")
    private String keyForSorting;

    @Column(name = "\"XMWNO\"")
    private String taxCodeRequired;

    @Column(name = "\"XSALH\"")
    private String manageBalancesInLocalCurrencyOnly;

    @Column(name = "\"TOGRU\"")
    private String toleranceGroup;

    @Column(name = "\"KATYP\"")
    private String costElementCategory;
}
