package gmedia.net.id.vasgmediasemarang.menu_job_daily_vas;

public class ModelListJobDailyVAS {
	private String id, customerID, tgl, nama, alamat, jam, isReport, keterangan;

	public ModelListJobDailyVAS(String id, String customerID, String tanggalFromAPI, String nama_site, String alamat_site, String waktu, String isReport, String note) {
		this.id = id;
		this.customerID = customerID;
		this.tgl = tanggalFromAPI;
		this.nama = nama_site;
		this.alamat = alamat_site;
		this.jam = waktu;
		this.isReport = isReport;
		this.keterangan = note;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTgl() {
		return tgl;
	}

	public void setTgl(String tgl) {
		this.tgl = tgl;
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

	public String getJam() {
		return jam;
	}

	public void setJam(String jam) {
		this.jam = jam;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getIsReport() {
		return isReport;
	}

	public void setIsReport(String isReport) {
		this.isReport = isReport;
	}
}
