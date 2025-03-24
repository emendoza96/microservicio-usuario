package com.microservice.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "construction_type")
public class ConstructionType {

    @Id
    private Integer id;
    @Enumerated(EnumType.STRING)
    private ConstructionTypeEnum type;

    public ConstructionType() {}

    public ConstructionType(Integer id, ConstructionTypeEnum type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public ConstructionTypeEnum getType() {
        return type;
    }
    public void setType(ConstructionTypeEnum type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ConstructionType [id=" + id + ", type=" + type + "]";
    }

    public static enum ConstructionTypeEnum {
        REPAIR,
        HOUSE,
        BUILDING,
        ROAD
    }

}
