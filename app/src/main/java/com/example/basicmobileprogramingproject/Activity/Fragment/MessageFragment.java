package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.basicmobileprogramingproject.Adapter.AccountAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class MessageFragment extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.account_list, container, false);
        // mapping id

        AccountEntity accountEntity = new AccountEntity(getContext());
        ArrayList<AccountModel> accountList = accountEntity.getAccountList();
        if (accountList.isEmpty()) {
            AlertDialogUtils.showInfoDialog(getContext(), "No account found");
        }

        ListView lvAccounts = view.findViewById(R.id.lvAccounts);
        AccountAdapter adapter = new AccountAdapter(getContext(), R.layout.account_item, accountList);
        lvAccounts.setAdapter(adapter);

        return view;
    }
}
