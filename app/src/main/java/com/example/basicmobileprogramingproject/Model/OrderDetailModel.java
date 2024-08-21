package com.example.basicmobileprogramingproject.Model;

public class OrderDetailModel {
    public int OrderDetailId;
    public int OrderId;
    public int ProductId;
    public int Quantity;
    public double Price;
    public double DiscountAmount;
    public int VoucherId;

    public OrderDetailModel() {
        this.OrderDetailId = 0;
        this.OrderId = 0;
        this.ProductId = 0;
        this.Quantity = 0;
        this.Price = 0;
        this.DiscountAmount = 0;
        this.VoucherId = 0;
    }
}
