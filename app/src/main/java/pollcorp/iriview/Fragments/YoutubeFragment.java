package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;
import pollcorp.iriview.adapters.YoutubeAdapter;
import pollcorp.iriview.models.Video;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class YoutubeFragment extends Fragment implements AbsListView.OnItemClickListener {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	/**
	 * The fragment's ListView/GridView.
	 */
	private ListView listView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private YoutubeAdapter adapter;
	private List<Video> videos = new ArrayList<Video>();

	// TODO: Rename and change types of parameters
	public static YoutubeFragment newInstance(String param1, String param2) {
		YoutubeFragment fragment = new YoutubeFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public YoutubeFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		if (videos.isEmpty()) {
			try {
				JSONArray arr = MyApp.getInstance().getDetailResponseObj().getJSONArray("yt_videos");
				for (int i = 0; i < arr.length(); i++)
					videos.add(new Video(arr.getJSONObject(i)));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		adapter = new YoutubeAdapter(getActivity(), videos);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_youtube, container, false);
		// Set the adapter
		listView = (ListView) view.findViewById(android.R.id.list);
		if (adapter != null)
			listView.setAdapter(adapter);
		// Set OnItemClickListener so we can be notified on item clicks
		listView.setOnItemClickListener(this);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
			int mLastFirstVisibleItem = 0;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getId() == listView.getId()) {
					final int currentFirstVisibleItem = listView.getFirstVisiblePosition();

					if (currentFirstVisibleItem > mLastFirstVisibleItem) {
						// getSherlockActivity().getSupportActionBar().hide();
						((ActionBarActivity) getActivity()).getSupportActionBar().hide();
					} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
						// getSherlockActivity().getSupportActionBar().show();
						((ActionBarActivity) getActivity()).getSupportActionBar().show();
					}
					mLastFirstVisibleItem = currentFirstVisibleItem;
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + videos.get((int) id).getObjectId()));
		startActivity(intent);
	}


}
