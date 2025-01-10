package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.basicmobileprogramingproject.Adapter.NewsAdapter;
import com.example.basicmobileprogramingproject.Adapter.NewsAdapter;
import com.example.basicmobileprogramingproject.Entity.NewsEntity;
import com.example.basicmobileprogramingproject.Entity.NewsEntity;
import com.example.basicmobileprogramingproject.Model.NewsModel;
import com.example.basicmobileprogramingproject.Model.NewsModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    View view;
    private EditText edtSearch;
    private ImageView btnClear;
    private ImageButton btnSearch;
    private RecyclerView rvNews;
    private Button btnAddNews, btnEditNews, btnDeleteNews;
    NewsEntity newsEntity;
    ArrayList<NewsModel> newsList;
    NewsAdapter newsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_news_management, container, false);
        // mapping id
        rvNews = view.findViewById(R.id.rv_news);
        btnAddNews = view.findViewById(R.id.btn_add_news);
        btnEditNews = view.findViewById(R.id.btn_edit_news);
        btnDeleteNews = view.findViewById(R.id.btn_delete_news);

        edtSearch = view.findViewById(R.id.edtSearch);
        btnClear = view.findViewById(R.id.btnClear);
        btnSearch = view.findViewById(R.id.btnSearch);
        handleSearch();

        newsEntity = new NewsEntity(getContext());
        newsList = newsEntity.getNewsList();
        if (newsList.isEmpty()) {
            AlertDialogUtils.showInfoDialog(getContext(), "No news found");
        }

        newsAdapter = new NewsAdapter(getContext(), newsList);
        uploadDataToRecyclerViewNews();

        return view;
    }

    public void uploadDataToRecyclerViewNews() {
        int numberOfColumns = 1; // Số cột bạn muốn hiển thị
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(numberOfColumns, StaggeredGridLayoutManager.VERTICAL);
        rvNews.setLayoutManager(layoutManager);
        rvNews.setAdapter(newsAdapter);
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
                ArrayList<NewsModel> searchedNewsList = newsEntity.getSearchedNewsList(searchKeyword);
                newsAdapter.updateNewsList(searchedNewsList);
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
}