package pollcorp.iriview.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;
import pollcorp.iriview.Util.DB;
import pollcorp.iriview.Util.RConstant;
import pollcorp.iriview.adapters.FavoriteAdapter;
import pollcorp.iriview.dbmodels.DBProduct;
import pollcorp.iriview.models.Product;

public class DetailScreen extends ActionBarActivity implements ActionBar.TabListener, ReviewsFragment.OnFragmentInteractionListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private JSONObject responseObj;
	private SpecFragment specFragment;
	private ReviewsFragment reviewFrag;
	private YoutubeFragment ytFrag;
	private BenchmarkFragment benchFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_screen);
		setTitle(MyApp.getInstance().getCurName());
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(
					actionBar.newTab()
							.setText(mSectionsPagerAdapter.getPageTitle(i))
							.setTabListener(this));
		}
		getProductDetail();
	}

	private void getProductDetail() {
		String url = RConstant.url_device_detail;
		final String TAG = getClass().getSimpleName();
		StringRequest jsonObjReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Log.d(TAG, response);
				try {
					JSONObject jsonObj = new JSONObject(response);
					JSONObject json = jsonObj.getJSONObject("result");
					MyApp.getInstance().setDetailResponseObj(json);
					//TODO F5 pager adapter.
					if (specFragment != null)
						specFragment.notifyDataChanged();
					if (reviewFrag != null)
						reviewFrag.notifyDataChanged();
				} catch (JSONException e) {
					Log.d(TAG, "Can not parse responded json.");
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, error.toString());
			}
		}) {
			@Override
			protected Map<String, String> getParams() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("Content-Type", "application/x-www-form-urlencoded");
				params.put("objectId", MyApp.getInstance().getCurrentObjectId());
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
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_detail_screen, menu);
		if (menu.size() == 0)
			return true;
		MenuItem favor = menu.getItem(0);
		Product product = MyApp.getInstance().getCurrentProduct();
		if (product.isFavor()) {
			favor.setIcon(R.drawable.ic_favorite_white_48dp);
//			favor.setEnabled(false);
		} else {
			favor.setIcon(R.drawable.ic_favorite_black_24dp);
//			favor.setEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (item.getItemId()) {
			case android.R.id.home:
				if (id == android.R.id.home) {
					// app icon in action bar clicked; goto parent activity.
					this.finish();
					return true;
				}
			case R.id.action_favorite:
				Product product = MyApp.getInstance().getCurrentProduct();
				if (product.isFavor()) {
					//Un favor on view, then del from db.
					product.setFavor(false);
					item.setIcon(R.drawable.ic_favorite_black_24dp);
					DB.delOne(new DBProduct(product));
					product.setFavor(false);
				} else {
					//Favor on view, then add the db.
					product.setFavor(true);
					item.setIcon(R.drawable.ic_favorite_white_48dp);
					DB.addProduct(new DBProduct(product));
				}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onFragmentInteraction(String id) {
		Log.e(getClass().getSimpleName(), "OnFragmentInteractionListener " + id);
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0://Review fragment
					if (reviewFrag == null)
						reviewFrag = ReviewsFragment.newInstance("1", "2");
					return reviewFrag;
				case 1://Benchmark
					if (benchFragment == null)
						benchFragment = BenchmarkFragment.newInstance("1", "2");
					return benchFragment;
				case 2://Spec
					if (specFragment == null)
						specFragment = SpecFragment.newInstance("1", "2");
					return specFragment;
				case 3://Youtube
					if (ytFrag == null)
						ytFrag = YoutubeFragment.newInstance("1", "2");
					return ytFrag;
				default:
					return PlaceholderFragment.newInstance(position + 1);

			}


		}

		@Override
		public int getCount() {
			// Show 4 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return "Reviews".toUpperCase(l);
				case 1:
					return "Benchmarks".toUpperCase(l);
				case 2:
					return "Specs".toUpperCase(l);
				case 3:
					return "Videos".toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section
		 * number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_detail_screen, container, false);
			return rootView;
		}
	}

}
