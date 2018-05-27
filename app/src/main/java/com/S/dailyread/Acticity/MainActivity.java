package com.S.dailyread.Acticity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.S.dailyread.Fragment.BaseFragment;
import com.S.dailyread.Fragment.MainFragment;
import com.S.dailyread.Fragment.ThemeFragment;
import android.widget.Toast;
import com.S.dailyread.Net.HttpUtil;



import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private static final int REQUEST_CODE_GO_TO_REGIST = 100;
    //文章的发布日期
    private String date;

    //用来实现再按一次退出程序的效果
    private boolean isExit;

    private int currentId;            //当前MainActivity中的Fragment对应的id

    public boolean isHomepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();
        currentId = -1;                     //定义当前碎片的id

        // 将指定的fragment添加到对应的容器中，最后一个参数对应的是每一个fragment的Tag
        getTransition().add(R.id.fl_content, new MainFragment(), "Fragment" + currentId).commit();    //动态添加碎片到f1_content中

        isHomepage = true;
        final TextView txtViewLogin = (TextView)findViewById(R.id.tv_login);

        txtViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = txtViewLogin.getText().toString();
                if(a.equals("请登录")){
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_GO_TO_REGIST);
                }else{
                    Toast.makeText(MainActivity.this,"已成功登陆",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {                 //处理应用遇到意外情况的问题
        super.onSaveInstanceState(outState);
    }

    private void initView() {           //
        toolbar = (Toolbar) findViewById(R.id.toolbar);                // 初始化toolbar并设置标题
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("享受阅读的乐趣");
    }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);                  //主布局包括左拉出的那个Fragment
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);          //下拉刷新组件

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,           //ActionBarDrawerToggle是一个开关，点击toolbar的home按钮就可弹出抽屉，用于打开/关闭DrawerLayout抽屉
                toolbar, R.string.app_name, R.string.app_name);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_red_light);  //设置刷新控件动画中的颜色。参数为资源id
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {                //设置刷新监听
                if (isHomepage) {                                                   //只能在主页面的时候执行刷新操作，其他就不能
                    MainFragment mainFragment = (MainFragment) getFragmentByTag("Fragment" + currentId);
                    mainFragment.getLatestArticleList();
                } else {
                    ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag("Fragment" + currentId);
                    themeFragment.refreshData();
                }
            }
        });
    }

    //获取主页
    public void getHomepage() {
        MainFragment mainFragment = (MainFragment) getFragmentByTag("Fragment" + "-1");
        ThemeFragment themeFragment = (ThemeFragment) getFragmentByTag("Fragment" + currentId);
        FragmentTransaction transition = getTransition();
        transition.hide(themeFragment);
        if (mainFragment == null) {
            transition.add(R.id.fl_content, new MainFragment(), "Fragment" + "-1").commit();
        } else {
            transition.show(mainFragment).commit();
        }
        currentId = -1;
        isHomepage = true;
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("享受阅读的乐趣");
        }
    }

    //获取主题Fragment
    public void getThemeFragment(int id, Bundle bundle) {                    //每个Fragment对应不同的id
        ThemeFragment toFragment = (ThemeFragment) getFragmentByTag("Fragment" + id);
        BaseFragment nowFragment;
        if (isHomepage) {                                                               //现在的容器中可能存在的Fragment: HomepageFragmen 和 某个主题下的Fragment
            nowFragment = (MainFragment) getFragmentByTag("Fragment" + currentId);
        } else {
            nowFragment = (ThemeFragment) getFragmentByTag("Fragment" + currentId);
        }
        FragmentTransaction transition = getTransition();
        transition.hide(nowFragment);                         //隐藏现在的Fragment, 判断目标fragment是否为空
        if (toFragment == null) {
            ThemeFragment themeFragment = new ThemeFragment();
            themeFragment.setArguments(bundle);                                                 //bundle中已经携带了将要获取的themeFragment的信息
            transition.add(R.id.fl_content, themeFragment, "Fragment" + id).commit();
        } else {
            transition.show(toFragment).commit();
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(bundle.getString("Title"));
            }
        }
        currentId = id;
        isHomepage = false;
        setRefresh(false);
    }

    @Override
    public void onBackPressed() {               //返回按键时对应的操作
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {            //如果抽屉，就关闭抽屉
            closeDrawerLayout();
            return;
        }
        if (!isHomepage) {                                                 //如果不在主页，那就回到主页
            getHomepage();
            return;
        }
        if (isExit) {
            refreshLayout.setRefreshing(false);                                  //停止刷新功能，
            HttpUtil.client.cancelAllRequests(true);          //停止所有网络请求
            super.onBackPressed();                                                  //退出应用
        } else {
            hint();                                                         // 触发再按一次退出那个提示的出现
        }
    }

    private void hint() {
        Snackbar snackbar = Snackbar.make(refreshLayout, "再按一次退出", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(Color.parseColor("#a9a9a9"));
        snackbar.setCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                isExit = false;
            }

            @Override
            public void onShown(Snackbar snackbar) {
                isExit = true;
            }
        }).show();
    }

    private FragmentTransaction getTransition() {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();         //获取FragmentTransaction实例，每次使用都得重新获取
        transition.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);        //定义转换Fragment时的动画
        return transition;
    }

    public Fragment getFragmentByTag(String tag) {                                           //利用标签寻找对应的fragment
        return getSupportFragmentManager().findFragmentByTag(tag);
    }

    public int getCurrentId() {
        return currentId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRefresh(boolean flag) {                                           // 打开 / 关闭刷新组件
        refreshLayout.setRefreshing(flag);
    }

    public void closeDrawerLayout() {
        this.drawerLayout.closeDrawer(GravityCompat.START);
    }

}
