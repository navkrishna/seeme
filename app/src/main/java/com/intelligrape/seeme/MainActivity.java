package com.intelligrape.seeme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.intelligrape.seeme.loader.APICaller;
import com.intelligrape.seeme.loader.LoaderCallback;
import com.intelligrape.seeme.model.Login;
import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.model.Request;
import com.intelligrape.seeme.parser.LoginParser;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.AppConstants;
import com.intelligrape.seeme.utility.PrefStore;
import com.intelligrape.seeme.utility.Utility;

import java.util.HashMap;


public class MainActivity extends BaseActivity {

    private BaseActivity mActivity = this;
    private EditText mEtUsername, mEtPassword;
    private TextView mTvErrorMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsAndAddListener();
        setToolbar(R.id.toolbar);
    }

    private void findViewsAndAddListener() {
        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mTvErrorMessage = (TextView) findViewById(R.id.error_bar);

        findViewById(R.id.btn_login).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_forgot_password).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_register).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    Utility.clearError(mTvErrorMessage);
                    if (validate())
                        requestLogin();
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

    private boolean validate() {
        String message = "";
        String username = Utility.getText(mEtUsername);
        String password = Utility.getText(mEtPassword);
        if (username.length() == 0 && password.length() == 0)
            message = "Enter username and password.";
        else if (username.length() == 0)
            message = "Enter username";
        else if (!Utility.validate(AppConstants.EMAIL_PATTERN, username))
            message = "Enter valid email id";
        else if (password.length() == 0)
            message = "Enter password";

        if (message.length() > 0)
            Utility.setError(mTvErrorMessage, message);

        return message.length() == 0;

    }

    private void requestLogin() {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("email", Utility.getText(mEtUsername));
        paramMap.put("password", Utility.getText(mEtPassword));
        paramMap.put("actionName", ApiDetails.ACTION_LOGIN);
        final Request request = new Request();
        request.setDialogMessage(getString(R.string.progress_dialog_msg));
        request.setParamMap(paramMap);
        request.setUrl(ApiDetails.HOME_URL);
        request.setRequestType(Request.HttpRequestType.POST);
        LoginParser loginParser = new LoginParser();
        final LoaderCallback loaderCallback = new LoaderCallback(mActivity, loginParser);
        loaderCallback.requestToServer(request);
        loaderCallback.setServerResponse(new APICaller() {
            @Override
            public void onComplete(Model model) {
                if (model instanceof Login) {
                    Login login = (Login) model;
                    if (login.getStatus() == 1) {
                        PrefStore.setString(mActivity, AppConstants.PREF_KEY_EMAIL_ID, login.getEmail());
                        PrefStore.setString(mActivity, AppConstants.PREF_KEY_ACCESS_TOKEN, login.getAccessToken());
                        PrefStore.setString(mActivity, AppConstants.PREF_KEY_USER_ID, login.getUserId());
                        PrefStore.setBoolean(mActivity, AppConstants.PREF_KEY_IS_LOGGED_IN, true);
                        PrefStore.setBoolean(mActivity, AppConstants.PREF_KEY_IS_ACCOUNT_VERIFIED, login.isAccountVerified());
//                        showHomeScreen();
                    } else {
                        Utility.setError(mTvErrorMessage, login.getMessage());
                        PrefStore.clearAll(mActivity);
                    }
                    Utility.setError(mTvErrorMessage, login.getMessage());
                }
            }
        });

    }

}
