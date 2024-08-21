package com.example.basicmobileprogramingproject.Entity;


import android.content.Context;
import android.database.Cursor;


import com.example.basicmobileprogramingproject.Model.OrderDetailModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailEntity {
    private DatabaseHandler databaseHandler;
    private Context context;

    public OrderDetailEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
        this.context = context;
    }

    public ArrayList<OrderDetailModel> getOrderDetailList() {
        ArrayList<OrderDetailModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM OrderDetail";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    OrderDetailModel orderDetailModel = new OrderDetailModel();
                    orderDetailModel.OrderDetailId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderDetailId"));
                    orderDetailModel.OrderId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderId"));
                    orderDetailModel.ProductId = cursor.getInt(cursor.getColumnIndexOrThrow("ProductId"));
                    orderDetailModel.Quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                    orderDetailModel.Price = cursor.getDouble(cursor.getColumnIndexOrThrow("Price"));
                    orderDetailModel.DiscountAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("DiscountAmount"));
                    orderDetailModel.VoucherId = cursor.getInt(cursor.getColumnIndexOrThrow("VoucherId"));

                    arrayList.add(orderDetailModel);
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

    public ArrayList<OrderDetailModel> getOrderDetailByOrderId(int orderId) {
        ArrayList<OrderDetailModel> orderDetailList = getOrderDetailList();
        ArrayList<OrderDetailModel> filteredList = new ArrayList<>();
        for (OrderDetailModel orderDetail : orderDetailList) {
            if (orderDetail.OrderId == orderId) {
                filteredList.add(orderDetail);
            }
        }
        return filteredList;
    }

//    public ArrayList<ProductModel> getBestSellingProducts() {
//        ProductEntity productEntity = new ProductEntity(context);
//        ArrayList<ProductModel> productList = productEntity.getProductList();
//        ArrayList<OrderDetailModel> orderDetailList = getOrderDetailList();
//        ArrayList<OrderDetailModel>
//        for (OrderDetailModel orderDetail : orderDetailList) {
//        }
//    }

    public List<ProductModel> getBestSellingProducts(List<OrderDetailModel> orderDetails, List<ProductModel> products) {
        // Map để lưu trữ tổng số lượng đã bán cho mỗi sản phẩm
        Map<Integer, Integer> productSalesMap = new HashMap<>();

        // Tổng hợp số lượng đã bán cho mỗi sản phẩm
        for (OrderDetailModel orderDetail : orderDetails) {
            int productId = orderDetail.ProductId;
            int quantitySold = orderDetail.Quantity;

            productSalesMap.put(productId, productSalesMap.getOrDefault(productId, 0) + quantitySold);
        }

        // Sắp xếp sản phẩm dựa trên tổng số lượng đã bán
        products.sort((p1, p2) -> {
            int quantity1 = productSalesMap.getOrDefault(p1.ProductId, 0);
            int quantity2 = productSalesMap.getOrDefault(p2.ProductId, 0);
            return Integer.compare(quantity2, quantity1); // Sắp xếp giảm dần
        });

        return products;
    }

    public boolean insertOrderDetail(OrderDetailModel orderDetailModel) {
        String sqlStatement = "INSERT INTO OrderDetail (OrderId, ProductId, Quantity, Price, DiscountAmount, VoucherId) " +
                "VALUES (" + orderDetailModel.OrderId + ", " +
                orderDetailModel.ProductId + ", " +
                orderDetailModel.Quantity + ", " +
                orderDetailModel.Price + ", " +
                orderDetailModel.DiscountAmount + ", " +
                orderDetailModel.VoucherId + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrderDetail(OrderDetailModel orderDetailModel) {
        String sqlStatement = "UPDATE OrderDetail SET " +
                "OrderId = " + orderDetailModel.OrderId + ", " +
                "ProductId = " + orderDetailModel.ProductId + ", " +
                "Quantity = " + orderDetailModel.Quantity + ", " +
                "Price = " + orderDetailModel.Price + ", " +
                "DiscountAmount = " + orderDetailModel.DiscountAmount + ", " +
                "VoucherId = " + orderDetailModel.VoucherId + " " +
                "WHERE OrderDetailId = " + orderDetailModel.OrderDetailId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrderDetail(OrderDetailModel orderDetailModel) {
        String sqlStatement = "DELETE FROM OrderDetail WHERE OrderDetailId = " + orderDetailModel.OrderDetailId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

