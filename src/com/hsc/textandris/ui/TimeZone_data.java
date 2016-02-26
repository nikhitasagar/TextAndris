package com.hsc.textandris.ui;



import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.hsc.textandris.R;
 
public class TimeZone_data extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         
        // storing string resources into Array
        String[] adobe_products = getResources().getStringArray(R.array.time_zones);
         
        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.timezones_data, R.id.label, adobe_products));
         
    }
}
