package com.hsc.textandris.ui;




import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hsc.textandris.R;
import com.hsc.textandris.ui.log.Logs;

public class TextAndrisMainScreen extends Activity{
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	 }
	        
	 public void newMessage(View V){
		
		// launch the new message screen activity
			Intent mIntent = new Intent(getBaseContext(),
					NewMessage.class);
			startActivity(mIntent);	 
	 }
	 
	 public void aboutMessage(View V){
			
			// launch the new message screen activity
				Intent mIntent = new Intent(getBaseContext(),
						AboutText.class);
				startActivity(mIntent);	 
	}
	 public void logLayout(View V){
			
			// launch the new message screen activity
				Intent mIntent = new Intent(getBaseContext(),
						Logs.class);
				startActivity(mIntent);	 
	}
	 }
