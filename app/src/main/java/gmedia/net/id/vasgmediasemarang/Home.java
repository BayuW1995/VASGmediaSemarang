package gmedia.net.id.vasgmediasemarang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import gmedia.net.id.vasgmediasemarang.menu_job_daily_vas.JobDailyVAS;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_ts.JobDailyTS;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;

public class Home extends Fragment {
	private Context context;
	private View view;
	private LinearLayout menuDailyJoblist, menuVisitMaintenance, menuJobDailyTS;
	private TextView nama, nip;
	private SessionManager session;

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
//		menuVisitMaintenance = (LinearLayout) view.findViewById(R.id.menuVisitMaintenance);
		menuJobDailyTS = (LinearLayout) view.findViewById(R.id.menuJobDailyTS);
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
		/*menuVisitMaintenance.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.posisi = false;
				Intent intent = new Intent(context, PilihKostumer.class);
				intent.putExtra("home", "menu maintenance");
				startActivity(intent);
			}
		});*/
		menuJobDailyTS.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				MainActivity.posisi = false;
				Intent intent = new Intent(context, JobDailyTS.class);
				startActivity(intent);
			}
		});
	}
}
