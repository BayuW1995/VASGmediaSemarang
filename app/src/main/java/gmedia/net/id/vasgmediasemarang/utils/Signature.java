package gmedia.net.id.vasgmediasemarang.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.github.gcacace.signaturepad.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import gmedia.net.id.vasgmediasemarang.R;
import gmedia.net.id.vasgmediasemarang.menu_equipment.Equipment;

public class Signature extends AppCompatActivity {
	private SignaturePad signaturePad;
	private Button btnClear;
	private Button btnSave;
	private Proses proses;
	public static String idUploadGambarTTD = "";
	private SessionManager session;
	private RequestQueue requestQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signature);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setTitle("Draw Signature");
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setElevation(0);
		session = new SessionManager(this);
		proses = new Proses(this);
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
//				Toast.makeText(Signature.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
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
				Bitmap signatureBitmapTransparent = signaturePad.getTransparentSignatureBitmap(true);
				addJpgSignatureToGallery(signatureBitmapTransparent);
//				Bitmap signatureBitmapTransparent = signaturePad.getSignatureBitmap();
//				Bitmap signatureBitmapTransparent = signaturePad.getSignatureSvg();
				/*if (addSvgSignatureToGallery(signaturePad.getSignatureSvg())) {
					Toast.makeText(Signature.this, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(Signature.this, "Unable to store the signature", Toast.LENGTH_SHORT).show();
				}*/
			}
		});
		btnClear.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				signaturePad.clear();
			}
		});
	}

	/*private void saveImage(Bitmap bitmap, String filename) {

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		OutputStream outStream = null;

		File file = new File(filename);
		if (file.exists()) {
			file.delete();
			file = new File(extStorageDirectory, filename);
			Log.e("file exist", "" + file + ",Bitmap= " + filename);
		}
		try {
			// make a new bitmap from your file
			bitmap = BitmapFactory.decodeFile(file.getName());
			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
			System.out.println("closed");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}*/
	public boolean addSvgSignatureToGallery(String signatureSvg) {
		boolean result = false;
		try {
			File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
			OutputStream stream = new FileOutputStream(svgFile);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(signatureSvg);
			writer.close();
			stream.flush();
			stream.close();
			scanMediaFile(svgFile);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean addJpgSignatureToGallery(Bitmap signature) {
		boolean result = false;
		try {
			File outputDir = getCacheDir(); // context being the Activity pointer
			File outputFile = File.createTempFile("Signature", "Signature_%d.png", outputDir);
			saveBitmapToPNG(signature, outputFile);
			scanMediaFile(outputFile);
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
//		File photo = new File(String.format("Signature_%d.png", System.currentTimeMillis()));
//		File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.png", System.currentTimeMillis()));
		/*try {
			saveBitmapToPNG(signature, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*scanMediaFile(photo);
		result = true;*/
		return result;
	}

	private static Bitmap codec(Bitmap src, Bitmap.CompressFormat format,
								int quality) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		src.compress(format, quality, os);

		byte[] array = os.toByteArray();
		return BitmapFactory.decodeByteArray(array, 0, array.length);
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

	public void saveBitmapToPNG(Bitmap bitmap, File photo) throws IOException {
		Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//		newBitmap.eraseColor(Color.WHITE);
		Canvas canvas = new Canvas(newBitmap);
//		canvas.drawColor(Color.WHITE);
//		canvas.eras(Color.WHITE);
		canvas.drawBitmap(bitmap, 0, 0, null);
		OutputStream stream = new FileOutputStream(photo);
		newBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//		stream.flush();
		stream.close();
		uploadBitmap(getFileDataFromDrawable(newBitmap));
		Bitmap bitmapttd = Bitmap.createScaledBitmap(newBitmap, 120, 120, false);
		Equipment.bitmapTTD = bitmapttd;
	}

	public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

	private void uploadBitmap(final byte[] bitmap) {
		proses.ShowDialog();
		Equipment.idUploadTTD = "";
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Client-Service", "Gmedia_SUPPORTVAS");
		headers.put("Auth-Key", "frontend_client");
//		headers.put("Content-Type", "application/json");
		headers.put("User-Id", session.getKeyUserId());
		headers.put("Token", session.getKeyToken());

		VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, LinkURL.UrlUploadGambarTTDReportVAS, headers, new Response.Listener<NetworkResponse>() {
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
						Equipment.idUploadTTD = responseAPI.getString("id");
						Toast.makeText(Signature.this, message, Toast.LENGTH_SHORT).show();
						Equipment.signatureIsDone = true;
						onBackPressed();
					} else {
						Toast.makeText(Signature.this, message, Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				proses.DismissDialog();
				Toast.makeText(Signature.this, "Terjadi Kesalahan upload gambar", Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
				Map<String, DataPart> params = new HashMap<>();
				long imageName = System.currentTimeMillis();
				params.put("pic", new VolleyMultipartRequest.DataPart(imageName + ".png", bitmap));
				return params;
			}
		};
		if (requestQueue == null) {
			requestQueue = Volley.newRequestQueue(Signature.this);
		}

		// retry when timeout
		request.setRetryPolicy(new DefaultRetryPolicy(
				8 * 1000, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
		));
		request.setShouldCache(false);
		requestQueue.add(request);
		requestQueue.getCache().clear();
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
