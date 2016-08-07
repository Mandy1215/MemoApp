package com.example.mandy.memo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Detials extends Activity implements View.OnClickListener{
    private Button delete_detials ,back_detials ,change_detials;
    private EditText detialsET;
    private NoteDB noteDB;
    private SQLiteDatabase writer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detials);

        detialsET = (EditText) findViewById(R.id.detialsET);
        delete_detials = (Button) findViewById(R.id.delete_detials);
        back_detials = (Button) findViewById(R.id.back_detials);
        change_detials = (Button) findViewById(R.id.change_detials);
        noteDB = new NoteDB(this);
        writer = noteDB.getWritableDatabase();

        delete_detials.setOnClickListener(this);
        back_detials.setOnClickListener(this);
        change_detials.setOnClickListener(this);
        detialsET.setText(getIntent().getStringExtra(NoteDB.CONTENT));
        detialsET.setSelection(detialsET.getText().length() );
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_detials:
                finish();
                break;
            case R.id.delete_detials:
                deletedetials();
                finish();
                break;
            case R.id.change_detials:
                changeSave();
                finish();
                break;
        }
    }

    private void changeSave() {
        ContentValues contentValue = new ContentValues();
        contentValue.put(NoteDB.CONTENT, detialsET.getText().toString());
        //    contentValues.put(NoteDB.CALCULATIME ,);
        writer.insert(NoteDB.TABLE_NAME, null, contentValue);
    }

    private void deletedetials() {
        writer.delete(NoteDB.TABLE_NAME,
                "_id=" + getIntent().getIntExtra(NoteDB.ID, 0), null);
    }


}
