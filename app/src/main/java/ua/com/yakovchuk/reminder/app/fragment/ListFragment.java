package ua.com.yakovchuk.reminder.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.activity.MainActivity;
import ua.com.yakovchuk.reminder.app.lib.FloatingActionButton;

public class ListFragment extends Fragment {

    public final static String TAG = "ListFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setupFloatingButton();
        return inflater.inflate(R.layout.list_view, null);
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
