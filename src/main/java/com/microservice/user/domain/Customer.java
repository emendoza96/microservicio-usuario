package com.microservice.user.domain;

import java.util.List;

public class Customer {

    private int id;
    private String businessName;
    private String cuit;
    private String email;
    private Boolean onlineEnabled;
    private Double maxPay;
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Customer [id=" + id + ", businessName=" + businessName + ", cuit=" + cuit + ", email=" + email
                + ", onlineEnabled=" + onlineEnabled + ", maxPay=" + maxPay + "]";
    }

}
