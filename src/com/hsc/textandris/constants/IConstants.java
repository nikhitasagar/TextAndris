package com.hsc.textandris.constants;

/**
 * Put constants her. DO NOT HARD-CODE anywhere!!!!!!
 * @author Ankur
 *
 */
public interface IConstants {
	
	interface ALARM_MANAGER_CONSTANTS{
		//minutes
		int MIN_ALLOWABLE_TIME_INTERVAL = 1;
		
		//minutes i.e. hourly
		int MIN_REPEAT_INTERVAL = 60;
	}
	
	interface MESSENGER_CONSTANTS{
		String ADD_ON_MESSAGE = "\n\nSent via TextAndris";
		
		int SEND_MESSAGE = 1001;
		int CANCEL_ALL_MESSAGES = SEND_MESSAGE +1;
	}
	
	interface DB_FIELDS{
		//The key_id is not here, it is the mandatory, secret field, invisible to the UI
		
		String TO_NAMES = "tonames";
		
		String TO_NUMBERS = "tonumbers";
		
		String UNIQUE_IDS = "uniqueids";
		
		String MESSAGE_CONTENT = "msgcontents";
		
		String START_TIME = "dtstart";
		
		String END_TIME = "dtend";
		
		String TIMEZONE = "timezone";
		
		String IS_ACTIVE = "isactive";
		
		String IS_REPEAT = "isrepeat";
		
		String REPEAT_FACTOR = "repeatfactor";
		
		String NEXT_REPEAT_TIME = "nextrepeattime";
	}
	
	interface REPEAT_CONSTANTS{
		int base = 1500;
		
		int ONCE = base +2;
		
		int HOURLY = base +3;
		
		int DAILY = base + 4;
		
		int WEEKLY = base + 5;
		
		int MONTHLY = base +6;
		
		int YEARLY = base +7;
		
		
	}
	
	interface DIALOG_CONSTANTS
	{		
		int MESSAGE_CONTENT_DIALOG = 1;
		
		int PHONE_NO_DIALOG = 2;
		
	}
	
	interface DATE_TIME_PICKER_CONSTANTS
	{		
		int START_DATE_TIME_PICKER = 1;
		
		int END_DATE_TIME_PICKER = 2;
		
	}



}
