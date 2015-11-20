package pollcorp.iriview.dbmodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import pollcorp.iriview.models.Product;

/**
 * Created by hetpin on 11/18/15.
 */
@Table(name = "Product")
public class DBProduct extends Model {
	// If name is omitted, then the field name is used.
	@Column(name = "ObjectId")
	public String objectId;
	@Column(name = "Name")
	public String name;
	@Column(name = "Score")
	public String score;
	@Column(name = "thumbnailLink")
	public String thumbnailLink;

	public DBProduct() {
		super();
	}

	public DBProduct(String objectId, String name, String score, String thumbnailLink) {
		super();
		this.objectId = objectId;
		this.name = name;
		this.score = score;
		this.thumbnailLink = thumbnailLink;
	}
	public DBProduct(Product product) {
		super();
		this.objectId = product.getObjectId();
		this.name = product.getName();
		this.thumbnailLink = product.getThumbnail();
		this.score = product.getScore();
	}
}
