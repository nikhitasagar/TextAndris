package com.hsc.textandris.ui.log;



import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hsc.textandris.R;
import com.hsc.textandris.constants.IConstants.REPEAT_CONSTANTS;
import com.hsc.textandris.data.SmsStorageData;
import com.hsc.textandris.db.DatabaseHelper;



public class Logs extends Activity {
	
	private DatabaseHelper dbHelper;
	 public Button deleteBtn;
	 public ArrayList<LogsItem> selectedDeleteList = new ArrayList<LogsItem>();
	 ArrayList<LogsItem> results = new ArrayList<LogsItem>();
	 List<SmsStorageData> dbList;
	 public CustomListAdapter logsAdapter;
	 StringBuffer ContactList;
	 
	 public ListView lv1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logs);
        dbHelper = DatabaseHelper.getInstance(getBaseContext());
        deleteBtn = (Button) findViewById( R.id.delete);
        
        deleteBtn.setBackgroundResource(R.drawable.discard_grey_xl);
 
        
        lv1 = (ListView) findViewById(R.id.custom_list);
        getDBData();
        logsAdapter = new CustomListAdapter(this, results, deleteBtn, selectedDeleteList);
       lv1.setAdapter(logsAdapter);
        
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if (dbHelper == null) {
            		 dbHelper = DatabaseHelper.getInstance(getBaseContext());
            	}
            	dbHelper.open(); 
//            	SmsStorageData smsDataElement = new SmsStorageData();
            	for(int i = 0; i< selectedDeleteList.size();i++)
            	{
            	    
            	   
            	    dbHelper.delete(selectedDeleteList.get(i).getId());
            	    
            	}
            	dbHelper.close();
            	getDBData();
            	
            	Runnable run = new Runnable(){
            	     public void run(){
            	    	 refresh();
            	     }
            	};
            	runOnUiThread(run);
            }
        }); 
      
 
    }
    
    private void refresh(){
    	logsAdapter.notifyDataSetChanged();
    	lv1.invalidate();
    	deleteBtn.setBackgroundResource(R.drawable.discard_grey_xl);
    }
    
    
    @Override
    public void onResume() {
            super.onResume();
            getDBData();
            logsAdapter.notifyDataSetChanged();
            lv1.invalidate();
    }
 
    public ArrayList<LogsItem> getDBData() {
        results.clear();
        
   
        dbHelper.open();
        dbList = dbHelper.readAll();
        dbHelper.close();
        String strContactName;
        
        for(int i = 0; i< dbList.size(); i++)
        {
        	LogsItem logsData = new LogsItem();
        	 ContactList = new StringBuffer();
        
        	String dbData = dbList.get(i).uniqueIDs;
        	
        	String delimiter = ",";
        	 
        	  String[] dataList = dbData.split(delimiter);

        	  for(int j =0; j < dataList.length ; j++)
        	  {
        	    System.out.println(dataList[j]);
        	    strContactName =  getList(dataList[j]);
        	    ContactList = ContactList.append(strContactName);
        	    if(j<dataList.length-1)
        	    ContactList = ContactList.append(",");
        	  
        	  }   
        	
        	logsData.setId(dbList.get(i).id);
        	logsData.setName(ContactList.toString());
        	logsData.setMessageText(dbList.get(i).messageContent);
        	logsData.setRepeat(getFrequency(dbList.get(i).repeatFactor));        	 
        	logsData.setDate(getDate(dbList.get(i).nextRepeatTime, "dd/MM/yyyy HH:mm"));
        	logsData.setActive(dbList.get(i).isActive);
        	results.add(logsData);
        }
        
     
        
     return results;
    }
    
    
    private String getList(String smsId)
    {
    	ContentResolver cr = getBaseContext().getContentResolver();
		Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
				, new String[]{ContactsContract.CommonDataKinds.Phone._ID,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER},
				ContactsContract.CommonDataKinds.Phone._ID+" =?" , new String[]{smsId},
				ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
						+ " ASC");
		if (cursor != null && cursor.getCount()>0){
			cursor.moveToFirst();
			String name = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			/*String number = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));*/
			/*String id = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));*/
			
			
			return name;		
			
		}else{			
			return "";			
		}
    }
    
    
    
    
    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format 
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date. 
         Calendar calendar = Calendar.getInstance();
         calendar.setTimeInMillis(milliSeconds);
         return formatter.format(calendar.getTime());
    }
    
    private String getFrequency(int mRepeat)
    {
    	String strRepeat = null;
    	 if(mRepeat == REPEAT_CONSTANTS.ONCE)
         {
    		 strRepeat = "Once";
         }
    	 else if(mRepeat == REPEAT_CONSTANTS.HOURLY)
         {
    		 strRepeat = "Hourly";
         }
    	 else if(mRepeat == REPEAT_CONSTANTS.DAILY)
         {
    		 strRepeat = "Daily";
         }
    	 else if(mRepeat == REPEAT_CONSTANTS.WEEKLY)
         {
    		 strRepeat = "Weekly";
         }
    	 else if(mRepeat == REPEAT_CONSTANTS.MONTHLY)
         {
    		 strRepeat = "Monthly";
         }
    	 else if(mRepeat == REPEAT_CONSTANTS.YEARLY)
         {
    		 strRepeat = "Yearly";
         }

    	 return  strRepeat;
        
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if(requestCode==100 && resultCode == Activity.RESULT_OK){
    		Runnable run = new Runnable(){
    			public void run(){
    				getDBData();
    				refresh();
    			}
    		};
    		runOnUiThread(run);
    		/*Toast.makeText(getApplicationContext(), "SMS Edited",
					Toast.LENGTH_SHORT).show();*/
    	}
    }
}