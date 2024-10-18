package com.adro.collecta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {

    private ArrayList<Channel> channelList;

    public ChannelAdapter(ArrayList<Channel> channelList) {
        this.channelList = channelList;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_channel, parent, false);
        return new ChannelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        Channel channel = channelList.get(position);
        holder.channelName.setText(channel.getName());
        holder.channelDes.setText(channel.getDescription());
    }

    @Override
    public int getItemCount() {
        return channelList.size();
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder {
        TextView channelName, channelDes;

        public ChannelViewHolder(View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.channelName);
            channelDes = itemView.findViewById(R.id.channelDes);
        }
    }
}