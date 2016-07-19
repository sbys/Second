package com.example.second;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by 袁帅 on 2016/7/15.
 */
public class MyPoiOverlay {
    private AMap b;
    private ArrayList<MyHashMap> d = new ArrayList<MyHashMap>();
    ArrayList<LatLng> latLnglist = new ArrayList<LatLng>();

    public MyPoiOverlay(AMap aMap, ArrayList<MyHashMap> list) {
        this.b = aMap;
        this.d = list;
    }

    public void changeIt() {
        LatLng latLng = null;
        for (int i = 0; i < d.size(); i++) {
            double in = (int) d.get(i).map.get("x");
            double inn = (int) d.get(i).map.get("y");
            latLng = new LatLng(in,inn);
            latLnglist.add(latLng);
        }
    }


    public void set() {
        b.clear();
        MarkerOptions markerOptions=new MarkerOptions();
    for(int i=0;i<latLnglist.size();i++){
        markerOptions.position(latLnglist.get(i));
        markerOptions.visible(true);
        markerOptions.title(this.d.get(i).map.get("ID").toString()).snippet(d.get(i).map.get("ID").toString()          //这里能设置标题和详细信息
        );
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.locate_shop));
        this.b.addMarker(markerOptions);
        }
        if(latLnglist.size()>0){
        CameraUpdate cu= CameraUpdateFactory.newLatLng(latLnglist.get(0));
            CameraUpdate cu1=CameraUpdateFactory.zoomTo(20);
        b.moveCamera(cu);
        b.moveCamera(cu1);}



}

}
