package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Adapter.CustomChartView;
import com.example.basicmobileprogramingproject.Adapter.CustomChartView.ChartType;
import com.example.basicmobileprogramingproject.Adapter.ProductAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.OrderDetailEntity;
import com.example.basicmobileprogramingproject.Entity.OrderEntity;
import com.example.basicmobileprogramingproject.Entity.ProductEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
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
    Button btnExportReport;
    AccountEntity accountEntity;
    UserEntity userEntity;
    ProductEntity productEntity;
    OrderEntity orderEntity;
    OrderDetailEntity orderDetailEntity;
    ArrayList<OrderModel> orderList;
    ArrayList<OrderDetailModel> orderDetailList;
    ArrayList<ProductModel> productList;
    ArrayList<UserModel> userList;
    ArrayList<AccountModel> accountList;
    CustomChartView chartTotalRevenue, chartTotalOrders, chartBestSelling, chartLowStock, chartTotalUsers;

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

        btnExportReport = view.findViewById(R.id.btnExportReport);

        chartTotalRevenue = view.findViewById(R.id.chartTotalRevenue);
        chartTotalOrders = view.findViewById(R.id.chartTotalOrders);
        chartBestSelling = view.findViewById(R.id.chartBestSelling);
        chartLowStock = view.findViewById(R.id.chartLowStock);
        chartTotalUsers = view.findViewById(R.id.chartTotalUsers);

        accountEntity = new AccountEntity(getContext());
        accountList = accountEntity.getAccountList();
        userEntity = new UserEntity(getContext());
        userList = userEntity.getUserList();
        orderEntity = new OrderEntity(getContext());
        orderList = orderEntity.getOrderList();
        orderDetailEntity = new OrderDetailEntity(getContext());
        orderDetailList = orderDetailEntity.getOrderDetailList();
        productEntity = new ProductEntity(getContext());
        productList = productEntity.getProductList();

        handleEventsClickButton();
        setupChartData();
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
                double totalRevenue = 0;
                for (OrderDetailModel orderDetail : orderDetailList) {
                    totalRevenue += orderDetail.Price * orderDetail.Quantity;
                }
                int totalOrders = orderList.size();
                int totalUsers = userList.size();
                int totalProducts = productList.size();
                canvas.drawText("Total Revenue: " + String.format("%.2f", totalRevenue) + " USD", 20, yPosition, paint);
                yPosition += 20;
                canvas.drawText("Total Orders: " + totalOrders, 20, yPosition, paint);
                yPosition += 20;
                canvas.drawText("Total Users: " + totalUsers, 20, yPosition, paint);
                yPosition += 20;
                canvas.drawText("Total Products: " + totalProducts, 20, yPosition, paint);
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

                // Lưu tệp PDF vào thư mục Downloads
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "report.pdf");
                try {
                    pdfDocument.writeTo(new FileOutputStream(file));

                    // Hiển thị thông báo thành công
                    Toast.makeText(getContext(), "PDF created successfully!", Toast.LENGTH_SHORT).show();

                    // Hiển thị đường dẫn tệp đã lưu
                    Toast.makeText(getContext(), "File saved at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();

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

    public void setupChartData() {
        // Biểu đồ Doanh thu theo tháng (Bar Chart)
        List<Float> revenueValues = new ArrayList<>();
        List<String> revenueLabels = new ArrayList<>();
        List<Integer> revenueColors = new ArrayList<>();
        revenueValues.add(1500f);  // Tháng 1
        revenueValues.add(2000f);  // Tháng 2
        revenueValues.add(2200f);  // Tháng 3
        revenueValues.add(1700f);  // Tháng 4
        revenueValues.add(2500f);  // Tháng 5
        revenueValues.add(1900f);  // Tháng 6
        revenueLabels.add("Jan");
        revenueLabels.add("Feb");
        revenueLabels.add("Mar");
        revenueLabels.add("Apr");
        revenueLabels.add("May");
        revenueLabels.add("Jun");
        revenueColors.add(Color.parseColor("#FF6F61"));
        revenueColors.add(Color.parseColor("#4CAF50"));
        revenueColors.add(Color.parseColor("#FFEB3B"));
        revenueColors.add(Color.parseColor("#03A9F4"));
        revenueColors.add(Color.parseColor("#9C27B0"));
        revenueColors.add(Color.parseColor("#FF9800"));
        chartTotalRevenue.setData(revenueValues, revenueLabels, revenueColors, CustomChartView.ChartType.BAR_CHART);

        // Biểu đồ Đơn hàng theo ngày (Line Chart)
        List<Float> orderValues = new ArrayList<>();
        List<String> orderLabels = new ArrayList<>();
        List<Integer> orderColors = new ArrayList<>();
        orderValues.add(50f);  // Ngày 1
        orderValues.add(60f);  // Ngày 2
        orderValues.add(40f);  // Ngày 3
        orderValues.add(30f);  // Ngày 4
        orderValues.add(90f);  // Ngày 5
        orderValues.add(70f);  // Ngày 6
        orderValues.add(80f);  // Ngày 7
        orderLabels.add("1");
        orderLabels.add("2");
        orderLabels.add("3");
        orderLabels.add("4");
        orderLabels.add("5");
        orderLabels.add("6");
        orderLabels.add("7");
        orderColors.add(Color.parseColor("#FF9800"));
        orderColors.add(Color.parseColor("#2196F3"));
        orderColors.add(Color.parseColor("#4CAF50"));
        orderColors.add(Color.parseColor("#9C27B0"));
        orderColors.add(Color.parseColor("#FFEB3B"));
        orderColors.add(Color.parseColor("#03A9F4"));
        orderColors.add(Color.parseColor("#FF6F61"));
        chartTotalOrders.setData(orderValues, orderLabels, orderColors, CustomChartView.ChartType.LINE_CHART);

        // Biểu đồ Top 5 sản phẩm bán chạy (Pie Chart)
        List<ProductModel> bestSellingProducts = orderDetailEntity.getBestSellingProducts(orderDetailList, productList);
        List<Float> bestSellingValues = new ArrayList<>();
        List<String> bestSellingLabels = new ArrayList<>();
        List<Integer> bestSellingColors = new ArrayList<>();
        bestSellingValues.add(45f);  // Sản phẩm A
        bestSellingValues.add(35f);  // Sản phẩm B
        bestSellingValues.add(10f);  // Sản phẩm C
        bestSellingValues.add(5f);   // Sản phẩm D
        bestSellingValues.add(5f);   // Sản phẩm E
        bestSellingLabels.add("Product A");
        bestSellingLabels.add("Product B");
        bestSellingLabels.add("Product C");
        bestSellingLabels.add("Product D");
        bestSellingLabels.add("Product E");
        bestSellingColors.add(Color.parseColor("#FF6F61"));
        bestSellingColors.add(Color.parseColor("#4CAF50"));
        bestSellingColors.add(Color.parseColor("#FFEB3B"));
        bestSellingColors.add(Color.parseColor("#03A9F4"));
        bestSellingColors.add(Color.parseColor("#9C27B0"));
        chartBestSelling.setData(bestSellingValues, bestSellingLabels, bestSellingColors, CustomChartView.ChartType.PIE_CHART);

        // Biểu đồ Top 5 sản phẩm sắp hết (Pie Chart)
        List<Float> lowStockValues = new ArrayList<>();
        List<String> lowStockLabels = new ArrayList<>();
        List<Integer> lowStockColors = new ArrayList<>();
        lowStockValues.add(15f);  // Sản phẩm X
        lowStockValues.add(20f);  // Sản phẩm Y
        lowStockValues.add(25f);  // Sản phẩm Z
        lowStockValues.add(30f);  // Sản phẩm W
        lowStockValues.add(10f);  // Sản phẩm V
        lowStockLabels.add("Product X");
        lowStockLabels.add("Product Y");
        lowStockLabels.add("Product Z");
        lowStockLabels.add("Product W");
        lowStockLabels.add("Product V");
        lowStockColors.add(Color.parseColor("#FF9800"));
        lowStockColors.add(Color.parseColor("#FFEB3B"));
        lowStockColors.add(Color.parseColor("#03A9F4"));
        lowStockColors.add(Color.parseColor("#4CAF50"));
        lowStockColors.add(Color.parseColor("#9C27B0"));
        chartLowStock.setData(lowStockValues, lowStockLabels, lowStockColors, CustomChartView.ChartType.PIE_CHART);

        // Biểu đồ Người dùng mới theo tháng (Bar Chart)
        List<AccountModel> accountModelList = accountList;
        List<UserModel> userModelList = userList;

        List<Float> userValues = new ArrayList<>();
        List<String> userLabels = new ArrayList<>();
        List<Integer> userColors = new ArrayList<>();
        userValues.add(120f);  // Tháng 1
        userValues.add(150f);  // Tháng 2
        userValues.add(180f);  // Tháng 3
        userValues.add(200f);  // Tháng 4
        userValues.add(230f);  // Tháng 5
        userValues.add(250f);  // Tháng 6
        userLabels.add("Jan");
        userLabels.add("Feb");
        userLabels.add("Mar");
        userLabels.add("Apr");
        userLabels.add("May");
        userLabels.add("Jun");
        userColors.add(Color.parseColor("#2196F3"));
        userColors.add(Color.parseColor("#4CAF50"));
        userColors.add(Color.parseColor("#FFEB3B"));
        userColors.add(Color.parseColor("#FF9800"));
        userColors.add(Color.parseColor("#FF6F61"));
        userColors.add(Color.parseColor("#9C27B0"));
        chartTotalUsers.setData(userValues, userLabels, userColors, CustomChartView.ChartType.BAR_CHART);
    }
}
