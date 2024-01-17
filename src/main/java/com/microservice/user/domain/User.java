package com.microservice.user.domain;

public class User {
    
    private int id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String lastname;
    
    public User(String username, String password, String email, String name, String lastname) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLastname() {
        return lastname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Override
    public String toString() {
        return "user [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", name="
                + name + ", lastname=" + lastname + "]";
    }

}
