package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    private Context context;
    private List<AccountModel> accountList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AccountAdapter(Context context, List<AccountModel> accountList) {
        this.context = context;
        this.accountList = accountList;
    }

    public void updateAccountList(List<AccountModel> accountList) {
        this.accountList = accountList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_account_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountModel account = accountList.get(position);
        holder.tvAccountName.setText("Account Name: " + account.AccountName);
        holder.tvAccountId.setText("Account Id: " + account.AccountId);
        holder.tvPassword.setText("Password: " + account.Password);
        holder.tvRole.setText("Role: " + account.Role);
        holder.tvEmail.setText("Email: " + account.Email);
        holder.tvStatus.setText("Status: " + account.Status);
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAccountName, tvAccountId, tvPassword, tvRole, tvEmail, tvStatus;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            tvAccountId = itemView.findViewById(R.id.tvAccountId);
            tvAccountName = itemView.findViewById(R.id.tvAccountName);
            tvPassword = itemView.findViewById(R.id.tvPassword);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
