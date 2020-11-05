package com.example.delixie.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.net.wifi.WifiManager;
import android.content.Context;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;

import java.util.Calendar;
import android.os.Message;
import android.os.Handler;
import java.util.TimerTask;
import java.util.Timer;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import  android.os.PowerManager;
import  android.os.PowerManager.WakeLock;
import  android.content.Intent;
import  android.view.WindowManager;
import android.os.CountDownTimer;

public class MainActivity extends AppCompatActivity {
    private TextView tvCurrentTime;
    private WifiManager wifiManager;
    private boolean bIsPunchIn = true;
    private boolean bIsPunchOut = false;
    private int timePunchIn = 0;//Math.random() * 28.0;
    private int timePunchOut = 0;//Math.random() * 28.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        tvCurrentTime = (TextView) findViewById(R.id.tvCurrentTime);

        if (!bIsPunchIn){
            timePunchIn = (int)Math.floor(Math.random() * 28.0);
        }

        if (!bIsPunchOut){
            timePunchOut = (int)Math.floor(Math.random() * 15.0);
        }

        final Handler startTimehandler = new Handler(){
             public void handleMessage(android.os.Message msg) {
                 Calendar calendar = Calendar.getInstance();
                 int year = calendar.get(Calendar.YEAR);
                 int month = calendar.get(Calendar.MONTH) + 1;
                 int day = calendar.get(Calendar.DAY_OF_MONTH);
                 int hour = calendar.get(Calendar.HOUR_OF_DAY);
                 int minute = calendar.get(Calendar.MINUTE);
                 int week = calendar.get(Calendar.DAY_OF_WEEK);

                 String time = getTimeStr(year) + '年' +
                         getTimeStr(month) + '月' +
                         getTimeStr(day) + '日' +
                         getTimeStr(hour) + '点' +
                         getTimeStr(minute) + '分';

                 switch (week) {
                     case Calendar.SUNDAY:
                         time += " 周日";
                         break;
                     case Calendar.MONDAY:
                         time += " 周一";
                         break;
                     case Calendar.TUESDAY:
                         time += " 周二";
                         break;
                     case Calendar.WEDNESDAY:
                         time += " 周三";
                         break;
                     case Calendar.THURSDAY:
                         time += " 周四";
                         break;
                     case Calendar.FRIDAY:
                         time += " 周五";
                         break;
                     case Calendar.SATURDAY:
                         time += " 周六";
                         break;
                     default:
                         break;
                 }

                 tvCurrentTime.setText(time);

                 if (week != Calendar.SUNDAY && week != Calendar.SATURDAY){
                    if (!bIsPunchIn){
                        if (hour == 9 && minute == timePunchIn){
                            wifiManager.setWifiEnabled(true);
                            bIsPunchIn = true;
                            bIsPunchOut = false;
                            timePunchOut = (int)Math.floor(Math.random() * 15.0);
                        }
                    }

                    if (!bIsPunchOut){
                        if (hour == 18 && minute == (timePunchOut + 30)){
                            wifiManager.setWifiEnabled(false);
                            bIsPunchOut = true;
                            bIsPunchIn = false;
                            timePunchIn = (int)Math.floor(Math.random() * 28.0);
                        }
                    }

                    if (bIsPunchIn&&!bIsPunchOut){
                        if (!wifiManager.isWifiEnabled()){
                            wifiManager.setWifiEnabled(true);
                        }
                    }

                     if (!bIsPunchIn&&bIsPunchOut){
                         if (wifiManager.isWifiEnabled()){
                             wifiManager.setWifiEnabled(false);
                         }
                     }
                 }
            }
        };
        new Timer("计时器").scheduleAtFixedRate(new TimerTask() {
        @Override
            public void run() {
                Message msg = new Message();
                startTimehandler.sendMessage(msg);
            }
        }, 0, 1000L);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getTimeStr(int num)
    {
        if (num < 10){
            return  "0" + Integer.toString(num);
        }else{
            return  Integer.toString(num);
        }
    }

}
