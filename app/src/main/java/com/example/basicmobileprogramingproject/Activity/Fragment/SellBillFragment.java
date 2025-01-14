package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.basicmobileprogramingproject.Adapter.SellBillAdapter;
import com.example.basicmobileprogramingproject.Entity.OrderEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;

import com.example.basicmobileprogramingproject.Model.OrderModel;

import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class SellBillFragment extends Fragment {
    TextView txtDeletedSellBillId, badgeNumberOfSellBillsDeleted, txtSellBillId, tvTotalAmount, textDayBuy, textUserId, textStaffId, textTotalAmount, textOrderStatus, textDeliveryAddress;
    EditText edtSearch, edtUserId, edtStaffId, edtOrderStatus, edtDayBuy, edtDeliveryAddress, edtProductId, edtPrice, edtQuantity, edtDiscountAmount, edtVoucherId;
    ImageButton btnTurnOnBlockAddSellBill, btnSearch, btnTurnOnBlockDeleteSellBill;
    ImageView btnClear, imgSellBillImage, btnExitLinearLayoutSellBillDetail, btnClose, btnCancel;
    Button btnAddNewSellBill, btnFixSellBill, btnEditSellBill, btnDeleteSellBill, btnTurnOnBlockEditSellBill, btnRestoreSellBill, btnDeleteActualSellBill;
    LinearLayout linearLayoutAddAndEditSellBill, linearLayoutSellBillDetail, linearLayoutDeleteAndRestoreSellBill, linearLayoutSellBillHasBeenDeleted, linearLayoutDeletedSellBillDetail;
    RecyclerView recyclerViewSellBill, recyclerViewSellBillHasBeenDeleted;
    SellBillAdapter sellBillAdapter, sellBillListingHasBeenDeletedAdapter;
    OrderEntity sellBillEntity, sellBillListingHasBeenDeletedEntity;
    ArrayList<OrderModel> sellBillList, sellBillListingHasBeenDeleted;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedSellBillId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_sell_bill, container, false);
        // mapping id
        clickedSellBillId = 0;
        edtUserId = view.findViewById(R.id.edtUserId);
        edtStaffId = view.findViewById(R.id.edtStaffId);
        edtDayBuy = view.findViewById(R.id.edtDayBuy);
        edtDeliveryAddress = view.findViewById(R.id.edtDeliveryAddress);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        edtProductId = view.findViewById(R.id.edtProductId);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtQuantity = view.findViewById(R.id.edtQuantity);
        edtDiscountAmount = view.findViewById(R.id.edtDiscountAmount);
        edtVoucherId = view.findViewById(R.id.edtVoucherId);

        txtSellBillId = view.findViewById(R.id.txtSellBillId);
        textDayBuy = view.findViewById(R.id.textDayBuy);
        textUserId = view.findViewById(R.id.textUserId);
        textOrderStatus = view.findViewById(R.id.textOrderStatus);
        textDeliveryAddress = view.findViewById(R.id.textDeliveryAddress);
        textStaffId = view.findViewById(R.id.textStaffId);
        textTotalAmount = view.findViewById(R.id.textTotalAmount);

        txtDeletedSellBillId = view.findViewById(R.id.txtDeletedSellBillId);
        btnTurnOnBlockDeleteSellBill = view.findViewById(R.id.btnTurnOnBlockDeleteSellBill);
        btnRestoreSellBill = view.findViewById(R.id.btnRestoreSellBill);
        btnDeleteActualSellBill = view.findViewById(R.id.btnDeleteActualSellBill);

        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        handleSearch();

        btnExitLinearLayoutSellBillDetail = view.findViewById(R.id.btnExitLinearLayoutSellBillDetail);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        linearLayoutAddAndEditSellBill = view.findViewById(R.id.linearLayoutAddAndEditSellBill);
        linearLayoutSellBillDetail = view.findViewById(R.id.linearLayoutSellBillDetail);
        linearLayoutDeleteAndRestoreSellBill = view.findViewById(R.id.linearLayoutDeleteAndRestoreSellBill);
        linearLayoutDeletedSellBillDetail = view.findViewById(R.id.linearLayoutDeletedSellBillDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());

        // get SellBill list
        sellBillEntity = new OrderEntity(getContext());
        sellBillList = sellBillEntity.getOrderList();
        sellBillListingHasBeenDeleted = sellBillEntity.getOrderList();

        // call adapter and event click item
        sellBillAdapter = new SellBillAdapter(getContext(), sellBillList);
        sellBillListingHasBeenDeletedAdapter = new SellBillAdapter(getContext(), sellBillListingHasBeenDeleted);
        handleSelectSellBill();

