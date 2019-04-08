package gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child;

public class ModelListEquipmentChild {
	private String id = "", jenis_child = "", standard = "", tipe = "", radio1 = "", isian = "", noteTeks1 = "";

	public ModelListEquipmentChild(String id, String jenis_child, String standard, String tipe, String radio1, String noteTeks1, String isian) {
		this.id = id;
		this.jenis_child = jenis_child;
		this.standard = standard;
		this.tipe = tipe;
		this.radio1 = radio1;
		this.isian = isian;
		this.noteTeks1 = noteTeks1;
//		this.noteTeks2 = noteTeks2;
	}

	public String getJenis_child() {
		return jenis_child;
	}

	public void setJenis_child(String jenis_child) {
		this.jenis_child = jenis_child;
	}

	public String getIsian() {
		return isian;
	}

	public void setIsian(String isian) {
		this.isian = isian;
	}

	public String getRadio1() {
		return radio1;
	}

	public void setRadio1(String radio1) {
		this.radio1 = radio1;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getTipe() {
		return tipe;
	}

	public void setTipe(String tipe) {
		this.tipe = tipe;
	}

	public String getNoteTeks1() {
		return noteTeks1;
	}

	public void setNoteTeks1(String noteTeks1) {
		this.noteTeks1 = noteTeks1;
	}
}
