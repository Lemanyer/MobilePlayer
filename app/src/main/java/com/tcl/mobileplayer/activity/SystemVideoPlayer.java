package com.tcl.mobileplayer.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.tcl.mobileplayer.R;
import com.tcl.mobileplayer.domain.MediaItem;
import com.tcl.mobileplayer.utils.LogUtil;
import com.tcl.mobileplayer.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemVideoPlayer extends Activity implements View.OnClickListener {


    private static final int PROGRESS = 1;

    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView  tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button switchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVedio;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;
    private VideoView videoView;

    private Uri uri;

    private Utils utils;
    //监听电量变化的广播
    private MyReceiver myReceiver;

    private ArrayList<MediaItem> mediaItems;
    private int position;

    private GestureDetector gestureDetector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);

        initData();
        findViews();
        getData();
        setData();
        setListener();

        //设置控制版面
        //videoView.setMediaController(new MediaController(this));

    }

    private void setListener(){
        //准备好的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());
        //播放出错的监听
        videoView.setOnErrorListener(new MyOnErrorListener());
        //播放完成的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());
        //设置seekbar状态变化的监听
        seekbarVedio.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());

    }

    private void setData() {
        if (mediaItems!=null&&mediaItems.size()>0){
            tvName.setText(mediaItems.get(position).getName());
            videoView.setVideoPath(mediaItems.get(position).getData());
        }else if (uri != null){
            videoView.setVideoURI(uri);
        }
    }

    private void getData(){
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position",0);
    }



    private void initData(){

        utils=new Utils();
        gestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                Toast.makeText(SystemVideoPlayer.this,"长按",Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Toast.makeText(SystemVideoPlayer.this,"双击",Toast.LENGTH_SHORT).show();
                return super.onDoubleTap(e);

            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Toast.makeText(SystemVideoPlayer.this,"单击",Toast.LENGTH_SHORT).show();
                return super.onSingleTapConfirmed(e);
            }
        });
        //注册电量广播
        myReceiver  =  new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        //当电量变化的时候发送广播
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(myReceiver,intentFilter);
    }
    class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level" , 0) ;
            setBattey(level);
        }
    }

    private void setBattey(int level) {
        if (level<=0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if (level<=10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if (level<=20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if (level<=40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if (level<=60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if (level<=80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if (level<=100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }


    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
            //1.关联总长度
            int duartion = videoView.getDuration();
            seekbarVedio.setMax(duartion);
            tvDuration.setText(utils.stringForTime(duartion));
            //2.发消息
            handler.sendEmptyMessage(PROGRESS);
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Toast.makeText(SystemVideoPlayer.this,"播放出错",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            playNextVideo();
        }
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        /**
         * 当手指滑动的时候，会引起SeekBar的进度变化，会回调这个方法
         * @param seekBar
         * @param progress
         * @param fromUser 如果是用户引起的是true，不是用户引起的是false
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                videoView.seekTo(progress);
            }
        }


        /**
         * 手指触碰的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }


        /**
         * 当手指离开的时候回调这个方法
         * @param seekBar
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    //1.得到当前的视频播放进度
                    int currentPosition = videoView.getCurrentPosition();
                    //2.seekbar.setProgress(当前进度)
                    seekbarVedio.setProgress(currentPosition);
                    //更新文本的播放进度
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));
                    //设置系统时间
                    tvSystemTime.setText(getSystemTime());
                    //3.每秒更新一次
                    removeMessages(PROGRESS);
                    sendEmptyMessageDelayed(PROGRESS,1000);
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }


    private void findViews() {
        videoView = findViewById(R.id.videoview);
        llTop = findViewById( R.id.ll_top );
        tvName = findViewById( R.id.tv_name );
        ivBattery = findViewById( R.id.iv_battery );
        tvSystemTime = findViewById( R.id.tv_sytemTime );
        btnVoice = findViewById( R.id.btn_voice );
        seekbarVoice = findViewById( R.id.seekbar_voice );
        switchPlayer = findViewById( R.id.switch_player );
        llBottom = findViewById( R.id.ll_bottom );
        tvCurrentTime = findViewById( R.id.tv_current_time );
        seekbarVedio = findViewById( R.id.seekbar_vedio );
        tvDuration = findViewById( R.id.tv_duration );
        btnExit = findViewById( R.id.btn_exit );
        btnVideoPre = findViewById( R.id.btn_video_pre );
        btnVideoStartPause = findViewById( R.id.btn_video_start_pause );
        btnVideoNext =findViewById( R.id.btn_video_next );
        btnVideoSwitchScreen = findViewById( R.id.btn_video_switch_screen );

        btnVoice.setOnClickListener( this );
        switchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );
    }


    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {

            // Handle clicks for btnVoice
        } else if ( v == switchPlayer ) {
            // Handle clicks for switchPlayer
        } else if ( v == btnExit ) {
            finish();
            // Handle clicks for btnExit
        } else if ( v == btnVideoPre ) {
            playPreVideo();
            // Handle clicks for btnVideoPre
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            if (videoView.isPlaying()){
                //视频在播放设置暂停
                //按钮状态切换为播放状态
                videoView.pause();
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
            }else{
                //视频播放
                //按钮的状态切换为暂停
                videoView.start();
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);
            }
        } else if ( v == btnVideoNext ) {
            playNextVideo();
            // Handle clicks for btnVideo
        } else if ( v == btnVideoSwitchScreen ) {
            // Handle clicks for btnVideoSwitchScreen
        }
    }

    private void playPreVideo() {
        if (mediaItems!=null && mediaItems.size()>0){
            //播放上
            position--;
            if (position == -1){
                position = 0;
                Toast.makeText(SystemVideoPlayer.this,"已经是最一部影片了",Toast.LENGTH_SHORT).show();
            }else{
                tvName.setText(mediaItems.get(position).getName());
                videoView.setVideoPath(mediaItems.get(position).getData());
            }


        }else if (uri != null){

        }
    }

    private void playNextVideo() {
        if (mediaItems!=null && mediaItems.size()>0){
            //播放下一个
            position++;
            if (position<mediaItems.size()){
                tvName.setText(mediaItems.get(position).getName());
                videoView.setVideoPath(mediaItems.get(position).getData());
            }else if (position == mediaItems.size()){

                position = mediaItems.size()-1;
                LogUtil.e(position+"");
                Toast.makeText(SystemVideoPlayer.this,"已经是最后一部影片了",Toast.LENGTH_SHORT).show();
            }
        }else if (uri != null){

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        //释放资源的时候，先释放子类，在释放父类
        if (myReceiver!=null){
            unregisterReceiver(myReceiver);
            myReceiver=null;
        }
        super.onDestroy();
    }
}
