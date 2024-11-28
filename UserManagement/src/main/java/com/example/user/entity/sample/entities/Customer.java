package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Customer")
@Table(name = "\"Customer\"", schema = "s4Data")
@IdClass(CustomerPK.class)
public class Customer {
    @Id
    @Column(name = "\"AuxSysId\"", length = 6)
    private String auxSysId;
    @Id
    @Column(name = "\"AuxSysCustomerNumber\"", length = 60)
    private String auxSysCustomerNumber;
    @Id
    @Column(name = "\"DivisionId\"", length = 5)
    private String divisionId;

    @Column(name = "\"SalesOrgCode\"", length = 4)
    private String salesOrgCode;

    @Column(name = "\"CustomerName\"", length = 40)
    private String customerName;

    @Column(name = "\"SapCustomerNumber\"", length = 10)
    private String sapCustomerNumber;

    @Column(name = "\"ModifiedOn\"")
    private LocalDate modifiedOn;
}
