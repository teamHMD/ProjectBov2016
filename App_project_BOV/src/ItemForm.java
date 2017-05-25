package com.example.bov;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemForm extends LinearLayout {

    TextView textView1;
    TextView textView2;
    TextView textView3;

    public ItemForm(Context context) {
        super(context);
        init(context);
    }

    public ItemForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_row, this, true);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);

    }

    public void setTextView(int index, String data) {
        if (index == 1) {
            textView1.setText(data);
        } else if (index == 2) {
            textView2.setText(data);
        } else if (index == 3) {
            textView3.setText(data);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
