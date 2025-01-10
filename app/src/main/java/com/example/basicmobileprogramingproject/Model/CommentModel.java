package com.example.basicmobileprogramingproject.Model;

import com.example.basicmobileprogramingproject.Utils.DateUtils;

public class CommentModel {
    public int CommentId;
    public String Content;
    public String Time;
    public int SenderId;
    public int ProductId;
    public Boolean Deleted;

    public CommentModel(){
        this.CommentId = 0;
        this.Content = "";
        this.Time = DateUtils.getToday();
        this.SenderId = 0;
        this.ProductId = 0;
        this.Deleted = false;
    }
}
