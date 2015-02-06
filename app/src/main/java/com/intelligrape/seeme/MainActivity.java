package com.intelligrape.seeme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.intelligrape.seeme.loader.APICaller;
import com.intelligrape.seeme.loader.LoaderCallback;
import com.intelligrape.seeme.model.Login;
import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.model.Request;
import com.intelligrape.seeme.parser.LoginParser;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.AppConstants;
import com.intelligrape.seeme.utility.PrefStore;
import com.intelligrape.seeme.utility.RequestParam;
import com.intelligrape.seeme.utility.SMTextWatcher;
import com.intelligrape.seeme.utility.Utility;

import java.util.HashMap;


public class MainActivity extends BaseActivity {

    private EditText mEtEmail, mEtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsAndAddListener();
        setToolbar(R.id.toolbar);
    }

    private void findViewsAndAddListener() {
        mEtEmail = (EditText) findViewById(R.id.et_email);
        mEtPassword = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_login).setOnClickListener(mOnClickListener);
        findViewById(R.id.tv_forgot_password).setOnClickListener(mOnClickListener);
        findViewById(R.id.btn_register).setOnClickListener(mOnClickListener);

        new SMTextWatcher(mEtEmail, getString(R.string.hint_email));
        new SMTextWatcher(mEtPassword, getString(R.string.hint_password));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
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
        String message;
        String email = Utility.getText(mEtEmail);
        String password = Utility.getText(mEtPassword);
        if (email.length() == 0) {
            message = getString(R.string.error_no_email);
            Utility.setError(mEtEmail, message);
        } else if (!Utility.validate(AppConstants.EMAIL_PATTERN, email)) {
            message = getString(R.string.error_invalid_email);
            Utility.setError(mEtEmail, message);
        } else if (password.length() == 0) {
            message = getString(R.string.error_no_password);
            Utility.setError(mEtPassword, message);
        } else {
            return true;
        }
        return false;

    }

    private void requestLogin() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(RequestParam.EMAIL, Utility.getText(mEtEmail));
        paramMap.put(RequestParam.PASSWORD, Utility.getText(mEtPassword));
        paramMap.put(RequestParam.ACTION_NAME, ApiDetails.ACTION_LOGIN);
        Request request = new Request();
        request.setDialogMessage(getString(R.string.progress_dialog_msg));
        request.setParamMap(paramMap);
        request.setUrl(ApiDetails.HOME_URL);
        request.setRequestType(Request.HttpRequestType.POST);
        LoaderCallback loaderCallback = new LoaderCallback(mActivity, new LoginParser());
        loaderCallback.requestToServer(request);
        loaderCallback.setServerResponse(new APICaller() {
            @Override
            public void onComplete(Model model) {
                if (model instanceof Login) {
                    Login login = (Login) model;
                    Utility.showToastMessage(mActivity, model.getMessage());
                    if (login.getStatus() == 1) {
                        PrefStore.setString(mActivity, AppConstants.PREF_KEY_EMAIL_ID, login.getEmail());
                        PrefStore.setString(mActivity, AppConstants.PREF_KEY_ACCESS_TOKEN, login.getAccessToken());
                        PrefStore.setString(mActivity, AppConstants.PREF_KEY_USER_ID, login.getUserId());
                        PrefStore.setBoolean(mActivity, AppConstants.PREF_KEY_IS_LOGGED_IN, true);
                        PrefStore.setBoolean(mActivity, AppConstants.PREF_KEY_IS_ACCOUNT_VERIFIED, login.isAccountVerified());
                        startActivity(new Intent(mActivity, ValidateAccount.class));
                        finish();
                    } else {
                        Utility.showToastMessage(mActivity, login.getMessage());
                        PrefStore.clearAll(mActivity);
                    }
                }
            }
        });

    }

}
