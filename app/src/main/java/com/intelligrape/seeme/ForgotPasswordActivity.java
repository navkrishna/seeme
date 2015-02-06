package com.intelligrape.seeme;

import android.os.Bundle;
import android.text.TextUtils;
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


public class ForgotPasswordActivity extends BaseActivity {
    private EditText mEtEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewsAndAddListener();
        setToolbar(R.id.toolbar, true);
    }

    private void findViewsAndAddListener() {
        mEtEmail = (EditText) findViewById(R.id.et_email);
        findViewById(R.id.btn_send_code).setOnClickListener(mOnClickListener);
        new SMTextWatcher(mEtEmail, getString(R.string.hint_email));
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send_code:
                    if (validate())
                        requestForgotPasswordCode();
                    break;
            }
        }
    };

    private boolean validate() {
        String email = Utility.getText(mEtEmail);
        if (TextUtils.isEmpty(email)) {
            Utility.setError(mEtEmail, getString(R.string.error_no_email));
        } else if (!Utility.validate(AppConstants.EMAIL_PATTERN, email)) {
            Utility.setError(mEtEmail, getString(R.string.error_invalid_email));
        } else {
            return true;
        }
        return false;
    }

    private void requestForgotPasswordCode() {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put(RequestParam.EMAIL, Utility.getText(mEtEmail));
        paramMap.put(RequestParam.ACTION_NAME, ApiDetails.ACTION_FORGOT_PASSWORD);
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
