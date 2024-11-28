package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "GLAccount")
@Table(name = "\"GLAccount\"", schema = "s4Data")
@IdClass(GLAccountPK.class)
public class GLAccount {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"SAKNR\"")
    private String accountNumber;

    @Column(name = "\"KTOPL\"")
    private String chartOfAccounts;

    @Column(name = "\"GVTYP\"")
    private String pLAccount;

    @Column(name = "\"KTOKS\"")
    private String accountGroup;

    @Column(name = "\"XBILK\"")
    private String balanceSheetAccount;

    @Column(name = "\"XLOEV\"")
    private String markedForDeletion;

    @Column(name = "\"XSPEA\"")
    private String blockedForCreation;

    @Column(name = "\"XSPEB\"")
    private String blockedForPosting;

    @Column(name = "\"XSPEP\"")
    private String blockedForPlanning;

    @Column(name = "\"GLACCOUNT_TYPE\"")
    private String accountType;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumns({ @JoinColumn(name = "\"SAKNR\"", referencedColumnName = "\"SAKNR\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"Type\"", referencedColumnName = "\"Type\"", insertable = false, updatable = false) })
    private Collection<GLAccountCompanyCode> companyCode = new ArrayList<>();
}