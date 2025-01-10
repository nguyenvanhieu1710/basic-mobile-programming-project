package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Model.NewsModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsModel> newslList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public NewsAdapter(Context context, List<NewsModel> newslList) {
        this.context = context;
        this.newslList = newslList;
    }

    public void updateNewsList(List<NewsModel> newslList) {
        this.newslList = newslList;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_news_item, parent, false);
        return new NewsAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        NewsModel news = newslList.get(position);
        holder.txtNewsTitle.setText(news.NewsName);
        holder.txtPostingDate.setText(news.PostingDate);
        holder.txtNewsContent.setText(news.Content);

        if (news.NewsImage != null && !news.NewsImage.isEmpty()) {
            holder.imgNews.setImageBitmap(BitmapFactory.decodeFile(news.NewsImage));
        } else {
            holder.imgNews.setImageResource(R.drawable.anh_nen);
        }
    }

    @Override
    public int getItemCount() {
        return newslList.size();
    }

    public void setOnItemClickListener(NewsAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgNews;
        TextView txtNewsTitle, txtPostingDate, txtNewsContent;

        public ViewHolder(@NonNull View itemView, NewsAdapter.OnItemClickListener listener) {
            super(itemView);
            imgNews = itemView.findViewById(R.id.imgNews);
            txtNewsTitle = itemView.findViewById(R.id.txtNewsTitle);
            txtPostingDate = itemView.findViewById(R.id.txtPostingDate);
            txtNewsContent = itemView.findViewById(R.id.txtNewsContent);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}