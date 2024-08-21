package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.basicmobileprogramingproject.Model.UserModel;
import com.example.basicmobileprogramingproject.Model.VoucherModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class CustomPayAdapter<T> extends ArrayAdapter<T> {

    private Context context;
    private List<T> dataList;
    private int resourceId;

    public CustomPayAdapter(Context context, int resourceId, List<T> dataList) {
        super(context, resourceId, dataList);
        this.context = context;
        this.dataList = dataList;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceId, parent, false);
        }

        T item = dataList.get(position);

        // Configure view based on the type of item
        if (item instanceof VoucherModel) {
            TextView tvVoucherId = convertView.findViewById(R.id.textViewVoucherId);
            TextView tvVoucherName = convertView.findViewById(R.id.textViewVoucherName);
            TextView tvVoucherPrice = convertView.findViewById(R.id.textViewVoucherPrice);
            tvVoucherId.setText(String.valueOf(((VoucherModel) item).VoucherId));
            tvVoucherName.setText(((VoucherModel) item).VoucherName);
            tvVoucherPrice.setText("$" + ((VoucherModel) item).Price);
        } else if (item instanceof UserModel) {
            TextView tvStaffId = convertView.findViewById(R.id.textViewStaffId);
            TextView tvStaffName = convertView.findViewById(R.id.textViewStaffName);
            tvStaffId.setText(String.valueOf(((UserModel) item).UserId));
            tvStaffName.setText(((UserModel) item).Name);
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}
