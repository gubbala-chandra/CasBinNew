package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetDepreciationAreaPK implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "\"Type\"")
    private String type;

    @Column(name = "\"ASSET-SUBNUMBER\"")
    private String mainAndSubNumber;

    @Column(name = "\"AREA\"")
    private String realDepreciationArea;

}
