package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.lib.LocationGPS;

public class MainFragment extends Fragment {

    public final static String TAG = "MainFragment";
    private Button go;
    private LocationGPS gps;
    private Location location;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(), LocationGPS.class));
        return inflater.inflate(R.layout.main_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        /*gps = new LocationGPS(getActivity().getApplicationContext());
        TextView textView = (TextView) getActivity().findViewById(R.id.textView4);
        if (gps.canGetLocation()) {
            textView.setText(gps.getLocationName());
        } else {
            textView.setText("Cannot get location");
        }*/
        getActivity().invalidateOptionsMenu();
    }
}
