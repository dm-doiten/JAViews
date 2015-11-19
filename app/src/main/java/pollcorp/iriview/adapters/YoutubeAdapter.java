package pollcorp.iriview.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pollcorp.iriview.R;
import pollcorp.iriview.models.Video;

/**
 * Created by hetpin on 10/21/15.
 */
public class YoutubeAdapter extends BaseAdapter {
	private Context context;
	private List<Video> list;

	public YoutubeAdapter(Context context, List<Video> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Video getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		public TextView name;
		public ImageView thumbnail;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Video product = list.get(position);
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.name = (TextView) view.findViewById(R.id.name);
			viewHolder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			view.setTag(viewHolder);
		}
		// fill data
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.name.setText(product.getTitle());
		Picasso.with(context).load(product.getThumbnail()).fit().centerCrop().error(R.drawable.error_thumbnail).into(holder.thumbnail);
		return view;
	}

}