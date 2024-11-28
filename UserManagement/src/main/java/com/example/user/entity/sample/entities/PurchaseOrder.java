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
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PurchaseOrder")
@Table(name = "\"PurchaseOrder\"", schema = "s4Data")
public class PurchaseOrder {
    @Id
    @Column(name = "\"BELNR\"", length = 35)
    private String orderNumber;

    @Column(name = "\"CURCY\"", length = 3)
    private String currency;

    @Column(name = "\"BUKRS\"", length = 4)
    private String companyCode;

    @Column(name = "\"AEDAT\"")
    private LocalDate creationDate;

    @Column(name = "\"ERNAM\"", length = 12)
    private String createdBy;

    @Column(name = "\"BEDAT\"")
    private LocalDate documentDate;

    @Column(name = "\"LIFNR\"", length = 17)
    private String supplierNumber;

    @Column(name = "\"LIFNAME\"", length = 35)
    private String supplierName;

    @Column(name = "\"RLWRT\"", precision = 13, scale = 2)
    private BigDecimal netValue;

    @Column(name = "\"KONNR\"", length = 35)
    private String purchaseAgreementNumber;

    @Column(name = "\"KTPNR\"", length = 6)
    private String purchaseAgreementItemNumber;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;

    @Column(name = "\"LOEKZ\"")
    private Boolean deleted;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "\"BELNR\"", referencedColumnName = "\"BELNR\"", insertable = false, updatable = false)
    private Collection<PurchaseOrderItem> items = new ArrayList<>();
}