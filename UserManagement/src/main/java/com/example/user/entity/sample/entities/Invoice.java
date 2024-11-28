package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Invoice")
@Table(name = "\"Invoice\"", schema = "s4Data")
@IdClass(InvoicePK.class)
public class Invoice {
    // @Id
    // @Column(name = "\"QDate\"")
    // private LocalDate qDate;
    @Id
    @Column(name = "\"AuxSysId\"")
    private String auxSysId;
    @Id
    @Column(name = "\"DivisionId\"")
    private String divisionId;
    @Id
    @Column(name = "\"SalesOrgCode\"")
    private String salesOrgCode;
    @Id
    @Column(name = "\"SalesOrderNumber\"")
    private String salesOrderNumber;
    @Id
    @Column(name = "\"BillingDocumentNumber\"")
    private String billingDocumentNumber;

    @Column(name = "\"AuxSysCustomerNumber\"")
    private String auxSysCustomerNumber;

    @Column(name = "\"SoldToPartyNumber\"")
    private String soldToPartyNumber;

    @Column(name = "\"CustomerReference\"")
    private String customerReference;

    @Column(name = "\"ClearingStatus\"")
    private String clearingStatus;

    @Column(name = "\"StatusField\"")
    private String statusField;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;
}