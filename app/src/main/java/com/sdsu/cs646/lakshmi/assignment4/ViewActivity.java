package com.sdsu.cs646.lakshmi.assignment4;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends Activity
{
    private static final String TAG = "RecyclerViewExample";
    private List<ListItems> listItemsList = new ArrayList<ListItems>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter adapter;
    private String jsonPothhole;
    private static final String pothHoleUrl_1 = "http://bismarck.sdsu.edu/city/batch?type=street&batch-number=";
    private static final String pothHoleUrl_2 = "&size=20";
    private ProgressDialog progressDialog;
    int batch_number =0;

    int json_arr_count = 0;
    int total_batch_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        getJSONArrayCount();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(Color.BLACK)
                        .build());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        updateList();
        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("SCROLL PAST UPDATE", "You hit me");
                int lastFirstVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);
                loadMore(jsonPothhole);
            }
        });
    }
    public void updateList()
    {
        String pothHoleUrl = pothHoleUrl_1 + ""+batch_number +pothHoleUrl_2;
        adapter = new RecyclerAdapter(ViewActivity.this, listItemsList);
        mRecyclerView.setAdapter(adapter);
        RequestQueue queue = Volley.newRequestQueue(this);
        adapter.clearAdapter();
        showPD();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(pothHoleUrl, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray)
            {
                hidePD();
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

                        listItemsList.add(item);
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                // update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error" + error.getMessage());
                hidePD();
            }
        });
        queue.add(jsObjRequest);
    }

    public void loadMore(String pothHoleUrl)
    {

        batch_number= batch_number+1;
        if (batch_number == total_batch_count){
            return;
        }
        pothHoleUrl = pothHoleUrl_1 + ""+batch_number +pothHoleUrl_2;;
        adapter = new RecyclerAdapter(ViewActivity.this, listItemsList);
        mRecyclerView.setAdapter(adapter);
        RequestQueue queue = Volley.newRequestQueue(this);
        showPD();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(pothHoleUrl, new Response.Listener<JSONArray>()
        {
            @Override
            public void onResponse(JSONArray jsonArray)
            {
                hidePD();
                try
                {
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

                        listItemsList.add(item);
                    }
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                // update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error" + error.getMessage());
                hidePD();
            }
        });
        queue.add(jsObjRequest);
    }

    private void getJSONArrayCount(){

        String _url = "http://bismarck.sdsu.edu/city/count?type=street";
        RequestQueue queue = Volley.newRequestQueue(this);

        // prepare the Request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, _url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        try {
                            json_arr_count = response.getInt("count");
                            total_batch_count = (int) Math.ceil(json_arr_count/20);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        queue.add(jsonObjectRequest);

    }
    private void showPD()
    {
        if(progressDialog == null)
        {
            progressDialog  = new ProgressDialog(this);
            progressDialog .setMessage("Loading...");
            progressDialog .setCancelable(false);
            progressDialog .setCanceledOnTouchOutside(false);
            progressDialog .show();
        }
    }
    // function to hide the loading dialog box
    private void hidePD()
    {
        if (progressDialog  != null)
        {
            progressDialog .dismiss();
            progressDialog  = null;
        }
    }
    // Stop app from running
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        hidePD();
    }

}
