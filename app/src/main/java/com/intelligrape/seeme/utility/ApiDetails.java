package com.intelligrape.seeme.utility;

/**
 * Created by rajendra on 17/9/14.
 */
public interface ApiDetails extends Api {
    //API Status response key
    int STATUS_CODE_SUCCESS = 1;
    int STATUS_CODE_FAILURE = 0;
    //    String HOME_URL = "http://famelive-api.qa3.intelligrape.net/api/v1";
    String API_VERSION = "1.0";
    String APP_KEY = "myAppKey";

    String ACTION_REGISTER = "register";
    String ACTION_LOGIN = "login";
    String ACTION_LOGOUT = "logout";
    String ACTION_RESEND_VERIFICATION_CODE = "resendVerificationToken";
    String ACTION_FORGOT_PASSWORD = "forgotPassword";
    String ACTION_VALIDATE_FORGOT_PASSWORD = "validateForgotPasswordCode";
    String ACTION_UPDATE_FORGOT_PASSWORD = "updateForgotPassword";
    String ACTION_VERIFY_ACCOUNT = "verifyAccount";
    String ACTION_CHANGE_PASSWORD = "changePassword";
    String ACTION_FETCH_EVENT_DETAILS = "fetchEventDetails";

    String API_KEY_STATUS = "status";
    String API_KEY_MESSAGE = "message";
    String API_KEY_DATA = "data";
    String API_KEY_EMAIL = "email";
    String API_KEY_ACCESS_TOKEN = "access_token";
    String API_KEY_USER_ID = "userId";
    String API_KEY_IS_ACCOUNT_VERIFIED = "isAccountVerified";
}
