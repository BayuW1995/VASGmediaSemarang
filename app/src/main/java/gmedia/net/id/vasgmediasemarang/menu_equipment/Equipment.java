package gmedia.net.id.vasgmediasemarang.menu_equipment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.menu_equipment_child.ModelListEquipmentChild;
import gmedia.net.id.vasgmediasemarang.utils.Signature;

public class Equipment extends AppCompatActivity {
	private ListView listView;
	private ArrayList<ModelListEquipment> list;
	private ListAdapterEquipment adapter;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_equipment);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Equipment Room/Rack");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		initUI();
		initAction();
	}

	private void initUI() {
		listView = (ListView) findViewById(R.id.lv_equipment);
		btnCustSignature = (RelativeLayout) findViewById(R.id.btnCustSignature);
		btnCustImage = (RelativeLayout) findViewById(R.id.btnCustImage);
	}

	private void initAction() {
		list = new ArrayList<>();
		list = prepareDataEquipment();
		adapter = new ListAdapterEquipment(Equipment.this, list);
		listView.setAdapter(adapter);
		btnCustSignature.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
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
			Log.d("bmpFrontCamera", String.valueOf(photo));
		}
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

	private ArrayList<ModelListEquipment> prepareDataEquipment() {
		ArrayList<ModelListEquipment> rvData = new ArrayList<>();
		for (int i = 0; i < jenis.length; i++) {
			ModelListEquipment model = new ModelListEquipment();
			model.setJenis(jenis[i]);
			for (int j = 0; j < jenis_child.length; j++) {
				model.setList(new ModelListEquipmentChild(
						jenis_child[j]
				));
			}
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
