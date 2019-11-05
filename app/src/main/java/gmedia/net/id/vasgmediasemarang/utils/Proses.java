package gmedia.net.id.vasgmediasemarang.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Window;

import gmedia.net.id.vasgmediasemarang.R;


public class Proses {
	private Dialog dialog;

	public Proses(Context context) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();

		int device_TotalWidth = metrics.widthPixels;
		int device_TotalHeight = metrics.heightPixels;

		dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.loading);

		if(dialog.getWindow() != null){
			dialog.getWindow().setLayout(device_TotalWidth * 70 / 100 , device_TotalHeight * 15 / 100); // set here your value
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		}
		dialog.setCancelable(false);
	}

	public void ShowDialog() {
		dialog.show();
	}

	public void DismissDialog() {
		dialog.dismiss();
	}

}
