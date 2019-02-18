package gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer;

public class ModelListPilihKostumer {
	private String service_id, idkostumer, nama, alamat;

	public ModelListPilihKostumer(String customer_id, String service_id, String nama_site, String alamat) {
		this.idkostumer = customer_id;
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

	public String getIdkostumer() {
		return idkostumer;
	}

	public void setIdkostumer(String idkostumer) {
		this.idkostumer = idkostumer;
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
