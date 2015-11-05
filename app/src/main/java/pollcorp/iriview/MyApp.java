package pollcorp.iriview;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

import pollcorp.iriview.models.Product;

/**
 * Created by hetpin on 10/21/15.
 * This singleton will save all data in app lifecycle.
 */
public class MyApp extends Application {
	private static MyApp mInstance;
	//Volley variables
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private List<Product> products;
	private String curObjectId;
	private String curName;

	private JSONObject detailResponse;


	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		//Init Volley variables
		mRequestQueue = getRequestQueue();
		mImageLoader = new ImageLoader(mRequestQueue,
				new ImageLoader.ImageCache() {
					private final LruCache<String, Bitmap>
							cache = new LruCache<String, Bitmap>(20);
					@Override
					public Bitmap getBitmap(String url) {
						return cache.get(url);
					}

					@Override
					public void putBitmap(String url, Bitmap bitmap) {
						cache.put(url, bitmap);
					}
				});
	}
	public static synchronized MyApp getInstance() {
		return mInstance;
	}
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public Product getProductPosition(int position) {
		if (position >= 0 && products != null) {
			return products.get(position);
		}
		return null;
	}
	public String getCurrentObjectId(){

		//return curObjectId;
		return "TbLN0m3W24";
	}
	public void setCurrentObjectId(String objectId){
		curObjectId = objectId;
	}

	public JSONObject getDetailResponseObj() {
		return detailResponse;
	}
	public void setDetailResponseObj(JSONObject obj){
		detailResponse = obj;
	}
	public String getCurName() {
		return curName;
	}

	public void setCurName(String curName) {
		this.curName = curName;
	}
}
