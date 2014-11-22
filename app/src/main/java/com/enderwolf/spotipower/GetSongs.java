package com.enderwolf.spotipower;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetSongs extends Activity {


    //Playlist playlist = Singleton.getInstance().getPlaylist();
    String URLSong = "https://api.spotify.com/v1/tracks/1iMLBVEEx5XmgVV4j4WVIK";
    String URLSpotify = "https://api.spotify.com/v1/search?q=";
    String Limit = "&limit=10";
    String Offset = "&offset=0";
    String itemKind;
    String URLSearch;

    TextView mDisplaySong;

    JSONObject item = null;


    public String finalComposedURL(String _search, String _kindOfSearch){
        return URLSpotify + _search + "&type=" + _kindOfSearch + Limit + Offset ;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_songs);
        Log.d("Oncreate", "1");

        Bundle searchQuery = getIntent().getExtras();
        itemKind = searchQuery.getString("kindOfSearch") + "s";
        URLSearch = finalComposedURL(searchQuery.getString("searchQuery"), searchQuery.getString("kindOfSearch"));
        Log.d("Oncreate3", URLSearch);
        new GetSongInfoFromSpotify().execute();

        mDisplaySong = (TextView)findViewById(R.id.showSong);
        mDisplaySong.setText("");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.get_songs, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }







    JSONParser jsonParser = new JSONParser();
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TRACKS = "tracks";

    JSONArray tracks = null;





    class GetSongInfoFromSpotify extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {

            //Dialog message that shows the user that something is indeed happening and it will show as long as the process is taking to complete
            super.onPreExecute();
            pDialog = new ProgressDialog(GetSongs.this);
            pDialog.setMessage("Trying");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }



        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {

            Log.d("DoInbackground", "1");
            Log.d("URLSong  ", URLSearch);
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // getting JSON string from URL
            JSONObject json = jsonParser.makeHttpRequest(URLSearch, "GET", params);

            Log.d("DoInbackground", "2");
            // Check your log cat for JSON reponse
            Log.d(": ", json.toString());

            try {

                item = json.getJSONObject(itemKind);
                if(itemKind == "track"){
                   JSONArray images = json.getJSONArray("images");
                }


                JSONArray items = item.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject identifier = items.getJSONObject(i);



                    String id = identifier.getString("id");
                    String name = identifier.getString("name");
                    String url = images.getString("url");
                    Singleton.getInstance().playlist.addSong(id, name);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String fileURL) {
            // dismiss the dialog once got all details

            mDisplaySong.setText(Singleton.getInstance().playlist.displaySong());
            pDialog.dismiss();

        }
    }





}

