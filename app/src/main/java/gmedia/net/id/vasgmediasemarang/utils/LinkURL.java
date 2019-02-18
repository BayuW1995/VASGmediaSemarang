package gmedia.net.id.vasgmediasemarang.utils;

public class LinkURL {
	public static String BaseURLLocalHost = "http://192.168.20.34:8091/api/";
	public static String UrlLogin = BaseURLLocalHost + "authentication/login";
	public static String UrlDaftarPelanggan = BaseURLLocalHost + "customer/list?";
	public static String UrlDaftarJadwalVAS = BaseURLLocalHost + "vas/schedule/list?date=";
	public static String UrlTambahJadwal = BaseURLLocalHost + "vas/schedule/add?";
	public static String UrlEditJadwal = BaseURLLocalHost + "vas/schedule/edit?";
	public static String UrlDeleteJadwal = BaseURLLocalHost + "vas/schedule/delete?";
	public static String UrlPublishJadwal = BaseURLLocalHost + "vas/schedule/publish?";
	public static String UrlDaftarJadwalTS = BaseURLLocalHost + "support/schedule/list?date=";
	public static String UrlDetailJobDailyTS = BaseURLLocalHost + "support/schedule/view/";
}
