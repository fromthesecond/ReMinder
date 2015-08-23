package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.yakovchuk.reminder.R;

public class CreateMindFragment extends Fragment {

    public final static String TAG = "CreateMindFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.create_mind_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().invalidateOptionsMenu();
    }
}
