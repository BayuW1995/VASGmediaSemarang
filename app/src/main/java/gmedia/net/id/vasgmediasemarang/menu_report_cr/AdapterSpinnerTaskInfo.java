package gmedia.net.id.vasgmediasemarang.menu_report_cr;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.MasterModelPIC;

public class AdapterSpinnerTaskInfo extends ArrayAdapter {
	private List<ModelDropdownTaskInfo> list;
	LayoutInflater inflater;
	private TextView nama, email;

	public AdapterSpinnerTaskInfo(Activity context, int resourceID, int textviewId, List<ModelDropdownTaskInfo> list) {
		super(context, resourceID, textviewId, list);
		this.list = list;
		inflater = context.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	/*@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            *//*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*//*
			convertView = inflater.inflate(R.layout.view_spinner_pic, null);
			holder.nama = (TextView) convertView.findViewById(R.id.txtNamaPIC);
			holder.email = (TextView) convertView.findViewById(R.id.txtEmailPIC);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		MasterModelPIC modelPIC = list.get(position);
		holder.nama.setText(modelPIC.getNama());
		holder.email.setText(modelPIC.getEmail());
		return convertView;
	}*/

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_spinner_task_info, null, true);
			nama = (TextView) convertView.findViewById(R.id.txtNamaTaskInfo);
		}
		ModelDropdownTaskInfo modelPIC = list.get(position);
		nama.setText(modelPIC.getText());
		return convertView;
	}

	/*@Override
	public boolean isEnabled(int position) {
		if (position == 0) {
			// Disable the first item from Spinner
			// First item will be use for hint
//			dropdownPIC.setAlpha(0.5f);
			return false;
		} else if (position == list.size() - 1) {
			Toast.makeText(getContext(), "iki posisi trakhir coooyy", Toast.LENGTH_SHORT).show();
			return true;
		} else {
//			dropdownPIC.setAlpha(1);
			return true;
		}
	}*/
}
