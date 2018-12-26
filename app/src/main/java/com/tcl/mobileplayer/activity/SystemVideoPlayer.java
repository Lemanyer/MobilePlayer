package com.tcl.mobileplayer.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.tcl.mobileplayer.R;

public class SystemVideoPlayer extends Activity {

    private VideoView videoView;
    private Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);
        videoView = findViewById(R.id.videoview);

        //准备好的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());
        //播放出错的监听
        videoView.setOnErrorListener(new MyOnErrorListener());
        //播放完成的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        videoView =findViewById(R.id.videoview);
        //得到播放地址
        uri = getIntent().getData();
        if (uri != null){
            videoView.setVideoURI(uri);
        }
        //设置控制版面
        videoView.setMediaController(new MediaController(this));

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();
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
            Toast.makeText(SystemVideoPlayer.this,"播放完成="+uri,Toast.LENGTH_SHORT).show();
        }
    }
}
