package gmedia.net.id.vasgmediasemarang.menu_report_survey;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts.DetailJobDailyTs;
import gmedia.net.id.vasgmediasemarang.menu_equipment.Equipment;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.SimpleObjectModel;

public class ReportSurvey extends AppCompatActivity {
	private ListView listView;
	private ListAdapterReportSurvey adapter;
	private ArrayList<ModelListReportSurvey> list;
	private int jenisLayout[] =
			{
					1,
					2,
					2,
					1,
					2,
					2,
					1,
					2,
					2
			};
	private LinearLayout btnKirim, layoutFO, layoutWireless;
	private String layanan_fo = "", layanan_wireless = "", idJobDailyTS = "",
			jenis_job = "", resultProject = "", resultFO = "", resultWireless = "",
			kondisi = "", statusSurvey = "", flag_custom = "", update_progress = "", note = "";
	private int selectedIdProject = -1, selectedIDFO = -1, selectedIDWireless = -1;
	private RadioButton radioButtonProject, radioButtonFO, radioButtonWireless;
	private RadioGroup radioGroupProject, radioGroupFO, radioGroupWireless;
	private Boolean project = false, fo = false, wireless = false;
	private Proses proses;
	private RadioButton radioButtonDoneProject, radioButtonUndoneProject, radioButtonCoverFo,
			radioButtonUncoverFo, radioButtonCoverWireless, radioButtonUncoverWireless;
	private EditText noteReportSurvey;

