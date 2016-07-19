package com.example.second;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Fragment2 extends Fragment implements
    AMap.OnMarkerClickListener, AMap.OnInfoWindowClickListener, AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener,SensorEventListener {
    private MapView mMap;
    private AMap mAmap;
    private EditText search;                                                                                      //搜索框
    private ImageButton show,locate,goThere;                                                                       //预期效果点一下可以显示周围美食
    private LocationManager mlocationManager;
    private String keyWord = "";                                                                    // 要输入的poi搜索关键字
    private ProgressDialog progDialog = null;                                                          // 搜索时进度条     // 要输入的城市名字或者城市区号
    private PoiResult poiResult;                                                                     // poi返回的结果
    private int currentPage = 0;                                                                    // 当前页面，从0开始计数
    private PoiSearch.Query query;                                                                   // Poi查询条件类
    private PoiSearch poiSearch;                                                                    // POI搜索//管理位置
    private final int ROUTE_TYPE_WALK = 3;
    private LatLonPoint mStartPoint =null;                       //起点，
    private LatLonPoint mEndPoint=null;                          //终点，
    private RouteSearch mRouteSearch;                                                               //路径规划
    private WalkRouteResult mWalkRouteResult;                                                       //步行路径规划
    SensorManager sensorManager = null;
    Vibrator vibrator = null;
    private ArrayList<MyHashMap> mydata2;                                                           //接收activity传过来的数据
    static boolean goStatus=false;                                                                          //检查状态
    static boolean status_marker=false;
    public Fragment2() {
    }

    public static Fragment2 newInstance( ArrayList<HashMap<String,Object>> data) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putParcelableArrayList("mydata2",changeData(data));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mydata2 = getArguments().getParcelableArrayList("mydata2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView=inflater.inflate(R.layout.fragment_fragment2, container, false);
        mMap=(MapView)rootView.findViewById(R.id.map);
        mMap.onCreate(savedInstanceState);
        search=(EditText)rootView.findViewById(R.id.search);
        show=(ImageButton)rootView.findViewById(R.id.show);                                         //点击搜索
        locate=(ImageButton)rootView.findViewById(R.id.locate);                                     //定位按钮
        goThere=(ImageButton)rootView.findViewById(R.id.gothere);                                   //去这里


        initEvent();                                                                                //初始化活动
        locate();                                                                                   //定位
        registerListener();                                                                         //监听


        /*搜索按钮*/
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyWord=search.getText().toString();
                search.setText("");
                doSearchQuery();
               }
        });
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                        //定位自己
                if(mStartPoint!=null)
                locate2();
            }
        });
        goThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                       //去这里
                if(goStatus){
                    searchRouteResult(3,1);
                    goStatus=false;
                }

            }
        });



                                                                                    //定位
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event

    public void initEvent(){
        if (mAmap == null) {
            mAmap = mMap.getMap();
            mAmap.setMapType(AMap.MAP_TYPE_NORMAL);
            // 创建一个设置放大级别的CameraUpdate
            CameraUpdate cu = CameraUpdateFactory.zoomTo(20);
            CameraUpdate cu1 = CameraUpdateFactory.changeTilt(30);      //倾斜角
            // 设置地图的默认放大级别
            mAmap.moveCamera(cu);
            mAmap.moveCamera(cu1);
            mRouteSearch = new RouteSearch(getContext());
            mRouteSearch.setRouteSearchListener(this);
            mlocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
            vibrator = (Vibrator) getActivity().getSystemService(Service.VIBRATOR_SERVICE);
            flush(mydata2);


        }}
    /**
     * 注册监听
     */
    private void registerListener() {
        mAmap.setOnMarkerClickListener(this);
        mAmap.setOnInfoWindowClickListener(this);
        mAmap.setInfoWindowAdapter(this);

    }
    /*
    * 刷新周围美食店*/
    public void flush(ArrayList<MyHashMap> data ){
        MyPoiOverlay mypoivoerlay=new MyPoiOverlay(mAmap,data);
        mypoivoerlay.changeIt();
        mypoivoerlay.set();
        updatePosition2();

    }

        @Override
    public void onPause() {
        super.onPause();
        mMap.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onDestroy() {super.onDestroy();
        mMap.onDestroy();
        sensorManager.unregisterListener(this);

    }


    /*更新自己位置的方法*/

    private void updatePosition(Location location)
    {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        // 创建一个设置经纬度的CameraUpdate
        mStartPoint=new LatLonPoint(location.getLatitude(),location.getLongitude());
        CameraUpdate cu = CameraUpdateFactory.changeLatLng(pos);
        // 更新地图的显示区域
        CameraUpdate cu1 = CameraUpdateFactory.zoomTo(20);
        mAmap.moveCamera(cu);
        mAmap.moveCamera(cu1);
        // 创建一个MarkerOptions对象
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(pos);
        // 设置MarkerOptions使用自定义图标
        if(!status_marker){
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));  }     //放自己想要放的图片，高德文件夹里应该有，自己的位置
        markerOptions.draggable(true);
        status_marker=true;
        // 添加MarkerOptions（实际上是添加Marker）
         mAmap.addMarker(markerOptions);

    }

    /*定位，五秒更新一次*/

    public void locate(){
        try{
            mlocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 8, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    System.out.println("dingwei");
                    updatePosition(location);
                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    try{

                        updatePosition(mlocationManager.getLastKnownLocation(provider));
                    }
                    catch (SecurityException se){se.printStackTrace();
                        }
                }
                @Override
                public void onProviderEnabled(String provider) {

                }
                @Override
                public void onProviderDisabled(String provider) {
                }
            });}
        catch (SecurityException se){
            se.printStackTrace();

        }
    }



    /*
    * 手动定位*/
    public void locate2(){
        try{

        mAmap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude())));
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(20));
    }
    catch (SecurityException se){
    se.printStackTrace();}
    }




    /*每一次搜索或者刷新数据之后，自己位置的Marker都会被清除，所以需要一个方法来保持自己的marker*/


    public void updatePosition2(){
        if(mStartPoint!=null){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(mStartPoint.getLatitude(),mStartPoint.getLongitude()));
        // 设置MarkerOptions使用自定义图标
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));       //放自己想要放的图片，高德文件夹里应该有，自己的位置
        markerOptions.draggable(true);
        // 添加MarkerOptions（实际上是添加Marker）
        mAmap.addMarker(markerOptions);}


    }
    /**
     * 按关键字搜索时的进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getContext());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(false);
        progDialog.setMessage("正在搜索:\n" + keyWord);
        progDialog.show();
    }
    /**
     * 显示路径规划时的进度框
     */
    private void showProgressDialog1() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getContext());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
    /**
     * 开始进行poi搜索
     */
    protected void doSearchQuery() {
        showProgressDialog();                                           // 显示进度框
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "", "济南");             // 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
        query.setPageSize(8);                                           // 设置一夜最多返回八条
        query.setPageNum(currentPage);                                  // 设置查第一页

        poiSearch = new PoiSearch(getContext(), query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult result, int i) {
                dissmissProgressDialog();                               // 隐藏对话框
                if (i != 1000) {
                    if (result != null && result.getQuery() != null) {  // 搜索poi的结果
                        if (result.getQuery().equals(query)) {// 是否是同一条
                            poiResult = result;

                            List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
                            List<SuggestionCity> suggestionCities = poiResult
                                    .getSearchSuggestionCitys();        // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

                            if (poiItems != null && poiItems.size() > 0) {
                                mAmap.clear();                          // 清理之前的图标
                                locate();
                                PoiOverlay poiOverlay = new PoiOverlay(mAmap, poiItems);
                                poiOverlay.removeFromMap();
                                poiOverlay.addToMap();
                                poiOverlay.zoomToSpan();
                                updatePosition2();

                            } else if (suggestionCities != null
                                    && suggestionCities.size() > 0) {
                                showSuggestCity(suggestionCities);
                            } else {
                                Toast.makeText(getActivity(), "无结果", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {

                        Toast.makeText(getActivity(), "无结果", Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(getActivity(), "返回"+i, Toast.LENGTH_SHORT).show();             //返回错误码
                }




            }

            @Override
            public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {


            }
        });
        poiSearch.searchPOIAsyn();                                                                   //关键字异步搜索
    }
    /**
     * poi没有搜索到数据，返回一些推荐城市的信息
     */
    private void showSuggestCity(List<SuggestionCity> cities) {
        String infomation = "推荐城市\n";
        for (int i = 0; i < cities.size(); i++) {
            infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
                    + cities.get(i).getCityCode() + "城市编码:"
                    + cities.get(i).getAdCode() + "\n";
        }
        Toast.makeText(getActivity(), infomation, Toast.LENGTH_SHORT).show();

    }




    @Override
    public View getInfoContents(Marker marker) {
        marker.getId();
        Toast.makeText(getActivity(), ""+marker.getId(), Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int i) {
        dissmissProgressDialog();                                                                           //取消进度框
        mAmap.clear();// 清理地图上的所有覆盖物
        if (i != 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            getContext(), mAmap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();

                    int dis = (int) walkPath.getDistance();
//                    int dur = (int) walkPath.getDuration();
                    String des="距离"+dis;
                    Toast.makeText(getContext(),des.toString(), Toast.LENGTH_SHORT).show();


                } else if (result != null && result.getPaths() == null) {
                    Toast.makeText(getActivity(), "无结果", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getActivity(), "无结果", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), ""+i, Toast.LENGTH_SHORT).show();
        }
        updatePosition2();

    }

    @Override
    public View getInfoWindow(Marker marker) {                       //显示标题之类的
        marker.hideInfoWindow();
        Toast.makeText(getContext(), "aaaaa", Toast.LENGTH_SHORT).show();
        return null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getContext(), "bbbb", Toast.LENGTH_SHORT).show();
        marker.hideInfoWindow();

        return false;
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), "getInfoWindow"+marker.getId(), Toast.LENGTH_SHORT).show();
        mEndPoint=new LatLonPoint(marker.getPosition().latitude,marker.getPosition().longitude);
        goStatus=true;
        //locate.setImageResource();   这里把按钮的图片设为选中
    }
    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            Toast.makeText(getActivity(), "定位中，稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mEndPoint == null) {
            Toast.makeText(getActivity(), "终点未设置", Toast.LENGTH_SHORT).show();
        }
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);

            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
            mRouteSearch.calculateWalkRouteAsyn(query);// 异步路径规划步行模式查询


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    /*
    * 摇一摇刷新周围数据，摇一次向服务器请求一次*/
    @Override
    public void onSensorChanged(SensorEvent event) {

        int sensorType = event.sensor.getType();
                //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
                float[] values = event.values;
                if (sensorType == Sensor.TYPE_ACCELEROMETER)
                    {
                        if ((Math.abs(values[0]) > 19.75 || Math.abs(values[1]) > 19.75|| Math
                                .abs(values[2]) > 19.75))
                        {
                                Log.d("sensor x ", "============ values[0] = " + values[0]);
                                Log.d("sensor y ", "============ values[1] = " + values[1]);
                                Log.d("sensor z ", "============ values[2] = " + values[2]);
                                //摇动手机后，再伴随震动提示~~
                            /*向服务器请求数据,把得到的数据用限免的changeData（）换格式赋给mydata2,取坐标，这样如果数据没刷新还能用上一次的数据*/
                            showProgressDialog1();
                            /*判断是否成功*/
                            flush(mydata2);
                            dissmissProgressDialog();





                                vibrator.vibrate(500);
                            }

                    }


    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroyView() {

        super.onDestroyView();
        sensorManager.unregisterListener(this);


    }
    /*
    *
    *
    * 处理data的方法,得到坐标和商家id
    * */
    public static ArrayList<MyHashMap> changeData(ArrayList<HashMap<String,Object>> data){
        ArrayList<MyHashMap> finalData=new ArrayList<MyHashMap>();
        for(HashMap<String,Object> map:data){
            MyHashMap myHashMap=new MyHashMap();
            myHashMap.map=map;
            finalData.add(myHashMap);
        }

        return finalData;}

}
