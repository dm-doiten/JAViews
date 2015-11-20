package pollcorp.iriview.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import pollcorp.iriview.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link BenchmarkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BenchmarkFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	protected HorizontalBarChart mChart1;
	protected HorizontalBarChart mChart2;
	protected HorizontalBarChart mChart3;


	public static BenchmarkFragment newInstance(String param1, String param2) {
		BenchmarkFragment fragment = new BenchmarkFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public BenchmarkFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_benchmark, container, false);
		mChart1 = (HorizontalBarChart) view.findViewById(R.id.chart1);
//		mChart2 = (HorizontalBarChart) view.findViewById(R.id.chart2);
//		mChart3 = (HorizontalBarChart) view.findViewById(R.id.chart3);

		setupChart(mChart1);
		((ActionBarActivity) getActivity()).getSupportActionBar().show();
		return view;
	}

	private void setupChart(HorizontalBarChart mChart) {
		mChart.setPinchZoom(false);

		XAxis xl = mChart.getXAxis();
		xl.setPosition(XAxis.XAxisPosition.BOTTOM);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(true);
		xl.setGridLineWidth(0.3f);

		YAxis yl = mChart.getAxisLeft();
		yl.setDrawAxisLine(true);
		yl.setDrawGridLines(true);
		yl.setGridLineWidth(0.3f);

		YAxis yr = mChart.getAxisRight();
		yr.setDrawAxisLine(true);
		yr.setDrawGridLines(false);

		setData(3, 5);
		mChart.animateY(2500);
	}

	private void setData(int count, float range) {

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<String> xVals = new ArrayList<String>();

		for (int i = 0; i < count; i++) {
			xVals.add("abc");
			yVals1.add(new BarEntry((float) (Math.random() * range), i));
		}

		BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(xVals, dataSets);
		data.setValueTextSize(10f);

		mChart1.setData(data);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}
}
