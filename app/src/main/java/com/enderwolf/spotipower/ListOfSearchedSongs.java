package com.enderwolf.spotipower;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.enderwolf.spotipower.adapter.CustomeSongList;
import com.enderwolf.spotipower.app.AppController;

public class ListOfSearchedSongs extends Activity {

    private static final String TAG = ListOfSearchedSongs.class.getSimpleName();

    private static final String url = "https://api.spotify.com/v1/search?query=flame&offset=0&limit=20&type=track";
    private ProgressDialog pDialog;
    private List<Song> Songs = new ArrayList<Song>();
    private ListView listView;
    private CustomeSongList adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_searched_songs);

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomeSongList(this, Songs);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id){
                System.out.println("Working " + Songs.get(position).getName());


            }
        });

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        hidePDialog();
                        Log.d("Response", response.toString());
                        try {
                            JSONObject type = response.getJSONObject(("tracks"));
                            JSONArray items = type.getJSONArray("items");
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject typeItem = items.getJSONObject(i);           // Every track
                                JSONObject album = typeItem.getJSONObject("album");     //get the album the song is in
                                JSONArray artists = typeItem.getJSONArray("artists");    //get all artist who is featured in the song
                                String albumName = album.getString("name");                  // get album name
                                JSONArray images = album.getJSONArray("images");        // get array of different image quality
                                JSONObject thumbnailUrl = images.getJSONObject(1);      //get medium quality [0 = bad] [ 1 = medium] [2 = good]
                                String imageURL = thumbnailUrl.getString("url");    // get url to the image used in the album

                                ArrayList<String> artistList = new ArrayList<String>();

                                Log.d("Response value of artist", String.valueOf(artists.length()));

                                for (int j = 0; j < artists.length(); j++) {
                                    JSONObject artist = artists.getJSONObject(j);
                                    artistList.add(artist.getString("name"));
                                    Log.d("Response", artist.getString("name") );
                                }



                                Song song = new Song();
                                song.setId(typeItem.getString("id"));
                                song.setName(typeItem.getString("name"));
                                song.setAlbumName(albumName);
                                song.setThumbnailUrl(imageURL);
                                song.setArtist(artistList);
                                Songs.add(song);


                                System.out.println("id: " + String.valueOf(song.getId())+ "\n");
                                System.out.println("name: " + song.getName() + "\n");
                                System.out.println("album name: " + song.getAlbumName() + "\n");
                                System.out.println("image " + song.getThumbnailUrl() + "\n");

                                ArrayList<String> n1artistList = song.getArtist();
                                for(String s : n1artistList)
                                {
                                    System.out.println(s);
                                }







                                //Log.d("Response 1", name + " image:  " + ThumbnailUrl);
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        hidePDialog();

                    }
                });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(getRequest);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_of_searched_songs, menu);
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
}
