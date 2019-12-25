package com.sty.ne.aoplogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sty.ne.aoplogin.annotation.ClickBehavior;
import com.sty.ne.aoplogin.annotation.LoginCheck;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "sty--->";
    private Button btnLogin;
    private Button btnArea;
    private Button btnCoupon;
    private Button btnScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        btnArea = findViewById(R.id.btn_area);
        btnCoupon = findViewById(R.id.btn_coupon);
        btnScore = findViewById(R.id.btn_score);

        btnLogin.setOnClickListener(this);
        btnArea.setOnClickListener(this);
        btnCoupon.setOnClickListener(this);
        btnScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                onBtnLoginClicked();
                break;
            case R.id.btn_area:
                onBtnAreaClicked();
                break;
            case R.id.btn_coupon:
                onBtnCouponClicked();
                break;
            case R.id.btn_score:
                onBtnScoreClicked();
                break;
            default:
                break;
        }
    }

    //登录点击事件（用户行为统计）
    @ClickBehavior("登录")
    private void onBtnLoginClicked() {
        Log.i(TAG, "模拟接口请求...验证通过，登录成功");
    }
    //用户行为统计
    @ClickBehavior("我的专区")
    @LoginCheck
    private void onBtnAreaClicked() {
        Log.i(TAG, "开始跳转到 -> 我的专区 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
    //用户行为统计
    @ClickBehavior("我的优惠券")
    @LoginCheck
    private void onBtnCouponClicked() {
        Log.i(TAG, "开始跳转到 -> 我的优惠券 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
    //用户行为统计
    @ClickBehavior("我的积分")
    @LoginCheck
    private void onBtnScoreClicked() {
        Log.i(TAG, "开始跳转到 -> 我的积分 Activity");
        startActivity(new Intent(this, OtherActivity.class));
    }
}
