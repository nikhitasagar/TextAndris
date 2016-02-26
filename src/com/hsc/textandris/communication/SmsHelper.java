package com.hsc.textandris.communication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import com.hsc.textandris.constants.IConstants.MESSENGER_CONSTANTS;
import com.hsc.textandris.data.SmsData;

/**
 * Turning this to singleton!!! And adding a Handler!!!
 * 
 * I do not want any weird behavior while sending my messages...
 * @author Ankur
 *
 */
public class SmsHelper {
	private static SmsHelper mInstance = null;
	
	private Handler mSmsHandler = null;
	
	private SmsManager mSmsManager = null;
	
	private SmsHelper(){
		mSmsHandler = new SmsHandler();
		mSmsManager = SmsManager.getDefault();
	}
	
	/**
	 * Utility function to send sms
	 * @param smsData
	 */
	public synchronized void sendMessage(SmsData smsData){
		mSmsHandler.sendMessage(mSmsHandler.obtainMessage(MESSENGER_CONSTANTS.SEND_MESSAGE, smsData));
	}
	
	/**
	 * Utility function only
	 */
	public void cancelPendingMessages(){
		mSmsHandler.removeMessages(MESSENGER_CONSTANTS.SEND_MESSAGE);
	}
	
	public static synchronized SmsHelper getInstance(){
		if( mInstance==null){
			mInstance = new SmsHelper();
		}
		
		return mInstance;
	}
	
	private class SmsHandler extends Handler{
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSENGER_CONSTANTS.SEND_MESSAGE:
				SmsData smsData = (SmsData) msg.obj;
				
				if(smsData==null || smsData.toNumbers == null || smsData.toNumbers.trim().equals("")){
					//Error, exit
					return;
				}
				
				Log.d("Sms", "Trying to send sms : Data = "+smsData.message + " : Receiver:" + smsData.toNumbers);
				
				List<String> numbers = Arrays.asList(smsData.toNumbers.split("\\s*,\\s*"));
				
				for(String number : numbers){
					sendSms(number, smsData.message);
				}
				
				break;
				
			default:
				break;
			}
		}
	}
	
	/**
	 * This can send normal Sms as well as Long Sms, no problems
	 * @param toNumber
	 * @param message
	 */
	private void sendSms(String toNumber, String message) {
		if(mSmsManager == null){
			return;
		}

		message += MESSENGER_CONSTANTS.ADD_ON_MESSAGE;
		
		ArrayList<String> parts = mSmsManager.divideMessage(message);
		
		/*
		 *If message sending fails, the app should not crash!! 
		 */
		try{
			mSmsManager.sendMultipartTextMessage(toNumber, null, parts, null, null);
		}catch(Exception e){
			//Do nothing
		}
	}

}
