package com.example.nutrinet.ui;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URI;
import org.apache.http.client.HttpClient;


import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Nutrition
{
    /*private static String key = "b2rbH8bEoIa1CeNb4Hi9Fa6K3b72SA5Nv3i5A5k2";

    //finds the general food item of a given search and displays basic nutrition facts
    public static String FindandDisplayFood(String item) throws IOException, InterruptedException
    {
        JSONObject body = new JSONObject(ItemRequest(item));        //save request response as json object
        JSONArray foodArr = new JSONArray(body.getJSONArray("foods")); //get food json array

        for(int i = 0; i < foodArr.length(); i++)   //find item matching
        {
            JSONObject currentItem = foodArr.getJSONObject(i);

            if(stringContainsItemFromList((String)currentItem.get("lowercaseDescription"), item.toLowerCase().split(" ")))
            {
                return GetTopNutrients(currentItem.getJSONArray("foodNutrients"));
            }

        }

        return "No matching nutritional data Found for item " + item;
    }

    //helper function that sends the web request
    private static String ItemRequest(String item) throws IOException, InterruptedException {



        item = item.replaceAll(" ", "%20");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().GET()
                .uri(URI.create("https://api.nal.usda.gov/fdc/v1/foods/search?api_key=" + key + "&query="
                        + item + "&numberOfResultsPerPage=1&pageSize=1&dataType=Survey%20(FNDDS)")).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return (String) response.body();
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        return Arrays.stream(items).anyMatch(inputStr::contains);
    }
    //returns a \n separated string of nutrition info
    private static String GetTopNutrients(JSONArray nutrientsArray)
    {
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
        if(nutrient.get("nutrientName").equals("Energy"))
        {
            return "Calories:" + nutrient.get("value") + " Calories";
        }

        return nutrient.get("nutrientName") + ":" + nutrient.get("value") + nutrient.get("unitName").toString().toLowerCase();

    }*/


}