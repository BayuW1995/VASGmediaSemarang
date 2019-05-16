package gmedia.net.id.vasgmediasemarang.menu_report_cr;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.ListAdapterMenuDrawer;
import gmedia.net.id.vasgmediasemarang.R;

public class AdapterListLayananReportCR extends ArrayAdapter {
	private Context context;
	private List<ModelLayananReportCR> list;

	public AdapterListLayananReportCR(Context context, List<ModelLayananReportCR> list) {
		super(context, R.layout.view_lv_layanan_report_cr, list);
		this.context = context;
		this.list = list;
	}

	public void addMoreData(List<ModelLayananReportCR> moreData) {

		list.addAll(moreData);
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		private TextView txtValue;
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
			convertView = inflater.inflate(R.layout.view_lv_layanan_report_cr, null);
			holder.txtValue = (TextView) convertView.findViewById(R.id.txtValueLayananReportCR);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelLayananReportCR model = list.get(position);
		holder.txtValue.setText(model.getValue());
		return convertView;
	}
}
