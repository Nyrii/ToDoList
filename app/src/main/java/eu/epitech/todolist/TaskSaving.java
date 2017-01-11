package eu.epitech.todolist;

import java.util.ArrayList;

/**
 * Created by noboud_n on 11/01/2017
 */
public class TaskSaving {
    private static ArrayList<Task> toDoTasks = null;
    private static ArrayList<Task> doneTasks = null;
    private static ArrayList<Task> tasks = null;


    public static ArrayList<Task> getToDoTasks() {
        return toDoTasks;
    }

    public static void setToDoTasks(ArrayList<Task> toDoTasks) {
        TaskSaving.toDoTasks = toDoTasks;
    }

    public static ArrayList<Task> getDoneTasks() {
        return doneTasks;
    }

    public static void setDoneTasks(ArrayList<Task> doneTasks) {
        TaskSaving.doneTasks = doneTasks;
    }

    public static ArrayList<Task> getTasks() {
        return tasks;
    }

    public static void setTasks(ArrayList<Task> tasks) {
        TaskSaving.tasks = tasks;
    }

    public static void addNewTask(Task task) {
        if (toDoTasks == null) {
            toDoTasks = new ArrayList<Task>();
        }
        if (tasks == null) {
            tasks = new ArrayList<Task>();
        }
        toDoTasks.add(task);
        tasks.add(task);
    }

    public static void toDoFromDone(Task task) {
        for (Task tmp : toDoTasks) {
            if (task.equals(tmp)) {
                // Remove in the toDoTasks list + add in the doneTasks list + update in sharedPreferences
                toDoTasks.remove(task);
                if (doneTasks == null) {
                    doneTasks = new ArrayList<Task>();
                }
                doneTasks.add(task);

                // Recreate a new list with all the tasks for later
                tasks = new ArrayList<Task>();
                if (toDoTasks != null && !toDoTasks.isEmpty()) {
                    tasks.addAll(toDoTasks);
                }
                if (doneTasks != null && !doneTasks.isEmpty()) {
                    tasks.addAll(doneTasks);
                }
                // update in sharedPreferences autre part, faire une string avec tous les array et envoyer ca dans les sharedpreferences depuis une activite
            }
        }
    }

    public static void removeTask(Task task) {
        if (task.getStatus() == Task.Status.TODO) {
            toDoTasks.remove(task);
        } else if (task.getStatus() == Task.Status.DONE) {
            doneTasks.remove(task);
        }
    }

}
