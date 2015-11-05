package pollcorp.iriview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pollcorp.iriview.R;
import pollcorp.iriview.models.Spec;

/**
 * Created by hetpin on 10/22/15.
 */
public class SpecAdapter extends BaseAdapter {
	private Context context;
	private List<Spec> specs;

	public SpecAdapter(Context context, List<Spec> products) {
		this.context = context;
		this.specs = products;
	}

	@Override
	public int getCount() {
		return specs.size();
	}

	@Override
	public Spec getItem(int position) {
		return specs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	static class ViewHolder {
		public TextView key;
		public TextView val;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		Spec spec = specs.get(position);
		if (view == null){
			view = LayoutInflater.from(context).inflate(R.layout.spec_item, parent, false);
			// configure view holder
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.key = (TextView) view.findViewById(R.id.key);
			viewHolder.val = (TextView) view.findViewById(R.id.val);
			view.setTag(viewHolder);
		}
		// fill data
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.key.setText(spec.getKey());
		holder.val.setText(spec.getVal());

		return view;
	}
}