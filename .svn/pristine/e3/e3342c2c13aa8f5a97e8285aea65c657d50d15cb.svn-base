package com.hsc.textandris.contacts;

import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hsc.textandris.R;

public class ListArrayAdapter extends ArrayAdapter<SMSList> {
	
		private List<SMSList> list = null;
		private Set<String> smsIds = null;
		public List<SMSList> getList() {
			return list;
		}


		private final Activity context;

		public ListArrayAdapter(Activity context, List<SMSList> list, Set<String> smsId) {
			super(context, R.layout.contactlist, list);
			this.context = context;
			this.list = list;
			smsIds = smsId;
		}

		static class ViewHolder {
			private TextView text;
			private TextView uniqueId;
			private ImageView image;
		}
		

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			
			//smsIds = prefs.getStringSet("smsList", new HashSet<String>());
			if (convertView == null) {
				LayoutInflater inflator = context.getLayoutInflater();
				view = inflator.inflate(R.layout.smslist, null);
				final ViewHolder viewHolder = new ViewHolder();
				viewHolder.text = (TextView) view.findViewById(R.id.label1);
				viewHolder.uniqueId = 	(TextView) view.findViewById(R.id.label2);

				view.setTag(viewHolder);
				
			} else {
				view = convertView;
//				((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
			}

			final ViewHolder holder = (ViewHolder) view.getTag();
			holder.text.setText(list.get(position).getName());
			holder.image = (ImageView) view.findViewById(R.id.remove);
			holder.uniqueId.setText(list.get(position).getUniqueId());
//			holder.checkbox.setChecked(list.get(position).isSelected());
			holder.image.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 smsIds.remove(holder.uniqueId.getText().toString());
					 
//					String contactNumber[] =  holder.text.getText().toString().split("\n");									
//					
//					
//					String[] groupProjection = new String[] {
//			  				ContactsContract.Groups._ID, ContactsContract.Groups.TITLE };
//					 String groupId = null;		         
//				  		Cursor getGroupIDCursor = null;
//				  		getGroupIDCursor = context.managedQuery(
//				  				ContactsContract.Groups.CONTENT_URI, groupProjection,
//				  				ContactsContract.Groups.TITLE + "=?",
//				  				new String[] {ContactUtility.GROUP_NAME }, null);
//				  		if(getGroupIDCursor != null && getGroupIDCursor.getCount()>0){
//				  		getGroupIDCursor.moveToFirst();
//				  		groupId = (getGroupIDCursor.getString(getGroupIDCursor
//				  				.getColumnIndex("_id")));	
//				  		}
//					
//					 String personId = null;		         
//				  		Cursor getPersonIDCursor = null;
//				  		getPersonIDCursor = context.managedQuery(
//				  				ContactsContract.Data.CONTENT_URI, null,
//				  				ContactsContract.CommonDataKinds.GroupMembership.DISPLAY_NAME + "=? and "+ ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + "=?" ,new String[]{contactNumber[0], groupId}, null);
//				  		Log.d("PRK","person id cursor size "+getPersonIDCursor.getCount());
//				  		if(getPersonIDCursor != null && getPersonIDCursor.getCount()>0){
//				  			getPersonIDCursor.moveToFirst();
//				  			personId = getPersonIDCursor.getString(getPersonIDCursor.getColumnIndex(ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID));
//				  		}
//				  		
//					
//					long personID= Long.parseLong(personId);
//					long groupID= Long.parseLong(groupId);
//					removeFromGroup(personID, groupID);	
					refreshAdapter();
					
				}

				
			});
			
			return view;
		}
		
		protected void refreshAdapter() {
			ListView mListView = (ListView) context.findViewById(R.id.smsList);
			
			try {
				//List<String> contactIdList = ContactUtility.getContactsInGroup(context);			
				
				// Create an array of Strings, for List
				ListArrayAdapter adapter = new ListArrayAdapter(context,
						ContactUtility.getModel(context,smsIds),smsIds);
				// Assign adapter to ListView
				mListView.setAdapter(adapter);
				
				if (mListView.getCount()==0){
					mListView.setVisibility(View.GONE);
				}else{
					mListView.setVisibility(View.VISIBLE);
				}
				
			} catch (Exception e) {
				//Log.d(LOG_TAG,"**** Exception: "+ e.getMessage());
			}
			
		}

//		private List<SMSList> getModel(List<String> contactIdList) {
//			List<SMSList> list = new ArrayList<SMSList>();
//
//			try {
//				ContentResolver cr = context.getContentResolver();
//				Cursor cursor = cr.query(
//						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
//						null, null,
//						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
//								+ " ASC");
//
//				cursor.moveToFirst();
//				if (cursor.moveToFirst()) {
//					do {
//						String name = cursor
//								.getString(cursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//						String contactId = cursor
//								.getString(cursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//						String number = cursor
//								.getString(cursor
//										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//						String s = name + "\n" + number;
//						if (contactIdList!= null && contactIdList.contains(contactId)){
//							list.add(get(s,number));
//						}
//						s = null;
//					} while (cursor.moveToNext());
//				}
//			} catch (Exception e) {
//				//Log.d(LOG_TAG, "???????? Error in Contacts Read: " + e.getMessage());
//			}
//
//			return list;
//		}
//		
//		
//		private SMSList get(String s,String isSelected, String uniqueId) {
//			return new SMSList(s,isSelected, uniqueId);
//		}	
		
//		public void removeFromGroup(long personId, long groupId){
//			Log.d("PRK", "group to delete p :"+personId +" g "+groupId);
//
//			ContentValues values = new ContentValues();
//			values.put(
//					ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID,
//					personId);
//			values.put(
//					ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID,
//					groupId);
//			values.put(
//					ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE,
//					ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
//
//		context.getContentResolver().delete(ContactsContract.Data.CONTENT_URI, ContactsContract.CommonDataKinds.GroupMembership.RAW_CONTACT_ID+"=? AND "
//			+ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID+" =?", new String[]{personId+"",groupId+""});
//
//		
//		}

		

}
