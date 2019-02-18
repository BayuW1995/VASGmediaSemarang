package gmedia.net.id.vasgmediasemarang.menu_history_ts;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_history_vas.ListAdapterHistoryVAS;

public class ListAdapterHistoryTS extends ArrayAdapter {
	private Context context;
	private List<ModelListHistoryTS> list;

	public ListAdapterHistoryTS(Context context, List<ModelListHistoryTS> list) {
		super(context, R.layout.view_lv_history_ts, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView tanggal, jam, nama, alamat, jenis_job;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@NonNull
	@Override
	public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_history_ts, null);
			holder.tanggal = (TextView) convertView.findViewById(R.id.txtTanggalHistoryTS);
			holder.jam = (TextView) convertView.findViewById(R.id.txtJamHistoryTS);
			holder.nama = (TextView) convertView.findViewById(R.id.txtNamaHistoryTS);
			holder.alamat = (TextView) convertView.findViewById(R.id.txtAlamatHistoryTS);
			holder.jenis_job = (TextView) convertView.findViewById(R.id.txtJenisJobHistoryTS);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelListHistoryTS model = list.get(position);
		holder.tanggal.setText(model.getTanggal());
		holder.jam.setText(model.getJam());
		holder.nama.setText(model.getNama());
		holder.alamat.setText(model.getAlamat());
		holder.jenis_job.setText(model.getJenis_job());
		return convertView;
	}
}
