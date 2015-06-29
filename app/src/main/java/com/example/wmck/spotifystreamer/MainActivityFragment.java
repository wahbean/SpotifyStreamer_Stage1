package com.example.wmck.spotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	private final String LOG_TAG = this.getClass().getSimpleName();

	public MainActivityFragment() {
	}


    ArrayAdapter<String> artistListAdapter;

	String artist = "";
	EditText searchText;
	ImageButton cancelBtn;
	ImageButton.OnClickListener cancelBtnListener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			searchText.setText("");
		}
	};

    // Create some dummy data for the ListView.  Here's a sample weekly forecast
    String[] data = {
            "Sade",
            "Tom Tom Club",
            "Cold Play",
            "Pitbull",
            "Flo Rida",
            "Lady Ga-Ga",
            "Duran Duran",
            "Sia",
            "Linkin Park",
            "Bob Marley",
            "Police"
    };
    List<String> artistList = new ArrayList<String>(Arrays.asList(data));

    EditText.OnClickListener textClickListener = new EditText.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Log.d(LOG_TAG, "Search Text field has been clicked");
            searchText.setError(null);
        }
    };


    EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            String artistName = searchText.getText().toString();


            if (artistName.trim() .equals("") || artistName == null || artistName.isEmpty())
            {
                searchText.setError(getString(R.string.error_empty__search_string));
            }else
            if (actionId == EditorInfo.IME_ACTION_DONE)
                    {
                handled = true;
                        // hide virtual keyboard once the search term has been submitted
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                FetchArtistsTask     artistsTask = new FetchArtistsTask()   ;
                        artistsTask.execute(artistName);




            }
            return handled;
        }
    };


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View theView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView)theView.findViewById(R.id.artist_list);


		searchText = (EditText)theView.findViewById(R.id.search_artist);
        searchText.setOnEditorActionListener(editorActionListener);
        searchText.setOnClickListener(textClickListener);
		cancelBtn = (ImageButton)theView.findViewById(R.id.cancel_search);
		cancelBtn.setOnClickListener(cancelBtnListener);

        artistListAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_artist,
                R.id.list_item_artist_textview,
                artistList
        );
        listView.setAdapter(artistListAdapter);

		return theView;
	}

    public class FetchArtistsTask extends AsyncTask<String, Void,Void>
    {
        protected Void doInBackground(String... params){
            String anArtist = params[0];
            ArtistsPager pager =  getArtists(anArtist);
            List<Artist> resultList = pager.artists.items;



            int numOfArtists =          pager.artists.total;
            Log.d(LOG_TAG, " Total Number is " + numOfArtists);
            for (Artist artist: resultList){
                Log.d (LOG_TAG, artist.name);
            }
            return null;
        }

    }
    private ArtistsPager getArtists(String artistName)
    {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        return spotify.searchArtists(artistName);
    }
}
