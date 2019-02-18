package gmedia.net.id.vasgmediasemarang.menu_history_ts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.MainActivity;
import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_ts.JobDailyTS;

public class HistoryTS extends AppCompatActivity {
	private ArrayList<ModelListHistoryTS> list;
	private ListAdapterHistoryTS adapter;
	private ListView listView;
	private String tanggal[] =
			{
					"11 januari 2013",
					"20 januari 2019",
					"11 januari 2013",
					"20 januari 2019",
					"11 januari 2013",
					"20 januari 2019",
					"11 januari 2013",
					"20 januari 2019",
			};
	private String jam[] =
			{
					"10.00",
					"12.00",
					"10.00",
					"12.00",
					"10.00",
					"12.00",
					"10.00",
					"12.00",
			};
	private String nama[] =
			{
					"E-Plaza",
					"Citraland",
					"E-Plaza",
					"Citraland",
					"E-Plaza",
					"Citraland",
					"E-Plaza",
					"Citraland",
			};
	private String alamat[] =
			{
					"Gajahmada Plaza Lt.2 Simpang Lima",
					"Jl. Jendral Sudirman No.294 Semarang",
					"Jl. Sisinga Mangaraja No.16 Semarang",
					"Gajahmada Plaza Lt.2 Simpang Lima",
					"Jl. Jendral Sudirman No.294 Semarang",
					"Jl. Sisinga Mangaraja No.16 Semarang",
					"Jl. Jendral Sudirman No.294 Semarang",
					"Jl. Sisinga Mangaraja No.16 Semarang",
			};
	private String jenis_job[] =
			{
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
					"Instalasi (LAN / Wifi)",
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_ts);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("History TS");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.layoutKuning)));
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = HistoryTS.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.notifBarKuning));
		}
		initUI();
		initAction();
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.lv_history_ts);
	}

	private void initAction() {
		list = new ArrayList<>();
		list = prepareDataHistoryTS();
		adapter = new ListAdapterHistoryTS(HistoryTS.this, list);
		listView.setAdapter(adapter);
	}

	private ArrayList<ModelListHistoryTS> prepareDataHistoryTS() {
		ArrayList<ModelListHistoryTS> rvData = new ArrayList<>();
		for (int i = 0; i < tanggal.length; i++) {
			ModelListHistoryTS model = new ModelListHistoryTS();
			model.setTanggal(tanggal[i]);
			model.setJam(jam[i]);
			model.setNama(nama[i]);
			model.setAlamat(alamat[i]);
			model.setJenis_job(jenis_job[i]);
			rvData.add(model);
		}
		return rvData;
	}

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
