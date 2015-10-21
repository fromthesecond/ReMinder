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
import java.util.List;
import java.util.Locale;

import ua.com.yakovchuk.reminder.R;

public class ViewMindFragment extends Fragment {
    public final static String TAG = "ViewMindFragment";
    private TextView textView;
    public String respondMessage;

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
        textView = (TextView) getActivity().findViewById(R.id.text_view_mind);
        textView.setText(getRespondMessage());
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
