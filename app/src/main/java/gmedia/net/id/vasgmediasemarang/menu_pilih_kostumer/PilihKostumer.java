package gmedia.net.id.vasgmediasemarang.menu_pilih_kostumer;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gmedia.net.id.vasgmediasemarang.MainActivity;
import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;

public class PilihKostumer extends AppCompatActivity {
	private List<ModelListPilihKostumer> listPilihKostumer;
	private List<ModelListPilihKostumer> moreListPilihKostumer;
	private ListView listView;
	private ListAdapterPilihKostumer adapter;
	private String idKostumer[] =
			{
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512",
					"03.165.4512"
			};
	private String nama[] =
			{
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza",
					"E-Plaza"
			};
	private String alamat[] =
			{
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima",
					"Gajamada Plaza Lt.2 B29 Simpang Lima"
			};
	public static boolean isTambah = false;
	public static boolean isEdit = false;
	private Proses proses;
	private int startIndex = 0;
	private int count = 10;
	private boolean isLoading = false;
	private View footerList;
	private String search = "", tanggal = "", id = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pilih_kostumer);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Pilih Kostumer");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		proses = new Proses(this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String kiriman = bundle.getString("home");
			tanggal = bundle.getString("tanggal");
			if (kiriman.equals("edit")) {
				id = bundle.getString("ID");
				isEdit = true;
				isTambah = false;
			} else if (kiriman.equals("tambah")) {
				isTambah = true;
				isEdit = false;
			}
		}
		initUI();
		initAction();
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.lv_pilih_kostumer);
		LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footerList = li.inflate(R.layout.footer_list, null);
	}

	private void initAction() {
		prepareDataListPilihKostumer();
	}


	/*private ArrayList<ModelListPilihKostumer> prepareDataListPilihKostumer() {
		ArrayList<ModelListPilihKostumer> rvData = new ArrayList<>();
		for (int i = 0; i < idKostumer.length; i++) {
			ModelListPilihKostumer model = new ModelListPilihKostumer();
			model.setIdkostumer(idKostumer[i]);
			model.setNama(nama[i]);
			model.setAlamat(alamat[i]);
			rvData.add(model);
		}
		return rvData;
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		final SearchView searchView = (SearchView) searchItem.getActionView();
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String s) {
				/*search = s;
				loadCustomer(true);*/
				search = s;
				prepareDataListPilihKostumer();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String s) {
//				Toast.makeText(PilihKostumer.this, "mencari....", Toast.LENGTH_LONG).show();

				/*if (!searchView.isIconified() && TextUtils.isEmpty(s)) {
					search = "";
					loadCustomer(true);
				}*/
				return true;
			}
		});
		return true;
	}

	private void prepareDataListPilihKostumer() {
		proses.ShowDialog();
		ApiVolley request = new ApiVolley(PilihKostumer.this, new JSONObject(), "GET", LinkURL.UrlDaftarPelanggan + "start=" + startIndex + "&limit=" + count + "&search=" + search, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				isLoading = false;
				listPilihKostumer = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray jsonArray = response.getJSONArray("customer_list");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject isi = jsonArray.getJSONObject(i);
							listPilihKostumer.add(new ModelListPilihKostumer(
									isi.getString("customer_id"),
									isi.getString("service_id"),
									isi.getString("nama_site"),
									isi.getString("alamat")
							));
						}
						listView.setAdapter(null);
						adapter = new ListAdapterPilihKostumer(PilihKostumer.this, listPilihKostumer, tanggal, id);
						listView.setAdapter(adapter);
						listView.setOnScrollListener(new AbsListView.OnScrollListener() {
							@Override
							public void onScrollStateChanged(AbsListView absListView, int i) {

							}

							@Override
							public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
								if (view.getLastVisiblePosition() == totalItemCount - 1 && listView.getCount() > (count - 1) && !isLoading) {
									isLoading = true;
									listView.addFooterView(footerList);
									startIndex += count;
//                                        startIndex = 0;
									getMoreData();
									//Log.i(TAG, "onScroll: last ");
								}
							}
						});
					} else {
						Toast.makeText(PilihKostumer.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(PilihKostumer.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void getMoreData() {
		isLoading = true;
		ApiVolley request = new ApiVolley(PilihKostumer.this, new JSONObject(), "GET", LinkURL.UrlDaftarPelanggan + "start=" + startIndex + "&limit=" + count + "&search=" + search, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				moreListPilihKostumer = new ArrayList<>();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						JSONObject response = object.getJSONObject("response");
						JSONArray jsonArray = response.getJSONArray("customer_list");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject isi = jsonArray.getJSONObject(i);
							listPilihKostumer.add(new ModelListPilihKostumer(
									isi.getString("customer_id"),
									isi.getString("service_id"),
									isi.getString("nama_site"),
									isi.getString("alamat")
							));
						}
						isLoading = false;
						listView.removeFooterView(footerList);
						if (adapter != null) adapter.addMoreData(moreListPilihKostumer);
					} else {
						Toast.makeText(PilihKostumer.this, message, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				Toast.makeText(PilihKostumer.this, "Terjadi Kesalahan", Toast.LENGTH_LONG).show();
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
