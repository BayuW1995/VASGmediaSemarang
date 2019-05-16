package gmedia.net.id.vasgmediasemarang.utils;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_detail_job_daily_ts.DetailJobDailyTs;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private String latitude = "", longitude = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Lokasi");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.layoutKuning)));
		if (android.os.Build.VERSION.SDK_INT >= 21) {
			Window window = MapsActivity.this.getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.notifBarKuning));
		}
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			latitude = bundle.getString("latitude");
			longitude = bundle.getString("longitude");
		}
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		// Add a marker in Sydney and move the camera
		LatLng sydney = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
		mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
		float zoomLevel = 16.0f; //This goes up to 21
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));
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
