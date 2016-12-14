package com.zhuoxin.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhuoxin.R;
import com.zhuoxin.adapter.RocketAdapter;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.biz.MemoryManage;

public class RocketActivity extends ActionBarActivity {
    TextView tv_brand;
    TextView tv_version;
    TextView tv_rocket_space;
    ProgressBar pb_rocket;
    ProgressBar pb_rocket_loading;
    ListView lv_rocket;
    Button btn_rocket;
    //创建adapter;
    RocketAdapter adapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        initView();
        initData();
        initActionBar(true, "手机加速", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initListView();
    }
    public void initView(){
        tv_brand = (TextView) findViewById(R.id.tv_brand);
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_rocket_space = (TextView) findViewById(R.id.tv_rocket_space);
        pb_rocket = (ProgressBar) findViewById(R.id.pb_rocket);
        pb_rocket_loading = (ProgressBar) findViewById(R.id.pb_rocket_loading);
        lv_rocket = (ListView) findViewById(R.id.lv_rocket);
        btn_rocket = (Button) findViewById(R.id.btn_rocket);
        //初始化事件
        btn_rocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //加速按钮
                //杀死进程
                MemoryManage.killRunning(RocketActivity.this);
                //刷新initListView
                initListView();
                //刷新运行空间
                getRunTimeMemory();
            }
        });
    }
    public void initData(){
        //获取当前的品牌、系统版本、运行内存
        String brand = Build.BRAND;
        tv_brand.setText(brand);
        String version = Build.VERSION.RELEASE;
        tv_version.setText(version);
        getRunTimeMemory();

    }
    public void getRunTimeMemory(){
        //设置进度条和文本信息的显示
        pb_rocket.setProgress(MemoryManage.usedPercent(this));
        tv_rocket_space.setText("剩余空间："+ MemoryManage.availabRAMleString(this)+"/"+ MemoryManage.tatolRAMLongString(this));
    }
    public void initListView(){

        adapter = new RocketAdapter(MemoryManage.getRunning(this),this);
        lv_rocket.setAdapter(adapter);
        pb_rocket_loading.setVisibility(View.GONE);
        lv_rocket.setVisibility(View.VISIBLE);
    }

}
