package com.microservice.user.domain;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private UserEntity user;
    private LocalDate dischargeDate;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Construction> constructionList;

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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
