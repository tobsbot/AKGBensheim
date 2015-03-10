package de.akg_bensheim.akgbensheim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by tobiaserthal on 10.03.15.
 */
public class ToolBarSpinnerAdapter extends ArrayAdapter<String>{

    private CharSequence title;
    private int dropdownViewResourceId;
    private int titleViewResourceId;

    public ToolBarSpinnerAdapter(Context context, int dropdownViewResourceId, int titleViewResourceId, String[] objects, CharSequence title) {
        super(context, titleViewResourceId, objects);

        this.title = title;
        this.dropdownViewResourceId = dropdownViewResourceId;
        this.titleViewResourceId = titleViewResourceId;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        DropdownHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(dropdownViewResourceId, null);
            holder = new DropdownHolder();
            holder.txt01 = (TextView) convertView.findViewById(android.R.id.text1);

            convertView.setTag(holder);
        } else {
            holder = (DropdownHolder) convertView.getTag();
        }

        holder.txt01.setText(getItem(position));
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(titleViewResourceId, null);
            holder = new ViewHolder();
            holder.txt01 = (TextView) convertView.findViewById(android.R.id.text1);
            holder.txt02 = (TextView) convertView.findViewById(android.R.id.text2);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt01.setText(title);
        holder.txt02.setText(getItem(position).toUpperCase(Locale.GERMANY));

        holder.txt02.setTextColor(getContext().getResources().getColor(android.R.color.secondary_text_light));

        return convertView;
    }

    class ViewHolder {
        TextView txt01;
        TextView txt02;
    }

    class DropdownHolder {
        TextView txt01;
    }
}
