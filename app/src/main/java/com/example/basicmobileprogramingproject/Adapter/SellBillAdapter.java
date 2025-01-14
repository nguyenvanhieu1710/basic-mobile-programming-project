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

import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class SellBillAdapter extends RecyclerView.Adapter<SellBillAdapter.ViewHolder> {
    private Context context;
    private List<OrderModel> sellBilllList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public SellBillAdapter(Context context, List<OrderModel> sellBilllList) {
        this.context = context;
        this.sellBilllList = sellBilllList;
    }

    public void updateSellBillList(List<OrderModel> sellBilllList) {
        this.sellBilllList = sellBilllList;
    }

    @NonNull
    @Override
    public SellBillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_sell_bill_item, parent, false);
        return new SellBillAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SellBillAdapter.ViewHolder holder, int position) {
        OrderModel sellBill = sellBilllList.get(position);
        holder.tvOrderId.setText("Order ID: " + sellBill.OrderId);
        holder.tvUserId.setText("User ID: " + sellBill.UserId);
        holder.tvStaffId.setText("Staff ID: " + sellBill.StaffId);
        holder.tvTotalAmount.setText("Total Amount: " + sellBill.TotalAmount);
        holder.tvOrderStatus.setText("Order Status: " + sellBill.OrderStatus);
        holder.tvDeliveryAddress.setText("Delivery Address: " + sellBill.DeliveryAddress);
    }

    @Override
    public int getItemCount() {
        return sellBilllList.size();
    }

    public void setOnItemClickListener(SellBillAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvUserId, tvStaffId, tvTotalAmount, tvOrderStatus, tvDeliveryAddress;

        public ViewHolder(@NonNull View itemView, SellBillAdapter.OnItemClickListener listener) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvUserId = itemView.findViewById(R.id.tvUserId);
            tvStaffId = itemView.findViewById(R.id.tvStaffId);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvDeliveryAddress = itemView.findViewById(R.id.tvDeliveryAddress);

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