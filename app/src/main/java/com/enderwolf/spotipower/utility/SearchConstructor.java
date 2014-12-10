package com.enderwolf.spotipower.utility;

import android.text.Html;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 * Created by vayner on 10.12.14.
 */
public class SearchConstructor {

    private static final String UrlBase = "https://api.spotify.com/v1/search?";

    private static final String UrlQuery = "query=";
    private static final String UrlOffset = "offset=";
    private static final String UrlLimit = "limit=";
    private static final String UrlType = "type=";

    public static String search (String query, int offset, int limit, Type type) {
        StringBuilder url = new StringBuilder(UrlBase);


        try {
            url.append(UrlQuery);
            url.append(URLEncoder.encode(query, "utf-8"));
            url.append('&');
            url.append(UrlOffset);
            url.append(offset);
            url.append('&');
            url.append(UrlLimit);
            url.append(limit);
            url.append('&');
            url.append(UrlType);
            url.append(type.getPart());

            return url.toString();
        } catch (UnsupportedEncodingException e) {
            Log.e("search", "Invalid url generated", e);
            return "";
        }
    }

    public enum Type {
        Track("track"),
        Album("album"),
        Artist("artist"),
        Playlist("playlist");

        private String part;

        Type(String part) {
            this.part = part;
        }

        public String getPart() {
            return this.part;
        }
    }
}
