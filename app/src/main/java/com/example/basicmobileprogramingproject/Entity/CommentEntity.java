package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.CommentModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class CommentEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public CommentEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<CommentModel> getCommentList() {
        ArrayList<CommentModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Comment WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CommentModel commentModel = new CommentModel();
                    commentModel.CommentId = cursor.getInt(cursor.getColumnIndexOrThrow("CommentId"));
                    commentModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    commentModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    commentModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    commentModel.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    commentModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(commentModel);
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

    public ArrayList<CommentModel> getCommentListingHasBeenDeleted() {
        ArrayList<CommentModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Comment WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CommentModel commentModel = new CommentModel();
                    commentModel.CommentId = cursor.getInt(cursor.getColumnIndexOrThrow("CommentId"));
                    commentModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    commentModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    commentModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    commentModel.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    commentModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(commentModel);
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

    public int getNumberOfCommentDeleted() {
        int numberOfCommentDeleted = 0;
        ArrayList<CommentModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Comment WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CommentModel commentModel = new CommentModel();
                    commentModel.CommentId = cursor.getInt(cursor.getColumnIndexOrThrow("CommentId"));
                    commentModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    commentModel.Time = cursor.getString(cursor.getColumnIndexOrThrow("Time"));
                    commentModel.SenderId = cursor.getInt(cursor.getColumnIndexOrThrow("SenderId"));
                    commentModel.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    commentModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(commentModel);
                } while (cursor.moveToNext());
            }

            numberOfCommentDeleted = arrayList.size();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return numberOfCommentDeleted;
    }

    public boolean insertComment(CommentModel commentModel) {
        String sqlStatement = "INSERT INTO Comment (CommentName, Content, Time, SenderId, ReceiverId, Deleted) " +
                "VALUES ('" + commentModel.Content + "', " +
                commentModel.Time + ", " +
                "'" + commentModel.SenderId + "', " +
                "'" + commentModel.ProductId + "', " +
                (commentModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateComment(CommentModel commentModel) {
        String sqlStatement = "UPDATE Comment SET " +
                "Content = " + commentModel.Content + ", " +
                "Time = " + commentModel.Time + ", " +
                "SenderId = " + commentModel.SenderId + ", " +
                "ReceiverId = " + commentModel.ProductId + ", " +
                "Deleted = " + (commentModel.Deleted ? 1 : 0) + " " +
                "WHERE CommentId = " + commentModel.CommentId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteComment(CommentModel commentModel) {
        String sqlStatement = "DELETE FROM Comment WHERE CommentId = " + commentModel.CommentId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<CommentModel> getSearchedCommentList(String searchText) {
        ArrayList<CommentModel> searchedCommentList = new ArrayList<>();
        ArrayList<CommentModel> commentList = getCommentList();
        for (CommentModel comment : commentList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (comment.Content.toLowerCase().contains(searchText.toLowerCase())) {
                searchedCommentList.add(comment);
            }
        }
        return searchedCommentList;
    }
}

