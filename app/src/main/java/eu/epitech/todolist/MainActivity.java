package eu.epitech.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TODOLIST = "TDL_List";
    private static final String TAG = "MainActivity";
    private ArrayList<Task> _tasks = null;
    ArrayList<TabLayout.Tab> tabs = new ArrayList<>();
    ArrayList<String> categories = null;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            TaskSaving.setTasks(_tasks); // TMP
        }

        if (_tasks != null && _tasks.isEmpty()) {
            Collections.sort(_tasks);
            Collections.reverse(_tasks);
        }

        categories = TaskSaving.getCategories();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                Intent intent = new Intent(this, AddingTask.class);
                startActivity(intent);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
