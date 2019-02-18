package gmedia.net.id.vasgmediasemarang.menu_job_daily_ts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts.DetailJobDailyTs;

public class ListAdapterJobDailyTS extends ArrayAdapter {
	private Context context;
	private List<ModelListJobDailyTS> list;
	private Boolean check;

	public ListAdapterJobDailyTS(Context context, List<ModelListJobDailyTS> list) {
		super(context, R.layout.view_lv_job_daily_ts, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {

		private LinearLayout utama;
		private ImageView imgCheck;
		private RelativeLayout garisCheck, garisUnCheck;
		private TextView waktu, lokasi, alamat, jenis_job;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_job_daily_ts, null);
			holder.utama = (LinearLayout) convertView.findViewById(R.id.layoutUtamaLvJobDailyTs);
			holder.imgCheck = (ImageView) convertView.findViewById(R.id.imgCheck);
			holder.garisCheck = (RelativeLayout) convertView.findViewById(R.id.garisCheck);
			holder.garisUnCheck = (RelativeLayout) convertView.findViewById(R.id.garisUnCheck);
			holder.waktu = (TextView) convertView.findViewById(R.id.txtWaktuLvJobDailyTS);
			holder.lokasi = (TextView) convertView.findViewById(R.id.lokasiLvJobDailyTS);
			holder.alamat = (TextView) convertView.findViewById(R.id.alamatLvJobDailyTS);
			holder.jenis_job = (TextView) convertView.findViewById(R.id.jenisJobLvJobDailyTS);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ModelListJobDailyTS model = list.get(position);
		final ViewHolder finalHolder = holder;
		holder.imgCheck.setBackgroundResource(R.drawable.icon_uncheck_job_daily_ts);
//		check = model.getCheck();
		/*if (check) {
			finalHolder.imgCheck.setBackgroundResource(R.drawable.icon_check_job_daily_ts);
			finalHolder.garisCheck.setVisibility(View.VISIBLE);
			finalHolder.garisUnCheck.setVisibility(View.GONE);

		} else {
			finalHolder.imgCheck.setBackgroundResource(R.drawable.icon_uncheck_job_daily_ts);
			finalHolder.garisCheck.setVisibility(View.GONE);
			finalHolder.garisUnCheck.setVisibility(View.VISIBLE);
		}*/
		holder.utama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, DetailJobDailyTs.class);
				intent.putExtra("id", model.getId());
				((Activity) context).startActivity(intent);
			}
		});
		/*holder.utama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!check) {
					finalHolder.imgCheck.setBackgroundResource(R.drawable.icon_check_job_daily_ts);
					finalHolder.garisCheck.setVisibility(View.VISIBLE);
					finalHolder.garisUnCheck.setVisibility(View.GONE);
					check = true;
				} else {
					finalHolder.imgCheck.setBackgroundResource(R.drawable.icon_uncheck_job_daily_ts);
					finalHolder.garisCheck.setVisibility(View.GONE);
					finalHolder.garisUnCheck.setVisibility(View.VISIBLE);
					check = false;
				}
			}
		});*/
		holder.waktu.setText(model.getWaktuMulai() + " - " + model.getWaktuSelesai());
		holder.lokasi.setText(model.getLokasi());
		holder.alamat.setText(model.getAlamat());
		holder.jenis_job.setText(model.getJenis_job());
		return convertView;
	}
}
