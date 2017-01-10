package eu.epitech.todolist;

import java.util.Date;

/**
 * Created by noboud_n on 10/01/2017.
 */
public class Task {
    private enum    Status {TODO, DONE};
    private String  title;
    private String  desc;
    private Date    dueDate;
    private Status  status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Task(String title, String desc, Date dueDate, Status status) {
        this.title = title;
        this.desc = desc;
        this.dueDate = dueDate;
        this.status = status;
    }

}
