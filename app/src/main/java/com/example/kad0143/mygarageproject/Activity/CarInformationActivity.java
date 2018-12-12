package com.example.kad0143.mygarageproject.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kad0143.mygarageproject.Database.Helper.SQLiteHelper;
import com.example.kad0143.mygarageproject.Database.Table.CarBasicInformationTable;
import com.example.kad0143.mygarageproject.Database.Table.OwnerDataTable;
import com.example.kad0143.mygarageproject.Entity.CarInformation;
import com.example.kad0143.mygarageproject.Entity.OwnerData;
import com.example.kad0143.mygarageproject.R;

public class CarInformationActivity extends Activity {

    TextView tachometerCurrentState;
    TextView registrationDate;
    TextView stkValidity;
    TextView doctorValidity;
    Button saveBasicInformationButton;
    TextView name;
    TextView phone;
    TextView street;
    TextView city;
    TextView postcode;
    Button saveOwnerDataButton;

    private CarInformation carInformation;
    private OwnerData ownerData;
    private Long entryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_information);

        tachometerCurrentState = (TextView) findViewById(R.id.tachometerCurrentState);
        registrationDate = (TextView) findViewById(R.id.registrationDate);
        stkValidity = (TextView) findViewById(R.id.stkValidity);
        doctorValidity = (TextView) findViewById(R.id.doctorValidity);
        saveBasicInformationButton = (Button) findViewById(R.id.saveBasicInformationButton);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        street = (TextView) findViewById(R.id.street);
        city = (TextView) findViewById(R.id.city);
        postcode = (TextView) findViewById(R.id.postcode);
        saveOwnerDataButton = (Button) findViewById(R.id.saveOwnerDataButton);

        entryId = getIntent().getExtras().getLong("entryId");
        if (entryId.equals("")) {
            startActivity(new Intent(this, MainActivity.class));
        }

        if (hasCarSomeInformations(entryId)) {
            getCarInformationsFromDb(entryId);
        }

        if (hasCarSomeOwnerData(entryId)) {
            getOwnerDataFromDb(entryId);
        }

        saveBasicInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHelper mDbHelper = new SQLiteHelper(v.getContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                db.delete(CarBasicInformationTable.TABLE_NAME, CarBasicInformationTable.COLUMN_NAME_CAR_ID + " = " + String.valueOf(entryId), null);

                ContentValues values = new ContentValues();
                values.put(CarBasicInformationTable.COLUMN_NAME_CAR_ID, entryId);
                values.put(CarBasicInformationTable.COLUMN_NAME_TACHOMETER, tachometerCurrentState.getText().toString());
                values.put(CarBasicInformationTable.COLUMN_NAME_REGISTRATION_DATE, registrationDate.getText().toString());
                values.put(CarBasicInformationTable.COLUMN_NAME_STK_VALIDITY, stkValidity.getText().toString());
                values.put(CarBasicInformationTable.COLUMN_NAME_DOCTOR_VALIDITY, doctorValidity.getText().toString());

                if (db.insert(CarBasicInformationTable.TABLE_NAME, null, values) >= 0) {
                    Toast.makeText(v.getContext(), "Uloženo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Chyba při ukladání", Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveOwnerDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteHelper mDbHelper = new SQLiteHelper(v.getContext());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                db.delete(OwnerDataTable.TABLE_NAME, OwnerDataTable.COLUMN_NAME_CAR_ID + " = " + String.valueOf(entryId), null);

                ContentValues values = new ContentValues();
                values.put(OwnerDataTable.COLUMN_NAME_CAR_ID, entryId);
                values.put(OwnerDataTable.COLUMN_NAME_NAME, name.getText().toString());
                values.put(OwnerDataTable.COLUMN_NAME_PHONE, phone.getText().toString());
                values.put(OwnerDataTable.COLUMN_NAME_STREET, street.getText().toString());
                values.put(OwnerDataTable.COLUMN_NAME_CITY, city.getText().toString());
                values.put(OwnerDataTable.COLUMN_NAME_POSTCODE, postcode.getText().toString());

                Log.d("asd", "asd");
                if (db.insert(OwnerDataTable.TABLE_NAME, null, values) >= 0) {
                    Toast.makeText(v.getContext(), "Uloženo", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(v.getContext(), "Chyba při ukladání", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getCarInformationsFromDb(Long entryString) {

        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CarBasicInformationTable._ID,
                CarBasicInformationTable.COLUMN_NAME_CAR_ID,
                CarBasicInformationTable.COLUMN_NAME_TACHOMETER,
                CarBasicInformationTable.COLUMN_NAME_REGISTRATION_DATE,
                CarBasicInformationTable.COLUMN_NAME_STK_VALIDITY,
                CarBasicInformationTable.COLUMN_NAME_DOCTOR_VALIDITY
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                CarBasicInformationTable._ID + " DESC";
        Cursor cursor = db.query(
                CarBasicInformationTable.TABLE_NAME,
                projection,
                "carId = " + String.valueOf(entryString),
                null,
                null,
                null,
                sortOrder,
                "1"
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(CarBasicInformationTable._ID));
            String carId = cursor.getString(cursor.getColumnIndexOrThrow(CarBasicInformationTable.COLUMN_NAME_CAR_ID));
            String tachometer = cursor.getString(cursor.getColumnIndexOrThrow(CarBasicInformationTable.COLUMN_NAME_TACHOMETER));
            String registrationDate = cursor.getString(cursor.getColumnIndexOrThrow(CarBasicInformationTable.COLUMN_NAME_REGISTRATION_DATE));
            String stkValidity = cursor.getString(cursor.getColumnIndexOrThrow(CarBasicInformationTable.COLUMN_NAME_STK_VALIDITY));
            String doctorValidity = cursor.getString(cursor.getColumnIndexOrThrow(CarBasicInformationTable.COLUMN_NAME_DOCTOR_VALIDITY));
            carInformation = new CarInformation(itemId, carId, tachometer, registrationDate, stkValidity, doctorValidity);
        }
        cursor.close();

        tachometerCurrentState.setText(carInformation.tachometer);
        registrationDate.setText(carInformation.registrationDate);
        stkValidity.setText(carInformation.stkValidity);
        doctorValidity.setText(carInformation.doctorValidity);
    }

    // TODO JK zkusit  sloucit s funcki nad, getCarInformationsFromDb()
    private boolean hasCarSomeInformations(Long entryString) {
        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                CarBasicInformationTable.COLUMN_NAME_CAR_ID,
        };

        Cursor cursor = db.query(
                CarBasicInformationTable.TABLE_NAME,
                projection,
                "carId = " + String.valueOf(entryString),
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }

    private void getOwnerDataFromDb(Long entryString) {

        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                OwnerDataTable._ID,
                OwnerDataTable.COLUMN_NAME_CAR_ID,
                OwnerDataTable.COLUMN_NAME_NAME,
                OwnerDataTable.COLUMN_NAME_PHONE,
                OwnerDataTable.COLUMN_NAME_STREET,
                OwnerDataTable.COLUMN_NAME_CITY,
                OwnerDataTable.COLUMN_NAME_POSTCODE
        };
        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                OwnerDataTable._ID + " DESC";
        Cursor cursor = db.query(
                OwnerDataTable.TABLE_NAME,
                projection,
                "carId = " + String.valueOf(entryString),
                null,
                null,
                null,
                sortOrder,
                "1"
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(OwnerDataTable._ID));
            String carId = cursor.getString(cursor.getColumnIndexOrThrow(OwnerDataTable.COLUMN_NAME_CAR_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(OwnerDataTable.COLUMN_NAME_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(OwnerDataTable.COLUMN_NAME_PHONE));
            String street = cursor.getString(cursor.getColumnIndexOrThrow(OwnerDataTable.COLUMN_NAME_STREET));
            String city = cursor.getString(cursor.getColumnIndexOrThrow(OwnerDataTable.COLUMN_NAME_CITY));
            String postcode = cursor.getString(cursor.getColumnIndexOrThrow(OwnerDataTable.COLUMN_NAME_POSTCODE));
            ownerData = new OwnerData(itemId, carId, name, phone, street, city, postcode);
        }
        cursor.close();

        name.setText(ownerData.name);
        phone.setText(ownerData.phone);
        street.setText(ownerData.street);
        city.setText(ownerData.city);
        postcode.setText(ownerData.postcode);
    }

    // TODO JK zkusit  sloucit s funcki nad, getCarInformationsFromDb()
    private boolean hasCarSomeOwnerData(Long entryString) {
        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                OwnerDataTable.COLUMN_NAME_CAR_ID,
        };

        Cursor cursor = db.query(
                OwnerDataTable.TABLE_NAME,
                projection,
                "carId = " + String.valueOf(entryString),
                null,
                null,
                null,
                null
        );

        if (cursor.getCount() > 0) {
            return true;
        }

        return false;
    }
}
