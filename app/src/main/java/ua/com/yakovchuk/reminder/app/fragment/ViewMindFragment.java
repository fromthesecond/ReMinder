package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.activity.MainActivity;
import ua.com.yakovchuk.reminder.app.entity.Mind;

public class ViewMindFragment extends Fragment implements View.OnClickListener {
    public final static String TAG = "ViewMindFragment";
    private TextView title;
    private TextView location;
    private TextView body;
    private TextView createdDate;
    private TextView lastModifedDate;
    private TextView openOnMapView;
    public String respondMessage;
    public Mind respondMind;
    private Mind deletedMind;
    private Button delete;
    private AlertDialog.Builder alertDialog;
    private LinearLayout hiddenTitleLayout;
    private EditText bodyEditText;
    private SimpleDateFormat date;

    public Mind getRespondMind() {
        return respondMind;
    }

    public void setRespondMind(Mind respondMind) {
        this.respondMind = respondMind;
    }

    public String getRespondMessage() {
        return respondMessage;
    }

    public void setRespondMessage(String respondMessage) {
        this.respondMessage = respondMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //toolbar = (Toolbar) getActivity().findViewById(R.id.collapse_toolbar);
        setHasOptionsMenu(true);
        delete = (Button) getActivity().findViewById(R.id.delete_button);
        //delete.setOnClickListener(this);
        return inflater.inflate(R.layout.view_mind_fragment, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        date = new SimpleDateFormat("yyyy, dd MMMM HH:mm");
        title = (TextView) getActivity().findViewById(R.id.text_view_mind);
        location = (TextView) getActivity().findViewById(R.id.position);
        body = (TextView) getActivity().findViewById(R.id.note_message);
        createdDate = (TextView) getActivity().findViewById(R.id.createdDate);
        lastModifedDate = (TextView) getActivity().findViewById(R.id.last_modifed);
        hiddenTitleLayout = (LinearLayout) getActivity().findViewById(R.id.editTitleView);
        bodyEditText = (EditText) getActivity().findViewById(R.id.bodyEditHidden);
        body.setOnClickListener(this);
        hiddenTitleLayout.setOnClickListener(this);
        title.setOnClickListener(this);

        title.setText(getRespondMind().getTitle());
        location.setText(getRespondMind().getLocationName());
        body.setText(getRespondMind().getBody());
        createdDate.setText(date.format(getRespondMind().getCreatedDate()));
        lastModifedDate.setText(date.format(getRespondMind().getLastModifedTime()));
        openOnMapView = (TextView) getActivity().findViewById(R.id.position);
        if (getRespondMind().getLatitude() != 0 && getRespondMind().getLocationName() == null) {
            location.setText("Click to open location on map");
        }
        openOnMapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (openOnMapView.length()!=0)
                new AlertDialog.Builder(getActivity())
                        .setTitle(null)
                        .setMessage("Are you sure to open location on map?")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               openOnMap();
                            }
                        })
                        .setNegativeButton("CANCEL", null)
                        .create().show();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().invalidateOptionsMenu();
    }

    public void onDeleteMind () {
        Mind mind = Mind.load(Mind.class, respondMind.getId());
        deletedMind = mind;
        mind.delete();
        Snackbar.make(getView(), "Mind " + mind.getTitle() + " has been deleted!", Snackbar.LENGTH_LONG)
                .setAction("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Mind mind1 = new Mind(deletedMind.getTitle(), deletedMind.getBody());
                        mind1.setLastModifedTime(deletedMind.getLastModifedTime());
                        mind1.setLocationName(deletedMind.getLocationName());
                        mind1.setLongtitude(deletedMind.getLongtitude());
                        mind1.setLatitude(deletedMind.getLatitude());
                        mind1.setCreatedDate(deletedMind.getCreatedDate());
                        mind1.save();
                        getActivity().getFragmentManager().popBackStack();
                        getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new ListFragment()).commit();
                    }
                }).show();
        getActivity().getFragmentManager().popBackStack();
        getActivity().getFragmentManager().beginTransaction().replace(R.id.container, new ListFragment()).commit();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.view_mind_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_button:
                onDeleteMind();
                break;
        }
        return false;
    }

    public void openOnMap () {
        if (getRespondMind().getLongtitude() != 0 && getRespondMind().getLatitude() != 0) {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", getRespondMind().getLatitude(),
                    getRespondMind().getLongtitude());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getActivity().startActivity(intent);
        } else {
            Toast.makeText(getActivity(), "Can`t open maps. Location is unavailable ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editTitleView:
                Toast.makeText(getActivity(), "Edit text", Toast.LENGTH_LONG).show();
                break;
            case R.id.text_view_mind:
                onChangeTitle();
                break;
            case R.id.note_message:
                Toast.makeText(getActivity(), "Body Text", Toast.LENGTH_LONG).show();
                onChangeBody();
                break;
        }
    }

    public void onChangeTitle() {
        title.setVisibility(View.GONE);
        final EditText editText = (EditText) getActivity().findViewById(R.id.editTitleText);
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.setEditedTitle);
        editText.setText(title.getText());
        hiddenTitleLayout.setVisibility(View.VISIBLE);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mind mind = Mind.load(Mind.class, respondMind.getId());
                hiddenTitleLayout.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                title.setText(editText.getText().toString());
                lastModifedDate.setText(date.format(new Date()));
                mind.setTitle(editText.getText().toString());
                mind.setLastModifedTime(new Date());
                mind.save();
            }
        });
    }

    public void onChangeBody() {
        ImageView imageView = (ImageView) getActivity().findViewById(R.id.saveBodyImageView);
        imageView.setVisibility(View.VISIBLE);
        body.setVisibility(View.GONE);
        bodyEditText.setVisibility(View.VISIBLE);
        bodyEditText.setText(body.getText().toString());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mind mind = Mind.load(Mind.class, respondMind.getId());
                mind.setBody(bodyEditText.getText().toString());
                mind.setLastModifedTime(new Date());
                mind.save();
                lastModifedDate.setText(date.format(new Date()));
                body.setVisibility(View.VISIBLE);
                body.setText(bodyEditText.getText().toString());
                bodyEditText.setVisibility(View.GONE);
            }
        });
    }
}
