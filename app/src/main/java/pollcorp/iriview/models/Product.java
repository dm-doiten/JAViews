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
	private boolean isFavor;

	public Product(String objectId, String name, String thumbnail) {
		super(objectId, name, thumbnail);
		isFavor = false;
	}

	public Product(JSONObject obj) {
		super(obj);
	}
	//Convert from DBProduct object, so isFavor is True obviously.
	public Product(DBProduct product) {
		super(product);
		isFavor = true;
	}

	public boolean isFavor() {
		return isFavor;
	}

	public void setFavor(boolean isFavor) {
		this.isFavor = isFavor;
	}
}
