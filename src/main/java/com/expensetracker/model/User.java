package com.expensetracker.model;

public class User {
    private long id;
    private String name;
    private String email;
    private String password; // stored as BCrypt hash
    private Double budget; // optional, can be null

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }
}
