package com.wipro.assessment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User1 on 01-06-2016.
 */
public class LazyLoadAdapter extends BaseAdapter {
    private Activity activity;
    private List<Item> itemList;
    private static LayoutInflater inflater = null;

    public LazyLoadAdapter(Activity a, List<Item> itemsArray) {
        this.activity = a;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemList = itemsArray;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            vi = inflater.inflate(R.layout.listrow, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) vi.findViewById(R.id.titleView);
            holder.descriptionView = (TextView) vi.findViewById(R.id.descriptionView);
            holder.imageView = (ImageView) vi.findViewById(R.id.imageView);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        holder.titleView.setText(itemList.get(position).getTitle());
        holder.descriptionView.setText(itemList.get(position).getDescription());
        //holder.imageView.setImageBitmap();
        return vi;
    }

    public static class ViewHolder {
        public TextView titleView;
        public TextView descriptionView;
        public ImageView imageView;
    }
}
