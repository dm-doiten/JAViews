package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;
import pollcorp.iriview.Util.RConstant;
import pollcorp.iriview.adapters.ProductAdapter;
import pollcorp.iriview.models.Product;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ProductListFragment.OnProductListFragmentInteractionListener}
 * interface.
 */
public class ProductListFragment extends Fragment implements AbsListView.OnItemClickListener {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnProductListFragmentInteractionListener mListener;
	SwipeRefreshLayout mSwipeRefreshLayout;
	/**
	 * The fragment's ListView/GridView.
	 */
	private ListView mListView;
	private ProductAdapter adapter;
	private List<Product> products;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private ListAdapter mAdapter;

	// TODO: Rename and change types of parameters
	public static ProductListFragment newInstance(String param1, String param2) {
		ProductListFragment fragment = new ProductListFragment();
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
	public ProductListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		//Init fake data.
		products = new ArrayList<Product>();
		//String testLink2 = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmh5oGqMgI3c4h3ggYysE7JNX590clSNJtPS0wpYzDX7o6_6U0";
		//String testLink = "http://image2.tin247.com/pictures/picsmall/2013/12/22/150/uly1387646449.jpg";
		//Init adapter.
		adapter = new ProductAdapter(getActivity().getApplicationContext(), products);
	}

	private void getListProduct() {
		String url = RConstant.url_latest_devices;
		final String TAG = getClass().getSimpleName();
		//Connect to url.
		mSwipeRefreshLayout.setRefreshing(true);
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
				url, null,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						Log.e(TAG, response.toString());
						mSwipeRefreshLayout.setRefreshing(false);
						try {
							products.clear();
							JSONArray arr = response.getJSONArray("result");
							for (int i = 0; i < arr.length(); i++) {
								JSONObject obj = arr.getJSONObject(i);
								Product product = new Product(obj);
								products.add(product);
							}
							Log.e(TAG, products.toString());
							adapter.notifyDataSetChanged();
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				VolleyLog.d(TAG, "Error: " + error.getMessage());
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}
		) {
			//Passing some request headers
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> headers = new HashMap<String, String>();
				//headers.put("Content-Type", "application/json");
				headers.put(RConstant.parse_app_id_key, RConstant.parse_app_id_val);
				headers.put(RConstant.parse_api_id_key, RConstant.parse_api_id_val);
				Log.d(TAG, headers.toString());
				return headers;
			}
		};
		//Adding request to request queue
		MyApp.getInstance().addToRequestQueue(jsonObjReq);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_product, container, false);
		mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeview_product);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				getListProduct();
//				String link = "";
//				GetProductTask task = (GetProductTask) new GetProductTask().execute(link);
			}
		});
		getListProduct();
		// Set the adapter
		mListView = (ListView) view.findViewById(android.R.id.list);
		mListView.setDivider(null);
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
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnProductListFragmentInteractionListener) activity;
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
		if (null != mListener) {
			// Notify the active callbacks interface (the activity, if the
			// fragment is attached to one) that an item has been selected.
			mListener.onProductListFragmentInteraction(products.get(position).getObjectId(), products.get(position).getName());
		}
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

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnProductListFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onProductListFragmentInteraction(String id, String name);
	}

	private class GetProductTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... link) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			//TODO update adapter.
			mSwipeRefreshLayout.setRefreshing(false);
		}
	}
}
