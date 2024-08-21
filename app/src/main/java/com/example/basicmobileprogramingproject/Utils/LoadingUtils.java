package com.example.basicmobileprogramingproject.Utils;

import android.content.Context;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.basicmobileprogramingproject.Activity.Fragment.LoadingDialogFragment;

public class LoadingUtils {
    private static LoadingDialogFragment loadingDialogFragment;

    public static void showLoading(Context context) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            loadingDialogFragment = new LoadingDialogFragment();
            loadingDialogFragment.show(activity.getSupportFragmentManager(), "loading");

            new Handler().postDelayed(() -> {
                if (loadingDialogFragment != null) {
                    loadingDialogFragment.dismiss();
                }
            }, 5000);
        }
    }
}
