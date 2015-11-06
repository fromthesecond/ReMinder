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
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.entity.Mind;

public class ViewMindFragment extends Fragment  {
    public final static String TAG = "ViewMindFragment";
    private TextView title;
    private TextView location;
    private TextView body;
    private TextView createdDate;
    private TextView lastModifedDate;
    public String respondMessage;
    public Mind respondMind;
    private Mind deletedMind;
    private Button delete;

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
        SimpleDateFormat date = new SimpleDateFormat("yyyy, dd MMMM HH:mm");
        title = (TextView) getActivity().findViewById(R.id.text_view_mind);
        location = (TextView) getActivity().findViewById(R.id.position);
        body = (TextView) getActivity().findViewById(R.id.note_message);
        createdDate = (TextView) getActivity().findViewById(R.id.createdDate);
        lastModifedDate = (TextView) getActivity().findViewById(R.id.last_modifed);

        title.setText(getRespondMind().getTitle());
        location.setText(getRespondMind().getLocationName());
        body.setText(getRespondMind().getBody());
        createdDate.setText(date.format(getRespondMind().getCreatedDate()));
        lastModifedDate.setText(date.format(getRespondMind().getLastModifedTime()));
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
}
