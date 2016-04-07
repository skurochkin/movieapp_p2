package com.ninjawebzen.movieapp_p2.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninjawebzen.movieapp_p2.R;
import com.ninjawebzen.movieapp_p2.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends BaseAdapter {

    List<Trailer> mTrailerList;
    Context mContext;

    public TrailerAdapter(List<Trailer> pTrailerList, Context pContext){
        mTrailerList = pTrailerList;
        mContext = pContext;
    }

    @Override
    public int getCount() {
        return mTrailerList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTrailerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("MovieApp", String.valueOf(position));
        View trailerView;
        if (convertView == null) {
            trailerView = View.inflate(parent.getContext(), R.layout.view_trailer_item, null);
        } else {
            trailerView = convertView;
        }
        Trailer trailerItem = (Trailer) getItem(position);
        ((TextView) trailerView.findViewById(R.id.trailer_name)).setText(trailerItem.getName());
        ImageView previewImageView = (ImageView) trailerView.findViewById(R.id.trailer_preview);

        String url = "http:/img.youtube.com/vi/"+trailerItem.getKey()+"/default.jpg";
        Picasso.with(mContext)
                .load(url)
                .into(previewImageView);

        return trailerView;
    }

}
