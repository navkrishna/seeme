package com.intelligrape.seeme.loader;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Toast;

import com.intelligrape.seeme.R;
import com.intelligrape.seeme.model.Model;
import com.intelligrape.seeme.model.Request;
import com.intelligrape.seeme.parser.Parser;

/**
 * Created by rajendra on 17/9/14.
 */
public class LoaderCallback implements LoaderManager.LoaderCallbacks<Model> {
    private Request request;
    private Activity activity;
    private Parser parser;
    private APICaller apiCaller;

    public void setServerResponse(APICaller apiCaller) {
        this.apiCaller = apiCaller;
    }

    public LoaderCallback(Activity activity, Parser parser) {
        this.activity = activity;
        this.parser = parser;
    }

    @Override
    public void onLoaderReset(Loader<Model> modelLoader) {
        modelLoader = null;
    }

    @Override
    public void onLoadFinished(Loader<Model> modelLoader, Model model) {
        dismissDialog();
        activity.getLoaderManager().destroyLoader(request.getId());
        onResponseFromServer(model);
    }

    public void onResponseFromServer(Model model) {
        apiCaller.onComplete(model);
    }

    @Override
    public Loader<Model> onCreateLoader(int i, Bundle bundle) {
        boolean showDialog = bundle.getBoolean("showDialog", true);
        if (showDialog) {
            showDialog(activity);
        }
        return new HttpAsyncTaskLoader(activity, request, parser);
    }

    public final void requestToServer(Request request) {
        /**
         * Checking the network Here. That network connection is available or not.
         */
        if (!hasConnectivity(activity)) {
            showMsg(activity.getString(R.string.no_internet_connection));
            return;
        }
        this.request = request;
        Bundle bundle = new Bundle();
        bundle.putBoolean("showDialog", request.isShowDialog());
        activity.getLoaderManager().initLoader(request.getId(), bundle, this);
//        activity.getLoaderManager().restartLoader(request.getId(),bundle,this);
    }

    public boolean hasConnectivity(Context context) {
        boolean rc = false;
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable()
                    && cm.getActiveNetworkInfo().isConnected()) {
                rc = true;
            }
        }
        return rc;
    }

    public void showMsg(String msg) {
        Toast.makeText(activity, "" + msg, Toast.LENGTH_SHORT).show();
    }

    private ProgressDialog pd;

    void showDialog(Context context) {
        if (context == null)
            return;
        if (pd != null) {
            pd.dismiss();
        }
        pd = new ProgressDialog(context);
        pd.setMessage(request.getDialogMessage());
        pd.setCancelable(true);
        pd.setIndeterminate(true);
        pd.show();
    }

    void dismissDialog() {
        if (pd != null) {
            pd.dismiss();
        }
    }
}
