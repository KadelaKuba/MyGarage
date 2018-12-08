package com.example.kad0143.mygarageproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewCar extends Activity {

    EditText brand;
    EditText model;
    EditText year;
    EditText engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        brand = (EditText) findViewById(R.id.brandEdit);
        model = (EditText) findViewById(R.id.modelEdit);
        year = (EditText) findViewById(R.id.yearEdit);
        engine = (EditText) findViewById(R.id.engineEdit);
        Button saveCarButton = (Button) findViewById(R.id.saveCarButton);
        saveCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveToDb(brand.getText().toString(), model.getText().toString(), year.getText().toString(), engine.getText().toString())) {
                    Toast.makeText(NewCar.this, "Uloženo", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NewCar.this, MainActivity.class));
                } else {
                    Toast.makeText(NewCar.this, "Chyba", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean saveToDb(String brand, String model, String year, String engine) {
        DbHelper mDbHelper = new DbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys

//        int deletedRows = db.delete(CarTable.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put(CarTable.COLUMN_NAME_BRAND, brand);
        values.put(CarTable.COLUMN_NAME_MODEL, model);
        values.put(CarTable.COLUMN_NAME_YEAR, year);
        values.put(CarTable.COLUMN_NAME_ENGINE, engine);
        // Insert the new row, returning the primary key value of the new row
        long neWid = db.insert(CarTable.TABLE_NAME, null, values);

        return true;
    }
}
