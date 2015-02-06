package com.intelligrape.seeme.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.intelligrape.seeme.R;
import com.intelligrape.seeme.model.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rajendra on 17/9/14.
 */
public class Utility {
    private final int timeoutConnection = 10 * 1000; //in Miliseconds
    private final int timeoutSocket = 15 * 1000;
    private Context context;

    public Utility(Context context) {
        this.context = context;
    }

    public Response doGet(String url) {
        Response response = new Response();
        if (!isNetworkAvailable(context)) {
            String error = context.getResources().getString(R.string.no_internet_connection);
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            response.setError(true);
            response.setErrorMsg(error);
            return response;
        }
        try {
            url = encodeURL(url);
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Content-type", "application/json");
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream in = entity.getContent();
                    String result = readStream(in);
                    response.setResponse(result);
                    response.setError(false);
                } else {
                    response.setError(true);
                    response.setErrorMsg("Error : Entity is null");
                }
            } else {
                response.setError(true);
                response.setErrorMsg("Error : Response status code is not Ok. Response status code : " + statusLine.getStatusCode());
            }
        } catch (Exception e) {
            response.setError(true);
            response.setErrorMsg("Error : " + e.toString());
        }
        Logger.i("Utility", response.getResponse());
        return response;
    }

    public Response doPost(String url, JSONObject jsonObject) {
        Log.e("url", url);
        Response response = new Response();
        if (!isNetworkAvailable(context)) {
            String error = context.getResources().getString(R.string.no_internet_connection);
            Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            response.setError(true);
            response.setErrorMsg(error);
            return response;
        }
        try {
            url = encodeURL(url);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json");
            if (jsonObject.has("accessToken")) {
                httpPost.setHeader("X-Auth-Token", jsonObject.getString("accessToken"));
                Logger.i("X-Auth-Token", jsonObject.getString("accessToken"));
                jsonObject.remove("accessToken");
            }
            StringEntity se = new StringEntity(jsonObject.toString());
            httpPost.setEntity(se);
            Logger.i("API Url::", url + " " + jsonObject.toString());
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            StatusLine statusLine = httpResponse.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream in = entity.getContent();
                    String result = readStream(in);
                    response.setError(false);
                    response.setResponse(result);
                } else {
                    response.setError(true);
                    response.setErrorMsg("Error : Entity is null");
                }
            } else {
                response.setError(true);
                response.setErrorMsg("Error : Response status code is not Ok. Response status code : " + statusLine.getStatusCode());
            }
        } catch (Exception e) {
            response.setError(true);
            response.setErrorMsg("Error : " + e.toString());
        }
        Logger.i("Utility", response.getResponse());
        return response;
    }

    public Response httpPostRequestToServer(String URL, Object paramsList) {
        String userAgent = "(Android; Mobile) Chrome";
        int TIME_OUT = 30000;
        String data = null;
        Log.e("url", URL);
        Response response = new Response();
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIME_OUT);
        HttpConnectionParams.setSoTimeout(httpParams, TIME_OUT);
        HttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpPost httppost = new HttpPost(URL);
        httppost.setHeader("User-Agent", userAgent);
        InputStream is = null;
        try {
            if (paramsList != null)
                httppost.setEntity(new UrlEncodedFormEntity(
                        (List<? extends NameValuePair>) paramsList));
            HttpResponse httpResponse = httpclient.execute(httppost);
            HttpEntity httpEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 204) {
                data = null;
                response.setError(true);
                response.setErrorMsg("Error : " + statusCode);
            }
            if (statusCode == 200) {
                is = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "iso-8859-1"));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                data = sb.toString();
                is.close();
                response.setError(false);
                response.setResponse(data);
                Log.e("data", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setError(true);
            response.setErrorMsg("Error : Response status code is not Ok. Response status code : ");
        }
        return response;
    }

    /*
    *
    * Below Method is used for the utility of the get and post method.
     */
    String readStream(InputStream inputStream) throws IOException {
//        InputStream in = new FileInputStream(new File("C:/temp/test.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        reader.close();
        return out.toString();
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                || connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING) {
            return true;
        } else if (connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {
            return true;
        } else
            return false;
    }

    public String encodeURL(String url) {
        return url.replaceAll(" ", "%20");
    }

    public static void showToastMessage(Context context, String msg) {
        Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
    }

    public int getAppVersion() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.d("RegisterActivity", "I never expected this! Going down, going down!" + e);
            throw new RuntimeException(e);
        }
    }

    public static String getText(TextView textView) {
        return textView != null ? textView.getText().toString().trim() : "";
    }

    public static void setError(EditText editText, String message) {
        editText.setText("");
        editText.setHint(message);
        editText.setBackgroundResource(R.drawable.custom_edit_text_material_error);
        editText.requestFocus();
    }

    public static void clearError(EditText editText, String hint) {
        editText.setHint(hint);
        editText.setBackgroundResource(R.drawable.custom_edit_text_material);
    }

    public static boolean validate(String regexPattern, String field) {
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(field);
        return matcher.matches();
    }
}
