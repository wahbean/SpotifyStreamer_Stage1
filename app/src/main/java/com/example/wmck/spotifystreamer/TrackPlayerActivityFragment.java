package com.example.wmck.spotifystreamer;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPlayerActivityFragment extends Fragment implements MediaPlayer.OnCompletionListener {

    private final String LOG_TAG = this.getClass().getSimpleName();

    public TrackPlayerActivityFragment() {
    }

    static final int FIRST_POSITION = 0;
    private View rootView;
    private String trackPreviewUrl = "";
    private ImageButton btnPlay;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    ImageView mThumbnailView;
    TextView mTopTrackTitle;
    TextView mAlbumName;
    private int mPosition;
    private ArrayList<TrackListItem> topTracks = new ArrayList<TrackListItem>();

    MediaPlayer mMediaPlayer = new MediaPlayer();

    ImageButton.OnClickListener playBtnListener = new ImageButton.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (mMediaPlayer != null){
                if( mMediaPlayer.isPlaying()){
                btnPlay.setImageResource(R.drawable.ic_media_play);
                mMediaPlayer.pause();
            }else{
                    playTrack(mPosition);
                    // change player's image to a pause button icon
                    btnPlay.setImageResource(R.drawable.ic_media_pause);
                }

            }

        }
    };



    @Override
    public void onCreate(Bundle currentInstanceState){
        super.onCreate(currentInstanceState);

        mMediaPlayer.setOnCompletionListener(this);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_track_player, container, false);
         mTopTrackTitle =  (TextView)rootView.findViewById(R.id.player_track);
         mAlbumName = (TextView)rootView.findViewById(R.id.player_album);
        TextView artistName = (TextView)rootView.findViewById(R.id.player_artist);
        mThumbnailView = (ImageView)rootView.findViewById(R.id.player_album_thumbnail);
        btnPlay = (ImageButton)rootView.findViewById(R.id.track_play);
        btnPrevious = (ImageButton)rootView.findViewById(R.id.track_prev);
        btnNext = (ImageButton)rootView.findViewById(R.id.track_next);

        Intent intent = getActivity().getIntent();


        if (intent != null){
            if (intent.hasExtra("currentPosition")){
                mPosition = intent.getIntExtra("currentPosition",0);

            }
            if (intent.hasExtra("topTracks")){
                topTracks = intent.getParcelableArrayListExtra("topTracks");
            }


            if (intent.hasExtra(getString(R.string.artist_name))){
                artistName.setText(intent.getStringExtra(getString(R.string.artist_name)));

            }
            mTopTrackTitle.setText(topTracks.get(mPosition).getTrackTitle());
            mAlbumName.setText(topTracks.get(mPosition).getAlbumName());
            String thumbnailUri = topTracks.get(mPosition).getThumbnailLargeUri();
            Picasso.with(rootView.getContext()).load(thumbnailUri).into(mThumbnailView);
            trackPreviewUrl = topTracks.get(mPosition).getPreviewUrl();

        }
        btnPlay.setOnClickListener(playBtnListener);

        btnPrevious.setOnClickListener(new View.OnClickListener(){
           @Override
        public void onClick(View theView){
               btnPlay.setImageResource(R.drawable.ic_media_pause);
               if (mPosition > FIRST_POSITION) { // not the first track in play list
                   mPosition = mPosition - 1;
               }
               else {
                   // play the last track
                   mPosition = topTracks.size() - 1;
               }
               updateUI(mPosition);
               playTrack(mPosition);
           }
        });

        btnNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View theView){
                btnPlay.setImageResource(R.drawable.ic_media_pause);
                if (mPosition < topTracks.size() - 1) { // not the last track in play list
                    mPosition = mPosition + 1;

                }
                else {
                    // play the first track
                    mPosition = FIRST_POSITION;
                }
                updateUI(mPosition);
                playTrack(mPosition);

            }
        });

        return rootView;
    }

    /**
     * On completion of song track redraw UI
     * */
    @Override
    public void onCompletion(MediaPlayer arg0) {

        btnPlay.setImageResource(R.drawable.ic_media_play);
    }

    public void playTrack (int trackPosition) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(topTracks.get(trackPosition).getPreviewUrl());
            mMediaPlayer.prepare(); // might take long! (for buffering, etc)
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUI(int trackPosition){
        mTopTrackTitle.setText(topTracks.get(mPosition).getTrackTitle());
        mAlbumName.setText(topTracks.get(mPosition).getAlbumName());
        String thumbnailUri = topTracks.get(mPosition).getThumbnailLargeUri();
        Picasso.with(rootView.getContext()).load(thumbnailUri).into(mThumbnailView);
    }

        @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) mMediaPlayer.release();
    }
}
