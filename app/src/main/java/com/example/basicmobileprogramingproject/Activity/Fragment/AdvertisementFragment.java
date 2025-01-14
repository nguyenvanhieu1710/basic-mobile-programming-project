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
import com.example.basicmobileprogramingproject.Adapter.AdvertisementAdapter;
import com.example.basicmobileprogramingproject.Entity.CategoryEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Entity.AdvertisementEntity;
import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Model.AdvertisementModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class AdvertisementFragment extends Fragment {
    TextView badgeNumberOfAdvertisementDeleted, txtDeletedAdvertisementName, txtAdvertisementName, txtAdvertiserId;
    EditText edtSearch, edtAdvertisementName, edtLocation, edtAdvertiserId;
    ImageButton btnTurnOnBlockAddAdvertisement, btnTurnOnBlockDeleteAdvertisement;
    Button btnAddNewAdvertisement, btnFixAdvertisement, btnEditAdvertisement, btnDeleteAdvertisement, btnTurnOnBlockEditAdvertisement, btnSelectAdvertisementImage, btnRestoreAdvertisement, btnDeleteActualAdvertisement;
    ImageButton btnSearch;
    ImageView imgAdvertisement, btnClear, btnCancelLinearLayoutDeletedAdvertisementDetail, btnCloseLinearLayoutDeleteAndRestoreAdvertisement;
    Spinner spinnerCategoryId;
    LinearLayout linearLayoutAddAndEditAdvertisement, linearLayoutAdvertisementDetail, linearLayoutDeleteAndRestoreAdvertisement, linearLayoutDeletedAdvertisementDetail;
    RecyclerView recyclerViewAdvertisement, recyclerViewAdvertisementHasBeenDeleted;
    AdvertisementAdapter advertisementAdapter, advertisementListingHasBeenDeletedAdapter;
    AdvertisementEntity advertisementEntity;
    ArrayList<AdvertisementModel> advertisementList, advertisementListingHasBeenDeleted;
    CategoryEntity categoryEntity;
    ArrayList<CategoryModel> categoryList;
    DatabaseHandler databaseHandler;
    View view;
    int clickedAdvertisementId;
    static final int PICK_IMAGE = 1;
    ImageView imgAdvertisementImage, btnExitLinearLayoutAdvertisementDetail, btnCloseLinearLayoutAddAndEditAdvertisement;
    Uri imageUri;
    String imagePath;
    String imageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_advertisement, container, false);

        // mapping id
        // part add advertisement
        clickedAdvertisementId = 0;
        imageName = "";
        edtAdvertisementName = view.findViewById(R.id.edtAdvertisementName);
        edtLocation = view.findViewById(R.id.edtLocation);
        edtAdvertiserId = view.findViewById(R.id.edtAdvertiserId);

        imgAdvertisementImage = view.findViewById(R.id.imgAdvertisementImage);
        btnSelectAdvertisementImage = view.findViewById(R.id.btnSelectAdvertisementImage);
        handleSelectAdvertisementImage();

        // part advertisement detail
        imgAdvertisement = view.findViewById(R.id.imgAdvertisement);
        txtAdvertisementName = view.findViewById(R.id.txtAdvertisementName);
        txtAdvertiserId = view.findViewById(R.id.txtAdvertiserId);

        // part delete actual advertisement
        txtDeletedAdvertisementName = view.findViewById(R.id.txtDeletedAdvertisementName);
        btnRestoreAdvertisement = view.findViewById(R.id.btnRestoreAdvertisement);
        btnDeleteActualAdvertisement = view.findViewById(R.id.btnDeleteActualAdvertisement);

        linearLayoutAddAndEditAdvertisement = view.findViewById(R.id.linearLayoutAddAndEditAdvertisement);
        btnCloseLinearLayoutAddAndEditAdvertisement = view.findViewById(R.id.btnCloseLinearLayoutAddAndEditAdvertisement);
        linearLayoutAdvertisementDetail = view.findViewById(R.id.linearLayoutAdvertisementDetail);
        btnExitLinearLayoutAdvertisementDetail = view.findViewById(R.id.btnExitLinearLayoutAdvertisementDetail);
        linearLayoutDeleteAndRestoreAdvertisement = view.findViewById(R.id.linearLayoutDeleteAndRestoreAdvertisement);
        btnCloseLinearLayoutDeleteAndRestoreAdvertisement = view.findViewById(R.id.btnCloseLinearLayoutDeleteAndRestoreAdvertisement);
        linearLayoutDeletedAdvertisementDetail = view.findViewById(R.id.linearLayoutDeletedAdvertisementDetail);
        btnCancelLinearLayoutDeletedAdvertisementDetail = view.findViewById(R.id.btnCancelLinearLayoutDeletedAdvertisementDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());


        // hide layout add advertisement and layout advertisement detail
        linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
        linearLayoutAdvertisementDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreAdvertisement.setVisibility(View.GONE);
        linearLayoutDeletedAdvertisementDetail.setVisibility(View.GONE);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        handleSearch();

        // get Category list
        categoryEntity = new CategoryEntity(getContext());
        categoryList = categoryEntity.getCategoryList();

        // get Advertisement list
        advertisementEntity = new AdvertisementEntity(getContext());
        advertisementList = advertisementEntity.getAdvertisementList();
        advertisementListingHasBeenDeleted = advertisementEntity.getAdvertisementListingHasBeenDeleted();

        // call adapter and event click item
        advertisementAdapter = new AdvertisementAdapter(getContext(), advertisementList);
        advertisementListingHasBeenDeletedAdapter = new AdvertisementAdapter(getContext(), advertisementListingHasBeenDeleted);
        handleSelectAdvertisement();

