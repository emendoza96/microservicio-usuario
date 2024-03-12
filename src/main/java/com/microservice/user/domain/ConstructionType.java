package com.microservice.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "construction_type")
public class ConstructionType {

    @Id
    @NotNull(message = "Id is mandatory")
    private Integer id;
    @NotNull(message = "Type is mandatory")
    private String type;

    public ConstructionType() {}

    public ConstructionType(Integer id, String type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ConstructionType [id=" + id + ", type=" + type + "]";
    }

}
