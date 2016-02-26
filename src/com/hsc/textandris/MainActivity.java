package com.hsc.textandris;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.devspark.appmsg.AppMsg;
import com.hsc.textandris.R;
import com.hsc.textandris.R.anim;
import com.hsc.textandris.R.id;
import com.hsc.textandris.R.layout;
import com.hsc.textandris.R.menu;
import com.hsc.textandris.R.string;
import com.hsc.textandris.constants.IConstants.REPEAT_CONSTANTS;
import com.hsc.textandris.data.EnhancedListView;
import com.hsc.textandris.data.EnhancedListView.SwipeDirection;
import com.hsc.textandris.data.SmsStorageData;
import com.hsc.textandris.data.EnhancedListView.OnDismissCallback;
import com.hsc.textandris.db.DatabaseHelper;
import com.hsc.textandris.ui.log.CustomListAdapter;
import com.hsc.textandris.ui.log.LogsItem;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a {@link FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this
	 * becomes too memory intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	public static CustomListAdapter logsAdapter, logsAdapter2; 
	public static  ArrayList<LogsItem> results = new ArrayList<LogsItem>();
	public static  ArrayList<LogsItem> results2 = new ArrayList<LogsItem>();
	public static DatabaseHelper dbHelper;
	public static List<SmsStorageData> dbList;
	public static StringBuffer contactList;
	public static View rootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			openSettings();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
 
	private void openSettings() {
	Toast.makeText(this, "SETTINGS", Toast.LENGTH_SHORT).show();
	startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class
			// below).
			return PlaceholderFragment.newInstance(position + 1);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void add (View view) {
		Intent intent = new Intent(this, NewMessage.class);
		startActivityForResult(intent, 1);
		overridePendingTransition(R.anim.push_up, R.anim.blank);	
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	            String result=data.getStringExtra("result");
	            if(result.equals("done")){
					Toast.makeText(getApplicationContext(), "Message successfully saved", Toast.LENGTH_SHORT).show();
	        		if(logsAdapter != null) {
	        	        getDBData();
	        	       logsAdapter.notifyDataSetChanged();
	            }
	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	        }
	    }
	        
	}//onActivityResult

		
		
		
		public static ArrayList<LogsItem> getDBData() {
	        results.clear();
	        
	   
	        dbHelper.open();
	        dbList = dbHelper.readAll();
	        dbHelper.close();
	        String strContactName;
	        
	        for(int i = 0; i< dbList.size(); i++)
	        {
	        	if(dbList.get(i).isActive==true){
	        	LogsItem logsData = new LogsItem();
	        	 contactList = new StringBuffer();
	        
	        	String dbData = dbList.get(i).uniqueIDs;
	        	
	        	String delimiter = ",";
	        	 
	        	  String[] dataList = dbData.split(delimiter);

	        	  for(int j =0; j < dataList.length ; j++)
	        	  {
	        	    System.out.println(dataList[j]);
	        	    strContactName =  getList(dataList[j]);
	        	    contactList = contactList.append(strContactName);
	        	    if(j<dataList.length-1)
	        	    contactList = contactList.append(",");
	        	  
	        	  }   
	        	
	        	logsData.setId(dbList.get(i).id);
	        	logsData.setName(contactList.toString());
	        	logsData.setMessageText(dbList.get(i).messageContent);
	        	logsData.setRepeat(getFrequency(dbList.get(i).repeatFactor));        	 
	        	logsData.setDate(getDate(dbList.get(i).nextRepeatTime, "dd/MM/yyyy HH:mm"));
	        	logsData.setActive(dbList.get(i).isActive);
	        	results.add(logsData);
	        }
	        }
	        
	        return results;
	       }
		
			
		
		
		public static ArrayList<LogsItem> getDBData2() {
	        results2.clear();
	        
	   
	        dbHelper.open();
	        dbList = dbHelper.readAll();
	        dbHelper.close();
	        String strContactName;
	        
	        for(int i = 0; i< dbList.size(); i++)
	        {
	        	if(dbList.get(i).isActive==false){
	        	LogsItem logsData = new LogsItem();
	        	 contactList = new StringBuffer();
	        
	        	String dbData = dbList.get(i).uniqueIDs;
	        	
	        	String delimiter = ",";
	        	 
	        	  String[] dataList = dbData.split(delimiter);

	        	  for(int j =0; j < dataList.length ; j++)
	        	  {
	        	    System.out.println(dataList[j]);
	        	    strContactName =  getList(dataList[j]);
	        	    contactList = contactList.append(strContactName);
	        	    if(j<dataList.length-1)
	        	    contactList = contactList.append(",");
	        	  
	        	  }   
	        	
	        	logsData.setId(dbList.get(i).id);
	        	logsData.setName(contactList.toString());
	        	logsData.setMessageText(dbList.get(i).messageContent);
	        	logsData.setRepeat(getFrequency(dbList.get(i).repeatFactor));        	 
	        	logsData.setDate(getDate(dbList.get(i).nextRepeatTime, "dd/MM/yyyy HH:mm"));
	        	logsData.setActive(dbList.get(i).isActive);
	        	results2.add(logsData);
	        	}
	        }
	        
	        return results2;
	       }
		
		public static String getList(String smsId)
	    {
	    	ContentResolver cr = rootView.getContext().getContentResolver();
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
		
		public static String getFrequency(int mRepeat)
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
		
		public static String getDate(long milliSeconds, String dateFormat)
	    {
	        // Create a DateFormatter object for displaying date in specified format.
	        DateFormat formatter = new SimpleDateFormat(dateFormat);

	        // Create a calendar object that will convert the date and time value in milliseconds to date. 
	         Calendar calendar = Calendar.getInstance();
	         calendar.setTimeInMillis(milliSeconds);
	         return formatter.format(calendar.getTime());
	    }
		
	public static class PlaceholderFragment extends Fragment {
		private SimpleCursorAdapter dataAdapter;
		
		
		 public Button deleteBtn;
		 public ArrayList<LogsItem> selectedDeleteList = new ArrayList<LogsItem>();

		 StringBuffer contactList;
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			switch (getArguments().getInt(ARG_SECTION_NUMBER)){
			
			case 1:
						rootView = inflater.inflate(R.layout.fragment_main_tab1, container,
							false);
		
						final EnhancedListView list1 = (EnhancedListView) rootView.findViewById(R.id.listview1);
					
						//list1.setSwipeDirection(SwipeDirection.START);
						dbHelper = DatabaseHelper.getInstance(rootView.getContext());
				        getDBData();
				        logsAdapter = new CustomListAdapter(rootView.getContext(), results, deleteBtn, selectedDeleteList);
				       list1.setAdapter(logsAdapter);
				       list1.setDismissCallback(new OnDismissCallback() {

							  @Override public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

							    // Store the item for later undo
							    /*final Person item = (Person) mAdapter.getItem(position);
							    // Remove the item from the adapter
							    mAdapter.remove(position);*/
						    	  /*final LinearLayout item = (LinearLayout)list1.getChildAt(position);
						    	  item.setVisibility(View.GONE);*/
								  final int itemid = (int) logsAdapter.getItemId(position);
								  dbHelper.open();
						    	  //dbHelper.delete((int) logsAdapter.getItemId(position));
						    	  SmsStorageData item = dbHelper.readThis(itemid);
						    	  item.isActive = false;
						    	  dbHelper.update(item);
						    	  dbHelper.close();
						    	  getDBData();
						    	  getDBData2();
						    	  logsAdapter.notifyDataSetChanged();
						    	  logsAdapter2.notifyDataSetChanged();
							    // return an Undoable
							    return new EnhancedListView.Undoable() {
							      // Reinsert the item to the adapter
							      @Override public void undo() {
							        //mAdapter.insert(position, item);
							    	  /*item.setVisibility(View.VISIBLE);
							    	  TranslateAnimation animate = new TranslateAnimation(-item.getWidth(),0,0,0);
							    	  animate.setDuration(200);
							    	  //animate.setFillAfter(true);
							    	  item.startAnimation(animate);*/
							    	  dbHelper.open();
							    	  //dbHelper.delete((int) logsAdapter.getItemId(position));
							    	  SmsStorageData item = dbHelper.readThis(itemid);
							    	  item.isActive = true;
							    	  dbHelper.update(item);
							    	  dbHelper.close();
							    	  getDBData();
							    	  getDBData2();
							    	  logsAdapter.notifyDataSetChanged();
							    	  logsAdapter2.notifyDataSetChanged();
							      }

							      // Return a string for your item
							      @Override public String getTitle() {
							        //return "Deleted '" + item.getFullName() . "'"; // Plz, use the resource system :)
							    	  return "Archived";
							      }

							      // Delete item completely from your persistent storage
							      @Override public void discard() {
							        //MyDatabasUtils.delete(item);
							    	  /*dbHelper.open();
							    	  //dbHelper.delete((int) logsAdapter.getItemId(position));
							    	  SmsStorageData item = dbHelper.readThis((int) logsAdapter.getItemId(position));
							    	  item.isActive = false;
							    	  dbHelper.update(item);
							    	  dbHelper.close();
							    	  getDBData();
							    	  getDBData2();
							    	  logsAdapter.notifyDataSetChanged();
							    	  logsAdapter2.notifyDataSetChanged();*/
							      }
							    };

							  }

							});
						list1.enableSwipeToDismiss();
						list1.setRequireTouchBeforeDismiss(false);
					       list1.setUndoHideDelay(3000);
				       //logsAdapter.notifyDataSetChanged();
						break;

			case 2:
				
				rootView = inflater.inflate(R.layout.fragment_main_tab2, container,
						false);
	
					final EnhancedListView list2 = (EnhancedListView) rootView.findViewById(R.id.listview2);
					dbHelper = DatabaseHelper.getInstance(rootView.getContext());
			        getDBData2();
			        logsAdapter2 = new CustomListAdapter(rootView.getContext(), results2, deleteBtn, selectedDeleteList);
			       list2.setAdapter(logsAdapter2);
			       list2.setDismissCallback(new OnDismissCallback() {

						  @Override public EnhancedListView.Undoable onDismiss(EnhancedListView listView, final int position) {

						    // Store the item for later undo
						    /*final Person item = (Person) mAdapter.getItem(position);
						    // Remove the item from the adapter
						    mAdapter.remove(position);*/
					    	  /*final LinearLayout item = (LinearLayout)list1.getChildAt(position);
					    	  item.setVisibility(View.GONE);*/
							  final int itemid = (int) logsAdapter2.getItemId(position);
							  dbHelper.open();
					    	  //dbHelper.delete((int) logsAdapter.getItemId(position));
					    	  final SmsStorageData item = dbHelper.readThis(itemid);
					    	  dbHelper.delete(itemid);
					    	  dbHelper.close();
					    	  getDBData2();
					    	  logsAdapter2.notifyDataSetChanged();
						    // return an Undoable
						    return new EnhancedListView.Undoable() {
						      // Reinsert the item to the adapter
						      @Override public void undo() {
						        //mAdapter.insert(position, item);
						    	  /*item.setVisibility(View.VISIBLE);
						    	  TranslateAnimation animate = new TranslateAnimation(-item.getWidth(),0,0,0);
						    	  animate.setDuration(200);
						    	  //animate.setFillAfter(true);
						    	  item.startAnimation(animate);*/
						    	  dbHelper.open();
						    	  long createResult = -1;
						    	  dbHelper.create(item);
						    	  dbHelper.close();
						    	  getDBData2();
						    	  logsAdapter2.notifyDataSetChanged();
						      }

						      // Return a string for your item
						      @Override public String getTitle() {
						        //return "Deleted '" + item.getFullName() . "'"; // Plz, use the resource system :)
						    	  return "Deleted";
						      }

						      // Delete item completely from your persistent storage
						      @Override public void discard() {
						        //MyDatabasUtils.delete(item);
						    	  /*dbHelper.open();
						    	  //dbHelper.delete((int) logsAdapter.getItemId(position));
						    	  SmsStorageData item = dbHelper.readThis((int) logsAdapter.getItemId(position));
						    	  item.isActive = false;
						    	  dbHelper.update(item);
						    	  dbHelper.close();
						    	  getDBData();
						    	  getDBData2();
						    	  logsAdapter.notifyDataSetChanged();
						    	  logsAdapter2.notifyDataSetChanged();*/
						      }
						    };

						  }

						});
			       list2.setRequireTouchBeforeDismiss(false);
			       list2.setUndoHideDelay(4000);
					list2.enableSwipeToDismiss();
			       
			       break;
			
				
			}
			return rootView;
		}
		
	    private String getList(String smsId)
	    {
	    	ContentResolver cr = rootView.getContext().getContentResolver();
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
		
	    
	    
	    
	        
		/*private void displayListView() {
			 
			 
			  Cursor cursor = dbHelper.fetchAllMessages();
			 
			  // The desired columns to be bound
			  String[] columns = new String[] {
			    MessageDbAdapter.KEY_INTERVAL,
			    MessageDbAdapter.KEY_NAME,
			    MessageDbAdapter.KEY_MESSAGE,
			    MessageDbAdapter.KEY_TIME
			  };
			 
			  // the XML defined views which the data will be bound to
			  int[] to = new int[] {
			    R.id.interval,
			    R.id.name,
			    R.id.message,
			    R.id.time,
			  };
			 
			  // create the adapter using the cursor pointing to the desired data
			  //as well as the layout information
			  dataAdapter = new SimpleCursorAdapter(
			    rootView.getContext(), R.layout.row,
			    cursor,
			    columns,
			    to,
			    0);
			 
			  EnhancedListView listView = (EnhancedListView) rootView.findViewById(R.id.listview);
			  // Assign adapter to ListView
			  listView.setAdapter(dataAdapter);
			 
			 
			  listView.setOnItemClickListener(new OnItemClickListener() {
			   @Override
			   public void onItemClick(AdapterView<?> listView, View view,
			     int position, long id) {
			   // Get the cursor, positioned to the corresponding row in the result set
			   Cursor cursor = (Cursor) listView.getItemAtPosition(position);
			 
			   // Get the state's capital from this row in the database.
			   String Messageinterval =
			    cursor.getString(cursor.getColumnIndexOrThrow("interval"));
			   Toast.makeText(rootView.getContext(),
			     Messageinterval, Toast.LENGTH_SHORT).show();
			 
			   }
			  });
			 
			  EditText myFilter = (EditText) rootView.findViewById(R.id.myFilter);
			  myFilter.addTextChangedListener(new TextWatcher() {
			 
			   public void afterTextChanged(Editable s) {
			   }
			 
			   public void beforeTextChanged(CharSequence s, int start,
			     int count, int after) {
			   }
			 
			   public void onTextChanged(CharSequence s, int start,
			     int before, int count) {
			    dataAdapter.getFilter().filter(s.toString());
			   }
			  });
			   
			  dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			         public Cursor runQuery(CharSequence constraint) {
			             return dbHelper.fetchMessagesByName(constraint.toString());
			         }
			     });
			 
			 }*/
	}

}
