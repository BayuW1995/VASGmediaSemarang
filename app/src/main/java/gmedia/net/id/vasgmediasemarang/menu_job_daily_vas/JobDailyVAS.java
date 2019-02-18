package gmedia.net.id.vasgmediasemarang.menu_job_daily_vas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer.PilihKostumer;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.ConvertDate;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;

public class JobDailyVAS extends AppCompatActivity {

	private RelativeLayout btnTambahKlien, btnPublish;
	private ListView listView;
	private ArrayList<ModelListJobDailyVAS> list;
	private ListAdapterJobDailyVAS adapter;
	private String tgl[] =
			{
					"Senin, 10 juli 2019",
					"Selasa, 11 juli 2019",
					"Rabu, 12 juli 2019",
					"Kamis, 13 juli 2019",
					"jum'at, 14 juli 2019",
					"Sabtu, 15 juli 2019"
			};
	private String nama[] =
			{
					"E-Plaza",
					"Fentura Windows Asia (KIC)",
					"E-Plaza",
					"Fentura Windows Asia (KIC)",
					"E-Plaza",
					"Fentura Windows Asia (KIC)"
			};
	private String alamat[] =
			{
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima"
			};
	private String jam[] =
			{
					"12.00",
					"12.00",
					"12.00",
					"12.00",
					"12.00",
					"12.00"
			};
	private String keterangan[] =
			{
					"Request CRO Request CRO Request CRO Request CRO Request CRO Request CRO Request CRO",
					"Request CRO",
					"Request CRO",
					"Request CRO",
					"Request CRO",
					"Request CRO"
			};
	private ImageView btnTanggal;
	private TextView txtTanggal;
	public static String tanggal = "";
	private String statusPublish = "";
	private Proses proses;
	private LinearLayout table;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily_joblist);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Job Daily VAS");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		session = new SessionManager(JobDailyVAS.this);
		proses = new Proses(this);
		initUI();
		initAction();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			tanggal = bundle.getString("tanggalLama");
			txtTanggal.setText(ConvertDate.convert("yyyy-MM-dd", "dd MMMM yyyy", tanggal));
			prepareDataJobDailyVAS();
		} else {
			tanggal = "";
			initDefaultDate();
		}
	}

	private void initUI() {
		txtTanggal = (TextView) findViewById(R.id.txtTanggalJobDailyVAS);
		btnTanggal = (ImageView) findViewById(R.id.btnTanggalJobDailyVAS);
		listView = (ListView) findViewById(R.id.lvJobDailyVAS);
		btnTambahKlien = (RelativeLayout) findViewById(R.id.btnTambahKlienJobDailyVAS);
		btnPublish = (RelativeLayout) findViewById(R.id.btnPublishJobDailyVAS);
		table = (LinearLayout) findViewById(R.id.table_job_daily_vas);
	}

	private void initAction() {
		/*if (tanggal.equals("")) {
			initDefaultDate();
		}*/
		btnTanggal.setOnClickListener(new View.OnClickListener() {
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
						txtTanggal.setText(sdFormat.format(customDate.getTime()));
						txtTanggal.setAlpha(1);
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
						tanggal = sdf.format(customDate.getTime());
						prepareDataJobDailyVAS();
					}
				};
				new DatePickerDialog(JobDailyVAS.this, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
			}
		});
		btnTambahKlien.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(JobDailyVAS.this, PilihKostumer.class);
				intent.putExtra("home", "tambah");
				intent.putExtra("tanggal", tanggal);
				startActivity(intent);
			}
		});
		btnPublish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (statusPublish.equals("draft")) {
					preparePublished();
				} else {
					Toast.makeText(JobDailyVAS.this, "sudah terpublish", Toast.LENGTH_LONG).show();
				}
			}
		});
	}


	private void initDefaultDate() {
		Date c = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
		String formattedDate = df.format(c);
		txtTanggal.setText(formattedDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		tanggal = sdf.format(c);
		prepareDataJobDailyVAS();
	}

	private void prepareDataJobDailyVAS() {
		proses.ShowDialog();
//		final String tanggalFromAPI="";
		ApiVolley request = new ApiVolley(JobDailyVAS.this, new JSONObject(), "GET", LinkURL.UrlDaftarJadwalVAS + tanggal, "", "", 0, new ApiVolley.VolleyCallback() {
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
						String tanggalFromAPI = jadwal.getString("tanggal");
						statusPublish = jadwal.getString("status");
						JSONArray details = response.getJSONArray("details");
						for (int i = 0; i < details.length(); i++) {
							JSONObject isi = details.getJSONObject(i);
							list.add(new ModelListJobDailyVAS(
									isi.getString("id"),
									tanggalFromAPI,
									isi.getString("nama_site"),
									isi.getString("alamat_site"),
									isi.getString("waktu"),
									isi.getString("note")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterJobDailyVAS(JobDailyVAS.this, list, statusPublish);
						listView.setAdapter(adapter);
						table.setVisibility(View.VISIBLE);
					} else if (status.equals("401")) {
						session.logoutUser();
					} else {
						Toast.makeText(JobDailyVAS.this, message, Toast.LENGTH_LONG).show();
						table.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(JobDailyVAS.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void preparePublished() {
		proses.ShowDialog();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("date", tanggal);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(JobDailyVAS.this, jBody, "POST", LinkURL.UrlPublishJadwal, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						statusPublish = "published";
						adapter = new ListAdapterJobDailyVAS(JobDailyVAS.this, list, statusPublish);
						listView.setAdapter(adapter);
						Toast.makeText(JobDailyVAS.this, message, Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(JobDailyVAS.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(JobDailyVAS.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}
	/*private ArrayList<ModelListJobDailyVAS> prepareDataListDailyJoblist() {
		ArrayList<ModelListJobDailyVAS> rvData = new ArrayList<>();
		for (int i = 0; i < tgl.length; i++) {
			ModelListJobDailyVAS model = new ModelListJobDailyVAS();
			model.setTgl(tgl[i]);
			model.setNama(nama[i]);
			model.setAlamat(alamat[i]);
			model.setJam(jam[i]);
			model.setKeterangan(keterangan[i]);
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