//       assign data up recyclerView
        recyclerViewSellBill = view.findViewById(R.id.recyclerViewSellBill);
        recyclerViewSellBillHasBeenDeleted = view.findViewById(R.id.recyclerViewSellBillHasBeenDeleted);
        uploadDataToRecyclerViewSellBill();

        uploadNumberOfSellBillsDeleted();

        btnDeleteSellBill = view.findViewById(R.id.btnDeleteSellBill);
        btnTurnOnBlockEditSellBill = view.findViewById(R.id.btnTurnOnBlockEditSellBill);
        btnAddNewSellBill = view.findViewById(R.id.btnAddNewSellBill);
        btnEditSellBill = view.findViewById(R.id.btnFixSellBill);
        btnFixSellBill = view.findViewById(R.id.btnFixSellBill);
        btnTurnOnBlockAddSellBill = view.findViewById(R.id.btnTurnOnBlockAddSellBill);
        handleEventsClickButton();

        defaultLayout();
        return view;
    }

    public void defaultLayout() {
        linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
        linearLayoutSellBillDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreSellBill.setVisibility(View.GONE);
        linearLayoutDeletedSellBillDetail.setVisibility(View.GONE);
    }

    public OrderModel assignData() {
        OrderModel sellBillModel = new OrderModel();
        sellBillModel.OrderId = clickedSellBillId;
        sellBillModel.UserId = Integer.parseInt(edtUserId.getText().toString().trim());
        sellBillModel.StaffId = Integer.parseInt(edtStaffId.getText().toString().trim());
        sellBillModel.OrderStatus = edtOrderStatus.getText().toString().trim();
        sellBillModel.DayBuy = edtDayBuy.getText().toString().trim();
        sellBillModel.TotalAmount = Double.parseDouble(tvTotalAmount.getText().toString().trim());
        return sellBillModel;
    }

    public void uploadDataToRecyclerViewSellBill() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager _staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewSellBill.setLayoutManager(_staggeredGridLayoutManager);
        recyclerViewSellBill.setAdapter(sellBillAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewSellBillHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewSellBillHasBeenDeleted.setAdapter(sellBillListingHasBeenDeletedAdapter);
    }

    public void handleSelectSellBill() {
        sellBillAdapter.setOnItemClickListener(position -> {
            OrderModel clickedSellBill = sellBillList.get(position);
            linearLayoutSellBillDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditSellBill.setVisibility(View.GONE);

            clickedSellBillId = clickedSellBill.OrderId;
            txtSellBillId.setText(String.valueOf(clickedSellBill.OrderId));
            textDayBuy.setText(clickedSellBill.DayBuy);
            textUserId.setText(clickedSellBill.UserId);
            textOrderStatus.setText(clickedSellBill.OrderStatus);
            textDeliveryAddress.setText(clickedSellBill.DeliveryAddress);
            textStaffId.setText(clickedSellBill.StaffId);
            textTotalAmount.setText(String.valueOf(clickedSellBill.TotalAmount));

            edtUserId.setText(clickedSellBill.UserId);
            edtStaffId.setText(clickedSellBill.StaffId);
            edtOrderStatus.setText(clickedSellBill.OrderStatus);
            edtDayBuy.setText(clickedSellBill.DayBuy);
            edtDeliveryAddress.setText(clickedSellBill.DeliveryAddress);
            tvTotalAmount.setText(String.valueOf(clickedSellBill.TotalAmount));
        });
        sellBillListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            OrderModel clickedSellBill = sellBillListingHasBeenDeleted.get(position);
            linearLayoutDeletedSellBillDetail.setVisibility(View.VISIBLE);
            linearLayoutSellBillDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditSellBill.setVisibility(View.GONE);

//            AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedSellBill.SellBillId + " " + clickedSellBill.SellBillName);

            clickedSellBillId = clickedSellBill.OrderId;
            txtDeletedSellBillId.setText(String.valueOf(clickedSellBill.OrderId));

            edtUserId.setText(clickedSellBill.UserId);
            edtStaffId.setText(clickedSellBill.StaffId);
            edtOrderStatus.setText(clickedSellBill.OrderStatus);
            edtDayBuy.setText(clickedSellBill.DayBuy);
            edtDeliveryAddress.setText(clickedSellBill.DeliveryAddress);
            tvTotalAmount.setText(String.valueOf(clickedSellBill.TotalAmount));
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void uploadNumberOfSellBillsDeleted() {
        badgeNumberOfSellBillsDeleted = view.findViewById(R.id.badgeNumberOfSellBillsDeleted);
        badgeNumberOfSellBillsDeleted.setText(String.valueOf(sellBillEntity.getOrderList()));
    }

    public void handleEventsClickButton() {
        btnExitLinearLayoutSellBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSellBillDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreSellBill.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditSellBill.setVisibility(View.VISIBLE);
                btnAddNewSellBill.setVisibility(View.VISIBLE);
                linearLayoutSellBillDetail.setVisibility(View.GONE);
                btnFixSellBill.setVisibility(View.GONE);
            }
        });
        btnAddNewSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel newSellBill = assignData();
                boolean isCheck = sellBillEntity.insertOrder(newSellBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add sellBill success");
                    reloadData();
                    linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add sellBill fail");
                }
            }
        });
        btnEditSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel newSellBill = assignData();
                boolean isCheck = sellBillEntity.updateOrder(newSellBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update SellBill success");
                    reloadData();
                    recyclerViewSellBill.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update SellBill fail");
                }
            }
        });

        btnTurnOnBlockEditSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditSellBill.setVisibility(View.VISIBLE);
                btnFixSellBill.setVisibility(View.VISIBLE);
                btnAddNewSellBill.setVisibility(View.GONE);
                linearLayoutSellBillDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel newSellBill = assignData();
                newSellBill.Deleted = true;
                boolean isCheck = sellBillEntity.updateOrder(newSellBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete SellBill success");
                    uploadNumberOfSellBillsDeleted();
                    reloadData();
                    recyclerViewSellBill.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
                    linearLayoutSellBillDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete SellBill fail");
                }
            }
        });
        btnTurnOnBlockDeleteSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreSellBill.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
                linearLayoutSellBillDetail.setVisibility(View.GONE);
                recyclerViewSellBill.setVisibility(View.GONE);
            }
        });
        btnRestoreSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel newSellBill = assignData();
                newSellBill.Deleted = false;
                boolean isCheck = sellBillEntity.updateOrder(newSellBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore sellBill success");
                    uploadNumberOfSellBillsDeleted();
                    recyclerViewSellBill.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreSellBill.setVisibility(View.GONE);
                    linearLayoutDeletedSellBillDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore sellBill fail");
                }
            }
        });
        btnDeleteActualSellBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderModel newSellBill = assignData();
                boolean isCheck = sellBillEntity.deleteOrder(newSellBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual sellBill success");
                    uploadNumberOfSellBillsDeleted();
                    reloadData();
                    recyclerViewSellBill.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditSellBill.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual sellBill fail");
                }
            }
        });
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
                ArrayList<OrderModel> searchedSellBillsList = sellBillEntity.getSearchedOrderList(searchKeyword);
                sellBillAdapter.updateSellBillList(searchedSellBillsList);
                sellBillAdapter.notifyDataSetChanged();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                reloadData();
            }
        });
    }

    public void reloadData() {
        sellBillList = sellBillEntity.getOrderList();
        sellBillAdapter = new SellBillAdapter(getContext(), sellBillList);
        uploadDataToRecyclerViewSellBill();
    }
}