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
    private ArrayList<Task> _toDoTasks = null;
    private ArrayList<Task> _doneTasks = null;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences(TODOLIST, Context.MODE_PRIVATE);
        String jsonToDoTasks = sharedPreferences.getString("Todo", "");

        Gson gson = new Gson();
        Task[] tasks = gson.fromJson(jsonToDoTasks, Task[].class);
        if ((_toDoTasks = TaskSaving.getToDoTasks()) == null) {
            if (tasks != null && tasks.length > 0) {
                _toDoTasks = new ArrayList<>();
                for (Task task : tasks) {
                    _toDoTasks.add(task);
                }
            }
            TaskSaving.setTasks(_toDoTasks); // TMP
        }

        String jsonDoneTasks = sharedPreferences.getString("Done", "");
        tasks = gson.fromJson(jsonDoneTasks, Task[].class);

        if ((_doneTasks = TaskSaving.getToDoTasks()) == null) {
            _doneTasks = new ArrayList<>();
            if (tasks != null) {
                for (Task task : tasks) {
                    _doneTasks.add(task);
                }
            }
        }

        if (_toDoTasks != null && _toDoTasks.isEmpty()) {
            Collections.sort(_toDoTasks);
            Collections.reverse(_toDoTasks);
        }
        if (_doneTasks != null && _doneTasks.isEmpty()) {
            Collections.sort(_doneTasks);
            Collections.reverse(_doneTasks);
        }

//        if (_toDoTasks != null && !_toDoTasks.isEmpty()) {
//            final ListView lv = (ListView) findViewById(R.id.ListViewTasks);
//            lv.setAdapter(new CustomBaseAdapter(this, _toDoTasks));
//            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                    Object o = lv.getItemAtPosition(position);
//                    Task fullObject = (Task) o;
//                    Toast.makeText(getBaseContext(), "You have chosen: " + fullObject.getTitle(), Toast.LENGTH_LONG).show();
//                }
//            });
//        }


        /*
        Assigning view variables to their respective view in xml
        by findViewByID method
         */

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        /*
        Creating Adapter and setting that adapter to the viewPager
        setSupportActionBar method takes the toolbar and sets it as
        the default action bar thus making the toolbar work like a normal
        action bar.
         */
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        setSupportActionBar(toolbar);

        /*
        TabLayout.newTab() method creates a tab view, Now a Tab view is not the view
        which is below the tabs, its the tab itself.
         */

        final TabLayout.Tab home = tabLayout.newTab();
        final TabLayout.Tab inbox = tabLayout.newTab();
        final TabLayout.Tab star = tabLayout.newTab();

        /*
        Setting Title text for our tabs respectively
         */

        home.setText("Home");
        inbox.setText("Inbox");
        star.setText("Star");

        /*
        Adding the tab view to our tablayout at appropriate positions
        As I want home at first position I am passing home and 0 as argument to
        the tablayout and like wise for other tabs as well
         */
        tabLayout.addTab(home, 0);
        tabLayout.addTab(inbox, 1);
        tabLayout.addTab(star, 2);

        /*
        TabTextColor sets the color for the title of the tabs, passing a ColorStateList here makes
        tab change colors in different situations such as selected, active, inactive etc

        TabIndicatorColor sets the color for the indiactor below the tabs
         */

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

        /*
        Adding a onPageChangeListener to the viewPager
        1st we add the PageChangeListener and pass a TabLayoutPageChangeListener so that Tabs Selection
        changes when a viewpager page changes.
         */

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    // Notify which kind of tasks will be called (save it in TaskSaving)

//                    switch (position){
//                        case 0:
//                            break;
//                        case 1:
//                            break;
//                        case 2:
//                            home.setIcon(R.drawable.ic_cancel);
//                            inbox.setIcon(R.drawable.ic_cancel);
//                            star.setIcon(R.drawable.ic_cancel);
//                            break;
//                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
        });


//        initInstancesDrawer();
    }

//    private void initInstancesDrawer() {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab One"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab Two"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab Three"));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab){
//                int position = tab.getPosition();
//            }
//        });
//    }


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
