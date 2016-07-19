package com.example.second;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by 袁帅 on 2016/7/13.
 * 处理HashMap   fragment传数据不接受ArrayList<HashMap<></>
 */
public class MyHashMap implements Parcelable {
    public HashMap<String,Object> map = new HashMap<String,Object> ();



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
    public static final Parcelable.Creator<MyHashMap> CREATOR = new Parcelable.Creator<MyHashMap>() {
//重写Creator

        @Override
        public MyHashMap createFromParcel(Parcel source) {
            MyHashMap p = new MyHashMap();
            p.map=source.readHashMap(HashMap.class.getClassLoader());
            return p;
        }
        @Override
        public MyHashMap[] newArray(int size) {
            // TODO Auto-generated method stub
            return null;
        }
    };

}
