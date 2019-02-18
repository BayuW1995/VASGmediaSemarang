package gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child;

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


public class ListAdapterEquipmentChild extends ArrayAdapter {
	private Context context;
	private List<ModelListEquipmentChild> list;

	public ListAdapterEquipmentChild(Context context, List<ModelListEquipmentChild> list) {
		super(context, R.layout.view_lv_equipment_child);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView jenis;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_equipment_child, null);
			holder.jenis = (TextView) convertView.findViewById(R.id.jenisEquipmentChild);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelListEquipmentChild model = list.get(position);
		holder.jenis.setText(model.getJenis_child());
		return convertView;
	}
}
