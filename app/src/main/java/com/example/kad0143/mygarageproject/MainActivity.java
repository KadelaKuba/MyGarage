package com.example.kad0143.mygarageproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ListView lvEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_cars);

        DbHelper mDbHelper = new DbHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CarTable._ID,
                CarTable.COLUMN_NAME_BRAND,
                CarTable.COLUMN_NAME_MODEL,
                CarTable.COLUMN_NAME_YEAR,
                CarTable.COLUMN_NAME_ENGINE,
                CarTable.COLUMN_NAME_IMAGE
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CarTable.COLUMN_NAME_BRAND + " ASC";
        Cursor cursor = db.query(
                CarTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List<Car> scoreRows = new ArrayList<Car>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CarTable._ID));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_BRAND));
            String model = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_MODEL));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_YEAR));
            String engine = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_ENGINE));
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_IMAGE));
            scoreRows.add(new Car(itemId, brand, model, year, engine, DbBitmapUtility.getImage(imgByte)));
        }
        cursor.close();

        lvEntries = (ListView) findViewById(R.id.listView1);
        DataAdapter adapter = new DataAdapter(this, R.layout.list_entry_layout, scoreRows);
        lvEntries.setAdapter(adapter);

        Button btnNewEntry = (Button) findViewById(R.id.addCarButton);
        btnNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewCar.class));
            }
        });

//        Button settingsButton = (Button) findViewById(R.id.settingsButton);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//            }
//        });

//        Button allCars = (Button) findViewById(R.id.allCars);
//        allCars.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ShowCarsActivity.class));
//            }
//        });
    }
}
