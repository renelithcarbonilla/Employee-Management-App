package com.example.employee_app;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    private String status;
    private String message;
    //LoginResponse: Implementation for Login

    // Kinahanglan motugma kini sa JSON keys gikan sa imong PHP
    @SerializedName("user_id")
    private int user_id;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("emp_type") // Kini ang mag-link sa "emp_type" sa PHP
    private String emp_type;

    @SerializedName("division") // Kini ang mag-link sa "division" sa PHP
    private String division;


    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public int getUserId() { return user_id; }
    public String getFullname() { return fullname; }
    public String getEmpType() { return emp_type; } // Mao ni ang 'getEmpType'
    public String getDivision() { return division; }
}