package com.example.developer.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.todo.R;
import com.example.developer.todo.model.Category;

import java.util.List;


public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categoryList;
    private LayoutInflater inflater;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_category, viewGroup, false);
        }

        final Category category = getCategory(i);
        ((TextView) view.findViewById(R.id.tvCategoryName)).setText(category.getTitle().toString());
        ((ImageView) view.findViewById(R.id.ivCategoryIcon)).setImageResource(context.getResources().getIdentifier(category.getIcon(), "drawable", context.getPackageName()));
        ((ImageView) view.findViewById(R.id.ivCategoryIcon)).setTag(category.getIcon());
        return view;

    }

    public Category getCategory(int position) {
        return ((Category) getItem(position));
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}