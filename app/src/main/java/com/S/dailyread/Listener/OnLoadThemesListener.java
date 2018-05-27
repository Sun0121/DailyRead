package com.S.dailyread.Listener;

//加载文章主题列表事件监听（就是左拉抽屉中那个Fragment的列表）

import com.S.dailyread.Bean.Others;

import java.util.List;

public interface OnLoadThemesListener {
    void onSuccess(List<Others> othersList);

    void onFailure();
}
