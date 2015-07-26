package ua.com.yakovchuk.reminder.app.lib;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ua.com.yakovchuk.reminder.R;


public class CustomAdapter extends ArrayAdapter<String> {

    private List <String> names;
    private List <String> description;

    public CustomAdapter (Context context, List<String> headers, List<String> description) {
        super(context, R.layout.list_view_fragment, headers);
        this.names = new ArrayList<String>();
        this.description = new ArrayList<String>();
        this.names = headers;
        this.description = description;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_view, parent, false);
        TextView view = (TextView) customView.findViewById(R.id.header);
        TextView view2 = (TextView) customView.findViewById(R.id.description);
        ImageView imageView = (ImageView) customView.findViewById(R.id.imageView);
        view.setText(names.get(position));
        view2.setText(description.get(position));
        imageView.setImageResource(R.drawable.circle);
        return customView;
    }
}
