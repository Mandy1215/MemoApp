package com.example.mandy.memo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private Button new_memo;
    private ListView listView;
    private Intent intent;
    private Cursor cursor;
    private MyAdapter myAdapter;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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
