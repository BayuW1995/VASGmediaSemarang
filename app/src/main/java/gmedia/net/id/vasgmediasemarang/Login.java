package gmedia.net.id.vasgmediasemarang;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import gmedia.net.id.vasgmediasemarang.utils.ApiVolley;
import gmedia.net.id.vasgmediasemarang.utils.LinkURL;
import gmedia.net.id.vasgmediasemarang.utils.Proses;
import gmedia.net.id.vasgmediasemarang.utils.SessionManager;

public class Login extends AppCompatActivity {

	private RelativeLayout btnLogin;
	private EditText username, password;
	private Proses proses;
	private SessionManager session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		session = new SessionManager(this);
		session.checkLogin();
		proses = new Proses(Login.this);
		initUI();
		initAction();
	}

	private void initUI() {
		username = (EditText) findViewById(R.id.etUsernameLogin);
		password = (EditText) findViewById(R.id.etPasswordLogin);
		btnLogin = (RelativeLayout) findViewById(R.id.btnLogin);
	}

	private void initAction() {
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (username.getText().toString().equals("")) {
					username.setError("Mohon Di isi");
					username.requestFocus();
					return;
				} else if (password.getText().toString().equals("")) {
					password.setError("Mohon Di isi");
					username.requestFocus();
					return;
				} else {
					prepareLogin();
					Intent intent = new Intent(Login.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	private void prepareLogin() {
		proses.ShowDialog();
		final JSONObject jBody = new JSONObject();
		try {
			jBody.put("username", username.getText().toString());
			jBody.put("password", password.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ApiVolley request = new ApiVolley(Login.this, jBody, "POST", LinkURL.UrlLogin, "", "", 0, new ApiVolley.VolleyCallback() {
			@Override
			public void onSuccess(String result) {
				proses.DismissDialog();
				try {
					JSONObject object = new JSONObject(result);
					String status = object.getJSONObject("metadata").getString("status");
					String message = object.getJSONObject("metadata").getString("message");
					if (status.equals("200")) {
						String userId = object.getJSONObject("response").getString("nip");
						String nama = object.getJSONObject("response").getString("nama");
						String token = object.getJSONObject("response").getString("token");
						session.createLoginSession(userId, nama, token);
						Intent intent = new Intent(Login.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						Toast.makeText(Login.this, message, Toast.LENGTH_LONG);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onError(String result) {
				proses.DismissDialog();
				Toast.makeText(Login.this, "terjadi kesalahan", Toast.LENGTH_LONG).show();
			}
		});
	}
}
