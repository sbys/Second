package com.example.second;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements Fragment1.GetShop{
    private MyViewPager mViewPager;   //滑动页
    final int FRAGMENT_TOTAL=3;    //fragment数目
    private SectionsPagerAdapter mSectionsPagerAdapter;  //适配器
    private TabLayout.Tab one;      //附近商家
    private TabLayout.Tab two;      //地图
    private TabLayout.Tab three;    //个人主页
    TabLayout mTabLayout;
    ArrayList<HashMap<String,Object>> mydata=new ArrayList<HashMap<String, Object>>();                  //测试fragment的数据
    public LocationManager mlocationManager;
    Location mlocation;                                                           //得到坐标
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         /* 测试数据*/
        HashMap<String,Object> map=new HashMap<String, Object>();
        map.put("ID","123456");
        map.put("image","" );
        map.put("title","杨明宇黄焖鸡米饭");
        map.put("numStars",2);
        map.put("sales", 50000);
        map.put("distance",5);
        map.put("benefit1", "买一送一");
        map.put("benefit2","买二送一");
        map.put("x",36);
        map.put("y",117);
        mydata.add(map);

        initViews();
        initEvents();

    }

    public void initViews(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (MyViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        System.out.println("yunxuhuadong");
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setScanScroll(false);
        one = mTabLayout.getTabAt(0);
        two = mTabLayout.getTabAt(1);
        three = mTabLayout.getTabAt(2);
        one.setIcon(R.drawable.nearby_selected);          //附近商家图标被选中
        two.setIcon(R.drawable.map_notselected);          //地图图标未被选中
        three.setIcon(R.drawable.me_notselected);        //个人主页图标未被选中


    }
    private void initEvents() {

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            /*下面设置每个图标被选中时图片*/
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:

                        one.setIcon(R.drawable.nearby_selected);
                        mViewPager.setCurrentItem(0);
                        mViewPager.setScanScroll(false);
                        break;
                    case 1:
                        two.setIcon(R.drawable.map_selected);
                        mViewPager.setCurrentItem(1);
                        mViewPager.setScanScroll(true);
                        break;
                    case 2:
                        three.setIcon(R.drawable.me_selected);
                        mViewPager.setCurrentItem(2);
                        mViewPager.setScanScroll(false);
                        break;

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                /*设置每个图标未被选中的图片*/
                switch (tab.getPosition()) {
                    case 0:

                        one.setIcon(R.drawable.nearby_notselected);
                        break;
                    case 1:
                        two.setIcon(R.drawable.map_notselected);
                        break;
                    case 2:
                        three.setIcon(R.drawable.me_notselected);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mlocationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        getLocation();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void getShop(String shopid) {
        Intent intent=new Intent(MainActivity.this,ShopIntroduce.class);
        intent.putExtra("shopid",shopid);
        startActivity(intent);

    }

    /*fragment适配器*/
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment=Fragment1.newInstance(mydata);
                    break;
                case 1:
                    fragment=Fragment2.newInstance(mydata);
                    break;
                case 2:
                    fragment=Fragment3.newInstance("个人主页");
                    break;
                default:

                    fragment=new Fragment1();
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return FRAGMENT_TOTAL;
        }


        @Override
        /*用于标签上显示的标题*/
        public CharSequence getPageTitle(int position) {

            return  null;
    }}
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    Toast.makeText(MainActivity.this,"将要进入该商家主页", Toast.LENGTH_SHORT).show();
                    getShop(result);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(MainActivity.this, "扫描出错", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    /*修改back键作用*/
    public void onBackPressed() {

        moveTaskToBack(true);
    }



    /*
    * 得到坐标，发送坐标会用到*/
    public void  getLocation(){
        try{
            mlocation=mlocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Toast.makeText(MainActivity.this, ""+mlocation.toString(), Toast.LENGTH_SHORT).show();}

        catch (SecurityException se){
            se.printStackTrace();
        }
        //得到经纬度
        double x=(double)mlocation.getLatitude();
        double y=(double)mlocation.getLongitude();


    }



}
