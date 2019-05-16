package gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_cr.JobDailyCR;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_vas.JobDailyVAS;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;

public class ListAdapterPilihKostumer extends ArrayAdapter {
	private final Proses proses;
	private Context context;
	private List<ModelListPilihKostumer> listPilihKostumer;
	//	private ModelListPilihKostumer modelListPilihKostumer;
	private TextView txtTime;
	private String tanggal = "", id = "";
	private EditText isianKeterangan;

	public ListAdapterPilihKostumer(Context context, List<ModelListPilihKostumer> listPilihKostumer, String tanggal, String id) {
		super(context, R.layout.view_lv_pilih_kostumer, listPilihKostumer);
		this.context = context;
		this.listPilihKostumer = listPilihKostumer;
		this.tanggal = tanggal;
		this.id = id;
//		this.service_id = service_id;
		proses = new Proses(context);
	}

	private static class ViewHolder {
		private TextView customer_id, service_id, nama, alamat;
		private RelativeLayout btnProses;
	}

	public void addMoreData(List<ModelListPilihKostumer> moreData) {
		listPilihKostumer.addAll(moreData);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_pilih_kostumer, null);
			holder.customer_id = (TextView) convertView.findViewById(R.id.txtCustomerID);
			holder.service_id = (TextView) convertView.findViewById(R.id.txtServiceID);
			holder.nama = (TextView) convertView.findViewById(R.id.txtNama);
			holder.alamat = (TextView) convertView.findViewById(R.id.txtAlamat);
			holder.btnProses = (RelativeLayout) convertView.findViewById(R.id.btnProsesPilihKostumer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ModelListPilihKostumer modelListPilihKostumer = listPilihKostumer.get(position);
//		service_id = modelListPilihKostumer.getService_id();
		holder.customer_id.setText(modelListPilihKostumer.getCustomer_id());
		holder.service_id.setText(modelListPilihKostumer.getService_id());
		holder.nama.setText(modelListPilihKostumer.getNama());
		holder.alamat.setText(modelListPilihKostumer.getAlamat());
		holder.btnProses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Dialog dialog = new Dialog(context);
				dialog.setContentView(R.layout.popup_input_jam_kerja);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				txtTime = (TextView) dialog.findViewById(R.id.txtTimePopupInputJamKerja);
				isianKeterangan = (EditText) dialog.findViewById(R.id.etKeteranganPopupInputJamKerja);
				ImageView btnTime = (ImageView) dialog.findViewById(R.id.btnTimePopupInputJamKerja);
				btnTime.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						java.util.Calendar mcurrentTime = java.util.Calendar.getInstance();
						int hour = 9;
						int minute = 0;
//						int hour = mcurrentTime.get(java.util.Calendar.HOUR_OF_DAY);
//						int minute = mcurrentTime.get(java.util.Calendar.MINUTE);
						TimePickerDialog mTimePicker;
						mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
							@Override
							public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
								String jam = String.valueOf(selectedHour);
								String menit = String.valueOf(selectedMinute);
								jam = jam.length() < 2 ? "0" + jam : jam;
								menit = menit.length() < 2 ? "0" + menit : menit;
								txtTime.setText(jam + ":" + menit);
//                        textTimePixer.setAlpha(1);
							}
						}, hour, minute, true);//Yes 24 hour time
						mTimePicker.setTitle("Select Time");
						mTimePicker.show();
					}
				});
				RelativeLayout btnOk = (RelativeLayout) dialog.findViewById(R.id.btnOKPopupInputJamKerja);
				btnOk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (txtTime.getText().toString().equals("")) {
//								dialog.dismiss();
							Toast.makeText(context, "Mohon pilih jam dahulu", Toast.LENGTH_LONG).show();
							return;
						} /*else if (isianKeterangan.getText().toString().equals("")) {
								isianKeterangan.setError("Mohon di isi terlebih dahulu");
								isianKeterangan.requestFocus();
								return;
							}*/ else {
							dialog.dismiss();
							if (PilihKostumer.isTambah) {
								prepareTambahDataPilihKostumer();
							} else if (PilihKostumer.isEdit) {
								prepareEditDataPilihKostumer();
							} else if (PilihKostumer.isTambahCR) {
								prepareTambahDataPilihKostumerCR();
							} else if (PilihKostumer.isEditCR) {
								prepareEditDataPilihKostumerCR();
							}
						}
					}

					private void prepareEditDataPilihKostumerCR() {
						proses.ShowDialog();
						JSONObject jBody = new JSONObject();
						try {
							jBody.put("id", id);
							jBody.put("customer_id", modelListPilihKostumer.getCustomer_id());
							jBody.put("service_id", modelListPilihKostumer.getService_id());
							jBody.put("time", txtTime.getText().toString());
							if (!isianKeterangan.getText().toString().equals("")) {
								jBody.put("note", isianKeterangan.getText().toString());
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlEditJadwalCR, "", "", 0, new ApiVolley.VolleyCallback() {
							@Override
							public void onSuccess(String result) {
								proses.DismissDialog();
								try {
									JSONObject object = new JSONObject(result);
									String status = object.getJSONObject("metadata").getString("status");
									String message = object.getJSONObject("metadata").getString("message");
									if (status.equals("200")) {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
										Intent intent = new Intent(context, JobDailyCR.class);
										intent.putExtra("tanggalLama", tanggal);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										((Activity) context).startActivity(intent);
										((Activity) context).finish();
									} else {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onError(String result) {
								proses.DismissDialog();
								Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
							}
						});
					}

					private void prepareTambahDataPilihKostumerCR() {
						proses.ShowDialog();
						JSONObject jBody = new JSONObject();
						try {
							jBody.put("date", tanggal);
							jBody.put("customer_id", modelListPilihKostumer.getCustomer_id());
							jBody.put("service_id", modelListPilihKostumer.getService_id());
							jBody.put("time", txtTime.getText().toString());
							if (!isianKeterangan.getText().toString().equals("")) {
								jBody.put("note", isianKeterangan.getText().toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlTambahJadwalCR, "", "", 0, new ApiVolley.VolleyCallback() {
							@Override
							public void onSuccess(String result) {
								proses.DismissDialog();
								try {
									JSONObject object = new JSONObject(result);
									String status = object.getJSONObject("metadata").getString("status");
									String message = object.getJSONObject("metadata").getString("message");
									if (status.equals("200")) {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
										Intent intent = new Intent(context, JobDailyCR.class);
										intent.putExtra("tanggalLama", tanggal);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										((Activity) context).startActivity(intent);
										((Activity) context).finish();
									} else {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onError(String result) {
								proses.DismissDialog();
								Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
							}
						});
					}

					private void prepareTambahDataPilihKostumer() {
						proses.ShowDialog();
						JSONObject jBody = new JSONObject();
						try {
							jBody.put("date", tanggal);
							jBody.put("customer_id", modelListPilihKostumer.getCustomer_id());
							jBody.put("service_id", modelListPilihKostumer.getService_id());
							jBody.put("time", txtTime.getText().toString());
							if (!isianKeterangan.getText().toString().equals("")) {
								jBody.put("note", isianKeterangan.getText().toString());
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlTambahJadwal, "", "", 0, new ApiVolley.VolleyCallback() {
							@Override
							public void onSuccess(String result) {
								proses.DismissDialog();
								try {
									JSONObject object = new JSONObject(result);
									String status = object.getJSONObject("metadata").getString("status");
									String message = object.getJSONObject("metadata").getString("message");
									if (status.equals("200")) {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
										Intent intent = new Intent(context, JobDailyVAS.class);
										intent.putExtra("tanggalLama", tanggal);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										((Activity) context).startActivity(intent);
										((Activity) context).finish();
									} else {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onError(String result) {
								proses.DismissDialog();
								Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
							}
						});
					}

					private void prepareEditDataPilihKostumer() {
						proses.ShowDialog();
						JSONObject jBody = new JSONObject();
						try {
							jBody.put("id", id);
							jBody.put("customer_id", modelListPilihKostumer.getCustomer_id());
							jBody.put("service_id", modelListPilihKostumer.getService_id());
							jBody.put("time", txtTime.getText().toString());
							if (!isianKeterangan.getText().toString().equals("")) {
								jBody.put("note", isianKeterangan.getText().toString());
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
						ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlEditJadwal, "", "", 0, new ApiVolley.VolleyCallback() {
							@Override
							public void onSuccess(String result) {
								proses.DismissDialog();
								try {
									JSONObject object = new JSONObject(result);
									String status = object.getJSONObject("metadata").getString("status");
									String message = object.getJSONObject("metadata").getString("message");
									if (status.equals("200")) {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
										Intent intent = new Intent(context, JobDailyVAS.class);
										intent.putExtra("tanggalLama", tanggal);
										intent.addCategory(Intent.CATEGORY_HOME);
										intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
										((Activity) context).startActivity(intent);
										((Activity) context).finish();
									} else {
										Toast.makeText(context, message, Toast.LENGTH_LONG).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}

							@Override
							public void onError(String result) {
								proses.DismissDialog();
								Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
							}
						});
					}
				});
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
				/*if (PilihKostumer.isEdit) {
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.popup_input_jam_kerja);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					RelativeLayout btnOk = (RelativeLayout) dialog.findViewById(R.service_id.btnOKPopupInputJamKerja);
					btnOk.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
							prepareEditDataPilihKostumer();
							Intent intent = new Intent(context, JobDailyVAS.class);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							((Activity) context).startActivity(intent);
							((Activity) context).finish();
						}
					});
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				} else if (PilihKostumer.isTambah) {
					final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.popup_input_jam_kerja);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					txtTime = (TextView) dialog.findViewById(R.service_id.txtTimePopupInputJamKerja);
					isianKeterangan = (EditText) dialog.findViewById(R.service_id.etKeteranganPopupInputJamKerja);
					ImageView btnTime = (ImageView) dialog.findViewById(R.service_id.btnTimePopupInputJamKerja);
					btnTime.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							java.util.Calendar mcurrentTime = java.util.Calendar.getInstance();
							int hour = mcurrentTime.get(java.util.Calendar.HOUR_OF_DAY);
							int minute = mcurrentTime.get(java.util.Calendar.MINUTE);
							TimePickerDialog mTimePicker;
							mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
								@Override
								public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
									String jam = String.valueOf(selectedHour);
									String menit = String.valueOf(selectedMinute);
									jam = jam.length() < 2 ? "0" + jam : jam;
									menit = menit.length() < 2 ? "0" + menit : menit;
									txtTime.setText(jam + ":" + menit);
//                        textTimePixer.setAlpha(1);
								}
							}, hour, minute, true);//Yes 24 hour time
							mTimePicker.setTitle("Select Time");
							mTimePicker.show();
						}
					});
					RelativeLayout btnOk = (RelativeLayout) dialog.findViewById(R.service_id.btnOKPopupInputJamKerja);
					btnOk.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (txtTime.getText().toString().equals("")) {
//								dialog.dismiss();
								Toast.makeText(context, "Mohon pilih jam dahulu", Toast.LENGTH_LONG).show();
								return;
							} *//*else if (isianKeterangan.getText().toString().equals("")) {
				dialog.show();
								isianKeterangan.setError("Mohon di isi terlebih dahulu");
								isianKeterangan.requestFocus();
								return;
							}*//* else {
								dialog.dismiss();
								prepareTambahDataPilihKostumer();
							}
						}

						private void prepareTambahDataPilihKostumer() {
							proses.ShowDialog();
							JSONObject jBody = new JSONObject();
							try {
								jBody.put("date", tanggal);
								jBody.put("customer_id", modelListPilihKostumer.getCustomer_id());
								jBody.put("service_id", modelListPilihKostumer.getService_id());
								jBody.put("time", txtTime.getText().toString());
								if (!isianKeterangan.getText().toString().equals("")) {
									jBody.put("note", isianKeterangan.getText().toString());
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlTambahJadwal, "", "", 0, new ApiVolley.VolleyCallback() {
								@Override
								public void onSuccess(String result) {
									proses.DismissDialog();
									try {
										JSONObject object = new JSONObject(result);
										String status = object.getJSONObject("metadata").getString("status");
										String message = object.getJSONObject("metadata").getString("message");
										if (status.equals("200")) {
											Toast.makeText(context, message, Toast.LENGTH_LONG).show();
											Intent intent = new Intent(context, JobDailyVAS.class);
											intent.putExtra("tanggalLama",tanggal);
											intent.addCategory(Intent.CATEGORY_HOME);
											intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
											((Activity) context).startActivity(intent);
											((Activity) context).finish();
										} else {
											Toast.makeText(context, message, Toast.LENGTH_LONG).show();
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}
								}

								@Override
								public void onError(String result) {
									proses.DismissDialog();
									Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
								}
							});
						}
					});
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}*/
			}
		});
		return convertView;
	}

	/*private void prepareTambahDataPilihKostumer() {
		proses.ShowDialog();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("date", tanggal);
			jBody.put("customer_id", modelListPilihKostumer.getCustomer_id());
			jBody.put("service_id", modelListPilihKostumer.getService_id());
			jBody.put("time", txtTime.getText().toString());
			if (!isianKeterangan.getText().toString().equals("")) {
				jBody.put("note", isianKeterangan.getText().toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlTambahJadwal, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
						Intent intent = new Intent(context, JobDailyVAS.class);
						intent.putExtra("tanggalLama",tanggal);
						intent.addCategory(Intent.CATEGORY_HOME);
						intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						((Activity) context).startActivity(intent);
						((Activity) context).finish();
					} else {
						Toast.makeText(context, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}*/


}
/*final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.popup_pilih_service_id);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					RelativeLayout btnOke = (RelativeLayout) dialog.findViewById(R.service_id.btnOKPopupPilihServiceID);
					btnOke.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
							Intent intent = new Intent(context, Equipment.class);
							((Activity) context).startActivity(intent);
						}
					});
					RelativeLayout btnSkip = (RelativeLayout) dialog.findViewById(R.service_id.btnSkipPopupPilihServiceID);
					btnSkip.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
						}
					});
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();*/
