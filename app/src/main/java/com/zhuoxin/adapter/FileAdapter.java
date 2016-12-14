package com.zhuoxin.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.R;
import com.zhuoxin.base.MyBaseAdapter;
import com.zhuoxin.entity.FileInfo;
import com.zhuoxin.utils.FileTypeUtil;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class FileAdapter extends MyBaseAdapter<FileInfo> {
    public FileAdapter(List<FileInfo> dateList, Context context) {
        super(dateList, context);
    }

    public boolean isScroll = false;
    //创建软引用键值对
    //HashMap<String,SoftReference<Bitmap>> bitmapSoftMap = new HashMap<String,SoftReference<Bitmap>>();

    //创建Lru键值对
    int maxMemory = (int) Runtime.getRuntime().maxMemory();
    private final int LRUSIZE = maxMemory/16;
    LruCache<String,Bitmap> bitmapLruCache = new LruCache<String,Bitmap>(LRUSIZE){
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getHeight()*value.getRowBytes();
        }
    };
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolde holde;
        if (view == null){
            view = inflater.inflate(R.layout.item_file,null);
            holde = new ViewHolde();
            holde.cb_file = (CheckBox) view.findViewById(R.id.cb_file);
            holde.iv_fileicon = (ImageView) view.findViewById(R.id.iv_fileicon);
            holde.tv_filename = (TextView) view.findViewById(R.id.tv_filename);
            holde.tv_filetype = (TextView) view.findViewById(R.id.tv_filetype);
            view.setTag(holde);
        }else{
            holde = (ViewHolde) view.getTag();
        }
        //设置布局中的数据
        holde.cb_file.setTag(i);
        holde.cb_file.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //取出对应位置的顺序
                int index = (int) holde.cb_file.getTag();
                //修改对应位置的数据
                getItem(index).setSelect(b);
            }
        });
        holde.cb_file.setChecked((getItem(i).isSelect()));
        //图片 传统方式R.drawable.icon  image不可拆分和判断，建议用位图来操作
        Bitmap bitmap = null;
        /*SoftReference<Bitmap> bitmapSoftReference = bitmapSoftMap.get(getItem(i).getFile().getName());
        //如果软引用没有数据，==null
        if (bitmapSoftReference == null){
            //从系统中重新加载图片
            bitmap = getBitmap(getItem(i));
            //把加载后的图片设置到软引用中
            SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
            //用键值对关联软引用
            bitmapSoftMap.put(getItem(i).getFile().getName(),soft);
        }else{
            //如果软引用不为空则直接取出图片，不用出系统加载
            bitmap = bitmapSoftReference.get();
        }
        */
        if (isScroll){
            bitmap = bitmapLruCache.get(getItem(i).getFile().getName());
            if (bitmap == null){
                //从系统中重新加载图片
                bitmap = getBitmap(getItem(i));
                bitmapLruCache.put(getItem(i).getFile().getName(),bitmap);
            }
            holde.iv_fileicon.setImageBitmap(bitmap);
        }else {
            holde.iv_fileicon.setImageResource(R.drawable.item_arrow_right);

        }

        holde.tv_filename.setText((getItem(i).getFile().getName()));
        holde.tv_filetype.setText((getItem(i).getFileType()));
        return view;
    }
    static class ViewHolde{
        CheckBox cb_file;
        ImageView iv_fileicon;
        TextView tv_filename;
        TextView tv_filetype;
    }
    private Bitmap getBitmap(FileInfo fileInfo){
        //定义位图
        Bitmap bitmap = null;
        //判断文件类型
        //在decode图片资源之前，先获取和设置tup缩放率
        //new一个options
        BitmapFactory.Options options = new BitmapFactory.Options();


        if (fileInfo.getFileType().equals(FileTypeUtil.TYPE_IMAGE)){
            //把图片资源取出，放入options中
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileInfo.getFile().getAbsolutePath(),options);
            //计算缩放率并设置
            int scaleUtil = 60;
            int scale = (options.outHeight>options.outWidth?options.outHeight:options.outWidth)/scaleUtil;
            options.inSampleSize = scale;
            //根据设置好的options进行图片加载
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(fileInfo.getFile().getAbsolutePath(),options);
            return bitmap;
        }else{
            //其他情况，取drawable目录就可以了
            //通过getIdentifier来把fileType转换成资源文件下对应的R.id的形式
            int icon = context.getResources().getIdentifier(fileInfo.getIconName(),"drawable",context.getPackageName());
            //如果icon<=0，证明没有取出数据，给他一个默认值
            if(icon<=0){
                icon = R.drawable.item_arrow_right;
            }
            //把R.ic资源转换为bitmap
            //把图片资源取出，放入options中
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(),icon,options);
            //计算缩放率并设置
            int scaleUtil = 60;
            int scale = (options.outHeight>options.outWidth?options.outHeight:options.outWidth)/scaleUtil;
            options.inSampleSize = scale;
            //根据设置好的options进行图片加载
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeResource(context.getResources(),icon,options);
            return bitmap;
        }
    }
}
