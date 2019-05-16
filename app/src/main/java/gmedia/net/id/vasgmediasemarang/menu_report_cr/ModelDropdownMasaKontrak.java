package gmedia.net.id.vasgmediasemarang.menu_report_cr;

public class ModelDropdownMasaKontrak {
	private String value, text;

	public ModelDropdownMasaKontrak(String value, String text) {
		this.value = value;
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


}
