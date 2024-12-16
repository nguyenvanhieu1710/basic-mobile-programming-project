package com.example.basicmobileprogramingproject.Entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.sqlite";
    //    private static final String DATABASE_PATH = "/data/data/com.example.myapplication/databases/";
    private String DATABASE_PATH = "";

    private static final int DATABASE_VERSION = 1;
    private final Context context;
    private SQLiteDatabase sqLiteDatabase;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    public void getDatabasePath() {
        String path = context.getDatabasePath(DATABASE_NAME).getPath();
        AlertDialogUtils.showInfoDialog(context, "Database path: " + path);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDatabase() throws IOException {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDatabase();
                AlertDialogUtils.showSuccessDialog(context, "Create database success");
            } catch (IOException e) {
                AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
                throw new Error("Error copying database");
            }
        } else {
            AlertDialogUtils.showInfoDialog(context, "Database already exists");
        }
    }

    private boolean checkDatabase() {
        try (SQLiteDatabase checkDB = SQLiteDatabase.openDatabase(DATABASE_PATH, null, SQLiteDatabase.OPEN_READONLY)) {
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Database doesn't exist");
            return false;
        }
    }

    private void copyDatabase() throws IOException {
        InputStream input = context.getAssets().open(DATABASE_NAME);
        String outFileName = DATABASE_PATH + DATABASE_NAME;
        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }
        output.flush();
        output.close();
        input.close();
    }

    public void openDatabase() {
        String path = DATABASE_PATH + DATABASE_NAME;
        sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (sqLiteDatabase != null && sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }

    public void executeSQL(String sql) {
        try {
            openDatabase();
            sqLiteDatabase.execSQL(sql);
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
        } finally {
            closeDatabase();
        }
    }

    public Cursor getData(String sql) {
        Cursor cursor = null;
        try {
            openDatabase();
            cursor = sqLiteDatabase.rawQuery(sql, null);
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
        }
        return cursor;
    }

    public Boolean deleteDatabase() {
        return context.deleteDatabase(DATABASE_NAME);
    }
}
