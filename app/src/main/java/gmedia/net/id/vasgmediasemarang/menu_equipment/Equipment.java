package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.LoadMoreScrollListener;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;
import gmedia.net.id.vasgmediasemarang.utils.VolleyMultipartRequest;
import gmedia.net.id.vasgmediasemarang.utils.WrapContentListView;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.Signature;

public class Equipment extends AppCompatActivity {
	private ListView listView;
	private ArrayList<ModelListEquipment> list;
	private ArrayList<ModelListEquipmentChild> listChild;
	private ListAdapterEquipment adapter;

	private RelativeLayout btnCustSignature, btnCustImage;
	private int PICK_IMAGE_REQUEST = 1;
	private Proses proses;
	private int start = 0, count = 10;

	private LinearLayout btnKirim;
	public static String id_header = "";
	private RadioGroup btnRadio;
	private int selectedId = -1;
	private RadioButton radioButton;
	private int booleanLayak = -1;
	private SessionManager session;
	private RequestQueue requestQueue;
	private String idUploadGambarSelfie = "";
	public static String idUploadTTD = "";
	private String idList = "";

	//private List<MasterModelPIC> masterPIC;
    private List<MasterModelPIC> listPic = new ArrayList<>();
	private String idPIC = "", isReport = "";
	private EditText isian;
	private ImageView imgSelfie, imgTTD;
	public static Bitmap bitmapTTD;
	public static Boolean signatureIsDone = false;
	private TextView txtNamaSite;
	private RadioButton radioButtonLayak, radioButtonBlmLayak;
	private String status_layanan = "", note = "", pic_selfie = "", pic_ttd = "";
	private boolean jBodyIsNull = true;
	private JSONObject jBody, jBodySementara;

	private View btn_pic;
	private TextView txt_pic;
	private Dialog pic_dialog;
	private AdapterPic adapterPic;
	private LoadMoreScrollListener loadManager;

	private String search = "";
	private String customerID = "";
	private String serviceID = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Report on Site");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);

		proses = new Proses(this);
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
		txtNamaSite = findViewById(R.id.txtNamaSite);
		listView = findViewById(R.id.lv_equipment);
		btnCustSignature = findViewById(R.id.btnCustSignature);
		btnCustImage = findViewById(R.id.btnCustImage);

		btnKirim = findViewById(R.id.btnKirimReportEquipment);
		isian = findViewById(R.id.etEquipment);
		btnRadio = findViewById(R.id.radioGrupLayak);
		imgSelfie = findViewById(R.id.imgSelfie);
		imgTTD = findViewById(R.id.imgTTD);
		radioButtonLayak = findViewById(R.id.radioButtonLayak);
		radioButtonBlmLayak = findViewById(R.id.radioButtonTdkLayak);

        txt_pic = findViewById(R.id.txt_pic);
        txt_pic.setText("Pilih PIC..");
        btn_pic = findViewById(R.id.btn_pic);

