package com.example.basicmobileprogramingproject.Activity.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.basicmobileprogramingproject.Adapter.VoucherAdapter;
import com.example.basicmobileprogramingproject.Adapter.VoucherAdapter;
import com.example.basicmobileprogramingproject.Entity.VoucherEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;

import com.example.basicmobileprogramingproject.Model.VoucherModel;
import com.example.basicmobileprogramingproject.Model.VoucherModel;
import com.example.basicmobileprogramingproject.Model.VoucherModel;

import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class VoucherManagementFragment extends Fragment {
    TextView badgeNumberOfVouchersDeleted, txtVoucherName, txtVoucherPrice, txtMinimumPrice, txtVoucherQuantity, txtStartDate, txtEndDate, txtDeletedVoucherName;
    EditText edtSearch, edtVoucherName, edtQuantity, edtPrice, edtStartDate, edtEndDate, edtMinimumPrice;
    ImageButton btnTurnOnBlockAddVoucher, btnSearch, btnTurnOnBlockDeleteVoucher;
    ImageView btnClear, imgVoucherImage, btnExit, btnClose, btnCancel;
    Button btnAddNewVoucher, btnFixVoucher, btnEditVoucher, btnDeleteVoucher, btnTurnOnBlockEditVoucher, btnRestoreVoucher, btnDeleteActualVoucher;
    LinearLayout linearLayoutAddAndEditVoucher, linearLayoutVoucherDetail, linearLayoutDeleteAndRestoreVoucher, linearLayoutVoucherHasBeenDeleted, linearLayoutDeletedVoucherDetail;
    RecyclerView recyclerViewVoucher, recyclerViewVoucherHasBeenDeleted;
    VoucherAdapter voucherAdapter, voucherListingHasBeenDeletedAdapter;
    VoucherEntity voucherEntity, voucherListingHasBeenDeletedEntity;
    ArrayList<VoucherModel> voucherList, voucherListingHasBeenDeleted;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedVoucherId;
    static final int PICK_IMAGE = 1;
    Uri imageUri;
    String imagePath;
    String imageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.voucher_fragment, container, false);
        // mapping id
        clickedVoucherId = 0;
        edtVoucherName = view.findViewById(R.id.edtVoucherName);
        edtQuantity = view.findViewById(R.id.edtQuantity);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtMinimumPrice = view.findViewById(R.id.edtMinimumPrice);
        edtStartDate = view.findViewById(R.id.edtStartDay);
        edtEndDate = view.findViewById(R.id.edtEndDate);

        txtVoucherName = view.findViewById(R.id.txtVoucherName);
        txtVoucherPrice = view.findViewById(R.id.txtPrice);
        txtMinimumPrice = view.findViewById(R.id.txtMinimumPrice);
        txtVoucherQuantity = view.findViewById(R.id.txtQuantity);
        txtStartDate = view.findViewById(R.id.txtStartDay);
        txtEndDate = view.findViewById(R.id.txtEndDate);

        txtDeletedVoucherName = view.findViewById(R.id.txtDeletedVoucherName);
        btnTurnOnBlockDeleteVoucher = view.findViewById(R.id.btnTurnOnBlockDeleteVoucher);
        btnRestoreVoucher = view.findViewById(R.id.btnRestoreVoucher);
        btnDeleteActualVoucher = view.findViewById(R.id.btnDeleteActualVoucher);

        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        handleSearch();

        btnExit = view.findViewById(R.id.btnExit);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        linearLayoutAddAndEditVoucher = view.findViewById(R.id.linearLayoutAddAndEditVoucher);
        linearLayoutVoucherDetail = view.findViewById(R.id.linearLayoutVoucherDetail);
        linearLayoutDeleteAndRestoreVoucher = view.findViewById(R.id.linearLayoutDeleteAndRestoreVoucher);
        linearLayoutDeletedVoucherDetail = view.findViewById(R.id.linearLayoutDeletedVoucherDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());

        // get Voucher list
        voucherEntity = new VoucherEntity(getContext());
        voucherList = voucherEntity.getVoucherList();
        voucherListingHasBeenDeleted = voucherEntity.getVoucherListingHasBeenDeleted();

        // call adapter and event click item
        voucherAdapter = new VoucherAdapter(getContext(), voucherList);
        voucherListingHasBeenDeletedAdapter = new VoucherAdapter(getContext(), voucherListingHasBeenDeleted);
        handleSelectVoucher();

