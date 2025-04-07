package com.messaging.bank.entities;

import com.messaging.bank.entities.enums.Direction;
import com.messaging.bank.entities.enums.ProcessedFlowType;
import jakarta.persistence.*;

@Entity
@Table(name = "partners")
public class PartnerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "alias", nullable = false)
    private String alias;

    @Column(name = "type", nullable = false)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private Direction direction;

    @Column(name = "application")
    private String application;

    @Enumerated(EnumType.STRING)
    @Column(name = "processed_flow_type", nullable = false)
    private ProcessedFlowType processedFlowType;

    @Column(name = "description", nullable = false)
    private String description;

    public PartnerEntity(String alias, String type, Direction direction, String application, ProcessedFlowType processedFlowType, String description) {
        this.alias = alias;
        this.type = type;
        this.direction = direction;
        this.application = application;
        this.processedFlowType = processedFlowType;
        this.description = description;
    }

    public PartnerEntity() {
    }

    public Long getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public String getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getApplication() {
        return application;
    }

    public ProcessedFlowType getProcessedFlowType() {
        return processedFlowType;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setProcessedFlowType(ProcessedFlowType processedFlowType) {
        this.processedFlowType = processedFlowType;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
