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


import com.example.basicmobileprogramingproject.Adapter.ImportBillAdapter;
import com.example.basicmobileprogramingproject.Entity.ImportBillEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;

import com.example.basicmobileprogramingproject.Model.ImportBillModel;

import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class ImportBillFragment extends Fragment {
    TextView txtDeletedImportBillId, badgeNumberOfImportBillsDeleted, txtImportBillId, tvTotalAmount, textInputDay, textSupplierId, textStaffId, textTotalAmount;
    EditText edtSearch, edtSupplierId, edtStaffId, edtInputDay, edtProductId, edtImportPrice, edtQuantity;
    ImageButton btnTurnOnBlockAddImportBill, btnSearch, btnTurnOnBlockDeleteImportBill;
    ImageView btnClear, imgImportBillImage, btnExitLinearLayoutImportBillDetail, btnClose, btnCancel;
    Button btnAddNewImportBill, btnFixImportBill, btnEditImportBill, btnDeleteImportBill, btnTurnOnBlockEditImportBill, btnRestoreImportBill, btnDeleteActualImportBill;
    LinearLayout linearLayoutAddAndEditImportBill, linearLayoutImportBillDetail, linearLayoutDeleteAndRestoreImportBill, linearLayoutImportBillHasBeenDeleted, linearLayoutDeletedImportBillDetail;
    RecyclerView recyclerViewImportBill, recyclerViewImportBillHasBeenDeleted;
    ImportBillAdapter importBillAdapter, importBillListingHasBeenDeletedAdapter;
    ImportBillEntity importBillEntity, importBillListingHasBeenDeletedEntity;
    ArrayList<ImportBillModel> importBillList, importBillListingHasBeenDeleted;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedImportBillId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.activity_import_bill, container, false);
        // mapping id
        clickedImportBillId = 0;
        edtSupplierId = view.findViewById(R.id.edtSupplierId);
        edtStaffId = view.findViewById(R.id.edtStaffId);
        edtInputDay = view.findViewById(R.id.edtInputDay);
        tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        edtProductId = view.findViewById(R.id.edtProductId);
        edtImportPrice = view.findViewById(R.id.edtImportPrice);
        edtQuantity = view.findViewById(R.id.edtQuantity);

        txtImportBillId = view.findViewById(R.id.txtImportBillId);
        textInputDay = view.findViewById(R.id.textInputDay);
        textSupplierId = view.findViewById(R.id.textSupplierId);
        textStaffId = view.findViewById(R.id.textStaffId);
        textTotalAmount = view.findViewById(R.id.textTotalAmount);

        txtDeletedImportBillId = view.findViewById(R.id.txtDeletedImportBillId);
        btnTurnOnBlockDeleteImportBill = view.findViewById(R.id.btnTurnOnBlockDeleteImportBill);
        btnRestoreImportBill = view.findViewById(R.id.btnRestoreImportBill);
        btnDeleteActualImportBill = view.findViewById(R.id.btnDeleteActualImportBill);

        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        handleSearch();

        btnExitLinearLayoutImportBillDetail = view.findViewById(R.id.btnExitLinearLayoutImportBillDetail);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        linearLayoutAddAndEditImportBill = view.findViewById(R.id.linearLayoutAddAndEditImportBill);
        linearLayoutImportBillDetail = view.findViewById(R.id.linearLayoutImportBillDetail);
        linearLayoutDeleteAndRestoreImportBill = view.findViewById(R.id.linearLayoutDeleteAndRestoreImportBill);
        linearLayoutDeletedImportBillDetail = view.findViewById(R.id.linearLayoutDeletedImportBillDetail);

        // get ImportBill list
        importBillEntity = new ImportBillEntity(getContext());
        importBillList = importBillEntity.getImportBillList();
        importBillListingHasBeenDeleted = importBillEntity.getImportBillList();

        // call adapter and event click item
        importBillAdapter = new ImportBillAdapter(getContext(), importBillList);
        importBillListingHasBeenDeletedAdapter = new ImportBillAdapter(getContext(), importBillListingHasBeenDeleted);
        handleSelectImportBill();

