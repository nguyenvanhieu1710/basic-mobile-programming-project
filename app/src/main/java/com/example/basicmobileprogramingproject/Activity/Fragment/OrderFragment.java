package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.OrderAdapter;
import com.example.basicmobileprogramingproject.Adapter.PayAdapter;
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
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class OrderFragment extends Fragment {
    View view;
    TextView tvOrderId;
    TextView tvUserId;
    TextView tvOrderStatus;
    TextView tvTotalAmount;
    TextView tvDayBuy;
    RecyclerView recyclerViewProductOfOrderDetail;
    LinearLayout linearLayoutOrderDetail;
    EditText edtSearch;
    ImageView btnClear, btnExit;
    ImageButton btnSearch;
    RecyclerView recyclerViewOrder;
    AccountEntity accountEntity;
    UserEntity userEntity;
    OrderAdapter orderAdapter;
    OrderEntity orderEntity;
    OrderDetailEntity orderDetailEntity;
    ProductEntity productEntity;
    ProductAdapter productAdapter;
    ArrayList<OrderModel> orderList;
    String role;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_fragment, container, false);
        // mapping id
        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        recyclerViewOrder = view.findViewById(R.id.recyclerViewOrder);

        linearLayoutOrderDetail = view.findViewById(R.id.linearLayoutOrderDetail);
        tvOrderId = view.findViewById(R.id.tvOrderId);
        tvUserId = view.findViewById(R.id.tvUserId);
        tvOrderStatus = view.findViewById(R.id.tvOrderStatus);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        tvDayBuy = view.findViewById(R.id.tvDayBuy);
        recyclerViewProductOfOrderDetail = view.findViewById(R.id.rvOrderDetails);
        btnExit = view.findViewById(R.id.btnExit);

        userEntity = new UserEntity(getContext());
        orderEntity = new OrderEntity(getContext());
        orderDetailEntity = new OrderDetailEntity(getContext());
        productEntity = new ProductEntity(getContext());

        accountEntity = new AccountEntity(getContext());
        AccountModel onlineAccount = accountEntity.getOnlineAccount();
        if (onlineAccount.Role.equals("User")) {
            orderList = orderEntity.getOrderForCustomer();
            orderAdapter = new OrderAdapter(getContext(), orderList);
        } else if (onlineAccount.Role.equals("Staff")) {
            orderList = orderEntity.getOrderForStaff();
            orderAdapter = new OrderAdapter(getContext(), orderList);
        }
        else {
            // if admin
            orderList = orderEntity.getOrderList();
            orderAdapter = new OrderAdapter(getContext(), orderList);
        }
        role = onlineAccount.Role;
        orderAdapter.getRole(onlineAccount.Role);

        linearLayoutOrderDetail.setVisibility(View.GONE);
        uploadDataToRecyclerView();
        handleSelectItem();
        handleSearch();
        handleEventsClickButton();

        return view;
    }

    public void uploadDataToRecyclerView() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewOrder.setLayoutManager(layoutManager);
        recyclerViewOrder.setAdapter(orderAdapter);
    }

    public void handleSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKeyword = edtSearch.getText().toString().trim();
                if (searchKeyword.isEmpty()) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Please enter search keyword");
                    return;
                }
                ArrayList<UserModel> userList = userEntity.getListOfCustomersWhoHavePurchasedTheProduct();
                ArrayList<UserModel> searchedUserList = new ArrayList<>();
                for (UserModel user : userList) {
                    if (user.Name.toLowerCase().contains(searchKeyword.toLowerCase())) {
                        searchedUserList.add(user);
                    }
                }
                ArrayList<OrderModel> orderListSearched = orderEntity.getListOfOrdersByUserID(searchedUserList);
                orderAdapter = new OrderAdapter(getContext(), orderListSearched);
                orderAdapter.getRole(role);
                recyclerViewOrder.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
    }

    public void handleEventsClickButton() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutOrderDetail.setVisibility(View.GONE);
            }
        });
    }

    public void handleSelectItem() {
        orderAdapter.setOnItemClickListener(position -> {
            OrderModel clickedOrder = orderList.get(position);
            linearLayoutOrderDetail.setVisibility(View.VISIBLE);

            tvOrderId.setText("Order ID: " + clickedOrder.OrderId);
            tvUserId.setText("User ID: " + clickedOrder.UserId);
            tvOrderStatus.setText("Order Status: " + clickedOrder.OrderStatus);
            tvTotalAmount.setText("Total Amount: $" + clickedOrder.TotalAmount);
            tvDayBuy.setText("Date of Purchase: " + clickedOrder.DayBuy);

            ArrayList<OrderDetailModel> orderDetailList = orderDetailEntity.getOrderDetailByOrderId(clickedOrder.OrderId);
            ArrayList<ProductModel> productList = new ArrayList<>();
            for (OrderDetailModel orderDetail : orderDetailList) {
                ProductModel product = productEntity.getProductById(orderDetail.ProductId);
                productList.add(product);
            }

            productAdapter = new ProductAdapter(getContext(), productList);
            recyclerViewProductOfOrderDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerViewProductOfOrderDetail.setAdapter(productAdapter);
        });
    }
}
