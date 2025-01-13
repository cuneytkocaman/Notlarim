package com.cuneyt.notlarim.entities;

public class TodoModel {
    private String id;
    private String todoTitle;
    private String todo;
    private String date;
    private String status;

    public TodoModel() {
    }

    public TodoModel(String id, String todo, String date, String status) {
        this.id = id;
        this.todo = todo;
        this.date = date;
        this.status = status;
    }

    public TodoModel(String id, String todoTitle, String todo, String date, String status) {
        this.id = id;
        this.todoTitle = todoTitle;
        this.todo = todo;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTodoTitle() {
        return todoTitle;
    }

    public void setTodoTitle(String todoTitle) {
        this.todoTitle = todoTitle;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
