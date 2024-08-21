package com.example.basicmobileprogramingproject.Model;

import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.Date;

public class VoucherModel {
        public int VoucherId;
        public String VoucherName;
        public double Price;
        public double MinimumPrice;
        public int Quantity;
        public String StartDay;
        public String EndDate;
        public Boolean Deleted;

    public VoucherModel() {
        this.VoucherId = 0;
        this.VoucherName = "";
        this.Price = 0;
        this.MinimumPrice = 0;
        this.Quantity = 0;
        this.StartDay = DateUtils.getToday();
        this.EndDate = DateUtils.getToday();
        this.Deleted = false;
    }
}
