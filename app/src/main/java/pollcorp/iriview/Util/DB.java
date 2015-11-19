package pollcorp.iriview.Util;

import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

import pollcorp.iriview.dbmodels.DBProduct;

/**
 * Created by hetpin on 11/18/15.
 */
public class DB {
	public static List<DBProduct> getAllProduct() {
		return new Select()
				.from(DBProduct.class)
				.execute();
	}

	public static List<DBProduct> getAllProduct(DBProduct product) {
		return new Select()
				.from(DBProduct.class)
				.where("Name = ?", product.name)
				.execute();
	}

	public static void addProduct(DBProduct product) {
		if (product == null) {
			Log.e("DB", "product null");
			return;
		}
		if (isExist(product)) {
			Log.e("DB", "save " + product.name);
			product.save();
		} else {
			Log.e("DB", "Product existed");
		}
	}

	public static boolean isExist(DBProduct product) {
		List<DBProduct> list = getAllProduct(product);
		if (list.size() == 0)
			return true;
		else
			return false;
	}

	public static void delAll() {
		new Delete().from(DBProduct.class).execute();
		return;
	}
}
