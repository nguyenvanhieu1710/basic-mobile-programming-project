package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.CartModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class CartEntity {
    private DatabaseHandler databaseHandler;
    private Context context;
    ProductEntity productEntity;

    public CartEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
        productEntity = new ProductEntity(context);
    }

    public ArrayList<CartModel> getCartList() {
        ArrayList<CartModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Cart";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    CartModel cartModel = new CartModel();
                    cartModel.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    cartModel.UserId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));

                    arrayList.add(cartModel);
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

    public boolean insertCart(CartModel cartModel) {
        String sqlStatement = "INSERT INTO Cart (ProductId, UserId) " +
                "VALUES (" + cartModel.ProductId + ", " + cartModel.UserId + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean updateCart(CartModel cartModel) {
        String sqlStatement = "UPDATE Cart SET " +
                "ProductId = " + cartModel.ProductId + " " +
                "WHERE UserId = " + cartModel.UserId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCart(CartModel cartModel) {
        String sqlStatement = "DELETE FROM Cart WHERE ProductId = " + cartModel.ProductId +
                " AND UserId = " + cartModel.UserId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            AlertDialogUtils.showErrorDialog(context, "Error: " + e.getMessage());
            return false;
        }
    }

    public boolean checkExist(CartModel cartModel) {
        ArrayList<CartModel> cartList = getCartList();
        for (CartModel cart : cartList) {
            if (cart.ProductId == cartModel.ProductId && cart.UserId == cartModel.UserId) {
                return true;
            }
        }
        return false;
    }

    public ProductModel getInfoProductInTheCartById(int productId) {
        ArrayList<ProductModel> productList = productEntity.getProductList();
        for (ProductModel product : productList) {
            if (product.ProductId == productId) {
                return product;
            }
        }
        return null;
    }

    public ArrayList<ProductModel> getProductsInTheCart() {
        ArrayList<ProductModel> productList = productEntity.getProductList();
        ArrayList<ProductModel> productsInTheCart = new ArrayList<>();
        for (ProductModel product : productList) {
            for (CartModel cart : getCartList()) {
                if (cart.ProductId == product.ProductId) {
                    productsInTheCart.add(product);
                }
            }
        }
        return productsInTheCart;
    }

    public ArrayList<ProductModel> getSearchedProductsList(String searchKeyword) {
        ArrayList<ProductModel> productInTheCartList = getProductsInTheCart();
        ArrayList<ProductModel> searchedProductsList = new ArrayList<>();
        for (ProductModel product : productInTheCartList) {
            if (product.ProductName.toLowerCase().contains(searchKeyword.toLowerCase())) {
                searchedProductsList.add(product);
            }
        }
        return searchedProductsList;
    }

    public ArrayList<CartModel> getCartListAfterSearch(String searchKeyword) {
        ArrayList<ProductModel> searchedProductsList = getSearchedProductsList(searchKeyword);
        ArrayList<CartModel> cartList = getCartList();
        ArrayList<CartModel> cartListAfterSearch = new ArrayList<>();
        for (CartModel cart : cartList) {
            for (ProductModel product : searchedProductsList) {
                if (cart.ProductId == product.ProductId) {
                    cartListAfterSearch.add(cart);
                }
            }
        }
        return cartListAfterSearch;
    }
}

