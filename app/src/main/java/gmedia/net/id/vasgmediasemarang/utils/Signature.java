package gmedia.net.id.vasgmediasemarang.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import gmedia.net.id.vasgmediasemarang.R;

public class Signature extends AppCompatActivity {
	private SignaturePad signaturePad;
	private Button btnClear;
	private Button btnSave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Draw Signature");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		initUI();
		initAction();
	}

	private void initUI() {
		signaturePad = (SignaturePad) findViewById(R.id.signature_pad);
		btnClear = (Button) findViewById(R.id.clear_button);
		btnSave = (Button) findViewById(R.id.save_button);
	}

	private void initAction() {
		signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
			@Override
			public void onStartSigning() {
				Toast.makeText(Signature.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSigned() {
				btnSave.setEnabled(true);
				btnClear.setEnabled(true);
			}

			@Override
			public void onClear() {
				btnSave.setEnabled(false);
				btnClear.setEnabled(false);
			}
		});
		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bitmap signatureBitmap = signaturePad.getSignatureBitmap();
				if (addJpgSignatureToGallery(signatureBitmap)) {
					Toast.makeText(Signature.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Signature.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public File getAlbumStorageDir(String albumName) {
		// Get the directory for the user's public pictures directory.
		File file = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), albumName);
		if (!file.mkdirs()) {
			Log.e("SignaturePad", "Directory not created");
		}
		return file;
	}

	public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(newBitmap);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		OutputStream stream = new FileOutputStream(photo);
		newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
		stream.close();
	}

	public boolean addJpgSignatureToGallery(Bitmap signature) {
		boolean result = false;
		try {
			File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
			saveBitmapToJPG(signature, photo);
			scanMediaFile(photo);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void scanMediaFile(File photo) {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri contentUri = Uri.fromFile(photo);
		mediaScanIntent.setData(contentUri);
		Signature.this.sendBroadcast(mediaScanIntent);
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
