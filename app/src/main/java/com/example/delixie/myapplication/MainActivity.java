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

public class MainActivity extends AppCompatActivity {
    private TextView tvCurrentTime;
    private TextView tvYear;
    private TextView tvMonth;
    private TextView tvDay;
    private TextView tvHours;
    private TextView tvMinutes;

    private Button btHoursPlus;
    private Button btHoursSub;
    private Button btMinutesPlus;
    private Button btMinutesSub;

    private Button btYearPlus;
    private Button btYearSub;
    private Button btMonthPlus;
    private Button btMonthSub;
    private Button btDayPlus;
    private Button btDaySub;

    private Button btWifi;

    private CheckBox cbSwitch;

    private WifiManager wifiManager;

    private int nYear = 2019;
    private int nMonth = 3;
    private int nDay = 1;
    private int nHour = 9;
    private int nMinute = 30;
    private boolean bIsChecked = false;
    private WakeLock wakeLock;
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
        tvYear = (TextView) findViewById(R.id.tvYear);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvHours = (TextView) findViewById(R.id.tvHours);
        tvMinutes = (TextView) findViewById(R.id.tvMinutes);

        btHoursPlus = (Button) findViewById(R.id.btHoursPlus);
        btHoursSub = (Button) findViewById(R.id.btHoursSub);
        btMinutesPlus = (Button) findViewById(R.id.btMinutesPlus);
        btMinutesSub = (Button) findViewById(R.id.btMinutesSub);

        btDayPlus = (Button) findViewById(R.id.btDayPlus);
        btDaySub = (Button) findViewById(R.id.btDaySub);
        btDayPlus = (Button) findViewById(R.id.btDayPlus);
        btDaySub = (Button) findViewById(R.id.btDaySub);
        btYearPlus = (Button) findViewById(R.id.btYearPlus);
        btYearSub = (Button) findViewById(R.id.btYearSub);

        btWifi = (Button) findViewById(R.id.btWifi);

        cbSwitch = (CheckBox) findViewById(R.id.cbSwitch);

        final Handler startTimehandler = new Handler(){
             public void handleMessage(android.os.Message msg) {
                 Calendar calendar = Calendar.getInstance();
                 int year = calendar.get(Calendar.YEAR);
                 int month = calendar.get(Calendar.MONTH) + 1;
                 int day = calendar.get(Calendar.DAY_OF_MONTH);
                 int hour = calendar.get(Calendar.HOUR_OF_DAY);
                 int minute = calendar.get(Calendar.MINUTE);

                 String time = "当前时间：" +
                         getTimeStr(year) + '年' +
                         getTimeStr(month) + '月' +
                         getTimeStr(day) + '日' +
                         getTimeStr(hour) + '点' +
                         getTimeStr(minute) + '分';
                 tvCurrentTime.setText(time);

                if (bIsChecked && year == nYear && month == nMonth && day == nDay && hour == nHour && minute == nMinute){
                    wifiManager.setWifiEnabled(true);
                    btWifi.setText("禁用WIFI");
                    bIsChecked = false;
                    cbSwitch.setChecked(false);
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

        if (wifiManager.isWifiEnabled()) {
            btWifi.setText("禁用");
        } else {
            btWifi.setText("开启");
        }

        tvHours.setText(getTimeStr(nHour) + '点');
        tvMinutes.setText(getTimeStr(nMinute) + '分');


        OnCheckedChangeListener myCheckChangelistener = new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bIsChecked = isChecked;
            }
        };

        cbSwitch.setOnCheckedChangeListener(myCheckChangelistener);


        //
        //PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        //wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakelockTag");
        //wakeLock.acquire();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
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

    public void onEnableWifi(View view)
    {
        if (wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
            btWifi.setText("开启");
        } else {
            wifiManager.setWifiEnabled(true);
            btWifi.setText("禁用");
        }
    }

    public void onHoursPlus(View view)
    {
        if (nHour >= 24){
            nHour = 1;
        }else{
            nHour += 1;
        }
        tvHours.setText(getTimeStr(nHour) + '点');
    }

    public void onHoursSub(View view)
    {
        if (nHour <= 1){
            nHour = 24;
        }else{
            nHour -= 1;
        }
        tvHours.setText(getTimeStr(nHour) + '点');
    }

    public void onMinutesPlus(View view)
    {
        if (nMinute >= 59){
            nMinute = 0;
        }else{
            nMinute += 1;
        }
        tvMinutes.setText(getTimeStr(nMinute) + '分');
    }

    public void onMinutesSub(View view)
    {
        if (nMinute <= 0){
            nMinute = 59;
        }else{
            nMinute -= 1;
        }
        tvMinutes.setText(getTimeStr(nMinute) + '分');
    }

    public void onYearPlus(View view)
    {
        nYear += 1;
        tvYear.setText(getTimeStr(nYear) + '年');
    }

    public void onYearSub(View view)
    {

        nYear -= 1;
        tvYear.setText(getTimeStr(nYear) + '年');
    }

    public void onMonthPlus(View view)
    {
        if (nMonth >= 12){
            nMonth = 1;
        }else{
            nMonth += 1;
        }
        tvMonth.setText(getTimeStr(nMonth) + '月');
    }

    public void onMonthSub(View view)
    {
        if (nMonth <= 1){
            nMonth = 12;
        }else{
            nMonth -= 1;
        }
        tvMonth.setText(getTimeStr(nMonth) + '月');
    }

    public void onDayPlus(View view)
    {
        if (nDay >= 31){
            nDay = 1;
        }else{
            nDay += 1;
        }
        tvDay.setText(getTimeStr(nDay) + '号');
    }

    public void onDaySub(View view)
    {
        if (nDay <= 1){
            nDay = 31;
        }else{
            nDay -= 1;
        }
        tvDay.setText(getTimeStr(nDay) + '号');
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
