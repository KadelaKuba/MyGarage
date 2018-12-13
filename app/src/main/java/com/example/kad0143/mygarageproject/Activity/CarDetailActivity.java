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
import android.widget.Toast;

import com.example.kad0143.mygarageproject.Database.Table.CarBasicInformationTable;
import com.example.kad0143.mygarageproject.Database.Table.OwnerDataTable;
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
    Button deleteCarButton;

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
        deleteCarButton = (Button) findViewById(R.id.deleteCarButton);

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

        deleteCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteCarFromDb(String.valueOf(entryId))) {
                    Toast.makeText(v.getContext(), "Auto bylo smazáno", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(CarDetailActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(v.getContext(), "Nepodařilo se smazat auto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDataForCarFromDb(String entryString) {
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


    private boolean deleteCarFromDb(String entryString) {
        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        db.delete(CarBasicInformationTable.TABLE_NAME, CarBasicInformationTable.COLUMN_NAME_CAR_ID + "=" + entryString, null);
        db.delete(OwnerDataTable.TABLE_NAME, OwnerDataTable.COLUMN_NAME_CAR_ID + "=" + entryString, null);

        return db.delete(CarTable.TABLE_NAME, CarTable._ID + "=" + entryString, null) > 0;
    }
}
