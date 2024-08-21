package com.example.basicmobileprogramingproject.Model;

import java.util.Date;

public class ProductModel {
    public int ProductId;
    public String ProductName;
    public int Quantity;
    public double Price;
    public String Description;
    public String Brand;
    public String ProductImage;
    public String ProductDetail;
    public int CategoryId;
    public Boolean Deleted;

    public ProductModel() {
        this.ProductId = 0;
        this.ProductName = "";
        this.Quantity = 0;
        this.Price = 0;
        this.Description = "";
        this.Brand = "";
        this.ProductImage = "";
        this.ProductDetail = "";
        this.CategoryId = 0;
        this.Deleted = false;
    }
}
