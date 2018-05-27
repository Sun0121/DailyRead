package com.S.dailyread.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.S.dailyread.Adapter.ArticleListAdapter;
import com.S.dailyread.Bean.ArticleBefore;
import com.S.dailyread.Bean.ArticleLatest;
import com.S.dailyread.Bean.TopStories;
import com.S.dailyread.Listener.OnLoadBeforeArticleListener;
import com.S.dailyread.Listener.OnLoadLatestArticleListener;
import com.S.dailyread.Listener.OnSlideToTheBottomListener;
import com.S.dailyread.Net.HttpUtil;


import java.util.List;

import com.S.dailyread.Acticity.R;

public class MainFragment extends BaseFragment {
    //文章列表
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;

    private ArticleListAdapter adapter;                   //设置recycleView的填充器

    private OnLoadLatestArticleListener latestListener;

    private OnLoadBeforeArticleListener beforeListener;

    private boolean flag;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_article_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.articleList);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);    //注册监听，点击便回到recycleView的顶部
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(0);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        return view;
    }
    protected void initData() {
        adapter = new ArticleListAdapter(mActivity);                  //此处为填充recyclerView添加适配器
        recyclerView.setAdapter(adapter);
//      加载最新文章事件监听
        latestListener = new OnLoadLatestArticleListener() {
            @Override
            public void onSuccess(ArticleLatest articleLatest) {
                adapter.setData(articleLatest);
                getRootActivity().setDate(articleLatest.getDate());
                List<TopStories> topStoriesList = articleLatest.getTop_stories();
                if (adapter.loadTopArticleListener != null) {
                    adapter.loadTopArticleListener.onSuccess(topStoriesList);
                }
                stopRefresh();
                if (!flag) {
                    flag = true;
                } else {
                    hint(recyclerView, "已经是最新文章啦", Color.parseColor("#a9a9a9"));
                }
//               加载最新文章成功后在后台再加载下一页
                getBeforeArticleList();
            }

            @Override
            public void onFailure() {
                if (mActivity != null) {

                    hint(recyclerView, "好奇怪，文章加载不来", Color.parseColor("#a9a9a9"));

                }
                stopRefresh();
            }
        };
//        加载过去文章事件监听
        beforeListener = new OnLoadBeforeArticleListener() {
            @Override
            public void onSuccess(ArticleBefore articleBefore) {
                adapter.addData(articleBefore.getStories());
                adapter.notifyDataSetChanged();
                getRootActivity().setDate(articleBefore.getDate());
            }

            @Override
            public void onFailure() {
                if (mActivity != null) {
                    hint(recyclerView, "好奇怪，文章加载不来", Color.parseColor("#a9a9a9"));

                }
            }
        };
//        滑动到底部事件监听
        OnSlideToTheBottomListener slideListener = new OnSlideToTheBottomListener() {                   //主界面滑动到底部进行刷新
            @Override
            public void onSlideToTheBottom() {
                getBeforeArticleList();
            }
        };
        adapter.setSlideToTheBottomListener(slideListener);                  //为adapter添加滑动到底部事件监听
        getLatestArticleList();

    }
    public void getLatestArticleList() {              //获取最近出现的文章
        if (!HttpUtil.isNetworkConnected(mActivity)) {
            hint(recyclerView, "似乎没有连接网络？", Color.parseColor("#a9a9a9"));
            stopRefresh();
            return;
        }
        HttpUtil.getLatestArticleList(latestListener);
    }
    public void getBeforeArticleList() {             //获取之前的文章
        if (!HttpUtil.isNetworkConnected(mActivity)) {
            hint(recyclerView, "似乎没有连接网络？", Color.parseColor("#a9a9a9"));
            return;
        }
        HttpUtil.getBeforeArticleList(getRootActivity().getDate(), beforeListener);
    }
    public void stopRefresh() {
        if (getRootActivity() != null) {
            getRootActivity().setRefresh(false);
        }
    }
}
