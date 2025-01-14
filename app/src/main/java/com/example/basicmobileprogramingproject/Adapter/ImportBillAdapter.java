package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Model.ImportBillModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class ImportBillAdapter extends RecyclerView.Adapter<ImportBillAdapter.ViewHolder> {
    private Context context;
    private List<ImportBillModel> importBilllList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public ImportBillAdapter(Context context, List<ImportBillModel> importBilllList) {
        this.context = context;
        this.importBilllList = importBilllList;
    }

    public void updateImportBillList(List<ImportBillModel> importBilllList) {
        this.importBilllList = importBilllList;
    }

    @NonNull
    @Override
    public ImportBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_import_bill_item, parent, false);
        return new ImportBillAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ImportBillAdapter.ViewHolder holder, int position) {
        ImportBillModel importBill = importBilllList.get(position);
        holder.tvImportBillId.setText(importBill.ImportBillId);
        holder.tvSupplier.setText(importBill.SupplierId);
        holder.tvStaff.setText(importBill.StaffId);
        holder.tvTotalAmount.setText(String.valueOf(importBill.ToTalAmount));
        holder.tvInputDay.setText(importBill.InputDay);
    }

    @Override
    public int getItemCount() {
        return importBilllList.size();
    }

    public void setOnItemClickListener(ImportBillAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvImportBillId, tvSupplier, tvStaff, tvTotalAmount, tvInputDay;

        public ViewHolder(@NonNull View itemView, ImportBillAdapter.OnItemClickListener listener) {
            super(itemView);
            tvImportBillId = itemView.findViewById(R.id.tvImportBillId);
            tvSupplier = itemView.findViewById(R.id.tvSupplierId);
            tvStaff = itemView.findViewById(R.id.tvStaffId);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvInputDay = itemView.findViewById(R.id.tvInputDay);

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