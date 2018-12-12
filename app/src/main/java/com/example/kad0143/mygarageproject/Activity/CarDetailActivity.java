package com.example.kad0143.mygarageproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kad0143.mygarageproject.Entity.CarEntity;
import com.example.kad0143.mygarageproject.Database.Table.CarTable;
import com.example.kad0143.mygarageproject.Database.Helper.SQLiteHelper;
import com.example.kad0143.mygarageproject.Database.DbBitmapUtility;
import com.example.kad0143.mygarageproject.R;

public class CarDetailActivity extends Activity {

    TextView carBrandDetail;
    TextView carModelDetail;
    TextView carYearDetail;
    TextView carEngineDetail;
    ImageView carImageDetail;
    Button carInfomationsButton;

    private CarEntity car;
    private Long entryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        carBrandDetail = (TextView) findViewById(R.id.carBrandDetail);
        carModelDetail = (TextView) findViewById(R.id.carModelDetail);
        carYearDetail = (TextView) findViewById(R.id.carYearDetail);
        carEngineDetail = (TextView) findViewById(R.id.carEngineDetail);
        carImageDetail = (ImageView) findViewById(R.id.carImageDetail);
        carInfomationsButton = (Button) findViewById(R.id.carInformationsButton);

        entryId = getIntent().getExtras().getLong("entryId");
        if (!entryId.equals("")) {
            getDataForCarFromDb(String.valueOf(entryId));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }

        carInfomationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CarDetailActivity.this, CarInformationActivity.class);
                intent.putExtra("entryId", entryId);
                startActivity(intent);
            }
        });
    }

    private void getDataForCarFromDb(String entryString) {

        SQLiteHelper mDbHelper = new SQLiteHelper(this);
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
                "_id = " + entryString,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            // TODO JK zkontrolovat dat.typy
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CarTable._ID));
            String brand = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_BRAND));
            String model = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_MODEL));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_YEAR));
            String engine = cursor.getString(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_ENGINE));
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndexOrThrow(CarTable.COLUMN_NAME_IMAGE));
            car = new CarEntity(itemId, brand, model, year, engine, DbBitmapUtility.getImage(imgByte));
        }
        cursor.close();

        carBrandDetail.setText(car.brand);
        carModelDetail.setText(car.model);
        carYearDetail.setText(car.year);
        carEngineDetail.setText(car.engine);
        carImageDetail.setImageBitmap(car.image);
    }
}
