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
import com.example.basicmobileprogramingproject.Adapter.NewsAdapter;
import com.example.basicmobileprogramingproject.Entity.CategoryEntity;
import com.example.basicmobileprogramingproject.Entity.DatabaseHandler;
import com.example.basicmobileprogramingproject.Entity.NewsEntity;
import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.Model.NewsModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NewsFragment extends Fragment {
    TextView badgeNumberOfNewsDeleted, txtDeletedNewsName, txtNewsName, txtPostingDate, txtPersonPosting, txtNewsContent;
    EditText edtSearch, edtNewsName, edtContent, edtPostingDate, edtPersonPostingId;
    ImageButton btnTurnOnBlockAddNews, btnTurnOnBlockDeleteNews;
    Button btnAddNewNews, btnFixNews, btnEditNews, btnDeleteNews, btnTurnOnBlockEditNews, btnSelectNewsImage, btnRestoreNews, btnDeleteActualNews;
    ImageButton btnSearch;
    ImageView imgNews, btnClear, btnCancelLinearLayoutDeletedNewsDetail, btnCloseLinearLayoutDeleteAndRestoreNews;
    Spinner spinnerCategoryId;
    LinearLayout linearLayoutAddAndEditNews, linearLayoutNewsDetail, linearLayoutDeleteAndRestoreNews, linearLayoutDeletedNewsDetail;
    RecyclerView recyclerViewNews, recyclerViewNewsHasBeenDeleted;
    NewsAdapter newsAdapter, newsListingHasBeenDeletedAdapter;
    NewsEntity newsEntity;
    ArrayList<NewsModel> newsList, newsListingHasBeenDeleted;
    CategoryEntity categoryEntity;
    ArrayList<CategoryModel> categoryList;
    DatabaseHandler databaseHandler;
    View view;
    int clickedNewsId;
    static final int PICK_IMAGE = 1;
    ImageView imgNewsImage, btnExitLinearLayoutNewsDetail, btnCloseLinearLayoutAddAndEditNews;
    Uri imageUri;
    String imagePath;
    String imageName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_news_management, container, false);

        // mapping id
        // part add news
        clickedNewsId = 0;
        imageName = "";
        edtNewsName = view.findViewById(R.id.edtNewsName);
        edtContent = view.findViewById(R.id.edtContent);
        edtPostingDate = view.findViewById(R.id.edtPostingDate);
        edtPersonPostingId = view.findViewById(R.id.edtPersonPostingId);

        imgNewsImage = view.findViewById(R.id.imgNewsImage);
        btnSelectNewsImage = view.findViewById(R.id.btnSelectNewsImage);
        handleSelectNewsImage();

        // part news detail
        imgNews = view.findViewById(R.id.imgNews);
        txtNewsName = view.findViewById(R.id.txtNewsName);
        txtPostingDate = view.findViewById(R.id.txtPostingDate);
        txtPersonPosting = view.findViewById(R.id.txtPersonPosting);
        txtNewsContent = view.findViewById(R.id.txtNewsContent);

        // part delete actual news
        txtDeletedNewsName = view.findViewById(R.id.txtDeletedNewsName);
        btnRestoreNews = view.findViewById(R.id.btnRestoreNews);
        btnDeleteActualNews = view.findViewById(R.id.btnDeleteActualNews);

        linearLayoutAddAndEditNews = view.findViewById(R.id.linearLayoutAddAndEditNews);
        btnCloseLinearLayoutAddAndEditNews = view.findViewById(R.id.btnCloseLinearLayoutAddAndEditNews);
        linearLayoutNewsDetail = view.findViewById(R.id.linearLayoutNewsDetail);
        btnExitLinearLayoutNewsDetail = view.findViewById(R.id.btnExitLinearLayoutNewsDetail);
        linearLayoutDeleteAndRestoreNews = view.findViewById(R.id.linearLayoutDeleteAndRestoreNews);
        btnCloseLinearLayoutDeleteAndRestoreNews = view.findViewById(R.id.btnCloseLinearLayoutDeleteAndRestoreNews);
        linearLayoutDeletedNewsDetail = view.findViewById(R.id.linearLayoutDeletedNewsDetail);
        btnCancelLinearLayoutDeletedNewsDetail = view.findViewById(R.id.btnCancelLinearLayoutDeletedNewsDetail);

        // call database
        databaseHandler = new DatabaseHandler(requireContext());


        // hide layout add news and layout news detail
        linearLayoutAddAndEditNews.setVisibility(View.GONE);
        linearLayoutNewsDetail.setVisibility(View.GONE);
        linearLayoutDeleteAndRestoreNews.setVisibility(View.GONE);
        linearLayoutDeletedNewsDetail.setVisibility(View.GONE);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnSearch = view.findViewById(R.id.btnSearch);
        btnClear = view.findViewById(R.id.btnClear);
        handleSearch();

        // get Category list
        categoryEntity = new CategoryEntity(getContext());
        categoryList = categoryEntity.getCategoryList();

        // get News list
        newsEntity = new NewsEntity(getContext());
        newsList = newsEntity.getNewsList();
        newsListingHasBeenDeleted = newsEntity.getNewsListingHasBeenDeleted();

        // call adapter and event click item
        newsAdapter = new NewsAdapter(getContext(), newsList);
        newsListingHasBeenDeletedAdapter = new NewsAdapter(getContext(), newsListingHasBeenDeleted);
        handleSelectNews();

