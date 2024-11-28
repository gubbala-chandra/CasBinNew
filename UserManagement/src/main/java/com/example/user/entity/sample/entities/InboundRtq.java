package com.example.user.entity.sample.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "InboundRtq")
@Table(name = "mid_inbound_rtq", schema = "s4Data")
@IdClass(InboundActPK.class)
public class InboundRtq {

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

    @Column(name = "transformed_request_body", columnDefinition = "jsonb")
    private String transformedRequestBody;

    @Column(name = "error_status")
    private int errorStatus;

    @Column(name = "error_message", columnDefinition = "jsonb")
    private String errorMessage;

    @Column(name = "status", length = 32)
    private String status;

    @Column(name = "retry_no")
    private int retryNo;
}