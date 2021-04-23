package com.example.nutrinet.ui;

public class User {
    private int id;
    private String name;
    private String email;
    private String password;
    private String budget;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setBudget(String budget){ this.budget = budget; }
    public String getBudget(){return budget;}

}