//       assign data up recyclerView
        recyclerViewVoucher = view.findViewById(R.id.recyclerViewVoucher);
        recyclerViewVoucherHasBeenDeleted = view.findViewById(R.id.recyclerViewVoucherHasBeenDeleted);
        uploadDataToRecyclerViewVoucher();

        uploadNumberOfVouchersDeleted();

        btnDeleteVoucher = view.findViewById(R.id.btnDeleteVoucher);
        btnTurnOnBlockEditVoucher = view.findViewById(R.id.btnTurnOnBlockEditVoucher);
        btnAddNewVoucher = view.findViewById(R.id.btnAddNewVoucher);
        btnEditVoucher = view.findViewById(R.id.btnFixVoucher);
        btnFixVoucher = view.findViewById(R.id.btnFixVoucher);
        btnTurnOnBlockAddVoucher = view.findViewById(R.id.btnTurnOnBlockAddVoucher);
        handleEventsClickButton();

        defaultLayout();
        return view;
    }

    public void defaultLayout() {
        linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
        linearLayoutVoucherDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreVoucher.setVisibility(View.GONE);
        linearLayoutDeletedVoucherDetail.setVisibility(View.GONE);
    }

    public VoucherModel assignData() {
        VoucherModel voucherModel = new VoucherModel();
        voucherModel.VoucherId = clickedVoucherId;
        voucherModel.VoucherName = edtVoucherName.getText().toString();
        voucherModel.Quantity = Integer.parseInt(edtQuantity.getText().toString());
        voucherModel.Price = Double.parseDouble(edtPrice.getText().toString());
        voucherModel.MinimumPrice = Double.parseDouble(edtMinimumPrice.getText().toString());
        voucherModel.StartDay = edtStartDate.getText().toString();
        voucherModel.EndDate = edtEndDate.getText().toString();
        voucherModel.Deleted = false;
        return voucherModel;
    }

    public void resetData() {
        edtVoucherName.setText("");
        edtQuantity.setText("");
        edtPrice.setText("");
    }

    public void uploadDataToRecyclerViewVoucher() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVoucher.setLayoutManager(layoutManager);
        recyclerViewVoucher.setAdapter(voucherAdapter);

        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewVoucherHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewVoucherHasBeenDeleted.setAdapter(voucherListingHasBeenDeletedAdapter);
    }

    public void handleSelectVoucher() {
        voucherAdapter.setOnItemClickListener(position -> {
            VoucherModel clickedVoucher = voucherList.get(position);
            linearLayoutVoucherDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditVoucher.setVisibility(View.GONE);

            clickedVoucherId = clickedVoucher.VoucherId;
            txtVoucherName.setText(clickedVoucher.VoucherName);
            txtVoucherPrice.setText("Price: $" + String.valueOf(clickedVoucher.Price));
            txtMinimumPrice.setText("Minium price: $" + String.valueOf(clickedVoucher.MinimumPrice));
            txtVoucherQuantity.setText("Quantity: " + String.valueOf(clickedVoucher.Quantity));
            txtStartDate.setText("Start Day: " + clickedVoucher.StartDay);
            txtEndDate.setText("End Date: " + clickedVoucher.EndDate);

            edtVoucherName.setText(clickedVoucher.VoucherName);
            edtQuantity.setText(String.valueOf(clickedVoucher.Quantity));
            edtPrice.setText(String.valueOf(clickedVoucher.Price));
            edtMinimumPrice.setText(String.valueOf(clickedVoucher.MinimumPrice));
            edtStartDate.setText(clickedVoucher.StartDay);
            edtEndDate.setText(clickedVoucher.EndDate);
        });
        voucherListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            VoucherModel clickedVoucher = voucherListingHasBeenDeleted.get(position);
            linearLayoutDeletedVoucherDetail.setVisibility(View.VISIBLE);
            linearLayoutVoucherDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditVoucher.setVisibility(View.GONE);

//            AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedVoucher.VoucherId + " " + clickedVoucher.VoucherName);

            clickedVoucherId = clickedVoucher.VoucherId;
            txtDeletedVoucherName.setText(clickedVoucher.VoucherName);

            edtVoucherName.setText(clickedVoucher.VoucherName);
            edtQuantity.setText(String.valueOf(clickedVoucher.Quantity));
            edtPrice.setText(String.valueOf(clickedVoucher.Price));
            edtMinimumPrice.setText(String.valueOf(clickedVoucher.MinimumPrice));
            edtStartDate.setText(clickedVoucher.StartDay);
            edtEndDate.setText(clickedVoucher.EndDate);
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void uploadNumberOfVouchersDeleted() {
        badgeNumberOfVouchersDeleted = view.findViewById(R.id.badgeNumberOfVouchersDeleted);
        badgeNumberOfVouchersDeleted.setText(String.valueOf(voucherEntity.getNumberOfVouchersDeleted()));
    }

    public void handleEventsClickButton() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutVoucherDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreVoucher.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditVoucher.setVisibility(View.VISIBLE);
                btnAddNewVoucher.setVisibility(View.VISIBLE);
                linearLayoutVoucherDetail.setVisibility(View.GONE);
                btnFixVoucher.setVisibility(View.GONE);
            }
        });
        btnAddNewVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherModel newVoucher = assignData();
                boolean isCheck = voucherEntity.insertVoucher(newVoucher);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add voucher success");
                    reloadData();
                    linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add voucher fail");
                }
            }
        });
        btnEditVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherModel newVoucher = assignData();
                boolean isCheck = voucherEntity.updateVoucher(newVoucher);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update Voucher success");
                    linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update Voucher fail");
                }
            }
        });

        btnTurnOnBlockEditVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditVoucher.setVisibility(View.VISIBLE);
                btnFixVoucher.setVisibility(View.VISIBLE);
                btnAddNewVoucher.setVisibility(View.GONE);
                linearLayoutVoucherDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherModel newVoucher = assignData();
                newVoucher.Deleted = true;
                boolean isCheck = voucherEntity.updateVoucher(newVoucher);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete Voucher success");
                    uploadNumberOfVouchersDeleted();
                    reloadData();
                    recyclerViewVoucher.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
                    linearLayoutVoucherDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete Voucher fail");
                }
            }
        });
        btnTurnOnBlockDeleteVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreVoucher.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
                linearLayoutVoucherDetail.setVisibility(View.GONE);
                recyclerViewVoucher.setVisibility(View.GONE);
            }
        });
        btnRestoreVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherModel newVoucher = assignData();
                newVoucher.Deleted = false;
                boolean isCheck = voucherEntity.updateVoucher(newVoucher);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore voucher success");
                    uploadNumberOfVouchersDeleted();
                    recyclerViewVoucher.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreVoucher.setVisibility(View.GONE);
                    linearLayoutDeletedVoucherDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore voucher fail");
                }
            }
        });
        btnDeleteActualVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoucherModel newVoucher = assignData();
                boolean isCheck = voucherEntity.deleteVoucher(newVoucher);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual voucher success");
                    uploadNumberOfVouchersDeleted();
                    reloadData();
                    recyclerViewVoucher.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditVoucher.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual voucher fail");
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
                ArrayList<VoucherModel> searchedVouchersList = voucherEntity.getSearchedVouchersList(searchKeyword);
                voucherAdapter.updateVoucherList(searchedVouchersList);
                voucherAdapter.notifyDataSetChanged();
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
        voucherList = voucherEntity.getVoucherList();
        voucherAdapter = new VoucherAdapter(getContext(), voucherList);
        uploadDataToRecyclerViewVoucher();
    }
}