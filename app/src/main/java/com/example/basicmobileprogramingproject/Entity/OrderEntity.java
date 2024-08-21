package com.example.basicmobileprogramingproject.Entity;

import java.util.Date;

import android.content.Context;
import android.database.Cursor;

import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.Model.UserModel;

import java.util.ArrayList;

public class OrderEntity {
    private DatabaseHandler databaseHandler;

    public OrderEntity(Context context) {
        this.databaseHandler = new DatabaseHandler(context);
    }

    public ArrayList<OrderModel> getOrderList() {
        ArrayList<OrderModel> arrayList = new ArrayList<>();
        Cursor cursor = null;
        String sqlStatement = "SELECT * FROM Orders WHERE Deleted = 0";

        try {
            cursor = databaseHandler.getData(sqlStatement);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    OrderModel orderModel = new OrderModel();
                    orderModel.OrderId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderId"));
                    orderModel.UserId = cursor.getInt(cursor.getColumnIndexOrThrow("UserId"));
                    orderModel.StaffId = cursor.getInt(cursor.getColumnIndexOrThrow("StaffId"));
                    orderModel.OrderStatus = cursor.getString(cursor.getColumnIndexOrThrow("OrderStatus"));
                    orderModel.TotalAmount = cursor.getDouble(cursor.getColumnIndexOrThrow("TotalAmount"));
                    orderModel.DayBuy = cursor.getString(cursor.getColumnIndexOrThrow("DayBuy"));
                    orderModel.DeliveryAddress = cursor.getString(cursor.getColumnIndexOrThrow("DeliveryAddress"));
                    orderModel.Deleted = cursor.getInt(cursor.getColumnIndexOrThrow("Deleted")) == 1;

                    arrayList.add(orderModel);
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

    public ArrayList<OrderModel> getOrderForStaff(){
        ArrayList<OrderModel> orderList = getOrderList();
        ArrayList<OrderModel> orderListForStaff = new ArrayList<>();
        for (OrderModel order : orderList) {
            if (order.OrderStatus.equals("Waiting for confirmation")) {
                orderListForStaff.add(order);
            }
        }
        return orderListForStaff;
    }

    public ArrayList<OrderModel> getOrderForCustomer(){
        ArrayList<OrderModel> orderList = getOrderList();
        ArrayList<OrderModel> orderListForCustomer = new ArrayList<>();
        for (OrderModel order : orderList) {
            if (order.OrderStatus.equals("Received the goods")) {
                orderListForCustomer.add(order);
            }
        }
        return orderListForCustomer;
    }

    public int getFinalOrderId() {
        int finalOrderId = 0;
        ArrayList<OrderModel> orderList = getOrderList();
        for (OrderModel order : orderList) {
            if (order.OrderId > finalOrderId) {
                finalOrderId = order.OrderId;
            }
        }
        return finalOrderId;
    }

    public ArrayList<OrderModel> getListOfOrdersByUserID(ArrayList<UserModel> userModel) {
        ArrayList<OrderModel> orderList = getOrderList();
        ArrayList<OrderModel> searchedOrderList = new ArrayList<>();
        for (OrderModel order : orderList) {
            for (UserModel user : userModel) {
                if (order.UserId == user.UserId) {
                    searchedOrderList.add(order);
                }
            }
        }
        return searchedOrderList;
    }

    public double getTotalRevenue() {
        double totalRevenue = 0;
        ArrayList<OrderModel> orderList = getOrderList();
        for (OrderModel order : orderList) {
            totalRevenue += order.TotalAmount;
        }
        return totalRevenue;
    }

    public boolean insertOrder(OrderModel orderModel) {
        String sqlStatement = "INSERT INTO Orders (UserId, StaffId, OrderStatus, TotalAmount, DayBuy, DeliveryAddress, Deleted) " +
                "VALUES (" + orderModel.UserId + ", " +
                orderModel.StaffId + ", " +
                "'" + orderModel.OrderStatus + "', " +
                orderModel.TotalAmount + ", " +
                orderModel.DayBuy + ", " +
                "'" + orderModel.DeliveryAddress + "', " +
                (orderModel.Deleted ? 1 : 0) + ")";

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrder(OrderModel orderModel) {
        String sqlStatement = "UPDATE Orders SET " +
                "UserId = " + orderModel.UserId + ", " +
                "StaffId = " + orderModel.StaffId + ", " +
                "OrderStatus = '" + orderModel.OrderStatus + "', " +
                "TotalAmount = " + orderModel.TotalAmount + ", " +
                "DayBuy = " + orderModel.DayBuy + ", " +
                "DeliveryAddress = '" + orderModel.DeliveryAddress + "', " +
                "Deleted = " + (orderModel.Deleted ? 1 : 0) + " " +
                "WHERE OrderId = " + orderModel.OrderId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOrder(OrderModel orderModel) {
        String sqlStatement = "DELETE FROM Orders WHERE OrderId = " + orderModel.OrderId;

        try {
            databaseHandler.executeSQL(sqlStatement);
            return true; // Trả về true nếu câu lệnh thực thi thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu có lỗi xảy ra
        }
    }
}

