package com.example.lenovo.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.myapplication.R;
import com.example.lenovo.myapplication.bean.Item;

import java.util.List;

/**
 * Created by lenovo on 2018/3/28.
 */

public class MyListAdapter extends ArrayAdapter<Item> {
    private int resourceId;
    public MyListAdapter(Context context, int resource, List<Item> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView imageView = view.findViewById(R.id.item_pic);
        TextView textView = view.findViewById(R.id.item_desc);
        imageView.setImageResource(item.getId());
        textView.setText(item.getDesc());
        return view;
    }
}
