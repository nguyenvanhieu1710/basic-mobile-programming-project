package com.example.basicmobileprogramingproject.Model;

import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.Date;

public class UserModel {
    public int UserId;
    public String Name;
    public String Birthday;
    public String PhoneNumber;
    public String Image;
    public String Gender;
    public String Address;
    public Boolean Deleted;

    public UserModel() {
        this.UserId = 0;
        this.Name = "";
        this.Birthday = DateUtils.getToday();
        this.PhoneNumber = "";
        this.Image = "";
        this.Gender = "Male";
        this.Address = "";
        this.Deleted = false;
    }

}
