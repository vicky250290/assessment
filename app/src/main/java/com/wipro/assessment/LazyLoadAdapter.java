package com.wipro.assessment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by User1 on 01-06-2016.
 */
public class LazyLoadAdapter extends BaseAdapter {
    private Activity activity;
    private List<Item> itemList;
    private static LayoutInflater inflater = null;
    ImageLoader imageLoader;
    DisplayImageOptions thumbnailoptions;
    Bitmap noimage;

    public LazyLoadAdapter(Activity a, List<Item> itemsArray) {
        this.activity = a;
        inflater = (LayoutInflater) activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.itemList = itemsArray;
        noimage = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_noimg);
        imageLoader = ImageLoader.getInstance();
        thumbnailoptions = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_noimg).showImageOnFail(R.drawable.ic_noimg).cacheInMemory(true)
                .resetViewBeforeLoading(true).build();
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
        String imageUrl = itemList.get(position).getImageUrl();
        if (imageUrl == null || imageUrl == "null") {
            holder.imageView.setImageBitmap(noimage);
        } else {
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(imageUrl, holder.imageView, thumbnailoptions);
        }
        return vi;
    }

    public static class ViewHolder {
        public TextView titleView;
        public TextView descriptionView;
        public ImageView imageView;
    }
}
