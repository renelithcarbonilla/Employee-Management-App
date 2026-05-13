package com.example.employee_app;

import com.google.gson.annotations.SerializedName;

public class PayslipResponse {
    //Payslip Response: Implementation for Payslip
    private String status;
    private String fullname;

    @SerializedName("net_pay")
    private double net_pay;

    @SerializedName("bonus")
    private double bonus;

    @SerializedName("deduction")
    private double deduction;

    @SerializedName("overtime")
    private double overtime;

    // Getters
    public String getStatus() { return status; }
    public String getFullname() { return fullname; }
    public double getNetPay() { return net_pay; }
    public double getBonus() { return bonus; }
    public double getDeduction() { return deduction; }
    public double getOvertime() { return overtime; }
}