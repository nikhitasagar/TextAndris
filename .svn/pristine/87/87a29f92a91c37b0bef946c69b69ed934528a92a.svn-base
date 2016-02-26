package com.hsc.textandris.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hsc.textandris.R;
import com.hsc.textandris.constants.IConstants.DATE_TIME_PICKER_CONSTANTS;
import com.hsc.textandris.constants.IConstants.REPEAT_CONSTANTS;
import com.hsc.textandris.data.SmsStorageData;
import com.hsc.textandris.db.DatabaseHelper;

public class Scheduler extends Activity implements
AdapterView.OnItemSelectedListener{

	private TextView mStartDateDisplay, mEndDateDisplay;
	private Button mStartPickDate, mEndPickDate;
	private ImageButton saveBtn;
	private int mYear, mStartYear, mEndYear;
	private int mMonth, mStartMonth, mEndMonth;
	private int mDay, mStartDay,mEndDay ;
	private TextView mStartTimeDisplay, mEndTimeDisplay;
	private Button mStartPickTime, mEndPickTime;
	private Spinner frequency;
	private long startDateTime, endDateTime;

	private int mhour,mEndhour,mStarthour;
	private int mminute,mEndminute,mStartminute;
	private int selectedDateTimePicker;
	private int selectedFrequency;
	static final int TIME_DIALOG_ID = 1;

	static final int DATE_DIALOG_ID = 0;
	private String uniqueIDs, strMessageBox;
	private DatabaseHelper dbHelper;
	private static final int REQUEST_CODE = 10;
	private boolean isReady = false;
	private SmsStorageData mSmsStorageElement = null;
	private String AM_PM = "AM";
	private String mHrs;

	String[] items = { "Once", "Hourly", "Daily", "Weekly", 
			"Monthly", "Yearly" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scheduler_picker);

		// Contact name & phone no list

		uniqueIDs = this.getIntent().getStringExtra("uniqueIds");


		strMessageBox = this.getIntent().getStringExtra("Message_Box");

		mSmsStorageElement = (SmsStorageData) this.getIntent().getSerializableExtra("editElement");



		mStartDateDisplay =(TextView)findViewById(R.id.startDate);
		mStartPickDate =(Button)findViewById(R.id.startDatePicker);
		mStartTimeDisplay = (TextView) findViewById(R.id.startTime);
		mStartPickTime = (Button) findViewById(R.id.startTimePicker);

		mEndDateDisplay =(TextView)findViewById(R.id.endDate);
		mEndPickDate =(Button)findViewById(R.id.endDatePicker);
		mEndTimeDisplay = (TextView) findViewById(R.id.endTime);
		mEndPickTime = (Button) findViewById(R.id.endTimePicker);


		saveBtn = (ImageButton) findViewById(R.id.save);



		//Pick start time's click event listener
		//	mStartPickTime.setOnTimeChangedListener(mStartTimeChangedListener);
		mStartPickTime.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedDateTimePicker = DATE_TIME_PICKER_CONSTANTS.START_DATE_TIME_PICKER;
				showDialog(TIME_DIALOG_ID);
			}

		});

		//Pick start Date's click event listener 
		mStartPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedDateTimePicker = DATE_TIME_PICKER_CONSTANTS.START_DATE_TIME_PICKER;
				setCalendarDate();
				showDialog(DATE_DIALOG_ID);

			}
		});

		//Pick end time's click event listener
		mEndPickTime.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectedDateTimePicker = DATE_TIME_PICKER_CONSTANTS.END_DATE_TIME_PICKER;

				showDialog(TIME_DIALOG_ID);
			}

		});

		//Pick end Date's click event listener 
		mEndPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				selectedDateTimePicker = DATE_TIME_PICKER_CONSTANTS.END_DATE_TIME_PICKER;
				setCalendarDate();
				showDialog(DATE_DIALOG_ID);

			}
		});

		dbHelper = DatabaseHelper.getInstance(getBaseContext());

		// Spinner		
		frequency = (Spinner) findViewById(R.id.frequency);
		frequency.setOnItemSelectedListener(this);
		ArrayAdapter  dataAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,items);
		dataAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		frequency.setAdapter(dataAdapter);

		if(mSmsStorageElement != null){
			//Update the UI first
			Calendar cal = Calendar.getInstance();
			// Start DateTime
			cal.setTimeInMillis(mSmsStorageElement.startTime);
			mStartYear = cal.get(Calendar.YEAR);
			mStartMonth = cal.get(Calendar.MONTH);
			mStartDay = cal.get(Calendar.DATE);
			mStarthour = cal.get(Calendar.HOUR_OF_DAY);
			mStartminute = cal.get(Calendar.MINUTE);

			
			
			mStartDateDisplay.setText(new StringBuilder().append(mStartDay).append("/")
					.append(mStartMonth+1).append("/").append(mStartYear).append(" "));
			// To convert 24 hrs format to 12 hr			
			 mHrs = getHourIn12Format(mStarthour, mStartminute);

			mStartTimeDisplay.setText(new StringBuilder().append(mHrs)); 
			// End DateTime
			cal.setTimeInMillis(mSmsStorageElement.endTime);
			mEndYear = cal.get(Calendar.YEAR);
			mEndMonth = cal.get(Calendar.MONTH);
			mEndDay = cal.get(Calendar.DATE);
			mEndhour = cal.get(Calendar.HOUR_OF_DAY);
			mEndminute = cal.get(Calendar.MINUTE);

			mEndDateDisplay.setText(new StringBuilder().append(mEndDay).append("/")
					.append(mEndMonth+1).append("/").append(mEndYear).append(" "));

			// To convert 24 hrs format to 12 hr			
			mHrs = getHourIn12Format(mEndhour, mEndminute);
			mEndTimeDisplay.setText(new StringBuilder().append(mHrs));

			frequency.setSelection(mSmsStorageElement.repeatFactor - REPEAT_CONSTANTS.ONCE);

			//Set the values as well
			startDateTime = mSmsStorageElement.startTime;
			endDateTime = mSmsStorageElement.endTime;
			selectedFrequency = mSmsStorageElement.repeatFactor;
		}



	}


	//-------------------------------------------update date----------------------------------------//    
	private void updateDate(int selectedDatePicker) {

		if(selectedDatePicker == DATE_TIME_PICKER_CONSTANTS.END_DATE_TIME_PICKER)
		{
			mEndDateDisplay.setText(
					new StringBuilder()
					// Month is 0 based so add 1
					.append(mEndDay).append("/")
					.append(mEndMonth + 1).append("/")
					.append(mEndYear).append(" "));
			endDateTime = Date_to_MilliSeconds( mEndDay,  mEndMonth,  mEndYear,  mEndhour,  mEndminute);
		}
		else if(selectedDatePicker == DATE_TIME_PICKER_CONSTANTS.START_DATE_TIME_PICKER)
		{
			mStartDateDisplay.setText(
					new StringBuilder()
					// Month is 0 based so add 1
					.append(mStartDay).append("/")
					.append(mStartMonth + 1).append("/")
					.append(mStartYear).append(" "));
			startDateTime = Date_to_MilliSeconds( mStartDay,  mStartMonth,  mStartYear,  mStarthour,  mStartminute);

		}
		showDialog(TIME_DIALOG_ID);
		

	}

	//-------------------------------------------update time----------------------------------------//    
	public void updatetime(int selectedDatePicker)
	{
		
		String time ;
		if(selectedDatePicker == DATE_TIME_PICKER_CONSTANTS.END_DATE_TIME_PICKER)
		{
			
			endDateTime = Date_to_MilliSeconds( mEndDay,  mEndMonth,  mEndYear,  mEndhour,  mEndminute);
			System.out.println("endDateTime :"+ endDateTime);
			 time = pad(mEndhour)+":"+pad(mEndminute);
			
			
			 mHrs = getHourIn12Format(mEndhour, mEndminute);
			
			mEndTimeDisplay.setText(new StringBuilder().append(mHrs));
		
		}
		else if(selectedDatePicker == DATE_TIME_PICKER_CONSTANTS.START_DATE_TIME_PICKER)
		{
			startDateTime = Date_to_MilliSeconds( mStartDay,  mStartMonth,  mStartYear,  mStarthour,  mStartminute);
			System.out.println("startDateTime :"+ startDateTime);
		
			mHrs = getHourIn12Format(mStarthour, mStartminute);
			
			mStartTimeDisplay.setText(new StringBuilder().append(mHrs)); 

			
		}
	
	}
	
	private String getHourIn12Format(int  mhour, int mminute) {
		
		String str_return = "";

		  String timeSet = "";
	        if (mhour > 12) {
	        	mhour -= 12;
	            timeSet = "PM";
	        } else if (mhour == 0) {
	        	mhour += 12;
	            timeSet = "AM";
	        } else if (mhour == 12)
	            timeSet = "PM";
	        else
	            timeSet = "AM";
	 
	         
	        String minutes = "";
	        if (mminute < 10)
	            minutes = "0" + mminute;
	        else
	            minutes = String.valueOf(mminute);
		
	        str_return = mhour +":" +minutes+" "+timeSet;
		
		
	    return str_return;
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);

	}

	private void setCalendarDate()
	{

		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		mhour = c.get(Calendar.HOUR_OF_DAY);
		mminute = c.get(Calendar.MINUTE);
	
	}

	public long Date_to_MilliSeconds(int day, int month, int year, int hour, int minute){
		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, minute, 00);    
		long message = c.getTimeInMillis();

		System.out.println("time in millisec :"+message);
		return message;

	}


	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			if(selectedDateTimePicker == DATE_TIME_PICKER_CONSTANTS.START_DATE_TIME_PICKER)
			{
			mStartYear = year;
			mStartMonth = monthOfYear;
			mStartDay = dayOfMonth;
			}
			else if(selectedDateTimePicker == DATE_TIME_PICKER_CONSTANTS.END_DATE_TIME_PICKER)
			{
				mEndYear = year;
				mEndMonth = monthOfYear;
				mEndDay = dayOfMonth;
			}
			updateDate(selectedDateTimePicker);
		}

	};



	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if(selectedDateTimePicker == DATE_TIME_PICKER_CONSTANTS.START_DATE_TIME_PICKER)
			{
			mStarthour = hourOfDay;
			mStartminute = minute;
			}else if(selectedDateTimePicker == DATE_TIME_PICKER_CONSTANTS.END_DATE_TIME_PICKER)
			{
				mEndhour = hourOfDay;
				mEndminute = minute;
			}
			updatetime(selectedDateTimePicker);
			
		}		
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay)   


			{
				@Override
				public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
				{   
					System.out.println("----------onDateChanged()-----------"+mDay+"--"+dayOfMonth);
					System.out.println("----------onDateChanged()-----------"+mMonth+"--"+monthOfYear);
					System.out.println("----------onDateChanged()-----------"+mYear+"--"+year);

					//These lines of commented code used for only setting the maximum date on Date Picker.. 

					if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
						view.updateDate(mYear, mMonth, mDay);
					if (monthOfYear < mMonth && year == mYear )
						view.updateDate(mYear, mMonth, mDay);
					if (year < mYear)
						view.updateDate(mYear, mMonth, mDay);

					String dateOutput = String.format("Date Selected: %02d/%02d/%04d", 
							dayOfMonth, monthOfYear+1, year);                   

				}
			};
		case TIME_DIALOG_ID:

			return new TimePickerDialog(this,
					mTimeSetListener, mhour, mminute, false){

			};
		}
		return null;
	}


	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) 
	{
		String selectedFreuency = items[position];		
		selectedFrequency = getSelectedFrequency(selectedFreuency);
	}

	public void onNothingSelected(AdapterView<?> parent) {
		//selection.setText("");
		getSelectedFrequency("Once");
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

	public void time_zones_data(View V){

		// launch the new message screen activity
		Intent mIntent = new Intent(getBaseContext(),
				TimeZone_data.class);
		startActivity(mIntent);	 
	}	

	private boolean checkDateTimeEntry()
	{
		if ((!mStartDateDisplay.getText().equals("Date") && !mStartTimeDisplay.getText().equals("Time"))
				&&(!mEndDateDisplay.getText().equals("Date") && !mEndTimeDisplay.getText().equals("Time"))){ 

			isReady = true;
		} 

		//saveBtn.setEnabled(isReady);
		if(!isReady)
		{
			Toast.makeText(getApplicationContext(), "Save Failed : Please check the Start and End Date/Time", Toast.LENGTH_SHORT).show();
		}
		
		saveBtn.setBackgroundResource(R.drawable.savebutton);
		return isReady;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent data = new Intent();		
		setResult(Activity.RESULT_CANCELED,data); 

		finish();
	}


	public void saveMessage(View V){
		if(checkDateTimeEntry())
		{
		if(mSmsStorageElement == null){
			long createResult = -1;
			SmsStorageData smsDataElement = new SmsStorageData();
			smsDataElement.toNames = "";
			smsDataElement.toNumbers = "";

			smsDataElement.uniqueIDs = uniqueIDs;
			smsDataElement.messageContent = strMessageBox;

			smsDataElement.startTime = startDateTime;
			smsDataElement.endTime = endDateTime;		
			smsDataElement.repeatFactor =selectedFrequency;


			dbHelper.open();
			createResult = dbHelper.create(smsDataElement);
			dbHelper.close();

			if(createResult >=0){
				System.out.println("message saved");
				Toast.makeText(getApplicationContext(), "Message saved successfully.", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();		
				setResult(RESULT_OK,data); 

				finish();
			}else{
				Toast.makeText(getApplicationContext(), "Message can be scheduled only for future : Check Start and End Date/Time", Toast.LENGTH_SHORT).show();
			}

		}else{
			mSmsStorageElement.uniqueIDs = uniqueIDs;
			mSmsStorageElement.messageContent = strMessageBox;

			mSmsStorageElement.startTime = startDateTime;
			mSmsStorageElement.endTime = endDateTime;		
			mSmsStorageElement.repeatFactor =selectedFrequency;

			
			long currentTime = System.currentTimeMillis();
			
			if(startDateTime > currentTime){
				mSmsStorageElement.nextRepeatTime = startDateTime;
			}
			
			if((selectedFrequency!=REPEAT_CONSTANTS.ONCE && endDateTime>currentTime) || startDateTime>currentTime){
				mSmsStorageElement.isActive = true;
			}

			dbHelper.open();
			boolean createResult = dbHelper.update(mSmsStorageElement);
			dbHelper.close();
			
			if(createResult){
				System.out.println("message saved");
				Toast.makeText(getApplicationContext(), "Message saved successfully.", Toast.LENGTH_SHORT).show();
				Intent data = new Intent();		
				setResult(RESULT_OK,data); 

				finish();
				
				
			}else{
				Toast.makeText(getApplicationContext(), "Message can be scheduled only for future : Check Start and End Date/Time", Toast.LENGTH_SHORT).show();
			}
		}
	}

	}



}