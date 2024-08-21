package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;

import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserEntity {
    private DatabaseHandler databaseHandler;
    private Context context;
    AccountEntity accountEntity;

    public UserEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
        accountEntity = new AccountEntity(context);
    }

    public ArrayList<UserModel> getUserList() {
        ArrayList<UserModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM User WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UserModel userModel = new UserModel();
                    userModel.UserId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));
                    userModel.Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    userModel.Birthday = cursor.getString(cursor.getColumnIndexOrThrow("Birthday"));
                    userModel.PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"));
                    userModel.Image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
                    userModel.Gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"));
                    userModel.Address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                    userModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(userModel);
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

    public ArrayList<UserModel> getCustomerList() {
        ArrayList<AccountModel> accountList = accountEntity.getAccountList();
        ArrayList<UserModel> userList = getUserList();
        ArrayList<UserModel> customerList = new ArrayList<>();
        for (UserModel user : userList) {
            for (AccountModel account : accountList) {
                if (user.UserId == account.AccountId && account.Role.equals("User")) {
                    customerList.add(user);
                }
            }
        }
        return customerList;
    }

    public ArrayList<UserModel> getStaffList() {
        ArrayList<AccountModel> accountList = accountEntity.getAccountList();
        ArrayList<UserModel> userList = getUserList();
        ArrayList<UserModel> staffList = new ArrayList<>();
        for (UserModel user : userList) {
            for (AccountModel account : accountList) {
                if (user.UserId == account.AccountId && account.Role.equals("Staff")) {
                    staffList.add(user);
                }
            }
        }
        return staffList;
    }

    public UserModel getUserById(int userId) {
        UserModel userModel = null;
        ArrayList<UserModel> userList = getUserList();
        for (UserModel user : userList) {
            if (user.UserId == userId) {
                userModel = user;
                break;
            }
        }
        return userModel;
    }

    public ArrayList<UserModel> getListOfCustomersWhoHavePurchasedTheProduct() {
        OrderEntity orderEntity = new OrderEntity(context);
        ArrayList<OrderModel> orderList = orderEntity.getOrderList();
        ArrayList<UserModel> customerList = getCustomerList();
        ArrayList<UserModel> listOfCustomersWhoHavePurchasedTheProduct = new ArrayList<>();
        for (OrderModel order : orderList) {
            for (UserModel customer : customerList) {
                if (order.UserId == customer.UserId) {
                    listOfCustomersWhoHavePurchasedTheProduct.add(customer);
                }
            }
        }
        return listOfCustomersWhoHavePurchasedTheProduct;
    }

    public ArrayList<UserModel> getUserListingHasBeenDeleted() {
        ArrayList<UserModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM User WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    UserModel userModel = new UserModel();
                    userModel.UserId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));
                    userModel.Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    userModel.Birthday = cursor.getString(cursor.getColumnIndexOrThrow("Birthday"));
                    userModel.PhoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber"));
                    userModel.Image = cursor.getString(cursor.getColumnIndexOrThrow("Image"));
                    userModel.Gender = cursor.getString(cursor.getColumnIndexOrThrow("Gender"));
                    userModel.Address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                    userModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(userModel);
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

    public ArrayList<UserModel> getCustomerListingHasBeenDeleted() {
        ArrayList<AccountModel> accountList = accountEntity.getAccountList();
        ArrayList<UserModel> userList = getUserListingHasBeenDeleted();
        ArrayList<UserModel> customerList = new ArrayList<>();
        for (UserModel user : userList) {
            for (AccountModel account : accountList) {
                if (user.UserId == account.AccountId && account.Role.equals("User")) {
                    customerList.add(user);
                }
            }
        }
        return customerList;
    }

    public ArrayList<UserModel> getStaffListingHasBeenDeleted() {
        ArrayList<AccountModel> accountList = accountEntity.getAccountList();
        ArrayList<UserModel> userList = getUserListingHasBeenDeleted();
        ArrayList<UserModel> staffList = new ArrayList<>();
        for (UserModel user : userList) {
            for (AccountModel account : accountList) {
                if (user.UserId == account.AccountId && account.Role.equals("Staff")) {
                    staffList.add(user);
                }
            }
        }
        return staffList;
    }

    public boolean insertUser(UserModel userModel) {
        String sqlStatement = "INSERT INTO User (Name, Birthday, PhoneNumber, Image, Gender, Address, Deleted) " +
                "VALUES ('" + userModel.Name + "', " +
                "'" + userModel.Birthday + "', " +
                "'" + userModel.PhoneNumber + "', " +
                "'" + userModel.Image + "', " +
                "'" + userModel.Gender + "', " +
                "'" + userModel.Address + "', " +
                (userModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(UserModel userModel) {
        String sqlStatement = "UPDATE User SET " +
                "Name = '" + userModel.Name + "', " +
                "Birthday = '" + userModel.Birthday + "', " +
                "PhoneNumber = '" + userModel.PhoneNumber + "', " +
                "Image = '" + userModel.Image + "', " +
                "Gender = '" + userModel.Gender + "', " +
                "Address = '" + userModel.Address + "', " +
                "Deleted = " + (userModel.Deleted ? 1 : 0) + " " +
                "WHERE UserId = " + userModel.UserId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(UserModel userModel) {
        String sqlStatement = "DELETE FROM User WHERE UserId = " + userModel.UserId;

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
        ArrayList<UserModel> staffList = getStaffListingHasBeenDeleted();
        for (UserModel staff : staffList) {
            if (staff.Deleted) {
                numberOfStaffsDeleted++;
            }
        }
        return numberOfStaffsDeleted;
    }

    public int getNumberOfCustomersDeleted() {
        int numberOfCustomersDeleted = 0;
        ArrayList<UserModel> customerList = getCustomerListingHasBeenDeleted();
        for (UserModel customer : customerList) {
            if (customer.Deleted) {
                numberOfCustomersDeleted++;
            }
        }
        return numberOfCustomersDeleted;
    }

    public ArrayList<UserModel> getSearchedCustomersList(String searchText) {
        ArrayList<UserModel> searchedCustomersList = new ArrayList<>();
        ArrayList<UserModel> customerList = getCustomerList();
        for (UserModel customer : customerList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (customer.Name.toLowerCase().contains(searchText.toLowerCase())) {
                searchedCustomersList.add(customer);
            }
        }
        return searchedCustomersList;
    }

    public ArrayList<UserModel> getSearchedStaffsList(String searchText) {
        ArrayList<UserModel> searchedStaffsList = new ArrayList<>();
        ArrayList<UserModel> staffList = getStaffList();
        for (UserModel staff : staffList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (staff.Name.toLowerCase().contains(searchText.toLowerCase())) {
                searchedStaffsList.add(staff);
            }
        }
        return searchedStaffsList;
    }
}
