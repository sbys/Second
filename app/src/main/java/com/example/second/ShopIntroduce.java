package com.example.second;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ShopIntroduce extends AppCompatActivity {
    TextView title;                                                                                 //商家名字
    ImageView shoppicture;                                                                          //商家图片
    RatingBar mRatringbar;                                                                          //商家评价
    TextView numRating;                                                                             //数字评价
    TextView info;                                                                                  //销量和距离
    TextView benefit1;                                                                               //优惠活动1
    TextView benefit2;                                                                              //优惠活动2
    TabLayout mTabLayout;                                                                           //标签页
    ViewPager mViewPager;                                                                           //放fragment
    ImageButton back;                                                                               //后退
    String[]  titles={"商品","评价","商家"};
    FragmentManager mfragmentManager;
    MyPagerAdater myPagerAdater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_introduce);
        String str=getIntent().getStringExtra("shopid");
        Toast.makeText(ShopIntroduce.this,""+str.toString(),Toast.LENGTH_SHORT).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        getDta();
        initAction();

    }
    public void initView(){
        title=(TextView)findViewById(R.id.shopname);
        shoppicture=(ImageView)findViewById(R.id.image_view);
        mRatringbar=(RatingBar)findViewById(R.id.ratingBar);
        numRating=(TextView)findViewById(R.id.ratingNum);
        info=(TextView)findViewById(R.id.info);
        benefit1=(TextView)findViewById(R.id.benefit1);
        benefit2=(TextView)findViewById(R.id.benefit2);
        mTabLayout=(TabLayout)findViewById(R.id.shop_tab);
        mViewPager=(ViewPager)findViewById(R.id.myContainer);
        back=(ImageButton)findViewById(R.id.back);

    }
    public void initAction(){
        /*要写的内容*/
//        title.setText();
//        shoppicture.setImageResource();
//        mRatringbar.setRating();
//        numRating.setText();
//        info.setText();       //两部分
//        benefit1.setText("优惠活动A： "+);
//        benefit2.setText("优惠活动B： "+);
        mfragmentManager=getSupportFragmentManager();
        myPagerAdater=new MyPagerAdater(mfragmentManager);
        mViewPager.setAdapter(myPagerAdater);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mViewPager.setCurrentItem(0);
                        break;
                    case 1:
                        mViewPager.setCurrentItem(1);
                        break;
                    case 2:
                        mViewPager.setCurrentItem(2);
                        break;
                    default:
                        mViewPager.setCurrentItem(0);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    /*写一个根据传过来的商家id得到数据的方法*/
    public void getDta(){}


    /*fragment的适配器*/
    class MyPagerAdater extends FragmentPagerAdapter{
        public MyPagerAdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment= Shop_Fragment1.newInstance("","");
                    break;
                case 1:
                    fragment=Shop_Fragment2.newInstance("","");
                    break;
                case 2:
                    fragment=Shop_Fragment3.newInstance("","");

                    break;
                default:
                    fragment=null;
            }
            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position].toString();

        }
    }

}
