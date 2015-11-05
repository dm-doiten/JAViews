package pollcorp.iriview.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hetpin on 10/29/15.
 */
public class BasePro {
	private String objectId;
	private String name;
	private String thumbnail;
	private String score;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public BasePro(String objectId, String name, String thumbnail) {
		this.objectId = objectId;
		this.name = name;
		this.thumbnail = thumbnail;
	}

	public BasePro(JSONObject obj) {
		//String testLink = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmh5oGqMgI3c4h3ggYysE7JNX590clSNJtPS0wpYzDX7o6_6U0";
		try {
			this.objectId = obj.getString("objectId");
		} catch (JSONException e) {
			this.objectId = "";
			e.printStackTrace();
		}
		try {
			this.name = obj.getString("name");
		} catch (JSONException e) {
			this.name = "";
			e.printStackTrace();
		}
		try {
			this.thumbnail = obj.getString("image");
		} catch (JSONException e) {
			this.thumbnail = "http://www.";
			e.printStackTrace();
		}
		try {
			this.score = obj.getString("score");
		} catch (JSONException e) {
			this.score = "N/A";
			e.printStackTrace();
		}
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String toString(){
		return this.objectId + " " + this.name + " " + this.score;
	}
}
