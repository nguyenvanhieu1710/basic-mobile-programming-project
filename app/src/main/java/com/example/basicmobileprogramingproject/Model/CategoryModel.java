package com.example.basicmobileprogramingproject.Model;

public class CategoryModel {
    public int CategoryId;
    public String CategoryName;
    public String CategoryImage;
    public int DadCategoryId;
    public Boolean Deleted;

    public CategoryModel() {
        this.CategoryId = 0;
        this.CategoryName = "";
        this.CategoryImage = "";
        this.DadCategoryId = 0;
        this.Deleted = false;
    }
}
