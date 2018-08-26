package com.maralexbar.lpwidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main extends AppCompatActivity {


    EditText max,curr;
    CheckBox easy,norm,hard,expert;
    SharedPreferences prefs;
    int m,c,n;
    Calendar calendar;
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        max=findViewById(R.id.tvmax);
        curr=findViewById(R.id.tvcurr);
        easy=findViewById(R.id.ckeasy);
        norm=findViewById(R.id.cknormal);
        hard=findViewById(R.id.ckhard);
        expert=findViewById(R.id.ckexpert);

        SharedPreferences prefs;
        prefs = this.getSharedPreferences("config", Context.MODE_PRIVATE);
        m=prefs.getInt("max",1);
        c=prefs.getInt("current",1);
        n=prefs.getInt("notify",0);



        max.setText(Integer.toString(m));
        curr.setText(Integer.toString(c));

        switch(n) {
            case 1:
                easy.setChecked(true);
                break;
            case 2:
                norm.setChecked(true);
                break;
            case 3:
                hard.setChecked(true);
                break;
            case 4:
                expert.setChecked(true);
                break;
            default:
                break;
        }



        View.OnClickListener h1= new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                easy.setChecked(false);
                hard.setChecked(false);
                norm.setChecked(false);
                expert.setChecked(false);


                switch (v.getId()){

                    case R.id.ckeasy:

                        easy.setChecked(true);
                        n=1;
                        break;
                    case R.id.cknormal:
                        norm.setChecked(true);
                        n=2;
                        break;
                    case R.id.ckhard:
                        hard.setChecked(true);
                        n=3;
                        break;
                    case R.id.ckexpert:
                        expert.setChecked(true);
                        n=4;
                        break;




                }
            }
        };

        easy.setOnClickListener(h1);
        hard.setOnClickListener(h1);
        norm.setOnClickListener(h1);
        expert.setOnClickListener(h1);













    }




    public void save(View v){




        m=Integer.decode(max.getText().toString());
        c=Integer.decode(curr.getText().toString());

        if(m > 0 ){

            if(c > m) {


                Toast.makeText(getApplicationContext(), "LP actual no puede ser superior al maximo de LP", Toast.LENGTH_SHORT).show();

                new Thread() {
                    public void run() {
                        try {

                            Thread.sleep(2000);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    Toast.makeText(getApplicationContext(), "BU BUUU DESU WA!!", Toast.LENGTH_SHORT).show();


                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();



            }  else {



                Calendar calendar = Calendar.getInstance();
                //String comp = df.format(calendar.getTime());
                calendar.add(Calendar.MINUTE, (m-c)*6);
                String comp = df.format(calendar.getTime());

                prefs = this.getSharedPreferences("config",MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("max",m);
                editor.putInt("notify",n);
                editor.putInt("current",c);
                editor.putString("complete",comp);

                editor.commit();
                Intent intent = new Intent(this, NewAppWidget.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
                sendBroadcast(intent);

                //Intent myIntent = new Intent(this, Updater.class);
                //this.startService(myIntent);


                AlarmManager am =(AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE);
                Intent i = new Intent(getBaseContext(), Updater.class);
                PendingIntent pi = PendingIntent.getService(getBaseContext(), 0, i, 0);
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 1, pi);




            }

        } else {
            Toast.makeText(getApplicationContext(), "LP Maximo no puede ser igual a cero", Toast.LENGTH_SHORT).show();

            new Thread() {
                public void run() {
                        try {

                            Thread.sleep(2000);

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {


                                    Toast.makeText(getApplicationContext(), "Step! ZERO to ONE", Toast.LENGTH_SHORT).show();


                                }
                            });

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }.start();




        }










    }


}
