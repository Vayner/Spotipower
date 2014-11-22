package com.enderwolf.spotipower;



/**
 * Created by chris on 29.10.2014.
 */
public class Song {


    private String id;
    private String name;
    String urlImage;

    public Song(){};



    public Song(String id, String name, String URL){
        this.id = id;
        this.name = name;
        this.urlImage = URL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() { return urlImage; }

    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }


}




