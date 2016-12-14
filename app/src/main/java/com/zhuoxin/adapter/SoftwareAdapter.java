package com.zhuoxin.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.R;
import com.zhuoxin.base.MyBaseAdapter;
import com.zhuoxin.entity.AppInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */

public class SoftwareAdapter extends MyBaseAdapter<AppInfo> {
    public SoftwareAdapter(List dateList, Context context) {
        super(dateList, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.item_software, null);
            holder = new ViewHolder();
            holder.iv_appicon = (ImageView) view.findViewById(R.id.iv_appicon);
            holder.tv_appname = (TextView) view.findViewById(R.id.tv＿appname);
            holder.tv_packageName = (TextView) view.findViewById(R.id.tv_packageName);
            holder.tv_appversion = (TextView) view.findViewById(R.id.tv_appversion);
            holder.cb_appdelete = (CheckBox) view.findViewById(R.id.cb_appdelete);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.cb_appdelete.setTag(i);
        holder.iv_appicon.setImageDrawable(getItem(i).appicon);
        holder.tv_appname.setText(getItem(i).appname);
        holder.tv_packageName.setText(getItem(i).packageName);
        holder.tv_appversion.setText(getItem(i).appversion);
        //如果是系统的APP，禁用checkbox
        if(getItem(i).isSystem){
            holder.cb_appdelete.setClickable(false);
        }else{
            holder.cb_appdelete.setClickable(true);
        }
        holder.cb_appdelete.setChecked(getItem(i).isDelete);
        holder.cb_appdelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //ListViewzhon
                int index = (int) holder.cb_appdelete.getTag();
                getItem(index).isDelete = isChecked;
            }
        });
        return view;
    }

    private static class ViewHolder {
        TextView tv_appname;//注意这里不要定义成String
        TextView tv_packageName;
        ImageView iv_appicon;
        TextView tv_appversion;
        CheckBox cb_appdelete;
    }
}
