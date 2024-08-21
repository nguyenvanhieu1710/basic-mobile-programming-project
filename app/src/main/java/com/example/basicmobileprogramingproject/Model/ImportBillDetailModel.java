package com.example.basicmobileprogramingproject.Model;

public class ImportBillDetailModel {
    public int ImportBillDetailId;
    public int ImportBillId;
    public int ProductId;
    public double ImportPrice;
    public int Quantity;

    public ImportBillDetailModel() {
        this.ImportBillDetailId = 0;
        this.ImportBillId = 0;
        this.ProductId = 0;
        this.ImportPrice = 0;
        this.Quantity = 0;
    }
}
