package com.example.second;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by 袁帅 on 2016/7/12.
 */
public class BannerAdapter extends PagerAdapter {
    private List<ImageView> mList;
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getCount() {
        //return Integer.MAX_VALUE;    //无限循环时使用
        return 3;

    }


    public BannerAdapter(List<ImageView> list) {
        this.mList = list;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mList.get(Math.abs(position % mList.size())));
        return  mList.get(Math.abs(position%mList.size()));

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mList.get(Math.abs(position%mList.size())));

    }
}
