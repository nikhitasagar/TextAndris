package com.hsc.textandris.ui;




import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.hsc.textandris.R;

public class AboutText extends Activity {
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView (R.layout.about);
			//Some Change here
	        WebView aboutView = (WebView) findViewById(R.id.about_view);
	        aboutView.loadUrl("file:///android_asset/about.html");
	 }
}