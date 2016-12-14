package com.zhuoxin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuoxin.R;
import com.zhuoxin.base.ActionBarActivity;
import com.zhuoxin.biz.MemoryManage;
import com.zhuoxin.db.DBManager;
import com.zhuoxin.view.CleanCircleView;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhuoxin.view.CleanCircleView.isRunning;

public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    TextView tv_telmgr;
    TextView tv_softmgr;
    TextView tv_rocket;
    TextView tv_number;
    TextView tv_phonemgr;
    TextView tv_filemgr;
    CleanCircleView ccv_home;
    ImageView iv_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        initView();
        initActionBar(false, "手机管家", true, this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_number.setText(MemoryManage.usedPercent(this)+"%");
        int targetAngle = (int) (3.6* MemoryManage.usedPercent(this));
        ccv_home.setTargetAngle(targetAngle);
    }

    public void initView(){
        tv_telmgr = (TextView) findViewById(R.id.tv_telmgr);
        tv_softmgr = (TextView) findViewById(R.id.tv_softmgr);
        tv_rocket = (TextView) findViewById(R.id.tv_rocket);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_phonemgr = (TextView) findViewById(R.id.tv_phonemgr);
        tv_filemgr = (TextView) findViewById(R.id.tv_filemgr);
        ccv_home = (CleanCircleView) findViewById(R.id.ccv_home);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        tv_softmgr.setOnClickListener(this);
        tv_telmgr.setOnClickListener(this);
        tv_rocket.setOnClickListener(this);
        tv_number.setOnClickListener(this);
        iv_home.setOnClickListener(this);
        tv_phonemgr.setOnClickListener(this);
        tv_filemgr.setOnClickListener(this);

    }
    @OnClick(R.id.tv_sdclean)
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_menu:
                startActivity(SettingsActivity.class);
                break;
            case R.id.tv_telmgr:
                startActivity(PhoneActivity.class);
                break;
            case R.id.tv_softmgr:
                startActivity(SoftManagerActivity.class);
                break;
            case R.id.tv_phonemgr:
                startActivity(PhoneStateActivity.class);
                break;
            case R.id.tv_filemgr:
                startActivity(FileManagerActivity.class);
                break;
            case R.id.tv_sdclean:
                File file = new File(this.getFilesDir(),"clearpath.db");
                if (!DBManager.isExistsDB(file)){
                    DBManager.copyAssetsFileToFile(this,"clearpath.db",file);
                }

                startActivity(CleanActivity.class);
                break;
            case R.id.tv_rocket:
                startActivity(RocketActivity.class);
                break;
            case  R.id.iv_home:
            case R.id.tv_number:



                if (!isRunning) {
                    MemoryManage.killRunning(this);
                    isRunning = true;
                    tv_number.setText(MemoryManage.usedPercent(this)+"%");
                    int targetAngle = (int) (3.6* MemoryManage.usedPercent(this));
                    ccv_home.setTargetAngle(targetAngle);
                }
                break;

        }
    }
}
