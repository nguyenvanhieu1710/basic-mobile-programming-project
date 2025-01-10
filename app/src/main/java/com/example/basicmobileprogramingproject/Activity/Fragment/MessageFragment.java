package com.example.basicmobileprogramingproject.Activity.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Adapter.MessageAdapter;
import com.example.basicmobileprogramingproject.Model.MessageModel;
import com.example.basicmobileprogramingproject.R;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private RecyclerView recyclerViewMessages;
    private MessageAdapter messageAdapter;
    private List<MessageModel> messageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message, container, false);

        recyclerViewMessages = view.findViewById(R.id.recyclerViewMessages);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));

        // Dữ liệu mẫu
        int currentUserId = 1; // ID của người dùng hiện tại
        messageList = new ArrayList<>();
        messageList.add(new MessageModel(1, "Hello!", "10:30 AM", 1, 2, false));
        messageList.add(new MessageModel(2, "Hi, how can I help you?", "10:31 AM", 2, 1, false));
        messageList.add(new MessageModel(3, "I have a question about my order.", "10:32 AM", 1, 2, false));
        messageList.add(new MessageModel(4, "Sure, please provide the details.", "10:33 AM", 2, 1, false));

        // Gắn adapter
        messageAdapter = new MessageAdapter(getContext(), messageList, currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);

        return view;
    }
}
