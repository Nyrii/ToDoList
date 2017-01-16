package eu.epitech.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by noboud_n on 15/01/2017.
 */
public class FragmentPage extends Fragment {

    public FragmentPage() {}

    private static final String TODOLIST = "TaskManager_Noboud-Inpeng";
    private static final String TAG = "FragmentPage";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_view, container, false);

        if (TaskSaving.getTasksByCategory() != null) {
            final ListView lv = (ListView) view;
            lv.setAdapter(new CustomBaseAdapter(this.getContext(), TaskSaving.getTasksByCategory()));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object object = lv.getItemAtPosition(position);
                    Task task = (Task) object;
                    displayTaskOptions(task);
                }
            });
        }

        return view;
    }

    public void displayTaskOptions(final Task task) {
        CharSequence colors[] = new CharSequence[] {"See details", "Done", "Remove", "Change category"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose an option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewPagerAdapter vpa = MainActivity.getViewPagerAdapter();
                switch (which) {
                    case 0: // See details
                        // new Page
                        break;
                    case 1: // Done
                        TaskSaving.changeCategory(task, "DONE");
                        updateSharedPreferences();
                        // Update View
                        if (vpa != null) {
                            vpa.notifyDataSetChanged();
                        }
                        // refresh list please !
                        break;
                    case 2: // Remove
                        TaskSaving.removeTask(task);
                        updateSharedPreferences();
                        // Update View
                        if (vpa != null) {
                            vpa.notifyDataSetChanged();
                        }
                        break;
                    case 3: // Change category
                        changeCategory(task);
                        updateSharedPreferences();
                        // Update View
                        if (vpa != null) {
                            vpa.notifyDataSetChanged();
                        }
                        // refresh list please !
                        break;
                }
            }
        });

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.show();
    }

    private void changeCategory(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select a category");

        ArrayList<String> categories = TaskSaving.getCategories();
        final String[] arrayCategories = categories.toArray(new String[categories.size()]);

        builder.setItems(arrayCategories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setCategory(arrayCategories[which]);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void updateSharedPreferences() {
        SharedPreferences sharedPreferences;
        Gson gson = new Gson();
        sharedPreferences = this.getActivity().getSharedPreferences(TODOLIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonFinal = gson.toJson(TaskSaving.getTasks());
        editor.putString("Tasks", jsonFinal);
        jsonFinal = gson.toJson(TaskSaving.getCategories());
        editor.putString("Categories", jsonFinal);
        editor.apply();
        Log.d(TAG, sharedPreferences.getAll().toString());
    }

}
