package com.example.second;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Shop_Fragment1 extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView categoryRecycler;                                                                  //菜的种类
    RecyclerView dishRecycler;                                                                      //菜
//d
//    List<Category> list=new ArrayList<Category>();
    private int oldSelectedPosition = 0;
    private boolean needMove=false;
    private LinearLayoutManager mDishLayoutManager;
    private LinearLayoutManager mCategoryLayoutManager;
    private int movePosition;


    public Shop_Fragment1() {

    }


    public static Shop_Fragment1 newInstance(String param1, String param2) {
        Shop_Fragment1 fragment = new Shop_Fragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview=inflater.inflate(R.layout.fragment_shop__fragment1, container, false);
        categoryRecycler=(RecyclerView)rootview.findViewById(R.id.recyclerview_left);
        dishRecycler=(RecyclerView)rootview.findViewById(R.id.recyclerview_right);
        mDishLayoutManager=new LinearLayoutManager(getContext());
        mCategoryLayoutManager=new LinearLayoutManager(getContext());
//        initData();
//        initAction();
        return rootview;
    }
//    public void initAction(){
//        mcategoryAdapter=new CategoryAdapter(getContext(),list);
//        mdishAdapter=new DishAdapter(getContext(),list);
//        mcategoryAdapter.setOnItemClickListener(this);
//        categoryRecycler.setAdapter(mcategoryAdapter);
//        dishRecycler.setAdapter(mdishAdapter);
//
//        /*加标题*/
//        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mdishAdapter);
//        dishRecycler.addItemDecoration(headersDecor);
////        dishRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
////            @Override
////            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
////                super.onScrollStateChanged(recyclerView, newState);
////            }
////
////            @Override
////            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
////                int firstVisibleItem = mDishLayoutManager.findFirstCompletelyVisibleItemPosition();
////                int lastVisibleItem = mDishLayoutManager.findLastVisibleItemPosition();
////                //此判断，避免左侧点击最后一个item无响应
////                if (lastVisibleItem != mDishLayoutManager.getItemCount() - 1) {
////                    int sort = mdishAdapter.getType(firstVisibleItem);
////                    changeSelected(sort);
////                } else {
////                    changeSelected(mcategoryAdapter.getItemCount() - 1);
////                }
////                if (needMove) {
////                    needMove = false;
////                    //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
////                    int n = movePosition - mDishLayoutManager.findFirstVisibleItemPosition();
////                    if (0 <= n && n < dishRecycler.getChildCount()) {
////                        //获取要置顶的项顶部离RecyclerView顶部的距离
////                        int top = dishRecycler.getChildAt(n).getTop() - dip2px(getActivity(), 28);
////                        //最后的移动
////                        dishRecycler.scrollBy(0, top);
////                    }
////                }
////            }
////
////
////        });
//
//
//
//
//
//    }
//    public void initData(){
//        HashMap<String,Object> map=new HashMap<String, Object>();
//        map.put("name", "fkshfs");
//        ArrayList<HashMap<String,Object>> lit=new ArrayList<HashMap<String,Object>>();
//        lit.add(map);
//        lit.add(map);
//        Category category=new Category("类型",lit);
//        list.add(category);
//        list.add(category);
//
//    }
//
//    @Override
//    public void onItemClick(int position) {
//        changeSelected(position);
//        moveToThisSortFirstItem(position);
//
//    }
//    private void changeSelected(int position) {
//        list.get(oldSelectedPosition).setSelected(false);
//        list.get(position).setSelected(true);
//        oldSelectedPosition = position;
//        mcategoryAdapter.notifyDataSetChanged();
//    }
//    private void moveToThisSortFirstItem(int position) {
//        movePosition = 0;
//        for(int i=0;i<position;i++){
//            movePosition += mdishAdapter.getList().get(i).getDishs().size();}
//        moveToPosition(movePosition);
//    }
//
//    private void moveToPosition(int n) {
//        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
//        int firstItem = mDishLayoutManager.findFirstVisibleItemPosition();
//        int lastItem = mCategoryLayoutManager.findLastVisibleItemPosition();
//        //然后区分情况
//        if (n <= firstItem ){
//            //当要置顶的项在当前显示的第一个项的前面时
//            dishRecycler.scrollToPosition(n);
//        }else if ( n <= lastItem ){
//            //当要置顶的项已经在屏幕上显示时
//            int top = dishRecycler.getChildAt(n - firstItem).getTop();
//            dishRecycler.scrollBy(0, top-dip2px(getContext(),28));
//        }else{
//            //当要置顶的项在当前显示的最后一项的后面时
//            dishRecycler.scrollToPosition(n);
//            movePosition = n;
//            needMove = true;
//        }
//    }
    /**
     * 根据手机分辨率从dp转成px
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return(int) (dpValue * scale + 0.5f);
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
