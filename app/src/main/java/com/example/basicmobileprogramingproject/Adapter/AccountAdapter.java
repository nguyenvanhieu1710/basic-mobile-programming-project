package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;

public class AccountAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<AccountModel> accountList;

    public AccountAdapter(Context context, int layout, List<AccountModel> accountList) {
        this.context = context;
        this.layout = layout;
        this.accountList = accountList;
    }

    @Override
    public int getCount() {
        return accountList.size();
    }

    @Override
    public Object getItem(int position) {
        return accountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.tvAccountName = convertView.findViewById(R.id.tvAccountName);
            holder.tvAccountId = convertView.findViewById(R.id.tvAccountId);
            holder.tvPassword = convertView.findViewById(R.id.tvPassword);
            holder.tvRole = convertView.findViewById(R.id.tvRole);
            holder.tvEmail = convertView.findViewById(R.id.tvEmail);
            holder.tvStatus = convertView.findViewById(R.id.tvStatus);
            holder.tvDayCreated = convertView.findViewById(R.id.tvDayCreated);
            holder.cbRememberPassword = convertView.findViewById(R.id.cbRememberPassword);
            holder.cbDeleted = convertView.findViewById(R.id.cbDeleted);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        AccountModel account = accountList.get(position);

        holder.tvAccountName.setText("Account Name: " + account.AccountName);
        holder.tvAccountId.setText("Account Id: " + String.valueOf(account.AccountId));
        holder.tvPassword.setText("Password: " + account.Password);
        holder.tvRole.setText("Role: " + account.Role);
        holder.tvEmail.setText("Email: " + account.Email);
        holder.tvStatus.setText("Status: " + account.Status);
        holder.tvDayCreated.setText("Day Created: " + account.DayCreated);
        holder.cbRememberPassword.setChecked(account.RememberPassword);
        holder.cbDeleted.setChecked(account.Deleted);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvAccountName;
        TextView tvAccountId;
        TextView tvPassword;
        TextView tvRole;
        TextView tvEmail;
        TextView tvStatus;
        TextView tvDayCreated;
        CheckBox cbRememberPassword;
        CheckBox cbDeleted;
    }
}
