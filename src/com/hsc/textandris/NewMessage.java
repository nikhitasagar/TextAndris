package com.hsc.textandris;

import java.sql.Date;

import com.hsc.textandris.ui.log.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.devspark.appmsg.AppMsg;
import com.devspark.appmsg.AppMsg.Style;
import com.hsc.textandris.R;
import com.hsc.textandris.R.anim;
import com.hsc.textandris.R.array;
import com.hsc.textandris.R.color;
import com.hsc.textandris.R.id;
import com.hsc.textandris.R.layout;
import com.hsc.textandris.R.menu;
import com.hsc.textandris.constants.IConstants.REPEAT_CONSTANTS;
import com.hsc.textandris.contacts.ContactsEditText;
import com.hsc.textandris.data.SmsStorageData;
import com.hsc.textandris.db.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.opengl.Visibility;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewMessage extends ActionBarActivity {
	
	public static String date = "", time = "", name = "", interval = "", message = "";
	public static int repeat;
	static View rootView;
	public static int hour,minute, month,day,year,end_hour,end_minute, end_month,end_day,end_year;
	public static ArrayList<ContactsEditText.Contact> contactList;
	public ArrayList<LogsItem> results = new ArrayList<LogsItem>();
	public DatabaseHelper dbHelper;
	public List<SmsStorageData> dbList;
	public StringBuffer ContactList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_message);
		contactList = new ArrayList<ContactsEditText.Contact> ();
		repeat = -1;
		hour = -1;
		minute=-1;
		month=-1;
		day=-1;
		year=-1;
		end_hour = -1;
		end_minute=-1;
		end_month=-1;
		end_day=-1;
		end_year=-1;
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		dbHelper = DatabaseHelper.getInstance(getBaseContext());
	}
	
	
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.blank, R.anim.push_down);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.button_done && validate()) {
			long createResult = -1;
			SmsStorageData smsDataElement = new SmsStorageData();
			smsDataElement.uniqueIDs = "";
			
			for (ContactsEditText.Contact listElement : contactList){
				smsDataElement.uniqueIDs += listElement.id + ",";
				Log.d("ABC",listElement.lookupKey);
			}
			smsDataElement.uniqueIDs=smsDataElement.uniqueIDs.substring(0, smsDataElement.uniqueIDs.length()-1);
			Log.d("ABC",smsDataElement.uniqueIDs);

			for (LogsItem logData : getDBData()) {
				smsDataElement.toNames += logData.getName() + ",";
			}

			//smsDataElement.uniqueIDs = "123";
			smsDataElement.messageContent = ((EditText) findViewById(R.id.message)).getText().toString();

			Calendar start = Calendar.getInstance();
			start.set(year, month, day, hour, minute);
			smsDataElement.startTime = start.getTimeInMillis();
			Calendar end = Calendar.getInstance();
			end.set(end_year, end_month, end_day, end_hour, end_minute);
			smsDataElement.endTime = end.getTimeInMillis();
			//End time if repetition is once
			if(repeat == REPEAT_CONSTANTS.ONCE)smsDataElement.endTime = start.getTimeInMillis() + 120000;
			smsDataElement.repeatFactor = repeat;
			smsDataElement.isActive = true;

			DatabaseHelper dbHelper = DatabaseHelper.getInstance(getBaseContext());
			dbHelper.open();
			createResult = dbHelper.create(smsDataElement);
			dbHelper.close();

			if(createResult >=0){
				System.out.println("message saved");
				//Toast.makeText(getApplicationContext(), "Message saved successfully.", Toast.LENGTH_SHORT).show();
				Intent returnIntent = new Intent(this, MainActivity.class);
				returnIntent.putExtra("result","done");
				setResult(RESULT_OK,returnIntent);
				finish();
			} else {
				//Toast.makeText(getApplicationContext(), "Message can be scheduled only for future : Check Start and End Date/Time", Toast.LENGTH_SHORT).show();
				AppMsg.makeText(this, "Error saving message!", AppMsg.STYLE_ALERT).show();
			}
			closeKeyboard();
			return true;
		}	
		else if (id == R.id.button_discard) {
			Intent returnIntent = new Intent(this, MainActivity.class);
			setResult(RESULT_CANCELED,returnIntent);
			closeKeyboard();
			finish();
			return true;
		}
		
		else if (id == android.R.id.home) {
			Intent returnIntent = new Intent(this, MainActivity.class);
			setResult(RESULT_CANCELED,returnIntent);
			closeKeyboard();
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void closeKeyboard() {
		View v = this.getCurrentFocus();
		
	    if(v != null) {

		 InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		 imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	    }
	}

	
	
	
	public ArrayList<LogsItem> getDBData() {
        results.clear();
        
        if (dbHelper == null) {
        	 dbHelper = DatabaseHelper.getInstance(getBaseContext());
        }
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
	
	
	 public static String getDate(long milliSeconds, String dateFormat)
	    {
	        // Create a DateFormatter object for displaying date in specified format.
	        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

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
	
	
	
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		int selected = -1;
		AlertDialog ad;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_new_message,
					container, false);
			
			//Spinner spinner = (Spinner) rootView.findViewById(R.id.interval);
			//spinner1 = (Spinner) findViewById(R.id.spinner1);
			final Button button1 = (Button) rootView.findViewById(R.id.interval);

			final String[] intervals = getResources().getStringArray(R.array.interval_array);

			//  You can also use an adapter for the allert dialog if you'd like
			//  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, countries);		
			
			ad = new AlertDialog.Builder(rootView.getContext()).setSingleChoiceItems(intervals, selected,  
					new  DialogInterface.OnClickListener() {


							@Override
							public void onClick(DialogInterface dialog, int which) {
								button1.setText(intervals[which]);
								button1.setTextColor(getResources().getColor(R.color.black));
								selected = which;
								if(intervals[which].equals("Once")){
									rootView.findViewById(R.id.end_date).setVisibility(View.GONE);
									rootView.findViewById(R.id.end_time).setVisibility(View.GONE);
								}
								else{
									rootView.findViewById(R.id.end_date).setVisibility(View.VISIBLE);
									rootView.findViewById(R.id.end_time).setVisibility(View.VISIBLE);
								}
								repeat = getSelectedFrequency(intervals[which]);
								ad.dismiss();

							}
							
							private int getSelectedFrequency(String frequency)
							{
								int freqItem = 0;
								if(frequency.equals("Once"))
								{
									freqItem = REPEAT_CONSTANTS.ONCE;
								}else if(frequency.equals("Hourly"))
								{
									freqItem = REPEAT_CONSTANTS.HOURLY;
								}else if(frequency.equals("Daily"))
								{
									freqItem = REPEAT_CONSTANTS.DAILY;
								}else if(frequency.equals("Weekly"))
								{
									freqItem = REPEAT_CONSTANTS.WEEKLY;
								}else if(frequency.equals("Monthly"))
								{
									freqItem = REPEAT_CONSTANTS.MONTHLY;
								}else{
									freqItem = REPEAT_CONSTANTS.YEARLY;
								}
								return freqItem;

							}
			}).setTitle(R.string.hint_interval).create();	


			button1.setOnClickListener( new OnClickListener(){

				@Override
				public void onClick(View v) {
					ad.getListView().setSelection(selected);
					ad.show();				
				}});
			// Create an ArrayAdapter using the string array and a default spinner layout
			/*ArrayAdapter<CharSequence> spinnerad = ArrayAdapter.createFromResource(rootView.getContext(),
			        R.array.interval_array, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			spinnerad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			spinner.setAdapter(spinnerad);
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View view, 
			            int pos, long id) {
			        // An item was selected. You can retrieve the selected item using
			        // parent.getItemAtPosition(pos)
					if(parent.getItemAtPosition(pos).toString().equals("Once")){
						rootView.findViewById(R.id.end_date).setVisibility(View.GONE);
						rootView.findViewById(R.id.end_time).setVisibility(View.GONE);
					}
					else{
						rootView.findViewById(R.id.end_date).setVisibility(View.VISIBLE);
						rootView.findViewById(R.id.end_time).setVisibility(View.VISIBLE);
					}
					repeat = getSelectedFrequency(parent.getItemAtPosition(pos).toString());
			    }
				private int getSelectedFrequency(String frequency)
				{
					int freqItem = 0;
					if(frequency.equals("Once"))
					{
						freqItem = REPEAT_CONSTANTS.ONCE;
					}else if(frequency.equals("Hourly"))
					{
						freqItem = REPEAT_CONSTANTS.HOURLY;
					}else if(frequency.equals("Daily"))
					{
						freqItem = REPEAT_CONSTANTS.DAILY;
					}else if(frequency.equals("Weekly"))
					{
						freqItem = REPEAT_CONSTANTS.WEEKLY;
					}else if(frequency.equals("Monthly"))
					{
						freqItem = REPEAT_CONSTANTS.MONTHLY;
					}else{
						freqItem = REPEAT_CONSTANTS.YEARLY;
					}
					return freqItem;

				}

			    public void onNothingSelected(AdapterView<?> parent) {
			        // Another interface callback
			    }
			}); */
			
			message = String.valueOf(R.layout.fragment_new_message);
			final ContactsEditText editText = (ContactsEditText) rootView.findViewById(R.id.to);
			name = editText.getText().toString();
			
			editText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			    @Override
			    public void onItemClick(AdapterView<?> parent, View view, int position,
			            long id) {
			        ContactsEditText.Contact contact =
			                (ContactsEditText.Contact) parent.getItemAtPosition(position);

			        // Do something with the contact entry
			        contactList.add(contact);
			    }
			});
			return rootView;
		}
	}
	public boolean validate(){
		boolean name_e = contactList.size()>0;
		boolean message_e=((EditText)rootView.findViewById(R.id.message)).getText().length() > 0;
		Calendar st = Calendar.getInstance();
		st.set(year, month, day, hour, minute);
		Calendar en = Calendar.getInstance();
		en.set(end_year, end_month, end_day, end_hour, end_minute);
		boolean time_future_e = Calendar.getInstance().getTimeInMillis() < st.getTimeInMillis();
		boolean end_greater_e = repeat == REPEAT_CONSTANTS.ONCE||(en.getTimeInMillis() > st.getTimeInMillis());
		boolean time_e = hour >-1 && minute>-1;
		boolean end_time_e = repeat == REPEAT_CONSTANTS.ONCE||(end_hour >-1 && end_minute>-1);
		boolean date_e = year >-1 && day>-1 && month>-1;
		boolean end_date_e = repeat == REPEAT_CONSTANTS.ONCE||(end_year >-1 && end_day>-1 && end_month>-1);
		boolean repeat_e = repeat >= REPEAT_CONSTANTS.ONCE;
		String text = "";
		if(!end_greater_e){
			text = "The ending date and time should be after the starting date and time!";
		}
		if(!end_time_e){
			text = "Please enter an ending time!";
		}
		if(!end_date_e){
			text = "Please enter an ending date!";
		}
		if(!time_future_e){
			text = "Please enter a starting date and time in the future!";
		}
		if(!time_e){
			text = "Please enter a starting time!";
		}
		if(!date_e){
			text = "Please enter a starting date!";
		}
		if(!repeat_e){
			text = "Please select an interval!";
		}
		if(!message_e){
			text = "Please enter a message!";
		}
		if(!name_e){
			text = "Please add atleast one recepient!";
		}
		boolean condition = name_e && message_e && repeat_e && time_e && date_e && time_future_e && end_date_e && end_time_e && end_greater_e;
		if(!condition){
			AppMsg.makeText((Activity) rootView.getContext(), text, new AppMsg.Style(2000, R.color.alert)).show();
		}
		return condition;
		
	}
	static int time_id;
	public void showTimePickerDialog(View v) {
		time_id = v.getId();
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "timePicker");
	}
	
	public static class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
		if(minute==-1 && time_id==R.id.start_time){
		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		}
		else if(end_minute==-1 && time_id==R.id.end_time){
			final Calendar c = Calendar.getInstance();
			end_hour = c.get(Calendar.HOUR_OF_DAY);
			end_minute = c.get(Calendar.MINUTE);	
		}
		if(time_id==R.id.start_time){
		return new TimePickerDialog(getActivity(), this, hour, minute,
		DateFormat.is24HourFormat(getActivity()));
		}
		else{
			return new TimePickerDialog(getActivity(), this, end_hour, end_minute,
			DateFormat.is24HourFormat(getActivity()));
		}
		
		// Create a new instance of TimePickerDialog and return it
		
		}
		
		public void onTimeSet(TimePicker view, int h, int m) {
		// Do something with the time chosen by the user
			Button button = (Button)getActivity().findViewById(time_id);
			String timetext = "";
			String min = String.valueOf(m);
			if(m/10<1){min="0"+String.valueOf(m);}
			 if (!DateFormat.is24HourFormat(getActivity()))
			    {
				 if (h == 12) {
					 timetext += String.valueOf(h) + ":" + min + " PM";
				 }
				 else if (h == 0) {
					 timetext += String.valueOf(12) + ":" + min + " AM";
				 }
				 else if(h>12)
			       {
			    	   timetext += String.valueOf(Math.abs(12 - h)) + ":" + min + " PM";
			       } else {
			    	   timetext += String.valueOf(h) + ":" + min + " AM";
			       }
			    }
			  else {
				  timetext += String.valueOf(h) + ":" + min;     
			  }
			button.setText(timetext);
			date = button.getText().toString();
			button.setTextColor(getResources().getColor(R.color.black));
			if(time_id==R.id.start_time){
			minute = m;hour=h;
			}
			else{
				end_minute = m;end_hour=h;
			}
		}
	}
	
	public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
				// Use the current date as the default date in the picker
			if(month==-1 && date_id==R.id.start_date){
				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
				}
				else if(end_month==-1 && date_id==R.id.end_date){
					final Calendar c = Calendar.getInstance();
					end_year = c.get(Calendar.YEAR);
					end_month = c.get(Calendar.MONTH);
					end_day = c.get(Calendar.DAY_OF_MONTH);
				}
				if(date_id==R.id.start_date){
					return new DatePickerDialog(getActivity(), this, year, month, day);
				}
				else{
					return new DatePickerDialog(getActivity(), this, end_year, end_month, end_day);
				}
		}
		
		

		public void onDateSet(DatePicker view, int y, int m, int d) {
				// Do something with the date chosen by the user
			Button button = (Button)getActivity().findViewById(date_id);
	        button.setText(String.valueOf(d)+ordinal(d)+" "+textmonth(m+1)+" "+ String.valueOf(y));
	        date = button.getText().toString();
	        button.setTextColor(getResources().getColor(R.color.black));
			if(date_id==R.id.start_date){
				 year = y;month=m;day=d;
			}
			else{
				 end_year = y;end_month=m;end_day=d;
			}
	       
		}
		
		public String textmonth(int m){
			switch(m){
			case 1: return "January";
			case 2: return "February";
			case 3: return "March";
			case 4: return "April";
			case 5: return "May";
			case 6: return "June";
			case 7: return "July";
			case 8: return "August";
			case 9: return "September";
			case 10: return "October";
			case 11: return "November";
			case 12: return "December";
			default: return "";
			}
		}
		public String ordinal(int d){
			if((d >= 11) && (d <= 13)){
				return "th";
			}
			switch(d%10){
			case 1: return "st";
			case 2: return "nd";
			case 3: return "rd";
			default: return "th";
			}
		}
	}
	static int date_id;
	public void showDatePickerDialog(View v) {
		date_id = v.getId();
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	
	
}
