package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.lib.CustomAdapter;

public class ListFragment extends Fragment {

    public final static String TAG = "ListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //setupFloatingButton();
        getActivity().invalidateOptionsMenu();
        setupListView();
    }

    public void setupListView () {
        List<String> names = new ArrayList<String>();
        List<String> description = new ArrayList<String>();
        names.add("Adam Sendler");
        description.add("He says");
        names.add("Mario Gomez");
        description.add("Scored a few goals");
        names.add("Monica Beluchi");
        description.add("Had sex a few days ago");
        ListAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), names, description);
        ListView listView = (ListView) getView().findViewById(R.id.listView_container);
        listView.setAdapter(adapter);
    }
}
