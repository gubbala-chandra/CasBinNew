package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "InternalOrder")
@Table(name = "\"InternalOrder\"", schema = "s4Data")
@IdClass(InternalOrderPK.class)
public class InternalOrder {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Id
    @Column(name = "\"ORDERID\"")
    private String orderNumber;

    @Column(name = "\"ORDER_TYPE\"")
    private String orderType;

    @Column(name = "\"ORDER_CATG\"")
    private String orderCategory;

    @Column(name = "\"REFERENCE_ORDER\"")
    private String referenceOrderNumber;

    @Column(name = "\"CO_AREA\"")
    private String controllingArea;

    @Column(name = "\"RESPCCTR\"")
    private String responsibleCostCenter;

    @Column(name = "\"LOCATION\"")
    private String location;

    @Column(name = "\"LOCATION_PLANT\"")
    private String locationPlant;

    @Column(name = "\"WBS_ELEMENT\"")
    private String wbsElement;

    @Column(name = "\"REQUEST_CCTR\"")
    private String requestingCostCenter;

    @Column(name = "\"STATISTICAL\"")
    private String statisticalOrder;

    @Column(name = "\"CURRENCY\"")
    private String orderCurrency;

    @Column(name = "\"OVERHEAD_KEY\"")
    private String overheadKey;

    @Column(name = "\"APPLICANT\"")
    private String capitorId;

    @Column(name = "\"APPLICANT_PHONE\"")
    private String budgetYear;

    @Column(name = "\"PERSON_RESP\"")
    private String ward;

    @Column(name = "\"PERSON_RESP_PHONE\"")
    private String personResponsiblePhone;

    @Column(name = "\"ESTIMATED_COSTS\"")
    private String estimatedTotalCost;

    @Column(name = "\"APPLICATION_DATE\"")
    private LocalDate applicationDate;

    @Column(name = "\"DEPARTMENT\"")
    private String department;

    @Column(name = "\"DATE_WORK_BEGINS\"")
    private LocalDate workStart;

    @Column(name = "\"DATE_WORK_ENDS\"")
    private LocalDate endOfWork;

    @Column(name = "\"WORK_PERMIT\"")
    private String workPermit;

    @Column(name = "\"INVEST_PROFILE\"")
    private String investmentMeasureProfile;

    @Column(name = "\"INV_REASON\"")
    private String reasonForInvestment;

    @Column(name = "\"ORDER_STATUS\"")
    private String orderStatus;

    @Column(name = "\"DISALLOWED_T_GRP\"")
    private String groupOfDisallowedTransactions;

    @Column(name = "\"DELETION_FLAG\"")
    private String deletionFlag;

    @Column(name = "\"OBJECTCLASS\"")
    private String objectClass;

    @Column(name = "\"REQUEST_ORDER\"")
    private String requestingOrder;

    @Column(name = "\"FUNC_AREA\"")
    private String functionalArea;

    @Column(name = "\"FUNC_AREA_LONG\"")
    private String functionalAreaLong;

    @Column(name = "\"IN_CHARGE_USER\"")
    private String personResponsible;
}
