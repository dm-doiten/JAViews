package pollcorp.iriview.Fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import pollcorp.iriview.MyApp;
import pollcorp.iriview.R;

public class WebviewActivity extends ActionBarActivity {

	private WebView webView;
	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		String link = MyApp.getInstance().getWebviewLink();
		if (link.isEmpty()) {
			finish();
		}
		webView = (WebView) findViewById(R.id.webView);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeview_webview);
		swipeRefreshLayout.setEnabled(false);
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				swipeRefreshLayout.setRefreshing(false);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				super.shouldOverrideUrlLoading(view, url);
				view.loadUrl(url);
				return true;
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(link);
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setTitle(MyApp.getInstance().getCurName());
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (item.getItemId() == android.R.id.home) {
					this.finish();
					return true;
				}
		}
		return super.onOptionsItemSelected(item);
	}
}
