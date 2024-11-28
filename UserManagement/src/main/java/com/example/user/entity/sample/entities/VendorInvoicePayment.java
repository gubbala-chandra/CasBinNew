package com.example.user.entity.sample.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "VendorInvoicePayment")
@Table(name = "\"VendorInvoicePayment\"", schema = "s4Data")
@IdClass(VendorInvoicePaymentPK.class)
public class VendorInvoicePayment {
    @Id
    @Column(name = "\"AuxSysId\"")
    private String auxSysId;
    @Id
    @Column(name = "\"AuxInvoiceNumber\"")
    private String auxInvoiceNumber;
    @Id
    @Column(name = "\"ClearingDocumentNumber\"")
    private String clearingDocumentNumber;
    @Id
    @Column(name = "\"ClearingDocumentFiscalYear\"")
    private String clearingDocumentFiscalYear;

    @Column(name = "\"CompanyCode\"", length = 4)
    private String companyCode;

    @Column(name = "\"VendorNumber\"", length = 10)
    private String vendorNumber;

    @Column(name = "\"ClearingDate\"")
    private LocalDate clearingDate;

    @Column(name = "\"ClearingDocumentFiscalPeriod\"")
    private String clearingDocumentFiscalPeriod;

    @Column(name = "\"PaymentAmount\"", precision = 23, scale = 2)
    private BigDecimal paymentAmount;

    @Column(name = "\"DiscountAmount\"", precision = 23, scale = 2)
    private BigDecimal discountAmount;

    @Column(name = "\"PaymentCurrency\"")
    private String paymentCurrency;

    @Column(name = "\"PaymentMethod\"")
    private String paymentMethod;

    @Column(name = "\"SapInvoiceNumber\"")
    private String sapInvoiceNumber;

    @Column(name = "\"SapInvoiceFiscalYear\"")
    private String sapInvoiceFiscalYear;

    @Column(name = "\"CheckNumber\"")
    private String checkNumber;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;
}