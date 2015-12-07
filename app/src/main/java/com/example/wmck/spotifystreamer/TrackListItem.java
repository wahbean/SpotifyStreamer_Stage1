package com.example.wmck.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wmck on 02/07/15.
 */
public class TrackListItem implements Parcelable {
    String trackTitle;
    String albumName;
    String thumbnailSmallUri;
    String thumbnailLargeUri;
    String previewUrl;
    long   trackDuration; // in milliseconds



    public TrackListItem() {
    }

    public TrackListItem(String trackTitle, String albumName, String thumbnailSmallUri) {
        this.trackTitle = trackTitle;
        this.albumName = albumName;
        this.thumbnailSmallUri = thumbnailSmallUri;
    }

    public long getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(long trackDuration) {
        this.trackDuration = trackDuration;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.trackTitle);
        dest.writeString(this.albumName);
        dest.writeString(this.thumbnailSmallUri);
        dest.writeString(this.thumbnailLargeUri);
        dest.writeString(this.previewUrl);
        dest.writeLong(this.trackDuration);
    }

    protected TrackListItem(Parcel in) {
        this.trackTitle = in.readString();
        this.albumName = in.readString();
        this.thumbnailSmallUri = in.readString();
        this.thumbnailLargeUri = in.readString();
        this.previewUrl = in.readString();
        this.trackDuration = in.readLong();
    }

    public static final Parcelable.Creator<TrackListItem> CREATOR = new Parcelable.Creator<TrackListItem>() {
        public TrackListItem createFromParcel(Parcel source) {
            return new TrackListItem(source);
        }

        public TrackListItem[] newArray(int size) {
            return new TrackListItem[size];
        }
    };
}
