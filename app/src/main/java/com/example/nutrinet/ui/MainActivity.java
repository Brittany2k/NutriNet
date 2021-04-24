package com.example.nutrinet.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.textclassifier.ConversationActions;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] produceNameList;
    ArrayList<ProduceNames> arraylist = new ArrayList<ProduceNames>();
    String TAG = "GetKroger";
    public static AuthenticationPagerAdapter pagerAdapter;
    public static ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        viewPager = findViewById(R.id.viewPager);

        pagerAdapter = new AuthenticationPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragmet(new LoginFragment());
        pagerAdapter.addFragmet(new RegisterFragment());

        viewPager.setAdapter(pagerAdapter);
        try{
            getKrogerProduce();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Resume","Resume is called");

        //    final String sender=this.getIntent().getExtras().getString("Sender");


        //DETERMINE WHO STARTED THIS ACTIVITY
    }

    private void receiveData()
    {
        //RECEIVE DATA VIA INTENT
        Intent i = getIntent();
        String name = i.getStringExtra("Sender");
        viewPager.setCurrentItem(2);

    }


    public void getKrogerProduce() throws IOException {
        Log.d(TAG, "Start getKrogerProduce");
        OkHttpClient client = new OkHttpClient();

        Log.d(TAG, "MediaType and RequestBody");
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&scope=product.compact");

        Log.d(TAG, "All variables initializing");
        String CLIENT_ID = "nutrinet-8c08e27315708844ee777b3fe60068e15317180199662325940";
        String CLIENT_SECRET = "hDOSR2rtvE691Yub9S46tPOUY355a8OtFelJg2gz";
        String encodedData = DatatypeConverter.printBase64Binary((CLIENT_ID + ":" + CLIENT_SECRET).getBytes("UTF-8"));
        String authorizationHeaderString = "Basic " + encodedData;

        Log.d(TAG, "Actual Request starting");
        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/connect/oauth2/token")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", authorizationHeaderString)
                .build();

        Log.d(TAG, "Response");
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
                    Log.d(TAG, "Success" + yourResponse);
//                    Parses the response, tokens[5] is the accessToken
                    String delims = "[\"]+";
                    String[] tokens = yourResponse.split(delims);
                    getListOfProduce(tokens[5]);
                }else{
                    Log.d(TAG, "Not Successful" + yourResponse);
                }
            }
        });
        Log.d(TAG, "Response called");


    }
    public void getListOfProduce(String accessToken) throws IOException {
        Log.d(TAG, "Start getListOfProduce");

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Log.d(TAG, "new Request");
        String authorizationToken = "Bearer " + accessToken;
        Request request = new Request.Builder()
                .url("https://api.kroger.com/v1/products?filter.brand=Kroger&filter.term=milk")
                .get()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", authorizationToken)
                .build();

        Log.d(TAG, "Response");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String yourResponse = response.body().string();
                if(response.isSuccessful()){
                    Log.d(TAG, "Success" + yourResponse);
                }else{
                    Log.d(TAG, "Not Successful" + yourResponse);
                }
            }
        });
        Log.d(TAG, "Response called");
    }





    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        adapter.filter(text);
        return false;
    }

    class AuthenticationPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragmentList = new ArrayList<>();
        private FragmentManager fragMan;
        public AuthenticationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragmentList.get(i);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        void addFragmet(Fragment fragment) {
            fragmentList.add(fragment);
        }

        void removeFragment(Fragment fragment)
        {
            for(int i=0; i<fragmentList.size(); i++)
                fragMan.beginTransaction().remove(fragmentList.get(i)).commit();
            fragmentList.clear();
            fragmentList=new ArrayList<Fragment>();
            notifyDataSetChanged();

        }
    }
}