package eu.epitech.todolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TODOLIST = "TDL_List";
    private static final String TAG = "MainActivity";
    private ArrayList<Task> _toDoTasks = null;
    private ArrayList<Task> _doneTasks = null;

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

        if (!_toDoTasks.isEmpty()) {
            final ListView lv = (ListView) findViewById(R.id.ListViewTasks);
            lv.setAdapter(new CustomBaseAdapter(this, _toDoTasks));
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Object o = lv.getItemAtPosition(position);
                    Task fullObject = (Task) o;
                    Toast.makeText(getBaseContext(), "You have chosen: " + fullObject.getTitle(), Toast.LENGTH_LONG).show();
                }
            });
        }
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
