package com.example.nutrinet.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

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
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] produceNameList;
    static String[] tokens;
    ArrayList<ProduceNames> arraylist = new ArrayList<ProduceNames>();
    boolean searchStatus = false;
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

        //the length of the food categories string array
        /*(for (int i = 0; i < produceNameList.length; i++) {

            ProduceNames produceNames = new ProduceNames(produceNameList[i]);


            //ProduceNames produceNames = new ProduceNames(produceNameList[i]);
            // Binds all strings into an array
            arraylist.add(produceNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);*/

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
        //String text = newText;
        //adapter.filter(text);
        /*if(TextUtils.isEmpty(newText))
        {

        }
        else
        {

        }*/
        return false;
    }

    public void getKrogerProduce() throws IOException {
        Log.d("SearchActivity", "Start getKrogerProduce");
        OkHttpClient client = new OkHttpClient();

        Log.d("SearchActivity", "MediaType and RequestBody");
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&scope=product.compact");

        Log.d("SearchActivity", "All variables initializing");
        String CLIENT_ID = BuildConfig.ApiKey;
        String CLIENT_SECRET = BuildConfig.ApiSecret;
        String encodedData = DatatypeConverter.printBase64Binary((CLIENT_ID + ":" + CLIENT_SECRET).getBytes("UTF-8"));
        String authorizationHeaderString = "Basic " + encodedData;

        Log.d("SearchActivity", "Actual Request starting");
        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/connect/oauth2/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", authorizationHeaderString)
                .build();

        Log.d("SearchActivity", "Response");
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
                    Log.d("SearchActivity", "Success" + yourResponse);
//                    Parses the response, tokens[5] is the accessToken
                    String delims = "[\"]+";
                    tokens = yourResponse.split(delims);

                    //getListOfProduce(tokens[5]);
                }else{
                    Log.d("SearchActivity", "Not Successful" + yourResponse);
                }
            }
        });
        Log.d("SearchActivity", "Response called");


    }
    public void getListOfProduce(String accessToken, String query) throws IOException {
        Log.d("SearchActivity", "Start getListOfProduce");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        String url = "https://api.kroger.com/v1/products?filter.brand=Kroger&filter.term="+query;
        Log.d("SearchActivity", "new Request");
        String authorizationToken = "Bearer " + accessToken;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorizationToken)
                .build();

        Log.d("SearchActivity", "Response");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String yourResponse = response.body().string();
                if(response.isSuccessful()){
                    Log.d("SearchActivity", "Success" + yourResponse);
                    try {
                        JSONObject jsonObject = new JSONObject(yourResponse);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        arraylist.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject explrObject = jsonArray.getJSONObject(i);
                            ProduceNames produceNames = new ProduceNames();
                            produceNames.setId(explrObject.getString("productId"));
                            produceNames.setProduceName(explrObject.getString("description"));

                            JSONArray jsonArrayImages = explrObject.getJSONArray("images");
                            JSONObject jsonArrayObj = jsonArrayImages.getJSONObject(0);
                            JSONArray jsonArraySizes = jsonArrayObj.getJSONArray("sizes");
                            JSONObject jsonUrl = jsonArraySizes.getJSONObject(0);
                            produceNames.setImage(jsonUrl.getString("url"));

                            //JSONObject jsonArrayObj = jsonArrayImages.getJSONObject(0);

                            //JSONArray jsonArraySizes =

                            arraylist.add(produceNames);
                        }

                        // Pass results to ListViewAdapter Class
                        adapter = new ListViewAdapter(getApplicationContext(), arraylist);

                        // Binds the Adapter to the ListView
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Runnable","Im called");
                                list.setAdapter(null);
                                list.setAdapter(adapter);
                            }
                        });


                        // Locate the EditText in listview_main.xml
                        //editsearch = (SearchView) findViewById(R.id.search);
                        //editsearch.setOnQueryTextListener(getCallingActivity());
                        /*JSONArray jsonArray = new JSONArray(yourResponse);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                        }*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("SearchActivity", "Fail within JSONArray");
                    }
                }else{
                    Log.d("SearchActivity", "Not Successful" + yourResponse);
                }
            }
        });
        Log.d("SearchActivity", "Response called");
    }

    public boolean searchStatus(boolean searchStatus)
    {
        searchStatus = true;
        return searchStatus;
    }
}