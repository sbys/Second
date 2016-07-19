package com.example.second;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*第一个Fragment的代码，包括搜索框，流动广告，下面有附近商家推荐*/
public class Fragment1 extends Fragment {

    private static final String DATA = "data";
    private int[] bannerImages = { R.drawable.a, R.drawable.a, R.drawable.a };      //流动广告的图片
    private static ViewPager mViewPager;                                             //控制流动广告的滑动页
    private List<ImageView> mlist;                                                   //流动广告的三个ImageView
    private LinearLayout mLinearLayout;                                             //广告位下面的三个小圆点
    private BannerAdapter mAdapter;                                                  //广告位的适配器
    private BannerListener bannerListener;                                          //监听器
    private int pointIndex = 0;                                                     //圆圈标识
    private boolean isStop = false;                                                //线程标志
    private ArrayList<MyHashMap> mydata;                                            //接收activity传来的数据
    ListView mListView;                                                             //附近商家推荐
    MyAdapter myAdapter;                                                            //附近商家推荐listview的适配器
    ImageButton scan;                                                               //扫描二维码的适配器
    private  final int SCAN_CODE = 1;
    GetShop get;
    AutoCompleteTextView shopname;
    static boolean status_shopname=false;                                                      //满足点击shopname时获取焦点，要由一个判断状态的变量
    Thread thread;                                                                  //广告位的线程



    public Fragment1() {
        // Required empty public constructor
    }

