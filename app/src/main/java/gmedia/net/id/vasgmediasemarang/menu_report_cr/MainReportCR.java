package gmedia.net.id.vasgmediasemarang.menu_report_cr;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.vasgmediasemarang.MainActivity;
import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.MasterModelPIC;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.AdapterSpinnerPIC;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;

public class MainReportCR extends AppCompatActivity {
	private Context context;
	private TextView txtNamaPerusahaan, txtPaketInternet, txtLayanan;
	private Proses proses;
	private Spinner dropdownPilihPIC, dropdownTaskInfo, dropdownMasaKontrak;
	private ArrayAdapter<ModelDropdownTaskInfo> adapterDropdownTaskInfo;
	private ArrayAdapter<ModelDropdownMasaKontrak> adapterDropdownMasaKontrak;
	private List<ModelDropdownMasaKontrak> listContractUnit;
	private List<ModelDropdownTaskInfo> listTaskInfo;
	private List<MasterModelPIC> masterPIC;
	private ArrayAdapter<MasterModelPIC> adapterPIC;
	private String idDropdownTaksInfo = "", valueDropdownContractUnit = "", idPIC = "";
	private String idList = "", customerID = "", idLayanan = "", search = "";
	private EditText edtResultMarketing, edtResultTekhnis, edtInputMasaKontrak, edtReferensi, edtNextAction, edtSearchLayanan;
	private LinearLayout btnKirim;
	private LinearLayout btnLihatLayanan;
	private ListView lvLayanan;
	private AdapterListLayananReportCR adapterLayananReportCR;
	private ArrayList<ModelLayananReportCR> listLayananReportCR;
	private ArrayList<ModelLayananReportCR> moreListLayananReportCR;
	private boolean isLoading = false;
	private int startIndex = 0;
	private int count = 10;
	private View footerList;
	private ImageView imgPanahLayanan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_report_cr);

		context = this;
		proses = new Proses(context);

		LayoutInflater li = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Report CR");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			idList = bundle.getString("id");
			customerID = bundle.getString("customerID");
		}

		initUI();
		initAction();
	}

	private void initUI() {

		txtNamaPerusahaan = (TextView) findViewById(R.id.txtNamaPerusahaanReportCR);

		dropdownPilihPIC = (Spinner) findViewById(R.id.dropdownPilihPICReportCR);
		dropdownTaskInfo = (Spinner) findViewById(R.id.dropdownPilihTaksInfoReportCR);

		edtResultMarketing = (EditText) findViewById(R.id.edtResultMarketingReportCR);
		edtResultTekhnis = (EditText) findViewById(R.id.edtResultTekhnisReportCR);

		txtPaketInternet = (TextView) findViewById(R.id.txtPaketInternetReportCR);

		edtInputMasaKontrak = (EditText) findViewById(R.id.edtInputMasaKontrakReportCR);
		dropdownMasaKontrak = (Spinner) findViewById(R.id.dropdownPilihMasaKontrakReportCR);

		txtLayanan = (TextView) findViewById(R.id.txtUpgradeLayananReportCR);
		btnLihatLayanan = (LinearLayout) findViewById(R.id.btnLihatLayananReportCR);
		imgPanahLayanan = (ImageView) findViewById(R.id.imgPanahLayanan);

		edtReferensi = (EditText) findViewById(R.id.edtReferensiReportCR);

		edtNextAction = (EditText) findViewById(R.id.edtNextActionReportCR);

		btnKirim = (LinearLayout) findViewById(R.id.btnKirimReportCR);

	}

	private void initAction() {
		prepareDataReportCR();
		btnLihatLayanan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				preparePopupLayananReportCR();
			}
		});
		btnKirim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				prepareSaveDataReportCR();
			}
		});
	}


	private void prepareDataDropdownPIC() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDataDropdownPIC + customerID + "?", "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				masterPIC = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray pic_list = response.getJSONArray("pic_list");
						for (int i = 0; i < pic_list.length(); i++) {
							JSONObject isiPIC = pic_list.getJSONObject(i);
							masterPIC.add(new MasterModelPIC(
									isiPIC.getString("id"),
									isiPIC.getString("nama"),
									isiPIC.getString("email")
							));
						}
						masterPIC.add(new MasterModelPIC(
								"1",
								"Lainnya...",
								""
						));
						masterPIC.add(new MasterModelPIC(
								"0",
								"Silahkan pilih...",
								""
						));
						final int listSize = masterPIC.size() - 1;
						adapterPIC = new AdapterSpinnerPIC(((Activity) context), R.layout.view_spinner_pic, R.id.txtNamaPIC, masterPIC) {
							@Override
							public int getCount() {
								return (listSize);
							}

							@Override
							public View getView(int position, View convertView, ViewGroup parent) {
								View view = super.getView(position, convertView, parent);
								TextView nama = (TextView) view.findViewById(R.id.txtNamaPIC);
								TextView email = (TextView) view.findViewById(R.id.txtEmailPIC);
								email.setVisibility(View.GONE);
								return view;
							}
							/*@Override
							public boolean isEnabled(int position) {
								if (position == 0) {
									// Disable the first item from Spinner
									// First item will be use for hint
									dropdownPIC.setAlpha(0.5f);
									return false;
								} else {
									dropdownPIC.setAlpha(1);
									return true;
								}
							}*/

							@Override
							public View getDropDownView(int position, View convertView, ViewGroup parent) {
								View view = super.getDropDownView(position, convertView, parent);
								TextView nama = (TextView) view.findViewById(R.id.txtNamaPIC);
								TextView email = (TextView) view.findViewById(R.id.txtEmailPIC);
								if (position == masterPIC.size() - 2) {
									nama.setTextColor(Color.parseColor("#16b1eb"));
									nama.setTypeface(Typeface.DEFAULT_BOLD);
									email.setVisibility(View.GONE);
								} else if (position == listSize) {
									nama.setTextColor(Color.parseColor("#000000"));
									nama.setTypeface(Typeface.DEFAULT_BOLD);
									nama.setTextSize(16f);
									email.setVisibility(View.GONE);
								} else {
									nama.setTextColor(Color.parseColor("#000000"));
									nama.setTypeface(Typeface.DEFAULT_BOLD);
									email.setVisibility(View.VISIBLE);
								}
								return view;
							}
						};
						dropdownPilihPIC.setAdapter(adapterPIC);
						if (!idPIC.equals("")) {
							for (int i = 0; i < masterPIC.size(); i++) {
								String id = masterPIC.get(i).getId();
								if (id.equals(idPIC)) {
									dropdownPilihPIC.setSelection(i);
									break;
								}
							}
						} else {
							dropdownPilihPIC.setSelection(listSize);
						}
						dropdownPilihPIC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								if (position == masterPIC.size() - 2) {
//									Toast.makeText(Equipment.this, "iki posisi trakhir coooyy", Toast.LENGTH_SHORT).show();
									dropdownPilihPIC.setSelection(listSize);
									prepareDialogTambahPIC();
								} else {
									MasterModelPIC modelPIC = (MasterModelPIC) dropdownPilihPIC.getSelectedItem();
									idPIC = modelPIC.getId();
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});


						/*adapterPIC = new AdapterSpinnerPIC(Equipment.this, R.layout.view_spinner_pic,R.id.txtNamaPIC, masterPIC) {
							@Override
							public boolean isEnabled(int position) {
								if (position == 0) {
									// Disable the first item from Spinner
									// First item will be use for hint
									dropdownPIC.setAlpha(0.5f);
									return false;
								} else {
									dropdownPIC.setAlpha(1);
									return true;
								}
							}

							@Override
							public View getDropDownView(int position, View convertView,
														ViewGroup parent) {

								View view = super.getDropDownView(position, convertView, parent);
								TextView tv = (TextView) view;
								if (position == 0) {
									// Set the hint text color gray
									tv.setTextColor(Color.BLACK);
								} else {
									tv.setTextColor(Color.BLACK);
								}
								return view;
							}
						};*/
						prepareDataDropdownTaskInfo();
					} else if (status.equals("404")) {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
						masterPIC.add(new MasterModelPIC(
								"1",
								"tambah PIC",
								""
						));
						masterPIC.add(new MasterModelPIC(
								"0",
								"Silahkan tambah PIC...",
								""
						));
						final int listSize = masterPIC.size() - 1;
						adapterPIC = new AdapterSpinnerPIC((Activity) context, R.layout.view_spinner_pic, R.id.txtNamaPIC, masterPIC) {
							@Override
							public int getCount() {
								return (listSize);
							}

							@Override
							public View getView(int position, View convertView, ViewGroup parent) {
								View view = super.getView(position, convertView, parent);
								TextView nama = (TextView) view.findViewById(R.id.txtNamaPIC);
								TextView email = (TextView) view.findViewById(R.id.txtEmailPIC);
								email.setVisibility(View.GONE);
								return view;
							}
							/*@Override
							public boolean isEnabled(int position) {
								if (position == 0) {
									// Disable the first item from Spinner
									// First item will be use for hint
									dropdownPIC.setAlpha(0.5f);
									return false;
								} else {
									dropdownPIC.setAlpha(1);
									return true;
								}
							}*/

							@Override
							public View getDropDownView(int position, View convertView, ViewGroup parent) {
								View view = super.getDropDownView(position, convertView, parent);
								TextView nama = (TextView) view.findViewById(R.id.txtNamaPIC);
								TextView email = (TextView) view.findViewById(R.id.txtEmailPIC);
								if (position == masterPIC.size() - 2) {
									nama.setTextColor(Color.parseColor("#16b1eb"));
									nama.setTypeface(Typeface.DEFAULT_BOLD);
									email.setVisibility(View.GONE);
								} else if (position == listSize) {
									nama.setTextColor(Color.parseColor("#000000"));
									nama.setTypeface(Typeface.DEFAULT_BOLD);
									nama.setTextSize(16f);
									email.setVisibility(View.GONE);
								} else {
									nama.setTextColor(Color.parseColor("#000000"));
									nama.setTypeface(Typeface.DEFAULT_BOLD);
									email.setVisibility(View.VISIBLE);
								}
								return view;
							}
						};
						dropdownPilihPIC.setAdapter(adapterPIC);
						if (!idPIC.equals("")) {
							for (int i = 0; i < masterPIC.size(); i++) {
								String id = masterPIC.get(i).getId();
								if (id.equals(idPIC)) {
									dropdownPilihPIC.setSelection(i);
									break;
								}
							}
						} else {
							dropdownPilihPIC.setSelection(listSize);
						}
						dropdownPilihPIC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								if (position == masterPIC.size() - 2) {
//									Toast.makeText(Equipment.this, "iki posisi trakhir coooyy", Toast.LENGTH_SHORT).show();
									dropdownPilihPIC.setSelection(listSize);
									prepareDialogTambahPIC();
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});
						prepareDataDropdownTaskInfo();
					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

	private void prepareDialogTambahPIC() {
		final EditText namaTambahPIC, jabatanTambahPIC, emailTambahPIC, handphoneTambahPIC;
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.popup_tambah_pic);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		namaTambahPIC = (EditText) dialog.findViewById(R.id.namaPIC);
		jabatanTambahPIC = (EditText) dialog.findViewById(R.id.jabatanPIC);
		emailTambahPIC = (EditText) dialog.findViewById(R.id.emailPIC);
		handphoneTambahPIC = (EditText) dialog.findViewById(R.id.handphonePIC);
		RelativeLayout btnOK = (RelativeLayout) dialog.findViewById(R.id.tombolOKTambahPIC);
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (namaTambahPIC.getText().toString().equals("")) {
					Toast.makeText(context, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (jabatanTambahPIC.getText().toString().equals("")) {
					Toast.makeText(context, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (emailTambahPIC.getText().toString().equals("")) {
					Toast.makeText(context, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (handphoneTambahPIC.getText().toString().equals("")) {
					Toast.makeText(context, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else {
					prepareDataTambahPIC();
				}
			}

			private void prepareDataTambahPIC() {
				proses.ShowDialog();
				JSONObject jBody = new JSONObject();
				try {
					jBody.put("id_header", idList);
					jBody.put("nama", namaTambahPIC.getText().toString());
					jBody.put("jabatan", jabatanTambahPIC.getText().toString());
					jBody.put("email", emailTambahPIC.getText().toString());
					jBody.put("phone", handphoneTambahPIC.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlTambahDataPIC, "", "", 0, new ApiVolley.VolleyCallback() {
					@Override
					public void onSuccess(String result) {
						proses.DismissDialog();
						dialog.dismiss();
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getJSONObject("metadata").getString("status");
							String message = object.getJSONObject("metadata").getString("message");
							if (status.equals("200")) {
								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
								prepareDataDropdownPIC();
							} else {
								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(String result) {
						proses.DismissDialog();
						dialog.dismiss();
						Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
		RelativeLayout btnCancel = (RelativeLayout) dialog.findViewById(R.id.tombolCancelTambahPIC);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void prepareDataDropdownTaskInfo() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDataDropdownTaskInfoCR, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				listTaskInfo = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray task_info = object.getJSONObject("response").getJSONArray("task_info");
						for (int i = 0; i < task_info.length(); i++) {
							JSONObject isi = task_info.getJSONObject(i);
							listTaskInfo.add(new ModelDropdownTaskInfo(
									isi.getString("value"),
									isi.getString("text")
							));
						}
						listTaskInfo.add(new ModelDropdownTaskInfo(
								"0",
								"Silahkan pilih..."
						));
						final int listSize = listTaskInfo.size() - 1;
						adapterDropdownTaskInfo = new AdapterSpinnerTaskInfo(((Activity) context), R.layout.view_spinner_task_info, R.id.txtNamaTaskInfo, listTaskInfo) {
							@Override
							public int getCount() {
								return listSize;
							}
						};
						dropdownTaskInfo.setAdapter(adapterDropdownTaskInfo);
						if (!idDropdownTaksInfo.equals("")) {
							for (int i = 0; i < listTaskInfo.size(); i++) {
								String id = listTaskInfo.get(i).getId();
								if (id.equals(idDropdownTaksInfo)) {
									dropdownTaskInfo.setSelection(i);
									break;
								}
							}
						} else {
							dropdownTaskInfo.setSelection(listSize);
						}
						dropdownTaskInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								ModelDropdownTaskInfo model = listTaskInfo.get(position);
								idDropdownTaksInfo = model.getId();
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});
						prepareDataDropdownMasaKontrak();
					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT);
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

	private void prepareDataDropdownMasaKontrak() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDataDropdownContractUnitCR, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				listContractUnit = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray unit = object.getJSONObject("response").getJSONArray("unit");
						for (int i = 0; i < unit.length(); i++) {
							JSONObject isi = unit.getJSONObject(i);
							listContractUnit.add(new ModelDropdownMasaKontrak(
									isi.getString("value"),
									isi.getString("text")
							));
						}
						listContractUnit.add(new ModelDropdownMasaKontrak(
								"0",
								"Pilih..."
						));
						final int listSize = listContractUnit.size() - 1;
						adapterDropdownMasaKontrak = new AdapterSpinnerMasaKontrak(((Activity) context), R.layout.view_spinner_masa_kontrak, R.id.txtNamaMasaKontrak, listContractUnit) {
							@Override
							public int getCount() {
								return listSize;
							}
						};
						dropdownMasaKontrak.setAdapter(adapterDropdownMasaKontrak);
						if (!valueDropdownContractUnit.equals("")) {
							for (int i = 0; i < listContractUnit.size(); i++) {
								String value = listContractUnit.get(i).getValue();
								if (value.equals(valueDropdownContractUnit)) {
									dropdownMasaKontrak.setSelection(i);
									break;
								}
							}
						} else {
							dropdownMasaKontrak.setSelection(listSize);
						}
						dropdownMasaKontrak.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								ModelDropdownMasaKontrak model = listContractUnit.get(position);
								valueDropdownContractUnit = model.getValue();
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});
					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT);
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

	private void preparePopupLayananReportCR() {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.popup_layanan_report_cr);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
		dialog.setCanceledOnTouchOutside(false);
//		prepareDataLvLayanan();
		edtSearchLayanan = (EditText) dialog.findViewById(R.id.edtSearchLayanan);
		edtSearchLayanan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				return false;
			}
		});
		edtSearchLayanan.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				search = "";
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (editable.toString().equals("")){
					prepareDataLvLayanan();
				}
				search = editable.toString();
				/*final Handler handler = new Handler();
				final Runnable runnable = new Runnable() {
					@Override
					public void run() {
						if (!isLoading) {
							prepareDataLvLayanan();
						}
					}
				};
				handler.postDelayed(runnable, 1000);*/

			}
		});
		edtSearchLayanan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
				if (actionId == EditorInfo.IME_ACTION_GO) {
					//Perform your Actions here.
					prepareDataLvLayanan();
				}
				return false;
			}
		});
		lvLayanan = (ListView) dialog.findViewById(R.id.lv_layanan_report_cr);
		lvLayanan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				ModelLayananReportCR model = listLayananReportCR.get(position);
				idLayanan = model.getId();
				txtLayanan.setText(model.getValue());
				txtLayanan.setTextColor(Color.parseColor("#16b1eb"));
				dialog.dismiss();
			}
		});
		prepareDataLvLayanan();
		dialog.show();
	}

	private void prepareDataLvLayanan() {
		isLoading = true;
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDataLayananReportCR + "start=" + String.valueOf(startIndex) + "&limit=" + String.valueOf(count) + "&search=" + search, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				isLoading = false;
				proses.DismissDialog();
				listLayananReportCR = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray layanan = object.getJSONObject("response").getJSONArray("layanan");
						for (int i = 0; i < layanan.length(); i++) {
							JSONObject isi = layanan.getJSONObject(i);
							listLayananReportCR.add(new ModelLayananReportCR(
									isi.getString("value"),
									isi.getString("text")
							));
						}
						lvLayanan.setAdapter(null);
						adapterLayananReportCR = new AdapterListLayananReportCR(context, listLayananReportCR);
						lvLayanan.setAdapter(adapterLayananReportCR);
						lvLayanan.setOnScrollListener(new AbsListView.OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView view, int i) {

							}

							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
								if (view.getLastVisiblePosition() == totalItemCount - 1 && lvLayanan.getCount() > (count - 1) && !isLoading) {
									isLoading = true;
									lvLayanan.addFooterView(footerList);
									startIndex += count;
//                                        startIndex = 0;
									getMoreDataLayanan();
									//Log.i(TAG, "onScroll: last ");
								}
							}
						});
					} else if (status.equals("404")) {
						listLayananReportCR.clear();
						adapterLayananReportCR.notifyDataSetChanged();
						lvLayanan.setAdapter(null);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				isLoading = false;
				proses.DismissDialog();
				Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getMoreDataLayanan() {
		isLoading = true;
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDataLayananReportCR + "start=" + String.valueOf(startIndex) + "&limit=" + String.valueOf(count) + "&search=" + search, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				moreListLayananReportCR = new ArrayList<>();
				lvLayanan.removeFooterView(footerList);
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONArray layanan = object.getJSONObject("response").getJSONArray("layanan");
						for (int i = 0; i < layanan.length(); i++) {
							JSONObject isi = layanan.getJSONObject(i);
							moreListLayananReportCR.add(new ModelLayananReportCR(
									isi.getString("value"),
									isi.getString("text")
							));
						}
						isLoading = false;
						lvLayanan.removeFooterView(footerList);
						if (adapterLayananReportCR != null)
							adapterLayananReportCR.addMoreData(moreListLayananReportCR);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				isLoading = false;
				proses.DismissDialog();
				Toast.makeText(context, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void prepareDataReportCR() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(context, new JSONObject(), "GET", LinkURL.UrlDataDraftReportCR + idList, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject draft_report = object.getJSONObject("response").getJSONObject("draft_report");

						idList = draft_report.getString("id");
						txtNamaPerusahaan.setText(draft_report.getString("nama_perusahaan"));
						idPIC = draft_report.getString("contact_id");
						idDropdownTaksInfo = draft_report.getString("taskinfo_id");
						edtResultMarketing.setText(draft_report.getString("result_marketing"));
						edtResultTekhnis.setText(draft_report.getString("result_teknis"));
						txtPaketInternet.setText(draft_report.getString("nama_layanan"));
						edtInputMasaKontrak.setText(draft_report.getString("contract_nominal"));
						valueDropdownContractUnit = draft_report.getString("contract_unit");
						String textLayanan = draft_report.getString("upgrade_service_name");
						if (textLayanan.equals("")) {
							txtLayanan.setText("Lihat layanan");
						} else {
							txtLayanan.setText(textLayanan);
						}
						edtReferensi.setText(draft_report.getString("referensi"));
						edtNextAction.setText(draft_report.getString("next_action"));

						prepareDataDropdownPIC();

					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

	private void prepareSaveDataReportCR() {
		proses.ShowDialog();
		JSONObject jBody = new JSONObject();
		try {
			jBody.put("id", idList);
			jBody.put("contact_id", idPIC);
			jBody.put("taskinfo_id", idDropdownTaksInfo);
			jBody.put("result_marketing", edtResultMarketing.getText().toString());
			jBody.put("result_teknis", edtResultTekhnis.getText().toString());
			jBody.put("upgrade_service_id", idLayanan);
			jBody.put("referensi", edtReferensi.getText().toString());
			jBody.put("contract_nominal", edtInputMasaKontrak.getText().toString());
			jBody.put("contract_unit", valueDropdownContractUnit);
			jBody.put("next_action", edtNextAction.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(context, jBody, "POST", LinkURL.UrlSaveDataReportCR, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		MainActivity.posisi = true;
	}
}
