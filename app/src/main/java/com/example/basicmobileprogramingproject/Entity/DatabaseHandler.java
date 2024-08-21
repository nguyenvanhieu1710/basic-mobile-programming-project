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
    public void onCreate(SQLiteDatabase db) {
        // danh sách bảng không phụ thuộc
//        String CREATE_TABLE_CATEGORY = "CREATE TABLE Category (" +
//                "CategoryId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "CategoryName TEXT NOT NULL, " +
//                "DadCategoryId INTEGER NOT NULL, " +
//                "Deleted TEXT NOT NULL)";
//        sqLiteDatabase.execSQL(CREATE_TABLE_CATEGORY);
//
//        String CREATE_TABLE_SUPPLIER = "CREATE TABLE Supplier (" +
//                "SupplierId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "SupplierName TEXT NOT NULL, " +
//                "PhoneNumber TEXT NOT NULL, " +
//                "Address TEXT NOT NULL, " +
//                "Deleted BOOLEAN NOT NULL)";
//        sqLiteDatabase.execSQL(CREATE_TABLE_SUPPLIER);
//
//        String CREATE_TABLE_VOUCHER = "CREATE TABLE Voucher (" +
//                "VoucherId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "VoucherName TEXT NOT NULL, " +
//                "Price REAL NOT NULL, " +
//                "MinimumPrice REAL NOT NULL, " +
//                "Quantity INTEGER NOT NULL, " +
//                "StartDay TEXT NOT NULL, " +
//                "EndDate TEXT NOT NULL, " +
//                "Deleted INTEGER NOT NULL)";
//        sqLiteDatabase.execSQL(CREATE_TABLE_VOUCHER);
//
//        String CREATE_TABLE_ACCOUNT = "CREATE TABLE Account (" +
//                "AccountId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "AccountName TEXT NOT NULL, " +
//                "Password TEXT NOT NULL, " +
//                "Role TEXT NOT NULL, " +
//                "DayCreated TEXT NOT NULL, " +
//                "RememberPassword INTEGER NOT NULL, " +
//                "Email TEXT NOT NULL, " +
//                "Status TEXT NOT NULL, " +
//                "Deleted BOOLEAN NOT NULL)";
//        sqLiteDatabase.execSQL(CREATE_TABLE_ACCOUNT);
//
//        // danh sách bảng phụ thuộc
//        String CREATE_TABLE_PRODUCT = "CREATE TABLE Product (" +
//                "ProductId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "ProductName TEXT NOT NULL, " +
//                "Quantity INTEGER NOT NULL, " +
//                "Price REAL NOT NULL, " +
//                "Description TEXT NOT NULL, " +
//                "Brand TEXT NOT NULL, " +
//                "ProductImage TEXT NOT NULL, " +
//                "ProductDetail TEXT NOT NULL, " +
//                "CategoryId INTEGER NOT NULL, " +
//                "Deleted INTEGER NOT NULL, " +
//                "FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_PRODUCT);
//
//        String CREATE_TABLE_USER = "CREATE TABLE User (" +
//                "UserId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "Name TEXT NOT NULL, " +
//                "Birthday TEXT NOT NULL, " +
//                "PhoneNumber TEXT NOT NULL, " +
//                "Image TEXT NOT NULL, " +
//                "Gender TEXT NOT NULL, " +
//                "Address TEXT NOT NULL, " +
//                "Deleted INTEGER NOT NULL, " +
//                "FOREIGN KEY (UserId) REFERENCES Account(AccountId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
//
//        String CREATE_TABLE_CART = "CREATE TABLE Cart (" +
//                "ProductId INTEGER, " +
//                "UserId INTEGER, " +
//                "PRIMARY KEY (ProductId, UserId), " +
//                "FOREIGN KEY (ProductId) REFERENCES Product(ProductId), " +
//                "FOREIGN KEY (UserId) REFERENCES User(UserId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_CART);
//
//
//        String CREATE_TABLE_IMPORT_BILL = "CREATE TABLE ImportBill (" +
//                "ImportBillId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "SupplierId INTEGER NOT NULL, " +
//                "StaffId INTEGER NOT NULL, " +
//                "ToTalAmount REAL NOT NULL, " +
//                "InputDay TEXT NOT NULL, " +
//                "FOREIGN KEY (SupplierId) REFERENCES Supplier(SupplierId), " +
//                "FOREIGN KEY (StaffId) REFERENCES User(UserId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_IMPORT_BILL);
//
//        String CREATE_TABLE_IMPORT_BILL_DETAIL = "CREATE TABLE ImportBillDetail (" +
//                "ImportBillDetailId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "ImportBillId INTEGER NOT NULL, " +
//                "ProductId INTEGER NOT NULL, " +
//                "ImportPrice REAL NOT NULL, " +
//                "Quantity INTEGER NOT NULL, " +
//                "FOREIGN KEY (ImportBillId) REFERENCES ImportBill(ImportBillId), " +
//                "FOREIGN KEY (ProductId) REFERENCES Product(ProductId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_IMPORT_BILL_DETAIL);
//
//        String CREATE_TABLE_ORDER = "CREATE TABLE Orders (" +
//                "OrderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "UserId INTEGER NOT NULL, " +
//                "StaffId INTEGER NOT NULL, " +
//                "OrderStatus TEXT NOT NULL, " +
//                "TotalAmount REAL NOT NULL, " +
//                "DayBuy TEXT NOT NULL, " +
//                "DeliveryAddress TEXT NOT NULL, " +
//                "Deleted INTEGER NOT NULL, " +
//                "FOREIGN KEY (UserId) REFERENCES User(UserId), " +
//                "FOREIGN KEY (StaffId) REFERENCES User(UserId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_ORDER);
//
//        String CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE OrderDetail (" +
//                "OrderDetailId INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "OrderId INTEGER NOT NULL, " +
//                "ProductId INTEGER NOT NULL, " +
//                "Quantity INTEGER NOT NULL, " +
//                "Price REAL NOT NULL, " +
//                "DiscountAmount REAL NOT NULL, " +
//                "VoucherId INTEGER NOT NULL, " +
//                "FOREIGN KEY (VoucherId) REFERENCES Voucher(VoucherId), " +
//                "FOREIGN KEY (OrderId) REFERENCES Orders(OrderId), " +
//                "FOREIGN KEY (ProductId) REFERENCES Product(ProductId))";
//        sqLiteDatabase.execSQL(CREATE_TABLE_ORDER_DETAIL);
    }

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
