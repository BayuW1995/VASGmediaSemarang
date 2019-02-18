package gmedia.net.id.vasgmediasemarang.menu_job_daily_vas;

public class ModelListJobDailyVAS {
	private String id, tgl, nama, alamat, jam, keterangan;

	public ModelListJobDailyVAS(String id, String tanggalFromAPI, String nama_site, String alamat_site, String waktu, String note) {
		this.id = id;
		this.tgl = tanggalFromAPI;
		this.nama = nama_site;
		this.alamat = alamat_site;
		this.jam = waktu;
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
}
