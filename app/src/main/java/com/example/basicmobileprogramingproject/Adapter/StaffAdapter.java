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

import com.example.basicmobileprogramingproject.Model.StaffModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.ViewHolder> {

    private Context context;
    private List<StaffModel> staffList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public StaffAdapter(Context context, List<StaffModel> staffList) {
        this.context = context;
        this.staffList = staffList;
    }

    public void updateStaffList(List<StaffModel> newList) {
        this.staffList = newList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.staff_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StaffModel staff = staffList.get(position);
        holder.staffName.setText(staff.Name);
        holder.staffBirthday.setText(String.valueOf(staff.Birthday));
        holder.staffImage.setImageBitmap(BitmapFactory.decodeFile(staff.Image));
    }

    @Override
    public int getItemCount() {
        return staffList.size();
    }

    public void setOnItemClickListener(StaffAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView staffImage;
        TextView staffName;
        TextView staffBirthday;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            staffImage = itemView.findViewById(R.id.staff_image);
            staffName = itemView.findViewById(R.id.staff_name);
            staffBirthday = itemView.findViewById(R.id.birthday_of_staff);

            // Gán sự kiện click cho itemView
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
