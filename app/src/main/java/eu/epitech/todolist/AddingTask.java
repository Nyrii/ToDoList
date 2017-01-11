package eu.epitech.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eu.epitech.todolist.time.DatePickerFragment;
import eu.epitech.todolist.time.TimePickerFragment;


/**
 * Created by noboud_n on 10/01/2017.
 */
public class AddingTask extends AppCompatActivity {

    private static final String TODOLIST = "TDL_List";

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
                // Add shared preferences
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean addNewTask(View view) {
        EditText    titleEditText;
        EditText    descriptionEditText;
        TextView    dateTextView;
        TextView    timeTextView;
        String      title;
        String      description;

        titleEditText = (EditText)findViewById(R.id.title);
        descriptionEditText = (EditText)findViewById(R.id.description);
        dateTextView = (TextView)findViewById(R.id.dueDateLabel);
        timeTextView = (TextView)findViewById(R.id.dueTimeLabel);
        if (titleEditText != null && !titleEditText.getText().toString().isEmpty()
            && dateTextView != null) {
            title = titleEditText.getText().toString();
            if (descriptionEditText != null && !descriptionEditText.getText().toString().isEmpty()) {
                description = descriptionEditText.getText().toString();
            } else {
                description = "";
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                Date date = df.parse(dateTextView.getText().toString() + " " + timeTextView.getText().toString());
                Task task = new Task(title, description, date, Task.Status.TODO);
                TaskSaving.addNewTask(task);
                updateSharedPreferences();
            } catch (ParseException e) {
                return fieldError();
            }
            finish();
            return true;
        }
        return fieldError();
    }

    public void showCalendar(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePicker(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    public boolean fieldError() {
        AlertDialog alertDialog = new AlertDialog.Builder(AddingTask.this).create();
        alertDialog.setTitle("An error occured");
        alertDialog.setMessage("Please, fill at least the title field and inquire the due date and time.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        return false;
    }

    public void updateSharedPreferences() {
        SharedPreferences sharedPreferences;
        Gson gson = new Gson();

        String jsonFinal = gson.toJson(TaskSaving.getTasks());
        sharedPreferences = getSharedPreferences(TODOLIST, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Tasks", jsonFinal);
        jsonFinal = gson.toJson(TaskSaving.getToDoTasks());
        editor.putString("Todo", jsonFinal);
        jsonFinal = gson.toJson(TaskSaving.getDoneTasks());
        editor.putString("Done", jsonFinal);
        editor.apply();
        // Log
        System.out.println(sharedPreferences.getAll());
    }
}
