package com.hsc.textandris.scheduler;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hsc.textandris.constants.IConstants.ALARM_MANAGER_CONSTANTS;

/**
 * This is class that sets my Alarms. Also, cancels any previously scheduled alarms, to avoid double SMS delivery
 * @author Ankur
 *
 */
public class MyScheduleReceiver extends BroadcastReceiver {

	// Restart service every 30 seconds
	private static final long REPEAT_TIME = ALARM_MANAGER_CONSTANTS.MIN_ALLOWABLE_TIME_INTERVAL * 60 * 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		System.out.println("------------- My Broadcaster!!!!");
		AlarmManager service = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent i = new Intent(context, MyStartServiceReceiver.class);
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
				PendingIntent.FLAG_CANCEL_CURRENT);
		long time = System.currentTimeMillis();
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        
        int minute = cal.get(Calendar.MINUTE);
        
        int minuteOffset = (ALARM_MANAGER_CONSTANTS.MIN_ALLOWABLE_TIME_INTERVAL - 
        		(minute % ALARM_MANAGER_CONSTANTS.MIN_ALLOWABLE_TIME_INTERVAL)) % ALARM_MANAGER_CONSTANTS.MIN_ALLOWABLE_TIME_INTERVAL;
        
        int minutesToDelay = (minuteOffset == 0) ? (minuteOffset +ALARM_MANAGER_CONSTANTS.MIN_ALLOWABLE_TIME_INTERVAL) : minuteOffset;
        
        long millisToDelay = minutesToDelay * (60 * 1000) - cal.get(Calendar.SECOND) * 1000 - cal.get(Calendar.MILLISECOND);
        
        millisToDelay += time;
        
		//
		// InexactRepeating allows Android to optimize the energy consumption
		service.setRepeating(AlarmManager.RTC_WAKEUP, millisToDelay, REPEAT_TIME, pending);

		// service.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		// REPEAT_TIME, pending);

	}
} 