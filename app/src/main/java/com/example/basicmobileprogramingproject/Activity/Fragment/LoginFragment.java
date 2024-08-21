package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.LoadingUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class LoginFragment extends Fragment {
    View view;
    EditText edtUsername, edtPassword;
    Button btnLogin;
    ImageButton btnEye;
    private boolean isPasswordVisible = false;
    ArrayList<AccountModel> accountList;
    AccountEntity accountEntity;
    UserEntity userEntity;
    DatabaseHandler databaseHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment, container, false);
// mapping id
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnEye = view.findViewById(R.id.btnEye);
        btnLogin = view.findViewById(R.id.btnLogin);

        // call database
        databaseHandler = new DatabaseHandler(getContext());
//        databaseHandler.getDatabasePath();

        userEntity = new UserEntity(getContext());

        accountEntity = new AccountEntity(getContext());
        accountList = accountEntity.getAccountList();

        handleEventsClickButton();
        return view;
    }

    public void handleEventsClickButton() {
        btnEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Nếu mật khẩu đang hiển thị, ẩn nó đi và đổi icon
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    btnEye.setImageResource(R.drawable.icon_eye_close_blue); // Thay đổi icon mắt đóng
                } else {
                    // Nếu mật khẩu đang ẩn, hiển thị nó và đổi icon
                    edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    btnEye.setImageResource(R.drawable.icon_eye_open_blue); // Thay đổi icon mắt mở
                }

                // Di chuyển con trỏ đến cuối văn bản sau khi thay đổi inputType
                edtPassword.setSelection(edtPassword.getText().length());

                // Đổi trạng thái của biến isPasswordVisible
                isPasswordVisible = !isPasswordVisible;
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
//                LoadingUtils.showLoading(getContext());
                AccountModel account = assignData();
                boolean isExist = accountEntity.checkExistAccount(account);
                if (!isExist) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Username is not exists");
                    return;
                }
                AlertDialogUtils.showSuccessDialog(getContext(), "Login success");
                // deactivate Your Account Online
                accountEntity.DisableAllOnlineAccounts();
                // activate Your Account Online
                accountEntity.activateYourAccountOnline(account);
                // check role
                String role = accountEntity.getRole(account);
                account.AccountId = accountEntity.getAccountId(account);
                UserModel user = userEntity.getUserById(account.AccountId);
//                if (user == null) {
//                    AlertDialogUtils.showErrorDialog(getContext(), "User is not exists");
//                    return;
//                }
                if (role.equals("User")) {
                    Bundle result = new Bundle();
                    result.putString("username", user.Name);
                    result.putString("email", account.Email);
                    result.putBoolean("updateNavMenuOfUser", true);
                    getParentFragmentManager().setFragmentResult("requestKeyOfUser", result);
                    replaceFragment(new HomeFragment());
                } else if (role.equals("Staff")) {
                    Bundle result = new Bundle();
                    result.putString("username", user.Name);
                    result.putString("email", account.Email);
                    result.putBoolean("updateNavMenuOfStaff", true);
                    getParentFragmentManager().setFragmentResult("requestKeyOfStaff", result);
                    replaceFragment(new HomeFragment());
                } else if (role.equals("Admin")) {
                    Bundle result = new Bundle();
                    result.putBoolean("updateNavMenuOfAdmin", true);
                    getParentFragmentManager().setFragmentResult("requestKeyOfAdmin", result);
                    replaceFragment(new HomeFragment());
                }
            }
        });
    }

    public AccountModel assignData() {
        AccountModel accountModel = new AccountModel();
        accountModel.AccountName = edtUsername.getText().toString();
        accountModel.Password = edtPassword.getText().toString();
        return accountModel;
    }

    public boolean validateData() {
        if (edtUsername.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter username");
            return false;
        } else if (edtPassword.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter password");
            return false;
        }
        return true;
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }
}
