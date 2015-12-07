package com.example.wmck.spotifystreamer;

/**
 * Created by wmck on 01/07/15.
 */
public class ArtistListItem {
    String artistName;
    String artistIconUri;
    String artistId;



    public ArtistListItem(String artistName, String artistIconUri, String artistId) {
        this.artistName = artistName;
        this.artistIconUri = artistIconUri;
        this.artistId = artistId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistIconUri() {
        return artistIconUri;
    }

    public void setArtistIconUri(String artistIconUri) {
        this.artistIconUri = artistIconUri;
    }
}
