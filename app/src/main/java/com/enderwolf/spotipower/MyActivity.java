package com.enderwolf.spotipower;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;


public class MyActivity extends Activity {



    String search;
    String kindOfSearch = "artist";
    EditText mSearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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

    //chris
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.artistButton:
                if (checked)
                    kindOfSearch = "artist";
                break;
            case R.id.albumButton:
                if (checked)
                    kindOfSearch = "album";
                break;
            case R.id.trackButton:
                if (checked)
                    kindOfSearch = "track";
                break;
            case R.id.playlistButton:
                if (checked)
                    kindOfSearch = "playlist";
                break;
        }
    }

    public void Search(View view) {

        search = mSearchInput.getText().toString();


        Singleton.getInstance().playlist.deleteSongs();

        Log.d("search ", search);
        Intent intent = new Intent (this, GetSongs.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("searchQuery", search);
        mBundle.putString("kindOfSearch", kindOfSearch);
        intent.putExtras(mBundle);

        startActivity(intent);
    }

}

