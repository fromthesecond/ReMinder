package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.lib.CustomAdapter;
import ua.com.yakovchuk.reminder.app.lib.FloatingActionButton;

public class ListFragment extends Fragment {

    public final static String TAG = "ListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.list_view_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        setupFloatingButton();
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

    public void setupFloatingButton() {
        FloatingActionButton fabButton = new FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.ic_add_white_24dp))
                .withButtonColor(getResources().getColor(R.color.MaterialPink500))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 16, 16)
                .create();
        fabButton.showFloatingActionButton();
    }
}
