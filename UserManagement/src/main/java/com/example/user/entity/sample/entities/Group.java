package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Group")
@Table(name = "\"Group\"", schema = "s4Data")
@IdClass(GroupPK.class)
public class Group implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "\"GroupSet\"")
    private String groupSet;

    @Id
    @Column(name = "\"GroupId\"")
    private String groupId;

    @Column(name = "\"Name\"")
    private String name;

//	@Column(name = "\"OrgNumber\"")
//	private String orgNumber;

    @Column(name = "\"ParentGroupId\"")
    private String parentGroupId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "\"GroupSet\"", referencedColumnName = "\"GroupSet\""), @JoinColumn(name = "\"GroupId\"", referencedColumnName = "\"GroupId\"") })
//	@ElementCollection(fetch = FetchType.LAZY)
//	@CollectionTable(name = "\"V_CodingBlockEntry\"", joinColumns = @JoinColumn(name = "\"GroupId\""))
    private Collection<CodingBlockEntry> codingBlockEntries;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "\"ParentGroupId\"", referencedColumnName = "\"GroupId\"", insertable = false, updatable = false),
            @JoinColumn(name = "\"GroupSet\"", referencedColumnName = "\"GroupSet\"", insertable = false, updatable = false) })
    private Group parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private Collection<Group> children;
}
