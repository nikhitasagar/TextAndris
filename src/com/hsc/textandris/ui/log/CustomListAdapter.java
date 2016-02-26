package com.hsc.textandris.ui.log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hsc.textandris.R;
import com.hsc.textandris.db.DatabaseHelper;

public class CustomListAdapter extends BaseAdapter {
	 
    private ArrayList<LogsItem> listData;
    private  int count = 0;
    private Button discardBtn;
    private ArrayList<LogsItem> selectedDeleteList;
    private Context mContext;
    private static final int REQUEST_CODE = 9;

    private LayoutInflater layoutInflater;
 
    public CustomListAdapter(Context context, ArrayList<LogsItem> listData, Button btn, ArrayList<LogsItem> selectedDeleteList) {
    	this.mContext = context;
    	this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
        discardBtn = btn;
        this.selectedDeleteList = selectedDeleteList;
    }
 
    @Override
    public int getCount() {
        return listData.size();
    }
 
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return listData.get(position).getId();
    }
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.repeatView = (TextView) convertView.findViewById(R.id.interval);
            /*holder.occurrenceView = (TextView) convertView.findViewById(R.id.occurrence);
            */holder.reportedDateView = (TextView) convertView.findViewById(R.id.time);
            /*holder.sentView = (TextView) convertView.findViewById(R.id.sent);
           // holder.edit = (Button) convertView.findViewById(R.id.edit);
            holder.selectedCheckBox = (CheckBox) convertView.findViewById(R.id.check);
            */holder.msgView = (TextView) convertView.findViewById(R.id.message);
            /*holder.edit = (Button)convertView.findViewById(R.id.edit);*/
 			
          /*  holder.edit.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) 
	            {
	            	
	            	Intent mIntent = new Intent(mContext,
							NewMessage.class);	
	            	//mContext.startActivity(mIntent);
	            	mIntent.putExtra("editList", listData);
	            	mContext.startActivity(mIntent);
	            	//startActivityForResult(mIntent, REQUEST_CODE);
				
	            }
            });*/
            
            
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
                
        holder.nameView.setText(listData.get(position).getName());
        holder.repeatView.setText(" "+listData.get(position).getRepeat());  
        holder.reportedDateView.setText(""+listData.get(position).getDate());
        holder.msgView.setText(listData.get(position).getMessageText());
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(mContext);
        dbHelper.open();
        String id = dbHelper.readThis(listData.get(position).getId()).uniqueIDs.split(",")[0];
        Bitmap im = null;
        if(getPhoneNumbers(id).size()>0) im = getFacebookPhoto(getPhoneNumbers(id).get(0));
        dbHelper.close();
        if(im!=null)
        holder.imageView.setImageBitmap(im);

        /*
        if(!listData.get(position).isActive()){
        	holder.occurrenceView.setText("Sent");
        	holder.reportedDateView.setText("");
        }else{
        	holder.occurrenceView.setText("Next Occurrence :");
        }*/
 
        return convertView;
    }
    public Bitmap getFacebookPhoto(String phoneNumber) {
        Uri phoneUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Uri photoUri = null;
        ContentResolver cr = mContext.getContentResolver();
        Cursor contact = cr.query(phoneUri,
                new String[] { ContactsContract.Contacts._ID }, null, null, null);

        if (contact.moveToFirst()) {
            long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));
            photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);

        }
        else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.contact);
            return defaultPhoto;
        }
        if (photoUri != null) {
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    cr, photoUri);
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.contact);
            return defaultPhoto;
        }
        Bitmap defaultPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.contact);
        return defaultPhoto;
    }
    private ArrayList<String> getPhoneNumbers(String id) 
    {
        ArrayList<String> phones = new ArrayList<String>();

        Cursor cursor = mContext.getContentResolver().query(
        		ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
                null, 
                ContactsContract.CommonDataKinds.Phone._ID +" = ?", 
                new String[]{id}, null);

        while (cursor.moveToNext()) 
        {
            phones.add(cursor.getString(cursor.getColumnIndex(CommonDataKinds.Phone.NUMBER)));
        } 

        cursor.close();
        return(phones);
    }
 
    protected void startActivityForResult(Intent mIntent, int requestCode) {
		// TODO Auto-generated method stub
		
	}

	static class ViewHolder {
        TextView nameView;
        TextView repeatView;
        TextView occurrenceView;
        TextView sentView;
        TextView reportedDateView;
        TextView msgView;
        ImageView imageView;
        CheckBox selectedCheckBox;
        Button edit;
    }
 
}