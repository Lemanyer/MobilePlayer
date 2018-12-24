package com.tcl.mobileplayer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.mobileplayer.R;

public class TitleBar extends LinearLayout implements View.OnClickListener {

    private TextView tv_search;
    private RelativeLayout rl_game;
    private ImageView iv_record;
    private Context context;
    /**
     * 在代码中实例化该类的时候使用者该方法
     * @param context
     */
    public TitleBar(Context context) {
        this(context,null);
    }


    /**
     * 当在布局文件中使用该类的时候，Android系统通过这个构造方法实例化对象
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }


    /**
     * 当布局文件加载完成后，进行回调
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //得到孩子的实例
        tv_search = (TextView) getChildAt(1);
        rl_game  = (RelativeLayout) getChildAt(2);
        iv_record = (ImageView) getChildAt(3);

        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_search:
                Toast.makeText(context,"search",Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context,"game",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(context,"record",Toast.LENGTH_SHORT).show();
                break;
                default:
                    break;
        }
    }
}
