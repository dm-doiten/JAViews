package pollcorp.iriview.models;

/**
 * Created by hetpin on 10/22/15.
 */
public class Spec {
	private String key;
	private String val;
	public Spec(String key, String val){
		this.key = key;
		this.val = val;
	}

	public String getKey() {
		return key;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String toString(){
		return this.key + " " + this.val;
	}
}
