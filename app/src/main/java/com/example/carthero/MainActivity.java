package com.example.carthero;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Declare Variables
    ListView list;
    ListViewAdapter adapter;
    SearchView editsearch;
    String[] produceNameList;
    ArrayList<ProduceNames> arraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this would be used for search for foods by categories
        produceNameList = new String[]{"Fruits", "Vegetables", "Protein",
                "Dairy", "Grains", "Oils"};

        // Locate the ListView in listview_main.xml
        list = findViewById(R.id.listview);

        //the length of the food categories string array
        for (String s : produceNameList) {
            ProduceNames produceNames = new ProduceNames(s);
            // Binds all strings into an array
            arraylist.add(produceNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

    }
//The code relegating accessing the FDC API was implemented in Kotlin, will be utilized once we migrate the rest of the code from Kotlin to Java

//    Accesses the Food Data Central API, using a custom API key
//    https://fdc.nal.usda.gov/api-spec/fdc_api.html#/FDC/getFood docs regarding posting and getting foods
//    See the "Sample Calls" of (https://fdc.nal.usda.gov/api-guide.html)
//    Gives information on how to use the API


//    private fun accessFDC() {
//        val thread = Thread {
//            try {
//                val jsonString = URL("https://api.nal.usda.gov/fdc/v1/foods/search?api_key=<KEY GOES HERE>").readText()
//                Log.d(javaClass.simpleName, jsonString)
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//
//        thread.start()
//    }
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        return false;
    }
}