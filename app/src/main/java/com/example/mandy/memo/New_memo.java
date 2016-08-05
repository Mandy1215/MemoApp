package com.example.mandy.memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;



public class New_memo extends Activity implements View.OnClickListener, View.OnTouchListener {
    private String value;   //接收由 MainActivity传过来的键值
    private Button save, delete;
    private EditText editText, settimeET;
    private NoteDB noteDB;
    private SQLiteDatabase dbWriter;

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
                finish();
                break;
            case R.id.delete:
                finish();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = View.inflate(this, R.layout.set_time, null);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
            final TimePicker timePicker = (android.widget.TimePicker) view.findViewById(R.id.time_picker);
            builder.setView(view);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

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
