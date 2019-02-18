package gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_ts.JobDailyTS;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_vas.JobDailyVAS;
import gmedia.net.id.vasgmediasemarang.menu_report_survey.ReportSurvey;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.ConvertDate;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.MapsActivity;
import gmedia.net.id.vasgmediasemarang.utils.Proses;

public class DetailJobDailyTs extends AppCompatActivity {
	private ListView listView;
	private ListAdapterDetailJobDailyTS adapter;
	private ArrayList<ModelDetailJobDailyTS> list;
	private String nama[] =
			{
					"Joko Widodo",
					"Raharjo",
					"Joko Widodo",
					"Raharjo",
					"Joko Widodo",
					"Raharjo",
			};
	private String noHp[] =
			{
					"0897836427364",
					"0852384243723",
					"0897836427364",
					"0852384243723",
					"0897836427364",
					"0852384243723"
			};
	private RelativeLayout btnReportSurvey;
	public static String idJobDailyTS = "", latitude = "", longitude = "";
	private Proses proses;
	private TextView tanggal, waktu, namaLokasi, alamat, jenis_project, note;
	private LinearLayout btnMaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_job_daily_ts);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Detail Job Daily TS");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.layoutKuning)));
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = DetailJobDailyTs.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.notifBarKuning));
		}
		proses = new Proses(DetailJobDailyTs.this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			idJobDailyTS = bundle.getString("id");
		}
		initUI();
		initAction();
	}

	private void initUI() {
		tanggal = (TextView) findViewById(R.id.txtTanggalDetailJobDailyTS);
		waktu = (TextView) findViewById(R.id.txtWaktuDetailJobDailyTS);
		namaLokasi = (TextView) findViewById(R.id.txtNamaLokasiDetailJobDailyTS);
		alamat = (TextView) findViewById(R.id.txtAlamatDetailJobDailyTS);
		jenis_project = (TextView) findViewById(R.id.txtJenisProjectDetailJobDailyTS);
		note = (TextView) findViewById(R.id.txtNoteDetailJobDailyTS);
		btnReportSurvey = (RelativeLayout) findViewById(R.id.btnReportSurvey);
		listView = (ListView) findViewById(R.id.lv_detail_job_daily_ts);
		btnMaps = (LinearLayout) findViewById(R.id.btnMapsDetailJobDailyTS);
	}

	private void initAction() {
		tanggal.setText(ConvertDate.convert("yyyy-MM-dd", "dd MMMM yyyy", JobDailyTS.tanggalJobDailyTS));
		prepareDataJobDailyTS();
		btnMaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DetailJobDailyTs.this, MapsActivity.class);
				startActivity(intent);
			}
		});
		btnReportSurvey.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(DetailJobDailyTs.this, ReportSurvey.class);
				startActivity(intent);
			}
		});
	}

	private void prepareDataJobDailyTS() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(DetailJobDailyTs.this, new JSONObject(), "GET", LinkURL.UrlDetailJobDailyTS + idJobDailyTS, "", "", 0, new ApiVolley.VolleyCallback() {
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
						JSONObject detail = response.getJSONObject("detail");
						String waktuMulai = ConvertDate.convert("HH:mm:ss", "HH:mm", detail.getString("waktu_mulai"));
						String waktuSelesai = ConvertDate.convert("HH:mm:ss", "HH:mm", detail.getString("waktu_selesai"));
						waktu.setText(waktuMulai + " - " + waktuSelesai);
						namaLokasi.setText(detail.getString("lokasi"));
						alamat.setText(detail.getString("alamat"));
						jenis_project.setText(detail.getString("jenis_project"));
						note.setText(detail.getString("note"));
						latitude = detail.getString("latitude");
						longitude = detail.getString("longitude");
						JSONArray pic = detail.getJSONArray("pics");
						for (int i = 0; i < pic.length(); i++) {
							JSONObject isiPIC = pic.getJSONObject(i);
							list.add(new ModelDetailJobDailyTS(
									isiPIC.getString("nama"),
									isiPIC.getString("telepon")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterDetailJobDailyTS(DetailJobDailyTs.this, list);
						listView.setAdapter(adapter);
					} else {
						Toast.makeText(DetailJobDailyTs.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(DetailJobDailyTs.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	/*private ArrayList<ModelDetailJobDailyTS> prepareDataDetailJobDailyTS() {
		ArrayList<ModelDetailJobDailyTS> rvData = new ArrayList<>();
		for (int i = 0; i < nama.length; i++) {
			ModelDetailJobDailyTS model = new ModelDetailJobDailyTS();
			model.setNama(nama[i]);
			model.setNoHp(noHp[i]);
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
}
