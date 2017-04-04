package com.yulu.zhaoxinpeng.listviewdemo_facetest.first_activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.yulu.zhaoxinpeng.listviewdemo_facetest.R;
import com.yulu.zhaoxinpeng.listviewdemo_facetest.first_activity.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 如何在ScrollView 中嵌套 ListView？
 * 方法一：布局中手动设置listview高度
 * 方法二：动态设置listview的高度
 * 方法三：自定义ListView
 */
public class MainActivity extends AppCompatActivity {
    @BindView(R.id.listview)
    ListView listview;
    /*@BindView(R.id.linearlayout)
    LinearLayout linearlayout;*/
    /*@BindView(R.id.scrollView)
    ScrollView scrollView;*/

    private List<String> list;
    private Handler mHandler;
    private ArrayAdapter<String> mAdapter;
    private Button mButton;
    private ProgressBar mProgressBar;
    private int visibleLastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler();

        ButterKnife.bind(this);

        list = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            list.add("这是第" + i + "条数据");
        }

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);

        //addFootView(listview);

        listview.setAdapter(mAdapter);

        //ListView实现分页加载
        listview.setOnScrollListener(listener);

        //方法二：动态设置listview的高度
        //setListViewHeight(listview);
    }

    //分页加载，当用户滑动到ListView底部时，自动加载数据
    //实现：在滑动监听中，判断滑动是否静止，并且判断是否滑动到了底部（最后一条数据）
    AbsListView.OnScrollListener listener = new AbsListView.OnScrollListener() {
        //        SCROLL_STATE_IDLE;//静止状态
//        SCROLL_STATE_FLING;//滑翔状态（惯性滚动）
//        SCROLL_STATE_TOUCH_SCROLL;//触摸滑动（手指摁下移动）
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当滚动状态改变时触发的方法
            //最后一条数据下标
            int lastIndex = mAdapter.getCount() - 1;

            //判断滑动是否静止，并且判断是否滑动到了底部（最后一条数据）
            if (scrollState == SCROLL_STATE_IDLE && visibleLastIndex == lastIndex) {
                Log.e("==============","222222222222222222");
                loadMoreData();
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onScroll(AbsListView
                                     view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            //正在滑动时触发的方法
            visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            //      可见最后一条数据下标      可见的第一条下标        可见所有item数量
        }
    };

    //添加脚布局方法
    private void addFootView(MyListView listview) {
        View view = LayoutInflater.from(this).inflate(R.layout.listview_foot_view, null);
        listview.addFooterView(view);
        mButton = (Button) view.findViewById(R.id.button);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButton.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //加载更多
                        loadMoreData();
                        //视图操作，更新UI
                        mButton.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        //刷新数据
                        mAdapter.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });
    }

    /**
     * 方法二：动态设置listview的高度
     *
     * @param lv
     */
    //动态设置listView高度（计算所有item的总高度，设置给listView）
    public void setListViewHeight(ListView lv) {
        //获取ListView的adapter
        ListAdapter listAdapter = lv.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            //listAdapter.getCount()返回item的条数
            View item = listAdapter.getView(i, null, lv);  //看参数。。。ctrl + p
            //先绘制一下,计算item宽高
            item.measure(0, 0);
            //统计所有子项总高度
            totalHeight += item.getMeasuredHeight();
        }

        //设置listView高度
        ViewGroup.LayoutParams params = lv.getLayoutParams();

//        listView.getDividerHeight()//获取分割线(分隔符)高度
        params.height = totalHeight + (lv.getDividerHeight() * (listAdapter.getCount() - 1));

        lv.setLayoutParams(params);
    }


    private void loadMoreData() {
        for (int i = 0; i < 5; i++) {
            list.add("第" + i + "条数据（新）");
        }
    }
}
