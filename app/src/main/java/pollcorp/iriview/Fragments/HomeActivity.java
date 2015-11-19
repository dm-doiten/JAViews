package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SearchView;
import android.widget.TextView;

import com.jaredrummler.android.device.DeviceName;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;
import pollcorp.iriview.models.Product;


public class HomeActivity extends ActionBarActivity
		implements MyDrawerFragment.NavigationDrawerCallbacks,
		ProductListFragment.OnProductListFragmentInteractionListener,
		SearchView.OnQueryTextListener, SearchView.OnCloseListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private MyDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;
	private ProductListFragment fragment1;
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		//DB.delAll();
		setupActionbar();
		setupDrawableMenu();
		setupProfileBar();
	}

	private void setupDrawableMenu() {
		mNavigationDrawerFragment = (MyDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();
		// Set up the drawer.
		mNavigationDrawerFragment.setUp(
				R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
	}

	private void setupProfileBar() {
		String deviceName = "Unknown Device";
		DeviceName.with(getApplicationContext()).request(new DeviceName.Callback() {
			@Override
			public void onFinished(DeviceName.DeviceInfo info, Exception error) {
				String deviceName;
				if (error != null) {
					deviceName = info.getName();
				} else {
					deviceName = DeviceName.getDeviceName();
				}
				mNavigationDrawerFragment.setName(deviceName);
			}
		});
		//mNavigationDrawerFragment.getImg();//Set img
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();

		switch (position) {
			case 0: //Home screen
				MyApp.getInstance().isShowSearch = true;
				if (fragment1 == null)
					fragment1 = ProductListFragment.newInstance("1", "2");
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment1)
						.commit();
				break;
			case 1: //Bookmark Screen
				MyApp.getInstance().isShowSearch = false;
				WishlistFragment fragment2 = WishlistFragment.newInstance("1", "2");
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment2)
						.commit();
				break;
			case 2: //About Screen
				MyApp.getInstance().isShowSearch = false;
				AboutFragment fragment3 = AboutFragment.newInstance("1", "2");
				fragmentManager.beginTransaction()
						.replace(R.id.container, fragment3)
						.commit();
				break;
		}
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.title_section1);
				break;
			case 2:
				mTitle = getString(R.string.title_section2);
				break;
			case 3:
				mTitle = getString(R.string.title_section3);
				break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			restoreActionBar();

			if (MyApp.getInstance().isShowSearch) {
				getMenuInflater().inflate(R.menu.home, menu);

				// Associate searchable configuration with the SearchView
				SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
				searchView = (SearchView) menu.findItem(R.id.action_search)
						.getActionView();
				searchView.setSearchableInfo(searchManager
						.getSearchableInfo(getComponentName()));

				searchView.setSubmitButtonEnabled(true);
				searchView.setOnQueryTextListener(this);
				searchView.setOnCloseListener(this);
				searchView.setQueryHint("Device name");
				searchView.setSubmitButtonEnabled(false);
				final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
				searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
					@Override
					public void onFocusChange(View view, boolean queryTextFocused) {
						if (!queryTextFocused) {
							searchMenuItem.collapseActionView();
							searchView.setQuery("", false);
							searchView.setIconified(true);
						}
					}
				});
			}
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement

		return super.onOptionsItemSelected(item);
	}

	public void openDetailScreen() {
		Intent intent = new Intent(HomeActivity.this, DetailScreen.class);
		startActivity(intent);
	}

	@Override
	public void onProductListFragmentInteraction(Product product) {
		MyApp.getInstance().setCurrentProduct(product);
		openDetailScreen();
		return;
	}

	@Override
	public boolean onQueryTextSubmit(String s) {
		Log.e(getClass().getSimpleName(), "Submitted:" + s);
		if (fragment1 == null)
			fragment1 = ProductListFragment.newInstance("1", "2");
		fragment1.removeHeaderView();
		fragment1.search(s);
		return false;
	}

	@Override
	public boolean onQueryTextChange(String s) {
		if (fragment1 == null)
			fragment1 = ProductListFragment.newInstance("1", "2");
		fragment1.filter(s);
		Log.e(getClass().getSimpleName(), "Changed:" + s);
		MyApp.getInstance().setSearchText(s);
		return true;
	}

	@Override
	public boolean onClose() {
		if (fragment1 == null)
			fragment1 = ProductListFragment.newInstance("1", "2");
		fragment1.closeSearchView();
		Log.e("onClose", "onClose");
		return false;
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
			View rootView = inflater.inflate(R.layout.fragment_home, container, false);
			return rootView;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((HomeActivity) activity).onSectionAttached(
					getArguments().getInt(ARG_SECTION_NUMBER));
		}
	}

	public void setupActionbar() {
		int titleId = getResources().getIdentifier("action_bar_title", "id", "android");
		TextView abTitle = (TextView) findViewById(titleId);
		if (abTitle != null)
			abTitle.setTextColor(getResources().getColor(R.color.header_text));
	}

}
