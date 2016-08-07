package com.example.mandy.memo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyAdapter extends BaseAdapter {
    private Context context;
    private Cursor cursor;
    private LinearLayout linearLayout;

    public MyAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public Object getItem(int position) {
        return cursor.getPosition();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.every_list, null);
        TextView contentTV = (TextView) linearLayout.findViewById(R.id.list_content);
        TextView timeTV = (TextView) linearLayout.findViewById(R.id.list_time);
  //      TextView calculaTime = (TextView) linearLayout.findViewById(R.id.calculaTime);
        cursor.moveToPosition(position);
        String content = cursor.getString(cursor.getColumnIndex("content"));
        String time = cursor.getString(cursor.getColumnIndex("time"));
  //      String calculatime =cursor.getString(cursor.getColumnIndex("calculaTime"));

        contentTV.setText(content);
        timeTV.setText(time);
//        calculaTime.setText(calculatime);
        return linearLayout;
    }
}
