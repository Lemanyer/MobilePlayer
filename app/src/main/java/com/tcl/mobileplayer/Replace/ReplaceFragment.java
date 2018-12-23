package com.tcl.mobileplayer.Replace;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tcl.mobileplayer.base.BasePager;

@SuppressLint("ValidFragment")
public class ReplaceFragment extends Fragment {
    private BasePager currPager;

    public ReplaceFragment(BasePager basePager){
        this.currPager = basePager;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  currPager.rootview;
    }
}
