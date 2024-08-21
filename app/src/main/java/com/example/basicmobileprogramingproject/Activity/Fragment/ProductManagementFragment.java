package com.example.basicmobileprogramingproject.Activity.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.CustomCategoryAdapter;
import com.example.basicmobileprogramingproject.Adapter.ProductAdapter;
import com.example.basicmobileprogramingproject.Entity.CategoryEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Entity.ProductEntity;
import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ProductManagementFragment extends Fragment {
    TextView badgeNumberOfProductsDeleted, txtProductName, txtProductPrice, txtProductCategory, txtProductBrand, txtProductQuantity, txtProductDescription, txtDeletedProductName;
    EditText edtSearch, edtProductName, edtQuantity, edtPrice, edtDescription, edtBrand, edtProductDetail;
    ImageButton btnTurnOnBlockAddProduct, btnTurnOnBlockDeleteProduct;
    Button btnAddNewProduct, btnFixProduct, btnEditProduct, btnDeleteProduct, btnTurnOnBlockEditProduct, btnSelectProductImage, btnRestoreProduct, btnDeleteActualProduct;
    ImageButton btnSearch;
    ImageView btnClear;
    ImageView imgProduct;
    Spinner spinnerCategoryId;
    LinearLayout linearLayoutAddAndEditProduct, linearLayoutProductDetail, linearLayoutDeleteAndRestoreProduct, linearLayoutDeletedProductDetail;
    RecyclerView recyclerViewProduct, recyclerViewProductHasBeenDeleted;
    ProductAdapter productAdapter, productListingHasBeenDeletedAdapter;
    ProductEntity productEntity;
    ArrayList<ProductModel> productList, productListingHasBeenDeleted;
    CategoryEntity categoryEntity;
    ArrayList<CategoryModel> categoryList;
    DatabaseHandler databaseHandler;
    View view;
    int clickedProductId;
    int selectedCategoryId;
    static final int PICK_IMAGE = 1;
    ImageView imgProductImage;
    Uri imageUri;
    String imagePath;
    String imageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_fragment, container, false);

        // mapping id
        clickedProductId = 0;
        selectedCategoryId = 0;
        imageName = "";
        edtProductName = view.findViewById(R.id.edtProductName);
        edtQuantity = view.findViewById(R.id.edtQuantity);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtDescription = view.findViewById(R.id.edtDescription);
        edtBrand = view.findViewById(R.id.edtBrand);
        edtProductDetail = view.findViewById(R.id.edtProductDetail);

        imgProduct = view.findViewById(R.id.imgProduct);
        txtProductName = view.findViewById(R.id.txtProductName);
        txtProductPrice = view.findViewById(R.id.txtProductPrice);
        txtProductCategory = view.findViewById(R.id.txtProductCategory);
        txtProductBrand = view.findViewById(R.id.txtProductBrand);
        txtProductQuantity = view.findViewById(R.id.txtProductQuantity);
        txtProductDescription = view.findViewById(R.id.txtProductDescription);

        txtDeletedProductName = view.findViewById(R.id.txtDeletedProductName);
        btnRestoreProduct = view.findViewById(R.id.btnRestoreProduct);
        btnDeleteActualProduct = view.findViewById(R.id.btnDeleteActualProduct);

        imgProductImage = view.findViewById(R.id.imgProductImage);
        btnSelectProductImage = view.findViewById(R.id.btnSelectProductImage);
        handleSelectProductImage();

        linearLayoutAddAndEditProduct = view.findViewById(R.id.linearLayoutAddAndEditProduct);
        linearLayoutProductDetail = view.findViewById(R.id.linearLayoutProductDetail);
        linearLayoutDeleteAndRestoreProduct = view.findViewById(R.id.linearLayoutDeleteAndRestoreProduct);
        linearLayoutDeletedProductDetail = view.findViewById(R.id.linearLayoutDeletedProductDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());

//        databaseHandler.getDatabasePath();
//        try {
//            databaseHandler.createDatabase();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

// hide layout add product and layout product detail
        linearLayoutAddAndEditProduct.setVisibility(View.GONE);
        linearLayoutProductDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreProduct.setVisibility(View.GONE);
        linearLayoutDeletedProductDetail.setVisibility(View.GONE);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        handleSearch();

        // get Category list
        categoryEntity = new CategoryEntity(getContext());
        categoryList = categoryEntity.getCategoryList();

        // get Product list
        productEntity = new ProductEntity(getContext());
        productList = productEntity.getProductList();
        productListingHasBeenDeleted = productEntity.getProductListingHasBeenDeleted();

        // call adapter and event click item
        productAdapter = new ProductAdapter(getContext(), productList);
        productListingHasBeenDeletedAdapter = new ProductAdapter(getContext(), productListingHasBeenDeleted);
        handleSelectProduct();

