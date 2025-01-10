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

import com.example.basicmobileprogramingproject.Model.AdvertisementModel;
import com.example.basicmobileprogramingproject.Model.ProductModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class AdvertisementAdapter extends RecyclerView.Adapter<AdvertisementAdapter.ViewHolder> {
    private Context context;
    private List<AdvertisementModel> advertisementlList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AdvertisementAdapter(Context context, List<AdvertisementModel> advertisementlList) {
        this.context = context;
        this.advertisementlList = advertisementlList;
    }

    public void updateAdvertisementList(List<AdvertisementModel> advertisementlList) {
        this.advertisementlList = advertisementlList;
    }

    @NonNull
    @Override
    public AdvertisementAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_advertisement_item, parent, false);
        return new AdvertisementAdapter.ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisementAdapter.ViewHolder holder, int position) {
        AdvertisementModel advertisement = advertisementlList.get(position);
        holder.txtAdvertisementTitle.setText(advertisement.AdvertisementName);
        holder.tvAdvertisementLocation.setText(advertisement.Location);

        if (advertisement.AdvertisementImage != null && !advertisement.AdvertisementImage.isEmpty()) {
            holder.imgAdvertisement.setImageBitmap(BitmapFactory.decodeFile(advertisement.AdvertisementImage));
        } else {
            holder.imgAdvertisement.setImageResource(R.drawable.anh_nen);
        }
    }

    @Override
    public int getItemCount() {
        return advertisementlList.size();
    }

    public void setOnItemClickListener(AdvertisementAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAdvertisement;
        TextView txtAdvertisementTitle, tvAdvertisementLocation, txtAdvertisementContent;

        public ViewHolder(@NonNull View itemView, AdvertisementAdapter.OnItemClickListener listener) {
            super(itemView);
            imgAdvertisement = itemView.findViewById(R.id.imgAdvertisement);
            txtAdvertisementTitle = itemView.findViewById(R.id.tvAdvertisementTitle);
            tvAdvertisementLocation = itemView.findViewById(R.id.tvAdvertisementLocation);

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