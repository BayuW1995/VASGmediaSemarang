package gmedia.net.id.vasgmediasemarang.menu_history_vas;

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
import gmedia.net.id.vasgmediasemarang.menu_equipment.ListAdapterEquipment;

public class ListAdapterHistoryVAS extends ArrayAdapter {
	private Context context;
	private List<ModelHistoryVAS> list;

	public ListAdapterHistoryVAS(Context context, List<ModelHistoryVAS> list) {
		super(context, R.layout.view_lv_history_vas, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView tanggal, jam, nama, alamat;
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
			convertView = inflater.inflate(R.layout.view_lv_history_vas, null);
			holder.tanggal = (TextView) convertView.findViewById(R.id.txtTanggalHistoryVAS);
			holder.jam = (TextView) convertView.findViewById(R.id.txtJamHistoryVAS);
			holder.nama = (TextView) convertView.findViewById(R.id.txtNamaHistoryVAS);
			holder.alamat = (TextView) convertView.findViewById(R.id.txtAlamatHistoryVAS);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelHistoryVAS model = list.get(position);
		holder.tanggal.setText(model.getTanggal());
		holder.jam.setText(model.getJam());
		holder.nama.setText(model.getNama());
		holder.alamat.setText(model.getAlamat());
		return convertView;
	}
}
