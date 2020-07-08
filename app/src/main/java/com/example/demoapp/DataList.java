package com.example.demoapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class DataList extends ArrayAdapter<Data> {
    private Activity context;
    private List<Data> dataList;
    public DataList(Activity context, List<Data> registrationList) {
        super(context, R.layout.list_layout, registrationList);
        this.context = context;
        this.dataList = registrationList;

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewIteam = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewname = (TextView) listViewIteam.findViewById(R.id.textView_lis_name);


        Data data = dataList.get(position);
        textViewname.setText(data.getItem());
        return listViewIteam;


    }
}
