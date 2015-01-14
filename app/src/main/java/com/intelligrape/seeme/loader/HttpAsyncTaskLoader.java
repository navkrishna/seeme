package com.intelligrape.seeme.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.model.Request;
import com.intelligrape.seeme.model.Response;
import com.intelligrape.seeme.parser.Parser;
import com.intelligrape.seeme.utility.ApiDetails;
import com.intelligrape.seeme.utility.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rajendra on 17/9/14.
 */
public class HttpAsyncTaskLoader extends AsyncTaskLoader<Model> {
    private Request request;
    private Parser parser;
    private Context context;
    private Response serverResponse;

    public HttpAsyncTaskLoader(Context context) {
        super(context);
    }

    public HttpAsyncTaskLoader(Context context, Request request, Parser parser) {
        super(context);
        this.context = context;
        this.request = request;
        this.parser = parser;
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public Model loadInBackground() {
        Utility utility = new Utility(context);
        switch (request.getRequestType()) {
            case POST:
                serverResponse = utility.doPost(request.getUrl(), getJsonParam(request.getParamMap()));
                break;
            case GET:
                break;
        }
        if (serverResponse != null) {
            return parseResponse(serverResponse);
        } else {
            return new Response();
        }
    }

    private Model parseResponse(Response serverResponse) {
        if (serverResponse.isError()) {
            Model model = new Model();
            model.setStatus(0);
            model.setMessage(serverResponse.getErrorMsg());
            return model;
        } else {
            try {
                JSONObject jsonObject = new JSONObject(serverResponse.getResponse());
                Model model = parser.parse(jsonObject);
                return model;
            } catch (JSONException e) {
                e.printStackTrace();
                Model model = new Model();
                model.setStatus(0);
                model.setMessage(e.toString());
                return model;
            }
        }
    }

    @Override
    public void deliverResult(Model data) {
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override
    public void onCanceled(Model apps) {
        super.onCanceled(apps);
        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(apps);
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
    }

    public JSONObject getJsonParam(HashMap<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        JSONObject jsonObject = getKeysJson();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            try {
                if (entry.getValue() != null) {
                    if (entry.getValue().startsWith("[")
                            && entry.getValue().endsWith("]")) {
                        JSONArray jsonArray = new JSONArray(entry.getValue());
                        jsonObject.put(entry.getKey(), jsonArray);
                    } else {
                        jsonObject.put(entry.getKey(), entry.getValue()
                                .toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

    private JSONObject getKeysJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("apiVersion", ApiDetails.API_VERSION);
            jsonObject.put("appKey", ApiDetails.APP_KEY);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public List<NameValuePair> getParams(HashMap<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            paramsList.add(new BasicNameValuePair(entry.getKey(), entry
                    .getValue()));
        }
        return paramsList;
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(Model model) {
        model = null;
    }
}
