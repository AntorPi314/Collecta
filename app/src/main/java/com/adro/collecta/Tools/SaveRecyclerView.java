package com.adro.collecta.Tools;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

public class SaveRecyclerView<T> {

    private static final String PREFS_NAME = "RecyclerViewData";
    final private Gson gson;

    public SaveRecyclerView() {
        this.gson = new Gson();
    }

    public void saveItems(Context context, String key, List<T> itemList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(itemList);
        editor.putString(key, json);
        editor.apply();
    }

    public List<T> getItems(Context context, String key, Type typeOfT) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(key, null);
        if (json == null) {
            return null;
        } else {
            return gson.fromJson(json, typeOfT);
        }
    }

    public boolean isKeyExists(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    public void clearData(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public void clearAllData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
