package com.example.kad0143.mygarageproject;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static ArrayList<CarModel> carModels;
    public static ArrayList<String> modelList;

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

        modelList = new ArrayList<String>();
        carModels = new ArrayList<CarModel>();

        new DownloadXmlTask().execute("https://raw.githubusercontent.com/matthlavacka/car-list/master/car-list.json");

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


    private class DownloadXmlTask extends AsyncTask<String, String, String> {

        private String TAG = MainActivity.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://raw.githubusercontent.com/matthlavacka/car-list/master/car-list.json";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    Log.d("kadela", jsonStr);
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    // looping through All Contacts

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String brand = c.getString("brand");

                        // Phone node is JSON Object
                        JSONArray models = c.getJSONArray("models");
                        for (int j = 0; j < models.length(); j++) {
                            modelList.add(models.getString(j));
                        }

                        CarModel carModel = new CarModel(brand, modelList);
                        carModels.add(carModel);

                        Log.d("sizeKadela", String.valueOf(carModels.size()));
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Spinner spinner = (Spinner) findViewById(R.id.spinner);

            List<String> brands = new ArrayList<String>();
            for (CarModel carModel : carModels) {
                brands.add(carModel.brand);
            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, brands);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            spinner.setAdapter(dataAdapter);
        }
    }
}
