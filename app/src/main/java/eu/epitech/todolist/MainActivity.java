package eu.epitech.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TODOLIST = "TaskManager_Noboud-Inpeng";
    private static final String TAG = "MainActivity";
    private ArrayList<Task> _tasks = null;
    private ArrayList<TabLayout.Tab> tabs = new ArrayList<>();
    private ArrayList<String> categories = null;
    private static ViewPagerAdapter tmpVPA = null;
    TabLayout tabLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        retrieveTasks();
        retrieveCategories();
        createMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(getBaseContext(), AddingTask.class);
                startActivityForResult(intent, 1);
                return true;

            case R.id.action_add_category:
                generateCategoryAlarm();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // When an intent created from MainActivity is done
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (tmpVPA != null) {
                    // Update the fragment
                    tmpVPA.notifyDataSetChanged();
                }
            }

//            if (resultCode == Activity.RESULT_CANCELED) {
//                // No result
//            }
        }
    }

    public void saveViewPagerAdapter(ViewPagerAdapter viewPagerAdapter) {
        tmpVPA = viewPagerAdapter;
    }

    public void createMenu() {
        Log.d(TAG, "Create menu");
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        Toolbar toolbar;

        if (tabLayout != null) {
            tabLayout.removeAllTabs();
        }
        // As viewPagerAdapter can only be initialized here, we save it and attribute it to tmpVPA so it can be used later.
        saveViewPagerAdapter(viewPagerAdapter);

        categories = TaskSaving.getCategories();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager.setAdapter(viewPagerAdapter);
        setSupportActionBar(toolbar);

        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                final TabLayout.Tab tmp = tabLayout.newTab();
                tmp.setText(categories.get(i));
                tabs.add(tmp);
                tabLayout.addTab(tmp, i);
            }
        }

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(this, R.color.tab_selector));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.indicator));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition()); // Notify viewPager.onPageSelected
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // Notify which category of tasks will be called
                categories = TaskSaving.getCategories();
                if (categories != null && categories.size() > position) {
                    String category = categories.get(position);
                    TaskSaving.setCurrentCategory(category);
                    viewPagerAdapter.notifyDataSetChanged(); // Calls override function ViewPagerAdapter.getItemPosition. As getItemPosition will return none, the fragment will be regenerated and will update its data.
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void retrieveTasks() {
        Log.d(TAG, "Retrieve tasks in shared preferences");
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(TODOLIST, Context.MODE_PRIVATE);
        String jsonToDoTasks = sharedPreferences.getString("Tasks", "");

        Gson gson = new Gson();
        Task[] tasks = gson.fromJson(jsonToDoTasks, Task[].class);
        if ((_tasks = TaskSaving.getTasks()) == null) {
            if (tasks != null && tasks.length > 0) {
                _tasks = new ArrayList<>();
                for (Task task : tasks) {
                    _tasks.add(task);
                }
            }
            TaskSaving.setTasks(_tasks);
        }

        if (_tasks != null && _tasks.isEmpty()) {
            Collections.sort(_tasks);
            Collections.reverse(_tasks);
        }
    }

    public void retrieveCategories() {
        Log.d(TAG, "Retrieve tasks in shared preferences");
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(TODOLIST, Context.MODE_PRIVATE);
        String jsonCategories = sharedPreferences.getString("Categories", "");

        Gson gson = new Gson();
        String[] prefCategories = gson.fromJson(jsonCategories, String[].class);
        if (prefCategories == null) {
            categories = TaskSaving.getCategories();
        } else if (prefCategories != null && prefCategories.length > 0) {
                categories = new ArrayList<>();
                for (String category : prefCategories) {
                    categories.add(category);
                }
            TaskSaving.setCategories(categories);
        }
    }

    public void generateCategoryAlarm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add a category");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String category = input.getText().toString();
                if (TaskSaving.doesCategoryExist(category)) {
                    Toast.makeText(getApplicationContext(), "The category already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    TaskSaving.addNewCategory(category);
                    updateSharedPreferences();
                    tmpVPA.notifyDataSetChanged();
                    createMenu();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateSharedPreferences() {
        SharedPreferences sharedPreferences;
        Gson gson = new Gson();
        sharedPreferences = getSharedPreferences(TODOLIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonFinal = gson.toJson(TaskSaving.getTasks());
        editor.putString("Tasks", jsonFinal);
        jsonFinal = gson.toJson(TaskSaving.getCategories());
        editor.putString("Categories", jsonFinal);
        editor.apply();
        Log.d(TAG, sharedPreferences.getAll().toString());
    }

    public static ViewPagerAdapter getViewPagerAdapter() {
        return tmpVPA;
    }
}
