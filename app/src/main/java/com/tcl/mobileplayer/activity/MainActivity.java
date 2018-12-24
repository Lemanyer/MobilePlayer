package com.tcl.mobileplayer.activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;


import com.tcl.mobileplayer.R;
import com.tcl.mobileplayer.Replace.ReplaceFragment;
import com.tcl.mobileplayer.base.BasePager;
import com.tcl.mobileplayer.page.AudioPager;
import com.tcl.mobileplayer.page.NetAudioPager;
import com.tcl.mobileplayer.page.NetVideoPager;
import com.tcl.mobileplayer.page.VideoPager;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {
    private static final  String TAG = MainActivity.class.getSimpleName();
    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;

    private ArrayList<BasePager> basePagers;

    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fl_main_content = findViewById(R.id.fl_main_content);
        rg_bottom_tag = findViewById(R.id.rg_bottom_tag);

        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));
        basePagers.add(new AudioPager(this));
        basePagers.add(new NetVideoPager(this));
        basePagers.add(new NetAudioPager(this));


        //设置RadioGroup的一个监听
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_bottom_tag.check(R.id.rb_video);
    }

    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            switch (checkedId){
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_netaudio:
                    position = 3;
                    break;
                default:
                    VideoPager.isGrantExternalRW(MainActivity.this);
                    position = 0;
                    break;
            }
            setFragment();

        }
    }

    /**
     * 把页面添加到Fragment
     */
    private void setFragment() {

        //1.得到FragmentManger
        FragmentManager manager = getSupportFragmentManager();
        //2.开始事务
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        //3.替换
        fragmentTransaction.replace(R.id.fl_main_content,new ReplaceFragment(getBasePager()));
        //4.提交事务
        fragmentTransaction.commit();
    }

    /**
     * 根据位置得到对应的页面
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if (basePager != null && !basePager.isInitData){
            basePager.initData();
            basePager.isInitData=true;
        }
        return basePager;
    }


}
