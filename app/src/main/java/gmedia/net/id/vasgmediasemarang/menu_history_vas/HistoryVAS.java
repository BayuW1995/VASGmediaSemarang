package gmedia.net.id.vasgmediasemarang.menu_history_vas;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.MainActivity;
import gmedia.net.id.vasgmediasemarang.R;

public class HistoryVAS extends AppCompatActivity {
	private ArrayList<ModelHistoryVAS> list;
	private ListAdapterHistoryVAS adapter;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_vas);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("History VAS");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		initUI();
		initAction();
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.lv_history_vas);
	}

	private void initAction() {
		list = new ArrayList<>();
		list = prepareDataHistoryVAS();
		adapter = new ListAdapterHistoryVAS(HistoryVAS.this, list);
		listView.setAdapter(adapter);
	}

	private ArrayList<ModelHistoryVAS> prepareDataHistoryVAS() {
		ArrayList<ModelHistoryVAS> rvData = new ArrayList<>();
		for (int i = 0; i < tanggal.length; i++) {
			ModelHistoryVAS model = new ModelHistoryVAS();
			model.setTanggal(tanggal[i]);
			model.setJam(jam[i]);
			model.setNama(nama[i]);
			model.setAlamat(alamat[i]);
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
