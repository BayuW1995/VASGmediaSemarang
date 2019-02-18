package gmedia.net.id.vasgmediasemarang.menu_report_survey;

import android.app.Dialog;
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

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts.DetailJobDailyTs;
import gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer.ModelListPilihKostumer;

public class ReportSurvey extends AppCompatActivity {
	private ListView listView;
	private ListAdapterReportSurvey adapter;
	private ArrayList<ModelListReportSurvey> list;
	private int jenisLayout[] =
			{
					1,
					2,
					2,
					1,
					2,
					2,
					1,
					2,
					2
			};
	private LinearLayout btnKirim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_survey);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Report Survey");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.layoutKuning)));
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = ReportSurvey.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.notifBarKuning));
		}
		initUI();
		initAction();
	}

	private void initUI() {
//		listView = (ListView) findViewById(R.id.lv_report_survey);
		btnKirim = (LinearLayout) findViewById(R.id.btnKirimReport);
	}

	private void initAction() {
		/*list = new ArrayList<>();
		list = prepareDataFormReportSurvey();
		adapter = new ListAdapterReportSurvey(ReportSurvey.this, list);
		listView.setAdapter(adapter);*/
		btnKirim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Dialog dialog = new Dialog(ReportSurvey.this);
				dialog.setContentView(R.layout.popup_validasi_kirim_report);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				RelativeLayout btnIya = (RelativeLayout) dialog.findViewById(R.id.btnIyaPopupValidasiKirimReport);
				btnIya.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
				RelativeLayout btnTidak = (RelativeLayout) dialog.findViewById(R.id.btnTidakPopupValidasiKirimReport);
				btnTidak.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				Window window = dialog.getWindow();
				window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
			}
		});
	}

	private ArrayList<ModelListReportSurvey> prepareDataFormReportSurvey() {
		ArrayList<ModelListReportSurvey> rvData = new ArrayList<>();
		for (int i = 0; i < jenisLayout.length; i++) {
			ModelListReportSurvey model = new ModelListReportSurvey();
			model.setJenisLayout(jenisLayout[i]);
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
}
