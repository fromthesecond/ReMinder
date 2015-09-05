package ua.com.yakovchuk.reminder.app.fragment;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.GregorianCalendar;

import ua.com.yakovchuk.reminder.R;
import ua.com.yakovchuk.reminder.app.lib.AlarmTime;

public class MainFragment extends Fragment {

    public final static String TAG = "MainFragment";
    private Button go;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        go = (Button) getActivity().findViewById(R.id.checit);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(getView(), "Notification created", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                Long alertTime = new GregorianCalendar().getTimeInMillis() + 6 * 10000;

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getActivity(), AlarmTime.class); // AlarmTime.class required for receiver

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0,
                        intent, PendingIntent.FLAG_ONE_SHOT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, pendingIntent);
            }
        });
        getActivity().invalidateOptionsMenu();
    }
}
