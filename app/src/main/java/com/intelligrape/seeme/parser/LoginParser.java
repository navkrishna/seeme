package com.intelligrape.seeme.parser;

import com.intelligrape.seeme.model.Login;
import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rajendra on 18/9/14.
 */
public class LoginParser implements Parser<Model> {
    @Override
    public Model parse(JSONObject json) throws JSONException {
        Logger.i("LoginParser", json.toString());
        Login login = new Login();
        login.setStatus(json.getInt(ApiDetails.API_KEY_STATUS));
        login.setMessage(json.getString(ApiDetails.API_KEY_MESSAGE));
        if (json.getInt(ApiDetails.API_KEY_STATUS) == 1) {
            JSONObject dataJSON = json.getJSONObject(ApiDetails.API_KEY_DATA);
            login.setAccessToken(dataJSON.getString(ApiDetails.API_KEY_ACCESS_TOKEN));
            login.setEmail(dataJSON.getString(ApiDetails.API_KEY_EMAIL));
            login.setUserId(dataJSON.getString(ApiDetails.API_KEY_USER_ID));
            login.setAccountVerified(dataJSON.getBoolean(ApiDetails.API_KEY_IS_ACCOUNT_VERIFIED));
        }
        return login;
    }
}
