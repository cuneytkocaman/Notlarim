package com.cuneyt.notlarim.entities;

public class NoteModel {
    private String id;
    private String noteTitle;
    private String note;
    private String date;
    private String notification;

    public NoteModel() {
    }

    public NoteModel(String id, String noteTitle, String note, String date, String notification) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.note = note;
        this.date = date;
        this.notification = notification;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }
}
