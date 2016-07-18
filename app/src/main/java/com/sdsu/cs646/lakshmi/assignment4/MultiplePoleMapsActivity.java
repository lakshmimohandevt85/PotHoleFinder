package com.sdsu.cs646.lakshmi.assignment4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MultiplePoleMapsActivity extends FragmentActivity implements OnMapReadyCallback
{

    private GoogleMap mMap;

    Map<String, ListItems> mapData = new HashMap<String, ListItems>();
    int no_of_batches = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_pole_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        String pothHoleUrl = "";
        RequestQueue queue = Volley.newRequestQueue(this);

            pothHoleUrl = "http://bismarck.sdsu.edu/city/fromDate?type=street";
            JsonArrayRequest jsObjRequest = new JsonArrayRequest(pothHoleUrl, new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray jsonArray)
                {

                    try {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject post = jsonArray.getJSONObject(i);
                            ListItems item = new ListItems();
                            item.setId("" + post.getInt(getResources().getString(R.string.id)));
                            item.setType(post.getString(getResources().getString(R.string.type)));
                            item.setCreated_date(post.getString(getResources().getString(R.string.created)));
                            item.setLatitude("" + post.getDouble(getResources().getString(R.string.latitude)));
                            item.setLongitude("" + post.getDouble(getResources().getString(R.string.longitude)));
                            item.setImagetype(post.getString(getResources().getString(R.string.imagetype)));
                            item.setDescription(post.getString(getResources().getString(R.string.description)));

                            mapData.put(item.getId(), item);
                            LatLng location = new LatLng(post.getDouble(getResources().getString(R.string.latitude)), post.getDouble(getResources().getString(R.string.longitude)));
                            mMap.addMarker(new MarkerOptions().position(location).title(item.getId()));
                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener(){

                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                String _id = marker.getTitle();
                                Intent intent = new Intent(MultiplePoleMapsActivity.this, DetailedViewActivity.class);

                                intent.putExtra(getResources().getString(R.string.selectedId), _id);
                                intent.putExtra(getResources().getString(R.string.latitude), mapData.get(_id).getLatitude());
                                intent.putExtra(getResources().getString(R.string.longitude), mapData.get(_id).getLongitude());
                                intent.putExtra(getResources().getString(R.string.type), mapData.get(_id).getType());
                                intent.putExtra(getResources().getString(R.string.description), mapData.get(_id).getDescription());
                                intent.putExtra(getResources().getString(R.string.created), mapData.get(_id).getCreated());
                                intent.putExtra(getResources().getString(R.string.imagetype), mapData.get(_id).getImagetype());

                                startActivity(intent);

                                return false;
                            }
                        });

                        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback()
                        {
                            @Override
                            public void onMapLoaded()
                            {
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(7.0f));

                            }
                        });

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            });
            queue.add(jsObjRequest);
    }

}
