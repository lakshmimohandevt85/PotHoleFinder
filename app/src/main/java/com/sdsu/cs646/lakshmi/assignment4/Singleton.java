package com.sdsu.cs646.lakshmi.assignment4;

/**
 * Created by lakshmimohandev on 3/19/16.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class Singleton
{
    private static Singleton mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private Singleton(Context context)
    {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(
                LruBitmapCache.getCacheSize(mCtx)));
    }
    public static synchronized Singleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new Singleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
    public ImageLoader getImageLoader()
    {
        return mImageLoader;
    }
}
