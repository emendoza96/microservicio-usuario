package com.microservice.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private UserRole type;

    public Role() {}

    public Role(int id, UserRole type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRole getType() {
        return type;
    }

    public void setType(UserRole type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Role [id=" + id + ", type=" + type + "]";
    }

    public static enum UserRole {
        CUSTOMER,
        EMPLOYEE
    }

}
