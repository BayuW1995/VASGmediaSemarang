package gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;


public class ListAdapterEquipmentChild extends ArrayAdapter {
	private Context context;
	private List<ModelListEquipmentChild> list;
	private int selectedId = -1, selectedIdRst = -1;
	private String tipe = "";

	public ListAdapterEquipmentChild(Context context, List<ModelListEquipmentChild> list) {
		super(context, R.layout.view_lv_equipment_child, list);
		this.context = context;
		this.list = list;
	}

	private static class ViewHolder {
		private TextView jenisRadio, jenisTeks, standardRadio, standardTeks;
		private RadioGroup radioGroup, radioRst;
		private RadioButton radioButton, radioButtonRst;
		private RadioButton radio1, radio2, radio3;
		private EditText isian, note1Teks, note2teks;
		private LinearLayout layoutRadio, layoutTeks;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		final ModelListEquipmentChild model = list.get(position);
		tipe = model.getTipe();
//		tipe = "radio";
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_equipment_child, null);
			holder.layoutRadio = (LinearLayout) convertView.findViewById(R.id.layoutReportVASRadio);
			holder.layoutTeks = (LinearLayout) convertView.findViewById(R.id.layoutReportVASTeks);
			if (tipe.equals("radio")) {
				holder.layoutRadio.setVisibility(View.VISIBLE);
				holder.layoutTeks.setVisibility(View.GONE);
				holder.jenisRadio = (TextView) convertView.findViewById(R.id.jenisEquipmentChildRadio);
				holder.radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGrup);
//			holder.radioRst = (RadioGroup) convertView.findViewById(R.id.grupResult);
				holder.isian = (EditText) convertView.findViewById(R.id.noteRadio);
				holder.radio1 = (RadioButton) convertView.findViewById(R.id.radio1);
				holder.radio2 = (RadioButton) convertView.findViewById(R.id.radio2);
				holder.radio3 = (RadioButton) convertView.findViewById(R.id.radio3);
//				holder.standardRadio = (TextView) convertView.findViewById(R.id.standardReportVASRadio);
			} else if (tipe.equals("text")) {
				holder.layoutTeks.setVisibility(View.VISIBLE);
				holder.layoutRadio.setVisibility(View.GONE);
				holder.jenisTeks = (TextView) convertView.findViewById(R.id.jenisEquipmentChildTeks);
//				holder.standardTeks = (TextView) convertView.findViewById(R.id.standardReportVASTeks);
				holder.note1Teks = (EditText) convertView.findViewById(R.id.noteTeks1);
				holder.note2teks = (EditText) convertView.findViewById(R.id.noteTeks2);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final int finalPosition = position;
//		if (tipe.equals(""))ffdg
		if (tipe.equals("radio")) {
			holder.jenisRadio.setText(model.getJenis_child());
//			holder.standardRadio.setText(model.getStandard());
			final ViewHolder finalHolder = holder;
			final View finalConvertView = convertView;
			if (model.getRadio1().equals(holder.radio1.getText().toString())) {
				holder.radio1.setChecked(true);
			} else if (model.getRadio1().equals(holder.radio2.getText().toString())) {
				holder.radio2.setChecked(true);
			} else if (model.getRadio1().equals(holder.radio3.getText().toString())) {
				holder.radio3.setChecked(true);
			}
			holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
					selectedId = finalHolder.radioGroup.getCheckedRadioButtonId();
					finalHolder.radioButton = (RadioButton) finalConvertView.findViewById(selectedId);
					model.setRadio1(finalHolder.radioButton.getText().toString());
				}
			});
			holder.isian.setText(model.getIsian());
			holder.isian.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence c, int start, int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence c, int start, int before, int count) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.toString().equals("")) {
						model.setIsian("");
					} else {
						model.setIsian(editable.toString());
//						Log.d("posisi", String.valueOf(position));
					}
				}
			});
		} else if (tipe.equals("text")) {
			holder.jenisTeks.setText(model.getJenis_child());
//			holder.standardTeks.setText(model.getStandard());
			holder.note1Teks.setText(model.getNoteTeks1());
			final ViewHolder finalHolder1 = holder;
			holder.note1Teks.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.toString().equals("")) {
						finalHolder1.note1Teks.setError("Mohon di isi");
						finalHolder1.note1Teks.requestFocus();
					} else {
						model.setNoteTeks1(editable.toString());
//						Log.d("posisi", String.valueOf(position));
					}
				}
			});
			holder.note2teks.setText(model.getIsian());
			holder.note2teks.addTextChangedListener(new TextWatcher() {
				@Override
				public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

				}

				@Override
				public void afterTextChanged(Editable editable) {
					if (editable.toString().equals("")) {
						model.setIsian("");
					} else {
						model.setIsian(editable.toString());
//						Log.d("posisi", String.valueOf(position));
					}
				}
			});
		}

		return convertView;
	}
}
/*holder.isian.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View view, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					//check if the right key was pressed
					if (keyCode == KeyEvent.KEYCODE_BACK) {
						if (finalHolder.isian.getText().toString().equals("")) {
							model.setIsian("");
						} else {
							String isian = finalHolder.isian.getText().toString();
							model.setIsian(isian);
						}
						return true;
					}
				}
				return false;
			}
		});*/
