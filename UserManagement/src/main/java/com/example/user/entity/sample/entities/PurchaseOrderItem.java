package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PurchaseOrderItem")
@Table(name = "\"PurchaseOrderItem\"", schema = "s4Data")
@IdClass(PurchaseOrderItemPK.class)
public class PurchaseOrderItem {
    @Id
    @Column(name = "\"BELNR\"", length = 35)
    private String orderNumber;

    @Id
    @Column(name = "\"POSEX\"", length = 6)
    private String itemNumber;

    @Column(name = "\"MENGE\"", precision = 13, scale = 3)
    private BigDecimal quantity;

    @Column(name = "\"MENEE\"", length = 3)
    private String unitOfMeasure;

    @Column(name = "\"PEINH\"", precision = 5, scale = 0)
    private BigDecimal priceUnit;

    @Column(name = "\"PMENE\"", length = 3)
    private String priceUnitOfMeasure;

    @Column(name = "\"VPREI\"", precision = 11, scale = 2)
    private BigDecimal price;

    @Column(name = "\"EMATN\"", length = 35)
    private String materialNumber;

    @Column(name = "\"EMATNKTEXT\"", length = 70)
    private String materialName;

    @Column(name = "\"IDNLF\"", length = 35)
    private String vendorMaterialNumber;

    @Column(name = "\"IDNLFKTEXT\"", length = 70)
    private String vendorMaterialName;

    @Column(name = "\"NETWR\"", precision = 13, scale = 2)
    private BigDecimal netValue;

    @Column(name = "\"SAKNR\"", length = 10)
    private String gLAccount;

    @Column(name = "\"KOSTL\"", length = 10)
    private String cCWBSIO;

    @Column(name = "\"LOEKZ\"")
    private Boolean deleted;
}

