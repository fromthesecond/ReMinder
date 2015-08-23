package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ua.com.yakovchuk.reminder.R;

public class ViewMindFragment extends Fragment {
    public final static String TAG = "ViewMindFragment";
    private TextView textView;
    public String respondMessage;
    private Toolbar toolbar;

    public String getRespondMessage() {
        return respondMessage;
    }

    public void setRespondMessage(String respondMessage) {
        this.respondMessage = respondMessage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //toolbar = (Toolbar) getActivity().findViewById(R.id.collapse_toolbar);
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
        //((MainActivity)getActivity()).setSupportActionBar(toolbar);
    }

}
