package gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.ListAdapterEquipment;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_ts.ModelListJobDailyTS;

public class ListAdapterDetailJobDailyTS extends ArrayAdapter {
	private Context context;
	private List<ModelDetailJobDailyTS> list;

	public ListAdapterDetailJobDailyTS(Context context, List<ModelDetailJobDailyTS> list) {
		super(context, R.layout.view_lv_detail_job_daily_ts, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView nama, noHp;
		private LinearLayout layoutUtama;
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
			convertView = inflater.inflate(R.layout.view_lv_detail_job_daily_ts, null);
			holder.nama = (TextView) convertView.findViewById(R.id.txtNamaDetailJobDailyTS);
			holder.noHp = (TextView) convertView.findViewById(R.id.txtNoHpDetailJobDailyTS);
			holder.layoutUtama = (LinearLayout) convertView.findViewById(R.id.layoutUtamaLvDetailJobDailyTS);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ModelDetailJobDailyTS model = list.get(position);
		holder.nama.setText(model.getNama());
		holder.noHp.setText(model.getNoHp());
		holder.layoutUtama.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + model.getNoHp()));
				((Activity) context).startActivity(intent);
			}
		});
		return convertView;
	}
}
