package com.example.wmck.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPlayerActivityFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public TrackPlayerActivityFragment() {
    }

    private View rootView;
    private String trackPreviewUrl = "";
    private ImageButton btnPlay;

    MediaPlayer mMediaPlayer = new MediaPlayer();

    ImageButton.OnClickListener playBtnListener = new ImageButton.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Log.d(LOG_TAG, "onClick listener for Play Button ");
            Log.d(LOG_TAG, "Audio URL is '" + trackPreviewUrl + "'");
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mMediaPlayer.setDataSource(trackPreviewUrl);
                mMediaPlayer.prepare(); // might take long! (for buffering, etc)
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_track_player, container, false);
        TextView topTrackTitle =  (TextView)rootView.findViewById(R.id.player_track);
        TextView albumName = (TextView)rootView.findViewById(R.id.player_album);
        TextView artistName = (TextView)rootView.findViewById(R.id.player_artist);
        ImageView thumbnailView = (ImageView)rootView.findViewById(R.id.player_album_thumbnail);
        btnPlay = (ImageButton)rootView.findViewById(R.id.track_play);

        Intent intent = getActivity().getIntent();


        if (intent != null){
            if (intent.hasExtra(getString(R.string.artist_name))){
                artistName.setText(intent.getStringExtra(getString(R.string.artist_name)));

            }
            if (intent.hasExtra("trackTitle")){
                topTrackTitle.setText(intent.getStringExtra("trackTitle"));
            }
            if (intent.hasExtra("albumName")){
                albumName.setText(intent.getStringExtra("albumName"));
            }
            if (intent.hasExtra("thumbnailUri")){
                String thumbnailUri = intent.getStringExtra("thumbnailUri");
                Picasso.with(rootView.getContext()).load(thumbnailUri).into(thumbnailView);
            }
            if (intent.hasExtra(getString(R.string.track_preview_url))){
                trackPreviewUrl = intent.getStringExtra(getString(R.string.track_preview_url));
            }
        }
        btnPlay.setOnClickListener(playBtnListener);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) mMediaPlayer.release();
    }
}
