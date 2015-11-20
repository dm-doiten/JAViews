package pollcorp.iriview.models;

/**
 * Created by hetpin on 11/5/15.
 */
public class ProsCons {
	private String name;
	private boolean isCons;

	public ProsCons(String name, boolean isCons) {
		this.name = name;
		this.isCons = isCons;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isCons() {
		return isCons;
	}

	public void setCons(boolean isCons) {
		this.isCons = isCons;
	}

	@Override
	public String toString() {
		return "ProsCons{" +
				"name='" + name + '\'' +
				", isCons=" + isCons +
				'}';
	}
}
