package gmedia.net.id.vasgmediasemarang.menu_job_daily_cr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer.PilihKostumer;
import gmedia.net.id.vasgmediasemarang.menu_report_cr.MainReportCR;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.ConvertDate;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;

public class ListAdapterJobDailyCR extends ArrayAdapter {
	private Context context;
	private Proses proses;
	private List<ModelListJobDailyCR> list;
	private String statusPublish = "", isReport = "";


	public ListAdapterJobDailyCR(Context context, List<ModelListJobDailyCR> list, String statusPublish) {
		super(context, R.layout.view_lv_job_daily_cr, list);
		this.context = context;
		this.list = list;
		this.statusPublish = statusPublish;
		proses = new Proses(context);
	}

	private static class ViewHolder {
		private TextView tgl, nama, alamat, jam, keterangan;
		private LinearLayout btnEdit, layoutUtama, layoutEdit, layoutDone;
		private ImageView imgEdit, imgDone;
		private RelativeLayout flagPublish;
	}

	public void addMoreData(List<ModelListJobDailyCR> moreData) {
		list.addAll(moreData);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
//            LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            /*LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);*/
			convertView = inflater.inflate(R.layout.view_lv_job_daily_cr, null);
			holder.tgl = (TextView) convertView.findViewById(R.id.tglLvJobDailyCR);
			holder.nama = (TextView) convertView.findViewById(R.id.namaLvJobDailyCR);
			holder.alamat = (TextView) convertView.findViewById(R.id.alamatLvJobDailyCR);
			holder.jam = (TextView) convertView.findViewById(R.id.jamLvJobDailyCR);
			holder.keterangan = (TextView) convertView.findViewById(R.id.keteranganLvJobDailyCR);
			holder.layoutEdit = (LinearLayout) convertView.findViewById(R.id.layoutEditListJobDailyCR);
			holder.layoutDone = (LinearLayout) convertView.findViewById(R.id.layoutDoneListJobDailyCR);
			holder.btnEdit = (LinearLayout) convertView.findViewById(R.id.btnEditJobDailyCR);
			holder.layoutUtama = (LinearLayout) convertView.findViewById(R.id.layoutUtamaLvJobDailyCR);
			holder.imgEdit = (ImageView) convertView.findViewById(R.id.imgEditListJobDailyCR);
			holder.imgDone = (ImageView) convertView.findViewById(R.id.imgDoneListJobDailyCR);
			holder.flagPublish = (RelativeLayout) convertView.findViewById(R.id.flagBunderListJobDailyCR);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final ModelListJobDailyCR model = list.get(position);
		isReport = model.getIsReport();
		holder.tgl.setText(ConvertDate.convert("yyyy-MM-dd", "dd MMMM yyyy", model.getTgl()));
		holder.nama.setText(model.getNama());
		holder.alamat.setText(model.getAlamat());
		holder.jam.setText(model.getJam());
		holder.keterangan.setText(model.getKeterangan());
		if (statusPublish.equals("0")) {
//			holder.btnEdit.setVisibility(View.VISIBLE);
			holder.layoutEdit.setVisibility(View.VISIBLE);
//			holder.layoutDone.setVisibility(View.GONE);
//			holder.imgEdit.setBackgroundResource(R.drawable.icon_edit);
//			holder.imgEdit.setVisibility(View.VISIBLE);
			holder.layoutUtama.setClickable(false);
			holder.layoutUtama.setFocusable(false);
			holder.flagPublish.setBackground(ContextCompat.getDrawable(context, R.drawable.bcg_bunder_abu2));
			final ViewHolder finalHolder = holder;
			holder.btnEdit.setOnClickListener(new View.OnClickListener() {
				@RequiresApi(api = Build.VERSION_CODES.KITKAT)
				@Override
				public void onClick(View view) {
					PopupMenu popupMenu = new PopupMenu(context, finalHolder.btnEdit, Gravity.START);
					popupMenu.getMenuInflater()
							.inflate(R.menu.menu_pilihan_job_daily_vas, popupMenu.getMenu());
					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem menuItem) {
							if (menuItem.getItemId() == R.id.btn_edit) {
								Intent intent = new Intent(context, PilihKostumer.class);
								intent.putExtra("home", "editCR");
								intent.putExtra("ID", model.getId());
								intent.putExtra("tanggal", JobDailyCR.tanggal);
								((Activity) context).startActivity(intent);
							} else if (menuItem.getItemId() == R.id.btn_hapus) {
								proses.ShowDialog();
								JSONObject jBody = new JSONObject();
								try {
									jBody.put("id", model.getId());
								} catch (JSONException e) {
									e.printStackTrace();
								}
								ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlDeleteJadwalCR, "", "", 0, new ApiVolley.VolleyCallback() {
									@Override
									public void onSuccess(String result) {
										proses.DismissDialog();
										try {
											JSONObject object = new JSONObject(result);
											String status = object.getJSONObject("metadata").getString("status");
											String message = object.getJSONObject("metadata").getString("message");
											if (status.equals("200")) {
												Toast.makeText(context, message, Toast.LENGTH_LONG).show();
												remove(getItem(position));
												notifyDataSetChanged();
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
							return false;
						}
					});
					popupMenu.show();

				}
			});
		} else if (statusPublish.equals("1")) {
//			holder.btnEdit.setVisibility(View.GONE);
			holder.layoutEdit.setVisibility(View.VISIBLE);
//			holder.layoutDone.setVisibility(View.GONE);
//			holder.imgEdit.setVisibility(View.VISIBLE);
			holder.layoutUtama.setClickable(true);
			holder.layoutUtama.setFocusable(true);
			holder.flagPublish.setBackground(ContextCompat.getDrawable(context, R.drawable.bcg_bunder_biru));
			final ViewHolder finalHolder1 = holder;
			if (isReport.equals("1")) {
				holder.layoutEdit.setVisibility(View.GONE);
				holder.layoutDone.setVisibility(View.VISIBLE);
			}
			holder.layoutUtama.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(context, MainReportCR.class);
					intent.putExtra("id", model.getId());
					intent.putExtra("customerID", list.get(position).getCustomerID());
					intent.putExtra("isReport", isReport);
					((Activity) context).startActivity(intent);
					/*if (isReport.equals("0")) {
						Intent intent = new Intent(context, Equipment.class);
						intent.putExtra("id", model.getId());
						((Activity) context).startActivity(intent);
					} else if (isReport.equals("1")) {
//						Toast.makeText(context, "sudah kirim report", Toast.LENGTH_SHORT).show();

					}*/

					/*final Dialog dialog = new Dialog(context);
					dialog.setContentView(R.layout.popup_pilih_service_id);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					RelativeLayout btnOke = (RelativeLayout) dialog.findViewById(R.id.btnOKPopupPilihServiceID);
					btnOke.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
							Intent intent = new Intent(context, Equipment.class);
							intent.putExtra("id",model.getId());
							((Activity) context).startActivity(intent);
						}
					});
					RelativeLayout btnSkip = (RelativeLayout) dialog.findViewById(R.id.btnSkipPopupPilihServiceID);
					btnSkip.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							dialog.dismiss();
						}
					});
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();*/
				}
			});
			final ViewHolder finalHolder2 = holder;
			holder.btnEdit.setOnClickListener(new View.OnClickListener() {
				@RequiresApi(api = Build.VERSION_CODES.KITKAT)
				@Override
				public void onClick(View view) {
					PopupMenu popupMenu = new PopupMenu(context, finalHolder2.btnEdit, Gravity.START);
					popupMenu.getMenuInflater()
							.inflate(R.menu.menu_pilihan_job_daily_vas, popupMenu.getMenu());
					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem menuItem) {
							if (menuItem.getItemId() == R.id.btn_edit) {
								Intent intent = new Intent(context, PilihKostumer.class);
								intent.putExtra("home", "editCR");
								intent.putExtra("ID", model.getId());
								intent.putExtra("tanggal", JobDailyCR.tanggal);
								((Activity) context).startActivity(intent);
							} else if (menuItem.getItemId() == R.id.btn_hapus) {
								proses.ShowDialog();
								JSONObject jBody = new JSONObject();
								try {
									jBody.put("id", model.getId());
								} catch (JSONException e) {
									e.printStackTrace();
								}
								ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlDeleteJadwalCR, "", "", 0, new ApiVolley.VolleyCallback() {
									@Override
									public void onSuccess(String result) {
										proses.DismissDialog();
										try {
											JSONObject object = new JSONObject(result);
											String status = object.getJSONObject("metadata").getString("status");
											String message = object.getJSONObject("metadata").getString("message");
											if (status.equals("200")) {
												Toast.makeText(context, message, Toast.LENGTH_LONG).show();
												remove(getItem(position));
												notifyDataSetChanged();
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
							return false;
						}
					});
					popupMenu.show();

				}
			});
		}
		return convertView;
	}
}
