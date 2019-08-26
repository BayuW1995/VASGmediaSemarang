package gmedia.net.id.vasgmediasemarang.menu_job_daily_ts;

import android.app.DatePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gmedia.net.id.vasgmediasemarang.MainActivity;
import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.ConvertDate;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;

public class JobDailyTS extends AppCompatActivity {
	private ArrayList<ModelListJobDailyTS> list;
	private ListAdapterJobDailyTS adapter;
	private ListView listView;
	private Boolean check[] =
			{
					true,
					false,
					true,
					false,
					true,
					false
			};
	private String waktu[] =
			{
					"09.00",
					"10.00",
					"11.00",
					"12.00",
					"13.00",
					"14.00"
			};
	private String lokasi[] =
			{
					"E-Plaza",
					"Hotel Horison",
					"Hotel @home",
					"Hotel Ibis",
					"SMK 7 Pembangunan",
					"SMA N 11 Semarang"
			};
	private String alamat[] =
			{
					"Gajahmada Plaza Lt.2 Simpang Lima",
					"Jl. Jendral Sudirman No.294 Semarang",
					"Jl. Sisinga Mangaraja No.16 Semarang",
					"Gajahmada Plaza Lt.2 Simpang Lima",
					"Jl. Jendral Sudirman No.294 Semarang",
					"Jl. Sisinga Mangaraja No.16 Semarang"
			};
	private String jenis_job[] =
			{
					"Instalasi (LAN / Wifi)",
					"Survey (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Survey (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Survey (LAN / Wifi)"
			};
	private TextView txtTgl;
	private ImageView btnTgl;
	public static String tanggalJobDailyTS = "";
	private Proses proses;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_daily_ts);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Job Daily TS");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.layoutKuning)));
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = JobDailyTS.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.notifBarKuning));
		}
		session = new SessionManager(JobDailyTS.this);
		proses = new Proses(this);
		initUI();
		initAction();
	}

	private void initUI() {
		txtTgl = (TextView) findViewById(R.id.txtTanggalJobDailyTS);
		btnTgl = (ImageView) findViewById(R.id.btnTanggalJobDailyTS);
		listView = (ListView) findViewById(R.id.lv_job_daily_ts);
		initDefaultDate();
	}

	private void initAction() {
		btnTgl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final java.util.Calendar customDate = java.util.Calendar.getInstance();
				DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						customDate.set(java.util.Calendar.YEAR, year);
						customDate.set(java.util.Calendar.MONTH, month);
						customDate.set(java.util.Calendar.DATE, dayOfMonth);
						SimpleDateFormat sdFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
						txtTgl.setText(sdFormat.format(customDate.getTime()));
						txtTgl.setAlpha(1);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
						tanggalJobDailyTS = sdf.format(customDate.getTime());
						prepareDataJobDailyTS();
					}
				};
				new DatePickerDialog(JobDailyTS.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
			}
		});
	}


	private void initDefaultDate() {
		Date c = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
		String formattedDate = df.format(c);
		txtTgl.setText(formattedDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		tanggalJobDailyTS = sdf.format(c);
		prepareDataJobDailyTS();
	}

	private void prepareDataJobDailyTS() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(JobDailyTS.this, new JSONObject(), "GET", LinkURL.UrlDaftarJadwalTS + tanggalJobDailyTS, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				list = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONObject jadwal = response.getJSONObject("jadwal");
						tanggalJobDailyTS = jadwal.getString("tanggal");
						txtTgl.setText(ConvertDate.convert("yyyy-MM-dd", "dd MMMM yyyy", jadwal.getString("tanggal")));
						JSONArray details = response.getJSONArray("details");
						for (int i = 0; i < details.length(); i++) {
							JSONObject isi = details.getJSONObject(i);
							list.add(new ModelListJobDailyTS(
									isi.getString("id"),
									isi.getString("waktu_mulai"),
									isi.getString("waktu_selesai"),
									isi.getString("lokasi"),
									isi.getString("alamat"),
									isi.getString("jenis_project"),
									isi.getString("survey_lastmile"),
									isi.getString("flag_custom"),
									isi.getString("update_progress")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterJobDailyTS(JobDailyTS.this, list);
						listView.setAdapter(adapter);
					} else if (status.equals("401")) {
						session.logoutUser();
					} else {
						Toast.makeText(JobDailyTS.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(JobDailyTS.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	/*private ArrayList<ModelListJobDailyTS> prepareDataJobDailyTS() {
		ArrayList<ModelListJobDailyTS> rvData = new ArrayList<>();
		for (int i = 0; i < waktu.length; i++) {
			ModelListJobDailyTS model = new ModelListJobDailyTS();
			model.setCheck(check[i]);
			model.setWaktuMulai(waktu[i]);
			model.setLokasi(lokasi[i]);
			model.setAlamat(alamat[i]);
			model.setJenis_job(jenis_job[i]);
			rvData.add(model);
		}
		return rvData;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.posisi = true;
	}
}
