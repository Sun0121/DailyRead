package com.S.dailyread.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.S.dailyread.Acticity.MainActivity;



public abstract class BaseFragment extends android.support.v4.app.Fragment {
    protected Activity mActivity;

    public BaseFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        return initView(inflater, container, savedInstanceState);                     //创建View
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {                       //加载活动
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }
    protected abstract View initView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState);           //等待子类重写
    protected void initData() {                                                                                      //等待子类重写

    }
    protected void hint(View view, String content, int color) {
        Snackbar snackbar = Snackbar.make(view, content, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(color);
        snackbar.show();
    }
    public MainActivity getRootActivity() {
        return (MainActivity) mActivity;
    }
}
