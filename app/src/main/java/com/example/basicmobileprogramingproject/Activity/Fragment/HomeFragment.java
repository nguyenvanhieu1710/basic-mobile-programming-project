package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.CategoryAdapter;
import com.example.basicmobileprogramingproject.Adapter.ProductAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.CartEntity;
import com.example.basicmobileprogramingproject.Entity.CategoryEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Entity.ProductEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.CartModel;
import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    View view;
    TextView txtProductName, txtProductPrice, txtProductCategory, txtProductBrand, txtProductQuantity, txtProductDescription;
    ImageView imgProduct, btnClear, btnExit;
    ImageButton btnSearch;
    EditText edtSearch;
    Button btnAddToCart;
    LinearLayout linearLayoutProductDetail;
    RecyclerView recyclerViewProduct, recyclerViewCategory;
    ProductAdapter productAdapter;
    ProductEntity productEntity;
    ArrayList<ProductModel> productList;
    CategoryAdapter categoryAdapter;
    CategoryEntity categoryEntity;
    ArrayList<CategoryModel> categoryList;
    CartEntity cartEntity;
    AccountEntity accountEntity;
    DatabaseHandler databaseHandler;
    int clickedProductId;
    int clickedCategoryId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        // mapping id
        imgProduct = view.findViewById(R.id.imgProduct);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductPrice = view.findViewById(R.id.txtProductPrice);
        txtProductCategory = view.findViewById(R.id.txtProductCategory);
        txtProductBrand = view.findViewById(R.id.txtProductBrand);
        txtProductQuantity = view.findViewById(R.id.txtProductQuantity);
        txtProductDescription = view.findViewById(R.id.txtProductDescription);
        btnAddToCart = view.findViewById(R.id.btnAddToCart);
        handleEventsClickButton();

        btnExit = view.findViewById(R.id.btnExit);

        linearLayoutProductDetail = view.findViewById(R.id.linearLayoutProductDetail);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        handleSearch();

        // call database
        databaseHandler = new DatabaseHandler(getContext());
//        databaseHandler.getDatabasePath();
//        databaseHandler.deleteDatabase();
        try {
            databaseHandler.createDatabase();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        accountEntity = new AccountEntity(getContext());

        cartEntity = new CartEntity(getContext());

        // get Product list
        productEntity = new ProductEntity(getContext());
        productList = productEntity.getProductList();
//        if (productList.isEmpty()) {
//            AlertDialogUtils.showErrorDialog(getContext(), "No product found");
//        }

        // call adapter and event click item
        productAdapter = new ProductAdapter(getContext(), productList);
        handleSelectProduct();

        //       assign data up recyclerView
        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct);
        uploadDataToRecyclerViewProduct();

        // get Category list
        categoryEntity = new CategoryEntity(getContext());
        categoryList = categoryEntity.getCategoryList();
//        if (categoryList.isEmpty()) {
//            AlertDialogUtils.showErrorDialog(getContext(), "No category found");
//        }

        // call adapter and event click item
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        handleSelectCategory();
        uploadDataToRecyclerViewCategory();

        defaultLayout();
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutProductDetail.setVisibility(View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void defaultLayout() {
        linearLayoutProductDetail.setVisibility(View.GONE);
    }

    public void handleSearch() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchKeyword = edtSearch.getText().toString().trim();
                if (searchKeyword.isEmpty()) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Please enter search keyword");
                    return;
                }
                ArrayList<ProductModel> searchedProductsList = productEntity.getSearchedProductsList(searchKeyword);
                productAdapter.updateProductList(searchedProductsList);
                productAdapter.notifyDataSetChanged();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
                reloadData();
            }
        });
    }

    public void reloadData() {
        productList = productEntity.getProductList();
        productAdapter.updateProductList(productList);
        productAdapter.notifyDataSetChanged();

        categoryList = categoryEntity.getCategoryList();
        categoryAdapter.updateCategoryList(categoryList);
        categoryAdapter.notifyDataSetChanged();
    }

    public void handleSelectProduct() {
        productAdapter.setOnItemClickListener(position -> {
            linearLayoutProductDetail.setVisibility(View.VISIBLE);
            ProductModel clickedProduct = productList.get(position);
            clickedProductId = clickedProduct.ProductId;
            clickedCategoryId = clickedProduct.CategoryId;
            txtProductName.setText(clickedProduct.ProductName);
            txtProductPrice.setText("Price: $" + clickedProduct.Price);
            txtProductCategory.setText("Category: " + clickedProduct.CategoryId);
            txtProductBrand.setText("Brand: " + clickedProduct.Brand);
            txtProductQuantity.setText("Quantity: " + clickedProduct.Quantity);
            txtProductDescription.setText("Description: " + clickedProduct.Description);
            imgProduct.setImageBitmap(BitmapFactory.decodeFile(clickedProduct.ProductImage));
        });
    }

    public void handleSelectCategory() {
        categoryAdapter.setOnItemClickListener(position -> {
            CategoryModel clickedCategory = categoryList.get(position);
            clickedCategoryId = clickedCategory.CategoryId;
            ArrayList<ProductModel> productsByCategory = productEntity.getProductsByCategoryId(clickedCategoryId);
            productAdapter = new ProductAdapter(getContext(), productsByCategory);
            uploadDataToRecyclerViewProduct();
        });
    }

    public void uploadDataToRecyclerViewProduct() {
        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(layoutManager);
        recyclerViewProduct.setAdapter(productAdapter);
    }

    public void uploadDataToRecyclerViewCategory() {
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(categoryAdapter);
    }

    public void handleEventsClickButton() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check account online
                if (!accountEntity.checkAccountActive()) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Please login to continue");
                    return;
                }
                // get online account
                AccountModel onlineAccount = accountEntity.getOnlineAccount();
                CartModel cartModel = new CartModel();
                cartModel.UserId = onlineAccount.AccountId;
                cartModel.ProductId = clickedProductId;
                if (cartEntity.checkExist(cartModel)) {
                    AlertDialogUtils.showErrorDialog(getContext(), "Product already in cart");
                    return;
                }
                boolean isCheck = cartEntity.insertCart(cartModel);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add to cart success");
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add to cart failed");
                }
            }
        });
    }
}
