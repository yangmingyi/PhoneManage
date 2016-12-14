package com.zhuoxin.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    public List<T> dateList = new ArrayList<T>();
    public LayoutInflater inflater;
    public Context context;


    public MyBaseAdapter(List<T> dateList, Context context) {
        this.dateList = dateList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setDateList(List<T> list) {
        this.dateList.clear();
        dateList.addAll(list);
    }
    public List<T> getDateList(){
        return dateList;
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public T getItem(int i) {
        return dateList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public abstract View getView(int i, View view, ViewGroup viewGroup);
}
