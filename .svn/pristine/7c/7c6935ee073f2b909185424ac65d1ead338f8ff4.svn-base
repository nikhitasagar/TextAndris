package com.hsc.textandris.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactUtility {

	private static final String LOG_TAG = "TextAndris_ContactsUtility";
	
	private ContactUtility(){
		
	}
	
	public static List<String> getSMSContacts(Activity context){		
		List<String> contactNoList = new ArrayList<String>();
		
		Set<String> smsIds = new HashSet<String>();
		
		
		//smsIds = prefs.getStringSet("smsList", new HashSet<String>());
		Cursor cursor;
		Object[] smsIdList = smsIds.toArray();
		try {
		for(int i = 0; i < smsIdList.length; i++){
			ContentResolver cr = context.getContentResolver();
			cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
					, new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER},
					ContactsContract.CommonDataKinds.Phone._ID+"=?" , new String[]{smsIdList[i].toString()},
					ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
							+ " ASC");
			if (cursor != null && cursor.getCount()>0){
				cursor.moveToFirst();
			String name = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String number = cursor
					.getString(cursor
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			String s = name + "\n" + number;
			contactNoList.add(number);
			}
		}
		} catch (Exception e) {
			Log.d(LOG_TAG, "???????? Error in Contacts Read: " + e.getMessage());
		}	
			
		return contactNoList;
	}
	
	public static String makePlaceholders(int length){
		String placeHolders ="?";
		String returnString = null;
		if(length == 0){
			return "";
		} else{
			if(length == 1 ){
				return placeHolders;
			}else{
			for (int i = 0; i< length; i++){
				if(returnString != null){
				returnString = returnString+ ","+placeHolders;
				}else {
					returnString = placeHolders;
				}
			}
			}			
		}
		return returnString;
	}	
	
	public static List<SMSList> getModel(Context context,Set<String> smsIdSet) {
		List<SMSList> list = new ArrayList<SMSList>();
		Cursor cursor;
		Object[] smsIdList = smsIdSet.toArray();

		try {
			
			for(int i = 0; i < smsIdList.length; i++){
				ContentResolver cr = context.getContentResolver();
				cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI
						, new String[]{ContactsContract.CommonDataKinds.Phone._ID,ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER},
						ContactsContract.CommonDataKinds.Phone._ID+" =?" , new String[]{smsIdList[i].toString()},
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
								+ " ASC");
				if (cursor != null && cursor.getCount()>0){
					cursor.moveToFirst();
					String name = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String number = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					String id = cursor
							.getString(cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone._ID));
					
					String s = name + "\n" + number;
					list.add(get(s,number,id));
				}else{
					smsIdSet.remove(smsIdList[i]);
					
				}
				
			}
		
			if(list != null && list.size()>0){
				Collections.sort(list, new Comparator<SMSList>(){
					  public int compare(SMSList s1, SMSList s2) {
					    return s1.getName().compareToIgnoreCase(s2.getName());
					  }
					});
				}
		
		} catch (Exception e) {
			Log.d(LOG_TAG, "???????? Error in Contacts Read: " + e.getMessage());
		}
		
		return list;
	}
	
	private static SMSList get(String s,String isSelected, String uniqueId) {
		return new SMSList(s,isSelected,uniqueId);
	}	
	
}
