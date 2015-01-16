package com.intelligrape.seeme.parser;

import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Navkrishna on 17/1/15.
 */
public class MessageParser implements Parser<Model> {
    @Override
    public Model parse(JSONObject json) throws JSONException {
        Logger.i("LoginParser", json.toString());
        Model model = new Model();
        model.setStatus(json.getInt(ApiDetails.API_KEY_STATUS));
        model.setMessage(json.getString(ApiDetails.API_KEY_MESSAGE));
        return model;
    }
}
