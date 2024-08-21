package com.example.basicmobileprogramingproject.Model;

public class CartModel {
    public int ProductId;
    public int UserId;
    private boolean selected;
    private int quantity;
    private double calculatedPrice;

    public CartModel() {
        this.ProductId = 0;
        this.UserId = 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public double getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(double calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }
}
