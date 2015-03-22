package de.tobiaserthal.akgbensheim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import de.tobiaserthal.akgbensheim.R;

public class ToolBarSpinnerAdapter extends BaseAdapter{

    private CharSequence title;
    private ArrayList<String> items;

    public ToolBarSpinnerAdapter(String title) {
        super();
        this.title = title;
        this.items = new ArrayList<>();
    }

    public void addItem(String title) {
        items.add(title);
    }

    public void addItems(String[] titles) {
        Collections.addAll(items, titles);
    }

    public void clear() {
        items.clear();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int index) {
        return items.get(index);
    }

    public String getTitle(int index) {
        return index >= 0 && index < getCount() ?
                getItem(index).toString() : "";
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null || !convertView.getTag().toString().equals("DROPDOWN")) {
            convertView = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
            convertView.setTag("DROPDOWN");
        }

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
        textView.setText(getTitle(position));
        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null || !convertView.getTag().toString().equals("NON_DROPDOWN")) {
            convertView = ((LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.toolbar_spinner_item_toolbar, parent, false);
            convertView.setTag("NON_DROPDOWN");
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        textView1.setText(title);

        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);
        textView2.setText(getTitle(position).toUpperCase(Locale.getDefault()));
        return convertView;
    }
}
