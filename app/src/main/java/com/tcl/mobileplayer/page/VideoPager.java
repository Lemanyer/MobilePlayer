package com.tcl.mobileplayer.page;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tcl.mobileplayer.base.BasePager;
import com.tcl.mobileplayer.utils.LogUtil;

public class VideoPager extends BasePager {
    private TextView textView;
    public VideoPager(Context context) {
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
        LogUtil.e("本地视频初始化");
        super.initData();
        textView.setText("本地视频页面");
    }
}