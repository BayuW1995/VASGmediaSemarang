package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ListAdapterEquipmentChild;

public class ListAdapterEquipment extends ArrayAdapter {
	private Context context;
	private List<ModelListEquipment> list;
	private ListAdapterEquipmentChild adapterEquipmentChild;

	public ListAdapterEquipment(Context context, List<ModelListEquipment> list) {
		super(context, R.layout.view_lv_equipment_parent, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView jenis;
		private ListView listView;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}


	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_equipment_parent, null);
			holder.jenis = (TextView) convertView.findViewById(R.id.jenisEquipment);
			holder.listView = (ListView) convertView.findViewById(R.id.lv_equipment_child);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelListEquipment model = list.get(position);
		holder.jenis.setText(model.getJenis());
		adapterEquipmentChild = new ListAdapterEquipmentChild(context, model.getList());
		holder.listView.setAdapter(adapterEquipmentChild);
		return convertView;
	}
}
