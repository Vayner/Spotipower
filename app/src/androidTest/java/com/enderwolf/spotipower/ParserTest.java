package com.enderwolf.spotipower;

import android.test.suitebuilder.annotation.MediumTest;

import com.enderwolf.spotipower.utility.ParseCompleteCallback;
import com.enderwolf.spotipower.utility.Parser;

import junit.framework.TestCase;

/**
 * Created by vayner on 10.12.14.
 */
public class ParserTest extends TestCase{

    private static final String id = "2061tUGBzVsZvRdJS3D4hD";
    private static final String uri = "spotify:track:" + id;

    private Playlist playlist = null;

    @MediumTest
    public void TestParser () {

        Parser.ParseLookup(uri, new ParseCompleteCallback() {
            @Override
            public void OnParseComplete(Playlist playlist) {
                assertTrue(playlist != null);

                Song song = playlist.get(0);
                assertEquals(song.getSongUri(), uri);
                assertEquals(song.getId(), id);
            }
        });
    }

}
