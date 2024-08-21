package com.example.basicmobileprogramingproject.Model;

import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.Date;

public class ImportBillModel {
    public int ImportBillId;
    public int SupplierId;
    public int StaffId;
    public double ToTalAmount;
    public String InputDay;

    public ImportBillModel() {
        this.ImportBillId = 0;
        this.SupplierId = 0;
        this.StaffId = 0;
        this.ToTalAmount = 0;
        this.InputDay = DateUtils.getToday();
    }
}
