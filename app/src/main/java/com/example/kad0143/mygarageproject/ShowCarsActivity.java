package com.example.kad0143.mygarageproject;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowCarsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cars);

        TableLayout tl = (TableLayout) findViewById(R.id.table);
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
                CarTable.COLUMN_NAME_ENGINE
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
            scoreRows.add(new Car(itemId, brand, model, year, engine));
        }
        cursor.close();
        for (int i = 0; i < scoreRows.size(); i++) {
            Car row = scoreRows.get(i);
            TableRow tr1 = new TableRow(this);
            tr1.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            Log.d("ahoj", row.brand);
            TextView brand = new TextView(this);
            brand.setText(row.brand);
            tr1.addView(brand);

            TextView model = new TextView(this);
            model.setText(row.model);
            tr1.addView(model);

            TextView year = new TextView(this);
            year.setText(row.year);
            tr1.addView(year);

            TextView engine = new TextView(this);
            engine.setText(row.engine);
            tr1.addView(engine);

            tl.addView(tr1, new TableLayout.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        }
    }
}
