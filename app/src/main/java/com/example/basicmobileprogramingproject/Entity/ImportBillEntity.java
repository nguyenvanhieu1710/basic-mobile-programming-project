package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import com.example.basicmobileprogramingproject.Model.ImportBillModel;

import java.util.ArrayList;

public class ImportBillEntity {
    private DatabaseHandler databaseHandler;

    public ImportBillEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
    }

    public ArrayList<ImportBillModel> getImportBillList() {
        ArrayList<ImportBillModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM ImportBill";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ImportBillModel importBillModel = new ImportBillModel();
                    importBillModel.ImportBillId = cursor.getInt(cursor.getColumnIndexOrThrow("ImportBillId"));
                    importBillModel.SupplierId = cursor.getInt(cursor.getColumnIndexOrThrow("SupplierId"));
                    importBillModel.StaffId = cursor.getInt(cursor.getColumnIndexOrThrow("StaffId"));
                    importBillModel.ToTalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("ToTalAmount"));
                    importBillModel.InputDay = cursor.getString(cursor.getColumnIndexOrThrow("InputDay"));

                    arrayList.add(importBillModel);
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

    public boolean insertImportBill(ImportBillModel importBillModel) {
        String sqlStatement = "INSERT INTO ImportBill (SupplierId, StaffId, ToTalAmount, InputDay) " +
                "VALUES (" + importBillModel.SupplierId + ", " +
                importBillModel.StaffId + ", " +
                importBillModel.ToTalAmount + ", " +
                importBillModel.InputDay + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateImportBill(ImportBillModel importBillModel) {
        String sqlStatement = "UPDATE ImportBill SET " +
                "SupplierId = " + importBillModel.SupplierId + ", " +
                "StaffId = " + importBillModel.StaffId + ", " +
                "ToTalAmount = " + importBillModel.ToTalAmount + ", " +
                "InputDay = " + importBillModel.InputDay + " " +
                "WHERE ImportBillId = " + importBillModel.ImportBillId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteImportBill(ImportBillModel importBillModel) {
        String sqlStatement = "DELETE FROM ImportBill WHERE ImportBillId = " + importBillModel.ImportBillId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
