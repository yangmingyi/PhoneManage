package com.zhuoxin.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.R;
import com.zhuoxin.base.MyBaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/18.
 */

public class RocketAdapter extends MyBaseAdapter <ActivityManager.RunningAppProcessInfo>{
    public RocketAdapter(List<ActivityManager.RunningAppProcessInfo> dateList, Context context) {
        super(dateList, context);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null){
            view = inflater.inflate(R.layout.item_software,null);
            holder=new ViewHolder();
            holder.iv_icon = (ImageView) view.findViewById(R.id.iv_appicon);
            holder.tv_appname = (TextView) view.findViewById(R.id.tv＿appname);
            holder.tv_packagename = (TextView) view.findViewById(R.id.tv_packageName);
            holder.cb = (CheckBox) view.findViewById(R.id.cb_appdelete);
            holder.tv_version = (TextView) view.findViewById(R.id.tv_appversion);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        //对控件赋值
        holder.cb.setVisibility(View.GONE);
        holder.tv_version.setVisibility(View.GONE);
        try {
            holder.iv_icon.setImageDrawable(context.getPackageManager().getApplicationIcon(getItem(i).processName));
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(getItem(i).processName,PackageManager.MATCH_UNINSTALLED_PACKAGES);
            holder.tv_appname.setText(context.getPackageManager().getApplicationLabel(applicationInfo));
            holder.tv_packagename.setText(getItem(i).processName);
        } catch (PackageManager.NameNotFoundException e) {
            holder.iv_icon.setImageResource(R.drawable.item_arrow_right);
            holder.tv_appname.setText("未知程序");
            holder.tv_packagename.setText(getItem(i).processName);
        }
        return view;
    }
    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_appname;
        TextView tv_packagename;
        CheckBox cb;
        TextView tv_version;

    }
}
