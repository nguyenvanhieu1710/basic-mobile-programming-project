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

import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public UserAdapter(Context context, List<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    public void updateUserList(List<UserModel> newList) {
        this.userList = newList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserModel user = userList.get(position);
        holder.userName.setText(user.Name);
        holder.userBirthday.setText(String.valueOf(user.Birthday));
        holder.userImage.setImageBitmap(BitmapFactory.decodeFile(user.Image));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setOnItemClickListener(UserAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView userBirthday;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            userImage = itemView.findViewById(R.id.user_image);
            userName = itemView.findViewById(R.id.user_name);
            userBirthday = itemView.findViewById(R.id.birthday_of_user);

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
