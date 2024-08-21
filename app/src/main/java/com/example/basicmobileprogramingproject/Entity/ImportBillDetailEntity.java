package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.ImportBillDetailModel;

import java.util.ArrayList;

public class ImportBillDetailEntity {
    private DatabaseHandler databaseHandler;

    public ImportBillDetailEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
    }

    public ArrayList<ImportBillDetailModel> getImportBillDetailList() {
        ArrayList<ImportBillDetailModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM ImportBillDetail";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ImportBillDetailModel importBillDetailModel = new ImportBillDetailModel();
                    importBillDetailModel.ImportBillDetailId = cursor.getInt(cursor.getColumnIndexOrThrow("ImportBillDetailId"));
                    importBillDetailModel.ImportBillId = cursor.getInt(cursor.getColumnIndexOrThrow("ImportBillId"));
                    importBillDetailModel.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    importBillDetailModel.ImportPrice = cursor.getDouble(cursor.getColumnIndexOrThrow("ImportPrice"));
                    importBillDetailModel.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));

                    arrayList.add(importBillDetailModel);
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

    public boolean insertImportBillDetail(ImportBillDetailModel importBillDetailModel) {
        String sqlStatement = "INSERT INTO ImportBillDetail (ImportBillId, ProductId, ImportPrice, Quantity) " +
                "VALUES (" + importBillDetailModel.ImportBillId + ", " +
                importBillDetailModel.ProductId + ", " +
                importBillDetailModel.ImportPrice + ", " +
                importBillDetailModel.Quantity + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateImportBillDetail(ImportBillDetailModel importBillDetailModel) {
        String sqlStatement = "UPDATE ImportBillDetail SET " +
                "ImportBillId = " + importBillDetailModel.ImportBillId + ", " +
                "ProductId = " + importBillDetailModel.ProductId + ", " +
                "ImportPrice = " + importBillDetailModel.ImportPrice + ", " +
                "Quantity = " + importBillDetailModel.Quantity + " " +
                "WHERE ImportBillDetailId = " + importBillDetailModel.ImportBillDetailId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteImportBillDetail(ImportBillDetailModel importBillDetailModel) {
        String sqlStatement = "DELETE FROM ImportBillDetail WHERE ImportBillDetailId = " + importBillDetailModel.ImportBillDetailId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
