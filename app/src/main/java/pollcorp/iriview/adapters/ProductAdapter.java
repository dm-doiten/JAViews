package pollcorp.iriview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pollcorp.iriview.models.Product;
import pollcorp.iriview.R;

/**
 * Created by hetpin on 10/21/15.
 */
public class ProductAdapter extends BaseAdapter {
	private Context context;
	private List<Product> products;

	public ProductAdapter(Context context, List<Product> products) {
		this.context = context;
		this.products = products;
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Product getItem(int position) {
		return products.get(position);
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
		Product product = products.get(position);
		if (view == null){
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
}