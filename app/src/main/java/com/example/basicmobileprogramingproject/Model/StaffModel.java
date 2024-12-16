package com.example.basicmobileprogramingproject.Model;

import com.example.basicmobileprogramingproject.Utils.DateUtils;

public class StaffModel {
    public int StaffId;
    public String Name;
    public String Birthday;
    public String PhoneNumber;
    public String Image;
    public String Gender;
    public String Address;
    public String Position;
    public Boolean Deleted;

    public StaffModel() {
        this.StaffId = 0;
        this.Name = "";
        this.Birthday = DateUtils.getToday();
        this.PhoneNumber = "";
        this.Image = "";
        this.Gender = "Male";
        this.Address = "";
        this.Position = "";
        this.Deleted = false;
    }
}
