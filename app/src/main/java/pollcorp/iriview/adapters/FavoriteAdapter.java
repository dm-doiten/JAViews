package pollcorp.iriview.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pollcorp.iriview.R;
import pollcorp.iriview.Util.DB;
import pollcorp.iriview.dbmodels.DBProduct;
import pollcorp.iriview.models.Product;

/**
 * Created by hetpin on 10/21/15.
 */
public class FavoriteAdapter extends BaseAdapter {
	private Context context;
	private List<DBProduct> list;//original data.
	private ListView listView;

	public FavoriteAdapter(Context context, List<DBProduct> products) {
		this.context = context;
		this.list = new ArrayList<DBProduct>();
		this.list.addAll(products);
	}

	public void pull(List<DBProduct> newData) {
		this.list = new ArrayList<DBProduct>();
		this.list.addAll(newData);
		super.notifyDataSetChanged();//products change->filterList change -> update view.
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public DBProduct getItem(int position) {
		return list.get(position);
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
		View view = convertView;
		DBProduct product = list.get(position);
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
		holder.name.setText(product.name);
		holder.score.setText(product.score);
		Picasso.with(context).load(product.thumbnailLink).fit().centerCrop().error(R.drawable.error_thumbnail).into(holder.thumbnail);
		return view;
	}
}