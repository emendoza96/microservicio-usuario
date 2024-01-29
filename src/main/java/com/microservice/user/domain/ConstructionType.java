package com.microservice.user.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "construction_type")
public class ConstructionType {

    @Id
    private int id;
    private String type;

    public ConstructionType() {}

    public ConstructionType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
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
