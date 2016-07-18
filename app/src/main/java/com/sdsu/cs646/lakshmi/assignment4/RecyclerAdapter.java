package com.sdsu.cs646.lakshmi.assignment4;

/**
 * Created by lakshmimohandev on 3/19/16.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<ListRowViewHolder>
{
    private List<ListItems> listItemsList;
    private Context mContext;
    private ImageLoader mImageLoader;
    private int focusedItem = 0;

    public RecyclerAdapter(Context context, List<ListItems> listItemsList)
    {
        this.listItemsList = listItemsList;
        this.mContext = context;
    }
    @Override
    public ListRowViewHolder onCreateViewHolder(final ViewGroup viewGroup, int position)
    {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        ListRowViewHolder holder = new ListRowViewHolder(v);
        holder.recLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("List Size", Integer.toString(getItemCount()));
                TextView textView_id = (TextView) v.findViewById(R.id.id);
                TextView textView_latitude = (TextView) v.findViewById(R.id.latitude);
                TextView textView_longitude = (TextView) v.findViewById(R.id.longitude);
                TextView textView_type = (TextView) v.findViewById(R.id.type);
                TextView textView_description = (TextView) v.findViewById(R.id.url);
                TextView textView_created = (TextView) v.findViewById(R.id.created_date);
                TextView textView_imagetype = (TextView) v.findViewById(R.id.imagetype);

                String id = ""+textView_id.getText();
                String latitude = ""+textView_latitude.getText();
                String longitude = ""+textView_longitude.getText();
                String type = ""+textView_type.getText();
                String description = ""+textView_description.getText();
                String created = ""+textView_created.getText();
                String imagetype = ""+textView_imagetype.getText();

                Intent intent = new Intent(mContext, DetailedViewActivity.class);
                intent.putExtra("selectedId", id);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("type", type);
                intent.putExtra("description", description);
                intent.putExtra("created", created);
                intent.putExtra("imagetype", imagetype);

                mContext.startActivity(intent);
            }
        });
        return holder;
    }
    @Override
    public void onBindViewHolder(final ListRowViewHolder listRowViewHolder, int position)
    {
        ListItems listItems = listItemsList.get(position);
        listRowViewHolder.itemView.setSelected(focusedItem == position);
        listRowViewHolder.getLayoutPosition();
        mImageLoader = Singleton.getInstance(mContext).getImageLoader();
        listRowViewHolder.thumbnail.setImageUrl(listItems.getThumbnail(), mImageLoader);
        listRowViewHolder.thumbnail.setDefaultImageResId(R.drawable.placeholder);

        listRowViewHolder.id.setText(listItems.getId());
        listRowViewHolder.date.setText((listItems.getCreated()));
        listRowViewHolder.type.setText(listItems.getType());

        listRowViewHolder.longitude.setText(listItems.getLongitude());
        listRowViewHolder.latitude.setText(listItems.getLatitude());
        listRowViewHolder.imgtype.setText(listItems.getImagetype());
        listRowViewHolder.desc.setText(listItems.getDescription());
    }

    public void clearAdapter()
    {
        listItemsList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount()
    {
        return (null != listItemsList ? listItemsList.size() : 0);
    }
}
