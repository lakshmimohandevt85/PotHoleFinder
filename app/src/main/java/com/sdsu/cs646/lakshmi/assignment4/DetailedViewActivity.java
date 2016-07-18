package com.sdsu.cs646.lakshmi.assignment4;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class DetailedViewActivity extends AppCompatActivity
{
    ImageView image;
    TextView textview;
    String _latitude;
    String _longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        image = (ImageView) findViewById(R.id.imageviewPhoto);
        textview = (TextView) findViewById(R.id.textview_desc);
        Intent intent = getIntent();
        String _id = ""+intent.getStringExtra(getResources().getString(R.string.selectedId));
        String latitude = ""+intent.getStringExtra(getResources().getString(R.string.latitude));
        String longitude = ""+intent.getStringExtra(getResources().getString(R.string.longitude));
        String type = ""+intent.getStringExtra(getResources().getString(R.string.type));
        String description = ""+intent.getStringExtra(getResources().getString(R.string.description));
        String created = ""+intent.getStringExtra(getResources().getString(R.string.created));
        String imagetype = ""+intent.getStringExtra(getResources().getString(R.string.imagetype));

        ListItems data = new ListItems();
        data.setId(_id);
        data.setLatitude(latitude);
        data.setLongitude(longitude);
        data.setType(type);
        data.setDescription(description);
        data.setImagetype(imagetype);
        data.setCreated_date(created);

        _latitude = latitude;
        _longitude = longitude;

        if(imagetype == null || "none".equals(imagetype))
        {
            Bitmap noImage = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
            image.setImageBitmap(noImage);
        }else
        {
            setJSONDatafromServer(_id);
        }

        showDescription(data);
    }

    /**
     *
     * @param selectedID
     */
    private void setJSONDatafromServer(String selectedID)
    {
        Response.Listener<Bitmap> success = new Response.Listener<Bitmap>()
        {
            @Override
            public void onResponse(Bitmap response)
            {

                if (null == response)
                {
                    Bitmap noImage = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                    image.setImageBitmap(noImage);
                }
                else
                {
                    image.setImageBitmap(response);
                }
            }
        };
        Response.ErrorListener failure = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Bitmap noImage = BitmapFactory.decodeResource(getResources(), R.drawable.noimage);
                image.setImageBitmap(noImage);
            }
        };

        String url = "http://bismarck.sdsu.edu/city/image?id=" +selectedID;
        ImageRequest ir = new ImageRequest(url,
                success, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, failure);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(ir);
    }

    private void showDescription(ListItems pothhole_data){

        StringBuffer sb = new StringBuffer();
        sb.append("Details:     \n");
        sb.append("Latitude:     "+pothhole_data.getLatitude());
        sb.append("\t");
        sb.append("Longitude:     "+pothhole_data.getLongitude());
        sb.append("\n");
        sb.append("Description:    \n");
        sb.append(pothhole_data.getDescription());

        textview.setText(sb.toString());

    }

    public void viewMap(View button)
    {
        Intent viewmap = new Intent(this, MapsActivity.class);
        viewmap.putExtra("_latitude", _latitude);
        viewmap.putExtra("_longitude", _longitude);

        startActivity(viewmap);
    }

    public void back(View button)
    {
        finish();
    }
}
