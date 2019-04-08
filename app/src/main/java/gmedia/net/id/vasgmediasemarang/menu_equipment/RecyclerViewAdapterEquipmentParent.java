package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ListAdapterEquipmentChild;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;
import gmedia.net.id.vasgmediasemarang.utils.WrapContentListView;

public class RecyclerViewAdapterEquipmentParent extends RecyclerView.Adapter<RecyclerViewAdapterEquipmentParent.ViewHolder> {
	private Context context;
	private List<ModelListEquipment> list;
	private List<ModelListEquipmentChild> listChild;
	private ListAdapterEquipmentChild adapterEquipmentChild;

	public RecyclerViewAdapterEquipmentParent(Context context, ArrayList<ModelListEquipment> list) {
		this.context = context;
		this.list = list;
	}

	public void addMoreData(List<ModelListEquipment> moreData) {
		list.addAll(moreData);
		notifyDataSetChanged();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView jenis;
		private ListView listView;

		public ViewHolder(View itemView) {
			super(itemView);
			context = itemView.getContext();
			jenis = (TextView) itemView.findViewById(R.id.jenisEquipment);
			listView = (ListView) itemView.findViewById(R.id.lv_equipment_child);
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_lv_equipment_parent, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		ModelListEquipment model = list.get(position);
		holder.jenis.setText(model.getRomawi() + " " + model.getJudul());
		holder.listView.setAdapter(null);
		adapterEquipmentChild = new ListAdapterEquipmentChild(context, model.getList());
		holder.listView.setAdapter(adapterEquipmentChild);
		WrapContentListView.setListViewHeightBasedOnChildren(holder.listView);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	@Override
	public void onViewDetachedFromWindow(ViewHolder holder) {
		super.onViewDetachedFromWindow(holder);
		holder.itemView.clearAnimation();
	}
}