    public  static Fragment1 newInstance(ArrayList<HashMap<String,Object>> data){
        if(data==null){
            return null;
        }
        Fragment1 fragment = new Fragment1();
        Bundle args = new Bundle();
        args.putParcelableArrayList(DATA,changeData(data) );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mydata = getArguments().getParcelableArrayList(DATA);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_fragment1,container,false);
        mViewPager = (ViewPager) rootview.findViewById(R.id.container1);
        mLinearLayout = (LinearLayout)rootview.findViewById(R.id.points);
        mListView=(ListView)rootview.findViewById(R.id.listview);
        shopname=(AutoCompleteTextView)rootview.findViewById(R.id.search);
        scan=(ImageButton)rootview.findViewById(R.id.scan);
        initData();
        initAction();
        mViewPager.setCurrentItem(0);
        thread=new Thread(new Runnable() {

                                 @Override
                         public void run() {
                                 while (!isStop) {
                                         SystemClock.sleep(5000);
                                         getActivity().runOnUiThread(new Runnable() {

                                                         @Override
                                                 public void run() {
                                                             if(mViewPager.getCurrentItem()==2){
                                                                 mViewPager.setCurrentItem(0);
                                                             }
                                                             else {
                                                         mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);}
                                                     }
                                             });

                                     }
                             }
                     });
        thread.start();

        return rootview;
    }
    class BannerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % bannerImages.length;
            mLinearLayout.getChildAt(newPosition).setEnabled(true);
            mLinearLayout.getChildAt(pointIndex).setEnabled(false);
            pointIndex = newPosition;                                       // 更新标志位
        }

    }

        @Override
         public  void onDestroy() {
                 isStop = true;
                 super.onDestroy();
             }
        private void initData() {
                 mlist = new ArrayList<ImageView>();
                 View view;
                 ViewGroup.LayoutParams params;
                 for (int i = 0; i < bannerImages.length; i++) {
                         // 设置广告图
                     ImageView imageView = new ImageView(getContext());
                     imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                     imageView.setBackgroundResource(bannerImages[i]);
                     mlist.add(imageView);
                         // 设置圆圈点
                     view = new View(getContext());
                     params = new ViewGroup.LayoutParams(20, 20);
                     view.setBackgroundResource(R.drawable.point_back);
                     view.setLayoutParams(params);
                     view.setEnabled(false);
                     mLinearLayout.addView(view);
                     }
                 mAdapter = new BannerAdapter(mlist);
                 mViewPager.setAdapter(mAdapter);
                 myAdapter=new MyAdapter(getContext(),mydata);   //实例化要有数据
                 mListView.setAdapter(myAdapter);
        /*item点击事件*/
                 mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                         get.getShop(mydata.get(position).map.get("ID").toString());
                     }
                 });
                 scan.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (status_shopname) {
                             /*按照用户输入进入商家主页*/
                             String str = shopname.getText().toString().trim();
                                     /*下面应该是发送数据的方法得到shangjiaid*/
                             String shopid = "8008208820";
                             get.getShop(shopid);

                         } else {

                             /*通过扫一扫进入下一个商家主页*/
                             Intent intent = new Intent(getContext(), CaptureActivity.class);
                             getActivity().startActivityForResult(intent, SCAN_CODE);
//                                getActivity().startActivity(intent);
                         }

                     }
                 });

        }

    private void initAction() {
        bannerListener = new BannerListener();
        mViewPager.setOnPageChangeListener(bannerListener);
        //取中间数来作为起始位置
        int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mlist.size());
        //用来出发监听器
        mViewPager.setCurrentItem(index);
        mLinearLayout.getChildAt(pointIndex).setEnabled(true);
        shopname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!str.equals("")) {
                    status_shopname=true;
                    Toast.makeText(getActivity(), "" + str.toString(), Toast.LENGTH_SHORT).show();
                    //自动提示为了方便，不用Handler，用异步
                    new MyAsyncTask().execute(str);


                }
                else
                {
                    status_shopname=false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /*接口*/

    public interface GetShop{
        void getShop(String shopid);

    }





    /* listview的适配器*/
    public static class MyAdapter extends BaseAdapter{
    ArrayList<MyHashMap> mData;
    LayoutInflater inflater;
    public MyAdapter(Context context,ArrayList<MyHashMap> data){
        this.mData=data;
        this.inflater=LayoutInflater.from(context);

    }


    public int getCount(){
        return mData.size();
    }
    public View getView(int position, View convertView, ViewGroup parent){ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listview_line1, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar);
            viewHolder.ratingNum=(TextView)convertView.findViewById(R.id.ratingNum);
            viewHolder.info=(TextView)convertView.findViewById(R.id.info);
            viewHolder.benefit1=(TextView)convertView.findViewById(R.id.benefit1);
            viewHolder.benefit2=(TextView)convertView.findViewById(R.id.benefit2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //viewHolder.imageView.setBackgroundResource((Integer) mData.get(position).get("image"));
        viewHolder.title.setText(mData.get(position).map.get("title").toString());
        viewHolder.ratingBar.setNumStars(5);
        viewHolder.ratingBar.setRating((Integer) mData.get(position).map.get("numStars"));
        viewHolder.ratingNum.setText( mData.get(position).map.get("numStars").toString());
        viewHolder.info.setText(" 月售："+mData.get(position).map.get("sales").toString() + "  /  距离：" + mData.get(position).map.get("distance").toString()+"千米");
        viewHolder.benefit1.setText("优惠一："+mData.get(position).map.get("benefit1").toString());
        viewHolder.benefit2.setText("优惠二："+mData.get(position).map.get("benefit2").toString());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }
    class ViewHolder {
        ImageView imageView;
        TextView title;
        RatingBar ratingBar;
        TextView ratingNum;
        TextView info;
        TextView benefit1;
        TextView benefit2;
    }
}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        get=(GetShop)context;
    }
    /*转化数据*/
    public static ArrayList<MyHashMap> changeData(ArrayList<HashMap<String,Object>> data){
        ArrayList<MyHashMap> finalData=new ArrayList<MyHashMap>();
        for(HashMap<String,Object> map:data){
            MyHashMap myHashMap=new MyHashMap();
            myHashMap.map=map;
            finalData.add(myHashMap);
        }

        return finalData;}


    /*实现搜索框自动提示的异步任务新建内部类*/



    class MyAsyncTask extends AsyncTask<String,Integer,String> {
        List<String> suggestions=new ArrayList<String>();
        ArrayAdapter<String> mAdapter;
        @Override
        protected String doInBackground(String... newText) {
            /*这里参数就是搜索框的输入，根据这个参数在服务器的数据库里进行查询,下面是一个例子，
            得到数据之后加到suggestions中就可,方法里return null即可,但是现在发现这里好像有个
            尴尬的问题，异步任务应该只能执行一次，看后期测试吧*/

//            String newText = key[0];
//            newText = newText.trim();
//            newText = newText.replace(" ", "+");
//            try{
//                HttpClient hClient = new DefaultHttpClient();
//                HttpGet hGet = new HttpGet("http://en.wikipedia.org/w/api.php?action=opensearch&search="+newText+"&limit=8&namespace=0&format=json");
//                ResponseHandler<String> rHandler = new BasicResponseHandler();
//                data = hClient.execute(hGet,rHandler);
//                suggest = new ArrayList<String>();
//                JSONArray jArray = new JSONArray(data);
//                for(int i=0;i<jArray.getJSONArray(1).length();i++){
//                    String SuggestKey = jArray.getJSONArray(1).getString(i);
//                    suggest.add(SuggestKey);
//                }
//
//            }catch(Exception e){
//                Log.w("Error", e.getMessage());
//            }
//            return null;
//        }
//
//    }
            suggestions.add("我是歌手");
            suggestions.add("我是率比");
            suggestions.add("我是一具尸体");
            return null;
}

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mAdapter=new ArrayAdapter<String>(getContext(),R.layout.search_suggestions,R.id.suggestion,suggestions);
            shopname.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }



}

    @Override
    public void onDestroyView() {
        isStop=true;
        super.onDestroyView();

    }

    @Override
    public void onResume() {
        isStop=false;
        super.onResume();
    }
}

















