package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorInvoicePaymentPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Column(name = "\"AuxSysId\"")
    private String auxSysId;

    @Column(name = "\"AuxInvoiceNumber\"")
    private String auxInvoiceNumber;

    @Column(name = "\"ClearingDocumentNumber\"")
    private String clearingDocumentNumber;

    @Column(name = "\"ClearingDocumentFiscalYear\"")
    private String clearingDocumentFiscalYear;
}
