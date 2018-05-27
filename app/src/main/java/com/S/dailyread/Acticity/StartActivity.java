package com.S.dailyread.Acticity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.S.dailyread.Acticity.R;

import java.util.Random;

public class StartActivity extends AppCompatActivity {
    private  ImageView start_image;
    private  int[] images = {R.drawable.start0,R.drawable.start1,R.drawable.start2,R.drawable.start3,R.drawable.start4};
//  可网络接入
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        隐藏标题栏
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
//       隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);      //设置窗体全屏
        setContentView(R.layout.activity_start);                          //加载开始活动
        initImage();
    }
    private void initImage(){
        start_image = (ImageView) findViewById(R.id.start_image);
        Random random = new Random();
//        开机画面随机
        int index = random.nextInt(images.length);
        start_image.setImageResource(images[index]);
        ScaleAnimation startAnimation = new ScaleAnimation(1.4f,1.0f,1.4f,1.0f, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        startAnimation.setDuration(3000);
//        动画播放完成后保持形状
        startAnimation.setFillAfter(true);
        startAnimation.setAnimationListener(new Animation.AnimationListener(){             //为动画播放添加监听
            @Override
            public void onAnimationStart(Animation animation) {                           //播放前

            }

            @Override
            public void onAnimationEnd(Animation animation) {                             //播放后
             /* 1.*/  startActivity();
//                finish();
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {                          //重复播放

            }
        });
        start_image.startAnimation(startAnimation);
    }
//    2单独测试startActivity解除测试1,2,注释1.后的语句，整合后加入MainActivity
    private void startActivity(){
        Intent intent = new Intent(StartActivity.this,MainActivity.class);
        startActivity(intent);
//        overridePendingTransition这个函数有两个参数，一个参数是第一个activity进入时的动画，另外一个参数则是第二个activity退出时的动画。
//         它必须紧挨着startActivity()或者finish()函数之后调用
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        finish();
    }

}
