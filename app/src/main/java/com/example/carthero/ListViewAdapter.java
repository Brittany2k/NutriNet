package com.example.carthero;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

    public class ViewHolder {
        TextView name;
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
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(produceNamesList.get(position).getProduceName());
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
