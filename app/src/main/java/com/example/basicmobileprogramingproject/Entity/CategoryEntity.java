package com.example.basicmobileprogramingproject.Entity;

import android.content.Context;
import android.database.Cursor;

import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class CategoryEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public CategoryEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<CategoryModel> getCategoryList() {
        ArrayList<CategoryModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Category WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CategoryModel categoryModel = new CategoryModel();
                    categoryModel.CategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("CategoryId"));
                    categoryModel.CategoryName = cursor.getString(cursor.getColumnIndexOrThrow("CategoryName"));
                    categoryModel.CategoryImage = cursor.getString(cursor.getColumnIndexOrThrow("CategoryImage"));
                    categoryModel.DadCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("DadCategoryId"));
                    categoryModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(categoryModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public ArrayList<CategoryModel> getCategoryListingHasBeenDeleted() {
        ArrayList<CategoryModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Category WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CategoryModel categoryModel = new CategoryModel();
                    categoryModel.CategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("CategoryId"));
                    categoryModel.CategoryName = cursor.getString(cursor.getColumnIndexOrThrow("CategoryName"));
                    categoryModel.CategoryImage = cursor.getString(cursor.getColumnIndexOrThrow("CategoryImage"));
                    categoryModel.DadCategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("DadCategoryId"));
                    categoryModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(categoryModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public boolean insertCategory(CategoryModel categoryModel) {
        String sqlStatement = "INSERT INTO Category (CategoryName, CategoryImage, DadCategoryId, Deleted) " +
                "VALUES ('" + categoryModel.CategoryName + "', " +
                "'" + categoryModel.CategoryImage + "', " +
                categoryModel.DadCategoryId + ", " +
                (categoryModel.Deleted ? 1 : 0) + ")";
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCategory(CategoryModel categoryModel) {
        String sqlStatement = "UPDATE Category SET " +
                "CategoryName = '" + categoryModel.CategoryName + "', " +
                "CategoryImage = '" + categoryModel.CategoryImage + "', " +
                "DadCategoryId = " + categoryModel.DadCategoryId + ", " +
                "Deleted = " + (categoryModel.Deleted ? 1 : 0) + " " +
                "WHERE CategoryId = " + categoryModel.CategoryId;
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategory(CategoryModel categoryModel) {
        String sqlStatement = "DELETE FROM Category WHERE CategoryId = " + categoryModel.CategoryId;
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public Integer getNumberOfCategoriesDeleted() {
        Integer numberOfCategoriesDeleted = 0;
        ArrayList<CategoryModel> categoryList = getCategoryListingHasBeenDeleted();
        for (CategoryModel category : categoryList) {
            if (category.Deleted) {
                numberOfCategoriesDeleted++;
            }
        }
        return numberOfCategoriesDeleted;
    }

    public ArrayList<CategoryModel> getSearchedCategoriesList(String searchText) {
        ArrayList<CategoryModel> searchedCategoriesList = new ArrayList<>();
        ArrayList<CategoryModel> categoryList = getCategoryList();
        for (CategoryModel category : categoryList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (category.CategoryName.toLowerCase().contains(searchText.toLowerCase())) {
                searchedCategoriesList.add(category);
            }
        }
        return searchedCategoriesList;
    }
}
