package com.example.user.entity.sample.entities;

import lombok.*;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "\"Type\"")
    private String type;

    @Column(name = "\"ASSET-SUBNUMBER\"")
    private String mainAndSubNumber;

}
