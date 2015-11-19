package pollcorp.iriview.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hetpin on 11/13/15.
 */
public class Video {
	private String objectId;
	private String title;
	private String thumbnail;

	public Video(JSONObject json) {
		try {
			this.title = json.getString("title");
		} catch (JSONException e) {
			this.title = "";
			e.printStackTrace();
		}
		try {
			this.objectId = json.getString("yt_id");
		} catch (JSONException e) {
			this.objectId = "";
			e.printStackTrace();
		}
		try {
			this.thumbnail = json.getString("thumbnail");
		} catch (JSONException e) {
			this.thumbnail = "";
			e.printStackTrace();
		}
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}

