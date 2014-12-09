package com.enderwolf.spotipower;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.enderwolf.spotipower.adapter.CustomeSongList;
import com.enderwolf.spotipower.app.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListOfQuedSongs extends Activity {

    private static final String TAG = ListOfSearchedSongs.class.getSimpleName();

    private ListView listView;
    private CustomeSongList adapter;
    private AlertDialog.Builder dialogVoteUp;
    private Playlist quedSongs;
    private static final String url = "https://api.spotify.com/v1/tracks/?ids=0eGsygTp906u18L0Oimnem,1lDWb6b6ieDQ2xT7ewTC3G";
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_qued_songs);


        quedSongs = Singleton.getInstance().getPlaylist();

        dialogVoteUp = new AlertDialog.Builder(this);  // User dialog to request a song into playlist

        listView = (ListView) findViewById(R.id.listQued);
        adapter = new CustomeSongList(this, quedSongs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialogVoteUp.setMessage(quedSongs.get(position).getName())
                    .setPositiveButton("Upvote song?", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //TODO Send song to other android system
                            // Send song upvote +1
                            // Song -> to android phone
                            // EventBus.getDefault().post(stuff here);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
                dialogVoteUp.show();
            }
        });

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1b1b1b")));

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new SongParser(),
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    hidePDialog();
                }
            }
        );

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getRequest);
    }



    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    public void refreshPlaylist(View view)
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_of_qued_songs, menu);
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

    class SongParser implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            // display response
            hidePDialog();
            Log.d("Response", response.toString());
            try {
                JSONArray type = response.getJSONArray(("tracks"));
                for (int i = 0; i < type.length(); i++) {
                    JSONObject typeItem = type.getJSONObject(i);

                    Song song = Song.newInstance(typeItem);

                    quedSongs.add(song);

                    // -- TEST CASE --


                    ArrayList<String> TestArtistList = song.getArtist();
                    for(String s : TestArtistList) {
                        System.out.println(s);
                    }

                    // -- REMOVING WHEN PUBLISHED --
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            // Makes the custome listSearched updating itself with the new songs added to songs listSearched.
            adapter.notifyDataSetChanged();
        }
    }
}
