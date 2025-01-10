package com.example.basicmobileprogramingproject.Model;

public class MessageModel {
    public int MessageId;
    public String Content;
    public String Time;
    public int SenderId;
    public int ReceiverId;
    public Boolean Deleted;

    public MessageModel(){
        this.MessageId = 0;
        this.Content = "";
        this.Time = "";
        this.SenderId = 0;
        this.ReceiverId = 0;
        this.Deleted = false;
    }
    public MessageModel(int messageId, String content, String time, int senderId, int receiverId, Boolean deleted){
        this.MessageId = messageId;
        this.Content = content;
        this.Time = time;
        this.SenderId = senderId;
        this.ReceiverId = receiverId;
    }
}
