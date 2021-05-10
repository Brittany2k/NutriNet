package com.example.nutrinet.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private TextView mTextView;
    private TextView nutritionTextView;
    ListView list;
    ListViewAdapter adapter;
    private Spinner spinner;
    private String[] paths = new String[0];
    SearchView editsearch;
    String[] produceNameList;
    static String[] tokens;
    ArrayList<ProduceNames> arraylist = new ArrayList<ProduceNames>();
    boolean searchStatus = false;
    String foodResponse ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);

        mTextView = (TextView) findViewById(R.id.text);

        try{
            getKrogerProduce();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        //this would be used for search for foods by categories
        produceNameList = new String[]{"Fruits", "Vegetables", "Protein",
                "Dairy", "Grains", "Oils"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);
        nutritionTextView = (TextView) findViewById(R.id.nutrition_facts);

        //the length of the food categories string array


        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);


    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            getListOfProduce(tokens[5], query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action:
                finish();
                System.exit(0);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    public String getFoodResponse()
    {
        return foodResponse;
    }

    public void getNutritionInfo(String key, String query)
    {
        final String[] responser = new String[1];
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=" + key + "&query="
                + query + "&numberOfResultsPerPage=1&pageSize=1&dataType=Survey%20(FNDDS)";
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", key)
                .build();



        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Will return a string with the response
                final String yourResponse = response.body().string();
                if(response.isSuccessful()){

                    try {
                        foodResponse = FindandDisplayFood(yourResponse);

                        //nutritionTextView.setText(foodResponse);


                    } catch (InterruptedException | JSONException e) {

                        e.printStackTrace();
                    }
                }else{

                }
            }
        });

    }

    //finds the general food item of a given search and displays basic nutrition facts
    public static String FindandDisplayFood(String item) throws IOException, InterruptedException, JSONException {
        JSONObject body = new JSONObject(item);        //save request response as json object


        JSONArray foodArr = body.getJSONArray("foods"); //get food json array


        for(int i = 0; i < foodArr.length(); i++)   //find item matching
        {
            JSONObject currentItem = foodArr.getJSONObject(i);

            if(stringContainsItemFromList((String)currentItem.get("lowercaseDescription"), item.toLowerCase().split(" ")))
            {

                return GetTopNutrients(currentItem.getJSONArray("foodNutrients"));
            }

        }

        return "No matching nutritional data found for item " + item;
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).anyMatch(inputStr::contains);
    }
    //returns a \n separated string of nutrition info
    private static String GetTopNutrients(JSONArray nutrientsArray) throws JSONException {
        String calorieLine = "";
        String result = "";

        for(int i = 0; i < nutrientsArray.length();i++)
        {
            JSONObject currentNutrient = nutrientsArray.getJSONObject(i);
            String unit = (String)currentNutrient.get("nutrientName");
            if(unit.equals("Fatty acids, total monounsaturated") || unit.equals("Fatty acids, total polyunsaturated")
                    || unit.contains("Carbohydrate") || unit.equals("Sodium")
                    || unit.equals("Protein") || unit.equals("Cholesterol"))

                result += GetItemNutrient(currentNutrient) + "\n";
            if(unit.equals("Energy"))
                calorieLine = GetItemNutrient(currentNutrient) + "\n";
        }

        return calorieLine + result;

    }

    //returns a single line of nutrition info
    private static String GetItemNutrient(JSONObject nutrient)
    {
        try {
            if(nutrient.get("nutrientName").equals("Energy"))
            {
                return "Calories:" + nutrient.get("value") + " Calories";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return nutrient.get("nutrientName") + ":" + nutrient.get("value") + nutrient.get("unitName").toString().toLowerCase();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getKrogerProduce() throws IOException {

        OkHttpClient client = new OkHttpClient();


        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&scope=product.compact");

        String CLIENT_ID = BuildConfig.ApiKey;
        String CLIENT_SECRET = BuildConfig.ApiSecret;
        String encodedData = DatatypeConverter.printBase64Binary((CLIENT_ID + ":" + CLIENT_SECRET).getBytes("UTF-8"));
        String authorizationHeaderString = "Basic " + encodedData;

        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/connect/oauth2/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", authorizationHeaderString)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Will return a string with the response of the access token, includes expires_in, access_token, and token_type
                final String yourResponse = response.body().string();
                if(response.isSuccessful()){

//                    Parses the response, tokens[5] is the accessToken
                    String delims = "[\"]+";
                    tokens = yourResponse.split(delims);


                }

            }
        });



    }
    public void getListOfProduce(String accessToken, String query) throws IOException {


        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = "https://api.kroger.com/v1/products?filter.brand=Kroger&filter.term="+query;

        String authorizationToken = "Bearer " + accessToken;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorizationToken)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String yourResponse = response.body().string();
                if(response.isSuccessful()){

                    try {
                        JSONObject jsonObject = new JSONObject(yourResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        arraylist.clear();
                        boolean anyItemAvailable = false;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject explrObject = jsonArray.getJSONObject(i);

//                            Checks if the category of the item is acceptable (must be a food item)
                            if(checkCategory(explrObject.getString("categories"))) {
                                anyItemAvailable = true;
                                ProduceNames produceNames = new ProduceNames();
                                produceNames.setId(explrObject.getString("productId"));
                                produceNames.setProduceName(explrObject.getString("description"));


                                String key = "b2rbH8bEoIa1CeNb4Hi9Fa6K3b72SA5Nv3i5A5k2";
                                getNutritionInfo(key, explrObject.getString("description"));


                                JSONArray jsonArrayImages = explrObject.getJSONArray("images");
                                JSONObject jsonArrayObj = jsonArrayImages.getJSONObject(0);
                                JSONArray jsonArraySizes = jsonArrayObj.getJSONArray("sizes");
                                JSONObject jsonUrl = jsonArraySizes.getJSONObject(0);
                                produceNames.setImage(jsonUrl.getString("url"));
                                produceNames.setNutrition(foodResponse);
                                arraylist.add(produceNames);
                            }
                        }
                        if(!anyItemAvailable)
                        {
                            ProduceNames produceNames = new ProduceNames();
                            produceNames.setProduceName("NO ITEMS ARE AVAILABLE WITH THIS SEARCH CRITERIA, PLEASE TRY AGAIN");
                            arraylist.add(produceNames);

                        }

                        // Pass results to ListViewAdapter Class
                        adapter = new ListViewAdapter(getApplicationContext(), arraylist);

                        // Binds the Adapter to the ListView
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                list.setAdapter(null);
                                list.setAdapter(adapter);

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }
        });

    }

    public boolean searchStatus(boolean searchStatus)
    {
        searchStatus = true;
        return searchStatus;
    }
    public boolean checkCategory(String currCategory){
        String[] acceptableCategories = {"[\"Dairy\"]", "[\"Breakfast\"]", "[\"Natural & Organic\"]", "[\"Meat & Seafood\"]", "[\"Baking Goods\"]", "[\"Bakery\"]", "[\"Frozen\"]", "[\"Produce\"]", "[\"Canned & Packaged\"]", "[\"Beverages\"]", "[\"Pasta, Sauces, Grain\"]", "[\"Condiment & Sauces\"]", "[\"Snacks\"]", "[\"International\"]"};
        for(int i = 0; i < acceptableCategories.length; i++)
        {
            if(currCategory.equals(acceptableCategories[i]))
                return true;
        }
        return false;
    }
}