package com.adro.collecta.Recycler.Channel;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adro.collecta.MainActivity;
import com.adro.collecta.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterChannel extends RecyclerView.Adapter<AdapterChannel.ViewHolder> {

    private final List<ItemChannel> itemList2;
    public MainActivity mainActivity;
    private Context context;
    public int selectedPosition = 0;

    public AdapterChannel(MainActivity mainActivity, Context context, List<ItemChannel> itemList2) {
        this.mainActivity = mainActivity;
        this.context = context;
        this.itemList2 = itemList2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_rv_channel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemChannel item = itemList2.get(position);

        holder.channelName.setText(item.getName());
        holder.channelDes.setText(item.getDes());

        if (isValidHexColor(item.getTitleColor())) {
            holder.channelName.setTextColor(Color.parseColor(item.getTitleColor()));
        } else {
            holder.channelName.setTextColor(context.getResources().getColor(R.color.channelName));
        }
        if (isValidHexColor(item.getDesColor())) {
            holder.channelDes.setTextColor(Color.parseColor(item.getDesColor()));
        } else {
            holder.channelDes.setTextColor(context.getResources().getColor(R.color.channelDes));
        }
        if (isValidHexColor(item.getBackgroundColor())) {
            holder.itemView.setBackgroundColor(Color.parseColor(item.getBackgroundColor()));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }


        Glide.with(holder.itemView.getContext())
                .load(item.getImageLink())
                .placeholder(R.drawable.profile)
                .error(R.drawable.error)
                .into(holder.channelPic);
    }

    public void highLightUpdate(int prevPosition, int newPosition) {
        selectedPosition = newPosition;
        notifyItemChanged(prevPosition);
        notifyItemChanged(newPosition);
    }

    public void moveHighLightUpdate(int startPosition, int endPosition) {
        notifyChangesInRange(startPosition, endPosition);
        if (startPosition == selectedPosition || endPosition == selectedPosition) {
            highLightUpdate(selectedPosition, endPosition);
        }
    }

    public void notifyChangesInRange(int startPosition, int endPosition) {
        if (startPosition > endPosition) {
            int temp = startPosition;
            startPosition = endPosition;
            endPosition = temp;
        }
        startPosition = Math.max(0, startPosition);
        endPosition = Math.min(itemList2.size() - 1, endPosition);
        for (int position = startPosition; position <= endPosition; position++) {
            notifyItemChanged(position);
        }
    }

    public boolean isValidHexColor(String color) {
        String hexPattern = "^#(?:[0-9a-fA-F]{3}){1,2}$";
        return color.matches(hexPattern);
    }

    @Override
    public int getItemCount() {
        return itemList2.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView channelName, channelDes;
        private ImageView channelPic;

        public ViewHolder(View itemView) {
            super(itemView);
            channelName = itemView.findViewById(R.id.channelName);
            channelDes = itemView.findViewById(R.id.channelDes);
            channelPic = itemView.findViewById(R.id.channelImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.onItemClick2(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    selectedPosition = getAdapterPosition();
                    mainActivity.onItemHold2(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
