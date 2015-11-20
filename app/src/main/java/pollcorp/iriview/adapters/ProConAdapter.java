package pollcorp.iriview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pollcorp.iriview.R;
import pollcorp.iriview.models.ProsCons;
import pollcorp.iriview.models.Spec;

/**
 * Created by hetpin on 10/22/15.
 */
public class ProConAdapter extends BaseAdapter {
	private Context context;
	private List<ProsCons> list;

	public ProConAdapter(Context context, List<ProsCons> products) {
		this.context = context;
		this.list = products;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ProsCons getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	static class ViewHolder {
		public ImageView thumbnail;
		public TextView val;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ProsCons spec = list.get(position);
		if (view == null){
			view = LayoutInflater.from(context).inflate(R.layout.pro_con_item, parent, false);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
			viewHolder.val = (TextView) view.findViewById(R.id.text);
			view.setTag(viewHolder);
		}
		// fill data
		ViewHolder holder = (ViewHolder) view.getTag();
		if (spec.isCons())
			holder.thumbnail.setImageResource(R.drawable.minus);
		else
			holder.thumbnail.setImageResource(R.drawable.plus);
		holder.val.setText(spec.getName());

		return view;
	}
}