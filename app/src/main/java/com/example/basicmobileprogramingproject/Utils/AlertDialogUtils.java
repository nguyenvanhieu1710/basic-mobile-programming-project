package com.example.basicmobileprogramingproject.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basicmobileprogramingproject.R;

public class AlertDialogUtils {
    public static void showSuccessDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_success, null);

        TextView messageText = view.findViewById(R.id.successDesc);
        Button doneButton = view.findViewById(R.id.successDone);

        messageText.setText(message);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
        doneButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    public static void showErrorDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_error, null);

        TextView messageText = view.findViewById(R.id.errorDesc);
        Button okButton = view.findViewById(R.id.errorDone);

        messageText.setText(message);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        okButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    public static void showInfoDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_info, null);

        TextView messageText = view.findViewById(R.id.infoDesc);
        Button okButton = view.findViewById(R.id.infoDone);

        messageText.setText(message);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        okButton.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    public static void showQuestionDialog(Context context, String message,
                                          DialogInterface.OnClickListener confirmListener,
                                          DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_question, null);

        TextView messageText = view.findViewById(R.id.questionDesc);
        Button confirmButton = view.findViewById(R.id.questionConfirm);
        Button cancelButton = view.findViewById(R.id.questionCancel);

        messageText.setText(message);

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();

        confirmButton.setOnClickListener(v -> {
            if (confirmListener != null) {
                confirmListener.onClick(null, DialogInterface.BUTTON_POSITIVE);
            }
            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
//            if (cancelListener != null) {
//                cancelListener.onClick(null, DialogInterface.BUTTON_NEGATIVE);
//            }
            dialog.dismiss();
        });
    }

}
