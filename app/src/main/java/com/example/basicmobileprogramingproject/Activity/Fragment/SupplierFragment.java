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


import com.example.basicmobileprogramingproject.Adapter.SupplierAdapter;
import com.example.basicmobileprogramingproject.Adapter.SupplierAdapter;
import com.example.basicmobileprogramingproject.Entity.SupplierEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;

import com.example.basicmobileprogramingproject.Model.SupplierModel;
import com.example.basicmobileprogramingproject.Model.SupplierModel;
import com.example.basicmobileprogramingproject.Model.SupplierModel;

import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SupplierFragment extends Fragment {
    TextView badgeNumberOfSuppliersDeleted, txtSupplierName, txtDeletedSupplierName, textPhoneNumber, textAddress;
    EditText edtSearch, edtSupplierName, edtPhoneNumber, edtAddress;
    ImageButton btnTurnOnBlockAddSupplier, btnSearch, btnTurnOnBlockDeleteSupplier;
    ImageView btnClear, imgSupplierImage, btnExit, btnClose, btnCancel;
    Button btnAddNewSupplier, btnFixSupplier, btnEditSupplier, btnDeleteSupplier, btnTurnOnBlockEditSupplier, btnRestoreSupplier, btnDeleteActualSupplier;
    LinearLayout linearLayoutAddAndEditSupplier, linearLayoutSupplierDetail, linearLayoutDeleteAndRestoreSupplier, linearLayoutSupplierHasBeenDeleted, linearLayoutDeletedSupplierDetail;
    RecyclerView recyclerViewSupplier, recyclerViewSupplierHasBeenDeleted;
    SupplierAdapter supplierAdapter, supplierListingHasBeenDeletedAdapter;
    SupplierEntity supplierEntity, supplierListingHasBeenDeletedEntity;
    ArrayList<SupplierModel> supplierList, supplierListingHasBeenDeleted;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedSupplierId;
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
        view = inflater.inflate(R.layout.supplier_fragment, container, false);
        // mapping id
        clickedSupplierId = 0;
        edtSupplierName = view.findViewById(R.id.edtSupplierName);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtAddress = view.findViewById(R.id.edtAddress);

        txtSupplierName = view.findViewById(R.id.txtSupplierName);
        textPhoneNumber = view.findViewById(R.id.textPhoneNumber);
        textAddress = view.findViewById(R.id.textAddress);

        txtDeletedSupplierName = view.findViewById(R.id.txtDeletedSupplierName);
        btnTurnOnBlockDeleteSupplier = view.findViewById(R.id.btnTurnOnBlockDeleteSupplier);
        btnRestoreSupplier = view.findViewById(R.id.btnRestoreSupplier);
        btnDeleteActualSupplier = view.findViewById(R.id.btnDeleteActualSupplier);

        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        handleSearch();

        btnExit = view.findViewById(R.id.btnExit);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        linearLayoutAddAndEditSupplier = view.findViewById(R.id.linearLayoutAddAndEditSupplier);
        linearLayoutSupplierDetail = view.findViewById(R.id.linearLayoutSupplierDetail);
        linearLayoutDeleteAndRestoreSupplier = view.findViewById(R.id.linearLayoutDeleteAndRestoreSupplier);
        linearLayoutDeletedSupplierDetail = view.findViewById(R.id.linearLayoutDeletedSupplierDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());

        // get Supplier list
        supplierEntity = new SupplierEntity(getContext());
        supplierList = supplierEntity.getSupplierList();
        supplierListingHasBeenDeleted = supplierEntity.getSupplierListingHasBeenDeleted();

        // call adapter and event click item
        supplierAdapter = new SupplierAdapter(getContext(), supplierList);
        supplierListingHasBeenDeletedAdapter = new SupplierAdapter(getContext(), supplierListingHasBeenDeleted);
        handleSelectSupplier();

//       assign data up recyclerView
        recyclerViewSupplier = view.findViewById(R.id.recyclerViewSupplier);
        recyclerViewSupplierHasBeenDeleted = view.findViewById(R.id.recyclerViewSupplierHasBeenDeleted);
        uploadDataToRecyclerViewSupplier();

        uploadNumberOfSuppliersDeleted();

        btnDeleteSupplier = view.findViewById(R.id.btnDeleteSupplier);
        btnTurnOnBlockEditSupplier = view.findViewById(R.id.btnTurnOnBlockEditSupplier);
        btnAddNewSupplier = view.findViewById(R.id.btnAddNewSupplier);
        btnEditSupplier = view.findViewById(R.id.btnFixSupplier);
        btnFixSupplier = view.findViewById(R.id.btnFixSupplier);
        btnTurnOnBlockAddSupplier = view.findViewById(R.id.btnTurnOnBlockAddSupplier);
        handleEventsClickButton();

        defaultLayout();
        return view;
    }

    public void defaultLayout() {
        linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
        linearLayoutSupplierDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreSupplier.setVisibility(View.GONE);
        linearLayoutDeletedSupplierDetail.setVisibility(View.GONE);
    }

    public SupplierModel assignData() {
        SupplierModel supplierModel = new SupplierModel();
        supplierModel.SupplierId = clickedSupplierId;
        supplierModel.SupplierName = edtSupplierName.getText().toString();
        supplierModel.PhoneNumber = edtPhoneNumber.getText().toString();
        supplierModel.Address = edtAddress.getText().toString();
        supplierModel.Deleted = false;
        return supplierModel;
    }

    public void uploadDataToRecyclerViewSupplier() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSupplier.setLayoutManager(layoutManager);
        recyclerViewSupplier.setAdapter(supplierAdapter);

        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewSupplierHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewSupplierHasBeenDeleted.setAdapter(supplierListingHasBeenDeletedAdapter);
    }

    public void handleSelectSupplier() {
        supplierAdapter.setOnItemClickListener(position -> {
            SupplierModel clickedSupplier = supplierList.get(position);
            linearLayoutSupplierDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditSupplier.setVisibility(View.GONE);

            clickedSupplierId = clickedSupplier.SupplierId;
            txtSupplierName.setText(clickedSupplier.SupplierName);
            textPhoneNumber.setText("Phone Number: " + clickedSupplier.PhoneNumber);
            textAddress.setText("Address: " + clickedSupplier.Address);

            edtSupplierName.setText(clickedSupplier.SupplierName);
            edtPhoneNumber.setText(clickedSupplier.PhoneNumber);
            edtAddress.setText(clickedSupplier.Address);
        });
        supplierListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            SupplierModel clickedSupplier = supplierListingHasBeenDeleted.get(position);
            linearLayoutDeletedSupplierDetail.setVisibility(View.VISIBLE);
            linearLayoutSupplierDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditSupplier.setVisibility(View.GONE);

//            AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedSupplier.SupplierId + " " + clickedSupplier.SupplierName);

            clickedSupplierId = clickedSupplier.SupplierId;
            txtDeletedSupplierName.setText(clickedSupplier.SupplierName);

            edtSupplierName.setText(clickedSupplier.SupplierName);
            edtPhoneNumber.setText(clickedSupplier.PhoneNumber);
            edtAddress.setText(clickedSupplier.Address);
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void uploadNumberOfSuppliersDeleted() {
        badgeNumberOfSuppliersDeleted = view.findViewById(R.id.badgeNumberOfSuppliersDeleted);
        badgeNumberOfSuppliersDeleted.setText(String.valueOf(supplierEntity.getNumberOfSuppliersDeleted()));
    }

    public void handleEventsClickButton() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutSupplierDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreSupplier.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditSupplier.setVisibility(View.VISIBLE);
                btnAddNewSupplier.setVisibility(View.VISIBLE);
                linearLayoutSupplierDetail.setVisibility(View.GONE);
                btnFixSupplier.setVisibility(View.GONE);
            }
        });
        btnAddNewSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierModel newSupplier = assignData();
                boolean isCheck = supplierEntity.insertSupplier(newSupplier);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add supplier success");
                    reloadData();
                    linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add supplier fail");
                }
            }
        });
        btnEditSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierModel newSupplier = assignData();
                boolean isCheck = supplierEntity.updateSupplier(newSupplier);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update Supplier success");
                    linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update Supplier fail");
                }
            }
        });

        btnTurnOnBlockEditSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditSupplier.setVisibility(View.VISIBLE);
                btnFixSupplier.setVisibility(View.VISIBLE);
                btnAddNewSupplier.setVisibility(View.GONE);
                linearLayoutSupplierDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierModel newSupplier = assignData();
                newSupplier.Deleted = true;
                boolean isCheck = supplierEntity.updateSupplier(newSupplier);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete Supplier success");
                    uploadNumberOfSuppliersDeleted();
                    reloadData();
                    recyclerViewSupplier.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
                    linearLayoutSupplierDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete Supplier fail");
                }
            }
        });
        btnTurnOnBlockDeleteSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreSupplier.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
                linearLayoutSupplierDetail.setVisibility(View.GONE);
                recyclerViewSupplier.setVisibility(View.GONE);

//                AlertDialogUtils.showInfoDialog(getContext(), "success");
            }
        });
        btnRestoreSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierModel newSupplier = assignData();
                newSupplier.Deleted = false;
                boolean isCheck = supplierEntity.updateSupplier(newSupplier);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore supplier success");
                    uploadNumberOfSuppliersDeleted();
                    recyclerViewSupplier.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreSupplier.setVisibility(View.GONE);
                    linearLayoutDeletedSupplierDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore supplier fail");
                }
            }
        });
        btnDeleteActualSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SupplierModel newSupplier = assignData();
                boolean isCheck = supplierEntity.deleteSupplier(newSupplier);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual supplier success");
                    uploadNumberOfSuppliersDeleted();
                    reloadData();
                    recyclerViewSupplier.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditSupplier.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual supplier fail");
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
                ArrayList<SupplierModel> searchedSuppliersList = supplierEntity.getSearchedSuppliersList(searchKeyword);
                supplierAdapter.updateSupplierList(searchedSuppliersList);
                supplierAdapter.notifyDataSetChanged();
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
        supplierList = supplierEntity.getSupplierList();
        supplierAdapter = new SupplierAdapter(getContext(), supplierList);
        uploadDataToRecyclerViewSupplier();
    }
}