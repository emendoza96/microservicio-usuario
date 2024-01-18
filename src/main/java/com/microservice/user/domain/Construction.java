package com.microservice.user.domain;

public class Construction {

    private int id;
    private String description;
    private Float latitude;
    private Float longitude;
    private String direction;
    private int area;

    private Customer customer;
    private ConstructionType constructionType;

    public Construction(String description, Float latitude, Float longitude, String direction, int area, Customer customer) {
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.direction = direction;
        this.area = area;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public ConstructionType getConstructionType() {
        return constructionType;
    }

    public void setConstructionType(ConstructionType constructionType) {
        this.constructionType = constructionType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "Construction [id=" + id + ", description=" + description + ", latitude=" + latitude + ", longitude="
                + longitude + ", direction=" + direction + ", area=" + area + "]";
    }

}
