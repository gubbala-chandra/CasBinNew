package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InboundActPK implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "source_system_id")
    private String sourceSystemId;

    @Column(name = "message_id")
    private String messageId;
}