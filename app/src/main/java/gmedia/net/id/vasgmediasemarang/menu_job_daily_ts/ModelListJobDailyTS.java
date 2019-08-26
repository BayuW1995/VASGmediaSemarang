package gmedia.net.id.vasgmediasemarang.menu_job_daily_ts;

public class ModelListJobDailyTS {
	private Boolean check;
	private String id, waktuMulai, waktuSelesai, lokasi, alamat, jenis_job, status_survey, flag_custom, update_progress;

	public ModelListJobDailyTS(String id, String waktuMulai, String waktuSelesai, String lokasi, String alamat, String jenis_project, String status_survey, String flag_custom, String update_progress) {
		this.id = id;
		this.waktuMulai = waktuMulai;
		this.waktuSelesai = waktuSelesai;
		this.lokasi = lokasi;
		this.alamat = alamat;
		this.jenis_job = jenis_project;
		this.status_survey = status_survey;
		this.flag_custom = flag_custom;
		this.update_progress = update_progress;
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

	public String getStatus_survey() {
		return status_survey;
	}

	public void setStatus_survey(String status_survey) {
		this.status_survey = status_survey;
	}

	public String getFlag_custom() {
		return flag_custom;
	}

	public void setFlag_custom(String flag_custom) {
		this.flag_custom = flag_custom;
	}

	public String getUpdate_progress() {
		return update_progress;
	}

	public void setUpdate_progress(String update_progress) {
		this.update_progress = update_progress;
	}
}
