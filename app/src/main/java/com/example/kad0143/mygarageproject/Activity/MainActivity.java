package com.example.kad0143.mygarageproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.kad0143.mygarageproject.Entity.CarEntity;
import com.example.kad0143.mygarageproject.DataAdapter.DataAdapter;
import com.example.kad0143.mygarageproject.Database.Table.CarTable;
import com.example.kad0143.mygarageproject.Database.Helper.SQLiteHelper;
import com.example.kad0143.mygarageproject.Database.DbBitmapUtility;
import com.example.kad0143.mygarageproject.R;

import java.util.ArrayList;

public class MainActivity extends Activity {

    ListView lvEntries;
    private ArrayList<CarEntity> scoreRows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_cars);

        scoreRows = new ArrayList<CarEntity>();

        Button btnNewEntry = (Button) findViewById(R.id.addCarButton);
        lvEntries = (ListView) findViewById(R.id.listView1);
        renderListView();

        btnNewEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewCarActivity.class));
            }
        });

        lvEntries.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CarDetailActivity.class);
                long entryId = scoreRows.get(position).id;
                intent.putExtra("entryId", entryId);
                startActivity(intent);
            }
        });

//        Button settingsButton = (Button) findViewById(R.id.settingsButton);
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
//            }
//        });

    }

    private void renderListView() {
        getDataFromDB();
        DataAdapter adapter = new DataAdapter(this, R.layout.list_entry_layout, scoreRows);
        lvEntries.setAdapter(adapter);
    }

    private void getDataFromDB() {
        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                CarTable._ID,
                CarTable.COLUMN_NAME_BRAND,
                CarTable.COLUMN_NAME_MODEL,
                CarTable.COLUMN_NAME_YEAR,
                CarTable.COLUMN_NAME_ENGINE,
                CarTable.COLUMN_NAME_IMAGE
        };

        String sortOrder =
                CarTable._ID + " DESC";
        Cursor cursor = db.query(
                CarTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CarTable._ID));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_BRAND));
            String model = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_MODEL));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_YEAR));
            String engine = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_ENGINE));
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_IMAGE));
            scoreRows.add(new CarEntity(itemId, brand, model, year, engine, DbBitmapUtility.getImage(imgByte)));
        }
        cursor.close();
    }
}
