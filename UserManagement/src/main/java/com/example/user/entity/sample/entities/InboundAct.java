package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "InboundAct")
@Table(name = "mid_inbound_act", schema = "s4Data")
@IdClass(InboundActPK.class)
public class InboundAct {

    @Id
    @Column(name = "source_system_id", length = 32)
    private String sourceSystemId;

    @Id
    @Column(name = "message_id", length = 128)
    private String messageId;

    @Column(name = "interface_id", length = 32)
    private String interfaceId;

    @Column(name = "division_id", length = 32)
    private String divisionId;

    @Column(name = "source_request_body", columnDefinition = "jsonb")
    private String requestBody;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "message_date")
    private LocalDateTime messageDate;
}
