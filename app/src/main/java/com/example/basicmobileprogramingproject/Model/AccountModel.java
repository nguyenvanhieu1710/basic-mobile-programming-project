package com.example.basicmobileprogramingproject.Model;


import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.Date;

public class AccountModel {

    public int AccountId;
    public String AccountName;
    public String Password;
    public String Role;
    public String DayCreated;
    public Boolean RememberPassword;
    public String Email;
    public String Status;
    public Boolean Deleted;

    public AccountModel() {
        this.AccountId = 0;
        this.AccountName = "";
        this.Password = "";
        this.Role = "User";
        this.DayCreated = DateUtils.getToday();
        this.RememberPassword = false;
        this.Email = "default@gmail.com";
        this.Status = "Offline";
        this.Deleted = false;
    }
//    private int accountId ;
//    private String accountName ;
//    private String password ;
//    private String role ;
//    private Date dayCreated ;
//    private String rememberPassword ;
//    private String email ;
//    private String status;
//    public int getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(int accountId) {
//        this.accountId = accountId;
//    }
//
//    public String getAccountName() {
//        return accountName;
//    }
//
//    public void setAccountName(String accountName) {
//        this.accountName = accountName;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getRole() {
//        return role;
//    }
//
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    public LocalDate getDayCreated() {
//        return dayCreated;
//    }
//
//    public void setDayCreated(LocalDate dayCreated) {
//        this.dayCreated = dayCreated;
//    }
//
//    public String getRememberPassword() {
//        return rememberPassword;
//    }
//
//    public void setRememberPassword(String rememberPassword) {
//        this.rememberPassword = rememberPassword;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
}
