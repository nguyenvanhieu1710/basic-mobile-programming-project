package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Entity.CartEntity;
import com.example.basicmobileprogramingproject.Model.CartModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<CartModel> cartList;
    private OnItemClickListener onItemClickListener;
    private OnItemCheckedChangeListener onItemCheckedChangeListener;
    private OnTotalAmountChangeListener onTotalAmountChangeListener;
    CartEntity cartEntity;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemCheckedChangeListener {
        void onItemCheckedChanged();
    }

    public interface OnTotalAmountChangeListener {
        void onTotalAmountChanged(double totalAmount);
    }

    public CartAdapter(Context context, List<CartModel> cartList) {
        this.context = context;
        this.cartList = cartList;
        cartEntity = new CartEntity(context);
    }

    public void updateCartList(List<CartModel> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel cart = cartList.get(position);
        ProductModel product = cartEntity.getInfoProductInTheCartById(cart.ProductId);

        holder.tvProductName.setText(product.ProductName);
        holder.tvOriginalPrice.setText("Original Price: $" + product.Price);
        holder.ivProductImage.setImageBitmap(BitmapFactory.decodeFile(product.ProductImage));

        // Gọi cập nhật giá tiền dựa trên số lượng sản phẩm.
        updateCalculatedPrice(holder, cart, product);

        holder.etQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.etQuantity.addTextChangedListener(new QuantityTextWatcher(holder, cart, product));

        holder.checkboxProduct.setChecked(cart.isSelected());
        holder.checkboxProduct.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cart.setSelected(isChecked);
            onItemCheckedChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int currentQuantity = cart.getQuantity();
            if (currentQuantity > 1) {
                cart.setQuantity(--currentQuantity);
                holder.etQuantity.setText(String.valueOf(currentQuantity));
                onQuantityChanged(cart, product, holder);
            }
        });

        holder.btnPlus.setOnClickListener(v -> {
            int newQuantity = cart.getQuantity() + 1;
            if (newQuantity > product.Quantity) {
                AlertDialogUtils.showErrorDialog(context, "Quantity exceeds available stock");
            } else {
                cart.setQuantity(newQuantity);
                holder.etQuantity.setText(String.valueOf(newQuantity));
                onQuantityChanged(cart, product, holder);
            }
        });

    }

    private void updateCalculatedPrice(ViewHolder holder, CartModel cart, ProductModel product) {
        double totalPriceOfThisProduct = product.Price * cart.getQuantity();
        cart.setCalculatedPrice(totalPriceOfThisProduct);
        holder.tvCalculatedPrice.setText("Calculated Price: $" + totalPriceOfThisProduct);
    }

    private void onQuantityChanged(CartModel cart, ProductModel product, ViewHolder holder) {
        if (cart.getQuantity() > product.Quantity) {
            AlertDialogUtils.showErrorDialog(context, "Quantity exceeds available stock");
            cart.setQuantity(product.Quantity);
            holder.etQuantity.setText(String.valueOf(product.Quantity));
        } else {
            updateCalculatedPrice(holder, cart, product);
            if (onTotalAmountChangeListener != null) {
                onTotalAmountChangeListener.onTotalAmountChanged(getTotalAmount());
            }
        }
    }

    private void onItemCheckedChanged() {
        if (onItemCheckedChangeListener != null) {
            onItemCheckedChangeListener.onItemCheckedChanged();
        }
        if (onTotalAmountChangeListener != null) {
            onTotalAmountChangeListener.onTotalAmountChanged(getTotalAmount());
        }
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnItemCheckedChangeListener(OnItemCheckedChangeListener listener) {
        this.onItemCheckedChangeListener = listener;
    }

    public void setOnTotalAmountChangeListener(OnTotalAmountChangeListener listener) {
        this.onTotalAmountChangeListener = listener;
    }

    public void selectAll(boolean select) {
        for (CartModel cart : cartList) {
            cart.setSelected(select);
        }
        notifyDataSetChanged();
    }

    public List<CartModel> getSelectedItems() {
        List<CartModel> selectedItems = new ArrayList<>();
        for (CartModel cart : cartList) {
            if (cart.isSelected()) {
                selectedItems.add(cart);
            }
        }
        return selectedItems;
    }

    public double getTotalAmount() {
        double totalAmount = 0;
        for (CartModel cart : cartList) {
            if (cart.isSelected()) {
                ProductModel product = cartEntity.getInfoProductInTheCartById(cart.ProductId);
                int quantity = cart.getQuantity();
                totalAmount += product.Price * quantity;
            }
        }
        return totalAmount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName;
        TextView tvOriginalPrice;
        TextView tvCalculatedPrice;
        ImageButton btnMinus;
        EditText etQuantity;
        ImageButton btnPlus;
        CheckBox checkboxProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvCalculatedPrice = itemView.findViewById(R.id.tvCalculatedPrice);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            etQuantity = itemView.findViewById(R.id.etQuantity);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            checkboxProduct = itemView.findViewById(R.id.checkboxProduct);

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
    private class QuantityTextWatcher implements TextWatcher {
        private ViewHolder holder;
        private CartModel cart;
        private ProductModel product;

        public QuantityTextWatcher(ViewHolder holder, CartModel cart, ProductModel product) {
            this.holder = holder;
            this.cart = cart;
            this.product = product;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String quantityStr = s.toString();
            if (!quantityStr.isEmpty()) {
                int newQuantity = Integer.parseInt(quantityStr);
                if (newQuantity > product.Quantity) {
                    AlertDialogUtils.showErrorDialog(context, "Quantity exceeds available stock");
                    cart.setQuantity(product.Quantity);
                    holder.etQuantity.setText(String.valueOf(product.Quantity));
                } else {
                    cart.setQuantity(newQuantity);
                    onQuantityChanged(cart, product, holder);
                }
            }
        }

    }
}
