package eu.epitech.todolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by noboud_n on 15/01/2017.
 */
public class FragmentPage extends Fragment {

    public FragmentPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_view, container, false);

        if (TaskSaving.getTasks() != null) {
            final ListView lv = (ListView) view;
            lv.setAdapter(new CustomBaseAdapter(this.getContext(), TaskSaving.getTasks()));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = lv.getItemAtPosition(position);
                    Task fullObject = (Task) o;
                    Toast.makeText(view.getContext(), "You have chosen: " + fullObject.getTitle(), Toast.LENGTH_LONG).show();
                }
            });
        }

        Bundle arguments = getArguments();
        int position = arguments.getInt("position");

        return view;
    }
}
