package gmedia.net.id.vasgmediasemarang;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.RuntimePermissionsActivity;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;

public class MainActivity extends RuntimePermissionsActivity
		implements NavigationView.OnNavigationItemSelectedListener {
	private Fragment fragment;
	private RelativeLayout btnDrawer;
	private NavigationView navigationView;
	private TextView title;
	private DrawerLayout drawer;
	public static boolean posisi = true;
	private static final int REQUEST_PERMISSIONS = 20;
	private ListView listView;
	private ListAdapterMenuDrawer adapter;
	private ArrayList<ModelListMenuDrawer> listMenuDrawers;
	private Integer imageMenu[] =
			{
					R.drawable.icon_menu_home_menu_drawer,
					//R.drawable.icon_daily_job_menu_drawer,
//					R.drawable.icon_history_survei_menu_drawer,
					//R.drawable.icon_daily_job_menu_drawer,
//					R.drawable.icon_history_survei_menu_drawer,
					R.drawable.icon_logout_menu_drawer
			};
	private String teksMenu[] =
			{
					"Home",
					//"Daily Job Vas",
//					"History Survei VAS",
					//"Daily Job TS",
//					"History Survei TS",
					"Logout"
			};
	private RelativeLayout buttonSettting;
	private EditText passLama, passBaru, rePassBaru;
	private Boolean klikToVisiblePassLama = true;
	private Boolean klikToVisiblePassBaru = true;
	private Boolean klikToVisibleRePassBaru = true;
	private String token = "";
	private SessionManager session;
	private boolean doubleBackToExitPressedOnce = false;
	private Proses proses;
	private String version, latestVersion, link;
	private boolean updateRequired;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		FirebaseApp.initializeApp(MainActivity.this);
		FirebaseMessaging.getInstance().subscribeToTopic("ontime");
		token = FirebaseInstanceId.getInstance().getToken();
		try {
			Log.d("token", token);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d("token", e.getMessage());
		}
		session = new SessionManager(this);
		proses = new Proses(this);
		initPermission();
		checkVersion();
		initUI();
		initAction();
	}

	private void checkVersion() {

		PackageInfo pInfo = null;
		version = "";

		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		version = pInfo.versionName;
//        getSupportActionBar().setSubtitle(getResources().getString(R.string.app_name) + " v "+ version);
		latestVersion = "";
		link = "";

		ApiVolley request = new ApiVolley(MainActivity.this, new JSONObject(), "GET", LinkURL.UrlUpVersion, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {

				JSONObject responseAPI;
				try {
					responseAPI = new JSONObject(result);
					String status = responseAPI.getJSONObject("metadata").getString("status");

					if (status.equals("200")) {
						latestVersion = responseAPI.getJSONObject("response").getString("version");
						link = responseAPI.getJSONObject("response").getString("playstore_url");
						updateRequired = ((responseAPI.getJSONObject("response").getString("is_required")).equals("1")) ? true : false;
						if (!version.trim().equals(latestVersion.trim()) && link.length() > 0) {
							final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
							if (updateRequired) {
								builder.setIcon(R.mipmap.ic_launcher)
										.setTitle("Update")
										.setMessage("Versi terbaru " + latestVersion + " telah tersedia, mohon download versi terbaru.")
										.setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
												startActivity(browserIntent);
											}
										})
										.setCancelable(false)
										.show();
							} else {
								builder.setIcon(R.mipmap.ic_launcher)
										.setTitle("Update")
										.setMessage("Versi terbaru " + latestVersion + " telah tersedia, mohon download versi terbaru.")
										.setPositiveButton("Update Sekarang", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
												startActivity(browserIntent);
											}
										})
										.setNegativeButton("Update Nanti", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												dialog.dismiss();
											}
										}).show();
							}
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				Toast.makeText(getApplicationContext(), "Terjadi kesalahan saat memuat data", Toast.LENGTH_LONG).show();
			}
		});
	}

	private void initPermission() {
		if (ActivityCompat.checkSelfPermission(this,
				Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
				ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

			super.requestAppPermissions(new String[]{
							Manifest.permission.READ_EXTERNAL_STORAGE,
							Manifest.permission.WRITE_EXTERNAL_STORAGE,
							Manifest.permission.CALL_PHONE,
							Manifest.permission.CAMERA,
					},
					R.string.runtime_permissions_txt, REQUEST_PERMISSIONS);
		}
	}

	private void initUI() {
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		btnDrawer = (RelativeLayout) findViewById(R.id.drawer_button);
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		title = (TextView) findViewById(R.id.title);
		listView = (ListView) findViewById(R.id.lv_menu_drawer);
		buttonSettting = (RelativeLayout) findViewById(R.id.setting);
	}

	private void initAction() {
		if (fragment == null) {
			fragment = new Home();
			callFragment(fragment);
			title.setText("Home");
			drawer.closeDrawer(GravityCompat.START);
			posisi = true;
		}
		btnDrawer.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				drawer.openDrawer(GravityCompat.START);
			}
		});
		navigationView.setNavigationItemSelectedListener(this);
		listMenuDrawers = new ArrayList<>();
		listMenuDrawers = prepareDataMenuDrawer();
		adapter = new ListAdapterMenuDrawer(MainActivity.this, listMenuDrawers);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						Intent intentHome = new Intent(MainActivity.this, MainActivity.class);
						startActivity(intentHome);
						finish();
						drawer.closeDrawer(GravityCompat.START);
						break;
					/*case 1:
						Intent intentJobDailyVas = new Intent(MainActivity.this, JobDailyVAS.class);
						startActivity(intentJobDailyVas);
						drawer.closeDrawer(GravityCompat.START);
						break;*/
					/*case 2:
						Intent intentHistoryVAS = new Intent(MainActivity.this, HistoryVAS.class);
						*//*intentHistoryVAS.addCategory(Intent.CATEGORY_HOME);
						intentHistoryVAS.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*//*
						startActivity(intentHistoryVAS);
						drawer.closeDrawer(GravityCompat.START);
						break;*/
					/*case 2:
						Intent intentJobDailyTS = new Intent(MainActivity.this, JobDailyTS.class);
						startActivity(intentJobDailyTS);
						drawer.closeDrawer(GravityCompat.START);
						break;*/
					/*case 4:
						Intent intentHistoryTS = new Intent(MainActivity.this, HistoryTS.class);
						*//*intentHistoryTS.addCategory(Intent.CATEGORY_HOME);
						intentHistoryTS.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*//*
						startActivity(intentHistoryTS);
						drawer.closeDrawer(GravityCompat.START);
						break;*/
					case 1:
						session.logoutUser();
						drawer.closeDrawer(GravityCompat.START);
						break;
				}
			}
		});

		buttonSettting.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//                showPopupMenu(buttonSettting);
				final Dialog dialog = new Dialog(MainActivity.this);
				dialog.setContentView(R.layout.popup_ganti_password);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				final ImageView visiblePassLama = dialog.findViewById(R.id.visiblePassLama);
				final ImageView visiblePassBaru = dialog.findViewById(R.id.visiblePassBaru);
				final ImageView visibleRePassBaru = dialog.findViewById(R.id.visibleRePassBaru);
				passLama = dialog.findViewById(R.id.passLama);
				passBaru = dialog.findViewById(R.id.passBaru);
				rePassBaru = dialog.findViewById(R.id.reTypePassBaru);
				visiblePassLama.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (klikToVisiblePassLama) {
							visiblePassLama.setImageDrawable(getResources().getDrawable(R.drawable.visible));
							passLama.setInputType(InputType.TYPE_CLASS_TEXT);
							klikToVisiblePassLama = false;
						} else {
							visiblePassLama.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
							passLama.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							passLama.setTransformationMethod(PasswordTransformationMethod.getInstance());
							klikToVisiblePassLama = true;
						}
					}
				});
				visiblePassBaru.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (klikToVisiblePassBaru) {
							visiblePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.visible));
							passBaru.setInputType(InputType.TYPE_CLASS_TEXT);
							klikToVisiblePassBaru = false;
						} else {
							visiblePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
							passBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							passBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
							klikToVisiblePassBaru = true;
						}
					}
				});
				visibleRePassBaru.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (klikToVisibleRePassBaru) {
							visibleRePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.visible));
							rePassBaru.setInputType(InputType.TYPE_CLASS_TEXT);
							klikToVisibleRePassBaru = false;
						} else {
							visibleRePassBaru.setImageDrawable(getResources().getDrawable(R.drawable.invisible));
							rePassBaru.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
							rePassBaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
							klikToVisibleRePassBaru = true;
						}
					}
				});
				RelativeLayout OK = dialog.findViewById(R.id.tombolOKgantiPassword);
				OK.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						// validasi
						if (passLama.getText().toString().isEmpty()) {
							passLama.setError("Password lama harap diisi");
							passLama.requestFocus();
							return;
						} else {
							passLama.setError(null);
						}

						if (passBaru.getText().toString().isEmpty()) {
							passBaru.setError("Password baru harap diisi");
							passBaru.requestFocus();
							return;
						} else {
							passBaru.setError(null);
						}

						if (rePassBaru.getText().toString().isEmpty()) {
							rePassBaru.setError("Password baru ulang harap diisi");
							rePassBaru.requestFocus();
							return;
						} else {
							rePassBaru.setError(null);
						}
						if (!rePassBaru.getText().toString().equals(passBaru.getText().toString())) {
							rePassBaru.setError("Password ulang tidak sama");
							rePassBaru.requestFocus();
							return;
						} else {
							rePassBaru.setError(null);
						}
