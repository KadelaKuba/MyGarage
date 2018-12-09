package com.example.kad0143.mygarageproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class NewCar extends Activity {

    EditText brand;
    EditText model;
    EditText year;
    EditText engine;
    ImageView carImage;
    Button saveCarButton;
    Button uploadImageButton;

    final int GET_FROM_GALLERY = 1;
    public static Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        brand = (EditText) findViewById(R.id.brandEdit);
        model = (EditText) findViewById(R.id.modelEdit);
        year = (EditText) findViewById(R.id.yearEdit);
        engine = (EditText) findViewById(R.id.engineEdit);
        carImage = (ImageView) findViewById(R.id.carImage);
        saveCarButton = (Button) findViewById(R.id.saveCarButton);
        uploadImageButton = (Button) findViewById(R.id.uploadImageButton);

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        Log.d("saving", "funguje to?");

        saveCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("saving", "clickSave");
                if (saveToDb(brand.getText().toString(), model.getText().toString(), year.getText().toString(), engine.getText().toString())) {
                    Toast.makeText(NewCar.this, "Uloženo", Toast.LENGTH_SHORT).show();
                    Log.d("saving", "OK");
                    startActivity(new Intent(NewCar.this, MainActivity.class));
                } else {
                    Log.d("saving", "chyba");
                    Toast.makeText(NewCar.this, "Chyba", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                carImage.setImageBitmap(bitmap);
                image = bitmap;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
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
        values.put(CarTable.COLUMN_NAME_IMAGE, DbBitmapUtility.getBytes(image));

        // Insert the new row, returning the primary key value of the new row
        long neWid = db.insert(CarTable.TABLE_NAME, null, values);
        Log.d("saving", "savetoDB");

        return true;
    }
}
