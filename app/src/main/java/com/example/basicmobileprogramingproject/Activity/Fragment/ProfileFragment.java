package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

public class ProfileFragment extends Fragment {
    private View view;
    private ImageView imgProfile;
    private ImageButton btnEditImage;
    private TextView tvName, tvUserId, tvBirthdayLabel, tvPhoneNumberLabel, tvGenderLabel, tvAddressLabel, tvAccountStatusLabel;
    private EditText edtBirthday, edtPhoneNumber, edtAddress;
    private Spinner spinnerGender;
    private Switch switchDeleted;
    private Button btnSaveProfile;
    AccountEntity accountEntity;
    UserEntity userEntity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        // Mapping IDs
        imgProfile = view.findViewById(R.id.imgProfile);
        btnEditImage = view.findViewById(R.id.btnEditImage);
        tvName = view.findViewById(R.id.tvName);
        tvUserId = view.findViewById(R.id.tvUserId);
        tvBirthdayLabel = view.findViewById(R.id.tvBirthdayLabel);
        edtBirthday = view.findViewById(R.id.edtBirthday);
        tvPhoneNumberLabel = view.findViewById(R.id.tvPhoneNumberLabel);
        edtPhoneNumber = view.findViewById(R.id.edtPhoneNumber);
        tvGenderLabel = view.findViewById(R.id.tvGenderLabel);
        spinnerGender = view.findViewById(R.id.spinnerGender);
        tvAddressLabel = view.findViewById(R.id.tvAddressLabel);
        edtAddress = view.findViewById(R.id.edtAddress);
        tvAccountStatusLabel = view.findViewById(R.id.tvAccountStatusLabel);
        switchDeleted = view.findViewById(R.id.switchDeleted);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        accountEntity = new AccountEntity(getContext());
        userEntity = new UserEntity(getContext());

        setupGenderSpinner();

        uploadDataToView();

        handleEventsClickButton();

        return view;
    }

    public void uploadDataToView() {
        AccountModel onlineAccount = accountEntity.getOnlineAccount();
        ArrayList<UserModel> userList = userEntity.getUserList();
        // format for spinner gender
        String[] genders = {"Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);
        for (UserModel user : userList) {
            if (user.UserId == onlineAccount.AccountId) {
                tvName.setText(user.Name);
                tvUserId.setText("User ID: " + user.UserId);
                edtBirthday.setText(user.Birthday.toString());
                edtPhoneNumber.setText(user.PhoneNumber);

                int position = genderAdapter.getPosition(user.Gender);
                spinnerGender.setSelection(position);

                edtAddress.setText(user.Address);
                switchDeleted.setChecked(true);
            }
        }
    }

    public void handleEventsClickButton() {
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                UserModel userModel = new UserModel();
                userModel.Name = tvName.getText().toString();
                userModel.Birthday = edtBirthday.getText().toString();
                userModel.PhoneNumber = edtPhoneNumber.getText().toString();
                userModel.Gender = spinnerGender.getSelectedItem().toString();
                userModel.Address = edtAddress.getText().toString();
                userModel.Deleted = false;
                if (userEntity.updateUser(userModel)) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update profile success");
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update profile fail");
                }
            }
        });
    }

    public boolean validateData() {
        if (tvName.getText().toString().trim().isEmpty()) {
            tvName.setError("Please enter your name");
            return false;
        }
        if (edtBirthday.getText().toString().trim().isEmpty()) {
            edtBirthday.setError("Please enter your birthday");
            return false;
        }
        if (!isValidBirthday(edtBirthday.getText().toString().trim())) {
            edtBirthday.setError("Please enter a valid birthday (dd/MM/yyyy)");
            return false;
        }
        if (edtPhoneNumber.getText().toString().trim().isEmpty()) {
            edtPhoneNumber.setError("Please enter your phone number");
            return false;
        }
        if (!isValidPhoneNumber(edtPhoneNumber.getText().toString().trim())) {
            edtPhoneNumber.setError("Please enter a valid phone number. Must be 10 digits.");
            return false;
        }
        if (spinnerGender.getSelectedItemPosition() == 0) {
            tvGenderLabel.setError("Please select your gender");
            return false;
        }
        if (edtAddress.getText().toString().trim().isEmpty()) {
            edtAddress.setError("Please enter your address");
            return false;
        }
        if (switchDeleted.isChecked()) {
            tvAccountStatusLabel.setError("Please select your account status");
            return false;
        }
        return true;
    }

    private boolean isValidBirthday(String birthday) {
        Date date = DateUtils.parseDate(birthday);
        if (date == null) {
            return false;
        }
        return true;
    }


    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^[0-9]{10,}$");
    }

    private void setupGenderSpinner() {
        String[] genders = {"Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_item,
                genders
        );

        spinnerGender.setAdapter(adapter);
    }

}
