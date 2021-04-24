package com.example.nutrinet.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private List<ProduceNames> produceNamesList = null;
    private ArrayList<ProduceNames> arraylist;

    public ListViewAdapter(Context context, List<ProduceNames> produceNamesList) {
        mContext = context;
        this.produceNamesList = produceNamesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ProduceNames>();
        this.arraylist.addAll(produceNamesList);
    }
//
    public class ViewHolder {
        TextView name;
        ImageView image;
        TextView nutrition_info;
    }

    @Override
    public int getCount() {
        return produceNamesList.size();
    }

    @Override
    public ProduceNames getItem(int position) {
        return produceNamesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_views_items, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.image = (ImageView) view.findViewById(R.id.imageView);
            holder.nutrition_info = (TextView) view.findViewById(R.id.nutrition_facts);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        String thename = produceNamesList.get(position).getProduceName();
        holder.name.setText(produceNamesList.get(position).getProduceName());
        holder.nutrition_info.setText(produceNamesList.get(position).getNutrition());
        Picasso.with(mContext).load(produceNamesList.get(position).getImage()).into(holder.image);
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        produceNamesList.clear();
        //if the user enters a string that is empty, add all original categories
        if (charText.length() == 0) {
            produceNamesList.addAll(arraylist);
        } else {
            for (ProduceNames wp : arraylist) {
                if (wp.getProduceName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    produceNamesList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
