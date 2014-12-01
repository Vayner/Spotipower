package com.enderwolf.spotipower.ui;

/**
 * Created by !Tulingen on 01.12.2014.
 */
public interface IGuiPlayback {

    void onPlay();
    void onPause();
    void onStop();
    void onNext();
    void onPrevious();
    void onForward();
    void onBackward();

    void onLoop(boolean state);
    void onRandom(boolean state);
}
