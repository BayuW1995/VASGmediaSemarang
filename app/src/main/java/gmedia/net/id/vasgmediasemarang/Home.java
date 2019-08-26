package gmedia.net.id.vasgmediasemarang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import gmedia.net.id.vasgmediasemarang.menu_job_daily_cr.JobDailyCR;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_vas.JobDailyVAS;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_ts.JobDailyTS;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;

public class Home extends Fragment {
	private Context context;
	private View view;
	private LinearLayout menuDailyJoblist, menuJobDailyTS, menuJobDailyCR;
	private TextView nama, nip;
	private SessionManager session;
	private final String TAG = "HOME";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home, viewGroup, false);
		context = getContext();
		session = new SessionManager(context);
		initUI();
		initAction();
		return view;
	}

	private void initUI() {
		nama = (TextView) view.findViewById(R.id.namaProfile);
		nip = (TextView) view.findViewById(R.id.nipProfile);
		menuDailyJoblist = (LinearLayout) view.findViewById(R.id.menuDailyJoblist);
		menuJobDailyTS = (LinearLayout) view.findViewById(R.id.menuJobDailyTS);
		menuJobDailyCR = (LinearLayout) view.findViewById(R.id.menuJobDailyCR);

		menuDailyJoblist.setVisibility(View.GONE);
		menuJobDailyTS.setVisibility(View.GONE);
		menuJobDailyCR.setVisibility(View.GONE);

		Set<String> dataMenu = session.getMenu();

		if(dataMenu.size() <= 0) {

			Toast.makeText(context, "Data mene masih kosong, harap login ulang.", Toast.LENGTH_LONG).show();
		}

		for(String menu : dataMenu){

			if(menu.toLowerCase().equals("job_daily_vas")){

				menuDailyJoblist.setVisibility(View.VISIBLE);
			}

			if(menu.toLowerCase().equals("job_daily_ts")){

				menuJobDailyTS.setVisibility(View.VISIBLE);
			}

			if(menu.toLowerCase().equals("job_daily_cr")){

				menuJobDailyCR.setVisibility(View.VISIBLE);
			}
		}

	}

	private void initAction() {
		nama.setText(session.getKeyName());
		nip.setText(session.getKeyUserId());
		menuDailyJoblist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.posisi = false;
				Intent intent = new Intent(context, JobDailyVAS.class);
				startActivity(intent);
			}
		});
		menuJobDailyTS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.posisi = false;
				Intent intent = new Intent(context, JobDailyTS.class);
				startActivity(intent);
			}
		});
		menuJobDailyCR.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.posisi = false;
				Intent intent = new Intent(context, JobDailyCR.class);
				startActivity(intent);
			}
		});
	}
}
