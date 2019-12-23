package com.viplav.nsl;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ShowData extends AppCompatActivity {
    DataBaseHelper dbInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        try {
            dbInstance = new DataBaseHelper(this);
            Intent intent = getIntent();
            String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, GetAllName(message));
            ListView listView = (ListView) findViewById(R.id.mobile_list1);
            listView.setAdapter(adapter);
        }
        catch (Exception e)
        {

        }
    }
    public String[] GetAllName(String name)
    {
        ArrayList<Data> al = new ArrayList<>();
        ArrayList<String> a2 = new ArrayList<>();
        Cursor c = dbInstance.getAllData(name);
        while (c.moveToNext())
        {
            Data d = new Data();
            d.Name = c.getString(1);
            d.Text = c.getString(2);
            try {
                d.Time = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(c.getString(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            al.add(d);
        }
        Collections.sort(al,new Comparator<Data>() {
            public int compare(Data d1, Data d2) {
                return d1.Time.compareTo(d2.Time);
            }
        });
        for (Data var : al)
        {
            a2.add(var.Name+"\n"+var.Text+"\n"+var.Time.toString());
        }
        Collections.reverse(a2);
        return  Arrays.copyOf(a2.toArray(),a2.size(),String[].class);
    }
}
