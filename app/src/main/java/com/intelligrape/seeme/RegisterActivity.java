package com.intelligrape.seeme;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.intelligrape.seeme.loader.APICaller;
import com.intelligrape.seeme.loader.LoaderCallback;
import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.model.Request;
import com.intelligrape.seeme.parser.MessageParser;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.AppConstants;
import com.intelligrape.seeme.utility.RequestParam;
import com.intelligrape.seeme.utility.SMTextWatcher;
import com.intelligrape.seeme.utility.Utility;

import java.util.HashMap;

/**
 * Created by Navkrishna on Jan 01, 2015
 */
public class RegisterActivity extends BaseActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;

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
        findViewById(R.id.btn_register).setOnClickListener(mOnClickListener);
        new SMTextWatcher(etUsername, getString(R.string.hint_username));
        new SMTextWatcher(etEmail, getString(R.string.hint_email));
        new SMTextWatcher(etPassword, getString(R.string.hint_password));
        new SMTextWatcher(etConfirmPassword, getString(R.string.hint_confirm_password));
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_register:
                    if (validate())
                        registerUser();
                    break;
            }
        }
    };

    private boolean validate() {
        String userName = Utility.getText(etUsername);
        String email = Utility.getText(etEmail);
        String password = Utility.getText(etPassword);
        String confirmPassword = Utility.getText(etConfirmPassword);

        if (userName.isEmpty()) {
            Utility.setError(etUsername, getString(R.string.error_no_username));
        } else if (email.isEmpty()) {
            Utility.setError(etEmail, getString(R.string.error_no_email));
        } else if (!Utility.validate(AppConstants.EMAIL_PATTERN, email)) {
            Utility.setError(etEmail, getString(R.string.error_invalid_email));
        } else if (password.isEmpty()) {
            Utility.setError(etPassword, getString(R.string.error_no_password));
        } else if (password.length() < 6) {
            Utility.setError(etPassword, getString(R.string.error_invalid_password));
        } else if (confirmPassword.isEmpty()) {
            Utility.setError(etConfirmPassword, getString(R.string.error_no_confirm_password));
        } else if (!password.equals(confirmPassword)) {
            Utility.setError(etConfirmPassword, getString(R.string.error_password_does_not_match));
        } else {
            return true;
        }

        return false;
    }

    private void registerUser() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(RequestParam.USERNAME, Utility.getText(etUsername));
        paramMap.put(RequestParam.EMAIL, Utility.getText(etEmail));
        paramMap.put(RequestParam.PASSWORD, Utility.getText(etPassword));
        paramMap.put(RequestParam.ACTION_NAME, ApiDetails.ACTION_REGISTER);
        Request request = new Request();
        request.setDialogMessage(getString(R.string.progress_dialog_msg));
        request.setParamMap(paramMap);
        request.setUrl(ApiDetails.HOME_URL);
        request.setRequestType(Request.HttpRequestType.POST);
        LoaderCallback loaderCallback = new LoaderCallback(mActivity, new MessageParser());
        loaderCallback.requestToServer(request);
        loaderCallback.setServerResponse(new APICaller() {
            @Override
            public void onComplete(Model model) {
                Utility.showToastMessage(mActivity, model.getMessage());
                if (model.getStatus() == 1) {
                    finish();
                }
            }
        });
    }
}
