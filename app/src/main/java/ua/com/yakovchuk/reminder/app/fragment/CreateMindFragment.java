package ua.com.yakovchuk.reminder.app.fragment;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.internal.view.menu.MenuItemImpl;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.lib.AlarmTime;

public class CreateMindFragment extends Fragment implements LocationListener, View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, MenuItemImpl.OnMenuItemClickListener {
    protected LocationManager locationManager;
    private double latitude;
    private double longitude;
    private TextView dateText;
    private TextView timeText;
    private Button remindBtn;
    private Button setup;
    private Calendar calendar;
    private Date remindDate = null;

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        SimpleDateFormat date = new SimpleDateFormat("yyyy, dd MMMM");
        calendar = Calendar.getInstance();
        dateText = (TextView) getActivity().findViewById(R.id.date_text_view);
        timeText = (TextView) getActivity().findViewById(R.id.time_text_view);
        remindBtn = (Button) getActivity().findViewById(R.id.remind);
        setup = (Button) getActivity().findViewById(R.id.setTest);
        remindBtn.setOnClickListener(this); // setup listener from interface
        setup.setOnClickListener(this); // setup listener from interface
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remind:
                createDateDialog();
                createTimeDialog();
                break;
            case R.id.setTest:
                saveMind();
                break;
        }
    }

    public void createDateDialog() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "TimePicker Dialog");
    }

    public void createTimeDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.vibrate(true);
        dpd.show(getFragmentManager(), "Datepicker Dialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1) {
        calendar.set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, i1);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy dd MMMM HH:mm");
        remindBtn.setText(simpleDateFormat.format(calendar.getTime()));
        this.remindDate = calendar.getTime();
        Toast.makeText(getActivity(), remindDate.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int i, int i1, int i2) {
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
    }

    public void saveMind() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy dd MMMM HH:mm");
        if (remindDate != null) {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getActivity(), AlarmTime.class); // AlarmTime.class required for receiver
            intent.putExtra("title", "Notification title");
            intent.putExtra("body", "Created at ");
            intent.putExtra("created_time", simpleDateFormat.format(new Date()));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0,
                    intent, PendingIntent.FLAG_ONE_SHOT);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindDate.getTime(), pendingIntent);
            Toast.makeText(getActivity(), "Created!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "NOT Created! Because date is false", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.okay_button:
                Toast.makeText(getActivity(), "Menu Item OK Button", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
