package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.AccountAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.StaffEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.StaffModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    View view;
    private TextView textAccountName, textPassword, textRole, textEmail, textStatus, badgeNumberOfAccountsDeleted;
    private EditText edtSearch, edtAccountName, edtPassword, edtRole, edtEmail, edtStatus;
    private ImageView btnClear, btnExitLinearLayoutAccountDetail, btnCloseLinearLayoutAddAndEditAccount, btnCancelLinearLayoutDeletedAccountDetail;
    private ImageButton btnSearch, btnTurnOnBlockDeleteAccount;
    private RecyclerView rvAccountList;
    private Button btnAddNewAccount, btnFixAccount, btnDeleteAccount,
            btnTurnOnBlockAddNewAccount, btnTurnOnBlockEditAccount, btnRestoreAccount, btnDeleteActualAccount;
    private LinearLayout linearLayoutAddAndEditAccount, linearLayoutAccountDetail, linearLayoutDeleteAndRestoreAccount, linearLayoutDeletedAccountDetail;
    UserEntity userEntity;
    StaffEntity staffEntity;
    AccountEntity accountEntity;
    ArrayList<AccountModel> accountList;
    AccountAdapter accountAdapter;
    int clickedAccountId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_account_management, container, false);
        // mapping id
        clickedAccountId = 0;
        edtAccountName = view.findViewById(R.id.edtAccountName);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtRole = view.findViewById(R.id.edtRole);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtStatus = view.findViewById(R.id.edtStatus);

        textAccountName = view.findViewById(R.id.textAccountName);
        textPassword = view.findViewById(R.id.textPassword);
        textRole = view.findViewById(R.id.textRole);
        textEmail = view.findViewById(R.id.textEmail);
        textStatus = view.findViewById(R.id.textStatus);

        btnAddNewAccount = view.findViewById(R.id.btnAddNewAccount);
        btnFixAccount = view.findViewById(R.id.btnFixAccount);
        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnTurnOnBlockAddNewAccount = view.findViewById(R.id.btnTurnOnBlockAddNewAccount);
        btnTurnOnBlockEditAccount = view.findViewById(R.id.btnTurnOnBlockEditAccount);
        btnRestoreAccount = view.findViewById(R.id.btnRestoreAccount);
        btnDeleteActualAccount = view.findViewById(R.id.btnDeleteActualAccount);
        btnTurnOnBlockDeleteAccount = view.findViewById(R.id.btnTurnOnBlockDeleteAccount);

        btnCloseLinearLayoutAddAndEditAccount = view.findViewById(R.id.btnCloseLinearLayoutAddAndEditAccount);
        btnExitLinearLayoutAccountDetail = view.findViewById(R.id.btnExitLinearLayoutAccountDetail);
        btnCancelLinearLayoutDeletedAccountDetail = view.findViewById(R.id.btnCancelLinearLayoutDeletedAccountDetail);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        handleSearch();

        rvAccountList = view.findViewById(R.id.rv_account_list);

        // hide layout add user and layout user detail
        linearLayoutAddAndEditAccount = view.findViewById(R.id.linearLayoutAddAndEditAccount);
        linearLayoutAccountDetail = view.findViewById(R.id.linearLayoutAccountDetail);
        linearLayoutDeleteAndRestoreAccount = view.findViewById(R.id.linearLayoutDeleteAndRestoreAccount);
        linearLayoutDeletedAccountDetail = view.findViewById(R.id.linearLayoutDeletedAccountDetail);

        userEntity = new UserEntity(getContext());
        staffEntity = new StaffEntity(getContext());
        accountEntity = new AccountEntity(getContext());
        accountList = accountEntity.getAccountList();
