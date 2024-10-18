package com.adro.collecta;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private ArrayList<Folder> folderList;

    public FolderAdapter(ArrayList<Folder> folderList) {
        this.folderList = folderList;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder, parent, false);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        Folder folder = folderList.get(position);
        holder.folderName.setText(folder.getName());
    }

    @Override
    public int getItemCount() {
        return folderList.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {
        TextView folderName;

        public FolderViewHolder(View itemView) {
            super(itemView);
            folderName = itemView.findViewById(R.id.folderName);
        }
    }
}