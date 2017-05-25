package com.example.bov;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    Context mContext;
    ItemForm itemForm;

    ArrayList<TextItem> mItem = new ArrayList<TextItem>();
    public void addItem(TextItem ti) {
        mItem.add(ti);
    }

    public ListViewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            itemForm = new ItemForm(mContext);
        } else {
            itemForm = (ItemForm) convertView;
        }
        itemForm.setTextView(1, mItem.get(position).getData(0));
        itemForm.setTextView(2, mItem.get(position).getData(1));
        itemForm.setTextView(3, mItem.get(position).getData(2));

        return itemForm;
    }
}