/*if (finalHolder.isian.getText().toString().equals("")) {
					model.setIsian("");
				} else {
					String isian = finalHolder.isian.getText().toString();
					model.setIsian(isian);
				}*/
/*switch (checkedId) {
					case R.id.radio1:
						selectedId = finalHolder.radioGroup.getCheckedRadioButtonId();
						finalHolder.radioButton = (RadioButton) finalConvertView.findViewById(selectedId);
						model.setRadio1(finalHolder.radioButton.getText().toString());
						break;
					case R.id.radio2:
						selectedId = finalHolder.radioGroup.getCheckedRadioButtonId();
						finalHolder.radioButton = (RadioButton) finalConvertView.findViewById(selectedId);
						model.setRadio1(finalHolder.radioButton.getText().toString());
						break;
					case R.id.radio3:
						selectedId = finalHolder.radioGroup.getCheckedRadioButtonId();
						finalHolder.radioButton = (RadioButton) finalConvertView.findViewById(selectedId);
						model.setRadio1(finalHolder.radioButton.getText().toString());
						break;
					default:
						model.setRadio1("");

				}*/
/*holder.radioRst.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
				switch (checkedId) {
					case R.id.radioRst1:
						selectedId = finalHolder.radioRst.getCheckedRadioButtonId();
						finalHolder.radioButtonRst = (RadioButton) finalConvertView.findViewById(selectedId);
						model.setRadio2(finalHolder.radioButtonRst.getText().toString());
						break;
					case R.id.radioRst2:
						selectedId = finalHolder.radioRst.getCheckedRadioButtonId();
						finalHolder.radioButtonRst = (RadioButton) finalConvertView.findViewById(selectedId);
						model.setRadio2(finalHolder.radioButtonRst.getText().toString());
						break;
				}
			}
		});*/
		/*selectedId = holder.radioGroup.getCheckedRadioButtonId();
		if (selectedId != -1) {
			holder.radioButton = (RadioButton) convertView.findViewById(selectedId);
			model.setRadio1(holder.radioButton.getText().toString());

		}
		selectedIdRst = holder.radioRst.getCheckedRadioButtonId();
		if (selectedIdRst != -1) {
			holder.radioButtonRst = (RadioButton) convertView.findViewById(selectedIdRst);
			model.setRadio2(holder.radioButtonRst.getText().toString());
		}
		if (!holder.isian.getText().toString().equals("")) {
			model.setIsian(holder.isian.getText().toString());
		}*/