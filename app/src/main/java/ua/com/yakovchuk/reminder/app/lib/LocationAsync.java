package ua.com.yakovchuk.reminder.app.lib;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.activity.MainActivity;


public class LocationAsync extends AsyncTask <Void, Void, Void> {

    private LocationGPS locationGPS;
    private Context context;

    public LocationAsync (Context context) {
        this.context = context;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        locationGPS = new LocationGPS(context);
        publishProgress();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(context, "Starting....", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Toast.makeText(context, "DONE!", Toast.LENGTH_SHORT).show();
    }
}
