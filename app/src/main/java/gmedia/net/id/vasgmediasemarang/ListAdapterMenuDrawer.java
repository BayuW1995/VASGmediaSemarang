package gmedia.net.id.vasgmediasemarang;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapterMenuDrawer extends ArrayAdapter {
	private Context context;
	private List<ModelListMenuDrawer> list;

	public ListAdapterMenuDrawer(Context context, List<ModelListMenuDrawer> list) {
		super(context, R.layout.view_lv_menu_drawer, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private ImageView icon;
		private TextView textIcon;
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
			convertView = inflater.inflate(R.layout.view_lv_menu_drawer, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.imgMenuDrawer);
			holder.textIcon = (TextView) convertView.findViewById(R.id.textMenuDrawer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ModelListMenuDrawer model = list.get(position);
		holder.icon.setImageDrawable(convertView.getResources().getDrawable(model.getIcon()));
		holder.textIcon.setText(model.getTextIcon());
		return convertView;
	}
}
