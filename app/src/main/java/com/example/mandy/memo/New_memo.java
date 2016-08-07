package com.example.mandy.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class New_memo extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private String value;   //接收由 MainActivity传过来的键值
    private Button save, delete;
    private EditText editText, settimeET;
    private NoteDB noteDB;
    private SQLiteDatabase dbWriter;

    private int i = 0 ;
    private Timer timer=null;
    private TimerTask timerTask = null ;
    private long different ;
    private Calendar cal ;
    private Handler mHandler;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_memo);

        value = getIntent().getStringExtra("flag");
        save = (Button) findViewById(R.id.save);
        delete = (Button) findViewById(R.id.delete);
        editText = (EditText) findViewById(R.id.edit);
        settimeET = (EditText) findViewById(R.id.settimeET);
        noteDB = new NoteDB(this);
        dbWriter = noteDB.getWritableDatabase();
        save.setOnClickListener(this);
        delete.setOnClickListener(this);

        settimeET.setOnTouchListener(this);

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                addDB();
       //         calculaTime();
                finish();
                break;
            case R.id.delete:
                finish();
                break;
        }
    }
/*//输入时间和系统时间差
    private void calculaTime() {
        Date d1 = new Date(System.currentTimeMillis());
        String inTime = editText.getText().toString();
        Date d2 = new Date(inTime);
        different = d2.getTime() -d1.getTime(); //这样得到的差值是微秒级
        i = (int) (different /(1000));
        long days = different / (1000 * 60 * 60 * 24);
        long hours = (different - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (different - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        String string = days + "天" + hours + "小时" + minutes + "分";
        Toast.makeText(New_memo.this , string ,Toast.LENGTH_SHORT).show();
        startTime();
        mHandler = new Handler(){
            public void handleMessage(Message mess) {
               // startTime();
            }
        };

    }

    private void startTime() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                i--;
                Message message = mHandler.obtainMessage();
                message.arg1 = i;
                mHandler.sendMessage(message);
            }
        };
        timer.schedule(timerTask, 1000);
    }*/

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.set_time, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH), null);

            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(cal.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(Calendar.MINUTE);

            if (v.getId() == R.id.settimeET) {
                final int inType = settimeET.getInputType();
                settimeET.setInputType(InputType.TYPE_NULL);
                settimeET.onTouchEvent(event);
                settimeET.setInputType(inType);
                settimeET.setSelection(settimeET.getText().length());

                builder.setTitle("选取起始时间");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        sb.append("  ");
                        sb.append(timePicker.getCurrentHour())
                                .append(":").append(timePicker.getCurrentMinute());

                        settimeET.setText(sb);
                        dialog.cancel();
                    }
                });
            }
            Dialog dialog = builder.create();
            dialog.show();
        }
        return true;
    }

  //添加数据的方法
    public void addDB() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteDB.CONTENT, editText.getText().toString());
        contentValues.put(NoteDB.TIME, getTime());
    //    contentValues.put(NoteDB.CALCULATIME ,);
       dbWriter.insert(NoteDB.TABLE_NAME, null, contentValues);
    }

    //时间
    private String getTime() {
        /*SimpleDateFormat format = new SimpleDateFormat("yyy年MM月DD日 HH:mm:ss");
        Date date = new Date();
        String string = format.format(date);*/

        String string = settimeET.getText().toString();
        return string;
    }


}
