package pollcorp.iriview.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;
import pollcorp.iriview.Util.DB;
import pollcorp.iriview.Util.RConstant;
import pollcorp.iriview.adapters.FavoriteAdapter;
import pollcorp.iriview.dbmodels.DBProduct;
import pollcorp.iriview.models.Product;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 */
public class BookmarkFragment extends Fragment implements AbsListView.OnItemClickListener {

	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	private String mParam1;
	private String mParam2;

	private SwipeMenuListView mListView;
	private FavoriteAdapter mAdapter;
	private List<DBProduct> data = new ArrayList<DBProduct>();
	private SwipeMenuCreator creator;

	// TODO: Rename and change types of parameters
	public static BookmarkFragment newInstance(String param1, String param2) {
		BookmarkFragment fragment = new BookmarkFragment();
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
	public BookmarkFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		data = DB.getAllProduct();
		mAdapter = new FavoriteAdapter(getActivity(), data);
		createSwipeCreator();
	}

	private void createSwipeCreator() {
		creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getActivity().getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(RConstant.dp2px(getActivity().getApplicationContext(), 90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete_white_48dp);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

		// Set the adapter
		mListView = (SwipeMenuListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
		mListView.setMenuCreator(creator);
		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);
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
		mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
				switch (index) {
					case 0:
						// delete
						DB.delOne(data.get(position));
						//TODO update view
						data = DB.getAllProduct();
						mAdapter = new FavoriteAdapter(getActivity(), data);
						mListView.setAdapter(mAdapter);
						break;
				}
				// false : close the menu; true : not close the menu
				return false;
			}
		});
		mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), DetailScreen.class);
		MyApp.getInstance().setCurrentProduct(new Product(data.get(position)));
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


}