	private ReportSurveyAdapter surveyAdapter;
	private List<String> listHeader = new ArrayList<>();
	private HashMap<String, List<ReportModel>> listForm = new HashMap<>();
	private RecyclerView rv_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report_survey);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Report");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.layoutKuning)));
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = ReportSurvey.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.notifBarKuning));
		}
		proses = new Proses(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			layanan_fo = bundle.getString("layanan_fo");
			layanan_wireless = bundle.getString("layanan_wireless");
			idJobDailyTS = bundle.getString("id_job_daily_ts");
			jenis_job = bundle.getString("jenis_project");
			statusSurvey = bundle.getString("statusSurvey");
			flag_custom = bundle.getString("flag_custom");
			update_progress = bundle.getString("update_progres");
			note = bundle.getString("note");
		}
		initUI();
		initAction();

        rv_data = findViewById(R.id.rv_data);
        rv_data.setItemAnimator(new DefaultItemAnimator());
        rv_data.setLayoutManager(new LinearLayoutManager(this));
        surveyAdapter = new ReportSurveyAdapter(this, listHeader, listForm);
        rv_data.setAdapter(surveyAdapter);

		proses.ShowDialog();
        loadDraft();
        loadMasterSatuan();
	}

	private void initUI() {
//		listView = (ListView) findViewById(R.id.lv_report_survey);
		btnKirim = (LinearLayout) findViewById(R.id.btnKirimReport);
		layoutFO = (LinearLayout) findViewById(R.id.layoutFO);
		layoutWireless = (LinearLayout) findViewById(R.id.layoutWireless);
		radioGroupProject = (RadioGroup) findViewById(R.id.radioGrupProject);
		radioGroupFO = (RadioGroup) findViewById(R.id.radioGrupFO);
		radioGroupWireless = (RadioGroup) findViewById(R.id.radioGrupWireless);
		radioButtonDoneProject = (RadioButton) findViewById(R.id.radioButtonDoneProject);
		radioButtonUndoneProject = (RadioButton) findViewById(R.id.radioButtonUndoneProject);
		radioButtonCoverFo = (RadioButton) findViewById(R.id.radioButtonCoverFo);
		radioButtonUncoverFo = (RadioButton) findViewById(R.id.radioButtonUncoverFo);
		radioButtonCoverWireless = (RadioButton) findViewById(R.id.radioButtonCoverWireless);
		radioButtonUncoverWireless = (RadioButton) findViewById(R.id.radioButtonUncoverWireless);
		noteReportSurvey = (EditText) findViewById(R.id.edtNoteReportSurveyTS);
	}

	private void initAction() {
		/*list = new ArrayList<>();
		list = prepareDataFormReportSurvey();
		adapter = new ListAdapterReportSurvey(ReportSurvey.this, list);
		listView.setAdapter(adapter);*/
		String jenis = statusSurvey;
		if (jenis.equals("1")) {
			if (!layanan_fo.equals("") && !layanan_wireless.equals("")) {
				layoutFO.setVisibility(View.VISIBLE);
				layoutWireless.setVisibility(View.VISIBLE);
				if (update_progress.equals("sudah")) {
					radioButtonDoneProject.setChecked(true);
					radioButtonUndoneProject.setChecked(false);
				} else if (update_progress.equals("belum")) {
					radioButtonUndoneProject.setChecked(true);
					radioButtonDoneProject.setChecked(false);
				}
				if (layanan_fo.equals("cover")) {
					radioButtonCoverFo.setChecked(true);
					radioButtonUncoverFo.setChecked(false);
				} else if (layanan_fo.equals("uncover")) {
					radioButtonUncoverFo.setChecked(true);
					radioButtonCoverFo.setChecked(false);
				}
				if (layanan_wireless.equals("cover")) {
					radioButtonCoverWireless.setChecked(true);
					radioButtonUncoverWireless.setChecked(false);
				} else if (layanan_wireless.equals("uncover")) {
					radioButtonUncoverWireless.setChecked(true);
					radioButtonCoverWireless.setChecked(false);
				}
				kondisi = "semua";
				project = true;
				fo = true;
				wireless = true;
			} else {
				if (update_progress.equals("sudah")) {
					radioButtonDoneProject.setChecked(true);
					radioButtonUndoneProject.setChecked(false);
				} else if (update_progress.equals("belum")) {
					radioButtonUndoneProject.setChecked(true);
					radioButtonDoneProject.setChecked(false);
				}
				if (!layanan_fo.equals("")) {
					layoutFO.setVisibility(View.VISIBLE);
					kondisi = "fo";
					if (layanan_fo.equals("cover")) {
						radioButtonCoverFo.setChecked(true);
						radioButtonUncoverFo.setChecked(false);
					} else if (layanan_fo.equals("uncover")) {
						radioButtonUncoverFo.setChecked(true);
						radioButtonCoverFo.setChecked(false);
					}
					project = true;
					fo = true;
					wireless = false;
				} else if (!layanan_wireless.equals("")) {
					layoutWireless.setVisibility(View.VISIBLE);
					kondisi = "wireless";
					if (layanan_wireless.equals("cover")) {
						radioButtonCoverWireless.setChecked(true);
						radioButtonUncoverWireless.setChecked(false);
					} else if (layanan_wireless.equals("uncover")) {
						radioButtonUncoverWireless.setChecked(true);
						radioButtonCoverWireless.setChecked(false);
					}
					project = true;
					wireless = true;
					fo = false;
				} else {
					layoutFO.setVisibility(View.GONE);
					layoutWireless.setVisibility(View.GONE);
					project = true;
					fo = false;
					wireless = false;
				}
			}
		} else {
			layoutFO.setVisibility(View.GONE);
			layoutWireless.setVisibility(View.GONE);
			kondisi = "kosong";
			if (update_progress.equals("sudah")) {
				radioButtonDoneProject.setChecked(true);
				radioButtonUndoneProject.setChecked(false);
			} else if (update_progress.equals("belum")) {
				radioButtonUndoneProject.setChecked(true);
				radioButtonDoneProject.setChecked(false);
			}
			project = true;
			fo = false;
			wireless = false;
		}
		if (!note.equals("")) {
			noteReportSurvey.setText(note);
		}
		radioGroupProject.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				selectedIdProject = radioGroupProject.getCheckedRadioButtonId();
				radioButtonProject = (RadioButton) findViewById(selectedIdProject);
				if (radioButtonProject.getText().toString().equals("Done")) {
					resultProject = "done";
				} else {
					resultProject = "undone";
				}
			}
		});
		radioGroupFO.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				selectedIDFO = radioGroupFO.getCheckedRadioButtonId();
				radioButtonFO = (RadioButton) findViewById(selectedIDFO);
				if (radioButtonFO.getText().toString().equals("Cover")) {
					resultFO = "cover";
				} else {
					resultFO = "uncover";
				}
				Log.d("resultFo",resultFO);
			}
		});
		radioGroupWireless.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				selectedIDWireless = radioGroupWireless.getCheckedRadioButtonId();
				radioButtonWireless = (RadioButton) findViewById(selectedIDWireless);
				if (radioButtonWireless.getText().toString().equals("Cover")) {
					resultWireless = "cover";
				} else {
					resultWireless = "uncover";
				}
			}
		});
		btnKirim.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (project && fo && wireless) {
					if (!resultProject.equals("") && !resultFO.equals("") && !resultWireless.equals("")) {
						popUpReportSurvey();
					} else {
						Toast.makeText(ReportSurvey.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
					}
				} else if (project && fo) {
					if (!resultProject.equals("") && !resultFO.equals("")) {
						popUpReportSurvey();
					} else {
						Toast.makeText(ReportSurvey.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
					}
				} else if (project && wireless) {
					if (!resultProject.equals("") && !resultWireless.equals("")) {
						popUpReportSurvey();
					} else {
						Toast.makeText(ReportSurvey.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
					}
				} else if (project) {
					if (!resultProject.equals("")) {
						popUpReportSurvey();
					} else {
						Toast.makeText(ReportSurvey.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(ReportSurvey.this, "Silahkan lengkapi data terlebih dahulu", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void loadMasterSatuan(){
		ApiVolley request = new ApiVolley(ReportSurvey.this, new JSONObject(), "GET",
				LinkURL.UrlMasterSatuanBarang, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				try{
					JSONObject jsonres = new JSONObject(result);
					if(jsonres.getJSONObject("metadata").getInt("status") == 200){
						JSONArray json_satuan = jsonres.getJSONObject("response").getJSONArray("satuan");
						List<SimpleObjectModel> list_satuan = new ArrayList<>();
						for(int i = 0; i < json_satuan.length(); i++){
							JSONObject satuan = json_satuan.getJSONObject(i);
							list_satuan.add(new SimpleObjectModel(satuan.getString("id"), satuan.getString("text")));
						}

						surveyAdapter.setListSatuan(list_satuan);
					}
					else{
						Toast.makeText(ReportSurvey.this, jsonres.getJSONObject("metadata").
								getString("message"), Toast.LENGTH_SHORT).show();
					}
				}
				catch (JSONException e){
					Log.e(LinkURL.TAG, e.getMessage());
				}
			}

			@Override
			public void onError(String result) {
				Log.e(LinkURL.TAG, result);
			}
		});
	}

	private void loadDraft(){
		String parameter = String.format(Locale.getDefault(), "/%s/%s", idJobDailyTS, flag_custom);
		ApiVolley request = new ApiVolley(ReportSurvey.this, new JSONObject(), "GET",
				LinkURL.UrlDraftReport + parameter, "", "",
                0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
                        JSONArray forms = object.getJSONObject("response").getJSONArray("forms");
                        for(int i = 0; i < forms.length(); i++){
                            JSONObject form = forms.getJSONObject(i);
                            String header = form.getString("form_name");
                            listHeader.add(header);

                            List<ReportModel> listReport = new ArrayList<>();
                            JSONArray fields = form.getJSONArray("fields");
                            for(int j = 0; j < fields.length(); j++){
                                JSONObject field = fields.getJSONObject(j);

                                ReportModel p;
                                if(field.getString("type").equals("select")){
                                    p = new ReportSelectModel(field.getString("id"), field.getString("field_name"),
											field.getInt("has_unit") == 1, field.getString("api_url"));
                                }
                                else{
                                    p = new ReportModel(field.getString("id"),
                                            field.getString("field_name"), field.getInt("has_unit") == 1);
                                }

                                listReport.add(p);
                            }
                            listForm.put(header, listReport);

                            surveyAdapter.notifyDataSetChanged();
                        }
					} else {
						Toast.makeText(ReportSurvey.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					Log.e(LinkURL.TAG, e.getMessage());
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(ReportSurvey.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private boolean isReportInputValid(){
		for(String s : listHeader){
			for(ReportModel r : listForm.get(s)){
				if(r.is_unit() && r.getUnit_id().isEmpty()){
					return false;
				}
				else if(r.getValue().isEmpty()){
					return false;
				}
			}
		}

		return true;
	}

	private JSONArray getInputData(){
		try{
			List<JSONObject> data = new ArrayList<>();
			for(String s : listHeader){
				for(ReportModel r : listForm.get(s)){
					JSONObject entry = new JSONObject();
					entry.put("id", r.getId());
					entry.put("value", r.getValue());

					if(r.is_unit()){
						entry.put("unit_id", r.getUnit_id());
					}
					else{
						entry.put("unit_id", "");
					}

					data.add(entry);
				}
			}

			return new JSONArray(data);
		}
		catch (JSONException e){
			Log.e(LinkURL.TAG, e.getMessage());
			return new JSONArray();
		}
	}

	private void popUpReportSurvey() {
		if(isReportInputValid()){
			final Dialog dialog = new Dialog(ReportSurvey.this);
			dialog.setContentView(R.layout.popup_validasi_kirim_report);
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			RelativeLayout btnIya = (RelativeLayout) dialog.findViewById(R.id.btnIyaPopupValidasiKirimReport);
			btnIya.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
					prepareSendDataReportSurvey();
				}

				private void prepareSendDataReportSurvey() {
					proses.ShowDialog();
					JSONObject jBody = new JSONObject();
					try {
						jBody.put("id", idJobDailyTS);
						jBody.put("status_job", resultProject);
						jBody.put("coverage_wireless", resultWireless);
						jBody.put("coverage_fo", resultFO);
						jBody.put("flag_custom", flag_custom);
						jBody.put("note", noteReportSurvey.getText().toString());

						JSONArray data = getInputData();
						Log.d(LinkURL.TAG, "data " + data);
						jBody.put("data", data);

					} catch (JSONException e) {
						e.printStackTrace();
					}

					ApiVolley request = new ApiVolley(ReportSurvey.this, jBody, "POST",
							LinkURL.UrlSendReportSurvey, "", "", 0,
                            new ApiVolley.VolleyCallback() {
						@Override
						public void onSuccess(String result) {
							proses.DismissDialog();
							try {
								JSONObject object = new JSONObject(result);
								String status = object.getJSONObject("metadata").getString("status");
								String message = object.getJSONObject("metadata").getString("message");
								Toast.makeText(ReportSurvey.this, message, Toast.LENGTH_SHORT).show();

								if (status.equals("200")) {
									onBackPressed();
								}
							} catch (JSONException e) {
								Log.e(LinkURL.TAG, e.getMessage());
								e.printStackTrace();
							}
						}

						@Override
						public void onError(String result) {
							proses.DismissDialog();
							Toast.makeText(ReportSurvey.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
						}
					});
				}
			});

			RelativeLayout btnTidak = (RelativeLayout) dialog.findViewById(R.id.btnTidakPopupValidasiKirimReport);
			btnTidak.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog.dismiss();
				}
			});
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			Window window = dialog.getWindow();
			window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		}
		else{
			Toast.makeText(this, "Pastikan semua field terisi", Toast.LENGTH_SHORT).show();
		}
	}

	/*private void popUpReportSurvey() {
		final Dialog dialog = new Dialog(ReportSurvey.this);
		dialog.setContentView(R.layout.popup_validasi_kirim_report);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		RelativeLayout btnIya = (RelativeLayout) dialog.findViewById(R.id.btnIyaPopupValidasiKirimReport);
		btnIya.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				prepareSendDataReportSurvey();
			}

			private void prepareSendDataReportSurvey() {
				proses.ShowDialog();
				JSONObject jBody = new JSONObject();
				try {
					jBody.put("id", idJobDailyTS);
					jBody.put("status_job", resultProject);
					jBody.put("coverage_wireless", resultWireless);
					jBody.put("coverage_fo", resultFO);
					jBody.put("flag_custom", flag_custom);
					jBody.put("note", noteReportSurvey.getText().toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}

				ApiVolley request = new ApiVolley(ReportSurvey.this, jBody, "POST",
						LinkURL.UrlSendReportSurvey, "", "", 0, new ApiVolley.VolleyCallback() {
					@Override
					public void onSuccess(String result) {
						proses.DismissDialog();
						try {
							JSONObject object = new JSONObject(result);
							String status = object.getJSONObject("metadata").getString("status");
							String message = object.getJSONObject("metadata").getString("message");
							if (status.equals("200")) {
								Toast.makeText(ReportSurvey.this, message, Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(ReportSurvey.this, message, Toast.LENGTH_SHORT).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(String result) {
						proses.DismissDialog();
						Toast.makeText(ReportSurvey.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
					}
				});
			}
		});

		RelativeLayout btnTidak = (RelativeLayout) dialog.findViewById(R.id.btnTidakPopupValidasiKirimReport);
		btnTidak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
	}*/

	private ArrayList<ModelListReportSurvey> prepareDataFormReportSurvey() {
		ArrayList<ModelListReportSurvey> rvData = new ArrayList<>();
		for (int i = 0; i < jenisLayout.length; i++) {
			ModelListReportSurvey model = new ModelListReportSurvey();
			model.setJenisLayout(jenisLayout[i]);
			rvData.add(model);
		}
		return rvData;
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
}