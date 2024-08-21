package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Entity.OrderEntity;
import com.example.basicmobileprogramingproject.Entity.UserEntity;
import com.example.basicmobileprogramingproject.Model.OrderModel;
import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> orderList;
    private OnItemClickListener onItemClickListener;
    UserEntity userEntity;
    OrderEntity orderEntity;
    String role;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public OrderAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    public void getRole(String role){
        this.role = role;
    }

    public void updateOrderList(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false);

        userEntity = new UserEntity(context);
        orderEntity = new OrderEntity(context);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orderList.get(position);
        UserModel userModel = userEntity.getUserById(order.UserId);
        holder.tvUserName.setText(userModel.Name);
        holder.tvTotalAmount.setText("Total Amount: $" + String.valueOf(order.TotalAmount));
//        holder.ivOrderImage.setImageBitmap(BitmapFactory.decodeFile(order.UserImage));
        if (role.equals("Staff")) {
            holder.btnConfirm.setText("Confirm");
            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    order.OrderStatus = "Received the goods";
                    if (orderEntity.updateOrder(order)) {
                        AlertDialogUtils.showSuccessDialog(context, "Confirm successfully");
                        updateOrderList(orderEntity.getOrderForStaff());
                        notifyDataSetChanged();
                    } else {
                        AlertDialogUtils.showErrorDialog(context, "Confirm failed");
                    }
                }
            });
        } else if (role.equals("User")) {
            holder.btnConfirm.setText("Rate");
            holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialogUtils.showInfoDialog(context, "Function not available");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivOrderImage;
        TextView tvUserName;
        TextView tvTotalAmount;
        Button btnConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivOrderImage = itemView.findViewById(R.id.ivOrderImage);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);

            // Gán sự kiện click cho itemView
            itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }
}

