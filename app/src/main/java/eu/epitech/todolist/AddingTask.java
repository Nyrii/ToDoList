package eu.epitech.todolist;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by noboud_n on 10/01/2017.
 */
public class AddingTask extends AppCompatActivity {

    private static final String TAG = "AddingTask";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_process);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addTask:
                Log.d(TAG, "Add a new task DUDE");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addNewTask(View view) {
        System.err.println("TG");
    }

    public void showCalendar(View view) {
        DialogFragment picker = new DatePickerFragment();
        picker.show(getSupportFragmentManager(), "datePicker");
    }
}
