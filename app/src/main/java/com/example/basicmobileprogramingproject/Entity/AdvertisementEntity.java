package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.AdvertisementModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class AdvertisementEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public AdvertisementEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<AdvertisementModel> getAdvertisementList() {
        ArrayList<AdvertisementModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Advertisement WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    AdvertisementModel advertisementModel = new AdvertisementModel();
                    advertisementModel.AdvertisementId = cursor.getInt(cursor.getColumnIndexOrThrow("AdvertisementId"));
                    advertisementModel.AdvertisementName = cursor.getString(cursor.getColumnIndexOrThrow("AdvertisementName"));
                    advertisementModel.AdvertisementImage = cursor.getString(cursor.getColumnIndexOrThrow("AdvertisementImage"));
                    advertisementModel.Location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
                    advertisementModel.AdvertiserId = cursor.getInt(cursor.getColumnIndexOrThrow("AdvertiserId"));
                    advertisementModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(advertisementModel);
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

    public ArrayList<AdvertisementModel> getAdvertisementListingHasBeenDeleted() {
        ArrayList<AdvertisementModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Advertisement WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    AdvertisementModel advertisementModel = new AdvertisementModel();
                    advertisementModel.AdvertisementId = cursor.getInt(cursor.getColumnIndexOrThrow("AdvertisementId"));
                    advertisementModel.AdvertisementName = cursor.getString(cursor.getColumnIndexOrThrow("AdvertisementName"));
                    advertisementModel.AdvertisementImage = cursor.getString(cursor.getColumnIndexOrThrow("AdvertisementImage"));
                    advertisementModel.Location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
                    advertisementModel.AdvertiserId = cursor.getInt(cursor.getColumnIndexOrThrow("AdvertiserId"));
                    advertisementModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(advertisementModel);
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

    public int getNumberOfAdvertisementsDeleted() {
        int numberOfAdvertisementsDeleted = 0;
        ArrayList<AdvertisementModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Advertisement WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    AdvertisementModel advertisementModel = new AdvertisementModel();
                    advertisementModel.AdvertisementId = cursor.getInt(cursor.getColumnIndexOrThrow("AdvertisementId"));
                    advertisementModel.AdvertisementName = cursor.getString(cursor.getColumnIndexOrThrow("AdvertisementName"));
                    advertisementModel.AdvertisementImage = cursor.getString(cursor.getColumnIndexOrThrow("AdvertisementImage"));
                    advertisementModel.Location = cursor.getString(cursor.getColumnIndexOrThrow("Location"));
                    advertisementModel.AdvertiserId = cursor.getInt(cursor.getColumnIndexOrThrow("AdvertiserId"));
                    advertisementModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(advertisementModel);
                } while (cursor.moveToNext());
            }

            numberOfAdvertisementsDeleted = arrayList.size();

        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return numberOfAdvertisementsDeleted;
    }

    public boolean insertAdvertisement(AdvertisementModel advertisementModel) {
        String sqlStatement = "INSERT INTO Advertisement (AdvertisementName, AdvertisementImage, Location, AdvertiserId, Deleted) " +
                "VALUES ('" + advertisementModel.AdvertisementName + "', " +
                advertisementModel.AdvertisementImage + ", " +
                advertisementModel.Location + ", " +
                "'" + advertisementModel.AdvertiserId + "', " +
                (advertisementModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateAdvertisement(AdvertisementModel advertisementModel) {
        String sqlStatement = "UPDATE Advertisement SET " +
                "AdvertisementName = '" + advertisementModel.AdvertisementName + "', " +
                "AdvertisementImage = " + advertisementModel.AdvertisementImage + ", " +
                "Location = " + advertisementModel.Location + ", " +
                "AdvertiserId = " + advertisementModel.AdvertiserId + ", " +
                "Deleted = " + (advertisementModel.Deleted ? 1 : 0) + " " +
                "WHERE AdvertisementId = " + advertisementModel.AdvertisementId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAdvertisement(AdvertisementModel advertisementModel) {
        String sqlStatement = "DELETE FROM Advertisement WHERE AdvertisementId = " + advertisementModel.AdvertisementId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<AdvertisementModel> getSearchedAdvertisementsList(String searchText) {
        ArrayList<AdvertisementModel> searchedAdvertisementsList = new ArrayList<>();
        ArrayList<AdvertisementModel> advertisementList = getAdvertisementList();
        for (AdvertisementModel advertisement : advertisementList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (advertisement.AdvertisementName.toLowerCase().contains(searchText.toLowerCase())) {
                searchedAdvertisementsList.add(advertisement);
            }
        }
        return searchedAdvertisementsList;
    }
}

