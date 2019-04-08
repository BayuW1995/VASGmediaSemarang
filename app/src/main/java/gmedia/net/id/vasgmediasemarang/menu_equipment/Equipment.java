package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.AdapterSpinnerPIC;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;
import gmedia.net.id.vasgmediasemarang.menu_job_daily_vas.JobDailyVAS;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;
import gmedia.net.id.vasgmediasemarang.utils.VolleyMultipartRequest;
import gmedia.net.id.vasgmediasemarang.utils.WrapContentListView;
import gmedia.net.id.vasgmediasemarang.utils.recyclerViewLoadMoreScrollListener;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.Signature;

public class Equipment extends AppCompatActivity {
	private RecyclerView recView;
	private ListView listView;
	private ArrayList<ModelListEquipment> list;
	private ArrayList<ModelListEquipment> moreList;
	private ArrayList<ModelListEquipmentChild> listChild;
	private ListAdapterEquipment adapter;
	private RecyclerViewAdapterEquipmentParent adapterRVEquipment;
	private RecyclerView.LayoutManager layoutManager;
	private String jenis[] =
			{
					"I. Floor, Rack & Environmen",
					"II. Lighting",
					"III. Air Conditioner",
					"IV. Grounding",
					"V. Rack Fan",
					"VI. Power Supply"
			};
	private String jenis_child[] =
			{
					"I. Floor, Rack & Environmen",
					"II. Lighting",
					"III. Air Conditioner",
					"IV. Grounding",
					"V. Rack Fan",
					"VI. Power Supply"
			};
	private RelativeLayout btnCustSignature, btnCustImage;
	private int PICK_IMAGE_REQUEST = 1;
	private Proses proses;
	private int start = 0, count = 10;
	private boolean isLoading = false;
	private View footerList;
	private recyclerViewLoadMoreScrollListener load;
	private LinearLayout btnKirim;
	public static String id_header = "";
	private RadioGroup btnRadio;
	private int selectedId = -1;
	private RadioButton radioButton;
	private int booleanLayak = -1;
	private SessionManager session;
	private RequestQueue requestQueue;
	private String idUploadGambarSelfie = "", idList = "", customerID = "";
	private Spinner dropdownPIC;
	private List<MasterModelPIC> masterPIC;
	private ArrayAdapter<MasterModelPIC> adapterPIC;
	private String idPIC = "", serviceID = "", isReport = "";
	private EditText isian;
	private ImageView imgSelfie, imgTTD;
	public static Bitmap bitmapTTD;
	public static Boolean signatureIsDone = false;
	private TextView txtPIC, txtNamaSite;
	private RadioButton radioButtonLayak, radioButtonBlmLayak;
	private String status_layanan = "", note = "", pic_selfie = "", pic_ttd = "", selected_pic_by_id = "";
	private boolean jBodyIsNull = true;
	private JSONObject jBody;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Equipment Room/Rack");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		proses = new Proses(Equipment.this);
		session = new SessionManager(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			idList = bundle.getString("id");
			customerID = bundle.getString("customerID");
		}
		initUI();
		initAction();
	}

	private void initUI() {
		txtNamaSite = (TextView) findViewById(R.id.txtNamaSite);
		listView = (ListView) findViewById(R.id.lv_equipment);
		/*layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		recView = (RecyclerView) findViewById(R.id.lv_equipment);
		recView.setLayoutManager(layoutManager);*/
		dropdownPIC = (Spinner) findViewById(R.id.dropdownPilihPICEquipment);
		btnCustSignature = (RelativeLayout) findViewById(R.id.btnCustSignature);
		btnCustImage = (RelativeLayout) findViewById(R.id.btnCustImage);
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);
		btnKirim = (LinearLayout) findViewById(R.id.btnKirimReportEquipment);
		isian = (EditText) findViewById(R.id.etEquipment);
		btnRadio = (RadioGroup) findViewById(R.id.radioGrupLayak);
		imgSelfie = (ImageView) findViewById(R.id.imgSelfie);
		imgTTD = (ImageView) findViewById(R.id.imgTTD);
		txtPIC = (TextView) findViewById(R.id.txtPIC);
		radioButtonLayak = (RadioButton) findViewById(R.id.radioButtonLayak);
		radioButtonBlmLayak = (RadioButton) findViewById(R.id.radioButtonTdkLayak);

	}

	private void initAction() {
		prepareDataEquipment();
//		list = prepareDataEquipment();
		btnCustSignature.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				signatureIsDone = false;
				Intent intent = new Intent(Equipment.this, Signature.class);
				startActivity(intent);
			}
		});
		btnCustImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, PICK_IMAGE_REQUEST);
				/*Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
			}
		});
		btnRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				selectedId = btnRadio.getCheckedRadioButtonId();
				radioButton = (RadioButton) findViewById(selectedId);
				String isiboolean = radioButton.getText().toString();
				if (isiboolean.equals("Layak")) {
					booleanLayak = 0;
				} else {
					booleanLayak = 1;
				}
			}
		});
		btnKirim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (isReport.equals("0")) {
					jBody = new JSONObject();
					try {
						jBody.put("id_header", id_header);
						jBody.put("id_gambar_ttd", Signature.idUploadGambarTTD);
						jBody.put("id_gambar_selfi", idUploadGambarSelfie);
						jBody.put("status_layanan", String.valueOf(booleanLayak));//radio button siap blm siap
//			jBody.put("id_gambar_selfi", "1");
//			jBody.put("id_gambar_ttd", "1");
//			jBody.put("status_layanan", "1");
						if (isian.getText().toString().equals("")) {
							jBody.put("note", "");
						} else {
							jBody.put("note", isian.getText().toString());
						}
						MasterModelPIC modelPIC = (MasterModelPIC) dropdownPIC.getSelectedItem();
						idPIC = modelPIC.getId();
						jBody.put("id_pic", idPIC);
						ArrayList<JSONObject> jsonArray = new ArrayList<>();
						for (int i = 0; i < list.size(); i++) {//7
							ModelListEquipment modelParent = list.get(i);
							for (int j = 0; j < modelParent.getList().size(); j++) {
								ModelListEquipmentChild modelChild = modelParent.getList().get(j);
								String tipe = modelChild.getTipe();
								JSONObject object = new JSONObject();
								if (tipe.equals("radio")) {
									if (modelChild.getRadio1().equals("")) {
										Toast.makeText(Equipment.this, "Mohon di isi semua pilihan terlebih dahulu", Toast.LENGTH_SHORT).show();
										return;
									} else {
										object.put("id", modelChild.getId());
										object.put("value_radio", modelChild.getRadio1());
										object.put("note", modelChild.getIsian());
										jsonArray.add(object);
									}
								} else if (tipe.equals("text")) {
									if (modelChild.getNoteTeks1().equals("")) {
										Toast.makeText(Equipment.this, "Mohon di isi semua isian terlebih dahulu", Toast.LENGTH_SHORT).show();
										return;
									} else {
										object.put("id", modelChild.getId());
										object.put("value_text", modelChild.getNoteTeks1());
										object.put("note", modelChild.getIsian());
										jsonArray.add(object);
									}
								}
							}
						}
						jBodyIsNull = false;
						jBody.put("form_items", new JSONArray(jsonArray));
						if (!jBodyIsNull) {
							if (booleanLayak == -1) {
								Toast.makeText(Equipment.this, "Silahkan Isi Kelayakan Layanan Terlebih Dahulu", Toast.LENGTH_SHORT).show();
							} else if (idPIC.equals("0")) {
								Toast.makeText(Equipment.this, "Silahkan pilih PIC terlebih dahulu", Toast.LENGTH_SHORT).show();
							} else if (idUploadGambarSelfie.equals("")) {
								Toast.makeText(Equipment.this, "Silahkan upload selfie terlebih dahulu", Toast.LENGTH_SHORT).show();
							} else if (Signature.idUploadGambarTTD.equals("")) {
								Toast.makeText(Equipment.this, "Silahkan upload tanda tangan terlebih dahulu", Toast.LENGTH_SHORT).show();
							} else {
								prepareDataReportEquipment();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				/*MasterModelPIC modelPIC = (MasterModelPIC) dropdownPIC.getSelectedItem();
				idPIC = modelPIC.getId();
				if (booleanLayak == -1) {
					Toast.makeText(Equipment.this, "Silahkan Isi Kelayakan Layanan Terlebih Dahulu", Toast.LENGTH_SHORT).show();
				} else if (idPIC.equals("0")) {
					Toast.makeText(Equipment.this, "Silahkan pilih PIC terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (idUploadGambarSelfie.equals("")) {
					Toast.makeText(Equipment.this, "Silahkan upload selfie terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (Signature.idUploadGambarTTD.equals("")) {
					Toast.makeText(Equipment.this, "Silahkan upload tanda tangan terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (isReport.equals("1")) {
					Toast.makeText(Equipment.this, "Anda sudah pernah mengirim report ini", Toast.LENGTH_SHORT).show();
				} else {
					prepareDataReportEquipment();
				}*/
				/*for (int i = 0; i < list.size(); i++) {//7
					ModelListEquipment modelParent = list.get(i);
					for (int j = 0; j < modelParent.getList().size(); j++) {
						ModelListEquipmentChild modelChild = modelParent.getList().get(j);
//						Log.d("listEquipment", String.valueOf(i) + " - " + modelChild.getRadio1() + " - " + modelChild.getIsian());

						Log.d("listEquipment", "posisi parent " + i + " " + modelParent.getRomawi() + " "
								+ modelParent.getJudul() + " posisi child " + j + " " + modelChild.getRadio1() + " - "
								+ modelChild.getIsian());
					}
				}*/
				/*listChild = list.get(0).getList();
				for (int i = 0; i < listChild.size(); i++) {
					ModelListEquipmentChild modelChild = listChild.get(i);
					Log.d("listEquipment", modelChild.getRadio1() + " - " + modelChild.getIsian());
				}*/
					//coro cepet
				/*for (ModelListEquipment modelParent : list) {//int o=0;o<list.size;o++
					for (ModelListEquipmentChild modelChild : modelParent.getList()) {
						Log.d("list", modelChild.getRadio1() + " " + modelChild.getIsian());
					}
				}*/
				} else if (isReport.equals("1")) {
					Toast.makeText(Equipment.this, "Anda sudah pernah mengirim report ini", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (signatureIsDone) {
			signatureIsDone = false;
			btnCustSignature.setBackgroundColor(Color.parseColor("#FFFFFF"));
			imgTTD.setImageDrawable(new BitmapDrawable(getResources(), bitmapTTD));
			imgTTD.setVisibility(View.VISIBLE);
			int sizeInDp = 38;
			float scale = getResources().getDisplayMetrics().density;
			int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
//			btnCustSignature.setPadding(0, dpAsPixels, 0, dpAsPixels);
		}
	}

	private void prepareDataEquipment() {
		proses.ShowDialog();
		isLoading = true;
		ApiVolley request = new ApiVolley(Equipment.this, new JSONObject(), "GET", LinkURL.UrlReportJobDailyVAS + idList + "?" + "start=" + String.valueOf(start) + "&limit=" + String.valueOf(count), "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				isLoading = false;
				list = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONObject header = response.getJSONObject("header");
						txtNamaSite.setText(header.getString("nama_site"));
						id_header = header.getString("id");
						isReport = header.getString("is_report");
						status_layanan = header.getString("status_layanan");
						note = header.getString("note");
						pic_selfie = header.getString("pic_selfi_image");
						pic_ttd = header.getString("pic_ttd_image");
						selected_pic_by_id = header.getString("pic_id");
						JSONArray formsParent = response.getJSONArray("forms");
						for (int i = 0; i < formsParent.length(); i++) {
							listChild = new ArrayList<>();
							JSONObject isiFormsParent = formsParent.getJSONObject(i);
							JSONArray formsChild = isiFormsParent.getJSONArray("items");
							for (int j = 0; j < formsChild.length(); j++) {
								JSONObject isiFormsChild = formsChild.getJSONObject(j);
								listChild.add(new ModelListEquipmentChild(
										isiFormsChild.getString("id"),
										isiFormsChild.getString("nama_item"),
										isiFormsChild.getString("standard"),
										isiFormsChild.getString("tipe"),
										isiFormsChild.getString("value_radiobutton"),
										isiFormsChild.getString("value_text"),
										isiFormsChild.getString("note")
								));
							}
							list.add(new ModelListEquipment(
									isiFormsParent.getString("nomor_romawi"),
									isiFormsParent.getString("nama_kategori"),
									listChild
							));
						}
						/*recView.setAdapter(null);
						adapterRVEquipment = new RecyclerViewAdapterEquipmentParent(Equipment.this, list);
						recView.setAdapter(adapterRVEquipment);*/
						listView.setAdapter(null);
						adapter = new ListAdapterEquipment(Equipment.this, list);
						listView.setAdapter(adapter);
						WrapContentListView.setListViewHeightBasedOnChildren(listView);
						final Handler handler = new Handler();
						final Runnable runnable = new Runnable() {
							@Override
							public void run() {
								if (!isLoading) {
									prepareDataDropdownPIC();
								}
							}
						};
						handler.postDelayed(runnable, 300);
						if (status_layanan.equals("OK")) {
							radioButtonLayak.setChecked(true);
						} else if (status_layanan.equals("NOK")) {
							radioButtonBlmLayak.setChecked(true);
						}
						if (!note.equals("")) {
							isian.setText(note);
						}
						if (!pic_selfie.equals("")) {
							Picasso.with(Equipment.this).load(pic_selfie).into(imgSelfie);
							imgSelfie.setVisibility(View.VISIBLE);
							int sizeInDp = 38;
							float scale = getResources().getDisplayMetrics().density;
							int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
							btnCustImage.setPadding(0, dpAsPixels, 0, dpAsPixels);
						}
						if (!pic_ttd.equals("")) {
							btnCustSignature.setBackgroundColor(Color.parseColor("#FFFFFF"));
							Picasso.with(Equipment.this).load(pic_ttd).resize(128, 128).into(imgTTD);
							imgTTD.setVisibility(View.VISIBLE);
							int sizeInDp = 38;
							float scale = getResources().getDisplayMetrics().density;
							int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
							btnCustSignature.setPadding(0, dpAsPixels, 0, dpAsPixels);
						}
						/*load = new recyclerViewLoadMoreScrollListener(new recyclerViewLoadMoreScrollListener.LoadListener() {
							@Override
							public void onLoad() {
								start += count;
								getMoreData();
							}
						});
						recView.addOnScrollListener(load);*/

						/*recView.setOnScrollListener(new AbsListView.OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView absListView, int i) {

							}

							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
								if (view.getLastVisiblePosition() == totalItemCount - 1 && recView.getCount() > (count - 1) && !isLoading) {
									isLoading = true;
									recView.addFooterView(footerList);
									start += count;
//                                        startIndex = 0;
									getMoreData();
									//Log.i(TAG, "onScroll: last ");
								}
							}
						});*/
					} else {
						proses.DismissDialog();
						Toast.makeText(Equipment.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(Equipment.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void prepareDataDropdownPIC() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(Equipment.this, new JSONObject(), "GET", LinkURL.UrlDataDropdownPIC + customerID + "?", "", "", 0, new ApiVolley.VolleyCallback() {
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
								"Pilih...",
								""
						));
						final int listSize = masterPIC.size() - 1;
						adapterPIC = new AdapterSpinnerPIC(Equipment.this, R.layout.view_spinner_pic, R.id.txtNamaPIC, masterPIC) {
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
						dropdownPIC.setAdapter(adapterPIC);
						if (!selected_pic_by_id.equals("")) {
							for (int i = 0; i < masterPIC.size(); i++) {
								String id = masterPIC.get(i).getId();
								if (id.equals(selected_pic_by_id)) {
									dropdownPIC.setSelection(i);
									break;
								}
							}
						} else {
							dropdownPIC.setSelection(listSize);
						}
						dropdownPIC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								if (position == masterPIC.size() - 2) {
//									Toast.makeText(Equipment.this, "iki posisi trakhir coooyy", Toast.LENGTH_SHORT).show();
									dropdownPIC.setSelection(listSize);
									prepareDialogTambahPIC();
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

					} else if (status.equals("404")){
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
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
						adapterPIC = new AdapterSpinnerPIC(Equipment.this, R.layout.view_spinner_pic, R.id.txtNamaPIC, masterPIC) {
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
						dropdownPIC.setAdapter(adapterPIC);
						if (!selected_pic_by_id.equals("")) {
							for (int i = 0; i < masterPIC.size(); i++) {
								String id = masterPIC.get(i).getId();
								if (id.equals(selected_pic_by_id)) {
									dropdownPIC.setSelection(i);
									break;
								}
							}
						} else {
							dropdownPIC.setSelection(listSize);
						}
						dropdownPIC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
							@Override
							public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
								if (position == masterPIC.size() - 2) {
//									Toast.makeText(Equipment.this, "iki posisi trakhir coooyy", Toast.LENGTH_SHORT).show();
									dropdownPIC.setSelection(listSize);
									prepareDialogTambahPIC();
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> adapterView) {

							}
						});
					}else {
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(Equipment.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});

	}

	private void prepareDialogTambahPIC() {
		final EditText namaTambahPIC, jabatanTambahPIC, emailTambahPIC, handphoneTambahPIC;
		final Dialog dialog = new Dialog(Equipment.this);
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
					Toast.makeText(Equipment.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (jabatanTambahPIC.getText().toString().equals("")) {
					Toast.makeText(Equipment.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (emailTambahPIC.getText().toString().equals("")) {
					Toast.makeText(Equipment.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else if (handphoneTambahPIC.getText().toString().equals("")) {
					Toast.makeText(Equipment.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				} else {
					prepareDataTambahPIC();
				}
			}

			private void prepareDataTambahPIC() {
				proses.ShowDialog();
				JSONObject jBody = new JSONObject();
				try {
					jBody.put("id_header", id_header);
					jBody.put("nama", namaTambahPIC.getText().toString());
					jBody.put("jabatan", jabatanTambahPIC.getText().toString());
					jBody.put("email", emailTambahPIC.getText().toString());
					jBody.put("phone", handphoneTambahPIC.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				ApiVolley request = new ApiVolley(Equipment.this, jBody, "POST", LinkURL.UrlTambahDataPIC, "", "", 0, new ApiVolley.VolleyCallback() {
					@Override
					public void onSuccess(String result) {
						proses.DismissDialog();
						dialog.dismiss();
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getJSONObject("metadata").getString("status");
							String message = object.getJSONObject("metadata").getString("message");
							if (status.equals("200")) {
								Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
								prepareDataDropdownPIC();
							} else {
								Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(String result) {
						proses.DismissDialog();
						dialog.dismiss();
						Toast.makeText(Equipment.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
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

	private void getMoreData() {
		isLoading = true;
		ApiVolley request = new ApiVolley(Equipment.this, new JSONObject(), "GET", LinkURL.UrlReportJobDailyVAS + "start=" + String.valueOf(start) + "&limit=" + String.valueOf(count), "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				moreList = new ArrayList<>();
				try {
					load.canLoad();
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray formsParent = response.getJSONArray("forms");
						for (int i = 0; i < formsParent.length(); i++) {
							ArrayList<ModelListEquipmentChild> listChild = new ArrayList<>();
							JSONObject isiFormsParent = formsParent.getJSONObject(i);
							JSONArray formsChild = isiFormsParent.getJSONArray("items");
							for (int j = 0; j < formsChild.length(); j++) {
								JSONObject isiFormsChild = formsChild.getJSONObject(j);
								listChild.add(new ModelListEquipmentChild(
										isiFormsChild.getString("id"),
										isiFormsChild.getString("tipe"),
										isiFormsChild.getString("standard"),
										isiFormsChild.getString("tipe"),
										isiFormsChild.getString("value_radiobutton"),
										isiFormsChild.getString("value_text"),
										isiFormsChild.getString("note")
								));
							}
							list.add(new ModelListEquipment(
									isiFormsParent.getString("nomor_romawi"),
									isiFormsParent.getString("nama_kategori"),
									listChild
							));
						}

						isLoading = false;
//						recView.removeFooterView(footerList);
						if (adapterRVEquipment != null) adapterRVEquipment.addMoreData(moreList);
					} else {
						load.cantLoad();
						Toast.makeText(Equipment.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				load.finishLoad();
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				load.finishLoad();
				Toast.makeText(Equipment.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void prepareDataReportEquipment() {
		proses.ShowDialog();
		/*final JSONObject jBody = new JSONObject();
		try {
			jBody.put("id_header", id_header);
			jBody.put("id_gambar_ttd", Signature.idUploadGambarTTD);
			jBody.put("id_gambar_selfi", idUploadGambarSelfie);
			jBody.put("status_layanan", String.valueOf(booleanLayak));//radio button siap blm siap
//			jBody.put("id_gambar_selfi", "1");
//			jBody.put("id_gambar_ttd", "1");
//			jBody.put("status_layanan", "1");
			if (isian.getText().toString().equals("")) {
				jBody.put("note", "");
			} else {
				jBody.put("note", isian.getText().toString());
			}
			jBody.put("id_pic", idPIC);
			ArrayList<JSONObject> jsonArray = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {//7
				ModelListEquipment modelParent = list.get(i);
				for (int j = 0; j < modelParent.getList().size(); j++) {
					ModelListEquipmentChild modelChild = modelParent.getList().get(j);
					JSONObject object = new JSONObject();
					if (modelChild.getRadio1().equals("") || modelChild.getNoteTeks1().equals("")) {
						Toast.makeText(Equipment.this, "Mohon di isi semua pilihan / isian terlebih dahulu", Toast.LENGTH_SHORT).show();
						proses.DismissDialog();
						return;
					} else if (!modelChild.getRadio1().equals("") && !modelChild.getNoteTeks1().equals("")) {
						object.put("id", modelChild.getId());
						object.put("value_radio", modelChild.getRadio1());
						object.put("value_text", modelChild.getNoteTeks1());
						object.put("note", modelChild.getIsian());
						jsonArray.add(object);
					}
				}
			}
			jBody.put("form_items", new JSONArray(jsonArray));

		} catch (JSONException e) {
			e.printStackTrace();
		}*/
		ApiVolley request = new ApiVolley(Equipment.this, jBody, "POST", LinkURL.UrlReportEquipment, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
						onBackPressed();
					} else {
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(Equipment.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void openFrontCamera() {
		int cameraCount = 0;
		Camera cam = null;
		Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
		cameraCount = Camera.getNumberOfCameras();
		for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
			Camera.getCameraInfo(camIdx, cameraInfo);
			if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				try {
//					cam = Camera.open(camIdx);
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra("android.intent.extras.CAMERA_FACING", camIdx);
					startActivityForResult(intent, PICK_IMAGE_REQUEST);
				} catch (RuntimeException e) {
					Log.e("error camera", "Camera failed to open: " + e.getLocalizedMessage());
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			imgSelfie.setImageDrawable(new BitmapDrawable(getResources(), photo));
			imgSelfie.setVisibility(View.VISIBLE);
			int sizeInDp = 38;
			float scale = getResources().getDisplayMetrics().density;
			int dpAsPixels = (int) (sizeInDp * scale + 0.5f);
			btnCustImage.setPadding(0, dpAsPixels, 0, dpAsPixels);
			Log.d("bmpFrontCamera", String.valueOf(photo));
			uploadBitmap(getFileDataFromDrawable(photo));
		}
	}

	public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	private void uploadBitmap(final byte[] bitmap) {
		proses.ShowDialog();
		idUploadGambarSelfie = "";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Client-Service", "Gmedia_SUPPORTVAS");
		headers.put("Auth-Key", "frontend_client");
//		headers.put("Content-Type", "application/json");
		headers.put("User-Id", session.getKeyUserId());
		headers.put("Token", session.getKeyToken());

		VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, LinkURL.UrlUploadGambarSelfieReportVAS, headers, new Response.Listener<NetworkResponse>() {
			@Override
			public void onResponse(NetworkResponse response) {
				proses.DismissDialog();
				try {
					String result = new String(response.data);
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject responseAPI = object.getJSONObject("response");
						idUploadGambarSelfie = responseAPI.getString("id");
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				proses.DismissDialog();
				Toast.makeText(Equipment.this, "Terjadi Kesalahan upload gambar", Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
				Map<String, DataPart> params = new HashMap<>();
				long imageName = System.currentTimeMillis();
				params.put("pic", new VolleyMultipartRequest.DataPart(imageName + ".jpg", bitmap));
				return params;
			}
		};
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(Equipment.this);
		}

		// retry when timeout
		request.setRetryPolicy(new DefaultRetryPolicy(
				8 * 1000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
		));
		request.setShouldCache(false);
		requestQueue.add(request);
		requestQueue.getCache().clear();
	}

	/*@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
			Uri uri = data.getData();
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
				Log.d("bmpGallery", String.valueOf(bitmap));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	/*private ArrayList<ModelListEquipment> prepareDataEquipment() {
		ArrayList<ModelListEquipment> rvData = new ArrayList<>();
		for (int i = 0; i < jenis.length; i++) {
			ModelListEquipment model = new ModelListEquipment();
			model.setJudul(jenis[i]);
			for (int j = 0; j < jenis_child.length; j++) {
				model.setList(new ModelListEquipmentChild(
						jenis_child[j]
				));
			}
			rvData.add(model);
		}
		return rvData;
	}*/

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
