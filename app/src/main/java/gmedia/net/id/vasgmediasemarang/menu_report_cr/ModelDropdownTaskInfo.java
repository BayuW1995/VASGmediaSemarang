package gmedia.net.id.vasgmediasemarang.menu_report_cr;

public class ModelDropdownTaskInfo {
	private String id, text;

	public ModelDropdownTaskInfo(String value, String text) {
		this.id = value;
		this.text = text;
	}
	@Override
	public String toString() {
		return text;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
