package com.enderwolf.spotipower.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.app.AppController;

import java.util.List;

/**
 * Created by chris on 06.12.2014.
 */
public class CustomeSongList extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Song> songs;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomeSongList(Activity activity, List<Song> songs) {
        this.activity = activity;
        this.songs = songs;

        System.out.println("size of songs" + songs.size());

    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int location) {
        return songs.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);

        TextView album = (TextView) convertView.findViewById(R.id.albumnName);

        // getting which song position
        Song s = songs.get(position);

        // thumbnail image url
        thumbNail.setImageUrl(s.getThumbnailUrl(), imageLoader);

        // title
        title.setText(s.getName());

        // artist
        artist.setText("Artist: " + String.valueOf(s.getStringOfArtists()));

        // release album
        album.setText(String.valueOf(s.getAlbumName()));

        return convertView;
    }
}