package pollcorp.iriview.Util;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by hetpin on 10/29/15.
 */
public class RConstant {
	public static String parse_app_id_key = "X-Parse-Application-Id";
	public static String parse_app_id_val = "HrETYQIANkE6ftjAwfVyHprav6HPcwjYwKxz6IbF";
	public static String parse_api_id_key = "X-Parse-REST-API-Key";
	public static String parse_api_id_val = "IwTlRZLtRzOBAIMLbGdxPrYa1uIX6a5oqBGPl8ya";

	public static String url_latest_devices = "https://api.parse.com/1/functions/lastest_devices";

	public static String url_device_detail = "https://api.parse.com/1/functions/device_by_id";
	public static String url_search = "https://api.parse.com/1/functions/search_by_name";
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}

}
