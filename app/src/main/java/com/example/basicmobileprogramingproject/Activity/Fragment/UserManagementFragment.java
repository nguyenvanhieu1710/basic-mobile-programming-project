package com.example.basicmobileprogramingproject.Activity.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.UserAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;
import com.example.basicmobileprogramingproject.Utils.RamdomUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserManagementFragment extends Fragment {
    ImageButton btnSearch, btnTurnOnBlockDeleteUser;
    ImageView imgUserImage, imageUser, btnClear, btnExit, btnClose, btnCancel;
    TextView textName, textPhoneNumber, textBirthday, textGender, textAddress, badgeNumberOfUsersDeleted, txtDeletedUserName;
    EditText edtName, edtPhoneNumber, edtBirthday, edtGender, edtAddress, edtSearch;
    Button btnAddNewUser, btnTurnOnBlockAddNewUser, btnFixUser, btnDelete, btnTurnOnBlockEditUser, btnSelectUserImage, btnRestoreUser, btnDeleteActualUser;
    LinearLayout linearLayoutAddAndEditUser, linearLayoutUserDetail, linearLayoutDeleteAndRestoreUser, linearLayoutDeletedUserDetail;
    RecyclerView recyclerViewUser, recyclerViewUserHasBeenDeleted;
    UserAdapter userAdapter, userListingHasBeenDeletedAdapter;
    UserEntity userEntity;
    ArrayList<UserModel> userList, userListingHasBeenDeleted;
    AccountEntity accountEntity;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedUserId;
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
        view = inflater.inflate(R.layout.user_management, container, false);

        clickedUserId = 0;
        edtName = view.findViewById(R.id.edtName);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        edtGender = view.findViewById(R.id.edtGender);
        edtAddress = view.findViewById(R.id.edtAddress);
        btnSelectUserImage = view.findViewById(R.id.btnSelectUserImage);
        imageUser = view.findViewById(R.id.imageUser);

        imgUserImage = view.findViewById(R.id.imgUserImage);
        textName = view.findViewById(R.id.textName);
        textPhoneNumber = view.findViewById(R.id.textPhoneNumber);
        textBirthday = view.findViewById(R.id.textBirthday);
        textGender = view.findViewById(R.id.textGender);
        textAddress = view.findViewById(R.id.textAddress);

        txtDeletedUserName = view.findViewById(R.id.txtDeletedUserName);
        btnTurnOnBlockDeleteUser = view.findViewById(R.id.btnTurnOnBlockDeleteUser);
        btnRestoreUser = view.findViewById(R.id.btnRestoreUser);
        btnDeleteActualUser = view.findViewById(R.id.btnDeleteActualUser);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        handleSearch();

        btnExit = view.findViewById(R.id.btnExit);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        // hide layout add user and layout user detail
        linearLayoutAddAndEditUser = view.findViewById(R.id.linearLayoutAddAndEditUser);
        linearLayoutUserDetail = view.findViewById(R.id.linearLayoutUserDetail);
        linearLayoutDeleteAndRestoreUser = view.findViewById(R.id.linearLayoutDeleteAndRestoreUser);
        linearLayoutDeletedUserDetail = view.findViewById(R.id.linearLayoutDeletedUserDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());
//        databaseHandler.getDatabasePath();

        // get user list
        userEntity = new UserEntity(getContext());
        userList = userEntity.getCustomerList();
        userListingHasBeenDeleted = userEntity.getCustomerListingHasBeenDeleted();
        if (userList.isEmpty()) {
            AlertDialogUtils.showInfoDialog(getContext(), "No user found");
        }

        accountEntity = new AccountEntity(getContext());

        // call adapter and event click item
        userAdapter = new UserAdapter(getContext(), userList);
        userListingHasBeenDeletedAdapter = new UserAdapter(getContext(), userListingHasBeenDeleted);

        // assign data up recyclerView
        recyclerViewUser = view.findViewById(R.id.recyclerViewUser);
        recyclerViewUserHasBeenDeleted = view.findViewById(R.id.recyclerViewUserHasBeenDeleted);
        uploadDataToRecyclerViewUser();

        // event click button
        btnTurnOnBlockAddNewUser = view.findViewById(R.id.btnTurnOnBlockAddNewUser);
        btnAddNewUser = view.findViewById(R.id.btnAddNewUser);
        btnTurnOnBlockEditUser = view.findViewById(R.id.btnTurnOnBlockEditUser);
        btnFixUser = view.findViewById(R.id.btnFixUser);
        btnDelete = view.findViewById(R.id.btnDelete);

        handleSelectUserImage();
        handleEventsClickButton();
        handleSelectUser();
        uploadNumberOfUsersDeleted();

        defaultLayout();

        return view;
    }

    public UserModel assignData() {
        UserModel userModel = new UserModel();
        userModel.UserId = clickedUserId;
        userModel.Name = edtName.getText().toString();
        userModel.Birthday = edtBirthday.getText().toString();
        userModel.PhoneNumber = edtPhoneNumber.getText().toString();
        userModel.Image = imagePath;
        userModel.Gender = edtGender.getText().toString();
        userModel.Address = edtAddress.getText().toString();
        userModel.Deleted = false;
        return userModel;
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

    public void uploadNumberOfUsersDeleted() {
        badgeNumberOfUsersDeleted = view.findViewById(R.id.badgeNumberOfUsersDeleted);
        badgeNumberOfUsersDeleted.setText(String.valueOf(userEntity.getNumberOfCustomersDeleted()));
    }

    public void handleSelectUser() {
        userAdapter.setOnItemClickListener(position -> {
            UserModel clickedUser = userList.get(position);
            linearLayoutUserDetail.setVisibility(View.VISIBLE);

            clickedUserId = clickedUser.UserId;
            textName.setText(clickedUser.Name);
            textPhoneNumber.setText("Phone: " + clickedUser.PhoneNumber);
            textBirthday.setText("Birthday: " + clickedUser.Birthday);
            textGender.setText("Gender: " + clickedUser.Gender);
            textAddress.setText("Address: " + clickedUser.Address);
            imgUserImage.setImageURI(Uri.parse(clickedUser.Image));
            imagePath = clickedUser.Image;

            imageUser.setImageURI(Uri.parse(clickedUser.Image));
            edtName.setText(clickedUser.Name);
            edtPhoneNumber.setText(clickedUser.PhoneNumber);
            edtBirthday.setText(clickedUser.Birthday);
            edtGender.setText(clickedUser.Gender);
            edtAddress.setText(clickedUser.Address);
        });
        userListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            UserModel clickedUser = userListingHasBeenDeleted.get(position);
            linearLayoutDeletedUserDetail.setVisibility(View.VISIBLE);
            linearLayoutUserDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditUser.setVisibility(View.GONE);

            clickedUserId = clickedUser.UserId;
            imagePath = clickedUser.Image;

            txtDeletedUserName.setText(clickedUser.Name);
//            textPhoneNumber.setText("Phone: " + clickedUser.PhoneNumber);
//            textBirthday.setText("Birthday: " + clickedUser.Birthday);
//            textGender.setText("Gender: " + clickedUser.Gender);
//            textAddress.setText("Address: " + clickedUser.Address);
//            imgUserImage.setImageURI(Uri.parse(clickedUser.Image));

            imageUser.setImageURI(Uri.parse(clickedUser.Image));
            edtName.setText(clickedUser.Name);
            edtPhoneNumber.setText(clickedUser.PhoneNumber);
            edtBirthday.setText(clickedUser.Birthday);
            edtGender.setText(clickedUser.Gender);
            edtAddress.setText(clickedUser.Address);
        });
    }

    public void handleSelectUserImage() {
        btnSelectUserImage.setOnClickListener(v -> {
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
                ArrayList<UserModel> searchedUserList = userEntity.getSearchedCustomersList(searchKeyword);
                userAdapter.updateUserList(searchedUserList);
                userAdapter.notifyDataSetChanged();
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
        linearLayoutAddAndEditUser.setVisibility(View.GONE);
        linearLayoutUserDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
        linearLayoutDeletedUserDetail.setVisibility(View.GONE);
    }

    public void uploadDataToRecyclerViewUser() {
        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewUser.setLayoutManager(layoutManager);
        recyclerViewUser.setAdapter(userAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewUserHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewUserHasBeenDeleted.setAdapter(userListingHasBeenDeletedAdapter);
    }

    public void handleEventsClickButton() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutUserDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditUser.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditUser.setVisibility(View.VISIBLE);
                btnAddNewUser.setVisibility(View.VISIBLE);
                linearLayoutUserDetail.setVisibility(View.GONE);
                btnFixUser.setVisibility(View.GONE);
                linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
            }
        });
        btnAddNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                UserModel newUser = assignData();
                AccountModel accountModel = new AccountModel();
                accountModel.AccountName = RamdomUtils.appendRandomNumbers(newUser.Name);
                accountModel.Password = "123";
                accountModel.Role = "User";
                if (!accountEntity.insertAccount(accountModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add Account fail");
                    return;
                }
                newUser.UserId = accountEntity.getFinalAccountId();
                boolean isCheck = userEntity.insertUser(newUser);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add User success");
                    reloadData();
                    linearLayoutAddAndEditUser.setVisibility(View.GONE);
                    linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add User fail");
                }
            }
        });

        // when click btnEdit is show linearLayoutUserDetail
        btnTurnOnBlockEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditUser.setVisibility(View.VISIBLE);
                linearLayoutUserDetail.setVisibility(View.GONE);
                btnAddNewUser.setVisibility(View.GONE);
                btnFixUser.setVisibility(View.VISIBLE);
                linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
            }
        });

        btnFixUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newUser = assignData();
                boolean isCheck = userEntity.updateUser(newUser);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update user success");
                    reloadData();
                    linearLayoutAddAndEditUser.setVisibility(View.GONE);
                    linearLayoutUserDetail.setVisibility(View.GONE);
                    linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update user fail");
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newUser = assignData();
                AccountModel accountModel = accountEntity.getAccountById(newUser.UserId);
                accountModel.Deleted = true;
                if (!accountEntity.updateAccount(accountModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete account fail");
                    return;
                }
                newUser.Deleted = true;
                boolean isCheck = userEntity.updateUser(newUser);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete user success");
                    reloadData();
                    uploadNumberOfUsersDeleted();
                    linearLayoutAddAndEditUser.setVisibility(View.GONE);
                    linearLayoutUserDetail.setVisibility(View.GONE);
                    linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete user fail");
                }
            }
        });
        btnTurnOnBlockDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreUser.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditUser.setVisibility(View.GONE);
                linearLayoutUserDetail.setVisibility(View.GONE);
                recyclerViewUser.setVisibility(View.GONE);
            }
        });
        btnRestoreUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newUser = assignData();
                AccountModel accountModel = accountEntity.getAccountById(newUser.UserId);
                accountModel.Deleted = false;
                if (!accountEntity.updateAccount(accountModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore account fail");
                    return;
                }
                newUser.Deleted = false;
                boolean isCheck = userEntity.updateUser(newUser);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore user success");
                    uploadNumberOfUsersDeleted();
                    recyclerViewUser.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreUser.setVisibility(View.GONE);
                    linearLayoutDeletedUserDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore user fail");
                }
            }
        });
        btnDeleteActualUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModel newUser = assignData();
                boolean isCheck = userEntity.deleteUser(newUser);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual user success");
                    uploadNumberOfUsersDeleted();
                    reloadData();
                    recyclerViewUser.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditUser.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual user fail");
                }
            }
        });
    }

    public void reloadData() {
        userList = userEntity.getCustomerList();
        userAdapter.updateUserList(userList);
        userAdapter.notifyDataSetChanged();

        userListingHasBeenDeleted = userEntity.getCustomerListingHasBeenDeleted();
        userListingHasBeenDeletedAdapter.updateUserList(userListingHasBeenDeleted);
        userListingHasBeenDeletedAdapter.notifyDataSetChanged();
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
            imgUserImage.setImageURI(imageUri);
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
            imgUserImage.setImageBitmap(bitmap);
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
