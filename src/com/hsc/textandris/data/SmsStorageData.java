package com.hsc.textandris.data;

import java.io.Serializable;
import java.sql.Time;
import com.hsc.textandris.constants.IConstants.REPEAT_CONSTANTS;

/**
 * Used to carry data to-and-fro db
 * @author Ankur
 *
 */
public class SmsStorageData implements Serializable{
	
	//This should not be set for insertion. This is set only AFTER reading and during updation
	public int id = -1;
	
	public String toNames = null;
	
	public String toNumbers = null;
	
	public String uniqueIDs = null;
	
	//No use setting it to null
	public String messageContent = "";
	
	public long startTime = 0;
	
	public long endTime = 0;
	
	public long nextRepeatTime = 0;
	
	public int timezone = 0;
	
	public boolean isActive = false;
	
	public boolean isRepeat = false;
	
	public int repeatFactor = REPEAT_CONSTANTS.ONCE;

}
