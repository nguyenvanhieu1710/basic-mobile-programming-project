package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.MessageModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class MessageEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public MessageEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<MessageModel> getMessageList() {
        ArrayList<MessageModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Message WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MessageModel messageModel = new MessageModel();
                    messageModel.MessageId = cursor.getInt(cursor.getColumnIndexOrThrow("MessageId"));
                    messageModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    messageModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    messageModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    messageModel.ReceiverId = cursor.getInt(cursor.getColumnIndexOrThrow("ReceiverId"));
                    messageModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(messageModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public ArrayList<MessageModel> getMessagesForUser(int userId) {
        ArrayList<MessageModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Message WHERE Deleted = 0 and (SenderId = " + userId + " or ReceiverId = " + userId + ")";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MessageModel messageModel = new MessageModel();
                    messageModel.MessageId = cursor.getInt(cursor.getColumnIndexOrThrow("MessageId"));
                    messageModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    messageModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    messageModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    messageModel.ReceiverId = cursor.getInt(cursor.getColumnIndexOrThrow("ReceiverId"));
                    messageModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(messageModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public ArrayList<MessageModel> getMessageListingHasBeenDeleted() {
        ArrayList<MessageModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Message WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MessageModel messageModel = new MessageModel();
                    messageModel.MessageId = cursor.getInt(cursor.getColumnIndexOrThrow("MessageId"));
                    messageModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    messageModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    messageModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    messageModel.ReceiverId = cursor.getInt(cursor.getColumnIndexOrThrow("ReceiverId"));
                    messageModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(messageModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public int getNumberOfMessageDeleted() {
        int numberOfMessageDeleted = 0;
        ArrayList<MessageModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Message WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    MessageModel messageModel = new MessageModel();
                    messageModel.MessageId = cursor.getInt(cursor.getColumnIndexOrThrow("MessageId"));
                    messageModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    messageModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    messageModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    messageModel.ReceiverId = cursor.getInt(cursor.getColumnIndexOrThrow("ReceiverId"));
                    messageModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(messageModel);
                } while (cursor.moveToNext());
            }

            numberOfMessageDeleted = arrayList.size();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return numberOfMessageDeleted;
    }

    public boolean insertMessage(MessageModel messageModel) {
        String sqlStatement = "INSERT INTO Message (Content, Time, SenderId, ReceiverId, Deleted) " +
                "VALUES ('" + messageModel.Content + "', '" +
                messageModel.Time + "', " +
                "'" + messageModel.SenderId + "', " +
                "'" + messageModel.ReceiverId + "', " +
                (messageModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateMessage(MessageModel messageModel) {
        String sqlStatement = "UPDATE Message SET " +
                "Content = '" + messageModel.Content + "', " +
                "Time = '" + messageModel.Time + "', " +
                "SenderId = " + messageModel.SenderId + ", " +
                "ReceiverId = " + messageModel.ReceiverId + ", " +
                "Deleted = " + (messageModel.Deleted ? 1 : 0) + " " +
                "WHERE MessageId = " + messageModel.MessageId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteMessage(MessageModel messageModel) {
        String sqlStatement = "DELETE FROM Message WHERE MessageId = " + messageModel.MessageId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<MessageModel> getSearchedMessageList(String searchText) {
        ArrayList<MessageModel> searchedMessageList = new ArrayList<>();
        ArrayList<MessageModel> messageList = getMessageList();
        for (MessageModel message : messageList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (message.Content.toLowerCase().contains(searchText.toLowerCase())) {
                searchedMessageList.add(message);
            }
        }
        return searchedMessageList;
    }
}

