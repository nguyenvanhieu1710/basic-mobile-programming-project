package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;

import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.Model.StaffModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StaffEntity {
    private DatabaseHandler databaseHandler;
    private Context context;
    AccountEntity accountEntity;

    public StaffEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
        accountEntity = new AccountEntity(context);
    }

    public ArrayList<StaffModel> getStaffList() {
        ArrayList<StaffModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Staff WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StaffModel staffModel = new StaffModel();
                    staffModel.StaffId = cursor.getInt(cursor.getColumnIndexOrThrow("StaffId"));
                    staffModel.Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    staffModel.Birthday = cursor.getString(cursor.getColumnIndexOrThrow("Birthday"));
                    staffModel.PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"));
                    staffModel.Image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
                    staffModel.Gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"));
                    staffModel.Address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                    staffModel.Position = cursor.getString(cursor.getColumnIndexOrThrow("Position"));
                    staffModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(staffModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public StaffModel getStaffById(int staffId) {
        StaffModel staffModel = null;
        ArrayList<StaffModel> staffList = getStaffList();
        for (StaffModel staff : staffList) {
            if (staff.StaffId == staffId) {
                staffModel = staff;
                break;
            }
        }
        return staffModel;
    }

    public ArrayList<StaffModel> getStaffListingHasBeenDeleted() {
        ArrayList<StaffModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Staff WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    StaffModel staffModel = new StaffModel();
                    staffModel.StaffId = cursor.getInt(cursor.getColumnIndexOrThrow("StaffId"));
                    staffModel.Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    staffModel.Birthday = cursor.getString(cursor.getColumnIndexOrThrow("Birthday"));
                    staffModel.PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"));
                    staffModel.Image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
                    staffModel.Gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"));
                    staffModel.Address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                    staffModel.Position = cursor.getString(cursor.getColumnIndexOrThrow("Position"));
                    staffModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(staffModel);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public boolean insertStaff(StaffModel staffModel) {
        String sqlStatement = "INSERT INTO Staff (StaffId, Name, Birthday, PhoneNumber, Image, Gender, Address, Position, Deleted) " +
                "VALUES ('" + staffModel.StaffId + "','" + staffModel.Name + "', " +
                "'" + staffModel.Birthday + "', " +
                "'" + staffModel.PhoneNumber + "', " +
                "'" + staffModel.Image + "', " +
                "'" + staffModel.Gender + "', " +
                "'" + staffModel.Address + "', " +
                "'" + staffModel.Position + "', " +
                (staffModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateStaff(StaffModel staffModel) {
        String sqlStatement = "UPDATE Staff SET " +
                "Name = '" + staffModel.Name + "', " +
                "Birthday = '" + staffModel.Birthday + "', " +
                "PhoneNumber = '" + staffModel.PhoneNumber + "', " +
                "Image = '" + staffModel.Image + "', " +
                "Gender = '" + staffModel.Gender + "', " +
                "Address = '" + staffModel.Address + "', " +
                "Position = '" + staffModel.Position + "', " +
                "Deleted = " + (staffModel.Deleted ? 1 : 0) + " " +
                "WHERE StaffId = " + staffModel.StaffId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteStaff(StaffModel staffModel) {
        String sqlStatement = "DELETE FROM Staff WHERE StaffId = " + staffModel.StaffId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getNumberOfStaffsDeleted() {
        int numberOfStaffsDeleted = 0;
        ArrayList<StaffModel> staffList = getStaffListingHasBeenDeleted();
        for (StaffModel staff : staffList) {
            if (staff.Deleted) {
                numberOfStaffsDeleted++;
            }
        }
        return numberOfStaffsDeleted;
    }

    public ArrayList<StaffModel> getSearchedStaffsList(String searchText) {
        ArrayList<StaffModel> searchedStaffsList = new ArrayList<>();
        ArrayList<StaffModel> staffList = getStaffList();
        for (StaffModel staff : staffList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (staff.Name.toLowerCase().contains(searchText.toLowerCase())) {
                searchedStaffsList.add(staff);
            }
        }
        return searchedStaffsList;
    }
}
