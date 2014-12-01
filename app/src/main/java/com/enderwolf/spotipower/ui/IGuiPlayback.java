package com.enderwolf.spotipower.ui;

/**
 * Created by !Tulingen on 01.12.2014.
 */
public interface IGuiPlayback {

    void onPlayPressed();
    void onPausePressed();
    void onStopPressed();
    void onNextPressed();
    void onPreviousPressed();
    void onForwardPressed();
    void onBackwardPressed();

    void onLoopPressed(boolean state);
    void onRandomPressed(boolean state);
}
