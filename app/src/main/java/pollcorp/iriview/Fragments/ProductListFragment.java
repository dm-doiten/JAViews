package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

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
import pollcorp.iriview.Util.DB;
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
	private List<Product> search_products = new ArrayList<Product>();
	private int loadedPage = 0;
	private int preLast;
	private View header;
	private Button searchBtn;

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
	}

	private void loadMorePage() {
		getProductList(loadedPage + 1);
	}

	private void getProductList(final int page) {
		String url = RConstant.url_latest_devices;
		final String TAG = getClass().getSimpleName();
		//Connect to url.
		mSwipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(true);
			}
		});
		StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e(TAG, response.toString());
				mSwipeRefreshLayout.setRefreshing(false);
				try {
					JSONObject jsonObj = new JSONObject(response);
					if (page == 0)
						products.clear();
					JSONArray arr = jsonObj.getJSONArray("result");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						Product product = new Product(obj);
						product.setFavor(DB.checkIsFavor(product));
						products.add(product);
					}
					Log.e(TAG, products.toString());
					adapter.pull(products);
					loadedPage = loadedPage + 1;
					MyApp.getInstance().setProducts(products);
				} catch (JSONException e) {
					Log.d(TAG, "Can not parse responded json.");
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, error.toString());
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				params.put("page", page + "");
				Log.d(TAG, params.toString());
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/x-www-form-urlencoded");
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
		//mSwipeRefreshLayout.setEnabled(false);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (MyApp.getInstance().getSearchText().isEmpty())
					getProductList(0);//Load the first page
				else
					search(MyApp.getInstance().getSearchText());
			}
		});
		// Set the adapter
		mListView = (ListView) view.findViewById(android.R.id.list);
		//mListView.setDivider(null);
		products = MyApp.getInstance().getProducts();
		if (products.isEmpty() || products == null)
			getProductList(0);//Load the first page
		//Init adapter.
		header = inflater.inflate(R.layout.search_button, null);
		searchBtn = (Button) header.findViewById(R.id.search_btn);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//TODO search online.
				Log.e(getClass().getSimpleName(), "search online");
				if (mListView != null && header != null && mListView.getHeaderViewsCount() > 0)
					mListView.removeHeaderView(header);
				RConstant.hideSoftKeyboard(getActivity());
				search(MyApp.getInstance().getSearchText());
			}
		});
		adapter = new ProductAdapter(getActivity().getApplicationContext(), products, mListView, header);
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
					// Sample calculation to determine if the last item is fully visible.
					final int lastItem = firstVisibleItem + visibleItemCount;
					if (lastItem == products.size()) {
						if (preLast != lastItem) { //to avoid multiple calls for last item
							Log.d("Last", "Last");
							preLast = lastItem;
							loadMorePage();
						}
					}
				}
			}
		});
		return view;
	}

	public void removeHeaderView() {
		if (mListView != null && header != null && mListView.getHeaderViewsCount() > 0)
			mListView.removeHeaderView(header);
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
			Activity act = (HomeActivity) getActivity();
			Log.e("position", " " + position);
			mListener.onProductListFragmentInteraction(adapter.getFilterList().get(position));
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

	public void filter(String text) {
		adapter.getFilter().filter(text);
	}

	public void search(final String text) {
		String url = RConstant.url_search;
		final String TAG = getClass().getSimpleName();
		//Connect to url.
		mSwipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				mSwipeRefreshLayout.setRefreshing(true);
			}
		});
		StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.e(TAG, response.toString());
				mSwipeRefreshLayout.setRefreshing(false);
				try {
					JSONObject jsonObj = new JSONObject(response);
					search_products.clear();
					JSONArray arr = jsonObj.getJSONArray("result");
					for (int i = 0; i < arr.length(); i++) {
						JSONObject obj = arr.getJSONObject(i);
						Product product = new Product(obj);
						search_products.add(product);
					}
					Log.e(TAG, search_products.toString());
					adapter.pull(search_products);
					MyApp.getInstance().setProducts(search_products);
				} catch (JSONException e) {
					Log.d(TAG, "Can not parse responded json.");
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, error.toString());
				mSwipeRefreshLayout.setRefreshing(false);
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				params.put("request_type", "0");
				params.put("device", text);
				Log.d(TAG, params.toString());
				return params;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> headers = new HashMap<String, String>();
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				headers.put(RConstant.parse_app_id_key, RConstant.parse_app_id_val);
				headers.put(RConstant.parse_api_id_key, RConstant.parse_api_id_val);
				Log.d(TAG, headers.toString());
				return headers;
			}
		};
		//Adding request to request queue
		MyApp.getInstance().addToRequestQueue(jsonObjReq);
	}

	public void closeSearchView() {
		MyApp.getInstance().setProducts(products);
		adapter.pull(products);
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
		public void onProductListFragmentInteraction(Product product);
//		public void closeSearchView();
	}
}
