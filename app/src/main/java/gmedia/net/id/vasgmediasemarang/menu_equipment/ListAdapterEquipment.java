package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ListAdapterEquipmentChild;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.WrapContentListView;

public class ListAdapterEquipment extends ArrayAdapter {
	private final Proses proses;
	private Context context;
	private List<ModelListEquipment> list;
	private List<ModelListEquipmentChild> listChild;
	private ListAdapterEquipmentChild adapterEquipmentChild;
	RecyclerViewAdapterEquipmentParent recyclerViewAdapterEquipmentParent;
	private String tipe = "";

	public ListAdapterEquipment(Context context, List<ModelListEquipment> list) {
		super(context, R.layout.view_lv_equipment_parent, list);
		this.context = context;
		this.list = list;
		proses = new Proses(context);
	}

	private static class ViewHolder {
		private TextView jenis;
		private ListView listView;
	}

	public void addMoreData(List<ModelListEquipment> moreData) {
		list.addAll(moreData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return super.getCount();
	}


	/*@Override
	public Object getItem(int position) {
		return super.getItem(position);
	}*/

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
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
		final ModelListEquipment model = list.get(position);
		holder.jenis.setText(model.getRomawi() + " " + model.getJudul());
		adapterEquipmentChild = new ListAdapterEquipmentChild(context, model.getList());
		holder.listView.setAdapter(adapterEquipmentChild);
		WrapContentListView.setListViewHeightBasedOnChildren(holder.listView);
		return convertView;
	}
}
/*holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				ModelListEquipmentChild modelChild = model.getList().get(position);
				String teks1 = (String) modelChild.getRadio1();
				String isian = modelChild.getIsian();
				Toast.makeText(context, "You clicked " + teks1 + " & " + isian, Toast.LENGTH_SHORT).show();
			}
		});*/
/*				Toast.makeText(context, "" + adapterView.getAdapter().getItem(position), Toast.LENGTH_LONG).show();
				adapterView.getAdapter().getItem(position);
				Object clickItemObj = adapterView.getAdapter().getItem(position);
				HashMap clickItemMap = (HashMap) clickItemObj;
				Toast.makeText(context, "" + clickItemObj, Toast.LENGTH_SHORT).show();
				ModelListEquipmentChild model = listChild.get(position);*/