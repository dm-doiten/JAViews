package pollcorp.iriview.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pollcorp.iriview.models.Product;
import pollcorp.iriview.R;

/**
 * Created by hetpin on 10/21/15.
 */
public class ProductAdapter extends BaseAdapter implements Filterable {
	private Context context;
	private FriendFilter friendFilter;
	private List<Product> filterList;
	private List<Product> list;//original data.
	private ListView listView;
	private View header;

	public ProductAdapter(Context context, List<Product> products, ListView listview, View view) {
		this.context = context;
		this.list = new ArrayList<Product>();
		this.list.addAll(products);
		this.filterList = new ArrayList<Product>();
		this.filterList.addAll(list);
		Log.e("Constructor", "list.size =  " + list.size());
		Log.e("Constructor", "filter.size =  " + filterList.size());
		this.listView = listview;
		this.header = view;
		getFilter();
	}

	public void pull(List<Product> newData) {
		this.list = new ArrayList<Product>();
		this.list.addAll(newData);
		this.filterList = new ArrayList<Product>();//Just copy for backup original data
		this.filterList.addAll(list);
		Log.e("notifyDataSetChanged", "list.size =  " + list.size());
		Log.e("notifyDataSetChanged", "filter.size =  " + filterList.size());
		super.notifyDataSetChanged();//products change->filterList change -> update view.
	}

	@Override
	public int getCount() {
		return filterList.size();
	}

	@Override
	public Product getItem(int position) {
		return filterList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		public TextView name;
		public ImageView thumbnail;
		public TextView score;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.e("getView", "v " + position);
		Log.e("getView", "size " + filterList.size());

		View view = convertView;
		Product product = filterList.get(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			viewHolder.score = (TextView) view.findViewById(R.id.score);
			view.setTag(viewHolder);
		}
		// fill data
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(product.getName());
		holder.score.setText(product.getScore());
		Picasso.with(context).load(product.getThumbnail()).fit().centerCrop().error(R.drawable.error_thumbnail).into(holder.thumbnail);
		return view;
	}

	/**
	 * Get custom filter
	 *
	 * @return filter
	 */
	@Override
	public Filter getFilter() {
		if (friendFilter == null) {
			friendFilter = new FriendFilter();
		}

		return friendFilter;
	}

	/**
	 * Custom filter for friend list
	 * Filter content in friend list according to the search text
	 */
	private class FriendFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults filterResults = new FilterResults();
			if (constraint != null && constraint.length() > 0) {
				ArrayList<Product> tempList = new ArrayList<Product>();

				// search content in friend list
				for (Product user : list) {
					if (user.toString().toLowerCase().contains(constraint.toString().toLowerCase())) {
						tempList.add(user);
					}
				}

				filterResults.count = tempList.size();
				filterResults.values = tempList;
			} else {
				filterResults.count = list.size();
				filterResults.values = list;
			}

			return filterResults;
		}

		/**
		 * Notify about filtered list to ui
		 *
		 * @param constraint text
		 * @param results    filtered result
		 */
		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			//filterList = new ArrayList<Product>();
			filterList = (ArrayList<Product>) results.values;
			Log.e(getClass().getSimpleName(), "Found " + filterList.size());
			notifyDataSetChanged();
			Log.e("after fil", "list.size =  " + list.size());
			Log.e("after fil", "filter.size =  " + filterList.size());
			if (filterList.size() == 0) {
				//TODO add header button to search online.
				addHeader();
			} else {
				//TODO remove header button if exist.
				removeHeader();
			}
		}
	}

	public List<Product> getFilterList() {
		return filterList;
	}

	public void addHeader() {
		if (header != null && listView != null && listView.getHeaderViewsCount() == 0)
			listView.addHeaderView(header);
	}

	public void removeHeader() {
		if (header != null && listView != null && listView.getHeaderViewsCount() == 1)
			listView.removeHeaderView(header);

	}
}