package com.viplav.nsl;

import android.content.Context;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NLService extends NotificationListenerService{
    Context context;
    DataBaseHelper dbInstance;
    public static ArrayList<String> appPkgs ;
    public static ArrayList<String> exceptTittle = new ArrayList<String>(){
        {
            add("WhatsApp Web");
            add("WhatsApp");
        }
    };
    public static ArrayList<String> exceptText = new ArrayList<String>(){
        {
            add("new messages");
        }
    };

    @Override
    public void onListenerConnected()
    {
        MainActivity.isNotificationAccessEnabled = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        appPkgs = new ArrayList<String>(Arrays.asList("com.whatsapp"));
        String pack = sbn.getPackageName();
        if(appPkgs.contains(pack) && !sbn.getKey().contains("null"))
        {
            String text = " ";
            String title = " ";
            try {
                Log.e("exc" , String.valueOf(sbn.getKey()));
                Bundle extras = sbn.getNotification().extras;
                CharSequence x = extras.getCharSequence("android.text");
                if(x!= null)
                    text = x.toString();
                title = extras.getString("android.title");
                if(!MessageFilter(title,text))
                    return;
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                dbInstance = new DataBaseHelper(context);
                dbInstance.insertData(title, text, dateFormat.format(date));
            } catch (Exception e) {
                Log.i("Exception",e.getMessage());
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

    }

    boolean MessageFilter(String tittle, String Msz){
        boolean stat = true;
        if(exceptTittle.contains(tittle))
            stat =  false;
        for (String v : exceptText){
            if(Msz.indexOf(v) != -1)
                stat = false;
        }
        if(tittle == null || Msz == null)
            stat = false;
        return stat;
    }
}
