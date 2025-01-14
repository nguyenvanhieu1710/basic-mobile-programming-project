package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.NewsModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class NewsEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public NewsEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<NewsModel> getNewsList() {
        ArrayList<NewsModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM News WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NewsModel newsModel = new NewsModel();
                    newsModel.NewsId = cursor.getInt(cursor.getColumnIndexOrThrow("NewsId"));
                    newsModel.NewsName = cursor.getString(cursor.getColumnIndexOrThrow("NewsName"));
                    newsModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    newsModel.NewsImage = cursor.getString(cursor.getColumnIndexOrThrow("NewsImage"));
                    newsModel.PostingDate = cursor.getString(cursor.getColumnIndexOrThrow("PostingDate"));
                    newsModel.PersonPostingId = cursor.getInt(cursor.getColumnIndexOrThrow("PersonPostingId"));
                    newsModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(newsModel);
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

    public ArrayList<NewsModel> getNewsListingHasBeenDeleted() {
        ArrayList<NewsModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM News WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NewsModel newsModel = new NewsModel();
                    newsModel.NewsId = cursor.getInt(cursor.getColumnIndexOrThrow("NewsId"));
                    newsModel.NewsName = cursor.getString(cursor.getColumnIndexOrThrow("NewsName"));
                    newsModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    newsModel.NewsImage = cursor.getString(cursor.getColumnIndexOrThrow("NewsImage"));
                    newsModel.PostingDate = cursor.getString(cursor.getColumnIndexOrThrow("PostingDate"));
                    newsModel.PersonPostingId = cursor.getInt(cursor.getColumnIndexOrThrow("PersonPostingId"));
                    newsModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(newsModel);
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

    public int getNumberOfNewsDeleted() {
        int numberOfNewsDeleted = 0;
        ArrayList<NewsModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM News WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    NewsModel newsModel = new NewsModel();
                    newsModel.NewsId = cursor.getInt(cursor.getColumnIndexOrThrow("NewsId"));
                    newsModel.NewsName = cursor.getString(cursor.getColumnIndexOrThrow("NewsName"));
                    newsModel.Content = cursor.getString(cursor.getColumnIndexOrThrow("Content"));
                    newsModel.NewsImage = cursor.getString(cursor.getColumnIndexOrThrow("NewsImage"));
                    newsModel.PostingDate = cursor.getString(cursor.getColumnIndexOrThrow("PostingDate"));
                    newsModel.PersonPostingId = cursor.getInt(cursor.getColumnIndexOrThrow("PersonPostingId"));
                    newsModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(newsModel);
                } while (cursor.moveToNext());
            }

            numberOfNewsDeleted = arrayList.size();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return numberOfNewsDeleted;
    }

    public boolean insertNews(NewsModel newsModel) {
        String sqlStatement = "INSERT INTO News (NewsName, Content, NewsImage, PostingDate, PersonPostingId, Rate, Deleted) " +
                "VALUES ('" + newsModel.NewsName + "', '" +
                newsModel.Content + "', '" +
                newsModel.NewsImage + "', '" +
                newsModel.PostingDate + "', " +
                newsModel.PersonPostingId + ", " +
                5 + ", " +
                (newsModel.Deleted ? 1 : 0) + ")";
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateNews(NewsModel newsModel) {
        String sqlStatement = "UPDATE News SET " +
                "NewsName = '" + newsModel.NewsName + "', " +
                "Content = '" + newsModel.Content + "', " +
                "NewsImage = '" + newsModel.NewsImage + "', " +
                "PostingDate = '" + newsModel.PostingDate + "', " +
                "PersonPostingId = " + newsModel.PersonPostingId + ", " +
                "Deleted = " + (newsModel.Deleted ? 1 : 0) + " " +
                "WHERE NewsId = " + newsModel.NewsId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteNews(NewsModel newsModel) {
        String sqlStatement = "DELETE FROM News WHERE NewsId = " + newsModel.NewsId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<NewsModel> getSearchedNewsList(String searchText) {
        ArrayList<NewsModel> searchedNewsList = new ArrayList<>();
        ArrayList<NewsModel> newsList = getNewsList();
        for (NewsModel news : newsList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (news.NewsName.toLowerCase().contains(searchText.toLowerCase())) {
                searchedNewsList.add(news);
            }
        }
        return searchedNewsList;
    }
}

