package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;

import pollcorp.iriview.adapters.ProConAdapter;
import pollcorp.iriview.models.ProsCons;
import pollcorp.iriview.models.RoundImage;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ReviewsFragment extends Fragment implements AbsListView.OnItemClickListener, AbsListView.OnScrollListener {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private List<ProsCons> list;

	private OnFragmentInteractionListener mListener;

	/**
	 * The fragment's ListView/GridView.
	 */
	private ListView mListView;
	private ProConAdapter adapter;
	private TextView tvHeadline;
	private TextView tvOveralScore;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private ImageView backgroundImage;
	private int lastTopValue = 0;
	private int mLastFirstVisibleItem = 0;
	NetworkImageView mNetworkImageView;
	private String IMAGE_URL;

	// TODO: Rename and change types of parameters
	public static ReviewsFragment newInstance(String param1, String param2) {
		ReviewsFragment fragment = new ReviewsFragment();
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
	public ReviewsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		list = new ArrayList<ProsCons>();
		adapter = new ProConAdapter(getActivity(), list);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_reviews, container, false);
		mListView = (ListView) view.findViewById(android.R.id.list);
		// inflate custom header and attach it to the list
		ViewGroup header = (ViewGroup) inflater.inflate(R.layout.parallax_header, mListView, false);
		tvHeadline = (TextView) header.findViewById(R.id.tv_headline);
		tvOveralScore = (TextView) header.findViewById(R.id.overal_score);
		mListView.addHeaderView(header, null, false);
		// Set the adapter
		((AdapterView<ListAdapter>) mListView).setAdapter(adapter);
		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);
		// we take the background image and button reference from the header
		backgroundImage = (ImageView) header.findViewById(R.id.listHeaderImage);
		mListView.setOnScrollListener(this);
		// Get the NetworkImageView that will display the image.
		mNetworkImageView = (NetworkImageView) header.findViewById(R.id.listHeaderImage);
		notifyDataChanged();
		((ActionBarActivity) getActivity()).getSupportActionBar().show();
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), WebviewActivity.class);
		//TODO Set link and title to MyApp Instance.
		startActivity(intent);
	}

	/**
	 * The default content for this Fragment has a TextView that is shown when
	 * the list is empty. If you would like to change the text, call this method
	 * to supply the text it should use.
	 */
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyView instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView absListView, int i) {

	}

	@Override
	public void onScroll(AbsListView absListView, int i, int i2, int i3) {
		if (absListView.getId() == mListView.getId()) {
			final int currentFirstVisibleItem = mListView.getFirstVisiblePosition();

			if (currentFirstVisibleItem > mLastFirstVisibleItem) {
				// getSherlockActivity().getSupportActionBar().hide();
				((ActionBarActivity) getActivity()).getSupportActionBar().hide();
			} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
				// getSherlockActivity().getSupportActionBar().show();
				((ActionBarActivity) getActivity()).getSupportActionBar().show();
			}
			mLastFirstVisibleItem = currentFirstVisibleItem;
		}
		Rect rect = new Rect();
		backgroundImage.getLocalVisibleRect(rect);
		if (lastTopValue != rect.top) {
			lastTopValue = rect.top;
			backgroundImage.setY((float) (rect.top / 2.0));
		}
	}

	public void notifyDataChanged() {
		JSONObject jsonObject = MyApp.getInstance().getDetailResponseObj();
		if (jsonObject == null)
			return;
		try {
			String headlineText = jsonObject.getString("headline");
			String overalScore = jsonObject.getString("score");
			if (headlineText != null)
				tvHeadline.setText(headlineText);
			if (overalScore != null)
				tvOveralScore.setText(overalScore);
			JSONArray images = jsonObject.getJSONArray("images");
			if (images.length() > 0)
				IMAGE_URL = images.getString(0);
			JSONArray jsonPros = jsonObject.getJSONArray("pros");
			JSONArray jsonCons = jsonObject.getJSONArray("cons");
			list = new ArrayList<ProsCons>();
			for (int i = 0; i < jsonPros.length(); i++) {
				list.add(new ProsCons(jsonPros.getString(i), false));
			}
			for (int i = 0; i < jsonCons.length(); i++) {
				list.add(new ProsCons(jsonCons.getString(i), true));
			}
			adapter = new ProConAdapter(getActivity(), list);
			mListView.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (IMAGE_URL != null && !IMAGE_URL.isEmpty())
			mNetworkImageView.setImageUrl(IMAGE_URL, MyApp.getInstance().getImageLoader());
		else
			mNetworkImageView.setImageResource(R.drawable.cellphones);
	}

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);
	}

}
