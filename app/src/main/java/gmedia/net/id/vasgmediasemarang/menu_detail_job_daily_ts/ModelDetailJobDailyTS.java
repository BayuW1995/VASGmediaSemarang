package gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts;

public class ModelDetailJobDailyTS {
	private String nama, noHp;

	public ModelDetailJobDailyTS(String nama, String telepon) {
		this.nama = nama;
		this.noHp = telepon;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getNoHp() {
		return noHp;
	}

	public void setNoHp(String noHp) {
		this.noHp = noHp;
	}
}
