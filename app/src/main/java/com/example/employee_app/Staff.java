package com.example.employee_app;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// Mao kini ang Model Class nga mo-match sa imong JSON gikan sa PHP
@Entity(tableName = "staff_table")
public class Staff {

    // Kinahanglan 'emp_id' ang ngalan para mo-match sa JSON key nimo
    @PrimaryKey(autoGenerate = true)
    public int emp_id;

    @ColumnInfo(name = "fname")
    public String fname;

    @ColumnInfo(name = "lname")
    public String lname;

    // Ang 'division' mao ang imong 'position' sa karaan nga code
    @ColumnInfo(name = "division")
    public String division;

    // Constructor para sa pag-insert (wala pay ID)
    public Staff(String fname, String lname, String division) {
        this.fname = fname;
        this.lname = lname;
        this.division = division;
    }

    // Constructor para sa pag-fetch/view (apil ang ID)
    @Ignore
    public Staff(int emp_id, String fname, String lname, String division) {
        this.emp_id = emp_id;
        this.fname = fname;
        this.lname = lname;
        this.division = division;
    }

    // Getters and Setters
    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}