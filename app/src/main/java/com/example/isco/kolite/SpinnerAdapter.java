package com.example.isco.kolite;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Isco on 6/23/2017.
 */
public class SpinnerAdapter extends ArrayAdapter<String> {

    private Activity context;
    String[] data = null;

    public SpinnerAdapter(Activity context, int resource, String[] data2)
    {
        super(context, resource, data2);
        this.context = context;
        this.data = data2;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            //inflate your customlayout for the textview
            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.list_spinner_row, parent, false);
        }
        String item = data[position];
        if(item != null)
        {
            TextView text1 = (TextView) row.findViewById(R.id.spinnertxtview);
//            text1.setTextColor(Color.BLACK);
            text1.setText(item);
        }

        return row;
    }
}