//       assign data up recyclerView
        recyclerViewAdvertisement = view.findViewById(R.id.recyclerViewAdvertisement);
        recyclerViewAdvertisementHasBeenDeleted = view.findViewById(R.id.recyclerViewAdvertisementHasBeenDeleted);
        uploadDataToRecyclerViewAdvertisement();

        uploadNumberOfAdvertisementDeleted();

        btnDeleteAdvertisement = view.findViewById(R.id.btnDeleteAdvertisement);
        btnTurnOnBlockEditAdvertisement = view.findViewById(R.id.btnTurnOnBlockEditAdvertisement);
        btnAddNewAdvertisement = view.findViewById(R.id.btnAddNewAdvertisement);
        btnEditAdvertisement = view.findViewById(R.id.btnFixAdvertisement);
        btnFixAdvertisement = view.findViewById(R.id.btnFixAdvertisement);
        btnTurnOnBlockAddAdvertisement = view.findViewById(R.id.btnTurnOnBlockAddAdvertisement);
        btnTurnOnBlockDeleteAdvertisement = view.findViewById(R.id.btnTurnOnBlockDeleteAdvertisement);
        handleEventsClickButton();

        return view;
    }

    public AdvertisementModel assignData() {
        AdvertisementModel advertisementModel = new AdvertisementModel();
        advertisementModel.AdvertisementId = clickedAdvertisementId;
        advertisementModel.AdvertisementName = edtAdvertisementName.getText().toString().trim();
        advertisementModel.AdvertisementImage = imagePath;
        advertisementModel.Location = edtLocation.getText().toString().trim();
        advertisementModel.AdvertiserId = Integer.parseInt(edtAdvertiserId.getText().toString().trim());
        return advertisementModel;
    }

    public boolean validateData() {
        if (imagePath == null) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please select image");
            return false;
        }
        String advertisementName = edtAdvertisementName.getText().toString().trim();
        if (advertisementName.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter advertisement name");
            return false;
        } else if (advertisementName.length() < 3) {
            AlertDialogUtils.showErrorDialog(getContext(), "Advertisement name must be at least 3 characters");
            return false;
        }
        String location = edtLocation.getText().toString().trim();
        if (location.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter location");
            return false;
        }
        String advertiserId = edtAdvertiserId.getText().toString().trim();
        if (advertiserId.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter advertiser id");
            return false;
        } else if (!advertiserId.matches("^\\d+$")) {
            AlertDialogUtils.showErrorDialog(getContext(), "Advertiser ID must be a valid number");
            return false;
        }
        return true;
    }

    public void handleSelectAdvertisement() {
        advertisementAdapter.setOnItemClickListener(position -> {
            AdvertisementModel clickedAdvertisement = advertisementList.get(position);
            linearLayoutAdvertisementDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);

            clickedAdvertisementId = clickedAdvertisement.AdvertisementId;
            imgAdvertisement.setImageBitmap(BitmapFactory.decodeFile(clickedAdvertisement.AdvertisementImage));
            txtAdvertisementName.setText(clickedAdvertisement.AdvertisementName);
            txtAdvertiserId.setText(String.valueOf(clickedAdvertisement.AdvertiserId));

            imgAdvertisementImage.setImageBitmap(BitmapFactory.decodeFile(clickedAdvertisement.AdvertisementImage));
            edtAdvertisementName.setText(clickedAdvertisement.AdvertisementName);
            edtLocation.setText(clickedAdvertisement.Location);
            edtAdvertiserId.setText(String.valueOf(clickedAdvertisement.AdvertiserId));
        });

        advertisementListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            AdvertisementModel clickedAdvertisement = advertisementListingHasBeenDeleted.get(position);
            linearLayoutDeletedAdvertisementDetail.setVisibility(View.VISIBLE);
            linearLayoutAdvertisementDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);

            // AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedAdvertisement.AdvertisementId + " " + clickedAdvertisement.AdvertisementName);

            clickedAdvertisementId = clickedAdvertisement.AdvertisementId;
            txtDeletedAdvertisementName.setText(clickedAdvertisement.AdvertisementName);

            imgAdvertisementImage.setImageBitmap(BitmapFactory.decodeFile(clickedAdvertisement.AdvertisementImage));
            edtAdvertisementName.setText(clickedAdvertisement.AdvertisementName);
            edtLocation.setText(clickedAdvertisement.Location);
            edtAdvertiserId.setText(String.valueOf(clickedAdvertisement.AdvertiserId));
        });
    }

    public void uploadDataToRecyclerViewAdvertisement() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewAdvertisement.setLayoutManager(layoutManager);
        recyclerViewAdvertisement.setAdapter(advertisementAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewAdvertisementHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewAdvertisementHasBeenDeleted.setAdapter(advertisementListingHasBeenDeletedAdapter);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void reloadData() {
        advertisementList = advertisementEntity.getAdvertisementList();
        advertisementAdapter.updateAdvertisementList(advertisementList);
        advertisementAdapter.notifyDataSetChanged();

        advertisementListingHasBeenDeleted = advertisementEntity.getAdvertisementListingHasBeenDeleted();
        advertisementListingHasBeenDeletedAdapter.updateAdvertisementList(advertisementListingHasBeenDeleted);
        advertisementListingHasBeenDeletedAdapter.notifyDataSetChanged();
    }

    public void uploadNumberOfAdvertisementDeleted() {
        badgeNumberOfAdvertisementDeleted = view.findViewById(R.id.badgeNumberOfAdvertisementDeleted);
        badgeNumberOfAdvertisementDeleted.setText(String.valueOf(advertisementEntity.getNumberOfAdvertisementsDeleted()));
    }

    public void handleEventsClickButton() {
        btnTurnOnBlockAddAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditAdvertisement.setVisibility(View.VISIBLE);
                btnAddNewAdvertisement.setVisibility(View.VISIBLE);
                linearLayoutAdvertisementDetail.setVisibility(View.GONE);
                btnFixAdvertisement.setVisibility(View.GONE);
            }
        });
        btnAddNewAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                AdvertisementModel newAdvertisement = assignData();
                boolean isCheck = advertisementEntity.insertAdvertisement(newAdvertisement);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add advertisement success");
                    reloadData();
                    recyclerViewAdvertisement.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add advertisement fail");
                }
            }
        });
        btnEditAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvertisementModel newAdvertisement = assignData();
                boolean isCheck = advertisementEntity.updateAdvertisement(newAdvertisement);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update advertisement success");
                    reloadData();
                    recyclerViewAdvertisement.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update advertisement fail");
                }
            }
        });

        btnTurnOnBlockEditAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditAdvertisement.setVisibility(View.VISIBLE);
                btnFixAdvertisement.setVisibility(View.VISIBLE);
                btnAddNewAdvertisement.setVisibility(View.GONE);
                linearLayoutAdvertisementDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvertisementModel newAdvertisement = assignData();
                newAdvertisement.Deleted = true;
                boolean isCheck = advertisementEntity.updateAdvertisement(newAdvertisement);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete advertisement success");
                    reloadData();
                    uploadNumberOfAdvertisementDeleted();
                    recyclerViewAdvertisement.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete advertisement fail");
                }
            }
        });
        btnTurnOnBlockDeleteAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreAdvertisement.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
                linearLayoutAdvertisementDetail.setVisibility(View.GONE);
                recyclerViewAdvertisement.setVisibility(View.GONE);
            }
        });
        btnRestoreAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvertisementModel newAdvertisement = assignData();
                newAdvertisement.Deleted = false;
                boolean isCheck = advertisementEntity.updateAdvertisement(newAdvertisement);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore advertisement success");
                    reloadData();
                    uploadNumberOfAdvertisementDeleted();
                    recyclerViewAdvertisementHasBeenDeleted.setVisibility(View.VISIBLE);
                    linearLayoutDeletedAdvertisementDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore advertisement fail");
                }
            }
        });
        btnDeleteActualAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdvertisementModel newAdvertisement = assignData();
                boolean isCheck = advertisementEntity.deleteAdvertisement(newAdvertisement);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual advertisement success");
                    reloadData();
                    uploadNumberOfAdvertisementDeleted();
                    recyclerViewAdvertisementHasBeenDeleted.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual advertisement fail");
                }
            }
        });
        btnExitLinearLayoutAdvertisementDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAdvertisementDetail.setVisibility(View.GONE);
            }
        });
        btnCloseLinearLayoutAddAndEditAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditAdvertisement.setVisibility(View.GONE);
            }
        });
        btnCloseLinearLayoutDeleteAndRestoreAdvertisement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreAdvertisement.setVisibility(View.GONE);
            }
        });
        btnCancelLinearLayoutDeletedAdvertisementDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeletedAdvertisementDetail.setVisibility(View.GONE);
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
                ArrayList<AdvertisementModel> searchedAdvertisementsList = advertisementEntity.getSearchedAdvertisementsList(searchKeyword);
                advertisementAdapter.updateAdvertisementList(searchedAdvertisementsList);
                advertisementAdapter.notifyDataSetChanged();
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
            imgAdvertisementImage.setImageURI(imageUri);
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
            imgAdvertisementImage.setImageBitmap(bitmap);
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

    public void handleSelectAdvertisementImage() {
        btnSelectAdvertisementImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

}


