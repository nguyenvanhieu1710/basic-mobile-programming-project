package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.SupplierModel;

import java.util.ArrayList;

public class SupplierEntity {
    private DatabaseHandler databaseHandler;

    public SupplierEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
    }

    public ArrayList<SupplierModel> getSupplierList() {
        ArrayList<SupplierModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Supplier WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SupplierModel supplierModel = new SupplierModel();
                    supplierModel.SupplierId = cursor.getInt(cursor.getColumnIndexOrThrow("SupplierId"));
                    supplierModel.SupplierName = cursor.getString(cursor.getColumnIndexOrThrow("SupplierName"));
                    supplierModel.PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"));
                    supplierModel.Address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                    supplierModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(supplierModel);
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

    public ArrayList<SupplierModel> getSupplierListingHasBeenDeleted() {
        ArrayList<SupplierModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Supplier WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SupplierModel supplierModel = new SupplierModel();
                    supplierModel.SupplierId = cursor.getInt(cursor.getColumnIndexOrThrow("SupplierId"));
                    supplierModel.SupplierName = cursor.getString(cursor.getColumnIndexOrThrow("SupplierName"));
                    supplierModel.PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"));
                    supplierModel.Address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                    supplierModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(supplierModel);
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

    public int getNumberOfSuppliersDeleted() {
        int numberOfSuppliersDeleted = 0;
        ArrayList<SupplierModel> supplierListingHasBeenDeleted = getSupplierListingHasBeenDeleted();
        for (SupplierModel supplier : supplierListingHasBeenDeleted) {
            if (supplier.Deleted) {
                numberOfSuppliersDeleted++;
            }
        }
        return numberOfSuppliersDeleted;
    }

    public boolean insertSupplier(SupplierModel supplierModel) {
        String sqlStatement = "INSERT INTO Supplier (SupplierName, PhoneNumber, Address, Deleted) " +
                "VALUES ('" + supplierModel.SupplierName + "', " +
                "'" + supplierModel.PhoneNumber + "', " +
                "'" + supplierModel.Address + "', " +
                (supplierModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateSupplier(SupplierModel supplierModel) {
        String sqlStatement = "UPDATE Supplier SET " +
                "SupplierName = '" + supplierModel.SupplierName + "', " +
                "PhoneNumber = '" + supplierModel.PhoneNumber + "', " +
                "Address = '" + supplierModel.Address + "', " +
                "Deleted = " + (supplierModel.Deleted ? 1 : 0) + " " +
                "WHERE SupplierId = " + supplierModel.SupplierId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSupplier(SupplierModel supplierModel) {
        String sqlStatement = "DELETE FROM Supplier WHERE SupplierId = " + supplierModel.SupplierId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<SupplierModel> getSearchedSuppliersList(String searchKeyword) {
        ArrayList<SupplierModel> searchedSuppliersList = new ArrayList<>();
        ArrayList<SupplierModel> allSuppliersList = getSupplierList();
        for (SupplierModel supplier : allSuppliersList) {
            if (supplier.SupplierName.toLowerCase().contains(searchKeyword.toLowerCase())) {
                searchedSuppliersList.add(supplier);
            }
        }
        return searchedSuppliersList;
    }
}
