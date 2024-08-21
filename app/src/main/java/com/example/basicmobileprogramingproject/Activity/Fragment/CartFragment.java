package com.example.basicmobileprogramingproject.Activity.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.TextView;

import com.example.basicmobileprogramingproject.Adapter.CartAdapter;
import com.example.basicmobileprogramingproject.Entity.CartEntity;
import com.example.basicmobileprogramingproject.Model.CartModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    View view;
    Button btnCheckout;
    EditText edtSearch;
    ImageView btnClear;
    ImageButton btnSearch;
    CheckBox checkboxSelectAll;
    RecyclerView recyclerViewProductsInTheCart;
    TextView textViewTotalAmount;
    CartAdapter cartAdapter;
    CartEntity cartEntity;
    ArrayList<CartModel> cartList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_fragment, container, false);

        // mapping id
        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        checkboxSelectAll = view.findViewById(R.id.checkboxSelectAll);
        recyclerViewProductsInTheCart = view.findViewById(R.id.recyclerViewProductsInTheCart);
        textViewTotalAmount = view.findViewById(R.id.textViewTotalAmount);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        cartEntity = new CartEntity(getContext());
        cartList = cartEntity.getCartList();

        cartAdapter = new CartAdapter(getContext(), cartList);
        cartAdapter.setOnTotalAmountChangeListener(totalAmount ->
                textViewTotalAmount.setText("Total Amount: $" + totalAmount)
        );

        recyclerViewProductsInTheCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewProductsInTheCart.setAdapter(cartAdapter);

        handleSearch();
        handleEventsClickButton();
        handleSelectAll();
        setupItemCheckedChangeListener();

        return view;
    }

    public void handleSearch() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                reloadData();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKeyword = edtSearch.getText().toString().trim();
                if (searchKeyword.isEmpty()) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Please enter search keyword");
                    return;
                }
                ArrayList<CartModel> cartListAfterSearch = cartEntity.getCartListAfterSearch(searchKeyword);
                cartAdapter.updateCartList(cartListAfterSearch);
                cartAdapter.notifyDataSetChanged();
            }
        });
    }

    public void reloadData() {
        cartList = cartEntity.getCartList();
        cartAdapter.updateCartList(cartList);
        cartAdapter.notifyDataSetChanged();
    }

    public void handleEventsClickButton() {
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtils.showQuestionDialog(getContext(), "Do you want to checkout?",
                        (dialog, which) -> {
                            List<CartModel> selectedItems = cartAdapter.getSelectedItems();
                            if (selectedItems.isEmpty()) {
                                AlertDialogUtils.showErrorDialog(getContext(), "Please select at least one item");
                                return;
                            }
                            List<CartModel> cartList = getSelectedProductsInfo();
                            PayFragment payFragment = new PayFragment();
                            payFragment.getProductsInfo(cartList);
                            replaceFragment(payFragment);
                        },
                        (dialog, which) -> {
                        });
            }
        });
    }

    public void handleSelectAll() {
        checkboxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            cartAdapter.selectAll(isChecked);
        });
        updateTotalAmount();
    }

    private void setupItemCheckedChangeListener() {
        cartAdapter.setOnItemCheckedChangeListener(() -> {
            // Cập nhật trạng thái "Select All" dựa trên trạng thái của các item
            List<CartModel> selectedItems = cartAdapter.getSelectedItems();
            boolean allSelected = (selectedItems.size() == cartList.size());
            checkboxSelectAll.setChecked(allSelected);
        });
    }

    private void updateTotalAmount() {
        double totalAmount = cartAdapter.getTotalAmount();
        textViewTotalAmount.setText("Total Amount: $" + totalAmount);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public List<CartModel> getSelectedProductsInfo() {
        List<CartModel> selectedProducts = new ArrayList<>();

        for (CartModel cart : cartList) {
            if (cart.isSelected()) {
                ProductModel product = cartEntity.getInfoProductInTheCartById(cart.ProductId);
                cart.setCalculatedPrice(product.Price * cart.getQuantity());

                selectedProducts.add(cart);
            }
        }
        return selectedProducts;
    }

}