//        if (accountList.isEmpty()) {
//            AlertDialogUtils.showInfoDialog(getContext(), "No account found");
//        }

        accountAdapter = new AccountAdapter(getContext(), accountList);
        uploadDataToRecyclerViewAccount();

        handleSelectAccount();
        uploadNumberOfAccountsDeleted();
        handleEventClickButton();
        defaultLayout();

        return view;
    }

    public void defaultLayout() {
        linearLayoutAddAndEditAccount.setVisibility(View.GONE);
        linearLayoutAccountDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreAccount.setVisibility(View.GONE);
        linearLayoutDeletedAccountDetail.setVisibility(View.GONE);
    }

    public void uploadDataToRecyclerViewAccount() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        rvAccountList.setLayoutManager(layoutManager);
        rvAccountList.setAdapter(accountAdapter);
    }

    public void handleSelectAccount() {
        accountAdapter.setOnItemClickListener(position -> {
            AccountModel clickedAccount = accountList.get(position);
            linearLayoutAccountDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditAccount.setVisibility(View.GONE);
            linearLayoutDeleteAndRestoreAccount.setVisibility(View.GONE);

            clickedAccountId = clickedAccount.AccountId;
            textAccountName.setText(clickedAccount.AccountName);
            textPassword.setText(clickedAccount.Password);
            textRole.setText(clickedAccount.Role);
            textEmail.setText(clickedAccount.Email);
            textStatus.setText(clickedAccount.Status);

            edtAccountName.setText(clickedAccount.AccountName);
            edtPassword.setText(clickedAccount.Password);
            edtRole.setText(clickedAccount.Role);
            edtEmail.setText(clickedAccount.Email);
            edtStatus.setText(clickedAccount.Status);
        });
    }

    public AccountModel assignData() {
        AccountModel accountModel = new AccountModel();
        accountModel.AccountId = clickedAccountId;
        accountModel.AccountName = edtAccountName.getText().toString().trim();
        accountModel.Password = edtPassword.getText().toString().trim();
        accountModel.Role = edtRole.getText().toString().trim();
        accountModel.DayCreated = DateUtils.getToday();
        accountModel.RememberPassword = false;
        accountModel.Email = edtEmail.getText().toString().trim();
        accountModel.Status = edtStatus.getText().toString().trim();
        return accountModel;
    }

    public boolean validate() {
        if (edtAccountName.getText().toString().trim().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter account name");
            return false;
        }
        if (edtPassword.getText().toString().trim().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter password");
            return false;
        }
        if (edtRole.getText().toString().trim().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter role");
            return false;
        }
        String role = edtRole.getText().toString().trim();
        if (!role.equals("Admin") && !role.equals("Staff") && !role.equals("User")) {
            AlertDialogUtils.showErrorDialog(getContext(), "Role must be Admin, Staff, or User");
            return false;
        }
        if (edtEmail.getText().toString().trim().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter email");
            return false;
        }
        if (edtStatus.getText().toString().trim().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter status");
            return false;
        }
        String status = edtStatus.getText().toString().trim();
        if (!status.equals("Online") && !status.equals("Offline")) {
            AlertDialogUtils.showErrorDialog(getContext(), "Status must be Online or Offline");
            return false;
        }
        return true;
    }

    public void handleEventClickButton() {
        btnExitLinearLayoutAccountDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAccountDetail.setVisibility(View.GONE);
            }
        });
        btnCloseLinearLayoutAddAndEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditAccount.setVisibility(View.GONE);
            }
        });
        btnCancelLinearLayoutDeletedAccountDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreAccount.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditAccount.setVisibility(View.VISIBLE);
                btnAddNewAccount.setVisibility(View.VISIBLE);
                linearLayoutAccountDetail.setVisibility(View.GONE);
                btnFixAccount.setVisibility(View.GONE);
            }
        });
        btnAddNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                AccountModel newAccount = assignData();
                boolean isCheck = accountEntity.insertAccount(newAccount);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add account success");
                    if (newAccount.Role == "User") {
                        UserModel userModel = new UserModel();
                        userModel.UserId = accountEntity.getFinalAccountId();
                        userModel.Name = newAccount.AccountName;
                        userModel.Birthday = "";
                        userModel.PhoneNumber = "";
                        userModel.Image = "";
                        userModel.Gender = "";
                        userModel.Address = "";
                        userModel.Deleted = false;
                        userEntity.insertUser(userModel);
                    } else if (newAccount.Role == "Staff") {
                        StaffModel staffModel = new StaffModel();
                        staffModel.StaffId = accountEntity.getFinalAccountId();
                        staffModel.Name = newAccount.AccountName;
                        staffModel.Birthday = "";
                        staffModel.PhoneNumber = "";
                        staffModel.Image = "";
                        staffModel.Gender = "";
                        staffModel.Address = "";
                        staffModel.Position = "";
                        staffModel.Deleted = false;
                        staffEntity.insertStaff(staffModel);
                    }
                    reloadData();
                    linearLayoutAddAndEditAccount.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add account fail");
                }
            }
        });
        btnFixAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate()) {
                    return;
                }
                AccountModel newAccount = assignData();
                boolean isCheck = accountEntity.updateAccount(newAccount);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update Account success");
                    reloadData();
                    rvAccountList.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAccount.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update Account fail");
                }
            }
        });

        btnTurnOnBlockEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditAccount.setVisibility(View.VISIBLE);
                btnFixAccount.setVisibility(View.VISIBLE);
                btnAddNewAccount.setVisibility(View.GONE);
                linearLayoutAccountDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountModel newAccount = assignData();
                newAccount.Deleted = true;
                if (newAccount.Role == "User") {
                    UserEntity userEntity = new UserEntity(getContext());
                    UserModel userModel = userEntity.getUserById(newAccount.AccountId);
                    userModel.Deleted = true;
                    userEntity.updateUser(userModel);
                } else if (newAccount.Role == "Staff") {
                    StaffEntity staffEntity = new StaffEntity(getContext());
                    StaffModel staffModel = staffEntity.getStaffById(newAccount.AccountId);
                    staffModel.Deleted = true;
                    staffEntity.updateStaff(staffModel);
                }
                boolean isCheck = accountEntity.updateAccount(newAccount);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete Account success");
                    uploadNumberOfAccountsDeleted();
                    reloadData();
                    rvAccountList.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAccount.setVisibility(View.GONE);
                    linearLayoutAccountDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete Account fail");
                }
            }
        });
        btnTurnOnBlockDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreAccount.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditAccount.setVisibility(View.GONE);
                linearLayoutAccountDetail.setVisibility(View.GONE);
                rvAccountList.setVisibility(View.GONE);
            }
        });
        btnRestoreAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountModel newAccount = assignData();
                newAccount.Deleted = false;
                boolean isCheck = accountEntity.updateAccount(newAccount);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore account success");
                    uploadNumberOfAccountsDeleted();
                    rvAccountList.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreAccount.setVisibility(View.GONE);
                    linearLayoutDeletedAccountDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore account fail");
                }
            }
        });
        btnDeleteActualAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountModel newAccount = assignData();
                if (newAccount.Role == "User") {
                    UserEntity userEntity = new UserEntity(getContext());
                    UserModel userModel = userEntity.getUserById(newAccount.AccountId);
                    userEntity.deleteUser(userModel);
                } else if (newAccount.Role == "Staff") {
                    StaffEntity staffEntity = new StaffEntity(getContext());
                    StaffModel staffModel = staffEntity.getStaffById(newAccount.AccountId);
                    staffEntity.deleteStaff(staffModel);
                }
                boolean isCheck = accountEntity.deleteAccount(newAccount);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual account success");
                    uploadNumberOfAccountsDeleted();
                    reloadData();
                    rvAccountList.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAccount.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual account fail");
                }
            }
        });
    }

    public void uploadNumberOfAccountsDeleted() {
        badgeNumberOfAccountsDeleted = view.findViewById(R.id.badgeNumberOfAccountsDeleted);
        int numberOfAccountsDeleted = accountEntity.getNumberOfAccountsDeleted();
        badgeNumberOfAccountsDeleted.setText(String.valueOf(numberOfAccountsDeleted));
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
                ArrayList<AccountModel> searchedAccountList = accountEntity.getSearchedAccountList(searchKeyword);
                accountAdapter.updateAccountList(searchedAccountList);
                accountAdapter.notifyDataSetChanged();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
    }

    public void reloadData() {
        accountList = accountEntity.getAccountList();
        accountAdapter.updateAccountList(accountList);
        accountAdapter.notifyDataSetChanged();
    }
}
