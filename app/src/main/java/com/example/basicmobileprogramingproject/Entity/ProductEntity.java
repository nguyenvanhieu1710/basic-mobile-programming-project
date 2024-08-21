package com.example.basicmobileprogramingproject.Entity;

import android.content.Context;
import android.database.Cursor;

import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class ProductEntity {
    DatabaseHandler databaseHandler;
    Context context;

    public ProductEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<ProductModel> getProductList() {
        ArrayList<ProductModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Product WHERE Deleted = 0 AND Quantity > 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ProductModel product = new ProductModel();
                    product.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    product.ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                    product.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    product.Price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"));
                    product.Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                    product.Brand = cursor.getString(cursor.getColumnIndexOrThrow("Brand"));
                    product.ProductImage = cursor.getString(cursor.getColumnIndexOrThrow("ProductImage"));
                    product.ProductDetail = cursor.getString(cursor.getColumnIndexOrThrow("ProductDetail"));
                    product.CategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("CategoryId"));
                    product.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public ProductModel getProductById(int productId) {
        ArrayList<ProductModel> productList = getProductList();
        ProductModel productModel = new ProductModel();
        for (ProductModel product : productList) {
            if (product.ProductId == productId) {
                productModel = product;
                break;
            }
        }
        return productModel;
    }

    public ArrayList<ProductModel> getProductsLowStock() {
        ArrayList<ProductModel> arrayList = new ArrayList<>();
        ArrayList<ProductModel> productList = getProductList();
        for (ProductModel product : productList) {
            if (product.Quantity < 2) {
                arrayList.add(product);
            }
        }
        return arrayList;
    }

    public boolean insertProduct(ProductModel productModel) {
        String sqlStatement = "INSERT INTO Product (ProductName, Quantity, Price, Description, Brand, ProductImage, ProductDetail, CategoryId, Deleted) " +
                "VALUES ('" + productModel.ProductName + "', " +
                productModel.Quantity + ", " +
                productModel.Price + ", " +
                "'" + productModel.Description + "', " +
                "'" + productModel.Brand + "', " +
                "'" + productModel.ProductImage + "', " +
                "'" + productModel.ProductDetail + "', " +
                productModel.CategoryId + ", " +
                (productModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateProduct(ProductModel productModel) {
        String sqlStatement = "UPDATE Product SET " +
                "ProductName = '" + productModel.ProductName + "', " +
                "Quantity = " + productModel.Quantity + ", " +
                "Price = " + productModel.Price + ", " +
                "Description = '" + productModel.Description + "', " +
                "Brand = '" + productModel.Brand + "', " +
                "ProductImage = '" + productModel.ProductImage + "', " +
                "ProductDetail = '" + productModel.ProductDetail + "', " +
                "CategoryId = " + productModel.CategoryId + ", " +
                "Deleted = " + (productModel.Deleted ? 1 : 0) + " " +
                "WHERE ProductId = " + productModel.ProductId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteProduct(ProductModel productModel) {
        String sqlStatement = "DELETE FROM Product WHERE ProductId = " + productModel.ProductId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public Integer getNumberOfProductsDeleted() {
        Integer numberOfProductsDeleted = 0;
        ArrayList<ProductModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Product WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ProductModel product = new ProductModel();
                    product.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    product.ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                    product.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    product.Price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"));
                    product.Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                    product.Brand = cursor.getString(cursor.getColumnIndexOrThrow("Brand"));
                    product.ProductImage = cursor.getString(cursor.getColumnIndexOrThrow("ProductImage"));
                    product.ProductDetail = cursor.getString(cursor.getColumnIndexOrThrow("ProductDetail"));
                    product.CategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("CategoryId"));
                    product.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(product);
                } while (cursor.moveToNext());
            }

            numberOfProductsDeleted = arrayList.size();

        } catch (Exception exception) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return numberOfProductsDeleted;
    }

    public ArrayList<ProductModel> getProductListingHasBeenDeleted() {
        ArrayList<ProductModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Product WHERE Deleted = 1";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ProductModel product = new ProductModel();
                    product.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    product.ProductName = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
                    product.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    product.Price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"));
                    product.Description = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                    product.Brand = cursor.getString(cursor.getColumnIndexOrThrow("Brand"));
                    product.ProductImage = cursor.getString(cursor.getColumnIndexOrThrow("ProductImage"));
                    product.ProductDetail = cursor.getString(cursor.getColumnIndexOrThrow("ProductDetail"));
                    product.CategoryId = cursor.getInt(cursor.getColumnIndexOrThrow("CategoryId"));
                    product.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(product);
                } while (cursor.moveToNext());
            }

        } catch (Exception exception) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + exception.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            databaseHandler.closeDatabase();
        }

        return arrayList;
    }

    public ArrayList<ProductModel> getSearchedProductsList(String searchText) {
        ArrayList<ProductModel> searchedProductsList = new ArrayList<>();
        ArrayList<ProductModel> productList = getProductList();
        for (ProductModel product : productList) {
            // chuyển thành chữ thường cho dễ tìm kiếm
            if (product.ProductName.toLowerCase().contains(searchText.toLowerCase())) {
                searchedProductsList.add(product);
            }
        }
        return searchedProductsList;
    }

    public ArrayList<ProductModel> getProductsByCategoryId(int categoryId) {
        ArrayList<ProductModel> productsByCategory = new ArrayList<>();
        ArrayList<ProductModel> productList = getProductList();
        for (ProductModel product : productList) {
            if (product.CategoryId == categoryId) {
                productsByCategory.add(product);
            }
        }
        return productsByCategory;
    }

    public boolean minusQuantityAfterPay(int productId, int quantity) {
        String sqlStatement = "UPDATE Product SET Quantity = Quantity - " + quantity + " WHERE ProductId = " + productId;
        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
        }
        return false;
    }
}
