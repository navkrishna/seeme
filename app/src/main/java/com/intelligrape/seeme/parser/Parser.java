package com.intelligrape.seeme.parser;

import com.intelligrape.seeme.model.Model;

import org.json.JSONException;
import org.json.JSONObject;

public interface Parser<T extends Model> {
    public abstract T parse(JSONObject json) throws JSONException;
}
