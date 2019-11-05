package gmedia.net.id.vasgmediasemarang.menu_report_survey;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;

public class ListAdapterReportSurvey extends ArrayAdapter {
	private Context context;
	private List<ModelListReportSurvey> list;

	public ListAdapterReportSurvey(Context context, List<ModelListReportSurvey> list) {
		super(context, R.layout.view_lv_report_survey, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private Spinner pilihan1, pilihan2;
		private EditText isian2;
		private LinearLayout layout1, layout2;
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
			convertView = inflater.inflate(R.layout.view_lv_report_survey, null);
			holder.pilihan1 = (Spinner) convertView.findViewById(R.id.pilihan1ReportSurvey);
			holder.pilihan2 = (Spinner) convertView.findViewById(R.id.pilihan2ReportSurvey);
			holder.isian2 = (EditText) convertView.findViewById(R.id.isian2ReportSurvey);
			holder.layout1 = (LinearLayout) convertView.findViewById(R.id.layoutNonSatuanReportSurvey);
			holder.layout2 = (LinearLayout) convertView.findViewById(R.id.layoutSatuanReportSurvey);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelListReportSurvey model = list.get(position);
		int jenisLayout = model.getJenisLayout();
		if (jenisLayout == 1) {
			holder.layout1.setVisibility(View.VISIBLE);
			holder.layout2.setVisibility(View.GONE);

		} else {
			holder.layout2.setVisibility(View.VISIBLE);
			holder.layout1.setVisibility(View.GONE);
		}
		return convertView;
	}
}
