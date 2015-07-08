package com.example.wmck.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wmck on 03/07/15.
 */
public class TrackListItemAdapter extends ArrayAdapter<TrackListItem> {
    private final String LOG_TAG = this.getClass().getSimpleName();

    public TrackListItemAdapter(Context context,  List<TrackListItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_track, parent, false);
        }

        TrackListItem trackItem = getItem(position);
        ImageView thumbnailView = (ImageView)convertView.findViewById(R.id.album_thumbnail);

        if (thumbnailView != null && !trackItem.getThumbnailSmallUri().isEmpty()){
            Picasso.with(getContext()).load(trackItem.getThumbnailSmallUri()).into(thumbnailView);
        }else
        if(trackItem.getThumbnailSmallUri().isEmpty()){
            thumbnailView.setImageResource(R.drawable.free_album);
        }

        TextView albumNameView = (TextView)convertView.findViewById(R.id.album_name);
        albumNameView.setText(trackItem.getAlbumName());
        TextView trackNameView = (TextView)convertView.findViewById(R.id.track_title);
        trackNameView.setText(trackItem.getTrackTitle());
        return convertView;
    }
}
