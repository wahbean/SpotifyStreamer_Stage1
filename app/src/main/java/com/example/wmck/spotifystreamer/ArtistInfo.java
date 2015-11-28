package com.example.wmck.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wmck on 27/11/15.
 */
public class ArtistInfo implements Parcelable {
    String artistName;
    String artistId;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    @Override public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<ArtistInfo> CREATOR = new Creator<ArtistInfo>() {
        public ArtistInfo createFromParcel(Parcel source) {
            ArtistInfo mArtistInfo = new ArtistInfo();
            mArtistInfo.artistId = source.readString();
            mArtistInfo.artistName = source.readString();

            return mArtistInfo;
        }
        public ArtistInfo[] newArray(int size) {
            return new ArtistInfo[size];
        }
    };

        public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(artistId);
        parcel.writeString(artistName);

    }
}
