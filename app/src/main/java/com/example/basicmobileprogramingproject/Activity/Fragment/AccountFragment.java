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

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.AccountAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class AccountFragment extends Fragment {
    View view;
    private LinearLayout layoutSearchBar, accountActionButtons;
    private EditText edtSearch;
    private ImageView btnClear;
    private ImageButton btnSearch;
    private RecyclerView rvAccountList;
    private Button btnAddAccount, btnEditAccount, btnDeleteAccount;
    AccountEntity accountEntity;
    ArrayList<AccountModel> accountList;
    AccountAdapter accountAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_account_management, container, false);
        // mapping id
        layoutSearchBar = view.findViewById(R.id.layout_search_bar);
        rvAccountList = view.findViewById(R.id.rv_account_list);
        accountActionButtons = view.findViewById(R.id.account_action_buttons);
        btnAddAccount = view.findViewById(R.id.btn_add_account);
        btnEditAccount = view.findViewById(R.id.btn_edit_account);
        btnDeleteAccount = view.findViewById(R.id.btn_delete_account);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        handleSearch();

        accountEntity = new AccountEntity(getContext());
        accountList = accountEntity.getAccountList();
        if (accountList.isEmpty()) {
            AlertDialogUtils.showInfoDialog(getContext(), "No account found");
        }

        accountAdapter = new AccountAdapter(getContext(), accountList);
        uploadDataToRecyclerViewAccount();

        return view;
    }

    public void uploadDataToRecyclerViewAccount() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        rvAccountList.setLayoutManager(layoutManager);
        rvAccountList.setAdapter(accountAdapter);
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
}
