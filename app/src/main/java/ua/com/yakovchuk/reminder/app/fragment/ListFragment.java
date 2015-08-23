package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.interfaces.Message;
import ua.com.yakovchuk.reminder.app.lib.CustomAdapter;

public class ListFragment extends Fragment {

    public final static String TAG = "ListFragment";
    private List<String> names;
    private List<String> description;
    private ListView listView;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Message message;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_view_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().invalidateOptionsMenu();
        setupListView();
        ListView listView = (ListView) getActivity().findViewById(R.id.listView_container);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                message.respond(String.valueOf("Message #") + i);
            }
        });
    }

    public void setupListView() {
        names = new ArrayList<String>();
        description = new ArrayList<String>();
        names.add("Adam Sendler");
        description.add("He says");
        names.add("Mario Gomez");
        description.add("Scored a few goals");
        names.add("Monica Beluchi");
        description.add("Had sex a few days ago");
        ListAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), names, description);
        listView = (ListView) getView().findViewById(R.id.listView_container);
        listView.setAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        message = (Message) activity;
    }
}