//       assign data up recyclerView
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews);
        recyclerViewNewsHasBeenDeleted = view.findViewById(R.id.recyclerViewNewsHasBeenDeleted);
        uploadDataToRecyclerViewNews();

        uploadNumberOfNewsDeleted();

        btnDeleteNews = view.findViewById(R.id.btnDeleteNews);
        btnTurnOnBlockEditNews = view.findViewById(R.id.btnTurnOnBlockEditNews);
        btnAddNewNews = view.findViewById(R.id.btnAddNewNews);
        btnEditNews = view.findViewById(R.id.btnFixNews);
        btnFixNews = view.findViewById(R.id.btnFixNews);
        btnTurnOnBlockAddNews = view.findViewById(R.id.btnTurnOnBlockAddNews);
        btnTurnOnBlockDeleteNews = view.findViewById(R.id.btnTurnOnBlockDeleteNews);
        handleEventsClickButton();

        return view;
    }

    public NewsModel assignData() {
        NewsModel newsModel = new NewsModel();
        newsModel.NewsId = clickedNewsId;
        newsModel.NewsName = edtNewsName.getText().toString().trim();
        newsModel.NewsImage = imagePath;
        newsModel.Content = edtContent.getText().toString().trim();
        newsModel.PostingDate = edtPostingDate.getText().toString().trim();
        newsModel.PersonPostingId = Integer.parseInt(edtPersonPostingId.getText().toString().trim());
        return newsModel;
    }

    public boolean validateData() {
        if (imagePath == null) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please select image");
            return false;
        }
        String newsName = edtNewsName.getText().toString().trim();
        if (newsName.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter news name");
            return false;
        } else if (newsName.length() < 3) {
            AlertDialogUtils.showErrorDialog(getContext(), "News name must be at least 3 characters");
            return false;
        }
        String content = edtContent.getText().toString().trim();
        if (content.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter content");
            return false;
        } else if (content.length() < 10) {
            AlertDialogUtils.showErrorDialog(getContext(), "Content must be at least 10 characters");
            return false;
        }
        String postingDate = edtPostingDate.getText().toString().trim();
        if (postingDate.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter posting date");
            return false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false);
            try {
                sdf.parse(postingDate);
            } catch (ParseException e) {
                AlertDialogUtils.showErrorDialog(getContext(), "Posting date must be in the format dd/MM/yyyy and valid");
                return false;
            }
        }
        String personPostingIdStr = edtPersonPostingId.getText().toString().trim();
        if (personPostingIdStr.isEmpty()) {
            AlertDialogUtils.showErrorDialog(getContext(), "Please enter person posting ID");
            return false;
        }
        try {
            int personPostingId = Integer.parseInt(personPostingIdStr);
            if (personPostingId <= 0) {
                AlertDialogUtils.showErrorDialog(getContext(), "Person posting ID must be a positive number");
                return false;
            }
        } catch (NumberFormatException e) {
            AlertDialogUtils.showErrorDialog(getContext(), "Person posting ID must be a valid number");
            return false;
        }
        return true;
    }

    public void handleSelectNews() {
        newsAdapter.setOnItemClickListener(position -> {
            NewsModel clickedNews = newsList.get(position);
            linearLayoutNewsDetail.setVisibility(View.VISIBLE);
            linearLayoutAddAndEditNews.setVisibility(View.GONE);

            clickedNewsId = clickedNews.NewsId;
            imgNews.setImageBitmap(BitmapFactory.decodeFile(clickedNews.NewsImage));
            txtNewsName.setText(clickedNews.NewsName);
            txtPostingDate.setText("Posting Date: " + clickedNews.PostingDate);
            txtPersonPosting.setText("Person Posting: " + String.valueOf(clickedNews.PersonPostingId));
            txtNewsContent.setText("Content: " + clickedNews.Content);

            imgNewsImage.setImageBitmap(BitmapFactory.decodeFile(clickedNews.NewsImage));
            edtNewsName.setText(clickedNews.NewsName);
            edtContent.setText(clickedNews.Content);
            edtPostingDate.setText(clickedNews.PostingDate);
            edtPersonPostingId.setText(String.valueOf(clickedNews.PersonPostingId));
        });

        newsListingHasBeenDeletedAdapter.setOnItemClickListener(position -> {
            NewsModel clickedNews = newsListingHasBeenDeleted.get(position);
            linearLayoutDeletedNewsDetail.setVisibility(View.VISIBLE);
            linearLayoutNewsDetail.setVisibility(View.GONE);
            linearLayoutAddAndEditNews.setVisibility(View.GONE);

            // AlertDialogUtils.showInfoDialog(getContext(), "Check: " + clickedNews.NewsId + " " + clickedNews.NewsName);

            clickedNewsId = clickedNews.NewsId;
            txtDeletedNewsName.setText(clickedNews.NewsName);

            imgNewsImage.setImageBitmap(BitmapFactory.decodeFile(clickedNews.NewsImage));
            edtNewsName.setText(clickedNews.NewsName);
            edtContent.setText(clickedNews.Content);
            edtPostingDate.setText(clickedNews.PostingDate);
            edtPersonPostingId.setText(String.valueOf(clickedNews.PersonPostingId));
        });
    }

    public void uploadDataToRecyclerViewNews() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewNews.setLayoutManager(layoutManager);
        recyclerViewNews.setAdapter(newsAdapter);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewNewsHasBeenDeleted.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewNewsHasBeenDeleted.setAdapter(newsListingHasBeenDeletedAdapter);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm fragment hiện tại vào backstack nếu muốn quay lại
        fragmentTransaction.commit();
    }

    public void reloadData() {
        newsList = newsEntity.getNewsList();
        newsAdapter.updateNewsList(newsList);
        newsAdapter.notifyDataSetChanged();

        newsListingHasBeenDeleted = newsEntity.getNewsListingHasBeenDeleted();
        newsListingHasBeenDeletedAdapter.updateNewsList(newsListingHasBeenDeleted);
        newsListingHasBeenDeletedAdapter.notifyDataSetChanged();
    }

    public void uploadNumberOfNewsDeleted() {
        badgeNumberOfNewsDeleted = view.findViewById(R.id.badgeNumberOfNewsDeleted);
        badgeNumberOfNewsDeleted.setText(String.valueOf(newsEntity.getNumberOfNewsDeleted()));
    }

    public void handleEventsClickButton() {
        btnTurnOnBlockAddNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditNews.setVisibility(View.VISIBLE);
                btnAddNewNews.setVisibility(View.VISIBLE);
                linearLayoutNewsDetail.setVisibility(View.GONE);
                btnFixNews.setVisibility(View.GONE);
            }
        });
        btnAddNewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateData()) {
                    return;
                }
                NewsModel newNews = assignData();
                boolean isCheck = newsEntity.insertNews(newNews);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Add news success");
                    reloadData();
                    recyclerViewNews.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditNews.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Add news fail");
                }
            }
        });
        btnEditNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsModel newNews = assignData();
                boolean isCheck = newsEntity.updateNews(newNews);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Update news success");
                    reloadData();
                    recyclerViewNews.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditNews.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Update news fail");
                }
            }
        });

        btnTurnOnBlockEditNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditNews.setVisibility(View.VISIBLE);
                btnFixNews.setVisibility(View.VISIBLE);
                btnAddNewNews.setVisibility(View.GONE);
                linearLayoutNewsDetail.setVisibility(View.GONE);
            }
        });
        btnDeleteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsModel newNews = assignData();
                newNews.Deleted = true;
                boolean isCheck = newsEntity.updateNews(newNews);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete news success");
                    reloadData();
                    uploadNumberOfNewsDeleted();
                    recyclerViewNews.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditNews.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete news fail");
                }
            }
        });
        btnTurnOnBlockDeleteNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreNews.setVisibility(View.VISIBLE);
                linearLayoutAddAndEditNews.setVisibility(View.GONE);
                linearLayoutNewsDetail.setVisibility(View.GONE);
                recyclerViewNews.setVisibility(View.GONE);
            }
        });
        btnRestoreNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsModel newNews = assignData();
                newNews.Deleted = false;
                boolean isCheck = newsEntity.updateNews(newNews);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Restore news success");
                    reloadData();
                    uploadNumberOfNewsDeleted();
                    recyclerViewNewsHasBeenDeleted.setVisibility(View.VISIBLE);
                    linearLayoutDeletedNewsDetail.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Restore news fail");
                }
            }
        });
        btnDeleteActualNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsModel newNews = assignData();
                boolean isCheck = newsEntity.deleteNews(newNews);
                if (isCheck) {
                    AlertDialogUtils.showSuccessDialog(getContext(), "Delete actual news success");
                    reloadData();
                    uploadNumberOfNewsDeleted();
                    recyclerViewNewsHasBeenDeleted.setVisibility(View.VISIBLE);
                    linearLayoutAddAndEditNews.setVisibility(View.GONE);
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Delete actual news fail");
                }
            }
        });
        btnExitLinearLayoutNewsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutNewsDetail.setVisibility(View.GONE);
            }
        });
        btnCloseLinearLayoutAddAndEditNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutAddAndEditNews.setVisibility(View.GONE);
            }
        });
        btnCloseLinearLayoutDeleteAndRestoreNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeleteAndRestoreNews.setVisibility(View.GONE);
            }
        });
        btnCancelLinearLayoutDeletedNewsDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutDeletedNewsDetail.setVisibility(View.GONE);
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
                ArrayList<NewsModel> searchedNewssList = newsEntity.getSearchedNewsList(searchKeyword);
                newsAdapter.updateNewsList(searchedNewssList);
                newsAdapter.notifyDataSetChanged();
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
            imgNewsImage.setImageURI(imageUri);
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
            imgNewsImage.setImageBitmap(bitmap);
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

    public void handleSelectNewsImage() {
        btnSelectNewsImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE);
        });

    }

}


