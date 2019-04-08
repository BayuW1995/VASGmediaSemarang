package gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer;

public class ModelListPilihKostumer {
	private String customer_id, service_id, nama, alamat;

	public ModelListPilihKostumer(String customer_id, String service_id, String nama_site, String alamat) {
		this.customer_id = customer_id;
		this.service_id = service_id;
		this.nama = nama_site;
		this.alamat = alamat;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}


}
