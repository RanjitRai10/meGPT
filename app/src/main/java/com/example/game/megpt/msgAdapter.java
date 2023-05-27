package com.example.game.megpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.MyvViewHolder> {

    List<msgModel> messageList;
    public msgAdapter(List<msgModel> messageList){
        this.messageList = messageList;
    }
    @NonNull
    @Override
    public MyvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,null);
        return new MyvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyvViewHolder holder, int position) {
        msgModel message = messageList.get(position);
        if(message.getSendBY().equals(msgModel.SENT_BY_ME)){
            holder.leftChat.setVisibility(View.GONE);
            holder.rightChat.setVisibility(View.VISIBLE);
            holder.rightchattxt.setText(message.getMessage());
        }
        else{
            holder.rightChat.setVisibility(View.GONE);
            holder.leftChat.setVisibility(View.VISIBLE);
            holder.leftchattxt.setText(message.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MyvViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftChat,rightChat;
        TextView leftchattxt,rightchattxt;

        public MyvViewHolder(@NonNull View itemView) {
            super(itemView);
            leftChat = itemView.findViewById(R.id.leftChat);
            rightChat = itemView.findViewById(R.id.rightChat);
            leftchattxt = itemView.findViewById(R.id.left_chat_text);
            rightchattxt = itemView.findViewById(R.id.right_chat_text);
        }
    }
}
