package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Adapter.ProductAdapter;
import com.example.basicmobileprogramingproject.Entity.OrderDetailEntity;
import com.example.basicmobileprogramingproject.Entity.OrderEntity;
import com.example.basicmobileprogramingproject.Entity.ProductEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.OrderDetailModel;
import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;

import org.w3c.dom.Document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StatisticalFragment extends Fragment {
    View view;
    TextView txtTotalRevenue, txtTotalOrders, txtBestSellingProducts, txtLowStockAlert, txtTotalUsers;
    RecyclerView recyclerBestSellingProducts, recyclerLowStockAlert;
    Button btnExportReport;
    UserEntity userEntity;
    ProductEntity productEntity;
    OrderEntity orderEntity;
    OrderDetailEntity orderDetailEntity;
    ProductAdapter productAdapter, productsLowStockAdapter;
    ArrayList<OrderModel> orderList;
    ArrayList<OrderDetailModel> orderDetailList;
    ArrayList<ProductModel> productList;
    ArrayList<UserModel> userModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.statistical_fragment, container, false);
        // mapping id
        txtTotalRevenue = view.findViewById(R.id.txtTotalRevenue);
        txtTotalOrders = view.findViewById(R.id.txtTotalOrders);
        txtBestSellingProducts = view.findViewById(R.id.txtBestSellingProducts);
        txtLowStockAlert = view.findViewById(R.id.txtLowStockAlert);
        txtTotalUsers = view.findViewById(R.id.txtTotalUsers);

        recyclerBestSellingProducts = view.findViewById(R.id.recyclerBestSellingProducts);
        recyclerLowStockAlert = view.findViewById(R.id.recyclerLowStockAlert);

        btnExportReport = view.findViewById(R.id.btnExportReport);

        userEntity = new UserEntity(getContext());
        userModel = userEntity.getCustomerList();
        orderEntity = new OrderEntity(getContext());
        orderList = orderEntity.getOrderList();
        orderDetailEntity = new OrderDetailEntity(getContext());
        orderDetailList = orderDetailEntity.getOrderDetailList();
        productEntity = new ProductEntity(getContext());
        productList = productEntity.getProductList();

        handleEventsClickButton();
        uploadDataToView();
        return view;
    }

    public void handleEventsClickButton() {
        btnExportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument pdfDocument = new PdfDocument();
                Paint paint = new Paint();
                paint.setTextSize(16);

                // Tạo trang mới trong tài liệu PDF
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                PdfDocument.Page page = pdfDocument.startPage(pageInfo);
                Canvas canvas = page.getCanvas();

                int yPosition = 50;

                // Thêm các thông tin thống kê vào PDF
                canvas.drawText("Total Revenue: " + txtTotalRevenue.getText().toString(), 20, yPosition, paint);
                yPosition += 20;
                canvas.drawText("Total Orders: " + txtTotalOrders.getText().toString(), 20, yPosition, paint);
                yPosition += 20;
                canvas.drawText("Total Users: " + txtTotalUsers.getText().toString(), 20, yPosition, paint);
                yPosition += 20;

                // Thêm danh sách các sản phẩm bán chạy nhất
                canvas.drawText("Best Selling Products:", 20, yPosition, paint);
                yPosition += 20;
                List<ProductModel> bestSellingProducts = orderDetailEntity.getBestSellingProducts(orderDetailList, productList);
                for (ProductModel product : bestSellingProducts) {
                    canvas.drawText("- " + product.ProductName, 40, yPosition, paint);
                    yPosition += 20;
                }

                // Thêm danh sách các sản phẩm có lượng hàng tồn kho thấp
                canvas.drawText("Low Stock Products:", 20, yPosition, paint);
                yPosition += 20;
                List<ProductModel> lowStockProducts = productEntity.getProductsLowStock();
                for (ProductModel product : lowStockProducts) {
                    canvas.drawText("- " + product.ProductName, 40, yPosition, paint);
                    yPosition += 20;
                }

                pdfDocument.finishPage(page);

                // Lưu tệp PDF vào bộ nhớ thiết bị
                File file = new File(getContext().getFilesDir(), "report.pdf");
                try {
                    pdfDocument.writeTo(new FileOutputStream(file));

                    // Hiển thị thông báo thành công
                    Toast.makeText(getContext(), "PDF created successfully!", Toast.LENGTH_SHORT).show();

                    // Kiểm tra sự tồn tại của tệp
                    if (file.exists()) {
                        // Tệp đã được tạo thành công
                        Toast.makeText(getContext(), "File saved at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    } else {
                        // Tệp không tồn tại
                        Toast.makeText(getContext(), "File creation failed!", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    // Hiển thị thông báo lỗi
                    Toast.makeText(getContext(), "Error creating PDF: " + e.getMessage(), Toast.LENGTH_LONG).show();
                } finally {
                    pdfDocument.close();
                }
            }
        });
    }

    public void uploadDataToView() {
        txtTotalRevenue.setText("Total Revenue: $" + String.valueOf(orderEntity.getTotalRevenue()));
        txtTotalOrders.setText("Total Orders: " + String.valueOf(orderList.size()));
        txtTotalUsers.setText("Total Users: " + String.valueOf(userModel.size()));

        productAdapter = new ProductAdapter(getContext(), orderDetailEntity.getBestSellingProducts(orderDetailList, productList));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerBestSellingProducts.setLayoutManager(layoutManager);
        recyclerBestSellingProducts.setAdapter(productAdapter);

        productsLowStockAdapter = new ProductAdapter(getContext(), productEntity.getProductsLowStock());
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerLowStockAlert.setLayoutManager(layoutManager2);
        recyclerLowStockAlert.setAdapter(productsLowStockAdapter);
    }
}
