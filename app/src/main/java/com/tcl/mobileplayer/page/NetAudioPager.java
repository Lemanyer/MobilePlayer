package com.tcl.mobileplayer.page;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tcl.mobileplayer.base.BasePager;
import com.tcl.mobileplayer.utils.LogUtil;

public class NetAudioPager extends BasePager {
    private TextView textView;
    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        LogUtil.e("网络音乐初始化");
        super.initData();
        textView.setText("网络音乐页面");
    }
}
