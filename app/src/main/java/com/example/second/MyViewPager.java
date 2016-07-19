package com.example.second;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by 袁帅 on 2016/7/17.
 */
public class MyViewPager extends ViewPager {
    private boolean isCanScroll = true;
    public MyViewPager(Context context) {
        super(context);
    }
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public void setScanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(isCanScroll){
            return false;
        }else{
            //false  不能左右滑动
            return super.onInterceptTouchEvent(arg0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(isCanScroll)
         return false;
        else
        return super.onTouchEvent(ev);
    }
}