package com.example.moove;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TestAdapter extends BaseAdapter {

    Context context;
    List<Test> testList;
    LayoutInflater layoutInflater;

    public TestAdapter(Context context, List<Test> tests){
        this.context = context;
        this.testList = tests;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return testList.size();
    }

    @Override
    public Test getItem(int position) {
        return testList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.row, null);

        Test currentTest = getItem(position);
        TextView textView = convertView.findViewById(R.id.textView22);

        textView.setText(currentTest.getName());
        return convertView;
    }
}
