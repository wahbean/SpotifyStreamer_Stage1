package com.example.wmck.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class TopTrackActivityFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public TopTrackActivityFragment() {
    }

    private String countryCode = "CA"; // hard-coded country code used for Top Tracks search
    private String errorDialogTitle = "";
    private String errorMessage = "";
    private View rootView;
    private String artistName = "";
    Tracks topTracks = null;
    TrackListItemAdapter trackListAdapter;
    List<TrackListItem> trackList = new ArrayList<TrackListItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         rootView = inflater.inflate(R.layout.fragment_top_tracks, container, false);


        ListView listView = (ListView) rootView.findViewById(R.id.top_track_list);
        TextView artistHeadingView = (TextView)rootView.findViewById(R.id.artist_heading);
        trackListAdapter = new TrackListItemAdapter(getActivity(),trackList );
        //This activity is called with the aid of an explicit intent
        Intent intent = getActivity().getIntent();
        String artistId = "";

        if (intent != null)
        {

            if (intent.hasExtra(getString(R.string.artist_key))) {
                 artistId = intent.getStringExtra(getString(R.string.artist_key));
            }
            if (intent.hasExtra("artistName")) {
                artistHeadingView.setText(intent.getStringExtra("artistName"));
            }
            TopTracksTask topTrackstask = new TopTracksTask();
            topTrackstask.execute(artistId, countryCode);
        }

        listView.setAdapter(trackListAdapter);

        return rootView;
    }


    public class TopTracksTask extends AsyncTask<String, Void,Tracks >
    {
        protected Tracks doInBackground(String... params){

            String anArtistId = params[0].trim();
            String country = params[1];

           if (topTracks == null) {

               topTracks = getTracks(anArtistId, country);
           }



            return topTracks;
        }
        @Override
        protected void onPostExecute(Tracks topTracks){
            super.onPostExecute(topTracks);
            if(topTracks !=null) {
                trackListAdapter.clear();
                if (topTracks.tracks.isEmpty()) {
                    AlertHandler.showAlertDialog(getString(R.string.no_top_tracks)
                            , getString(R.string.no_top_tracks_msg), rootView.getContext());
                } else {
                    for (Track currentTrack : topTracks.tracks) {

                        TrackListItem trackListItem = new TrackListItem();
                        trackListItem.setTrackTitle(currentTrack.name);
                        trackListItem.setAlbumName(currentTrack.album.name);
                        trackListItem.setPreviewUrl(currentTrack.preview_url);

                        for (Image albumImage : currentTrack.album.images) {
                            if (albumImage.width > 600 && albumImage.width < 800) {
                                trackListItem.setThumbnailLargeUri(albumImage.url);
                            }
                            if (albumImage.width > 200 && albumImage.width < 350) {
                                trackListItem.setThumbnailSmallUri(albumImage.url);
                            }
                        }
                        trackListAdapter.add(trackListItem);
                    }
                }
            }
        }
    }

    private Tracks getTracks(String artistID, String countryCode)
    {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();

        Tracks tracks =  null;
        Map<String, Object>options = new HashMap<String, Object>();
        options.put("country", countryCode);

        try {
            tracks = spotify.getArtistTopTrack(artistID,options);
        }catch (Exception trackException){
            // The result of wireless connectivity being disabled or a problem resolving the host.
            if (trackException.getCause() instanceof UnknownHostException) {

                errorDialogTitle = getString(R.string.error_title_network);
                errorMessage = getString(R.string.error_message_network);

            }else {
                errorDialogTitle = getString(R.string.error_title);
                errorMessage = getString(R.string.error_message_spotify);
            }
            rootView.post(new Runnable() {
                @Override
                public void run() {

                   AlertHandler.showAlertDialog(errorDialogTitle, errorMessage,rootView.getContext());

                }
            });
        }

        return tracks;
    }


    @Override
    public void onCreate(Bundle currentInstanceState){
        super.onCreate(currentInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        rootView = null;
        trackListAdapter = null;
    }
}
