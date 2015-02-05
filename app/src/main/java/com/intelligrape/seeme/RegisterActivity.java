package com.intelligrape.seeme;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.intelligrape.seeme.loader.APICaller;
import com.intelligrape.seeme.loader.LoaderCallback;
import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.model.Request;
import com.intelligrape.seeme.parser.MessageParser;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.AppConstants;
import com.intelligrape.seeme.utility.Utility;

import java.util.HashMap;

/**
 * Created by Navkrishna on Jan 01, 2015
 */
public class RegisterActivity extends BaseActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private TextView tvErrorBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        findViewsAndAddListener();
        setToolbar(R.id.toolbar, true);
    }

    private void findViewsAndAddListener() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);
        tvErrorBar = (TextView) findViewById(R.id.error_bar);
        findViewById(R.id.btn_register).setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    Utility.clearError(tvErrorBar);
                    if (validate())
                        registerUser();
                    break;
            }
        }
    };

    private boolean validate() {
        String message = "";
        String userName = Utility.getText(etUsername);
        String email = Utility.getText(etEmail);
        String password = Utility.getText(etPassword);
        String confirmPassword = Utility.getText(etConfirmPassword);

        if (TextUtils.isEmpty(userName)) {
            message = "Provide username";
        }
        if (TextUtils.isEmpty(email)) {
            message += "\nEnter your email id";
        } else if (!Utility.validate(AppConstants.EMAIL_PATTERN, email)) {
            message += "\nEnter a valid email id";
        }
        if (TextUtils.isEmpty(password)) {
            message += "\nProvide a password";
        } else if (password.length() < 6) {
            message += "\nMinimum length of password is 6";
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            message += "\nConfirm your password";
        } else if (!password.equals(confirmPassword)) {
            message += "\nPassword and confirm password does not match";
        }

        if (message.length() > 0) {
            Utility.setError(tvErrorBar, message.trim());
        }

        return message.length() == 0;
    }

    private void registerUser() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("username", Utility.getText(etUsername));
        paramMap.put("email", Utility.getText(etEmail));
        paramMap.put("password", Utility.getText(etPassword));
        paramMap.put("actionName", ApiDetails.ACTION_REGISTER);
        final Request request = new Request();
        request.setDialogMessage(getString(R.string.progress_dialog_msg));
        request.setParamMap(paramMap);
        request.setUrl(ApiDetails.HOME_URL);
        request.setRequestType(Request.HttpRequestType.POST);
        final LoaderCallback loaderCallback = new LoaderCallback(mActivity, new MessageParser());
        loaderCallback.requestToServer(request);
        loaderCallback.setServerResponse(new APICaller() {
            @Override
            public void onComplete(Model model) {
                if (model.getStatus() == 1) {
                    finish();
                    Utility.showToastMessage(mActivity, model.getMessage());
                } else {
                    Utility.setError(tvErrorBar, model.getMessage());
                }
            }
        });
    }
}
