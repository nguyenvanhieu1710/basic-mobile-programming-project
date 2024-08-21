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

import com.example.basicmobileprogramingproject.Model.VoucherModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.ViewHolder> {
    private Context context;
    private List<VoucherModel> voucherlList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public VoucherAdapter(Context context, List<VoucherModel> voucherlList) {
        this.context = context;
        this.voucherlList = voucherlList;
    }

    public void updateVoucherList(List<VoucherModel> voucherlList) {
        this.voucherlList = voucherlList;
    }

    @NonNull
    @Override
    public VoucherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.voucher_item, parent, false);
        return new VoucherAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherAdapter.ViewHolder holder, int position) {
        VoucherModel voucher = voucherlList.get(position);
        holder.textViewVoucherName.setText(voucher.VoucherName);
    }

    @Override
    public int getItemCount() {
        return voucherlList.size();
    }

    public void setOnItemClickListener(VoucherAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewVoucher;
        TextView textViewVoucherName;

        public ViewHolder(@NonNull View itemView, VoucherAdapter.OnItemClickListener listener) {
            super(itemView);
            imageViewVoucher = itemView.findViewById(R.id.imageViewVoucher);
            textViewVoucherName = itemView.findViewById(R.id.textViewVoucherName);

            // Gán sự kiện click cho itemView
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