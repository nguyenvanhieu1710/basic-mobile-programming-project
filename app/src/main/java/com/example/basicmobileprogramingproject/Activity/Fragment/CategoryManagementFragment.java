package com.example.basicmobileprogramingproject.Activity.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.basicmobileprogramingproject.Adapter.CategoryAdapter;
import com.example.basicmobileprogramingproject.Adapter.CategoryAdapter;
import com.example.basicmobileprogramingproject.Adapter.CategoryAdapter;
import com.example.basicmobileprogramingproject.Entity.CategoryEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;

import com.example.basicmobileprogramingproject.Entity.CategoryEntity;
import com.example.basicmobileprogramingproject.Model.CategoryModel;

import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CategoryManagementFragment extends Fragment {
    TextView badgeNumberOfCategoriesDeleted, txtCategoryName, txtDeletedCategoryName;
    EditText edtSearch, edtCategoryName, edtQuantity, edtPrice, edtDescription, edtBrand, edtCategoryDetail, edtDadCategoryId;
    ImageButton btnTurnOnBlockAddCategory, btnSearch, btnTurnOnBlockDeleteCategory;;
    ImageView btnClear, imgCategoryImage, imgCategory;
    ImageView btnExit, btnClose, btnCancel;
    Button btnAddNewCategory, btnFixCategory, btnEditCategory, btnDeleteCategory, btnTurnOnBlockEditCategory, btnSelectCategoryImage, btnRestoreCategory, btnDeleteActualCategory;;
    Spinner spinnerCategoryId;
    LinearLayout linearLayoutAddAndEditCategory, linearLayoutCategoryDetail, linearLayoutDeleteAndRestoreCategory, linearLayoutDeletedCategoryDetail;
    RecyclerView recyclerViewCategory, recyclerViewCategoryHasBeenDeleted;
    CategoryAdapter categoryAdapter, categoryListingHasBeenDeletedAdapter;;
    CategoryEntity categoryEntity;
    ArrayList<CategoryModel> categoryList, categoryListingHasBeenDeleted;
    DatabaseHandler databaseHandler;
    View view;
    Integer clickedCategoryId;
    static final int PICK_IMAGE = 1;
    Uri imageUri;
    String imagePath;
    String imageName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.category_management, container, false);
        // mapping id
        clickedCategoryId = 0;
        badgeNumberOfCategoriesDeleted = view.findViewById(R.id.badgeNumberOfCategoriesDeleted);
        imgCategoryImage = view.findViewById(R.id.imgCategoryImage);
        btnSelectCategoryImage = view.findViewById(R.id.btnSelectCategoryImage);
        edtCategoryName = view.findViewById(R.id.edtCategoryName);
        edtDadCategoryId = view.findViewById(R.id.edtDadCategoryId);

        imgCategory = view.findViewById(R.id.imgCategory);
        txtCategoryName = view.findViewById(R.id.txtCategoryName);

        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        edtSearch = view.findViewById(R.id.edtSearch);
        handleSearch();

        txtDeletedCategoryName = view.findViewById(R.id.txtDeletedCategoryName);
        btnTurnOnBlockDeleteCategory = view.findViewById(R.id.btnTurnOnBlockDeleteCategory);
        btnRestoreCategory = view.findViewById(R.id.btnRestoreCategory);
        btnDeleteActualCategory = view.findViewById(R.id.btnDeleteActualCategory);

        btnExit = view.findViewById(R.id.btnExit);
        btnClose = view.findViewById(R.id.btnClose);
        btnCancel = view.findViewById(R.id.btnCancel);

        linearLayoutAddAndEditCategory = view.findViewById(R.id.linearLayoutAddAndEditCategory);
        linearLayoutCategoryDetail = view.findViewById(R.id.linearLayoutCategoryDetail);
        linearLayoutDeleteAndRestoreCategory = view.findViewById(R.id.linearLayoutDeleteAndRestoreCategory);
        linearLayoutDeletedCategoryDetail = view.findViewById(R.id.linearLayoutDeletedCategoryDetail);
        // call database
        databaseHandler = new DatabaseHandler(requireContext());

