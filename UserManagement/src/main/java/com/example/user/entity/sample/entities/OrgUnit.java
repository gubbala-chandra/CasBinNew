package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "OrgUnit")
@Table(name = "\"OrgUnit\"", schema = "s4Data")
public class OrgUnit implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "\"OrgNumber\"")
    private String orgNumber;

    @Column(name = "\"OrgCode\"")
    private String orgCode;

    @Column(name = "\"OrgName\"")
    private String orgName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "\"OrgNumber\"")
    private Collection<CodingBlockEntry> codingBlockEntries = new ArrayList<>();
}