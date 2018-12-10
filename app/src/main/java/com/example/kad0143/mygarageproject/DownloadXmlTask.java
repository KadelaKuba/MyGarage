package com.example.kad0143.mygarageproject;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

class DownloadXmlTask extends AsyncTask<String, String, String> {

    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<String> modelList;
    ArrayList<CarModel> carModels;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... params) {
        HttpHandler sh = new HttpHandler();
        // Making a request to url and getting response
        String url = "https://raw.githubusercontent.com/matthlavacka/car-list/master/car-list.json";
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
        ArrayList<String> modelList = new ArrayList<String>();
        ArrayList<CarModel> carModels = new ArrayList<CarModel>();

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

}
