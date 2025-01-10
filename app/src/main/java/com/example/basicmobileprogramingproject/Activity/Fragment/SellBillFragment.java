package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.OrderAdapter;
import com.example.basicmobileprogramingproject.Entity.OrderEntity;
import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SellBillFragment extends Fragment {
    View view;
    private EditText edtSearch;
    private ImageView btnClear;
    private ImageButton btnSearch;
    private RecyclerView rvOrders;
    private FloatingActionButton fabAddSellBill;
    private OrderAdapter orderAdapter;
    private ArrayList<OrderModel> orderList;
    private OrderEntity orderEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_sell_bill, container, false);
        // mapping id
        rvOrders = view.findViewById(R.id.rvOrders);
        fabAddSellBill = view.findViewById(R.id.fabAddSellBill);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        handleSearch();

        orderList = orderEntity.getOrderList();
        orderAdapter = new OrderAdapter(getContext(), orderList);
        uploadDataToRecyclerViewSellBill();

        return view;
    }

    public void uploadDataToRecyclerViewSellBill() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(orderAdapter);
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
                ArrayList<OrderModel> searchedSellBillList = orderEntity.getSearchedOrderList(searchKeyword);
                orderAdapter.updateOrderList(searchedSellBillList);
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
}
