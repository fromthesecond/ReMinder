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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.entity.Mind;

public class ViewMindFragment extends Fragment {
    public final static String TAG = "ViewMindFragment";
    private TextView title;
    private TextView location;
    private TextView body;
    private TextView createdDate;
    private TextView lastModifedDate;
    public String respondMessage;
    public Mind respondMind;

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.view_mind_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
