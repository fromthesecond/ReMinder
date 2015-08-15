package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
}
