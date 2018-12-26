package com.tcl.mobileplayer.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcl.mobileplayer.R;
import com.tcl.mobileplayer.domain.MediaItem;
import com.tcl.mobileplayer.utils.Utils;

import java.util.ArrayList;

public class VideoPagerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MediaItem> mediaItems;
    private Utils utils;
    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
        utils = new Utils();
    }

    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHoder viewHoder;
        if (view == null){
            view = View.inflate(context,R.layout.item_vedio_pager,null);
            viewHoder = new ViewHoder();
            viewHoder.iv_icon = view.findViewById(R.id.iv_icon);
            viewHoder.tv_name = view.findViewById(R.id.tv_name);
            viewHoder.tv_time = view.findViewById(R.id.tv_time);
            viewHoder.tv_size = view.findViewById(R.id.tv_size);
            view.setTag(viewHoder);
        }else{
            viewHoder = (ViewHoder) view.getTag();
        }
        //得到数据
        MediaItem mediaItem = mediaItems.get(position);
        viewHoder.tv_name.setText(mediaItem.getName());
        viewHoder.tv_size.setText(Formatter.formatFileSize(context,mediaItem.getSize()));
        viewHoder.tv_time.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return view;
    }

    static class ViewHoder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}



