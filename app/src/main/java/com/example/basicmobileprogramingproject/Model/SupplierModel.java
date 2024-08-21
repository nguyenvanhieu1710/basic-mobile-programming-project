package com.example.basicmobileprogramingproject.Model;

public class SupplierModel {
    public int SupplierId;
    public String SupplierName;
    public String PhoneNumber;
    public String Address;
    public Boolean Deleted;

    public SupplierModel() {
        this.SupplierId = 0;
        this.SupplierName = "";
        this.PhoneNumber = "";
        this.Address = "";
        this.Deleted = false;
    }
}
