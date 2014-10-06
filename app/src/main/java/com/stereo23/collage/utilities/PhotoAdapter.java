package com.stereo23.collage.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.stereo23.collage.R;

import java.net.URL;
import java.util.ArrayList;


public class PhotoAdapter extends BaseAdapter {
    Context context;
    ArrayList<URL> objects;

    public PhotoAdapter(Context _context, ArrayList<URL> photos) {
        context = _context;
        objects = photos;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public URL getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.photo_item, parent, false);
        }
        URL url = getPhoto(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        Picasso.with(context).load(url.toString()).into(imageView);
        return view;
    }

    URL getPhoto(int position) {
        return ((URL) getItem(position));
    }


}




