package com.tcl.mobileplayer.base;

import android.content.Context;
import android.view.View;

public abstract class BasePager {

    public View rootview;
    public final Context context;
    public boolean isInitData;
    public BasePager(Context context){
        this.context = context;
        rootview = initView();
    }
    /**
     * 强制孩子执行
     */
    public abstract View initView() ;

    public void initData(){

    }
}
