package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Adapter.MessageAdapter;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Entity.MessageEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.Model.MessageModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.example.basicmobileprogramingproject.Utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private ImageButton btnSendMessage;
    private EditText editTextMessage;
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<MessageModel> messageList;
    private AccountEntity accountEntity;
    private MessageEntity messageEntity;
    int currentUserId;
    AccountModel onlineAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message, container, false);
        // mapping id
        btnSendMessage = view.findViewById(R.id.btnSendMessage);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
        // data
        messageEntity = new MessageEntity(getContext());
        accountEntity = new AccountEntity(getContext());
        onlineAccount = accountEntity.getOnlineAccount();
        currentUserId = onlineAccount.AccountId;
        messageList = messageEntity.getMessageList();

        // Gắn adapter
        messageAdapter = new MessageAdapter(getContext(), messageList, currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        handleEventClickButton();
        handleSelectMessage();

        return view;
    }

    public void handleEventClickButton() {
        // mặc định: người gửi là khách hàng, người nhận là nhân viên
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gửi tin nhắn
                String messageContent = editTextMessage.getText().toString();
                if (!messageContent.isEmpty()) {
                    int receiverId;
                    if (onlineAccount.Role.equals("User")) {
                        receiverId = 3; // id staff default
                    } else if (onlineAccount.Role.equals("Staff")) {
                        receiverId = 3; // ID of user
                    } else {
                        AlertDialogUtils.showErrorDialog(getContext(), "Invalid role. Not a user or staff.");
                        return;
                    }
                    MessageModel newMessage = new MessageModel();
                    newMessage.Content = messageContent;
                    newMessage.Time = DateUtils.getToday();
                    newMessage.SenderId = currentUserId;
                    newMessage.ReceiverId = receiverId;
                    newMessage.Deleted = false;
                    // add in database
                    messageEntity.insertMessage(newMessage);
                    // Thêm vào danh sách tin nhắn và cập nhật giao diện
                    messageList.add(newMessage);
                    messageAdapter.notifyItemInserted(messageList.size() - 1);
                    // Scroll đến tin nhắn mới nhất
                    recyclerViewMessages.scrollToPosition(messageList.size() - 1);
                    editTextMessage.setText("");
                } else {
                    AlertDialogUtils.showErrorDialog(getContext(), "Please enter a message.");
                }
            }
        });
    }

    public void handleSelectMessage() {
        messageAdapter.setOnItemClickListener(position -> {
            MessageModel clickedMessage = messageList.get(position);
            AlertDialogUtils.showQuestionDialog(getContext(),
                    "Do you want to edit of delete this message? Explain: Yes is Edit, No is Delete",
                    (dialog, which) -> {
                        messageEntity.updateMessage(clickedMessage);
                    },
                    (dialog, which) -> {
                        AlertDialogUtils.showSuccessDialog(getContext(), "Deleted successfully");
                    });
        });
    }
}