//       assign data up recyclerView
        recyclerViewProduct = view.findViewById(R.id.recyclerViewProduct);
        recyclerViewProductHasBeenDeleted = view.findViewById(R.id.recyclerViewProductHasBeenDeleted);
        uploadDataToRecyclerViewProduct();

        spinnerCategoryId = view.findViewById(R.id.spinnerCategoryId);
        uploadDataToSpinner();

        uploadNumberOfProductsDeleted();

        btnDeleteProduct = view.findViewById(R.id.btnDeleteProduct);
        btnTurnOnBlockEditProduct = view.findViewById(R.id.btnTurnOnBlockEditProduct);
        btnAddNewProduct = view.findViewById(R.id.btnAddNewProduct);
        btnEditProduct = view.findViewById(R.id.btnFixProduct);
        btnFixProduct = view.findViewById(R.id.btnFixProduct);
        btnTurnOnBlockAddProduct = view.findViewById(R.id.btnTurnOnBlockAddProduct);
        btnTurnOnBlockDeleteProduct = view.findViewById(R.id.btnTurnOnBlockDeleteProduct);
        handleEventsClickButton();

        return view;
    }

    public ProductModel assignData() {
        ProductModel productModel = new ProductModel();
        productModel.ProductId = clickedProductId;
        productModel.ProductName = edtProductName.getText().toString();
        productModel.Quantity = Integer.parseInt(edtQuantity.getText().toString());
        productModel.Price = Double.parseDouble(edtPrice.getText().toString());
        productModel.Description = edtDescription.getText().toString();
        productModel.Brand = edtBrand.getText().toString();
        productModel.ProductImage = imagePath;
        productModel.ProductDetail = edtProductDetail.getText().toString();
//        AlertDialogUtils.showInfoDialog(getContext(), "Check category id: " + selectedCategoryId);
        productModel.CategoryId = selectedCategoryId;
        return productModel;
    }

    public boolean validateData() {
        if (imagePath == null) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please select image");
            return false;
        }
        if (edtProductName.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter product name");
            return false;
        }
        if (edtQuantity.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter quantity");
            return false;
        }
        if (edtPrice.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter price");
            return false;
        }
        if (edtDescription.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter description");
            return false;
        }
        if (edtBrand.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter brand");
            return false;
        }
        if (edtProductDetail.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter product detail");
            return false;
        }
        if (selectedCategoryId == 0) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please select category");
            return false;
        }
        return true;
    }

    public void handleSelectProduct() {
        productAdapter.setOnItemClickListener(position -> {
            ProductModel clickedProduct = productList.get(position);
            linearLayoutProductDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditProduct.setVisibility(View.GONE);

            clickedProductId = clickedProduct.ProductId;
            imgProduct.setImageBitmap(BitmapFactory.decodeFile(clickedProduct.ProductImage));
            txtProductName.setText(clickedProduct.ProductName);
            txtProductPrice.setText("Price: $" + String.valueOf(clickedProduct.Price));
            txtProductCategory.setText("Category: " + String.valueOf(clickedProduct.CategoryId));
            txtProductBrand.setText("Brand: " + clickedProduct.Brand);
            txtProductQuantity.setText("Quantity: " + String.valueOf(clickedProduct.Quantity));
            txtProductDescription.setText("Description: " + clickedProduct.Description);

            imgProductImage.setImageBitmap(BitmapFactory.decodeFile(clickedProduct.ProductImage));
            edtProductName.setText(clickedProduct.ProductName);
            edtQuantity.setText(String.valueOf(clickedProduct.Quantity));
            edtPrice.setText(String.valueOf(clickedProduct.Price));
            edtDescription.setText(clickedProduct.Description);
            edtBrand.setText(clickedProduct.Brand);
            edtProductDetail.setText(clickedProduct.ProductDetail);
        });

        productListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            ProductModel clickedProduct = productListingHasBeenDeleted.get(position);
            linearLayoutDeletedProductDetail.setVisibility(View.VISIBLE);
            linearLayoutProductDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditProduct.setVisibility(View.GONE);

//            AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedProduct.ProductId + " " + clickedProduct.ProductName);

            clickedProductId = clickedProduct.ProductId;
            txtDeletedProductName.setText(clickedProduct.ProductName);

            imgProductImage.setImageBitmap(BitmapFactory.decodeFile(clickedProduct.ProductImage));
            edtProductName.setText(clickedProduct.ProductName);
            edtQuantity.setText(String.valueOf(clickedProduct.Quantity));
            edtPrice.setText(String.valueOf(clickedProduct.Price));
            edtDescription.setText(clickedProduct.Description);
            edtBrand.setText(clickedProduct.Brand);
            edtProductDetail.setText(clickedProduct.ProductDetail);
        });
    }

    public void uploadDataToRecyclerViewProduct() {
        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProduct.setLayoutManager(layoutManager);
        recyclerViewProduct.setAdapter(productAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewProductHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewProductHasBeenDeleted.setAdapter(productListingHasBeenDeletedAdapter);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void uploadDataToSpinner() {
        // Sử dụng CustomCategoryAdapter thay vì ArrayAdapter
        CustomCategoryAdapter categoryAdapter = new CustomCategoryAdapter(
                requireContext(),
                categoryList,
                0 // viewType là 0 cho Spinner
        );

        spinnerCategoryId.setAdapter(categoryAdapter);

        // Handle item selection
        spinnerCategoryId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CategoryModel selectedCategory = (CategoryModel) parent.getItemAtPosition(position);
                selectedCategoryId = selectedCategory.CategoryId;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý trường hợp không chọn item nào
            }
        });
    }

    public void reloadData(){
        productList = productEntity.getProductList();
        productAdapter.updateProductList(productList);
        productAdapter.notifyDataSetChanged();

        productListingHasBeenDeleted = productEntity.getProductListingHasBeenDeleted();
        productListingHasBeenDeletedAdapter.updateProductList(productListingHasBeenDeleted);
        productListingHasBeenDeletedAdapter.notifyDataSetChanged();
    }

    public void uploadNumberOfProductsDeleted() {
        badgeNumberOfProductsDeleted = view.findViewById(R.id.badgeNumberOfProductsDeleted);
        badgeNumberOfProductsDeleted.setText(String.valueOf(productEntity.getNumberOfProductsDeleted()));
    }

    public void handleEventsClickButton() {
        btnTurnOnBlockAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditProduct.setVisibility(View.VISIBLE);
                btnAddNewProduct.setVisibility(View.VISIBLE);
                linearLayoutProductDetail.setVisibility(View.GONE);
                btnFixProduct.setVisibility(View.GONE);
            }
        });
        btnAddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                ProductModel newProduct = assignData();
                boolean isCheck = productEntity.insertProduct(newProduct);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add product success");
                    reloadData();
                    recyclerViewProduct.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditProduct.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add product fail");
                }
            }
        });
        btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel newProduct = assignData();
                boolean isCheck = productEntity.updateProduct(newProduct);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update product success");
                    reloadData();
                    recyclerViewProduct.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditProduct.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update product fail");
                }
            }
        });

        btnTurnOnBlockEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditProduct.setVisibility(View.VISIBLE);
                btnFixProduct.setVisibility(View.VISIBLE);
                btnAddNewProduct.setVisibility(View.GONE);
                linearLayoutProductDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel newProduct = assignData();
                newProduct.Deleted = true;
                boolean isCheck = productEntity.updateProduct(newProduct);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete product success");
                    reloadData();
                    uploadNumberOfProductsDeleted();
                    recyclerViewProduct.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditProduct.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete product fail");
                }
            }
        });
        btnTurnOnBlockDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreProduct.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditProduct.setVisibility(View.GONE);
                linearLayoutProductDetail.setVisibility(View.GONE);
                recyclerViewProduct.setVisibility(View.GONE);
            }
        });
        btnRestoreProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel newProduct = assignData();
                newProduct.Deleted = false;
                boolean isCheck = productEntity.updateProduct(newProduct);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore product success");
                    reloadData();
                    uploadNumberOfProductsDeleted();
                    recyclerViewProductHasBeenDeleted.setVisibility(View.VISIBLE);
                    linearLayoutDeletedProductDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore product fail");
                }
            }
        });
        btnDeleteActualProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductModel newProduct = assignData();
                boolean isCheck = productEntity.deleteProduct(newProduct);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual product success");
                    reloadData();
                    uploadNumberOfProductsDeleted();
                    recyclerViewProductHasBeenDeleted.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditProduct.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual product fail");
                }
            }
        });
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
            }
        });
    }


    private String copyImageToInternalStorage(Uri imageUri, String imageName) {
        String imagePath = null;
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Lưu ảnh vào thư mục của ứng dụng (internal storage) với tên ảnh gốc
            File file = new File(getActivity().getFilesDir(), imageName);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();

            // Lấy đường dẫn của ảnh đã lưu
            imagePath = file.getAbsolutePath();
            // AlertDialogUtils.showInfoDialog(getActivity(), "Image saved at: " + imagePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageName = getFileName(imageUri); // Lấy tên ảnh gốc
            imgProductImage.setImageURI(imageUri);
            imagePath = copyImageToInternalStorage(imageUri, imageName);
        }
    }

    private void deleteImageFromInternalStorage(String fileName) {
        File file = new File(getActivity().getFilesDir(), fileName);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                AlertDialogUtils.showSuccessDialog(getActivity(), "Image deleted successfully: " + fileName);
            } else {
                AlertDialogUtils.showErrorDialog(getActivity(), "Failed to delete image: " + fileName);
            }
        } else {
            AlertDialogUtils.showInfoDialog(getActivity(), "Image not found: " + fileName);
        }
    }

    private void showImageFromPath() {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imgProductImage.setImageBitmap(bitmap);
        } else {
            AlertDialogUtils.showInfoDialog(getActivity(), "No image available to display.");
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        try {
            // Lấy ContentResolver
            String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                // Nếu cursor không null, di chuyển đến đầu và lấy tên ảnh
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(projection[0]);
                    fileName = cursor.getString(nameIndex);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public void handleSelectProductImage() {
        btnSelectProductImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

    }

}


