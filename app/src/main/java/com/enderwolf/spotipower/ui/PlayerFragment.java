package com.enderwolf.spotipower.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.Song;
import com.enderwolf.spotipower.app.AppController;
import com.enderwolf.spotipower.event.SongUpdateEvent;
import com.enderwolf.spotipower.utility.LruBitmapCache;

import de.greenrobot.event.EventBus;

public class PlayerFragment extends Fragment {

    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Song currentSong = null;
    NetworkImageView thumbNail;
    ImageView blurredBackground;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlayerFragment.
     */
    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player, container, false);

        thumbNail = (NetworkImageView) root.findViewById(R.id.albumHighCenter);
        blurredBackground = (ImageView) root.findViewById(R.id.backgroundBlurred);






        return root;
    }

    public void onEvent(SongUpdateEvent event){
        if(!event.song.equals(currentSong)){
            currentSong = event.song;

           //System.out.println("Image url large: " + currentSong.getThumbnailUrl(Song.Quality.Large));
            imageLoader.get(currentSong.getThumbnailUrl(Song.Quality.Small), new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                            blurredBackground.setImageBitmap(blurImage(imageContainer));
                            blurredBackground.setScaleX(8.0f);
                            blurredBackground.setScaleY(8.0f);
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
            thumbNail.setImageUrl(currentSong.getThumbnailUrl(Song.Quality.Large), imageLoader);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private Bitmap blurImage(ImageLoader.ImageContainer imageLoader){


        if(imageLoader.getBitmap() != null) {


            Bitmap blurred = imageLoader.getBitmap();
            blurred = Bitmap.createScaledBitmap(blurred, blurred.getWidth() / 4, blurred.getHeight() / 4, true);
            blurred = Bitmap.createScaledBitmap(blurred, blurred.getWidth() * 4, blurred.getHeight() * 4, true);

            return blurred;
        }else return null;

    }
}


