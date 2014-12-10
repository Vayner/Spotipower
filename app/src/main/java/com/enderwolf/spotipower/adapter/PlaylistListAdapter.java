package com.enderwolf.spotipower.adapter;

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
 * ListAdapter for a Playlist for fancy viewing
 * Created by chris on 06.12.2014.
 */
public class PlaylistListAdapter extends BaseAdapter {
    private final Context context;
    private final List<Song> songs;

    private LayoutInflater inflater;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public PlaylistListAdapter(Context context, List<Song> songs) {

        this.context = context;
        this.songs = songs;
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

        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        NetworkImageView thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView artist = (TextView) convertView.findViewById(R.id.artist);
        TextView album = (TextView) convertView.findViewById(R.id.album_name);
        TextView time = (TextView) convertView.findViewById(R.id.time);

        // getting which song position
        Song s = songs.get(position);

        // thumbnail image url
        thumbNail.setImageUrl(s.getThumbnailUrl(Song.Quality.Medium), imageLoader);

        // title
        title.setText(s.getName());

        // artist
        artist.setText("Artist: " + String.valueOf(s.getStringOfArtists()));

        // release album
        album.setText(String.valueOf(s.getAlbumName()));

        StringBuilder timeString = new StringBuilder();
        timeString.append(String.valueOf( s.getTotalTime() / 60000 ));
        timeString.append(':');

        String sec = String.valueOf( (s.getTotalTime() / 1000) % 60 );

        if(sec.length() == 1) {
            timeString.append('0');
        }

        timeString.append(sec);

        time.setText(timeString.toString());

        return convertView;
    }
}
