package com.sdsu.cs646.lakshmi.assignment4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class PostReportsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,OnClickListener
{

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    Button buttonTakePhoto;
    ImageView imageTakenPhoto;
    EditText description;
    Button submit;
    PostReportsData postReportsData;
    static final int REQUEST_TAKE_PHOTO = 1;
    private  Double latitude;
    private  Double longitude;
    byte[] byteArray;
    private static final String IMG_TYPE= "jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reports);
        buttonTakePhoto = (Button) findViewById(R.id.button1);
        imageTakenPhoto = (ImageView) findViewById(R.id.imageviewPhoto);
        description = (EditText)findViewById(R.id.editText_desc);
        submit = (Button) findViewById(R.id.buttonSubmit);
        // add click listener to Button "POST"
        submit.setOnClickListener(this);
        buildGoogleApiClient();

    }

    public void POST( PostReportsData postReportsData)
    {
        String url = "http://bismarck.sdsu.edu/city/report";
        JSONObject data = new JSONObject();
        try
        {
            data.put(getResources().getString(R.string.latitude), latitude);
            data.put(getResources().getString(R.string.user), getResources().getString(R.string.RedId));
            data.put(getResources().getString(R.string.longitude), longitude);
            data.put(getResources().getString(R.string.type), getResources().getString(R.string.street));
            data.put(getResources().getString(R.string.description), description.getText());

            if (null != byteArray)
            {
                // image object:
                String bin64EncodedImage = Base64.encodeToString(byteArray, Base64.NO_WRAP);
                data.put("image", bin64EncodedImage);
                data.put(getResources().getString(R.string.imagetype), IMG_TYPE);
            }
            else
            {
                data.put(getResources().getString(R.string.imagetype), "none");
            }
        }
        catch (JSONException error)
        {
            error.printStackTrace();

        }
        Response.Listener<JSONObject> success = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

            }
        };
        Response.ErrorListener failure = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        };
        JsonObjectRequest postRequest = new JsonObjectRequest(url, data, success, failure);
        RequestQueue queue = Volley.newRequestQueue(PostReportsActivity.this);
        queue.add(postRequest);

    }
    @Override
    public void onClick(View view)
    {
        postReportsData = new PostReportsData();
        postReportsData.setDescription(description.getText().toString());
        POST(postReportsData);
        Toast.makeText(getApplicationContext(), "Report Submitted !",
                Toast.LENGTH_SHORT).show();
    }
   protected synchronized void buildGoogleApiClient()
    {
        Log.d("Building", "Building");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    protected void onStart()
    {
        mGoogleApiClient.connect();
        super.onStart();
    }
    protected void onPause()
    {
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    public String filename()
    {
        return "JPEG_FILE.jpg";
    }
    private File imageFile()
    {
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        String file = dir + filename();
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e)
        {
        }
        return newfile;
    }
    public void dispatchTakePictureIntent(View button)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = imageFile();
            if (photoFile != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK)
        {
            File photoFile = imageFile();
            if (photoFile != null)
            {
                Bitmap bitmap = BitmapFactory.decodeFile(imageFile().getPath());
                imageTakenPhoto.setImageBitmap((bitmap));

                imageTakenPhoto.setImageBitmap(decodeSampledBitmapFromFile(imageFile().getAbsolutePath(), 500, 250));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byteArray = baos.toByteArray();
        }
    }}

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                     int reqWidth, int reqHeight)
    {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
        if (height > reqHeight)
        {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth)
        {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
    public void back(View button)
    {
        finish();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        Log.d("CONNECTED", "I am connected");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)
        {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null)
        {
            Log.d("Latitude", String.valueOf(mLastLocation.getLatitude()));
            Log.d("Longitude", String.valueOf(mLastLocation.getLongitude()));
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    }
    @Override
    public void onConnectionSuspended(int i)
    {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }
}