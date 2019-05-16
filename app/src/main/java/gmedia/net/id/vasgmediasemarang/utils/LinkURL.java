package gmedia.net.id.vasgmediasemarang.utils;

public class LinkURL {
	public static String BaseURL = "https://erpsmg.gmedia.id/vas_v2/api/";
	//	public static String BaseURL = "http://192.168.20.34:8091/api/";
	public static String UrlLogin = BaseURL + "authentication/login";
	public static String UrlDaftarPelanggan = BaseURL + "customer/list?";
	public static String UrlDaftarJadwalVAS = BaseURL + "vas/schedule/list?date=";
	public static String UrlDaftarJadwalCR = BaseURL + "cr/schedule/list?date=";
	public static String UrlTambahJadwal = BaseURL + "vas/schedule/add?";
	public static String UrlTambahJadwalCR = BaseURL + "cr/schedule/add?";
	public static String UrlEditJadwal = BaseURL + "vas/schedule/edit?";
	public static String UrlEditJadwalCR = BaseURL + "cr/schedule/edit?";
	public static String UrlDeleteJadwal = BaseURL + "vas/schedule/delete?";
	public static String UrlDeleteJadwalCR = BaseURL + "cr/schedule/delete?";
	public static String UrlPublishJadwalVAS = BaseURL + "vas/schedule/publish?";
	public static String UrlPublishJadwalCR = BaseURL + "cr/schedule/publish?";
	public static String UrlDaftarJadwalTS = BaseURL + "support/schedule/list?date=";
	public static String UrlDetailJobDailyTS = BaseURL + "support/schedule/view/";
	public static String UrlReportJobDailyVAS = BaseURL + "vas/report/draft/";
	public static String UrlReportEquipment = BaseURL + "vas/report/send";
	public static String UrlReportSementaraEquipment = BaseURL + "vas/report/save_draft";
	public static String UrlUploadGambarSelfieReportVAS = BaseURL + "vas/report/upload_selfi_pic";
	public static String UrlUploadGambarTTDReportVAS = BaseURL + "vas/report/upload_ttd_pic";
	public static String UrlSendReportSurvey = BaseURL + "support/schedule/update_progress";
	public static String UrlDataDropdownPIC = BaseURL + "customer/pic_customer/";
	public static String UrlTambahDataPIC = BaseURL + "customer/add_pic_report";
	public static String UrlDataDropdownTaskInfoCR = BaseURL + "master/task_info?start=0&limit=10&search=";
	public static String UrlDataDropdownContractUnitCR = BaseURL + "master/contract_unit";
	public static String UrlDataLayananReportCR = BaseURL + "master/service?";
	public static String UrlDataDraftReportCR = BaseURL + "cr/report/draft/";
	public static String UrlSaveDataReportCR = BaseURL + "cr/report/save";
	public static String UrlSendDataReportCR = BaseURL + "cr/report/send";
	public static String UrlUpVersion = BaseURL + "app/about";
}