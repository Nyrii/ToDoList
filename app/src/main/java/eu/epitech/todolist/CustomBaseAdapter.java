package eu.epitech.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by noboud_n on 11/01/2017.
 */
public class CustomBaseAdapter extends BaseAdapter {
    private static ArrayList<Task> searchArrayList = TaskSaving.getToDoTasks();

    private LayoutInflater mInflater;

    public CustomBaseAdapter(Context context, ArrayList<Task> results) {
        searchArrayList = results;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Object getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_row_view, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.txtDescription = (TextView) convertView.findViewById(R.id.description);
            holder.txtDueDate = (TextView) convertView.findViewById(R.id.dueDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(searchArrayList.get(position).getTitle());
        holder.txtDescription.setText(searchArrayList.get(position).getDesc());
        try {
            SimpleDateFormat df = new SimpleDateFormat("dd EE MMMM yyyy");
            String date = df.format(searchArrayList.get(position).getDueDate());
            df = new SimpleDateFormat("hh:mm");
            String time = df.format(searchArrayList.get(position).getDueDate());
            holder.txtDueDate.setText("Due date : " + date + " at " + time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtTitle;
        TextView txtDescription;
        TextView txtDueDate;
    }
}
