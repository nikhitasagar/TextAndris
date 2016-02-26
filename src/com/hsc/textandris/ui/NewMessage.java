package com.hsc.textandris.ui;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hsc.textandris.R;
import com.hsc.textandris.communication.SmsHelper;
import com.hsc.textandris.contacts.ContactUtility;
import com.hsc.textandris.contacts.ListArrayAdapter;
import com.hsc.textandris.data.SmsData;
import com.hsc.textandris.data.SmsStorageData;

public class NewMessage extends Activity {
	private static final int REQUEST_CODE = 10;
	
	ListView mListView;
	ListArrayAdapter adapter;
	TextView noContact;	
	private Set<String> smsIds = new HashSet<String>();
	private EditText messageBox;
	private SmsStorageData mSmsStorageElement = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.contacts_screen);
		
		Bundle extras = getIntent().getExtras(); 
		if(extras !=null) {
			mSmsStorageElement = (SmsStorageData)extras.getSerializable("editElement");
		    
		    
		    if(mSmsStorageElement.uniqueIDs != null && mSmsStorageElement.uniqueIDs.length()>0){
				String[] ids= mSmsStorageElement.uniqueIDs.split("\\s*,\\s*");
				for(int j = 0; j< ids.length; j++){
					smsIds.add(ids[j]);
				}
			}
		}
		//smsIds = prefs.getStringSet("smsList", new HashSet<String>());
		
		
		LinearLayout ll = (LinearLayout) findViewById(R.id.new_message_contacts_layout);
		ImageView addImage = (ImageView)ll.findViewById(R.id.addImage);
		addImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(smsIds != null && smsIds.size() >= 4){
					showAlert();
				}else{
				Intent intent = new Intent(Intent.ACTION_PICK);
		        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);				
				startActivityForResult(intent,1);
				}
			}
		});	
	
		messageBox = (EditText)findViewById(R.id.messagebox);
		
		if(mSmsStorageElement!=null && mSmsStorageElement.messageContent != null){
			messageBox.setText(mSmsStorageElement.messageContent);
		}
		
		noContact = (TextView)findViewById(R.id.nocontacts);
		
		mListView = (ListView) findViewById(R.id.smsList);
		
		try {
			//List<String> contactIdList = ContactUtility.getContactsInGroup(this);
			
			// Create an array of Strings, for List
			adapter = new ListArrayAdapter(this,
					ContactUtility.getModel(this,smsIds),smsIds);
			// Assign adapter to ListView
			mListView.setAdapter(adapter);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void showAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder.setTitle("TextAndris");
		alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		  });

		alertDialogBuilder
		.setMessage("SMS Group can not have more members.");
		AlertDialog alertDialog = alertDialogBuilder.create();
		 
		// show it
		alertDialog.show();

		
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			//List<String> contactIdList = ContactUtility.getContactsInGroup(this);
			// Create an array of Strings, for List
			adapter = new ListArrayAdapter(this,
					ContactUtility.getModel(this,smsIds),smsIds);
			// Assign adapter to ListView
			mListView.setAdapter(adapter);		
			
		} catch (Exception e) {
			//Log.d(LOG_TAG,"**** Exception: "+ e.getMessage());
		}
		
		
		if (mListView.getCount()==0){
			mListView.setVisibility(View.GONE);
			noContact.setVisibility(View.VISIBLE);
		}else{
			mListView.setVisibility(View.VISIBLE);
			noContact.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == Activity.RESULT_OK) {
				Cursor cursor = managedQuery(data.getData(), null, null, null,
						null);

				if (cursor != null && cursor.getCount() > 0) {
					cursor.moveToNext();
					/*String rawContactId = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));*/
					String uniqueId = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
					smsIds.add(uniqueId);
				}
			}
		}else if(requestCode==REQUEST_CODE && resultCode == Activity.RESULT_OK){
			setResult(Activity.RESULT_OK);
			finish();
		}
	}
	
	
	/**
	 * Send SMS
	 */
	public void sendNow(View V){

		if(smsIds == null || smsIds.size()==0||
				messageBox.getText().toString().length()==0){
			Toast.makeText(getApplicationContext(), "SMS sending failed : Check the To Numbers and Message Content",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Cursor cursor;

		// launch the new message screen activity
		try {
			SmsHelper smsHelper = SmsHelper.getInstance();
			SmsData smsData = new SmsData();
			smsData.message = messageBox.getText().toString();
			for(String uid : smsIds){
				ContentResolver cr = this.getContentResolver();
				cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
						, new String[]{ContactsContract.CommonDataKinds.Phone._ID,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER},
						ContactsContract.CommonDataKinds.Phone._ID+" =?" , new String[]{uid},
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " ASC");
				if (cursor != null && cursor.getCount()>0){
					cursor.moveToFirst();

					String number = cursor.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


					smsData.toNumbers += number+",";
				}else{
					continue ;

				}

			}

			if(smsData.toNumbers.trim().equals("")){
				Toast.makeText(getApplicationContext(), "SMS sending failed : Invalid Contact",
						Toast.LENGTH_SHORT).show();
				return;
			}
			
			smsHelper.sendMessage(smsData);
			Toast.makeText(getApplicationContext(), "Message Sent!",
					Toast.LENGTH_SHORT).show();
			Intent data = new Intent();		
			setResult(RESULT_OK,data); 

			finish();
		} catch (Exception e) {

			Toast.makeText(getApplicationContext(),
					"SMS sending failed : Network problem!",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace(); 
		}
		
	}
	
	/**
	 * Schedule SMS
	 */
	public void scheduleMessage(View v){
		if(smsIds == null || smsIds.size()==0||
				messageBox.getText().toString().length()==0){
			Toast.makeText(getApplicationContext(), "SMS scheduling failed : Check the To Numbers and Message Content",
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		String uidList = "";
		for(String uid : smsIds){
			uidList += uid + ",";
		}
			
		// launch the new message screen activity
		Intent mIntent = new Intent(getBaseContext(),Scheduler.class);

		mIntent.putExtra("uniqueIds", uidList);
		
		mIntent.putExtra("Message_Box", messageBox.getText().toString());
		
		if(mSmsStorageElement!=null && mSmsStorageElement.id >=0){
			mSmsStorageElement.uniqueIDs = uidList;
			mSmsStorageElement.messageContent = messageBox.getText().toString();
		}

		mIntent.putExtra("editElement", mSmsStorageElement);
		
		startActivityForResult(mIntent, REQUEST_CODE);	
	}
}