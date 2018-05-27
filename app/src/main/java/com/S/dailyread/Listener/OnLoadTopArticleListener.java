package com.S.dailyread.Listener;

//加载顶部Banner文章事件监听

import com.S.dailyread.Bean.TopStories;

import java.util.List;

public interface OnLoadTopArticleListener {
    void onSuccess(List<TopStories> topStoriesList);

    void onFailure();
}
