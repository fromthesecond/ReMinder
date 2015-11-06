package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.entity.Mind;
import ua.com.yakovchuk.reminder.app.interfaces.Message;
import ua.com.yakovchuk.reminder.app.interfaces.MindMessage;
import ua.com.yakovchuk.reminder.app.lib.CustomAdapter;

public class ListFragment extends Fragment {

    public final static String TAG = "ListFragment";
    private List<String> names;
    private List<String> description;
    private ListView listView;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private Message message;
    private MindMessage mindMessage;
    public  Mind deletedMind;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.list_view_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().invalidateOptionsMenu();
        setupListView();
    }

    public void setupListView() {
        final List<Mind> minds = new Select().from(Mind.class).execute();
        SimpleDateFormat date = new SimpleDateFormat("yyyy, dd MMMM");
        names = new ArrayList<String>();
        description = new ArrayList<String>();
        for (Mind m: minds) {
            names.add(m.getTitle());
            description.add(m.getBody());
        }

        ListAdapter adapter = new CustomAdapter(getActivity().getApplicationContext(), names, description);
        listView = (ListView) getView().findViewById(R.id.listView_container);
        listView.setAdapter(adapter);
        listView.setLongClickable(true);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Mind mind = Mind.load(Mind.class, minds.get(i).getId());
                deletedMind = mind;
                mind.delete();
                Snackbar.make(getView(), "Mind " + minds.get(i).getTitle() + " has been deleted!", Snackbar.LENGTH_LONG)
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
                                setupListView();
                            }
                        }).show();
                setupListView();
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<Mind> minds = new Select().from(Mind.class).execute();
                //message.respondObj(minds.get(i));
                mindMessage.respondMind(minds.get(i));
                message.respond(String.valueOf("Message #") + minds.get(i).getTitle());
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        message = (Message) activity;
        mindMessage = (MindMessage) activity;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume() {
        setupListView();
        super.onResume();
    }
}
