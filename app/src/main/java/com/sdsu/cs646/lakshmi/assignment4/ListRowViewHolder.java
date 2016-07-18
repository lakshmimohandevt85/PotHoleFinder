package com.sdsu.cs646.lakshmi.assignment4;

/**
 * Created by lakshmimohandev on 3/19/16.
 */
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
public class ListRowViewHolder extends RecyclerView.ViewHolder
{
    protected NetworkImageView thumbnail;
    protected TextView id;

    protected RelativeLayout recLayout;
    protected TextView longitude;
    protected TextView latitude;
    protected TextView date;
    protected TextView type;
    protected TextView desc;
    protected TextView imgtype;

    public ListRowViewHolder(View view)
    {
        super(view);
        this.thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        this.id = (TextView) view.findViewById(R.id.id);
        this.date = (TextView) view.findViewById(R.id.created_date);
        this.recLayout = (RelativeLayout) view.findViewById(R.id.recLayout);
        this.latitude = (TextView) view.findViewById(R.id.latitude);
        this.longitude = (TextView) view.findViewById(R.id.longitude);
        this.type = (TextView) view.findViewById(R.id.type);
        this.desc = (TextView) view.findViewById(R.id.url);
        this.imgtype = (TextView) view.findViewById(R.id.imagetype);
        view.setClickable(true);
    }
}

