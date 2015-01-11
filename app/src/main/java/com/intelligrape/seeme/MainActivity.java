package com.intelligrape.seeme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends BaseActivity {

    private BaseActivity mActivity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsAndAddListener();
        setToolbar(R.id.toolbar);
    }

    private void findViewsAndAddListener() {
        findViewById(R.id.btn_login).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_forgot_password).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_register).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    break;
                case R.id.tv_forgot_password:
                    Intent intentForgotPassword = new Intent(mActivity, ForgotPasswordActivity.class);
                    startActivity(intentForgotPassword);
                    break;
                case R.id.btn_register:
                    Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intentRegister);
                    break;
            }
        }
    };

}
