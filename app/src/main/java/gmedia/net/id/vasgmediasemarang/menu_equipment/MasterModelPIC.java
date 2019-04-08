package gmedia.net.id.vasgmediasemarang.menu_equipment;

public class MasterModelPIC {
	private String id = "", nama = "", email = "";

	public MasterModelPIC(String id, String nama, String email) {
		this.id = id;
		this.nama = nama;
		this.email = email;
	}

	@Override
	public String toString() {
		return nama;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