// hide layout add Category and layout Category detail
        linearLayoutAddAndEditCategory.setVisibility(View.GONE);
        linearLayoutCategoryDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreCategory.setVisibility(View.GONE);
        linearLayoutDeletedCategoryDetail.setVisibility(View.GONE);

        // get Category list
        categoryEntity = new CategoryEntity(getContext());
        categoryList = categoryEntity.getCategoryList();
        categoryListingHasBeenDeleted = categoryEntity.getCategoryListingHasBeenDeleted();

        // call adapter and event click item
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        categoryListingHasBeenDeletedAdapter = new CategoryAdapter(getContext(), categoryListingHasBeenDeleted);
        handleSelectCategory();

//       assign data up recyclerView
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategoryHasBeenDeleted = view.findViewById(R.id.recyclerViewCategoryHasBeenDeleted);
        uploadDataToRecyclerViewCategory();

        uploadNumberOfCategorysDeleted();

        btnDeleteCategory = view.findViewById(R.id.btnDeleteCategory);
        btnTurnOnBlockEditCategory = view.findViewById(R.id.btnTurnOnBlockEditCategory);
        btnAddNewCategory = view.findViewById(R.id.btnAddNewCategory);
        btnEditCategory = view.findViewById(R.id.btnFixCategory);
        btnFixCategory = view.findViewById(R.id.btnFixCategory);
        btnTurnOnBlockAddCategory = view.findViewById(R.id.btnTurnOnBlockAddCategory);
        handleEventsClickButton();

        handleSelectCategoryImage();

        return view;
    }

    public CategoryModel assignData() {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.CategoryId = clickedCategoryId;
        categoryModel.CategoryName = edtCategoryName.getText().toString();
        categoryModel.CategoryImage = imagePath;
        categoryModel.DadCategoryId = Integer.parseInt(edtDadCategoryId.getText().toString());
        categoryModel.Deleted = false;
        return categoryModel;
    }

    public boolean validateData() {
        if (imagePath == null) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please select category image");
            return false;
        }
        if (edtCategoryName.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter category name");
            return false;
        }
        if (edtDadCategoryId.getText().toString().isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter dad category id");
            return false;
        }
        return true;
    }

    public void uploadDataToRecyclerViewCategory() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setAdapter(categoryAdapter);

        int numberOfColumns = 2; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewCategoryHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewCategoryHasBeenDeleted.setAdapter(categoryListingHasBeenDeletedAdapter);
    }

    public void handleSelectCategory() {
        categoryAdapter.setOnItemClickListener(position -> {
            CategoryModel clickedCategory = categoryList.get(position);
            linearLayoutCategoryDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditCategory.setVisibility(View.GONE);

            clickedCategoryId = clickedCategory.CategoryId;
            txtCategoryName.setText(clickedCategory.CategoryName);
            imgCategory.setImageURI(Uri.parse(clickedCategory.CategoryImage));
            imagePath = clickedCategory.CategoryImage;

            imgCategoryImage.setImageURI(Uri.parse(clickedCategory.CategoryImage));
            edtCategoryName.setText(clickedCategory.CategoryName);
            edtDadCategoryId.setText(String.valueOf(clickedCategory.DadCategoryId));
        });
        categoryListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            CategoryModel clickedCategory = categoryListingHasBeenDeleted.get(position);
            linearLayoutDeletedCategoryDetail.setVisibility(View.VISIBLE);
            linearLayoutCategoryDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditCategory.setVisibility(View.GONE);

//            AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedCategory.CategoryId + " " + clickedCategory.CategoryName);

            clickedCategoryId = clickedCategory.CategoryId;
            txtDeletedCategoryName.setText(clickedCategory.CategoryName);
            imgCategory.setImageURI(Uri.parse(clickedCategory.CategoryImage));
            imagePath = clickedCategory.CategoryImage;

            imgCategoryImage.setImageURI(Uri.parse(clickedCategory.CategoryImage));
            edtCategoryName.setText(clickedCategory.CategoryName);
            edtDadCategoryId.setText(String.valueOf(clickedCategory.DadCategoryId));
        });
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void uploadNumberOfCategorysDeleted() {
        badgeNumberOfCategoriesDeleted = view.findViewById(R.id.badgeNumberOfCategoriesDeleted);
        badgeNumberOfCategoriesDeleted.setText(String.valueOf(categoryEntity.getNumberOfCategoriesDeleted()));
    }

    public void handleEventsClickButton() {
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutCategoryDetail.setVisibility(View.GONE);
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditCategory.setVisibility(View.GONE);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreCategory.setVisibility(View.GONE);
            }
        });
        btnTurnOnBlockAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditCategory.setVisibility(View.VISIBLE);
                btnAddNewCategory.setVisibility(View.VISIBLE);
                linearLayoutCategoryDetail.setVisibility(View.GONE);
                btnFixCategory.setVisibility(View.GONE);
            }
        });
        btnAddNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                CategoryModel newCategory = assignData();
                AlertDialogUtils.showQuestionDialog(getContext(), "Are you sure to add new category?",
                        (dialog, which) -> {
                            boolean isCheck = categoryEntity.insertCategory(newCategory);
                            if (isCheck) {
                                AlertDialogUtils.showSuccessDialog(getContext(), "Add category success");
                                reloadData();
                                linearLayoutAddAndEditCategory.setVisibility(View.GONE);
                            } else {
                                AlertDialogUtils.showErrorDialog(getContext(), "Add category fail");
                            }
                        },
                        (dialog, which) -> {
                        });
            }
        });
        btnEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryModel newCategory = assignData();
                boolean isCheck = categoryEntity.updateCategory(newCategory);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update Category success");
                    reloadData();
                    linearLayoutAddAndEditCategory.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update Category fail");
                }
            }
        });

        btnTurnOnBlockEditCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditCategory.setVisibility(View.VISIBLE);
                btnFixCategory.setVisibility(View.VISIBLE);
                btnAddNewCategory.setVisibility(View.GONE);
                linearLayoutCategoryDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryModel newCategory = assignData();
                newCategory.Deleted = true;
                boolean isCheck = categoryEntity.updateCategory(newCategory);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete Category success");
                    uploadNumberOfCategorysDeleted();
                    reloadData();
                    recyclerViewCategory.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditCategory.setVisibility(View.GONE);
                    linearLayoutCategoryDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete Category fail");
                }
            }
        });
        btnTurnOnBlockDeleteCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreCategory.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditCategory.setVisibility(View.GONE);
                linearLayoutCategoryDetail.setVisibility(View.GONE);
                recyclerViewCategory.setVisibility(View.GONE);
            }
        });
        btnRestoreCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryModel newCategory = assignData();
                newCategory.Deleted = false;
                boolean isCheck = categoryEntity.updateCategory(newCategory);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore category success");
                    uploadNumberOfCategorysDeleted();
                    recyclerViewCategory.setVisibility(View.VISIBLE);
                    linearLayoutDeleteAndRestoreCategory.setVisibility(View.GONE);
                    linearLayoutDeletedCategoryDetail.setVisibility(View.GONE);
                    reloadData();
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore category fail");
                }
            }
        });
        btnDeleteActualCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryModel newCategory = assignData();
                boolean isCheck = categoryEntity.deleteCategory(newCategory);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual category success");
                    uploadNumberOfCategorysDeleted();
                    reloadData();
                    recyclerViewCategory.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditCategory.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual category fail");
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
                ArrayList<CategoryModel> searchedCategoriesList = categoryEntity.getSearchedCategoriesList(searchKeyword);
                categoryAdapter.updateCategoryList(searchedCategoriesList);
                categoryAdapter.notifyDataSetChanged();
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
        categoryList = categoryEntity.getCategoryList();
        categoryAdapter = new CategoryAdapter(getContext(), categoryList);
        uploadDataToRecyclerViewCategory();
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
//            AlertDialogUtils.showInfoDialog(getActivity(), "Image saved at: " + imagePath);

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
            imgCategoryImage.setImageURI(imageUri);
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
            imgCategoryImage.setImageBitmap(bitmap);
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

    public void handleSelectCategoryImage() {
        btnSelectCategoryImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

    }

}