package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;
import pollcorp.iriview.adapters.ProConAdapter;
import pollcorp.iriview.adapters.SpecAdapter;
import pollcorp.iriview.models.ProsCons;
import pollcorp.iriview.models.Spec;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * interface.
 */
public class SpecFragment extends Fragment implements ListView.OnItemClickListener {

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	/**
	 * The fragment's ListView/GridView.
	 */
	private ListView mListView;
	private SpecAdapter adapter;
	private List<Spec> specs;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private ListAdapter mAdapter;

	public static SpecFragment newInstance(String param1, String param2) {
		SpecFragment fragment = new SpecFragment();
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
	public SpecFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_spec, container, false);

		// Set the adapter
		mListView = (ListView) view.findViewById(android.R.id.list);
		mListView.setAdapter(adapter);
		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);
		//Show/hide actionbar when scroll up/down.
		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			int mLastFirstVisibleItem = 0;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if (view.getId() == mListView.getId()) {
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
			}
		});
		notifyDataChanged();
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

	public void notifyDataChanged() {
		JSONObject jsonObject = MyApp.getInstance().getDetailResponseObj();
		if (jsonObject == null)
			return;
		try {
			specs = new ArrayList<Spec>();
			JSONArray jsonArray = jsonObject.getJSONArray("specs");
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				String group_name = obj.getString("group_name");
				JSONArray group_specs = obj.getJSONArray("group_specs");
				for (int j = 0; j < group_specs.length(); j++) {
					specs.add(new Spec(group_name + " " + group_specs.getJSONObject(j).getString("title"), group_specs.getJSONObject(j).getString("info")));
				}
			}
			adapter = new SpecAdapter(getActivity(), specs);
			mListView.setAdapter(adapter);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
