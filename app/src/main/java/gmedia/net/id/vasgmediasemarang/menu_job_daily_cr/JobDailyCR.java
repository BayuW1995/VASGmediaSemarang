package gmedia.net.id.vasgmediasemarang.menu_job_daily_cr;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.List;
import java.util.Locale;

import gmedia.net.id.vasgmediasemarang.MainActivity;
import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer.PilihKostumer;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.ConvertDate;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;
import gmedia.net.id.vasgmediasemarang.utils.WrapContentListView;

public class JobDailyCR extends AppCompatActivity {

	private SessionManager session;
	private Context context;
	private Proses proses;
	private ImageView btnTanggal;
	private TextView txtTanggal;
	public static String tanggal = "";
	private String statusPublish = "";
	private LinearLayout table;
	private RelativeLayout btnTambahKlien, btnPublish;
	private ListView listView;
	private List<ModelListJobDailyCR> list;
	private ListAdapterJobDailyCR adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job_daily_cr);

		context = this;

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Job Daily CR");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);

		session = new SessionManager(context);
		proses = new Proses(context);

		initUI();
		initAction();

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			tanggal = bundle.getString("tanggalLama");
			txtTanggal.setText(ConvertDate.convert("yyyy-MM-dd", "dd MMMM yyyy", tanggal));
			prepareDataJobDailyCR();
		} else {
			tanggal = "";
			initDefaultDate();
		}
	}


	private void initUI() {
		txtTanggal = (TextView) findViewById(R.id.txtTanggalJobDailyCR);
		btnTanggal = (ImageView) findViewById(R.id.btnTanggalJobDailyCR);
		listView = (ListView) findViewById(R.id.lvJobDailyCR);
		btnTambahKlien = (RelativeLayout) findViewById(R.id.btnTambahKlienJobDailyCR);
		btnPublish = (RelativeLayout) findViewById(R.id.btnPublishJobDailyCR);
		table = (LinearLayout) findViewById(R.id.table_job_daily_cr);
	}

	private void initDefaultDate() {
		Date c = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
		String formattedDate = df.format(c);
		txtTanggal.setText(formattedDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		tanggal = sdf.format(c);
		prepareDataJobDailyCR();
	}

	private void initAction() {
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
						prepareDataJobDailyCR();
					}
				};
				new DatePickerDialog(context, date, customDate.get(java.util.Calendar.YEAR), customDate.get(java.util.Calendar.MONTH), customDate.get(java.util.Calendar.DATE)).show();
			}
		});
		btnTambahKlien.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, PilihKostumer.class);
				intent.putExtra("home", "tambahCR");
				intent.putExtra("tanggal", tanggal);
				startActivity(intent);
			}
		});
		btnPublish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				preparePublished();
				/*if (statusPublish.equals("0")) {
					preparePublished();
				} else {
					Toast.makeText(JobDailyVAS.this, "sudah terpublish", Toast.LENGTH_LONG).show();
				}*/
			}
		});
	}

	private void prepareDataJobDailyCR() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDaftarJadwalCR + tanggal, "", "", 0, new ApiVolley.VolleyCallback() {
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
						statusPublish = jadwal.getString("is_publish");
						JSONArray details = response.getJSONArray("details");
						for (int i = 0; i < details.length(); i++) {
							JSONObject isi = details.getJSONObject(i);
							list.add(new ModelListJobDailyCR(
									isi.getString("id"),
									isi.getString("customer_id"),
									tanggalFromAPI,
									isi.getString("nama_site"),
									isi.getString("alamat_site"),
									isi.getString("waktu"),
									isi.getString("is_report"),
									isi.getString("note")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterJobDailyCR(context, list, statusPublish);
						listView.setAdapter(adapter);
						WrapContentListView.setListViewHeightBasedOnChildren(listView);
						table.setVisibility(View.VISIBLE);
					} else if (status.equals("401")) {
						session.logoutUser();
					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
						table.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
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
		ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlPublishJadwalCR, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						/*statusPublish = "published";
						adapter = new ListAdapterJobDailyVAS(JobDailyVAS.this, list, statusPublish);
						listView.setAdapter(adapter);*/
						prepareDataJobDailyCR();
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.send, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case R.id.action_send:
				prepareSendDataJobDailyCR();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void prepareSendDataJobDailyCR() {
		proses.ShowDialog();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("date", tanggal);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request=new ApiVolley(context, jBody, "POST", LinkURL.UrlSendDataReportCR, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object=new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.posisi = true;
	}
}
