package com.example.nutrinet;

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
    ArrayList<ProduceNames> arraylist = new ArrayList<ProduceNames>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this would be used for search for foods by categories
        produceNameList = new String[]{"Fruits", "Vegetables", "Protein",
                "Dairy", "Grains", "Oils"};

        // Locate the ListView in listview_main.xml
        list = (ListView) findViewById(R.id.listview);

        //the length of the food categories string array
        for (int i = 0; i < produceNameList.length; i++) {
            ProduceNames produceNames = new ProduceNames(produceNameList[i]);
            // Binds all strings into an array
            arraylist.add(produceNames);
        }

        // Pass results to ListViewAdapter Class
        adapter = new ListViewAdapter(this, arraylist);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);

        // Locate the EditText in listview_main.xml
        editsearch = (SearchView) findViewById(R.id.search);
        editsearch.setOnQueryTextListener(this);

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
}