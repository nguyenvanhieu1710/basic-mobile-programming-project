package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basicmobileprogramingproject.Model.CategoryModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class CustomCategoryAdapter extends BaseAdapter {
    private Context context;
    List<CategoryModel> dataList;
    private int viewType;

    // Constructor để nhận vào dữ liệu và kiểu view
    public CustomCategoryAdapter(Context context, List<CategoryModel> dataList, int viewType) {
        this.context = context;
        this.dataList = dataList;
        this.viewType = viewType;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            // Inflate layout cho Spinner item
            view = LayoutInflater.from(context).inflate(R.layout.category_item_of_spinner, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // Lấy dữ liệu từ dataList
        CategoryModel item = dataList.get(position);

        // Bind dữ liệu vào view
        viewHolder.textViewCategoryId.setText(String.valueOf(item.CategoryId));
        viewHolder.textViewCategoryName.setText(item.CategoryName);

        return view;
    }


    static class ViewHolder {
        TextView textViewCategoryId;
        TextView textViewCategoryName;

        ViewHolder(View view) {
            textViewCategoryId = view.findViewById(R.id.textViewCategoryId);
            textViewCategoryName = view.findViewById(R.id.textViewCategoryName);
        }
    }
}
