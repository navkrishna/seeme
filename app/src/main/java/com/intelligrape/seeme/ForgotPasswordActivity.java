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
import com.intelligrape.seeme.utility.PrefStore;
import com.intelligrape.seeme.utility.Utility;

import java.util.HashMap;


public class ForgotPasswordActivity extends BaseActivity {
    private TextView mTvErrorMessage;
    private EditText mEtEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewsAndAddListener();
        setToolbar(R.id.toolbar);
    }

    private void findViewsAndAddListener() {
        mTvErrorMessage = (TextView) findViewById(R.id.error_bar);
        mEtEmail = (EditText) findViewById(R.id.et_email);
        findViewById(R.id.btn_send_code).setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_send_code:
                    Utility.clearError(mTvErrorMessage);
                    if (validate())
                        requestForgotPasswordCode();
                    break;
            }
        }
    };

    private boolean validate() {
        String message = "";
        String email = Utility.getText(mEtEmail);
        if (TextUtils.isEmpty(email)) {
            message = "Provide an email id";
        } else if (!Utility.validate(AppConstants.EMAIL_PATTERN, email)) {
            message = "Provide a valid email id";
        }

        if (message.length() > 0) {
            Utility.setError(mTvErrorMessage, message);
        }
        return message.length() == 0;
    }

    private void requestForgotPasswordCode() {
        HashMap<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("email", Utility.getText(mEtEmail));
        paramMap.put("actionName", ApiDetails.ACTION_FORGOT_PASSWORD);
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
//                        showHomeScreen();
                } else {
                    Utility.setError(mTvErrorMessage, model.getMessage());
                    PrefStore.clearAll(mActivity);
                }
                Utility.setError(mTvErrorMessage, model.getMessage());
            }
        });
    }

}
