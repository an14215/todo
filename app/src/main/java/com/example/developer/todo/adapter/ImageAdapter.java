package com.example.developer.todo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.developer.todo.R;

public class ImageAdapter extends BaseAdapter {
    private Context сontext;

    public ImageAdapter(Context c) {
        сontext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(сontext);
            imageView.setLayoutParams(new GridView.LayoutParams(150, 150));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    private Integer[] mThumbIds = {R.drawable.beach, R.drawable.book, R.drawable.broom, R.drawable.bus,
            R.drawable.chef_hat, R.drawable.dumbbell, R.drawable.graduation,R.drawable.home,R.drawable.ingredients,
            R.drawable.kitchen, R.drawable.meal, R.drawable.music, R.drawable.sleeping, R.drawable.swimmer,
            R.drawable.walking, R.drawable.work};
}