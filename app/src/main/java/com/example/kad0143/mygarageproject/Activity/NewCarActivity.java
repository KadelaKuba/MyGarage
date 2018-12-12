package com.example.kad0143.mygarageproject.Activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kad0143.mygarageproject.Entity.BrandWithModelsEntity;
import com.example.kad0143.mygarageproject.Database.Table.CarTable;
import com.example.kad0143.mygarageproject.Database.Helper.SQLiteHelper;
import com.example.kad0143.mygarageproject.Database.DbBitmapUtility;
import com.example.kad0143.mygarageproject.Network.HttpHandler;
import com.example.kad0143.mygarageproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewCarActivity extends Activity {

    EditText brand;
    EditText model;
    EditText year;
    EditText engine;
    ImageView carImage;
    Button saveCarButton;
    Button uploadImageButton;
    Spinner spinner;
    Spinner spinner2;

    final int GET_FROM_GALLERY = 1;
    public static Bitmap image;
    public static boolean isOfflineMode;

    public static ArrayList<BrandWithModelsEntity> carModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);

        // TODO JK - zkontrolovat dat. typy
        brand = (EditText) findViewById(R.id.brandEdit);
        model = (EditText) findViewById(R.id.modelEdit);
        year = (EditText) findViewById(R.id.yearEdit);
        engine = (EditText) findViewById(R.id.engineEdit);
        carImage = (ImageView) findViewById(R.id.carImage);
        saveCarButton = (Button) findViewById(R.id.saveCarButton);
        uploadImageButton = (Button) findViewById(R.id.uploadImageButton);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        carModels = new ArrayList<BrandWithModelsEntity>();

        new DownloadAndParseJson().execute("https://raw.githubusercontent.com/matthlavacka/car-list/master/car-list.json");

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        saveCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long carSavedToDb;

                if (isOfflineMode) {
                    // TODO JK přidat validaci pro ostatní pole
                    if (TextUtils.isEmpty(year.getText())) {
                        year.setError("Rok výroby je povinný údaj!");
                    } else if (TextUtils.isEmpty(engine.getText())) {
                        engine.setError("Typ motoru je povinný údaj");
                    } else {
                        carSavedToDb = saveToDb(brand.getText().toString(), model.getText().toString(), year.getText().toString(), engine.getText().toString(), DbBitmapUtility.getBytes(image));

                        if (carSavedToDb >= 0) {
                            Toast.makeText(NewCarActivity.this, "Uloženo", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewCarActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(NewCarActivity.this, "Chyba", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (TextUtils.isEmpty(year.getText())) {
                        year.setError("Year is required!");
                    } else {
                        image = ((BitmapDrawable) carImage.getDrawable()).getBitmap();
                        carSavedToDb = saveToDb(spinner.getSelectedItem().toString(), spinner2.getSelectedItem().toString(), year.getText().toString(), engine.getText().toString(), DbBitmapUtility.getBytes(image));

                        if (carSavedToDb >= 0) {
                            Toast.makeText(NewCarActivity.this, "Uloženo", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewCarActivity.this, MainActivity.class));
                        } else {
                            Toast.makeText(NewCarActivity.this, "Chyba", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                carImage.setImageBitmap(bitmap);
                image = bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long saveToDb(String brand, String model, String year, String engine, byte[] image) {
        SQLiteHelper mDbHelper = new SQLiteHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
//        int deletedRows = db.delete(CarTable.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();
        values.put(CarTable.COLUMN_NAME_BRAND, brand);
        values.put(CarTable.COLUMN_NAME_MODEL, model);
        values.put(CarTable.COLUMN_NAME_YEAR, year);
        values.put(CarTable.COLUMN_NAME_ENGINE, engine);
        values.put(CarTable.COLUMN_NAME_IMAGE, image);

        return db.insert(CarTable.TABLE_NAME, null, values);
    }


    private class DownloadAndParseJson extends AsyncTask<String, String, String> {

        private String TAG = MainActivity.class.getSimpleName();
        public ArrayList<String> modelList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String url = "https://raw.githubusercontent.com/matthlavacka/car-list/master/car-list.json";
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                isOfflineMode = false;
                try {
                    JSONArray jsonArray = new JSONArray(jsonStr);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String brand = c.getString("brand");

                        JSONArray models = c.getJSONArray("models");
                        modelList = new ArrayList<String>();
                        for (int j = 0; j < models.length(); j++) {
                            modelList.add(models.getString(j));
                        }

                        BrandWithModelsEntity carModel = new BrandWithModelsEntity(brand, modelList);
                        carModels.add(carModel);

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

            if (!isOfflineMode && carModels.size() > 0) {
                final List<String> brands = new ArrayList<String>();
                for (BrandWithModelsEntity carModel : carModels) {
                    brands.add(carModel.brand);
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, brands);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinner.setAdapter(dataAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        BrandWithModelsEntity carModel = carModels.get(position);

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, carModel.models);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                        spinner2.setAdapter(dataAdapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {

                    }

                });
            } else {
                // TODO JK ADD EditText for message
                brand.setVisibility(View.VISIBLE);
                model.setVisibility(View.VISIBLE);

                spinner.setVisibility(View.INVISIBLE);
                spinner2.setVisibility(View.INVISIBLE);
            }
        }
    }
}
