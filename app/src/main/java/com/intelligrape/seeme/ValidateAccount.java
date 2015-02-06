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
import com.intelligrape.seeme.utility.PrefStore;
import com.intelligrape.seeme.utility.RequestParam;
import com.intelligrape.seeme.utility.SMTextWatcher;
import com.intelligrape.seeme.utility.Utility;

import java.util.HashMap;


public class ValidateAccount extends BaseActivity {
    private EditText mEtCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_account);
        setToolbar(R.id.toolbar);
        findViewsAndAddListener();
    }

    private void findViewsAndAddListener() {
        mEtCode = (EditText) findViewById(R.id.et_code);
        findViewById(R.id.btn_verify_account).setOnClickListener(mOnClickListener);
        new SMTextWatcher(mEtCode, getString(R.string.hint_email));
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_verify_account:
                    if (Utility.getText(mEtCode).isEmpty()) {
                        Utility.setError(mEtCode, getString(R.string.error_no_validation_code));
                    } else {
                        requestVerifyAccount();
                    }
                    break;
            }
        }
    };

    private void requestVerifyAccount() {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(RequestParam.VERIFICATION_TOKEN, Utility.getText(mEtCode));
        paramMap.put(RequestParam.EMAIL, PrefStore.getString(mActivity, AppConstants.PREF_KEY_EMAIL_ID));
        paramMap.put(RequestParam.ACTION_NAME, ApiDetails.ACTION_VERIFY_ACCOUNT);
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
                    PrefStore.setBoolean(mActivity, AppConstants.PREF_KEY_IS_ACCOUNT_VERIFIED, true);
//                    startActivity(new Intent(mActivity, ValidateAccount.class));
                }
            }
        });

    }
}
