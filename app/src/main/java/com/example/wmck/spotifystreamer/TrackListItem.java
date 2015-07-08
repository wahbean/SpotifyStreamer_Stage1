package com.example.wmck.spotifystreamer;

/**
 * Created by wmck on 02/07/15.
 */
public class TrackListItem {
    String trackTitle;
    String albumName;
    String thumbnailSmallUri;
    String thumbnailLargeUri;
    String previewUrl;



    public TrackListItem() {
    }

    public TrackListItem(String trackTitle, String albumName, String thumbnailSmallUri) {
        this.trackTitle = trackTitle;
        this.albumName = albumName;
        this.thumbnailSmallUri = thumbnailSmallUri;
    }

    public String getThumbnailLargeUri() {
        return thumbnailLargeUri;
    }

    public void setThumbnailLargeUri(String thumbnailLargeUri) {
        this.thumbnailLargeUri = thumbnailLargeUri;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getThumbnailSmallUri() {
        return thumbnailSmallUri;
    }

    public void setThumbnailSmallUri(String thumbnailSmallUri) {
        this.thumbnailSmallUri = thumbnailSmallUri;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}
