package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.entity.Mind;
import ua.com.yakovchuk.reminder.app.lib.AlarmTime;

public class CreateMindFragment extends Fragment implements LocationListener, View.OnClickListener,
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    protected LocationManager locationManager;
    private double latitude = 0;
    private double longitude = 0;
    private String locationName;
    private TextView dateText;
    private TextView timeText;
    private EditText title;
    private EditText body;
    private Button remindBtn;
    private Button takePicture;
    private Button openMaps;
    private Calendar calendar;
    private Date remindDate = null;
    private ProgressBar progressBar;
    private Location currentLocation;
    private File imageFile;

    public final static String TAG = "CreateMindFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setupLocation();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.create_mind_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        SimpleDateFormat date = new SimpleDateFormat("yyyy, dd MMMM");
        calendar = Calendar.getInstance();
        title = (EditText) getActivity().findViewById(R.id.title_edit_text);
        body = (EditText) getActivity().findViewById(R.id.body_edit_text);
        dateText = (TextView) getActivity().findViewById(R.id.date_text_view);
        timeText = (TextView) getActivity().findViewById(R.id.time_text_view);
        remindBtn = (Button) getActivity().findViewById(R.id.remind);
        takePicture = (Button) getActivity().findViewById(R.id.takePicture);
        openMaps = (Button) getActivity().findViewById(R.id.openInMaps);
        progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        remindBtn.setOnClickListener(this); // setup listener from interface
        takePicture.setOnClickListener(this); // setup listener from interface
        dateText.setText(date.format(new Date()));
        timeText.setText(time.format(new Date()));

        progressBar.setVisibility(View.GONE);
        getActivity().invalidateOptionsMenu();

        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (longitude != 0 && latitude != 0) {
                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    getActivity().startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Can`t open maps. Location is unavailable ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setupLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.getProvider(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
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
        currentLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        progressBar.setVisibility(View.VISIBLE);
        List<Address> addresses = null;
        if (isConnected()) {
            try {
                Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                textView.setText(e.toString());
                e.printStackTrace();
            }
            if (addresses == null || addresses.size() == 0) {
                textView.setText("Addresses are null");
            } else {
                Address address = addresses.get(0);
                progressBar.setVisibility(View.GONE);
                StringBuilder stringBuffer = new StringBuilder();
                stringBuffer.append(address.getCountryCode() + ", ");
                stringBuffer.append(address.getCountryName() + ", ");
                stringBuffer.append(address.getAdminArea() + ", ");
                stringBuffer.append(address.getAddressLine(0) + ", ");
                locationName = stringBuffer.toString().replace(", null", "");
                textView.setText(locationName);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            textView.setText("Connection is not available");
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Location temporarily unavailable");
    }

    @Override
    public void onProviderEnabled(String s) {
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        progressBar.setVisibility(View.VISIBLE);
        textView.setText("Searching for location...");
    }

    @Override
    public void onProviderDisabled(String s) {
        TextView textView = (TextView) getActivity().findViewById(R.id.location_text);
        progressBar.setVisibility(View.GONE);
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
            case R.id.takePicture:
                takePicture();
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
        tpd.setStartTime(now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE));
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
        dpd.setMinDate(now);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.create_mind_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.okay_button:
                onSaveMind();
                break;
            case R.id.delete_button_create_view:
                onDeleteMind();
                break;
        }
        return false;
    }

    public void onSaveMind() {
        if (title.getText().length() < 1 || body.getText().length() < 1) {
            Toast.makeText(getActivity(), "The contents of fields required!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Mind mind = new Mind(title.getText().toString(), body.getText().toString());
                mind.setCreatedDate(new Date());
                if (latitude != 0) {
                    mind.setLatitude(this.latitude);
                    mind.setLongtitude(this.longitude);
                    mind.setLocationName(locationName);
                }
                mind.setLastModifedTime(new Date());
                if (remindDate != null) {
                    remindForMind(mind);
                }
                mind.save();
                getActivity().getFragmentManager().popBackStack();
                getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new ListFragment()).commit();
            } catch (Exception c) {
                Log.e("DB", c.toString());
            }
        }
    }

    public void remindForMind (Mind mind) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy dd MMMM HH:mm");
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmTime.class); // AlarmTime.class required for receiver
        intent.putExtra("title", mind.getTitle());
        intent.putExtra("body", mind.getBody());
        intent.putExtra("created_time", mind.getCreatedDate().getTime());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0,
                intent, PendingIntent.FLAG_ONE_SHOT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, remindDate.getTime(), pendingIntent);
    }
    public void onDeleteMind(){
        // TODO Delete mind
    }

    public void takePicture () {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyddMMMMHHmmss.SSSSSS", Locale.US);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File out = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                simpleDateFormat.format(new Date())+".jpg");
        Uri uri = Uri.fromFile(out);
        /*intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);*/
        /*imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                simpleDateFormat.format(new Date())+".jpg");
        Uri uri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);*/
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.snap);
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.blueLineard);
        if (requestCode == 0) {
            //Uri selectedImage = data.getData();
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            //MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, "", "");
            //linearLayout.setBackground(bitmap);
            imageView.setImageBitmap(bitmap);
        }
        /*if (requestCode == 0) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (imageFile.exists()) {
                        Toast.makeText(getActivity(), "File successfully saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Error during saving file "+imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        *//*BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                        imageView.setImageBitmap(bitmap);*//*

                    }
                break;
                case Activity.RESULT_CANCELED:
                    Toast.makeText(getActivity(), "Result Canceled", Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            Toast.makeText(getActivity(), "Result code not 0", Toast.LENGTH_LONG).show();
        }*/
    }
}
