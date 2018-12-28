package com.tcl.mobileplayer.page;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tcl.mobileplayer.R;
import com.tcl.mobileplayer.activity.SystemVideoPlayer;
import com.tcl.mobileplayer.adapter.VideoPagerAdapter;
import com.tcl.mobileplayer.base.BasePager;
import com.tcl.mobileplayer.domain.MediaItem;
import com.tcl.mobileplayer.utils.LogUtil;
import java.util.ArrayList;

public class VideoPager extends BasePager  {
    private ListView listView;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;

    private VideoPagerAdapter videoPagerAdapter;

    private ArrayList<MediaItem> mediaItems;

    public VideoPager(Context context) {
        super(context);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mediaItems!=null && mediaItems.size()>0){
                //有数据
                //设施适配器
                //文本隐藏
                videoPagerAdapter = new VideoPagerAdapter(context,mediaItems);
                listView.setAdapter(videoPagerAdapter);
                tv_nomedia.setVisibility(View.GONE);
            }else {
                //没有数据
                //文本显示
                tv_nomedia.setVisibility(View.VISIBLE);
            }
            pb_loading.setVisibility(View.GONE);
        }
    };


    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.video_pager,null);
        listView = view.findViewById(R.id.listview);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);

        //设置ListView的Item的点击事件
        listView.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            MediaItem mediaItem = mediaItems.get(position);
            Toast.makeText(context,mediaItem.toString(),Toast.LENGTH_SHORT).show();
            //1.调起系统所有的播放
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);

            //2.调用自己写的播放器

//            Intent intent = new Intent(context,SystemVideoPlayer.class);
//            intent.setDataAndType(Uri.parse(mediaItem.getData()),"video/*");
//            context.startActivity(intent);


            //3.传递列表对象-序列化
            Intent intent = new Intent(context,SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist",mediaItems);
            intent.putExtra("position",position);
            intent.putExtras(bundle);
            context.startActivity(intent);

        }
    }


    //安卓6.0以上需要动态获取权限
    public static boolean isGrantExternalRW(Activity activity){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            activity.requestPermissions(new String[]{
                 Manifest.permission.READ_EXTERNAL_STORAGE,
                 Manifest.permission.WRITE_EXTERNAL_STORAGE
            },1);
            return false;
        }
        return true;
    }


    @Override
    public void initData() {
        LogUtil.e("本地视频初始化");
        super.initData();
        //加载本地视频数据
        getDataFromLocal();
    }


    /**
     * 从本地的sdcard得到数据
     * 1.遍历sdcard，后缀名
     * 2.从内容提供者里面获取视频
     */
    private void getDataFromLocal() {
       // getAbsoluteImagePath();
        new Thread(){
            @Override
            public void run() {
                super.run();
                mediaItems = new ArrayList<>();
                SystemClock.sleep(1000);
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                LogUtil.e(uri.toString());
                String[] objs ={
                        MediaStore.Video.Media.DISPLAY_NAME,  //视频在sdcard的名称
                        MediaStore.Video.Media.DURATION, //总时长
                        MediaStore.Video.Media.SIZE,    //文件大小
                        MediaStore.Video.Media.DATA,   //绝对地址
                        MediaStore.Video.Media.ARTIST //艺术家
                };
                Cursor cursor =  resolver.query(uri,objs,null,null,null);
                if (cursor!=null){
                    LogUtil.e("not null");
                    while (cursor.moveToNext()){
                        LogUtil.e("in");
                        MediaItem mediaItem = new MediaItem();

                        String name = cursor.getString(0);
                        LogUtil.e(name);
                        mediaItem.setName(name);
                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);
                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);
                        String data = cursor.getString(3);
                        mediaItem.setData(data);
                        String artist = cursor.getString(4);
                        mediaItem.setArtist(artist);
                        mediaItems.add(mediaItem);

                    }
                    cursor.close();
                }

                //发消息
                handler.sendEmptyMessage(10);
            }
        }.start();
    }


    //寻找视频存放的地址
    public void getPath(){
        ContentResolver resolver = context.getContentResolver();
        String myImageUrl = "content://media/external/video/media";
        Uri uri = Uri.parse(myImageUrl);


        String[] proj = { MediaStore.Video.Media.DATA };
        Cursor actualimagecursor = resolver.query(uri,proj,null,null,null);
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        String img_path = actualimagecursor.getString(actual_image_column_index);
        LogUtil.e(img_path);
    }


}
