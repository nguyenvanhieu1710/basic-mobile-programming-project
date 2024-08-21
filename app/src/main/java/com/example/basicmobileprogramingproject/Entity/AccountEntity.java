package com.example.basicmobileprogramingproject.Entity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.ArrayList;

public class AccountEntity {
    DatabaseHandler databaseHandler;
    Context context;

    public AccountEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<AccountModel> getAccountList() {
        ArrayList<AccountModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Account WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    AccountModel accountModel = new AccountModel();

                    accountModel.AccountId = cursor.getInt(cursor.getColumnIndexOrThrow("AccountId"));
                    accountModel.AccountName = cursor.getString(cursor.getColumnIndexOrThrow("AccountName"));
                    accountModel.Password = cursor.getString(cursor.getColumnIndexOrThrow("Password"));
                    accountModel.Role = cursor.getString(cursor.getColumnIndexOrThrow("Role"));
                    accountModel.DayCreated = cursor.getString(cursor.getColumnIndexOrThrow("DayCreated"));
                    accountModel.RememberPassword = cursor.getInt(cursor.getColumnIndexOrThrow("RememberPassword")) == 1;
                    accountModel.Email = cursor.getString(cursor.getColumnIndexOrThrow("Email"));
                    accountModel.Status = cursor.getString(cursor.getColumnIndexOrThrow("Status"));
                    accountModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(accountModel);
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

    public boolean checkExistAccount(AccountModel account) {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel accountModel : accountList) {
            if (accountModel.AccountName.equals(account.AccountName)) {
                return true;
            }
        }
        return false;
    }

    public AccountModel getAccountById(int accountId) {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel account : accountList) {
            if (account.AccountId == accountId) {
                return account;
            }
        }
        return null;
    }

    public String getRole(AccountModel account) {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.AccountName.equals(account.AccountName)) {
                return acc.Role;
            }
        }
        return null;
    }

    public int getAccountId(AccountModel account) {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.AccountName.equals(account.AccountName)) {
                return acc.AccountId;
            }
        }
        return -1;
    }

    public AccountModel getOnlineAccount() {
        ArrayList<AccountModel> accountList = getAccountList();
        AccountModel onlineAccount = null;
        for (AccountModel account : accountList) {
            if (account.Status.equals("Online")) {
                onlineAccount = account;
                break;
            }
        }
        return onlineAccount;
    }

    public Boolean activateYourAccountOnline(AccountModel accountModel) {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.AccountName.equals(accountModel.AccountName)) {
                acc.Status = "Online";
                updateAccount(acc);
                return true;
            }
        }
        return false;
    }

    public Boolean DisableAllOnlineAccounts() {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.Status.equals("Online")) {
                acc.Status = "Offline";
                updateAccount(acc);
            }
        }
        return false;
    }

    public Boolean deactivateYourAccountOnline(AccountModel accountModel) {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.AccountName.equals(accountModel.AccountName)) {
                acc.Status = "Offline";
                updateAccount(acc);
                return true;
            }
        }
        return false;
    }

    public Boolean checkAccountActive() {
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.Status.equals("Online")) {
                return true;
            }
        }
        return false;
    }

    public boolean insertAccount(AccountModel accountModel) {
        String sqlStatement = "INSERT INTO Account (AccountName, Password, Role, DayCreated, RememberPassword, Email, Status, Deleted) " +
                "VALUES ('" + accountModel.AccountName + "', '" +
                accountModel.Password + "', '" +
                accountModel.Role + "', '" +
                accountModel.DayCreated + "', " +
                (accountModel.RememberPassword ? 1 : 0) + ", '" +
                accountModel.Email + "', '" +
                accountModel.Status + "', " +
                (accountModel.Deleted ? 1 : 0) + ")";
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            throw e;
//            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
//            return false;
        }
    }

    public boolean updateAccount(AccountModel accountModel) {
        String sqlStatement = "UPDATE Account SET " +
                "AccountName = '" + accountModel.AccountName + "', " +
                "Password = '" + accountModel.Password + "', " +
                "Role = '" + accountModel.Role + "', " +
                "DayCreated = '" + accountModel.DayCreated + "', " +
                "RememberPassword = " + (accountModel.RememberPassword ? 1 : 0) + ", " +
                "Email = '" + accountModel.Email + "', " +
                "Status = '" + accountModel.Status + "', " +
                "Deleted = " + (accountModel.Deleted ? 1 : 0) + " " +
                "WHERE AccountId = " + accountModel.AccountId;
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAccount(AccountModel accountModel) {
        String sqlStatement = "DELETE FROM Account WHERE AccountId = " + accountModel.AccountId;
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public int getFinalAccountId() {
        int finalAccountId = -1;
        ArrayList<AccountModel> accountList = getAccountList();
        for (AccountModel acc : accountList) {
            if (acc.AccountId > finalAccountId) {
                finalAccountId = acc.AccountId;
            }
        }
        return finalAccountId;
    }
}
