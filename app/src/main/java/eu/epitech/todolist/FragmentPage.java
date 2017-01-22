package eu.epitech.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.Iterator;

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
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
                    Object object = lv.getItemAtPosition(position);
                    Task task = (Task) object;
                    displayTaskOptions(task);
                    return true;
                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object object = lv.getItemAtPosition(position);
                    Task task = (Task) object;
                    Intent intent = new Intent(getContext(), TaskDetails.class);
                    intent.putExtra("Task", (new Gson()).toJson(task));
                    startActivity(intent);
                }
            });
        }

        return view;
    }

    public void displayTaskOptions(final Task task) {
        CharSequence colors[] = new CharSequence[] {"Done", "Remove", "Change category"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select an option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ViewPagerAdapter vpa = MainActivity.getViewPagerAdapter();
                switch (which) {
                    case 0: // Done
                        TaskSaving.changeCategory(task, "DONE");
                        updateSharedPreferences();
                        // Update View
                        if (vpa != null) {
                            vpa.notifyDataSetChanged();
                        }
                        break;
                    case 1: // Remove
                        TaskSaving.removeTask(task);
                        updateSharedPreferences();
                        // Update View
                        if (vpa != null) {
                            vpa.notifyDataSetChanged();
                        }
                        break;
                    case 2: // Change category
                        changeCategory(task);
//                        updateSharedPreferences();
                        // View is updated in changeCategory as AlertBuilder is asynchronous
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

    public void changeCategory(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select a category");

        ArrayList<String> categories = new ArrayList<>(TaskSaving.getCategories());


        Iterator<String> tmp = categories.iterator();
        while (tmp.hasNext()) {
            String s = tmp.next();
            if (s.equals(task.getCategory())) {
                tmp.remove();
                break;
            }
        }

        final String[] arrayCategories = categories.toArray(new String[categories.size()]);

        builder.setItems(arrayCategories, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                task.setCategory(arrayCategories[which]);
                updateSharedPreferences();
                // Update View
                if (MainActivity.getViewPagerAdapter() != null) {
                    MainActivity.getViewPagerAdapter().notifyDataSetChanged();
                }
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
