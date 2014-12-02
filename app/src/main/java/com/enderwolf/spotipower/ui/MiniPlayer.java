package com.enderwolf.spotipower.ui;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import com.enderwolf.spotipower.R;
import com.enderwolf.spotipower.event.PlayBackProgressEvent;
import de.greenrobot.event.EventBus;

/**
 * Activities that contain this fragment must implement the
 * {@link IGuiPlayback} interface
 * to handle interaction events.
 * Use the {@link MiniPlayer#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class MiniPlayer extends Fragment {
    private IGuiPlayback listener;
    private ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MiniPlayer.
     */
    // TODO: Rename and change types and number of parameters
    public static MiniPlayer newInstance() {
        MiniPlayer fragment = new MiniPlayer();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public MiniPlayer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mini_player, container, false);

        ImageButton next = (ImageButton) root.findViewById(R.id.mini_next);
        ImageButton play = (ImageButton) root.findViewById(R.id.mini_play);
        ImageButton prev = (ImageButton) root.findViewById(R.id.mini_prev);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) {
                    return;
                }

                listener.onNextPressed();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) {
                    return;
                }

                listener.onPlayPressed();
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener == null) {
                    return;
                }

                listener.onPreviousPressed();
            }
        });

        progressBar = (ProgressBar) root.findViewById(R.id.mini_progress);

        return root;
    }

    public void onEvent(PlayBackProgressEvent event) {
        progressBar.setProgress(event.progress);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (IGuiPlayback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
       EventBus.getDefault().unregister(this);
    }
}
