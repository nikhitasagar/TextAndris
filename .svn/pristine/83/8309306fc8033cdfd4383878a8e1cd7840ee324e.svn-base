package com.hsc.textandris.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Starts my Sms service
 * 
 * @author Ankur
 *
 */
public class MyStartServiceReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
	  Log.d("MyStartServiceReceiver", "on Receive");
    Intent service = new Intent(context, SchedulerService.class);
    context.startService(service);
  }
} 