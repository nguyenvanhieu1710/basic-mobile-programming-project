package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.VoucherModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class VoucherEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public VoucherEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<VoucherModel> getVoucherList() {
        ArrayList<VoucherModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Voucher WHERE Deleted = 0 AND Quantity > 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VoucherModel voucherModel = new VoucherModel();
                    voucherModel.VoucherId = cursor.getInt(cursor.getColumnIndexOrThrow("VoucherId"));
                    voucherModel.VoucherName = cursor.getString(cursor.getColumnIndexOrThrow("VoucherName"));
                    voucherModel.Price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"));
                    voucherModel.MinimumPrice = cursor.getInt(cursor.getColumnIndexOrThrow("MinimumPrice"));
                    voucherModel.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    voucherModel.StartDay = cursor.getString(cursor.getColumnIndexOrThrow("StartDay"));
                    voucherModel.EndDate = cursor.getString(cursor.getColumnIndexOrThrow("EndDate"));
                    voucherModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(voucherModel);
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

    public ArrayList<VoucherModel> getVoucherListingHasBeenDeleted() {
        ArrayList<VoucherModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Voucher WHERE Deleted = 1 AND Quantity > 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VoucherModel voucherModel = new VoucherModel();
                    voucherModel.VoucherId = cursor.getInt(cursor.getColumnIndexOrThrow("VoucherId"));
                    voucherModel.VoucherName = cursor.getString(cursor.getColumnIndexOrThrow("VoucherName"));
                    voucherModel.Price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"));
                    voucherModel.MinimumPrice = cursor.getInt(cursor.getColumnIndexOrThrow("MinimumPrice"));
                    voucherModel.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    voucherModel.StartDay = cursor.getString(cursor.getColumnIndexOrThrow("StartDay"));
                    voucherModel.EndDate = cursor.getString(cursor.getColumnIndexOrThrow("EndDate"));
                    voucherModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(voucherModel);
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

    public int getNumberOfVouchersDeleted() {
        int numberOfVouchersDeleted = 0;
        ArrayList<VoucherModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Voucher WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    VoucherModel voucherModel = new VoucherModel();
                    voucherModel.VoucherId = cursor.getInt(cursor.getColumnIndexOrThrow("VoucherId"));
                    voucherModel.VoucherName = cursor.getString(cursor.getColumnIndexOrThrow("VoucherName"));
                    voucherModel.Price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"));
                    voucherModel.MinimumPrice = cursor.getInt(cursor.getColumnIndexOrThrow("MinimumPrice"));
                    voucherModel.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    voucherModel.StartDay = cursor.getString(cursor.getColumnIndexOrThrow("StartDay"));
                    voucherModel.EndDate = cursor.getString(cursor.getColumnIndexOrThrow("EndDate"));
                    voucherModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(voucherModel);
                } while (cursor.moveToNext());
            }

            numberOfVouchersDeleted = arrayList.size();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return numberOfVouchersDeleted;
    }

    public boolean insertVoucher(VoucherModel voucherModel) {
        String sqlStatement = "INSERT INTO Voucher (VoucherName, Price, MinimumPrice, Quantity, StartDay, EndDate, Deleted) " +
                "VALUES ('" + voucherModel.VoucherName + "', " +
                voucherModel.Price + ", " +
                voucherModel.MinimumPrice + ", " +
                voucherModel.Quantity + ", " +
                "'" + voucherModel.StartDay + "', " +
                "'" + voucherModel.EndDate + "', " +
                (voucherModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateVoucher(VoucherModel voucherModel) {
        String sqlStatement = "UPDATE Voucher SET " +
                "VoucherName = '" + voucherModel.VoucherName + "', " +
                "Price = " + voucherModel.Price + ", " +
                "MinimumPrice = " + voucherModel.MinimumPrice + ", " +
                "Quantity = " + voucherModel.Quantity + ", " +
                "StartDay = '" + voucherModel.StartDay + "', " +
                "EndDate = '" + voucherModel.EndDate + "', " +
                "Deleted = " + (voucherModel.Deleted ? 1 : 0) + " " +
                "WHERE VoucherId = " + voucherModel.VoucherId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteVoucher(VoucherModel voucherModel) {
        String sqlStatement = "DELETE FROM Voucher WHERE VoucherId = " + voucherModel.VoucherId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    // Tìm kiếm danh mục theo tên
    public ArrayList<VoucherModel> getSearchedVouchersList(String searchText) {
        ArrayList<VoucherModel> searchedVouchersList = new ArrayList<>();
        ArrayList<VoucherModel> voucherList = getVoucherList();
        for (VoucherModel voucher : voucherList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (voucher.VoucherName.toLowerCase().contains(searchText.toLowerCase())) {
                searchedVouchersList.add(voucher);
            }
        }
        return searchedVouchersList;
    }
}

