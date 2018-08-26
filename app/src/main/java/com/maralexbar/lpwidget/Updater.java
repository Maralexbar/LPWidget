package com.maralexbar.lpwidget;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;



public class Updater extends Service {


    SharedPreferences prefs; //Archivo de preferencias
    private NotificationManager notifManager;
    int m,c,n,mod,next; // m = Maximo, c = Actual
    String complete,date,msg; // complete = Hora y Fecha donde se completara, date = Hora y Fecha actual
    Date d1 = null,d2 = null; //d1 = Hora y Fecha donde se completara, d2 = Hora y Fecha actual (Formateado como Date)
    Calendar calendar; // calendar = Calendario para conseguir la hora y fecha
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm"); // df = Formateador de fecha y hora
    long dif; // Diferencia entre ambas fecha y hora




    public void onCreate() {
        super.onCreate();

    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {


        prefs = this.getSharedPreferences("config", android.content.Context.MODE_PRIVATE); //Abrir las preferencias
        m=prefs.getInt("max",1);// Conseguir el LP max guardado en las preferencias
        n=prefs.getInt("notify",1);
        next=prefs.getInt("next",0);
        c=prefs.getInt("current",1);// Conseguir el LP actual guardado en las preferencias

        complete=prefs.getString("complete","1"); // Conseguir la fecha donde se completara

        calendar = Calendar.getInstance(); //Conseguir la funcion del calendario








            if (!(complete == "1")) { // Si es la primera ejecucion entonces esperara a que se configure


                date = df.format(calendar.getTime()); // Conseguir la Fecha Actual

                try {
                    d1 = df.parse(date); //Darle formato a la fecha actual
                    d2 = df.parse(complete); //Darle formato a la de completado
                } catch (ParseException e) {
                    e.printStackTrace(); // Por si se genere algun erorr
                }


                //dif = m - (d2.getTime() - d1.getTime()) / 360000; //Restarle 1 LP al max cada 6 minutos ( 6 minutos * 60 segundos * 1000 milisegundos), es aproximado ya que se da el rendodeo

                dif = m - (d2.getTime() - d1.getTime()) / 360000;

                c = (int) dif;// Convertir a entero

                if(c > m){

                    c=m;

                }

                SharedPreferences.Editor editor = prefs.edit();// Abrir las preferencias en modo editor
                editor.putInt("current", c); //Guardar el LP actual
                editor.commit();// Cerrar el editor


                //Actualiza el widget, saltandose la limitacion de Android
                Intent in = new Intent(getBaseContext(), NewAppWidget.class);
                intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(in);


            }


            switch (n){

                case 1:
                    msg="Easy Disponible";
                    mod=5;
                    break;
                case 2:
                    msg="Normal Disponible";
                    mod=10;
                    break;
                case 3:
                    msg="Hard Disponible";
                    mod=15;
                    break;
                case 4:
                    msg="Expert Disponible";
                    mod=25;
                    break;
            }

            if ( (c % mod) == 0 && next != c){

                SharedPreferences.Editor editor = prefs.edit();// Abrir las preferencias en modo editor
                editor.putInt("next", c); //Guardar el LP actual
                editor.commit();// Cerrar el editor


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    createNotification(msg);

                } else{

                    Notification(msg);

                }



            }









        return START_STICKY;
    }


    public void Notification(String txt) {


        Intent in = new Intent(this, Main.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0,in, 0);

        Notification n = new Notification.Builder(this)
                .setContentTitle(txt)
                .setContentText("LP Actual: "+c)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_media_pause, "Pausar", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);







    }








    public void createNotification(String aMessage) {
        final int NOTIFY_ID = 1002;

        String name = "LP Widget";
        String id = "Notificaciones"; // The user-visible name of the channel.
        String description = "Notificaciones de LP"; // The user-visible description of the channel.

        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;

        if (notifManager == null) {
            notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableVibration(true);

                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this, id);

            builder.setContentTitle(aMessage)  // required
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // required
                    .setContentText("LP Actual: "+c)  // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(false)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setLights(Color.rgb(255, 51, 153),1000,2000)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setTicker(aMessage);

            Notification notification = builder.build();
            notifManager.notify(NOTIFY_ID, notification);


        }

    }













    public void onStop() {

    }


    public void onPause() {

    }

    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent objIndent) {
        return null;
    }


}