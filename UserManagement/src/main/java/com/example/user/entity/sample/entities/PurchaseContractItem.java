package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PurchaseContractItem")
@Table(name = "\"PurchaseContractItem\"", schema = "s4Data")
@IdClass(PurchaseContractItemPK.class)
public class PurchaseContractItem {
    @Id
    @Column(name = "\"BELNR\"", length = 35)
    private String contractNumber;

    @Id
    @Column(name = "\"EBELP\"", length = 6)
    private String itemNumber;

    @Column(name = "\"BEDNR\"", length = 10)
    private String requirementTrackingNumber;

    @Column(name = "\"ANGNR\"", length = 20)
    private String quotationNumber;

    @Column(name = "\"WERKS\"", length = 4)
    private String plant;

    @Column(name = "\"PSTYP\"", length = 1)
    private String itemCategory;

    @Column(name = "\"KNTTP\"", length = 1)
    private String accountAssignmentCategory;

    @Column(name = "\"EMATN\"", length = 35)
    private String materialNumber;

    @Column(name = "\"EMATNKTEXT\"", length = 70)
    private String materialName;

    @Column(name = "\"KTMNG\"", precision = 13, scale = 3)
    private BigDecimal targetQuantity;

    @Column(name = "\"MEINS3\"", length = 3)
    private String quantityUnit;

    @Column(name = "\"LGORT\"", length = 4)
    private String storageLocation;

    @Column(name = "\"MATKL\"", length = 9)
    private String materialGroup;

    @Column(name = "\"LOEKZ\"")
    private Boolean deleted;

    @Column(name = "\"IDNLF\"", length = 35)
    private String vendorMaterialNumber;

    @Column(name = "\"NETPR\"", precision = 13, scale = 2)
    private BigDecimal netPrice;

    @Column(name = "\"BPRME3\"", length = 3)
    private String priceUnit;
}
