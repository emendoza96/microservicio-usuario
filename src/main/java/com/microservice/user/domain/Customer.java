package com.microservice.user.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String businessName;
    private String cuit;
    private String email;
    private Boolean onlineEnabled;
    private Double maxPay;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDate dischargeDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Construction> constructionList = new ArrayList<>();

    public Customer() {}

    public Customer(String businessName, String cuit, String email, Boolean onlineEnabled, Double maxPay) {
        this.businessName = businessName;
        this.cuit = cuit;
        this.email = email;
        this.onlineEnabled = onlineEnabled;
        this.maxPay = maxPay;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getBusinessName() {
        return businessName;
    }
    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }
    public String getCuit() {
        return cuit;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getOnlineEnabled() {
        return onlineEnabled;
    }
    public void setOnlineEnabled(Boolean onlineEnabled) {
        this.onlineEnabled = onlineEnabled;
    }
    public Double getMaxPay() {
        return maxPay;
    }
    public void setMaxPay(Double maxPay) {
        this.maxPay = maxPay;
    }

    public List<Construction> getConstructionList() {
        return constructionList;
    }

    public void setConstructionList(List<Construction> constructionList) {
        this.constructionList = constructionList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(LocalDate dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", businessName=" + businessName + ", cuit=" + cuit + ", email=" + email
                + ", onlineEnabled=" + onlineEnabled + ", maxPay=" + maxPay + ", user=" + user + ", dischargeDate="
                + dischargeDate + "]";
    }

}
