package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "PurchaseContract")
@Table(name = "\"PurchaseContract\"", schema = "s4Data")
public class PurchaseContract {
    @Id
    @Column(name = "\"BELNR\"", length = 35)
    private String contractNumber;

    @Column(name = "\"CURCY\"", length = 3)
    private String currency;

    @Column(name = "\"BUKRS\"", length = 4)
    private String companyCode;

    @Column(name = "\"BEDAT\"")
    private LocalDate documentDate;

    @Column(name = "\"KDATB\"")
    private LocalDate validFromDate;

    @Column(name = "\"KDATE\"")
    private LocalDate validToDate;

    @Column(name = "\"EKGRP\"", length = 3)
    private String purchasingGroup;

    @Column(name = "\"EKORG\"", length = 4)
    private String purchasingOrganization;

    @Column(name = "\"LIFNR\"", length = 17)
    private String supplierNumber;

    // @Column(name = "\"LIFNAME\"", length = 35)
    // private String supplierName;

    @Column(name = "\"KTWRT\"", precision = 15, scale = 2)
    private BigDecimal targetValue;

    @Column(name = "\"NETBL\"", precision = 15, scale = 2)
    private BigDecimal netBalance;

    @Column(name = "\"IHREZ\"", length = 12)
    private String yourReference;

    @Column(name = "\"UNSEZ\"", length = 12)
    private String ourReference;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;

    @Column(name = "\"LOEKZ\"")
    private Boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "\"BELNR\"", referencedColumnName = "\"BELNR\"", insertable = false, updatable = false)
    private Collection<PurchaseContractItem> items = new ArrayList<>();
}
