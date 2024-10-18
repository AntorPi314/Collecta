package com.adro.collecta.Recycler;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;

import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adro.collecta.Recycler.Test.Adapter;
import com.adro.collecta.Recycler.Test.Item;

import com.adro.collecta.R;
import com.adro.collecta.Tools.SaveRecyclerView;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TestRecycler extends AppCompatActivity implements Adapter.OnItemActionListener {

    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<Item> itemList;
    public Context context;
    public SaveRecyclerView<Item> saveRecyclerViewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_recycler);

        context = getApplicationContext();
        recyclerView = findViewById(R.id.recyclerView);
        saveRecyclerViewData = new SaveRecyclerView<>();

        itemList = loadData();
        if (itemList == null) {
            itemList = new ArrayList<>();
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        // recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // itemList.add(new Item("Title 1", "Description 1"));
        itemList.add(0, new Item("Title 2", "Description 2"));

        adapter = new Adapter(itemList, this);
        recyclerView.setAdapter(adapter);
    }

    public void addItem2(View view) {

        addItem("aaa", "bbb");
    }

    public void addItem(String title, String description) {
        itemList.add(new Item(title, description));
        adapter.notifyItemInserted(itemList.size() - 1);
        saveData();
    }

    private List<Item> loadData() {
        Type typeOfItems = new TypeToken<List<Item>>() {
        }.getType();
        return saveRecyclerViewData.getItems(context, "myItemList", typeOfItems);
    }

    private void saveData() {
        saveRecyclerViewData.saveItems(context, "myItemList", itemList);
    }

    @Override
    public void onItemClick() {
        Toast.makeText(context, "You Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemHold() {
        Toast.makeText(context, "You Hold", Toast.LENGTH_SHORT).show();
    }
}

