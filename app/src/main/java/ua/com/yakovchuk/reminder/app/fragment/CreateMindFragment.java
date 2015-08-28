package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;

public class CreateMindFragment extends Fragment implements LocationListener {
    protected LocationManager locationManager;
    private double latitude;
    private double longitude;
    private TextView dateText;
    private TextView timeText;

    public final static String TAG = "CreateMindFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        return inflater.inflate(R.layout.create_mind_fragment, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm aaa");
        SimpleDateFormat date = new SimpleDateFormat("MM dd yyyy");
        dateText = (TextView) getActivity().findViewById(R.id.date_text_view);
        timeText = (TextView) getActivity().findViewById(R.id.time_text_view);
        dateText.setText(date.format(new Date()));
        timeText.setText(time.format(new Date()));
        getActivity().invalidateOptionsMenu();
    }

    public boolean isConnected() {
        ConnectivityManager connectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() != null) {
            if (connectivity.getActiveNetworkInfo().isConnected())
                return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if (isConnected()) {
            try {
                TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                textView.setText(addresses.get(0).getCountryName() +
                        ", " + addresses.get(0).getLocality() +
                        ", " + addresses.get(0).getAdminArea() +
                        ", " + addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
            textView.setText("You`re offline!");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        textView.setText("Location temporarily unavailable");
    }

    @Override
    public void onProviderEnabled(String s) {
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        textView.setText("Searching for location...");
    }

    @Override
    public void onProviderDisabled(String s) {
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        textView.setText("GPS is disabled");
    }

    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this); // close connection
    }
}
