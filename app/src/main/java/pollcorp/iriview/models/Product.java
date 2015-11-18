package pollcorp.iriview.models;

import org.json.JSONObject;

import pollcorp.iriview.dbmodels.DBProduct;

/**
 * Created by hetpin on 10/21/15.
 */
public class Product extends BasePro{
	public  String type;
	private String brand;
	private String released;
	private String headline;
	private int score;

	public Product(String objectId, String name, String thumbnail) {
		super(objectId, name, thumbnail);
	}

	public Product(JSONObject obj) {
		super(obj);
	}

	public Product(DBProduct product) {
		super(product);
	}
}
