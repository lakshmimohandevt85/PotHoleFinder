
package com.sdsu.cs646.lakshmi.assignment4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void mapView(View button)
    {
        Intent mapView = new Intent(this, MultiplePoleMapsActivity.class);
        startActivity(mapView);
        Toast.makeText(getApplicationContext(), (getResources().getString(R.string.mapView)),
                Toast.LENGTH_SHORT).show();
    }

    public void postReport(View button)
    {
        Intent postReport = new Intent(this, PostReportsActivity.class);
        startActivity(postReport);
        Toast.makeText(getApplicationContext(), (getResources().getString(R.string.postReport)),
                Toast.LENGTH_SHORT).show();
    }

    public void ViewReports(View button)
    {
        Intent ViewReports = new Intent(this, ViewActivity.class);
        startActivity(ViewReports);
        Toast.makeText(getApplicationContext(), (getResources().getString(R.string.viewReports)),
                Toast.LENGTH_SHORT).show();
    }

}
