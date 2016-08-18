package com.example.mandy.memo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Button new_memo;
    private ListView listView;
    private Intent intent;
    private Cursor cursor;
    private MyAdapter myAdapter;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;

    private Handler handler;
    private TextView welcome;

    int curAlpha = 255;     //当前透明度
    private final int SPEED = 3; //每隔多少毫秒改变一次透明度
    private int curColor = 0;   //当前颜色的位置
    int[] color = new int[]{Color.BLACK, Color.CYAN, Color.BLUE, Color.GREEN,
            Color.DKGRAY, Color.RED, Color.YELLOW};
    boolean isAdd =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcome = (TextView) findViewById(R.id.welcome);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {

                    if (msg.what == 0x123) {
                        if (curAlpha <= 0) {
                            curColor = (int) ((Math.random()*10)*0.7);
                            isAdd =true;
                        }
                        if (curAlpha >= 255) {
                            isAdd = false ;
                        }

                        if (isAdd){
                            curAlpha ++;
                        }else {
                            curAlpha--;
                        }
                        int newColor = ColorUtils.setAlphaComponent(color[curColor], curAlpha);
                        welcome.setTextColor(newColor);
                    }
                    return true;
                }

        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what =0x123 ;
                handler.sendMessage(msg);
            }
        } , 0 , SPEED);


        listView = (ListView) findViewById(R.id.list);
        new_memo = (Button) findViewById(R.id.new_memo);
        new_memo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, New_memo.class);
                startActivity(intent);
            }
        });
        noteDB = new NoteDB(this);
        dbReader = noteDB.getReadableDatabase();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(MainActivity.this, Detials.class);
                i.putExtra(NoteDB.ID, cursor.getInt(cursor.getColumnIndex(NoteDB.ID)));
                i.putExtra(NoteDB.CONTENT,
                        cursor.getString(cursor.getColumnIndex(NoteDB.CONTENT)));
                i.putExtra(NoteDB.TIME,
                        cursor.getString(cursor.getColumnIndex(NoteDB.TIME)));
                startActivity(i);
            }
        });

    }




    public void selectDB() {
        cursor = dbReader.query(NoteDB.TABLE_NAME, null, null, null,
                null, null, null);
        myAdapter = new MyAdapter(this, cursor);
        listView.setAdapter(myAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        selectDB();
    }
}
