package com.S.dailyread.Listener;

import com.S.dailyread.Bean.ArticleContent;

//加载文章内容事件监听
public interface OnLoadArticleContentListener {
    void onSuccess(ArticleContent content);

    void onFailure();
}
