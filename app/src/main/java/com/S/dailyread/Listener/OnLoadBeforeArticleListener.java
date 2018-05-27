package com.S.dailyread.Listener;

import com.S.dailyread.Bean.ArticleBefore;

//加载过去文章事件监听
public interface OnLoadBeforeArticleListener {
    void onSuccess(ArticleBefore articleBefore);

    void onFailure();
}
