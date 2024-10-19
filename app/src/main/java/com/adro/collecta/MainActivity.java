package com.adro.collecta;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adro.collecta.Recycler.Folder.Adapter;
import com.adro.collecta.Recycler.Folder.Item;

import com.adro.collecta.Recycler.Channel.AdapterChannel;
import com.adro.collecta.Recycler.Channel.ItemChannel;

import com.adro.collecta.Tools.SaveRecyclerView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.OnItemActionListener {
    public Context context;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Item> itemList;
    public SaveRecyclerView<Item> saveRecyclerViewData;

    private RecyclerView recyclerView2;
    private AdapterChannel adapter2;
    private List<ItemChannel> itemList2, itemList3;
    public SaveRecyclerView<ItemChannel> saveRecyclerViewData2;

    public int selectedFolder = 0, selectedChannel = 0;
    public String selectedFolderMS = "F0";
    private ImageView addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.folderRecyclerView);
        addImage = findViewById(R.id.addImage);
        saveRecyclerViewData = new SaveRecyclerView<>();
        addImageListener();

        recyclerView2 = findViewById(R.id.channelRecyclerView);
        saveRecyclerViewData2 = new SaveRecyclerView<>();

        itemList2 = loadData2();
        if (itemList2 == null) {
            itemList2 = new ArrayList<>();
            //itemList2.add(0, new ItemChannel("My Channel", "no description", "https://avatars.githubusercontent.com/u/123496530?s=96&v=4", "#FFFFFF", "#FFFFFF", "#000000", "C1", "user"));
            saveData2();
        }
        recyclerView2.setLayoutManager(new LinearLayoutManager(context));
        adapter2 = new AdapterChannel(this, context, itemList2);
        recyclerView2.setAdapter(adapter2);

        itemList = loadData();
        if (itemList == null) {
            itemList = new ArrayList<>();
            itemList.add(0, new Item("Home", "F0"));
            saveData();
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new Adapter(context, itemList, this);
        recyclerView.setAdapter(adapter);
        adapter.performClick(0);
    }

    private void showDialogAddFolder() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_folder, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);

        EditText folderPosition = dialogView.findViewById(R.id.folderPosition);
        EditText folderName = dialogView.findViewById(R.id.folderName);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button addButton = dialogView.findViewById(R.id.addButton);
        folderPosition.setText(String.valueOf(itemList.size()));

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderNameText = folderName.getText().toString().trim();
                String folderPositionText = folderPosition.getText().toString().trim();

                if (!folderNameText.isEmpty() && !folderPositionText.isEmpty()) {
                    int position = Integer.parseInt(folderPositionText);
                    if (position >= 1 && position <= itemList.size()) {
                        itemList.add(position, new Item(folderNameText, "F" + System.currentTimeMillis()));
                        adapter.notifyItemInserted(position);
                        recyclerView.scrollToPosition(position);
                        adapter.performClick(position);
                        saveData();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(MainActivity.this, "Invalid position", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please fill in both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void showDialogUpdateFolder(int position) {
        if (position < 1 || position >= itemList.size()) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_update_folder, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);

        EditText folderPosition = dialogView.findViewById(R.id.folderPosition);
        EditText folderName = dialogView.findViewById(R.id.folderName);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);
        Button updateButton = dialogView.findViewById(R.id.updateButton);
        folderPosition.setText(String.valueOf(position));
        folderName.setText(itemList.get(position).getName());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = LayoutInflater.from(MainActivity.this);
                View dialogView2 = inflater2.inflate(R.layout.dialog_confirm_delete, null);
                final AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this).create();
                dialog2.setView(dialogView2);

                Button btnYes = dialogView2.findViewById(R.id.btnYes);
                Button btnNo = dialogView2.findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList.remove(position);
                        adapter.notifyItemRemoved(position);
                        saveData();
                        saveRecyclerViewData2.clearData(context, itemList.get(position).getMs());
                        itemList2.clear();
                        adapter2.notifyDataSetChanged();
                        dialog.dismiss();
                        dialog2.dismiss();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String folderNameText = folderName.getText().toString().trim();
                String folderPositionText = folderPosition.getText().toString().trim();

                if (!folderNameText.isEmpty()) {
                    int newPosition = Integer.parseInt(folderPositionText);
                    if (newPosition < 1 || newPosition >= itemList.size()) {
                        Toast.makeText(MainActivity.this, "Invalid position", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    itemList.get(position).setName(folderNameText);

                    if (position != newPosition) {
                        moveItem(position, newPosition);
                    } else {
                        adapter.notifyItemChanged(position);
                    }
                    saveData();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Folder name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void showDialogAddChannel() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_channel, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);

        // Find UI components
        EditText channelPosition = dialogView.findViewById(R.id.channelPosition);
        channelPosition.setText("0");
        EditText channelName = dialogView.findViewById(R.id.channelName);
        EditText channelDes = dialogView.findViewById(R.id.channelDes);
        EditText channelPic = dialogView.findViewById(R.id.channelPic);
        EditText titleColor = dialogView.findViewById(R.id.titleColor);
        EditText descriptionColor = dialogView.findViewById(R.id.descriptionColor);
        EditText backgroundColor = dialogView.findViewById(R.id.backgroundColor);
        EditText channelType = dialogView.findViewById(R.id.channelType);
        EditText channelUserID = dialogView.findViewById(R.id.channelUserID);

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button addButton = dialogView.findViewById(R.id.addButton);

        // Cancel button action
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Add button action
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String positionText = channelPosition.getText().toString().trim();
                String name = channelName.getText().toString().trim();
                String description = channelDes.getText().toString().trim();
                String imageLink = channelPic.getText().toString().trim();
                String titleColorText = titleColor.getText().toString().trim();
                String descriptionColorText = descriptionColor.getText().toString().trim();
                String backgroundColorText = backgroundColor.getText().toString().trim();
                String type = channelType.getText().toString().trim();
                String userID = channelUserID.getText().toString().trim();

                // Validate inputs before adding
                if (!name.isEmpty() && !positionText.isEmpty()) {
                    int position = Integer.parseInt(positionText);
                    // Create a new ItemChannel object
                    ItemChannel newChannel = new ItemChannel(name, description, imageLink, titleColorText, descriptionColorText, backgroundColorText, type, userID);
                    addChannel(position, newChannel);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Name and Position are required!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void showDialogUpdateChannel(int position) {
        if (position < 0 || position >= itemList2.size()) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_update_channel, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(dialogView);

        EditText channelPosition = dialogView.findViewById(R.id.channelPosition);
        EditText channelName = dialogView.findViewById(R.id.channelName);
        EditText channelDes = dialogView.findViewById(R.id.channelDes);
        EditText channelPic = dialogView.findViewById(R.id.channelPic);
        EditText titleColor = dialogView.findViewById(R.id.titleColor);
        EditText descriptionColor = dialogView.findViewById(R.id.descriptionColor);
        EditText backgroundColor = dialogView.findViewById(R.id.backgroundColor);
        EditText channelType = dialogView.findViewById(R.id.channelType);
        EditText channelUserID = dialogView.findViewById(R.id.channelUserID);

        Button deleteButton = dialogView.findViewById(R.id.deleteButton);
        Button updateButton = dialogView.findViewById(R.id.updateButton);

        ItemChannel channel = itemList2.get(position);
        channelPosition.setText(String.valueOf(position));
        channelName.setText(channel.getName());
        channelDes.setText(channel.getDes());
        channelPic.setText(channel.getImageLink());
        titleColor.setText(channel.getTitleColor());
        descriptionColor.setText(channel.getDesColor());
        backgroundColor.setText(channel.getBackgroundColor());
        channelType.setText(channel.getType());
        channelUserID.setText(channel.getUserID());

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = LayoutInflater.from(MainActivity.this);
                View dialogView2 = inflater2.inflate(R.layout.dialog_confirm_delete, null);
                final AlertDialog dialog2 = new AlertDialog.Builder(MainActivity.this).create();
                dialog2.setView(dialogView2);

                Button btnYes = dialogView2.findViewById(R.id.btnYes);
                Button btnNo = dialogView2.findViewById(R.id.btnNo);

                btnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemList2.remove(position);
                        adapter2.notifyItemRemoved(position);
                        saveData2();
                        dialog.dismiss();
                        dialog2.dismiss();
                    }
                });
                btnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog2.dismiss();
                    }
                });
                dialog2.show();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = channelName.getText().toString().trim();
                String description = channelDes.getText().toString().trim();
                String imageLink = channelPic.getText().toString().trim();
                String titleColorText = titleColor.getText().toString().trim();
                String descriptionColorText = descriptionColor.getText().toString().trim();
                String backgroundColorText = backgroundColor.getText().toString().trim();
                String type = channelType.getText().toString().trim();
                String userID = channelUserID.getText().toString().trim();

                if (!name.isEmpty() && !channelPosition.getText().toString().isEmpty()) {
                    ItemChannel updatedChannel = new ItemChannel(name, description, imageLink, titleColorText, descriptionColorText, backgroundColorText, type, userID);
                    itemList2.set(position, updatedChannel);
                    adapter2.notifyItemChanged(position);

                    int newPosition = Integer.parseInt(channelPosition.getText().toString().trim());
                    if (newPosition < 0 || newPosition >= itemList2.size()) {
                        Toast.makeText(MainActivity.this, "Invalid position", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (position != newPosition) {
                        moveItem2(position, newPosition);
                    } else {
                        adapter2.notifyItemChanged(position);
                    }
                    saveData2();
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Channel name cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
    }

    private void addChannel(int position, ItemChannel newChannel) {
        itemList2.add(position, newChannel); // Add to the itemList2
        adapter2.notifyItemInserted(position); // Notify adapter of the new item
        recyclerView2.scrollToPosition(position); // Scroll to the new item
        saveData2(); // Save updated data
    }

    private void addImageListener() {
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddChannel();
            }
        });
        addImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogAddFolder();
                return true;
            }
        });
    }

    private void updateSelectedInfo() {
        selectedFolder = adapter.getSelectedPosition();
        selectedFolderMS = itemList.get(adapter.getSelectedPosition()).getMs();
    }

    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= itemList.size() || toPosition < 0 || toPosition >= itemList.size()) {
            return;
        }
        Item item = itemList.remove(fromPosition);
        itemList.add(toPosition, item);
        adapter.notifyItemMoved(fromPosition, toPosition);
        adapter.notifyItemChanged(toPosition);
        adapter.moveHighLightUpdate(fromPosition, toPosition);
        updateSelectedInfo();
    }

    public void moveItem2(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= itemList2.size() || toPosition < 0 || toPosition >= itemList2.size()) {
            return;
        }
        ItemChannel item2 = itemList2.remove(fromPosition);
        itemList2.add(toPosition, item2);
        adapter2.notifyItemMoved(fromPosition, toPosition);
        adapter2.notifyItemChanged(toPosition);
        adapter2.moveHighLightUpdate(fromPosition, toPosition);
    }

    public void addItem2(View view) {
        //showDialogAddFolder();
        showDialogAddChannel();
    }

    public void addItem(String name, String ms) {
        itemList.add(new Item(name, ms));
        adapter.notifyItemInserted(itemList.size() - 1);
        recyclerView.scrollToPosition(itemList.size() - 1);
        saveData();
    }

    private List<Item> loadData() {
        Type typeOfItems = new TypeToken<List<Item>>() {
        }.getType();
        return saveRecyclerViewData.getItems(context, "folder", typeOfItems);
    }

    private void saveData() {
        saveRecyclerViewData.saveItems(context, "folder", itemList);
    }

    public void Test(View view){
        itemList2.clear();
        itemList2.add(0, new ItemChannel("My Channel", "no description", "https://avatars.githubusercontent.com/u/123496530?s=96&v=4", "#FFFFFF", "#FFFFFF", "#000000", "C1", "user"));
        adapter2.notifyDataSetChanged();
    }

    private void clearAndReloadData() {
        itemList2.clear();
        if (!saveRecyclerViewData2.isKeyExists(context, selectedFolderMS)) {
            saveData2();
        }
        List<ItemChannel> loadedData = loadData2();
        if (loadedData != null) {
            itemList2.clear();
            itemList2.addAll(loadedData);
        }
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {
        recyclerView.scrollToPosition(position);
        updateSelectedInfo();
        clearAndReloadData();
    }

    @Override
    public void onItemHold(int position) {
        showDialogUpdateFolder(position);
    }

    private List<ItemChannel> loadData2() {
        Type typeOfItems2 = new TypeToken<List<ItemChannel>>() {
        }.getType();
        return saveRecyclerViewData2.getItems(context, selectedFolderMS, typeOfItems2);
    }

    private void saveData2() {
        saveRecyclerViewData2.saveItems(context, selectedFolderMS, itemList2);
    }

    public void onItemClick2(int position) {
        Toast.makeText(context, "You Clicked: " + position, Toast.LENGTH_SHORT).show();

    }

    public void onItemHold2(int position) {
        selectedChannel = position;
        showDialogUpdateChannel(position);
    }
}

