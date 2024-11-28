package com.example.user.entity.sample.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name = "CodingBlock")
@Table(name = "\"CodingBlock\"", schema = "s4Data")
public class CodingBlock {
    @Id
    @Column(name = "\"Type\"")
    private String type;

    @Column(name = "\"Description\"")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "\"Type\"")
    private Collection<CodingBlockEntry> codingBlockEntries = new ArrayList<>();
}