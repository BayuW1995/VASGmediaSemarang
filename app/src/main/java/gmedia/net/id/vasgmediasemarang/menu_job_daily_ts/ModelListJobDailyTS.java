package gmedia.net.id.vasgmediasemarang.menu_job_daily_ts;

public class ModelListJobDailyTS {
	private Boolean check;
	private String id, waktuMulai, waktuSelesai, lokasi, alamat, jenis_job;

	public ModelListJobDailyTS(String id, String waktuMulai, String waktuSelesai, String lokasi, String alamat, String jenis_project) {
		this.id = id;
		this.waktuMulai = waktuMulai;
		this.waktuSelesai = waktuSelesai;
		this.lokasi = lokasi;
		this.alamat = alamat;
		this.jenis_job = jenis_project;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public String getWaktuMulai() {
		return waktuMulai;
	}

	public void setWaktuMulai(String waktuMulai) {
		this.waktuMulai = waktuMulai;
	}

	public String getLokasi() {
		return lokasi;
	}

	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getJenis_job() {
		return jenis_job;
	}

	public void setJenis_job(String jenis_job) {
		this.jenis_job = jenis_job;
	}

	public String getWaktuSelesai() {
		return waktuSelesai;
	}

	public void setWaktuSelesai(String waktuSelesai) {
		this.waktuSelesai = waktuSelesai;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