//       assign data up recyclerView
        recyclerViewImportBill = view.findViewById(R.id.recyclerViewImportBill);
        recyclerViewImportBillHasBeenDeleted = view.findViewById(R.id.recyclerViewImportBillHasBeenDeleted);
        uploadDataToRecyclerViewImportBill();

        uploadNumberOfImportBillsDeleted();

        btnDeleteImportBill = view.findViewById(R.id.btnDeleteImportBill);
        btnTurnOnBlockEditImportBill = view.findViewById(R.id.btnTurnOnBlockEditImportBill);
        btnAddNewImportBill = view.findViewById(R.id.btnAddNewImportBill);
        btnEditImportBill = view.findViewById(R.id.btnFixImportBill);
        btnFixImportBill = view.findViewById(R.id.btnFixImportBill);
        btnTurnOnBlockAddImportBill = view.findViewById(R.id.btnTurnOnBlockAddImportBill);
        handleEventsClickButton();

        defaultLayout();
        return view;
    }

    public void defaultLayout() {
        linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
        linearLayoutImportBillDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreImportBill.setVisibility(View.GONE);
        linearLayoutDeletedImportBillDetail.setVisibility(View.GONE);
    }

    public ImportBillModel assignData() {
        ImportBillModel importBillModel = new ImportBillModel();
        importBillModel.ImportBillId = clickedImportBillId;
        importBillModel.SupplierId = Integer.parseInt(edtSupplierId.getText().toString().trim());
        importBillModel.StaffId = Integer.parseInt(edtStaffId.getText().toString().trim());
        importBillModel.InputDay = edtInputDay.getText().toString().trim();
        importBillModel.ToTalAmount = Double.parseDouble(tvTotalAmount.getText().toString().trim());
        return importBillModel;
    }

    public void uploadDataToRecyclerViewImportBill() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager _staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImportBill.setLayoutManager(_staggeredGridLayoutManager);
        recyclerViewImportBill.setAdapter(importBillAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewImportBillHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewImportBillHasBeenDeleted.setAdapter(importBillListingHasBeenDeletedAdapter);
    }

    public void handleSelectImportBill() {
        importBillAdapter.setOnItemClickListener(position -> {
            ImportBillModel clickedImportBill = importBillList.get(position);
            linearLayoutImportBillDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditImportBill.setVisibility(View.GONE);

            clickedImportBillId = clickedImportBill.ImportBillId;
            txtImportBillId.setText(String.valueOf(clickedImportBill.ImportBillId));
            textInputDay.setText(clickedImportBill.InputDay);
            textSupplierId.setText(clickedImportBill.SupplierId);
            textStaffId.setText(clickedImportBill.StaffId);
            textTotalAmount.setText(String.valueOf(clickedImportBill.ToTalAmount));

            edtSupplierId.setText(clickedImportBill.SupplierId);
            edtStaffId.setText(clickedImportBill.StaffId);
            edtInputDay.setText(clickedImportBill.InputDay);
            tvTotalAmount.setText(String.valueOf(clickedImportBill.ToTalAmount));
        });
        importBillListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            ImportBillModel clickedImportBill = importBillListingHasBeenDeleted.get(position);
            linearLayoutDeletedImportBillDetail.setVisibility(View.VISIBLE);
            linearLayoutImportBillDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditImportBill.setVisibility(View.GONE);

//            AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedImportBill.ImportBillId + " " + clickedImportBill.ImportBillName);

            clickedImportBillId = clickedImportBill.ImportBillId;
            txtDeletedImportBillId.setText(String.valueOf(clickedImportBill.ImportBillId));

            edtInputDay.setText(clickedImportBill.InputDay);
            edtSupplierId.setText(clickedImportBill.SupplierId);
            edtStaffId.setText(clickedImportBill.StaffId);
            tvTotalAmount.setText(String.valueOf(clickedImportBill.ToTalAmount));
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void uploadNumberOfImportBillsDeleted() {
        badgeNumberOfImportBillsDeleted = view.findViewById(R.id.badgeNumberOfImportBillsDeleted);
        badgeNumberOfImportBillsDeleted.setText("0");
    }

    public void handleEventsClickButton() {
        btnExitLinearLayoutImportBillDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutImportBillDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreImportBill.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditImportBill.setVisibility(View.VISIBLE);
                btnAddNewImportBill.setVisibility(View.VISIBLE);
                linearLayoutImportBillDetail.setVisibility(View.GONE);
                btnFixImportBill.setVisibility(View.GONE);
            }
        });
        btnAddNewImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBillModel newImportBill = assignData();
                boolean isCheck = importBillEntity.insertImportBill(newImportBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add importBill success");
                    reloadData();
                    linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add importBill fail");
                }
            }
        });
        btnEditImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBillModel newImportBill = assignData();
                boolean isCheck = importBillEntity.updateImportBill(newImportBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update ImportBill success");
                    reloadData();
                    recyclerViewImportBill.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update ImportBill fail");
                }
            }
        });

        btnTurnOnBlockEditImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditImportBill.setVisibility(View.VISIBLE);
                btnFixImportBill.setVisibility(View.VISIBLE);
                btnAddNewImportBill.setVisibility(View.GONE);
                linearLayoutImportBillDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBillModel newImportBill = assignData();
//                newImportBill.Deleted = true;
                boolean isCheck = importBillEntity.updateImportBill(newImportBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete ImportBill success");
                    uploadNumberOfImportBillsDeleted();
                    reloadData();
                    recyclerViewImportBill.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
                    linearLayoutImportBillDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete ImportBill fail");
                }
            }
        });
        btnTurnOnBlockDeleteImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreImportBill.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
                linearLayoutImportBillDetail.setVisibility(View.GONE);
                recyclerViewImportBill.setVisibility(View.GONE);
            }
        });
        btnRestoreImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBillModel newImportBill = assignData();
//                newImportBill.Deleted = false;
                boolean isCheck = importBillEntity.updateImportBill(newImportBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore importBill success");
                    uploadNumberOfImportBillsDeleted();
                    recyclerViewImportBill.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreImportBill.setVisibility(View.GONE);
                    linearLayoutDeletedImportBillDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore importBill fail");
                }
            }
        });
        btnDeleteActualImportBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportBillModel newImportBill = assignData();
                boolean isCheck = importBillEntity.deleteImportBill(newImportBill);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual importBill success");
                    uploadNumberOfImportBillsDeleted();
                    reloadData();
                    recyclerViewImportBill.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditImportBill.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual importBill fail");
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
                ArrayList<ImportBillModel> searchedImportBillsList = importBillEntity.getSearchedImportBillList(searchKeyword);
                importBillAdapter.updateImportBillList(searchedImportBillsList);
                importBillAdapter.notifyDataSetChanged();
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
        importBillList = importBillEntity.getImportBillList();
        importBillAdapter = new ImportBillAdapter(getContext(), importBillList);
        uploadDataToRecyclerViewImportBill();
    }
}