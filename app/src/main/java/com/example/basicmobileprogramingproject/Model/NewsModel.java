package com.example.basicmobileprogramingproject.Model;

public class NewsModel {
    public int NewsId;
    public String NewsName;
    public String Content;
    public String NewsImage;
    public String PostingDate;
    public int PersonPostingId;
    public Boolean Deleted;

    public NewsModel(){
        this.NewsId = 0;
        this.NewsName = "";
        this.Content = "";
        this.NewsImage = "";
        this.PostingDate = "";
        this.PersonPostingId = 0;
        this.Deleted = false;
    }
}
