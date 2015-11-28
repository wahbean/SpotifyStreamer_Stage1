package com.example.wmck.spotifystreamer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

	private final String LOG_TAG = this.getClass().getSimpleName();
    static  final String ARTIST_TAG = "ARTIST";
    private ArtistSelectedCallback mCallbacks;


	public MainActivityFragment() {
	}


    public static MainActivityFragment getNewInstance(int index){
        MainActivityFragment artistFragment = new MainActivityFragment();

        Bundle args = new Bundle();
        args.putInt(ARTIST_TAG,index);
        artistFragment.setArguments(args);
        return artistFragment;
    }
    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface ArtistSelectedCallback {
        /**
         * TopTrackActivityCallback for when an item has been selected.
         */
        public void onArtistSelected(String artistId, String artistName);
    }

    private String errorDialogTitle = "";
    private String errorMessage = "";


    ArtistListItemAdapter artistListItemAdapter;
    View rootView;


    String artist = "";
	EditText searchText;
	ImageButton cancelBtn;
	ImageButton.OnClickListener cancelBtnListener = new ImageButton.OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			searchText.setText(""); // clears the current search value when the ImageButton is clicked
		}
	};


    List<String> artistList = new ArrayList<String>();
    List<ArtistListItem> artistListItems = new ArrayList<ArtistListItem>();

    EditText.OnClickListener textClickListener = new EditText.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            searchText.setError(null); // erases any previous error message dialogs
        }
    };


    EditText.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;

            String artistName = searchText.getText().toString();

            if (artistName.trim() .equals("") ||  artistName.isEmpty())
            {
                searchText.setError(getString(R.string.error_empty__search_string));
            }else
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == MotionEvent.ACTION_DOWN))
                    {
                handled = true;
                        // hide virtual keyboard once the search term has been submitted
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                ArtistsTask artistsTask = new ArtistsTask()   ;
                artistsTask.execute(artistName);

            }
            return handled;
        }
    };


    @Override
    public void onCreate(Bundle currentInstanceState){
        super.onCreate(currentInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        // remove references to View object.
        rootView = null;
        artistListItemAdapter = null;

    }


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.artist_list);


		searchText = (EditText) rootView.findViewById(R.id.search_artist);
        searchText.setOnEditorActionListener(editorActionListener);
        searchText.setOnClickListener(textClickListener);
		cancelBtn = (ImageButton) rootView.findViewById(R.id.cancel_search);
		cancelBtn.setOnClickListener(cancelBtnListener);

        artistListItemAdapter = new ArtistListItemAdapter(getActivity(),artistListItems);

        listView.setAdapter(artistListItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String artistId = artistListItemAdapter.getItem(position).getArtistId();
                String artistName = artistListItemAdapter.getItem(position).getArtistName();
                ((ArtistSelectedCallback) getActivity()).onArtistSelected(artistId,artistName);
                /*
                Intent intent = new Intent(getActivity(), TopTrackActivity.class);
                intent.putExtra(getString(R.string.artist_key), artistId);
                intent.putExtra(getString(R.string.artist_name), artistName);
                startActivity(intent);
                */


            }
        });

		return rootView;
	}

    public class ArtistsTask extends AsyncTask<String, Void,List<Artist> >
    {
        protected List<Artist> doInBackground(String... params){
            String anArtist = params[0].trim();
            ArtistsPager pager =  getArtists(anArtist);
            List<Artist> resultList = new ArrayList<Artist>();

            if (pager != null)
            {
                resultList = pager.artists.items;
            }

            return resultList;
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            if (artists != null){

                if (artists.isEmpty()){
                    rootView.post(new Runnable() {
                        @Override
                        public void run() {
                            AlertHandler.showAlertDialog(getString(R.string.alert_no_results)
                                    , getString(R.string.try_another_search),rootView.getContext());
                        }
                    });
                }
                artistListItemAdapter.clear();
               for (Artist anArtist: artists){

                   for (Image image : anArtist.images){

                   }
                   Image artistImage;
                   String imageUrl = "";
                   if ( ! anArtist.images.isEmpty()){
                       //get the first image URL
                       artistImage = anArtist.images.get(0);
                       imageUrl = artistImage.url;

                   }
                   ArtistListItem currentItem = new ArtistListItem(anArtist.name,imageUrl, anArtist.id);
                   artistListItemAdapter.add(currentItem);
               }
            }
        }

    }

    private ArtistsPager getArtists(String artistName)
    {
        SpotifyApi api = new SpotifyApi();
        SpotifyService spotify = api.getService();
        ArtistsPager artistsPager = null;

        try{
            artistsPager = spotify.searchArtists(artistName);
        }
        catch (Exception ex){


          // The result of wireless connectivity being disabled or a problem with the network.
            if (ex.getCause() instanceof UnknownHostException) {

                errorDialogTitle = getString(R.string.error_title_network);
                errorMessage = getString(R.string.error_message_network);

            }else {
                errorDialogTitle = getString(R.string.error_title);
                errorMessage = getString(R.string.error_message_spotify);
            }
            rootView.post(new Runnable() {
                @Override
                public void run() {

                   AlertHandler.showAlertDialog(errorDialogTitle, errorMessage, rootView.getContext());

                }
            });
        }

        return artistsPager;
    }

@Override
    public void onAttach(Activity activity){
    super.onAttach(activity);
    mCallbacks = (ArtistSelectedCallback)activity;

}

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }



}
