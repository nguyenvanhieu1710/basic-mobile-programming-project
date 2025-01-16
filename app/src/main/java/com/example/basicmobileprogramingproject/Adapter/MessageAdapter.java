package com.example.basicmobileprogramingproject.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.basicmobileprogramingproject.Model.MessageModel;
import com.example.basicmobileprogramingproject.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    private Context context;
    private List<MessageModel> messageList;
    private int currentUserId;

    private MessageAdapter.OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public MessageAdapter(Context context, List<MessageModel> messageList, int currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message = messageList.get(position);
        if (message.SenderId == currentUserId) {
            return VIEW_TYPE_SENT; // Giá trị 1
        } else {
            return VIEW_TYPE_RECEIVED; // Giá trị 2
        }
    }

    public void setOnItemClickListener(MessageAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_message_sent_item, parent, false);
            return new SentMessageViewHolder(view, onItemClickListener);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_message_received_item, parent, false);
            return new ReceivedMessageViewHolder(view, onItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messageList.get(position);
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTime;

        public SentMessageViewHolder(@NonNull View itemView, MessageAdapter.OnItemClickListener listener) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textViewSentMessage);
            textTime = itemView.findViewById(R.id.textViewSentTime);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        void bind(MessageModel message) {
            textMessage.setText(message.Content);
            textTime.setText(message.Time);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage, textTime;

        public ReceivedMessageViewHolder(@NonNull View itemView, MessageAdapter.OnItemClickListener listener) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textViewReceivedMessage);
            textTime = itemView.findViewById(R.id.textViewReceivedTime);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        void bind(MessageModel message) {
            textMessage.setText(message.Content);
            textTime.setText(message.Time);
        }
    }
}
