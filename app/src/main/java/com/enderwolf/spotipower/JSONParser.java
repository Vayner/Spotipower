package com.enderwolf.spotipower;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      List<NameValuePair> params) {

        // Making HTTP request
        try {

            // check for request method
            if("POST".equals(method)){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else {
                if ("GET".equals(method)) {


                    Log.d("httpGet", "1");
                    // request method is GET
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    String getUrl = url + paramString;
                    HttpGet httpGet = new HttpGet(getUrl);
                    Log.d("httpGet", getUrl);
                    Log.d("httpGet", "4");

                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    //Log.d("httpGetResponce", httpResponse.toString());
                    //String responseStr = EntityUtils.toString(httpResponse.getEntity());
                    //Log.d("httpGet", responseStr);


                    HttpEntity httpEntity = httpResponse.getEntity();
                    //Log.d("httpGet entity", httpEntity.toString());
                    is = httpEntity.getContent();
                }
            }

        } catch (UnsupportedEncodingException e) {
            Log.e("JSON, Unsuported Encoding", Log.getStackTraceString(e));
        } catch (ClientProtocolException e) {
            Log.e("JSON, Protocol Exception", Log.getStackTraceString(e));
        } catch (IOException e) {
            Log.e("JSON, IO Exception", Log.getStackTraceString(e));
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;


            while ((line = reader.readLine()) != null) {
                Log.d("httpLine", line);
                sb.append(line + "\n");

            }


            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {

            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }
}

