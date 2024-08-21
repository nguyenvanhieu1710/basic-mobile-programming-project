package com.example.basicmobileprogramingproject.Model;

import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.Date;

public class OrderModel {
    public int OrderId;
    public int UserId;
    public int StaffId;
    public String OrderStatus;
    public double TotalAmount;
    public String DayBuy;
    public String DeliveryAddress;
    public Boolean Deleted;

    public OrderModel() {
        this.OrderId = 0;
        this.UserId = 0;
        this.StaffId = 0;
        this.OrderStatus = "";
        this.TotalAmount = 0;
        this.DayBuy = DateUtils.getToday();
        this.DeliveryAddress = "";
        this.Deleted = false;
    }
}
