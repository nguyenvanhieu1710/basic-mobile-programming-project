package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Entity.CartEntity;
import com.example.basicmobileprogramingproject.Model.CartModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> cartList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public PayAdapter(Context context, List<CartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    public void updatePayList(List<CartModel> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_of_pay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel cartModel = cartList.get(position);
        CartEntity cartEntity = new CartEntity(context);
        ProductModel productModel = cartEntity.getInfoProductInTheCartById(cartModel.ProductId);
        holder.tvProductName.setText(productModel.ProductName);
        holder.tvQuantity.setText("Quantity: " + String.valueOf(cartModel.getQuantity()));
        holder.tvCalculatedPrice.setText("Calculated price: $" + String.valueOf(cartModel.getCalculatedPrice()));
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvProductName;
        TextView tvQuantity;
        TextView tvCalculatedPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvCalculatedPrice = itemView.findViewById(R.id.tvCalculatedPrice);

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

