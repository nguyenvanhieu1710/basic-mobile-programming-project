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

import com.example.basicmobileprogramingproject.Model.SupplierModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class SupplierAdapter extends RecyclerView.Adapter<SupplierAdapter.ViewHolder> {
    private Context context;
    private List<SupplierModel> supplierlList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public SupplierAdapter(Context context, List<SupplierModel> supplierlList) {
        this.context = context;
        this.supplierlList = supplierlList;
    }

    public void updateSupplierList(List<SupplierModel> supplierlList) {
        this.supplierlList = supplierlList;
    }

    @NonNull
    @Override
    public SupplierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new SupplierAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplierAdapter.ViewHolder holder, int position) {
        SupplierModel supplier = supplierlList.get(position);
        holder.textViewSupplierName.setText(supplier.SupplierName);
        holder.imageViewSupplier.setImageResource(R.drawable.icon_user_white);
//        holder.textViewSupplierBirthday.setText(supplier.PhoneNumber);
//        holder.imageViewSupplier.setImageBitmap(BitmapFactory.decodeFile(supplier.ImagePath));
    }

    @Override
    public int getItemCount() {
        return supplierlList.size();
    }

    public void setOnItemClickListener(SupplierAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewSupplier;
        TextView textViewSupplierName;
        TextView textViewSupplierBirthday;

        public ViewHolder(@NonNull View itemView, SupplierAdapter.OnItemClickListener listener) {
            super(itemView);
            imageViewSupplier = itemView.findViewById(R.id.user_image);
            textViewSupplierName = itemView.findViewById(R.id.user_name);
            textViewSupplierBirthday = itemView.findViewById(R.id.birthday_of_user);

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