//						prepareDataGantiPassword();
						dialog.dismiss();
					}
				});
				RelativeLayout cancel = dialog.findViewById(R.id.tombolcancelGantiPassword);
				cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
                /*if (popup) {
                    popupmenu.setVisibility(View.VISIBLE);
                    popup = false;
                } else {
                    popupmenu.setVisibility(View.GONE);
                    popup = true;
                }*/
			}
		});
	}

	private ArrayList<ModelListMenuDrawer> prepareDataMenuDrawer() {
		ArrayList<ModelListMenuDrawer> rvData = new ArrayList<>();
		for (int i = 0; i < imageMenu.length; i++) {
			ModelListMenuDrawer model = new ModelListMenuDrawer();
			model.setIcon(imageMenu[i]);
			model.setTextIcon(teksMenu[i]);
			rvData.add(model);
		}
		return rvData;
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (posisi) {
			if (drawer.isDrawerOpen(GravityCompat.START)) {
				drawer.closeDrawer(GravityCompat.START);
			} else {
				if (doubleBackToExitPressedOnce) {
					Intent startMain = new Intent(Intent.ACTION_MAIN);
					startMain.addCategory(Intent.CATEGORY_HOME);
					startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(startMain);
				}
				doubleBackToExitPressedOnce = true;
				Toast.makeText(this, "Klik sekali lagi untuk keluar", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						doubleBackToExitPressedOnce = false;
					}
				}, 2000);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();

		if (id == R.id.nav_camera) {
			// Handle the camera action
		} else if (id == R.id.nav_gallery) {

		} else if (id == R.id.nav_slideshow) {

		} else if (id == R.id.nav_manage) {

		} else if (id == R.id.nav_share) {

		} else if (id == R.id.nav_send) {

		}
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (fragment == null) {
			fragment = new Home();
			callFragment(fragment);
			title.setText("");
			drawer.closeDrawer(GravityCompat.START);
			posisi = true;
		}
	}

	private void callFragment(Fragment fragment) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.mainLayout, fragment, fragment.getClass().getSimpleName())
				.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
				.addToBackStack(null)
				.commit();
	}

	@Override
	public void onPermissionsGranted(int requestCode) {

	}
}
