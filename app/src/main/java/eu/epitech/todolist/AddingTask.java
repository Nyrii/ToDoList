package eu.epitech.todolist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import eu.epitech.todolist.time.DatePickerFragment;
import eu.epitech.todolist.time.TimePickerFragment;


/**
 * Created by noboud_n on 10/01/2017.
 */
public class AddingTask extends AppCompatActivity {

    private static final String TODOLIST = "TDL_List";
    private static final String FIELD_ERROR = "Please, fill at least the title field and inquire the due date and time.";
    private static final String PARSE_DATE = "An error occured when managing the date, please try again later.";
    private static final String WRONG_DATE = "Please, pick a date posterior than the current one.";
    private static final String NOTIF_MESSAGE = "An event you planned to do has almost reached its deadline !";

    private EditText    _titleEditText;
    private EditText    _descriptionEditText;
    private TextView    _dateTextView;
    private TextView    _timeTextView;

    private static final String TAG = "AddingTask";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_process);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adding_process_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancelAdding:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void retrieveFieldValues() {
        _titleEditText = (EditText)findViewById(R.id.title);
        _descriptionEditText = (EditText)findViewById(R.id.description);
        _dateTextView = (TextView)findViewById(R.id.dueDateLabel);
        _timeTextView = (TextView)findViewById(R.id.dueTimeLabel);
    }

    public boolean addNewTask(View view) {
        String      title;
        String      description;

        retrieveFieldValues();
        if (_titleEditText != null && !_titleEditText.getText().toString().isEmpty()
            && _dateTextView != null) {
            title = _titleEditText.getText().toString();
            if (_descriptionEditText != null && !_descriptionEditText.getText().toString().isEmpty()) {
                description = _descriptionEditText.getText().toString();
            } else {
                description = "";
            }

            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            try {
                Date dueDate = dateFormatter.parse(_dateTextView.getText().toString() + " " + _timeTextView.getText().toString());
                Date currentDate = dateFormatter.parse(dateFormatter.format(new Date()));
                if (currentDate.after(dueDate)) {
                    return errorOccured(WRONG_DATE);
                }
                scheduleTask(title, description, dueDate);

            } catch (ParseException e) {
                return errorOccured(PARSE_DATE);
            }
            finish();
            return true;
        }
        return errorOccured(FIELD_ERROR);
    }

    public void scheduleTask(String title, String description, Date dueDate) {
        int notificationId = (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
        scheduleNotification(dueDate, "Reminder for : \"title\"", NOTIF_MESSAGE, notificationId, MainActivity.class);

        Task task = new Task(title, description, dueDate, notificationId, Task.Status.TODO);
        TaskSaving.addNewTask(task);
        updateSharedPreferences();
    }

    public void showCalendar(View view) {
        DialogFragment datePicker = new DatePickerFragment();
        datePicker.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePicker(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "timePicker");
    }

    public boolean errorOccured(String error) {
        AlertDialog alertDialog = new AlertDialog.Builder(AddingTask.this).create();
        alertDialog.setTitle("An error occured");
        alertDialog.setMessage(error);
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

    public void scheduleNotification(Date notificationDate, String title, String content, int notificationId, Class activityClass) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(notificationDate);
        cal.set(Calendar.HOUR_OF_DAY, cal.HOUR_OF_DAY - 1);
        Intent intent = new Intent(this, activityClass);
        PendingIntent activity = PendingIntent.getActivity(this, notificationId, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_alarm)
                .setContentTitle(title)
                .setContentText(content)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(activity);

        Notification notification = mBuilder.build();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, notificationId);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }
}
