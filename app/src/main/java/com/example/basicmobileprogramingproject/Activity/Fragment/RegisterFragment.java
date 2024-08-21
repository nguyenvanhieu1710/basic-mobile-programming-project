package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {
    View view;
    EditText edtUsername, edtPassword;
    Button btnRegister;
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
        view = inflater.inflate(R.layout.register_fragment, container, false);
        // mapping id
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnEye = view.findViewById(R.id.btnEye);
        btnRegister = view.findViewById(R.id.btnRegister);

        // call database
        databaseHandler = new DatabaseHandler(getContext());
//        databaseHandler.getDatabasePath();

        accountEntity = new AccountEntity(getContext());
        accountList = accountEntity.getAccountList();

        userEntity = new UserEntity(getContext());

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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                AccountModel newAccount = assignData();
                boolean isExist = accountEntity.checkExistAccount(newAccount);
                if (isExist) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Username is exists");
                    return;
                }
                if (!accountEntity.insertAccount(newAccount)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add Account fail");
                    return;
                }
                UserModel newUser = new UserModel();
                newUser.UserId = accountEntity.getFinalAccountId();
                newUser.Name = newAccount.AccountName;
                if (!userEntity.insertUser(newUser)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add User fail");
                    return;
                }
                AlertDialogUtils.showSuccessDialog(getContext(), "Register success");
                resetData();
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

    public void resetData() {
        edtUsername.setText("");
        edtPassword.setText("");
    }
}
