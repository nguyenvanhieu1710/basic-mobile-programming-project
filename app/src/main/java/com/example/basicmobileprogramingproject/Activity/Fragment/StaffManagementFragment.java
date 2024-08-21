package com.example.basicmobileprogramingproject.Activity.Fragment;

import androidx.fragment.app.Fragment;


import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


import com.example.basicmobileprogramingproject.Adapter.UserAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Model.AccountModel;

import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.RamdomUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StaffManagementFragment extends Fragment {
    ImageButton btnSearch, btnTurnOnBlockDeleteStaff;
    ImageView imgStaffImage, imageStaff, btnClear, btnExit, btnClose, btnCancel;
    TextView textName, textPhoneNumber, textBirthday, textGender, textAddress, badgeNumberOfStaffsDeleted, txtDeletedStaffName;
    EditText edtName, edtPhoneNumber, edtBirthday, edtGender, edtAddress, edtSearch;
    Button btnAddNewStaff, btnTurnOnBlockAddNewStaff, btnFixStaff, btnDelete, btnTurnOnBlockEditStaff, btnSelectStaffImage, btnRestoreStaff, btnDeleteActualStaff;
    LinearLayout linearLayoutAddAndEditStaff, linearLayoutStaffDetail, linearLayoutDeleteAndRestoreStaff, linearLayoutDeletedStaffDetail;
    RecyclerView recyclerViewStaff, recyclerViewStaffHasBeenDeleted;
    UserAdapter staffAdapter, staffListingHasBeenDeletedAdapter;
    UserEntity staffEntity;
    ArrayList<UserModel> staffList, staffListingHasBeenDeleted;
    AccountEntity accountEntity;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedStaffId;
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
        view = inflater.inflate(R.layout.staff_management, container, false);

        clickedStaffId = 0;
        edtName = view.findViewById(R.id.edtName);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        edtGender = view.findViewById(R.id.edtGender);
        edtAddress = view.findViewById(R.id.edtAddress);
        imgStaffImage = view.findViewById(R.id.imgStaffImage);
        btnSelectStaffImage = view.findViewById(R.id.btnSelectStaffImage);
        handleSelectStaffImage();

        textName = view.findViewById(R.id.textName);
        textPhoneNumber = view.findViewById(R.id.textPhoneNumber);
        textBirthday = view.findViewById(R.id.textBirthday);
        textGender = view.findViewById(R.id.textGender);
        textAddress = view.findViewById(R.id.textAddress);
        imageStaff = view.findViewById(R.id.imageStaff);

        txtDeletedStaffName = view.findViewById(R.id.txtDeletedStaffName);
        btnTurnOnBlockDeleteStaff = view.findViewById(R.id.btnTurnOnBlockDeleteStaff);
        btnRestoreStaff = view.findViewById(R.id.btnRestoreStaff);
        btnDeleteActualStaff = view.findViewById(R.id.btnDeleteActualStaff);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        handleSearch();

        btnExit = view.findViewById(R.id.btnExit);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        // hide layout add staff and layout staff detail
        linearLayoutAddAndEditStaff = view.findViewById(R.id.linearLayoutAddAndEditStaff);
        linearLayoutStaffDetail = view.findViewById(R.id.linearLayoutStaffDetail);
        linearLayoutDeleteAndRestoreStaff = view.findViewById(R.id.linearLayoutDeleteAndRestoreStaff);
        linearLayoutDeletedStaffDetail = view.findViewById(R.id.linearLayoutDeletedStaffDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());
//        databaseHandler.getDatabasePath();

        // get staff list
        staffEntity = new UserEntity(getContext());
        staffList = staffEntity.getStaffList();
        staffListingHasBeenDeleted = staffEntity.getStaffListingHasBeenDeleted();

        accountEntity = new AccountEntity(getContext());

        // call adapter
        staffAdapter = new UserAdapter(getContext(), staffList);
        staffListingHasBeenDeletedAdapter = new UserAdapter(getContext(), staffListingHasBeenDeleted);

        // assign data up recyclerView
        recyclerViewStaff = view.findViewById(R.id.recyclerViewStaff);
        recyclerViewStaffHasBeenDeleted = view.findViewById(R.id.recyclerViewStaffHasBeenDeleted);
        uploadDataToRecyclerViewStaff();

        // event click button
        btnTurnOnBlockAddNewStaff = view.findViewById(R.id.btnTurnOnBlockAddNewStaff);
        btnAddNewStaff = view.findViewById(R.id.btnAddNewStaff);
        btnTurnOnBlockEditStaff = view.findViewById(R.id.btnTurnOnBlockEditStaff);
        btnFixStaff = view.findViewById(R.id.btnFixStaff);
        btnDelete = view.findViewById(R.id.btnDelete);

        handleEventsClickButton();
        handleSelectStaff();

        uploadNumberOfStaffsDeleted();
        defaultLayout();
        return view;
    }

    public UserModel assignData() {
        UserModel staffModel = new UserModel();
        staffModel.UserId = clickedStaffId;
        staffModel.Name = edtName.getText().toString();
        staffModel.Birthday = edtBirthday.getText().toString();
        staffModel.PhoneNumber = edtPhoneNumber.getText().toString();
        staffModel.Image = imagePath;
        staffModel.Gender = edtGender.getText().toString();
        staffModel.Address = edtAddress.getText().toString();
        staffModel.Deleted = false;
        return staffModel;
    }

    public boolean validateData() {
        if (imagePath == null) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please select image");
            return false;
        }
        if (edtName.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter name");
            return false;
        }
        if (edtPhoneNumber.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter phone number");
            return false;
        }
        if (edtBirthday.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter birthday");
            return false;
        }
        if (edtGender.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter gender");
            return false;
        }
        if (edtAddress.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter address");
            return false;
        }
        return true;
    }

    public void handleSelectStaff() {
        staffAdapter.setOnItemClickListener(position -> {
            UserModel clickedStaff = staffList.get(position);
            linearLayoutStaffDetail.setVisibility(View.VISIBLE);

            clickedStaffId = clickedStaff.UserId;
            imageStaff.setImageURI(Uri.parse(clickedStaff.Image));
            textName.setText(clickedStaff.Name);
            textPhoneNumber.setText("Phone: " + clickedStaff.PhoneNumber);
            textBirthday.setText("Birthday: " + clickedStaff.Birthday);
            textGender.setText("Gender: " + clickedStaff.Gender);
            textAddress.setText("Address: " + clickedStaff.Address);

            imagePath = clickedStaff.Image;
            imgStaffImage.setImageURI(Uri.parse(clickedStaff.Image));
            edtName.setText(clickedStaff.Name);
            edtPhoneNumber.setText(clickedStaff.PhoneNumber);
            edtBirthday.setText(clickedStaff.Birthday);
            edtGender.setText(clickedStaff.Gender);
            edtAddress.setText(clickedStaff.Address);
        });
        staffListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            UserModel clickedStaff = staffListingHasBeenDeleted.get(position);
            linearLayoutDeletedStaffDetail.setVisibility(View.VISIBLE);
            linearLayoutStaffDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditStaff.setVisibility(View.GONE);

            clickedStaffId = clickedStaff.UserId;
            txtDeletedStaffName.setText(clickedStaff.Name);
//            imageStaff.setImageURI(Uri.parse(clickedStaff.Image));
//            textPhoneNumber.setText("Phone: " + clickedStaff.PhoneNumber);
//            textBirthday.setText("Birthday: " + clickedStaff.Birthday);
//            textGender.setText("Gender: " + clickedStaff.Gender);
//            textAddress.setText("Address: " + clickedStaff.Address);

            imagePath = clickedStaff.Image;
            imgStaffImage.setImageURI(Uri.parse(clickedStaff.Image));
            edtName.setText(clickedStaff.Name);
            edtPhoneNumber.setText(clickedStaff.PhoneNumber);
            edtBirthday.setText(clickedStaff.Birthday);
            edtGender.setText(clickedStaff.Gender);
            edtAddress.setText(clickedStaff.Address);
        });
    }

    public void handleSelectStaffImage() {
        btnSelectStaffImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
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
                ArrayList<UserModel> searchedUserList = staffEntity.getSearchedStaffsList(searchKeyword);
                staffAdapter.updateUserList(searchedUserList);
                staffAdapter.notifyDataSetChanged();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
    }

    public void defaultLayout() {
        linearLayoutAddAndEditStaff.setVisibility(View.GONE);
        linearLayoutStaffDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
        linearLayoutDeletedStaffDetail.setVisibility(View.GONE);
    }

    public void uploadDataToRecyclerViewStaff() {
        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewStaff.setLayoutManager(layoutManager);
        recyclerViewStaff.setAdapter(staffAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewStaffHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewStaffHasBeenDeleted.setAdapter(staffListingHasBeenDeletedAdapter);
    }

    public void handleEventsClickButton() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutStaffDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditStaff.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddNewStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditStaff.setVisibility(View.VISIBLE);
                btnAddNewStaff.setVisibility(View.VISIBLE);
                linearLayoutStaffDetail.setVisibility(View.GONE);
                btnFixStaff.setVisibility(View.GONE);
                linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
            }
        });
        btnAddNewStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                UserModel newStaff = assignData();
                AccountModel accountModel = new AccountModel();
                accountModel.AccountName = RamdomUtils.appendRandomNumbers(newStaff.Name);
                accountModel.Password = "123";
                accountModel.Role = "Staff";
                if (!accountEntity.insertAccount(accountModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add Account fail");
                    return;
                }
                newStaff.UserId = accountEntity.getFinalAccountId();
                boolean isCheck = staffEntity.insertUser(newStaff);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add Staff success");
                    reloadData();
                    linearLayoutAddAndEditStaff.setVisibility(View.GONE);
                    linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add Staff fail");
                }
            }
        });

        // when click btnEdit is show linearLayoutStaffDetail
        btnTurnOnBlockEditStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditStaff.setVisibility(View.VISIBLE);
                linearLayoutStaffDetail.setVisibility(View.GONE);
                btnAddNewStaff.setVisibility(View.GONE);
                btnFixStaff.setVisibility(View.VISIBLE);
                linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
            }
        });

        btnFixStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newStaff = assignData();
                boolean isCheck = staffEntity.updateUser(newStaff);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update staff success");
                    reloadData();
                    linearLayoutAddAndEditStaff.setVisibility(View.GONE);
                    linearLayoutStaffDetail.setVisibility(View.GONE);
                    linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update staff fail");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newStaff = assignData();
                AccountModel accountModel = accountEntity.getAccountById(newStaff.UserId);
                accountModel.Deleted = true;
                if (!accountEntity.updateAccount(accountModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete account fail");
                    return;
                }
                newStaff.Deleted = true;
                boolean isCheck = staffEntity.updateUser(newStaff);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete staff success");
                    reloadData();
                    uploadNumberOfStaffsDeleted();
                    linearLayoutStaffDetail.setVisibility(View.GONE);
                    linearLayoutAddAndEditStaff.setVisibility(View.GONE);
                    linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete staff fail");
                }
            }
        });
        btnTurnOnBlockDeleteStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreStaff.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditStaff.setVisibility(View.GONE);
                linearLayoutStaffDetail.setVisibility(View.GONE);
                recyclerViewStaff.setVisibility(View.GONE);
            }
        });
        btnRestoreStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newStaff = assignData();
                AccountModel accountModel = accountEntity.getAccountById(newStaff.UserId);
                accountModel.Deleted = false;
                if (!accountEntity.updateAccount(accountModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore account fail");
                    return;
                }
                newStaff.Deleted = false;
                boolean isCheck = staffEntity.updateUser(newStaff);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore staff success");
                    uploadNumberOfStaffsDeleted();
                    recyclerViewStaff.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreStaff.setVisibility(View.GONE);
                    linearLayoutDeletedStaffDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore staff fail");
                }
            }
        });
        btnDeleteActualStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newStaff = assignData();
                boolean isCheck = staffEntity.deleteUser(newStaff);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual staff success");
                    uploadNumberOfStaffsDeleted();
                    reloadData();
                    recyclerViewStaff.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditStaff.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual staff fail");
                }
            }
        });
    }

    public void reloadData() {
        staffList = staffEntity.getStaffList();
        staffAdapter.updateUserList(staffList);
        staffAdapter.notifyDataSetChanged();

        staffListingHasBeenDeleted = staffEntity.getStaffListingHasBeenDeleted();
        staffListingHasBeenDeletedAdapter.updateUserList(staffListingHasBeenDeleted);
        staffListingHasBeenDeletedAdapter.notifyDataSetChanged();
    }

    public void uploadNumberOfStaffsDeleted() {
        badgeNumberOfStaffsDeleted = view.findViewById(R.id.badgeNumberOfStaffsDeleted);
        badgeNumberOfStaffsDeleted.setText(String.valueOf(staffEntity.getNumberOfStaffsDeleted()));
    }

    private String copyImageToInternalStorage(Uri imageUri, String imageName) {
        String imagePath = null;
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Lưu ảnh vào thư mục của ứng dụng (internal storage) với tên ảnh gốc
            File file = new File(getActivity().getFilesDir(), imageName);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

            // Lấy đường dẫn của ảnh đã lưu
            imagePath = file.getAbsolutePath();
            // AlertDialogUtils.showInfoDialog(getActivity(), "Image saved at: " + imagePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageName = getFileName(imageUri); // Lấy tên ảnh gốc
            imgStaffImage.setImageURI(imageUri);
            imagePath = copyImageToInternalStorage(imageUri, imageName);
        }
    }

    private void deleteImageFromInternalStorage(String fileName) {
        File file = new File(getActivity().getFilesDir(), fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                AlertDialogUtils.showSuccessDialog(getActivity(), "Image deleted successfully: " + fileName);
            } else {
                AlertDialogUtils.showErrorDialog(getActivity(), "Failed to delete image: " + fileName);
            }
        } else {
            AlertDialogUtils.showInfoDialog(getActivity(), "Image not found: " + fileName);
        }
    }

    private void showImageFromPath() {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imgStaffImage.setImageBitmap(bitmap);
        } else {
            AlertDialogUtils.showInfoDialog(getActivity(), "No image available to display.");
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        try {
            // Lấy ContentResolver
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                // Nếu cursor không null, di chuyển đến đầu và lấy tên ảnh
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(projection[0]);
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }
}

