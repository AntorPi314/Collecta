package com.adro.collecta.Recycler.Folder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.adro.collecta.R;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    final private List<Item> itemList;
    final private OnItemActionListener listener;
    private static int selectedPosition = RecyclerView.NO_POSITION;
    private Context context;

    public interface OnItemActionListener {
        void onItemClick(int position);

        void onItemHold(int position);
    }

    public Adapter(Context context, List<Item> itemList, OnItemActionListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.folderName.setText(item.getName());
        holder.bind(selectedPosition);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performClick(position);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null) {
                    listener.onItemHold(position);
                }
                return true;
            }
        });
    }

    public void performClick(int position) {
        if (position >= 0 && position < itemList.size()) {
            highLightUpdate(selectedPosition, position);
            if (listener != null) {
                listener.onItemClick(position);
            }
        }
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void highLightUpdate(int prevPosition, int newPosition){
        selectedPosition = newPosition;
        notifyItemChanged(prevPosition);
        notifyItemChanged(newPosition);
    }

    public void moveHighLightUpdate(int startPosition, int endPosition){
        notifyChangesInRange(startPosition, endPosition);
        if(startPosition == selectedPosition || endPosition == selectedPosition){
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
        endPosition = Math.min(itemList.size() - 1, endPosition);
        for (int position = startPosition; position <= endPosition; position++) {
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView folderName;
        LinearLayout highlightLine;

        public ViewHolder(View itemView, OnItemActionListener listener) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
            highlightLine = itemView.findViewById(R.id.highlightLine);
        }

        public void bind(int selectedPosition) {
            if (getAdapterPosition() == selectedPosition) {
                folderName.setTextColor(Color.parseColor("#64ADFF"));
                highlightLine.setVisibility(View.VISIBLE);
            } else {
                folderName.setTextColor(Color.parseColor("#8B97A2"));
                highlightLine.setVisibility(View.INVISIBLE);
            }
        }
    }
}


