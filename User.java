package com.anish.collegeapp;

public class User {
    private String name;
    private String email;
    private final String password;
    private String department;
    private String studentId;
    private String phone;

    // Full constructor with all fields
    public User(String name, String email, String password, String department,
                String studentId, String phone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.department = department;
        this.studentId = studentId;
        this.phone = phone;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