        loadManager = new LoadMoreScrollListener() {
			@Override
			public void onLoadMore() {
				prepareDataDropdownPIC(false, search);
			}
		};
	}

	private void initAction() {
		prepareDataEquipment();
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
				radioButton = findViewById(selectedId);
				String isiboolean = radioButton.getText().toString();
				if (isiboolean.equals("Sudah Sesuai")) {
					booleanLayak = 1;
				} else if (isiboolean.equals("Belum Sesuai")){
					booleanLayak = 0;
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
						jBody.put("id_gambar_ttd", idUploadTTD);
						jBody.put("id_gambar_selfi", idUploadGambarSelfie);
						jBody.put("status_layanan", String.valueOf(booleanLayak));//radio button siap blm siap
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
								Toast.makeText(Equipment.this, "Silahkan Isi Kelayakan " +
										"Layanan Terlebih Dahulu", Toast.LENGTH_SHORT).show();
							} else if (idPIC.isEmpty()) {
								Toast.makeText(Equipment.this, "Silahkan pilih " +
										"PIC terlebih dahulu", Toast.LENGTH_SHORT).show();
							} else if (idUploadGambarSelfie.equals("")) {
								Toast.makeText(Equipment.this, "Silahkan upload " +
										"selfie terlebih dahulu", Toast.LENGTH_SHORT).show();
							} else if (idUploadTTD.equals("")) {
								Toast.makeText(Equipment.this, "Silahkan upload tanda " +
										"tangan terlebih dahulu", Toast.LENGTH_SHORT).show();
							} else {
								prepareDataReportEquipment();
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else if (isReport.equals("1")) {
					Toast.makeText(Equipment.this, "Anda sudah pernah mengirim report ini", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btn_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPic();
            }
        });
	}

	private void showDialogPic(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(size);
        }
        else{
            display.getSize(size);
        }

        int device_TotalWidth = size.x;
        int device_TotalHeight = size.y;

        pic_dialog = new Dialog(this);
        pic_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pic_dialog.setContentView(R.layout.popup_pilih_pic);
        if(pic_dialog.getWindow() != null){
			pic_dialog.getWindow().setLayout(device_TotalWidth * 90 / 100 ,
                    device_TotalHeight * 70 / 100);
			pic_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        EditText txt_search = pic_dialog.findViewById(R.id.txt_search);
        TextView txt_tambah = pic_dialog.findViewById(R.id.txt_tambah);
        RecyclerView rv_pic = pic_dialog.findViewById(R.id.rv_pic);
        rv_pic.setItemAnimator(new DefaultItemAnimator());
        rv_pic.setLayoutManager(new LinearLayoutManager(this));
        adapterPic = new AdapterPic(this, listPic);
        rv_pic.setAdapter(adapterPic);
        rv_pic.addOnScrollListener(loadManager);

        txt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            	search = s.toString();
                prepareDataDropdownPIC(true, search);
            }
        });

        txt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareDialogTambahPIC();
				pic_dialog.dismiss();
            }
        });

		pic_dialog.show();
    }

	@Override
	protected void onResume() {
		super.onResume();
		if (signatureIsDone) {
			signatureIsDone = false;
			btnCustSignature.setBackgroundColor(Color.parseColor("#FFFFFF"));
			imgTTD.setImageDrawable(new BitmapDrawable(getResources(), bitmapTTD));
			imgTTD.setVisibility(View.VISIBLE);
		}
	}

	public void updatePic(MasterModelPIC p){
		txt_pic.setText(p.getNama());
		idPIC = p.getId();
		if(pic_dialog != null && pic_dialog.isShowing()){
			pic_dialog.dismiss();
		}
	}

	private void prepareDataEquipment() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(Equipment.this, new JSONObject(), "GET",
				LinkURL.UrlReportJobDailyVAS + idList + "?" + "start=" + String.valueOf(start) + "&limit=" +
						String.valueOf(count), "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				Log.d("eq_log", result);
				list = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONObject header = response.getJSONObject("header");

						serviceID = header.getString("service_id");
						txtNamaSite.setText(header.getString("nama_site"));
						id_header = header.getString("id");
						isReport = header.getString("is_report");
						status_layanan = header.getString("status_layanan");
						note = header.getString("note");
						String idSelfie = header.getString("id_gambar_selfi");
						String idTTD = header.getString("id_gambar_ttd");
						pic_selfie = header.getString("pic_selfi_image");
						pic_ttd = header.getString("pic_ttd_image");
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
						listView.getViewTreeObserver()
								.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
									@Override
									public void onGlobalLayout() {
										proses.DismissDialog();
										listView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
									}
								});
						WrapContentListView.setListViewHeightBasedOnChildren(listView);

						if (status_layanan.equals("OK")) {
							radioButtonLayak.setChecked(true);
						} else if (status_layanan.equals("NOK")) {
							radioButtonBlmLayak.setChecked(true);
						}

						if (!note.equals("")) {
							isian.setText(note);
						}
						if (!idSelfie.equals("0")) {
							idUploadGambarSelfie = idSelfie;
						}
						if (!idTTD.equals("0")) {
							idUploadTTD = idTTD;
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

						prepareDataDropdownPIC(true, "");
					} else {
						Toast.makeText(Equipment.this, message, Toast.LENGTH_LONG).show();
						proses.DismissDialog();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("eq_log", e.getMessage());
					proses.DismissDialog();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Log.e("eq_log", result);
				Toast.makeText(Equipment.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void prepareDataDropdownPIC(final boolean init, String s) {
		if(init){
			loadManager.initLoad();
		}

		JSONObject body = new JSONObject();
		try{
			body.put("customer_id", customerID);
			body.put("service_id", serviceID);
			body.put("start", loadManager.getLoaded());
			body.put("limit", 20);
			body.put("search", s);
		}
		catch (JSONException e){
			Log.e("pic_log", e.getMessage());
			Toast.makeText(this, "Terjadi kesalahan data", Toast.LENGTH_LONG).show();
		}

		ApiVolley request = new ApiVolley(this, body, "POST",
				LinkURL.UrlDataDropdownPIC, "", "", 0,
				new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				Log.d("pic_log", result);
				try {
					if(init){
						listPic.clear();
					}

					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray pic_list = response.getJSONArray("pic_list");
						for (int i = 0; i < pic_list.length(); i++) {
							JSONObject isiPIC = pic_list.getJSONObject(i);
							listPic.add(new MasterModelPIC(
									isiPIC.getString("id"),
									isiPIC.getString("nama"),
									isiPIC.getString("email")
							));
						}

						loadManager.finishLoad(pic_list.length());
						if(pic_dialog != null && pic_dialog.isShowing()){
							adapterPic.notifyDataSetChanged();
						}
					}
					else if(status.equals("404")){
						if(init){
							listPic.clear();
							if(pic_dialog != null && pic_dialog.isShowing()){
								adapterPic.notifyDataSetChanged();
							}
						}

						loadManager.finishLoad(0);
					}
					else {
						Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
						loadManager.finishLoad(0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("pic_log", e.getMessage());
					loadManager.failedLoad();
				}
			}

			@Override
			public void onError(String result) {
				Log.e("pic_log", result);
				Toast.makeText(Equipment.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
				loadManager.failedLoad();
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
				ApiVolley request = new ApiVolley(Equipment.this, jBody, "POST",
						LinkURL.UrlTambahDataPIC, "", "", 0, new ApiVolley.VolleyCallback() {
					@Override
					public void onSuccess(String result) {
						proses.DismissDialog();
						dialog.dismiss();
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getJSONObject("metadata").getString("status");
							String message = object.getJSONObject("metadata").getString("message");

							Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
							if (status.equals("200")) {
								prepareDataDropdownPIC(true, "");
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
		RelativeLayout btnCancel = dialog.findViewById(R.id.tombolCancelTambahPIC);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	private void prepareDataReportEquipment() {
		proses.ShowDialog();

		Log.d("report_log", "body : " + jBody.toString());
		ApiVolley request = new ApiVolley(Equipment.this, jBody, "POST",
				LinkURL.UrlReportEquipment, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
					if (status.equals("200")) {
						onBackPressed();
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
		headers.put("User-Id", session.getKeyUserId());
		headers.put("Token", session.getKeyToken());

		VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST,
				LinkURL.UrlUploadGambarSelfieReportVAS, headers, new Response.Listener<NetworkResponse>() {
			@Override
			public void onResponse(NetworkResponse response) {
				proses.DismissDialog();
				try {
					String result = new String(response.data);
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();

					if (status.equals("200")) {
						JSONObject responseAPI = object.getJSONObject("response");
						idUploadGambarSelfie = responseAPI.getString("id");
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				break;
			case R.id.action_settings:
				prepareDataSimpanSementara();
				break;
		}
		return true;
	}

	private void prepareDataSimpanSementara() {
		proses.ShowDialog();
		jBodySementara = new JSONObject();
		try {
			jBodySementara.put("id_header", id_header);
			jBodySementara.put("id_gambar_ttd", idUploadTTD);
			jBodySementara.put("id_gambar_selfi", idUploadGambarSelfie);
			jBodySementara.put("status_layanan", String.valueOf(booleanLayak));//radio button siap blm siap
//			jBody.put("id_gambar_selfi", "1");
//			jBody.put("id_gambar_ttd", "1");
//			jBody.put("status_layanan", "1");
			if (isian.getText().toString().equals("")) {
				jBodySementara.put("note", "");
			} else {
				jBodySementara.put("note", isian.getText().toString());
			}

			jBodySementara.put("id_pic", idPIC);
			ArrayList<JSONObject> jsonArray = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {//7
				ModelListEquipment modelParent = list.get(i);
				for (int j = 0; j < modelParent.getList().size(); j++) {
					ModelListEquipmentChild modelChild = modelParent.getList().get(j);
					String tipe = modelChild.getTipe();
					JSONObject object = new JSONObject();
					if (tipe.equals("radio")) {
						object.put("id", modelChild.getId());
						object.put("value_radio", modelChild.getRadio1());
						object.put("note", modelChild.getIsian());
						jsonArray.add(object);
					} else if (tipe.equals("text")) {
						object.put("id", modelChild.getId());
						object.put("value_text", modelChild.getNoteTeks1());
						object.put("note", modelChild.getIsian());
						jsonArray.add(object);
					}
				}
			}
			jBodySementara.put("form_items", new JSONArray(jsonArray));
			prepareSimpanSementara();
			/*if (!jBodyIsNull) {
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
			}*/
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void prepareSimpanSementara() {
		ApiVolley request = new ApiVolley(Equipment.this, jBodySementara, "POST",
                LinkURL.UrlReportSementaraEquipment, "", "", 0,
                new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String message = object.getJSONObject("metadata").getString("message");
					Toast.makeText(Equipment.this, message, Toast.LENGTH_SHORT).show();
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

}
