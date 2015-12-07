package com.example.wmck.spotifystreamer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wmck.spotifystreamer.ArtistListItem;
import com.example.wmck.spotifystreamer.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wmck on 01/07/15.
 */
public class ArtistListItemAdapter extends ArrayAdapter<ArtistListItem> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public ArtistListItemAdapter(Context context,  List<ArtistListItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ArtistListItem artistListItem = getItem(position);
        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_artist_icon);
        if (iconView != null && !artistListItem.getArtistIconUri().trim().isEmpty()){
            // use Picasso to display the image URL
            Picasso.with(getContext()).load(artistListItem.getArtistIconUri()).into(iconView);
        }else // display a default image for the album thumbnail
        if (artistListItem.getArtistIconUri().trim().isEmpty()){
            iconView.setImageResource(R.drawable.free_album);
        }

        TextView artistNameView = (TextView)convertView.findViewById(R.id.list_item_artist_name);
        artistNameView.setText(artistListItem.getArtistName());

        return convertView;
    }
}
