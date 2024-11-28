package com.example.user.entity.sample.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Vendor")
@Table(name = "\"Vendor\"", schema = "s4Data")
// @IdClass(VendorPK.class)
public class Vendor {
    @Id
    @Column(name = "\"LIFNR\"", length = 10)
    private String accountNumber;

    @Column(name = "\"BUKRS\"", length = 4)
    private String companyCode;

    @Column(name = "\"BRSCH\"", length = 4)
    private String industryKey;

    @Column(name = "\"KTOKK\"", length = 4)
    private String accountGroup;

    @Column(name = "\"LAND1\"", length = 3)
    private String country;

    @Column(name = "\"NAME1\"", length = 35)
    private String contactName1;

    @Column(name = "\"NAME2\"", length = 35)
    private String contactName2;

    @Column(name = "\"ORT01\"", length = 35)
    private String city;

    @Column(name = "\"ORT02\"", length = 35)
    private String district;

    @Column(name = "\"PSTLZ\"", length = 10)
    private String postalCode;

    @Column(name = "\"REGIO\"", length = 3)
    private String region;

    // @Column(name = "\"SORTL\"", length = 10)

    @Column(name = "\"SPERR\"", length = 1)
    private String centralPostingBlock;

    @Column(name = "\"STCD1\"", length = 16)
    private String taxNumber;

    @Column(name = "\"STRAS\"", length = 35)
    private String streetNumber;

    @Column(name = "\"ADRNR\"", length = 10)
    private String address;

    @Column(name = "\"AKONT\"", length = 10)
    private String reconciliationAccount;

    @Column(name = "\"ZTERM\"", length = 4)
    private String termsOfPayment;

    @Column(name = "\"FDGRV\"", length = 10)
    private String planningGroup;

    @Column(name = "\"REPRF\"", length = 1)
    private String doubleInvoicesOrCreditMemos;

    @Column(name = "\"UZAWE\"", length = 2)
    private String paymentMethod;

    @Column(name = "\"EKORG\"", length = 4)
    private String purchaseOrganization;

    @Column(name = "\"WAERS\"", length = 5)
    private String purchaseCurrency;

    @Column(name = "\"INCO1\"", length = 3)
    private String incoterms;

    @Column(name = "\"INCO2_L\"", length = 70)
    private String incotermsLocation;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;
}

